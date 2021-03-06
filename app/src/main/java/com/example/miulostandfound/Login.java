package com.example.miulostandfound;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static java.lang.Thread.sleep;

public class Login extends AppCompatActivity {
    int RC_SIGN_IN = 1;
    int backButtonCount=0;
    String TAG = "Google sign in";
    FirebaseAuth mAuth;
    GoogleSignInClient mGoogleSignInClient;
    SignInButton googlesigninbtn;
    Button signOutbtn;
    Button button3;
    DatabaseReference reff;
    TextView textView3;
    TextView textView4;
    com.example.miulostandfound.student student;
    Intent intent;
private final String CHANNEL_ID="personal_notifications";
    long maxid = 0;

    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        googlesigninbtn = findViewById(R.id.signInButton);
        signOutbtn = findViewById(R.id.sign_out);
        textView3 = findViewById(R.id.textView3);
        button3 = findViewById(R.id.button3);
        textView4 = findViewById(R.id.textView4);
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

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mAuth = FirebaseAuth.getInstance();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        googlesigninbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });


        signOutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
                googlesigninbtn.setVisibility(View.VISIBLE);
                signOutbtn.setVisibility(View.GONE);
                textView4.setText("Please Wait A Moment");
            }
        });

        signOut();
        signOutbtn.setVisibility(View.GONE);

    }
    @Override
    public void onBackPressed()
    {
        if(backButtonCount >= 1)
        {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        else
        {
            Toast.makeText(this, "Press the back button once again to close the application.", Toast.LENGTH_SHORT).show();
            backButtonCount++;
        }
    }
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }
    public void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                    }
                });
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        googlesigninbtn.setVisibility(View.GONE);
        try {
            sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        textView3.setText("Loading");
        textView4.setText("Please Wait A Moment");
//        signOutbtn.setVisibility(View.VISIBLE);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);

            }
        }
    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                           updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
//                            Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }
    public void updateUI(FirebaseUser user) {
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        if (acct != null) {
            final String personName = acct.getDisplayName(); //full name
            final String personGivenName = acct.getGivenName();//first name
            final String personFamilyName = acct.getFamilyName(); //second name
            final String personEmail = acct.getEmail();
            final String personId = acct.getId();

            DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
            rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if (snapshot.child("student").hasChild(personId)) {
                        goFeed(personEmail);
                    }
                    else {
                         goSignup(personGivenName,personFamilyName,personEmail,personId);
               }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {}
            });

        }
    }
    public void goSignup(String personGivenName, String personFamilyName, String personEmail, String personId)
            {

                    intent = new Intent(this,Phone.class);
                        intent.putExtra("personGivenName",personGivenName);
                        intent.putExtra("personFamilyName",personFamilyName);
                        intent.putExtra("personEmail",personEmail);
                        intent.putExtra("personId",personId);
                    startActivity(intent);
                    finish();
            }
    private void signUp(String personGivenName, String personFamilyName, String personEmail, String personId) {
        student.setFName(personGivenName.toString().trim());
        student.setLName(personFamilyName.toString().trim());
        student.setEmail(personEmail.toString().trim());
        student.setId(personId.trim());
//        student.setNumber(personPhone.trim());
         reff.child(String.valueOf(personId)).setValue(student);
    }
    public void goFeed(String personEmail)
    {
        notifyMe();
        Intent intent = new Intent(this,MainActivity.class);
        intent.putExtra("personEmail",personEmail);
        startActivity(intent);
        finish();
    }
public void notifyMe()
{
    create();
    NotificationCompat.Builder builder = new NotificationCompat.Builder(Login.this,CHANNEL_ID);
    builder .setSmallIcon(R.drawable.miulogo);
    builder .setContentTitle("Welcome To MIU Lost&Found");
    builder.setContentText("Let's add an Image!");
    builder .setAutoCancel(true);
    builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
    NotificationManagerCompat n = NotificationManagerCompat.from(this);
    n.notify(001,builder.build());
}
    private void create()
{
    if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
    {
        CharSequence name= "Personal Notifications";
        String descreption = "Include all the personal notifications";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;

        NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID,name,importance);
        notificationChannel.setDescription(descreption);
        NotificationManager no = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        no.createNotificationChannel(notificationChannel);
    }
}

}