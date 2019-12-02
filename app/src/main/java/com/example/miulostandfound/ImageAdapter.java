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
        if (uploadCurrent.getIsFound()==flag)
        {
            holder.textViewName.setText(uploadCurrent.getImageName());
//        hna kn fe with(mContext) bs at3'yrt '
//        Picasso.get().load(uploadCurrent.getImageURL()).fit().centerCrop().into(holder.imageView);
            Picasso.get().load(uploadCurrent.getImageURL()).into(holder.imageView);
            System.out.println(img);
            Log.i("Tag",img.toString());
//        storageReference.child(img.child)
        }
        else
//            holder.textViewName.setText("NO data to show ");
            System.out.println("No data to show");
//        Log.i("Tag",img.toString());
//    Toast.makeText(ImageAdapter.this,"No items to show",Toast.LENGTH_LONG).show();
//        Toast.makeText(ImageAdapter.this, "Done", Toast.LENGTH_SHORT).show();


    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }









    public class ImageViewHolder extends RecyclerView.ViewHolder{
        public TextView textViewName;
        public ImageView imageView;
        public ImageViewHolder(View itemView){
            super(itemView);
            textViewName = itemView.findViewById(R.id.text_view_name);
            imageView = itemView.findViewById(R.id.image_view_upload);


        }




    }
}
