package com.example.miulostandfound;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
public class Main2Activity extends AppCompatActivity {

    private static final int Pick_Photo = 1;
    DatabaseReference databaseReference;
    Uri image;
    StorageReference storageReference;
    TextView mTextViewShowUploads;
    EditText ImageName ,ImageCaption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Image");
        storageReference = FirebaseStorage.getInstance().getReference().child("Image_File");
        ImageName = (EditText)findViewById(R.id.imgName);
        ImageCaption = (EditText)findViewById(R.id.imgCaption);
        mTextViewShowUploads = findViewById(R.id.text_view_show_uploads);
        mTextViewShowUploads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagesActivity();
            }
        });

    }

    private void openImagesActivity() {
        Intent intent = new Intent(this, imagesActivity.class);
        startActivity(intent);
    }

    public void UploadImage(View view) {


        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, Pick_Photo);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Pick_Photo && resultCode == RESULT_OK) {

            final Uri uri = data.getData();
            //this is for image file name
            final StorageReference filepath = storageReference.child("Photos").child(String.valueOf(System.currentTimeMillis()));
            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    final String TempImageName = ImageName.getText().toString().trim();
                    final String TempImageCaption = ImageCaption.getText().toString().trim();

                    filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
//                            URL[0] =String.valueOf(uri);
                            imageData imageData= new imageData(TempImageName,String.valueOf(uri),TempImageCaption);
                            String ImageUploadId = databaseReference.push().getKey();

                            databaseReference.child(ImageUploadId).setValue(imageData);

                            Toast.makeText(Main2Activity.this, "Done"+uri, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {

                    // Hiding the progressDialog.
                    // Showing exception erro message.
                    Toast.makeText(Main2Activity.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }

}