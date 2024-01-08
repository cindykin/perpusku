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

public class userAdapter extends RecyclerView.Adapter<recycleUser> {

    private Context context;
    private ArrayList id_user, username, email, password;

    private ArrayList<String> imgPaths;

    public userAdapter(Context context, ArrayList id_user, ArrayList username, ArrayList email, ArrayList password, ArrayList<String> imgPaths) {
        this.context = context;
        this.id_user = id_user;
        this.username = username;
        this.email = email;
        this.password = password;
        this.imgPaths = imgPaths;
    }

    @NonNull
    @Override
    public recycleUser onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_user,parent,false);
        return new recycleUser(view);
    }

    @Override
    public void onBindViewHolder(@NonNull recycleUser holder, int position) {
        holder.id_user.setText(String.valueOf(id_user.get(position)));
        holder.username.setText(String.valueOf(username.get(position)));
        holder.email.setText(String.valueOf(email.get(position)));


        // Load image from the provided path
        String currentImagePath = imgPaths.get(position);
        Bitmap bitmap = getBitmapFromPath(currentImagePath);
        if (bitmap != null) {
            holder.imgUser.setImageBitmap(bitmap);
        } else {
            holder.imgUser.setImageResource(R.drawable.default_book_image);
        }

        final String userId = String.valueOf(id_user.get(position));
        final String usernames = String.valueOf(username.get(position));
        final String emails = String.valueOf(email.get(position));
        final String imagePath = currentImagePath;

        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, view_member.class);
                intent.putExtra("userId", userId);
                intent.putExtra("username", usernames);
                intent.putExtra("email", emails);
                intent.putExtra("imgPath", imagePath);

                context.startActivity(intent);
            }
        });
    }


    private Bitmap getBitmapFromPath(String path) {
        try {
            if (path != null && !path.isEmpty()) {
                return BitmapFactory.decodeFile(path);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public int getItemCount() {
        return username.size();
    }

}
