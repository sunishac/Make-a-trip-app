package com.example.chala.group12_hw09_part_a;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class googleauth2 extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener, View.OnClickListener
        {
    private GoogleApiClient mGoogleApiClient;
            TextView mStatusTextView;
            FirebaseDatabase database;
            DatabaseReference ref;
            String email=null;
            String word=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_googleauth);

        database=FirebaseDatabase.getInstance();
        ref=database.getReference();

        mStatusTextView  = (TextView) findViewById(R.id.nameLogin);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);

        signInButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("demo", "onConnectionFailed:" + connectionResult);
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent,9001);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("demo","entered on googleAuth");
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == 9001) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }/*else if(requestCode==100)
        {
            String logout = getIntent().getExtras().getString("Logout");
            Log.d("demo","in logout"+logout);
            signOut();
        }*/
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d("demo", "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
           /* mStatusTextView.setText(acct.getDisplayName());
            updateUI(true);*/
            email=acct.getEmail();
            word=email;

            if(word.contains(".")){
                word= word.substring(0, word.indexOf("."));
                System.out.println(word);
            }



            ref.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    int c=0;
                    for(DataSnapshot post: dataSnapshot.getChildren())
                    {
                        String email1= post.getValue(String.class);
                        if(email1.equals(email))
                        {
                            c=1;
                        }
                    }
                    if(c==0)
                    {
                        long l=dataSnapshot.getChildrenCount();
                        ref.child("users").child(String.valueOf(l)).setValue(email);
                        ref.child(word).child("Email").setValue(email);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });


           Intent i = new Intent(googleauth2.this,homepage.class);
            i.putExtra("Email",acct.getEmail());
            i.putExtra("googleSignIn","true");
            startActivityForResult(i,100);
        } else {
            // Signed out, show unauthenticated UI.
            updateUI(false);
        }
    }




    private void updateUI(boolean signedIn) {
        if (signedIn) {
            findViewById(R.id.sign_in_button).setVisibility(View.GONE);
        } /*else {
            mStatusTextView.setText("signed out");
            findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
        }*/
    }

}
