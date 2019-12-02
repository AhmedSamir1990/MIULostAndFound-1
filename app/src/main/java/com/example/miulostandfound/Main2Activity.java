package com.example.miulostandfound;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class Main2Activity extends AppCompatActivity {
    private static final int Pick_Photo = 1;
    private static final int Camera =2;

    DatabaseReference databaseReference;
    Uri image;
    StorageReference storageReference;
    TextView mTextViewShowUploads;
    EditText ImageName ,ImageCaption,FoundAt;
    Button btn;
    static final int REQUEST_IMAGE_CAPTURE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Image");
        storageReference = FirebaseStorage.getInstance().getReference().child("Image_File");
        ImageName = (EditText)findViewById(R.id.imgName);
        ImageCaption = (EditText)findViewById(R.id.imgCaption);
        FoundAt = (EditText)findViewById(R.id.FoundAt);

        mTextViewShowUploads = findViewById(R.id.text_view_show_uploads);
        btn = findViewById(R.id.button2);
        mTextViewShowUploads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagesActivity();
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPictureDialog();
            }
        });


    }

    private void showPictureDialog() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera" };
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallary();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, Camera);
    }

    private void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, Pick_Photo);
    }


    private void openImagesActivity() {
        Intent intent = new Intent(this,imagesActivity.class);
        startActivity(intent);
        finish();
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
                    final String TempFoundAt = FoundAt.getText().toString().trim();
                    ///////////////////////////////////
                    final String user="hot hna l username aw email";
                    ////////////////////////////////////////
                    filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
//                            URL[0] =String.valueOf(uri);

                            // imageData imageData= new  imageData(TempImageName, String.valueOf(uri), TempImageCaption,  false, TempFoundAt,user);
                            imageData imageData = new imageData(TempImageName,String.valueOf(uri),TempImageCaption,false,TempFoundAt,user);

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
        else if (requestCode == 2) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
//            imageview.setImageBitmap(thumbnail);
            saveImage(thumbnail);
            Toast.makeText(Main2Activity.this, "Image Saved!", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + "IMAGE_DIRECTORY");
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }

        try {
            File f = new File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(this,
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath());

            f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
//        return "";
    }
}

