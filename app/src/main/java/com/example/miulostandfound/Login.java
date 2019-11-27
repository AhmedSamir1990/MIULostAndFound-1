package com.example.miulostandfound;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.firebase.auth.ProviderQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {
    int RC_SIGN_IN = 1;
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

    long maxid = 0;
    GoogleSignInResult result;
    DataSnapshot dataSnapshot;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Configure Google Sign In
        googlesigninbtn = findViewById(R.id.signInButton);
        signOutbtn = findViewById(R.id.sign_out);
        textView3 = findViewById(R.id.textView3);
        button3 = findViewById(R.id.button3);
        textView4 = findViewById(R.id.textView4);
//        textView7 = findViewById(R.id.textView7);

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
        {

        }


        signOutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
                googlesigninbtn.setVisibility(View.VISIBLE);
                signOutbtn.setVisibility(View.GONE);
            }
        });

        signOut();
        signOutbtn.setVisibility(View.GONE);

//        textView7.setVisibility(View.GONE);
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
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

        signOutbtn.setVisibility(View.VISIBLE);
        googlesigninbtn.setVisibility(View.GONE);
//        textView4.setVisibility(View.GONE);

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
                        // go to Feed
                    }
                    else {
                         goSignup(personGivenName,personFamilyName,personEmail,personId);
               }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {}
            });
//            signUp(personGivenName, personFamilyName, personEmail, personId);

        }
    }
            public void goSignup(String personGivenName, String personFamilyName, String personEmail, String personId)
            {
                Toast.makeText(this,"Gwa el intent",Toast.LENGTH_SHORT).show();
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

public void goAdd(View view)
{
    Intent intent = new Intent(this,Main2Activity.class);
    startActivity(intent);
  
}
}