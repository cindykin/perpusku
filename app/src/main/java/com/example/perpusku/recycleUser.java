package com.example.perpusku;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class recycleUser extends RecyclerView.ViewHolder {
    ImageView imgUser;
    TextView id_user,username, email;
    LinearLayout mainLayout;
    public recycleUser(@NonNull View itemView) {
        super(itemView);
        id_user = itemView.findViewById(R.id.id_user);
        username = itemView.findViewById(R.id.usernameText);
        email = itemView.findViewById(R.id.emailText);
        imgUser = itemView.findViewById(R.id.image_user);
        mainLayout = itemView.findViewById(R.id.memberListLayout);
    }
}
