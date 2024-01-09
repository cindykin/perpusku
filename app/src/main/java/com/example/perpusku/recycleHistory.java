package com.example.perpusku;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class recycleHistory extends RecyclerView.ViewHolder {

    TextView id_history, borrowDate, returnDate;
    LinearLayout mainLayout;

    public recycleHistory(@NonNull View itemView) {
        super(itemView);
        id_history = itemView.findViewById(R.id.id_history);
        borrowDate = itemView.findViewById(R.id.borrow_date);
        returnDate = itemView.findViewById(R.id.return_date);
        mainLayout = itemView.findViewById(R.id.historyListLayout);
    }
}
