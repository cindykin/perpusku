package com.example.perpusku;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "library.db";

    // Table names
    private static final String TABLE_USER = "tb_user";
    private static final String TABLE_BOOK = "tb_book";
    private static final String TABLE_HISTORY = "tb_history";

    // Common column names
    private static final String COLUMN_ID = "id";

    // Columns for tb_user
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_USER_PICTURE = "user_image";

    // Columns for tb_book
    private static final String COLUMN_BOOK_NAME = "book_name";
    private static final String COLUMN_AUTHOR = "author";
    private static final String COLUMN_BOOK_PICTURE = "book_picture";
    private static final String COLUMN_DESCRIPTION = "description";

    // Columns for tb_history
    private static final String COLUMN_BORROW_DATE = "borrow_date";
    private static final String COLUMN_RETURN_DATE = "return_date";
    private static final String COLUMN_BOOK_ID = "book_id";
    private static final String COLUMN_USER_ID = "user_id";

    // Create table queries
    private static final String CREATE_TABLE_USER = "CREATE TABLE " + TABLE_USER + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_USERNAME + " TEXT,"
            + COLUMN_PASSWORD + " TEXT,"
            + COLUMN_EMAIL + " TEXT,"
            + COLUMN_USER_PICTURE + " TEXT"
            + ")";


    private static final String CREATE_TABLE_BOOK = "CREATE TABLE " + TABLE_BOOK + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_BOOK_NAME + " TEXT,"
            + COLUMN_AUTHOR + " TEXT,"
            + COLUMN_BOOK_PICTURE + " TEXT,"
            + COLUMN_DESCRIPTION + " TEXT"
            + ")";

    private static final String CREATE_TABLE_HISTORY = "CREATE TABLE " + TABLE_HISTORY + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_BORROW_DATE + " TEXT,"
            + COLUMN_RETURN_DATE + " TEXT,"
            + COLUMN_BOOK_ID + " INTEGER,"
            + COLUMN_USER_ID + " INTEGER,"
            + " FOREIGN KEY (" + COLUMN_BOOK_ID + ") REFERENCES " + TABLE_BOOK + "(" + COLUMN_ID + "),"
            + " FOREIGN KEY (" + COLUMN_USER_ID + ") REFERENCES " + TABLE_USER + "(" + COLUMN_ID + ")"
            + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USER);
        db.execSQL(CREATE_TABLE_BOOK);
        db.execSQL(CREATE_TABLE_HISTORY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOK);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HISTORY);
        onCreate(db);
    }

    public long addUser( String username, String password, String imagePath, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_USER_PICTURE, imagePath);
        values.put(COLUMN_EMAIL, email);
        return db.insert(TABLE_USER, null, values);
    }

    public boolean updateUser(int userId, String newUsername, String newPassword, String newImagePath, String newEmail) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, newUsername);
        values.put(COLUMN_PASSWORD, newPassword);
        values.put(COLUMN_USER_PICTURE, newImagePath);

        // Use the original email value if the new email value is empty
        values.put(COLUMN_EMAIL, newEmail.isEmpty() ? getEmailById(userId) : newEmail);

        String whereClause = COLUMN_ID + "=?";
        String[] whereArgs = { String.valueOf(userId) };

        int rowsAffected = db.update(TABLE_USER, values, whereClause, whereArgs);
        return rowsAffected > 0; // Return true if update was successful
    }

    private String getEmailById(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = { COLUMN_EMAIL };
        String selection = COLUMN_ID + "=?";
        String[] selectionArgs = { String.valueOf(userId) };
        Cursor cursor = db.query(TABLE_USER, columns, selection, selectionArgs, null, null, null);
        String email = "";

        if (cursor != null && cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(COLUMN_EMAIL);
            if (columnIndex != -1) { // Check if column index exists
                email = cursor.getString(columnIndex);
            }
            cursor.close();
        }
        return email;
    }



    public boolean checkUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = COLUMN_USERNAME + "=? AND " + COLUMN_PASSWORD + "=?";
        String[] selectionArgs = { username, password };
        Cursor cursor = db.query(TABLE_USER, null, selection, selectionArgs, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        return count > 0; // Return true if user exists
    }

    public String getUserProfilePicture(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = { COLUMN_EMAIL };
        String selection = COLUMN_USERNAME + "=? AND " + COLUMN_PASSWORD + "=?";
        String[] selectionArgs = { username, password };
        Cursor cursor = db.query(TABLE_USER, columns, selection, selectionArgs, null, null, null);
        String profilePictureURI = null;

        if (cursor != null && cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(COLUMN_EMAIL);
            if (columnIndex != -1) { // Check if column index exists
                profilePictureURI = cursor.getString(columnIndex);
            }
            cursor.close();
        }
        return profilePictureURI;
    }

    public boolean updateUserProfilePicture(String username, String password, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_EMAIL, email);
        String whereClause = COLUMN_USERNAME + "=? AND " + COLUMN_PASSWORD + "=?";
        String[] whereArgs = { username, password };
        int rowsAffected = db.update(TABLE_USER, values, whereClause, whereArgs);
        return rowsAffected > 0; // Return true if update was successful
    }

    public boolean deleteUser(int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = COLUMN_ID + "=?";
        String[] whereArgs = { String.valueOf(userId) };
        int rowsAffected = db.delete(TABLE_USER, whereClause, whereArgs);
        return rowsAffected > 0; // Return true if deletion was successful
    }

    public long addBook(String title, String author, String imagePath, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_BOOK_NAME, title);
        values.put(COLUMN_AUTHOR, author);
        values.put(COLUMN_BOOK_PICTURE, imagePath);
        values.put(COLUMN_DESCRIPTION, description);
        return db.insert(TABLE_BOOK, null, values);
    }

    // Update image path for a specific book ID
    public boolean updateImagePath(long id, String imagePath) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_BOOK_PICTURE, imagePath);
        int affectedRows = db.update(TABLE_BOOK, values, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        return affectedRows > 0;
    }

    public boolean updateUserImage(long id, String imagePath) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_PICTURE, imagePath);
        int affectedRows = db.update(TABLE_USER, values, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        return affectedRows > 0;
    }


    // Method to retrieve all books from tb_book table
    public Cursor getAllBooks() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_BOOK, null, null, null, null, null, null);
    }

    public Cursor getAllUser() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_USER, null, null, null, null, null, null);
    }
    public boolean updateBook(int bookId, String bookName, String author, String bookPictureURI, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_BOOK_NAME, bookName);
        values.put(COLUMN_AUTHOR, author);
        values.put(COLUMN_BOOK_PICTURE, bookPictureURI);
        values.put(COLUMN_DESCRIPTION, description);
        String whereClause = COLUMN_ID + "=?";
        String[] whereArgs = { String.valueOf(bookId) };
        int rowsAffected = db.update(TABLE_BOOK, values, whereClause, whereArgs);
        return rowsAffected > 0; // Return true if update was successful
    }

    public boolean deleteBook(int bookId) {
        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = COLUMN_ID + "=?";
        String[] whereArgs = { String.valueOf(bookId) };
        int rowsAffected = db.delete(TABLE_BOOK, whereClause, whereArgs);
        return rowsAffected > 0; // Return true if deletion was successful
    }

    public long addHistory(int bookId, int userId, String borrowDate, String returnDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_BOOK_ID, bookId);
        values.put(COLUMN_USER_ID, userId);
        values.put(COLUMN_BORROW_DATE, borrowDate);
        values.put(COLUMN_RETURN_DATE, returnDate);
        return db.insert(TABLE_HISTORY, null, values);
    }

    public Cursor getAllHistoryRecords() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_HISTORY, null, null, null, null, null, null);
    }

    public boolean updateHistoryRecord(int historyId, String borrowDate, String returnDate, int bookId, int userId, String imagePath) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_BOOK_ID, bookId);
        values.put(COLUMN_USER_ID, userId);
        values.put(COLUMN_BORROW_DATE, borrowDate);
        values.put(COLUMN_RETURN_DATE, returnDate);
        // Add your logic to handle imagePath (if needed)

        String whereClause = COLUMN_ID + "=?";
        String[] whereArgs = {String.valueOf(historyId)};
        int rowsAffected = db.update(TABLE_HISTORY, values, whereClause, whereArgs);
        return rowsAffected > 0; // Return true if update was successful
    }


    public boolean deleteHistoryRecord(int historyId) {
        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = COLUMN_ID + "=?";
        String[] whereArgs = { String.valueOf(historyId) };
        int rowsAffected = db.delete(TABLE_HISTORY, whereClause, whereArgs);
        return rowsAffected > 0; // Return true if deletion was successful
    }

    // Method to update book details in tb_book table
//    public int updateBook(int bookId, String bookName, String author, String bookPictureURI, String description) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues values = new ContentValues();
//        values.put(COLUMN_BOOK_NAME, bookName);
//        values.put(COLUMN_AUTHOR, author);
//        values.put(COLUMN_BOOK_PICTURE, bookPictureURI);
//        values.put(COLUMN_DESCRIPTION, description);
//        String whereClause = COLUMN_ID + "=?";
//        String[] whereArgs = { String.valueOf(bookId) };
//        return db.update(TABLE_BOOK, values, whereClause, whereArgs);
//    }
//
//    // Method to delete a book from tb_book table
//    public int deleteBook(int bookId) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        String whereClause = COLUMN_ID + "=?";
//        String[] whereArgs = { String.valueOf(bookId) };
//        return db.delete(TABLE_BOOK, whereClause, whereArgs);
//    }
//
//    //history
//    public long addHistory(int bookId, int userId, String borrowDate, String returnDate) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues values = new ContentValues();
//        values.put(COLUMN_BOOK_ID, bookId);
//        values.put(COLUMN_USER_ID, userId);
//        values.put(COLUMN_BORROW_DATE, borrowDate);
//        values.put(COLUMN_RETURN_DATE, returnDate);
//        return db.insert(TABLE_HISTORY, null, values);
//    }
//
//    public Cursor getAllHistoryRecords() {
//        SQLiteDatabase db = this.getReadableDatabase();
//        return db.query(TABLE_HISTORY, null, null, null, null, null, null);
//    }
//
//    public int updateHistoryRecord(int historyId, int bookId, int userId, String borrowDate, String returnDate) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues values = new ContentValues();
//        values.put(COLUMN_BOOK_ID, bookId);
//        values.put(COLUMN_USER_ID, userId);
//        values.put(COLUMN_BORROW_DATE, borrowDate);
//        values.put(COLUMN_RETURN_DATE, returnDate);
//        String whereClause = COLUMN_ID + "=?";
//        String[] whereArgs = { String.valueOf(historyId) };
//        return db.update(TABLE_HISTORY, values, whereClause, whereArgs);
//    }
//
//    // Method to delete a history record from tb_history table
//    public int deleteHistoryRecord(int historyId) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        String whereClause = COLUMN_ID + "=?";
//        String[] whereArgs = { String.valueOf(historyId) };
//        return db.delete(TABLE_HISTORY, whereClause, whereArgs);
//    }
}

