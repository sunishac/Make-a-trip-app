package com.example.chala.group12_hw09_part_a;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    RelativeLayout rl;
    Button lgn,sgn;
    /*FirebaseDatabase database;
    DatabaseReference ref;*/
    EditText usr,psw;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rl=(RelativeLayout) findViewById(R.id.rl);
        usr=(EditText) findViewById(R.id.email);
        psw=(EditText) findViewById(R.id.password);
        lgn= (Button) findViewById(R.id.login);
        sgn=(Button) findViewById(R.id.signup_s);
        //database = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() != null){
            //that means user is already logged in
            //so close this activity
            finish();

            //and open profile activity
            Intent intent = new Intent(MainActivity.this, homepage.class);
            intent.putExtra("Email",firebaseAuth.getCurrentUser().getEmail());
            startActivityForResult(intent,200);
        }

        rl.setVisibility(View.INVISIBLE);

        final CharSequence[] items={"Email","Gmail"};
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle(" Choose the mail which you want to use ").setItems(items,new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                if(items[which].equals("Email")){
                    rl.setVisibility(View.VISIBLE);
                }else{
                    Intent i=new Intent(MainActivity.this, googleauth2.class);
                    startActivity(i);
                }
            } });
        final AlertDialog simpleItemAlert= builder.create();
        simpleItemAlert.setCancelable(false);
        simpleItemAlert.show();

        sgn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MainActivity.this,Signup.class);
                startActivityForResult(i,100);
            }
        });

        lgn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String email = usr.getText().toString();
                final String password = psw.getText().toString();

                if(email.equals(null)||email.equals("")||email.isEmpty()){
                    Toast.makeText(MainActivity.this,"email field is empty", Toast.LENGTH_LONG).show();
                } else if(password.equals(null)||password.equals("")){
                    Toast.makeText(MainActivity.this,"password field is empty", Toast.LENGTH_LONG).show();
                }else{
                    firebaseAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    // If sign in fails, display a message to the user. If sign in succeeds
                                    // the auth state listener will be notified and logic to handle the
                                    // signed in user can be handled in the listener.
                                    if (!task.isSuccessful()) {
                                        // there was an error
                                        Toast.makeText(MainActivity.this,"failed login" +task.getException(), Toast.LENGTH_LONG).show();
                                    } else {
                                        Intent intent = new Intent(MainActivity.this, homepage.class);
                                        intent.putExtra("Email",email);
                                        startActivityForResult(intent,200);
                                    }
                                }
                            });

                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            if(requestCode==100){
                rl.setVisibility(View.VISIBLE);
            }
            else if(requestCode ==200){
                rl.setVisibility(View.VISIBLE);
            }

        }
    }
}
