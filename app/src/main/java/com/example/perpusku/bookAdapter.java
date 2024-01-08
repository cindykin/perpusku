package com.example.perpusku;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class bookAdapter extends RecyclerView.Adapter<recycleBook> {

    private Context context;
    private ArrayList id_buku, judul, author, sinopsis;

    private ArrayList<String> imgPaths;

    public bookAdapter(Context context, ArrayList<String> id_buku, ArrayList<String> judul, ArrayList<String> author, ArrayList<String> imgPaths, ArrayList<String> sinopsis) {
        this.context = context;
        this.id_buku = id_buku;
        this.judul = judul;
        this.author = author;
        this.imgPaths = imgPaths;
        this.sinopsis = sinopsis;
    }

    @NonNull
    @Override
    public recycleBook onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_book,parent,false);
        return new recycleBook(view);
    }

    @Override
    public void onBindViewHolder(@NonNull recycleBook holder, int position) {
        holder.id_book.setText(String.valueOf(id_buku.get(position)));
        holder.title_book.setText(String.valueOf(judul.get(position)));
        holder.author.setText(String.valueOf(author.get(position)));

        // Load image from the provided path
        Bitmap bitmap = BitmapFactory.decodeFile(imgPaths.get(position));
        holder.imgBook.setImageBitmap(bitmap);

        final String bookId = String.valueOf(id_buku.get(position));
        final String bookTitle = String.valueOf(judul.get(position));
        final String bookAuthor = String.valueOf(author.get(position));
        final String imagePath = imgPaths.get(position);
        Object synopsisObj = sinopsis.get(position);
        final String synopsisValue = (synopsisObj != null) ? synopsisObj.toString() : "";

        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, view_book.class);
                intent.putExtra("id_book", bookId);
                intent.putExtra("title", bookTitle);
                intent.putExtra("author", bookAuthor);
                intent.putExtra("imgPath", imagePath);
                intent.putExtra("sinopsis", synopsisValue);
                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return judul.size();
    }

}
