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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
    Login log = new Login();

    DatabaseReference databaseReference;
    Uri image;
    StorageReference storageReference;
    TextView mTextViewShowUploads;
    EditText imageName ,imageCaption,FoundAt;
    Button btn;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    String personEmail;
    GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Image");
        storageReference = FirebaseStorage.getInstance().getReference().child("Image_File");
        imageName = (EditText)findViewById(R.id.imgName);
        imageCaption = (EditText)findViewById(R.id.imgCaption);
        FoundAt = (EditText)findViewById(R.id.FoundAt);
        personEmail=getIntent().getStringExtra("personEmail");
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater= getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return true;
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
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
//        finish();
    }


    public void UploadImage(View view) {

        String imageName2 = imageName.getText().toString();
        String imageCaption2 = imageCaption.getText().toString();
        String FoundAt2 = FoundAt.getText().toString();
        if (imageName2.isEmpty()) {
            imageName.setError("Item Name is required");
            imageName.requestFocus();
            return;
        } else if (imageCaption2.isEmpty()) {
            imageCaption.setError("Item Descreption is required");
            imageCaption.requestFocus();
            return;
        } else if (FoundAt2.isEmpty()) {
            FoundAt.setError("Valid room is required");
            FoundAt.requestFocus();
            return;
        } else {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, Pick_Photo);

        }
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
                    final String TempImageName = imageName.getText().toString().trim();
                    final String TempImageCaption = imageCaption.getText().toString().trim();
                    final String TempFoundAt = FoundAt.getText().toString().trim();
                    ///////////////////////////////////
                    final String user=personEmail;
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
            saveImage(thumbnail);
            Toast.makeText(Main2Activity.this, "Image Saved!", Toast.LENGTH_LONG).show();
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.item1:
                signOut();
                return true;
            case R.id.item2:
                Toast.makeText(this,"About",Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void signOut() {

        Intent intent = new Intent(this,Login.class);
        startActivity(intent);
        log.signOut();


    }


}

