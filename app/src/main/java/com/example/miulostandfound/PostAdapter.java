package com.example.miulostandfound;

import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.style.UpdateLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ImageViewHolder> {
    ArrayList<String> keys;
    private Context mContext;
    private List<imageData> mUploads;
    boolean del=false;
    boolean flag = false;
    static PostAdapter instance;
    MainActivity m=MainActivity.instance;
    StorageReference storageReference;
    public interface OnItemLongClickListener {
        public boolean onItemLongClicked(int position);
    }

    public PostAdapter( List<imageData> uploads,ArrayList<String> k) {

        this.mUploads = uploads;
        instance=this;
        keys=k;
    }
    public PostAdapter(){}

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        view = inflater.inflate(R.layout.image_item2,parent,false);
        return new ImageViewHolder(view);
    }
    @Override
    public void onBindViewHolder(ImageViewHolder holder, final int position) {
        imageData uploadCurrent = mUploads.get(position);
        String img = String.valueOf(uploadCurrent.getImageURL());
        holder.textViewCaption.setText(uploadCurrent.getImageCaption());
        holder.textViewFoundat.setText("Found At: "+uploadCurrent.getFoundAt());
        holder.textViewPostedby.setText("Posted By: "+uploadCurrent.getUser());
            holder.textViewName.setText(uploadCurrent.getImageName());
            Picasso.get().load(uploadCurrent.getImageURL()).into(holder.imageView);
            System.out.println(img);
            Log.i("Tag",img.toString());


        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m.openDialog(position);
//                deleteItem(position);
            }
        });


    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    public boolean deleteItem(int position){
        String key = keys.get(position);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Image");
        ref.child(key).removeValue();
        return true;
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder{
        TextView textViewName;
        ImageView imageView;
        Button deleteButton;
        TextView textViewCaption;
        TextView textViewFoundat;
        TextView textViewPostedby;
        ImageViewHolder(View itemView){
            super(itemView);
            textViewName = itemView.findViewById(R.id.text_view_name);
            textViewCaption = itemView.findViewById(R.id.textView7);
            textViewFoundat = itemView.findViewById(R.id.textView8);
            textViewPostedby = itemView.findViewById(R.id.User_email);
            imageView = itemView.findViewById(R.id.image_view_upload);
            deleteButton=itemView.findViewById(R.id.btnDel);
        }
    }



    public void setDel(boolean del) {
        this.del = del;
    }
}
