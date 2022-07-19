package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Map;

public class MainActivity4 extends AppCompatActivity {
    private EditText t1,t2,t3,t4;
    private Button b1;
    private FirebaseAuth auth,auth1;
    private FirebaseDatabase data;
    private ProgressDialog pd;
    private TextView t;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        t1=findViewById(R.id.name);
        t2=findViewById(R.id.email);
        auth=FirebaseAuth.getInstance();
        data=FirebaseDatabase.getInstance();
        t3=findViewById(R.id.pass);
        t4=findViewById(R.id.mob);
        t=findViewById(R.id.a2);
        t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity4.this,MainActivity5.class));
            }
        });
        b1=findViewById(R.id.reg);
        pd=new ProgressDialog(this);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.setMessage("Registering...");
                pd.show();
String email=t2.getText().toString();
String pass=t3.getText().toString();
auth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
    @Override
    public void onComplete(@NonNull Task<AuthResult> task) {
        if(task.isSuccessful())
        {
            pd.setMessage("Registered Saving details...");

            data.getReference("frndy").child("users").child(auth.getCurrentUser().getUid()).child("name").setValue(t1.getText().toString());
            data.getReference("frndy").child("users").child(auth.getCurrentUser().getUid()).child("email").setValue(email);
            data.getReference("frndy").child("users").child(auth.getCurrentUser().getUid()).child("mobile").setValue(t4.getText().toString());

              pd.dismiss();
              startActivity(new Intent(MainActivity4.this,MainActivity3.class));
              finish();
        }
    }
});

            }
        });

        getSupportActionBar().hide();

    }
    @Override
    protected void onStart()
    {
        super.onStart();
        auth1=FirebaseAuth.getInstance();
        if(auth1.getCurrentUser()!=null)
        {
            startActivity(new Intent(MainActivity4.this,MainActivity3.class));
        }
    }

}