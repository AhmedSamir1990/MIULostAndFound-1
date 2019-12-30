package com.example.miulostandfound;


import android.content.Context;
import android.nfc.Tag;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.Iterator;
import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    private Context mContext;
    private List<imageData> mUploads;
    boolean flag = false;
    StorageReference storageReference;
//    Iterator<DataSnapshot> items = databasetasna


    public ImageAdapter(Context context, List<imageData> uploads) {
        mContext = context;
        mUploads = uploads;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.image_item, parent, false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        imageData uploadCurrent = mUploads.get(position);
        String img = String.valueOf(uploadCurrent.getImageURL());

            holder.textViewName.setText(uploadCurrent.getImageName());
            holder.textViewCaption.setText(uploadCurrent.getImageCaption());
            holder.textViewFoundat.setText(uploadCurrent.getFoundAt());
            holder.textViewPostedby.setText(uploadCurrent.getUser());
            Picasso.get().load(uploadCurrent.getImageURL()).into(holder.imageView);
            System.out.println(img);
            Log.i("Tag",img.toString());


    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }



    public class ImageViewHolder extends RecyclerView.ViewHolder{
        public TextView textViewName;
        public TextView textViewCaption;
        public TextView textViewFoundat;
        public TextView textViewPostedby;
        public ImageView imageView;
        public ImageViewHolder(View itemView){
            super(itemView);
            textViewName = itemView.findViewById(R.id.text_view_name);
            textViewCaption = itemView.findViewById(R.id.textView7);
            textViewFoundat = itemView.findViewById(R.id.textView8);
            textViewPostedby = itemView.findViewById(R.id.User_email);
            imageView = itemView.findViewById(R.id.image_view_upload);


        }




    }
}
