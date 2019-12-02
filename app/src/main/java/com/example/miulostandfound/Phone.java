package com.example.miulostandfound;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Phone extends AppCompatActivity {


    com.example.miulostandfound.student student;
    DatabaseReference reff;
    EditText txtNumber;
    String personGivenName;
    String personFamilyName;
    String personEmail;
    String personId;
    String personPhone;
    long maxid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);
        txtNumber=findViewById(R.id.txtNumber);
        personGivenName=getIntent().getStringExtra("personGivenName");
        personFamilyName=getIntent().getStringExtra("personFamilyName");
        personEmail=getIntent().getStringExtra("personEmail");
        personId=getIntent().getStringExtra("personId");

        student= new com.example.miulostandfound.student();
        reff = FirebaseDatabase.getInstance().getReference().child("student");
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                    maxid=(dataSnapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

            Toast.makeText(this,"Hello "+personGivenName+personFamilyName,Toast.LENGTH_SHORT).show();


    }

    public void phone(View view)
    {
        personPhone=txtNumber.getText().toString();
        if(personPhone.isEmpty() || personPhone.length()<11)
        {
            txtNumber.setError("Valid Number is required");
            txtNumber.requestFocus();
            return;
        }
         String PhoneNo = "+20"+personPhone;
//        Intent intent= new Intent(this,Verify.class);
//        intent .putExtra("PhoneNo",PhoneNo);
        signUp(view);
    }

    public void signUp(View view) {

        student.setFName(personGivenName.toString().trim());
        student.setLName(personFamilyName.toString().trim());
        student.setEmail(personEmail.toString().trim());
        student.setId(personId.trim());
        student.setNumber("0");
        reff.child(String.valueOf(personId)).setValue(student);
    }


}
