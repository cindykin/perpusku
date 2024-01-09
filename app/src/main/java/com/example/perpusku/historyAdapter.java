package com.example.perpusku;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class historyAdapter extends RecyclerView.Adapter<recycleHistory> {

    private Context context;
    private ArrayList<String> id_history, borrowDate, returnDate, bookId, userId;

    public historyAdapter(Context context, ArrayList<String> id_history, ArrayList<String> borrowDate, ArrayList<String> returnDate, ArrayList<String> bookId, ArrayList<String> userId) {
        this.context = context;
        this.id_history = id_history;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
        this.bookId = bookId;
        this.userId = userId;
    }

    @NonNull
    @Override
    public recycleHistory onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_history, parent, false);
        return new recycleHistory(view);
    }

    @Override
    public void onBindViewHolder(@NonNull recycleHistory holder, int position) {
        holder.id_history.setText(String.valueOf(id_history.get(position)));
        holder.borrowDate.setText(String.valueOf(borrowDate.get(position)));
        holder.returnDate.setText(String.valueOf(returnDate.get(position)));

        final String historyId = String.valueOf(id_history.get(position));
        final String bookIdValue = String.valueOf(bookId.get(position));
        final String userIdValue = String.valueOf(userId.get(position));

        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, view_history.class);
                intent.putExtra("id_history", historyId);
                intent.putExtra("bookId", bookIdValue);
                intent.putExtra("userId", userIdValue);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return id_history.size();
    }
}
