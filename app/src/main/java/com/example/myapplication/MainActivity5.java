package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity5 extends AppCompatActivity {
    private EditText t1,t2;
    private Button b1;
    private FirebaseAuth auth;
    private ProgressDialog pd;
    private TextView t3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);
        t1=findViewById(R.id.email);
        t2=findViewById(R.id.pass);
        t3=findViewById(R.id.a2);
        t3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity5.this,MainActivity4.class));
            }
        });
        auth=FirebaseAuth.getInstance();
        b1=findViewById(R.id.login);
        pd=new ProgressDialog(this);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.setMessage("Logging in , Please Wait..");
                pd.show();
                String email=t1.getText().toString();
                String pass=t2.getText().toString();
                auth.signInWithEmailAndPassword(email,pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        pd.dismiss();
                        startActivity(new Intent(MainActivity5.this,MainActivity3.class));
                        finish();
                    }
                });
            }
        });
        getSupportActionBar().hide();
    }
}