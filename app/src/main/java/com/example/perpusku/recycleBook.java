package com.example.perpusku;

import android.text.Layout;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class recycleBook extends RecyclerView.ViewHolder {

     ImageView imgBook;
     TextView id_book,title_book, author;
     LinearLayout mainLayout;
    public recycleBook(@NonNull View itemView) {
        super(itemView);
        id_book = itemView.findViewById(R.id.id_book);
        imgBook = itemView.findViewById(R.id.image_book);
        title_book = itemView.findViewById(R.id.title_book);
        author = itemView.findViewById(R.id.author_book);
        mainLayout = itemView.findViewById(R.id.bookListLayout);
    }
}
