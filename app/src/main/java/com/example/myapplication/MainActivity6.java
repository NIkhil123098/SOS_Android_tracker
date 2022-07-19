package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;

public class MainActivity6 extends AppCompatActivity {
    private EditText t1,t2,t3,t4;
    private Button b1,b2;
    private FirebaseAuth auth;
    private FirebaseDatabase data;
    private DatabaseReference ref;
    private ProgressDialog pd;
    private ImageView imageView;

    // Uri indicates, where the image will be picked from
    private Uri filePath;

    // request code
    private final int PICK_IMAGE_REQUEST = 22;


    private FirebaseStorage storage;
    private StorageReference storageReference,s1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main6);
        t1=findViewById(R.id.name);
        t2=findViewById(R.id.email);
        auth=FirebaseAuth.getInstance();
        data=FirebaseDatabase.getInstance();
        ref=data.getReference("frndy").child("users").child(auth.getCurrentUser().getUid());
        t3=findViewById(R.id.pass);
        t4=findViewById(R.id.mob);
        b1=findViewById(R.id.reg);
        b2=findViewById(R.id.signout);
        imageView = findViewById(R.id.imgView);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        s1=storage.getReference("images").child(auth.getCurrentUser().getUid());
        try {
            final File local= File.createTempFile(auth.getCurrentUser().getUid(),"*");

            s1.getFile(local).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap= BitmapFactory.decodeFile(local.getAbsolutePath());
                    imageView.setImageBitmap(bitmap);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImage();
            }

            private void SelectImage() {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(
                        Intent.createChooser(
                                intent,
                                "Select Image from here..."),
                        PICK_IMAGE_REQUEST);

            }
        });
        pd=new ProgressDialog(this);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                user1 obj=snapshot.getValue(user1.class);

                t1.setText(obj.getName());
                t2.setText(obj.getEmail());
                t4.setText(obj.getMobile());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (filePath != null) {


                    // Defining the child of storageReference

                    StorageReference ref
                            = storageReference
                            .child(
                                    "images/"
                                            + auth.getCurrentUser().getUid());
                    ref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        }
                    });
                }

                    String email=t2.getText().toString();
                String pass=t3.getText().toString();
                pd.setMessage("Updating details...");
                pd.show();
                auth.getCurrentUser().updateEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });
                auth.getCurrentUser().updatePassword(pass).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });


                data.getReference("frndy").child("users").child(auth.getCurrentUser().getUid()).child("name").setValue(t1.getText().toString());
                data.getReference("frndy").child("users").child(auth.getCurrentUser().getUid()).child("email").setValue(email);
                data.getReference("frndy").child("users").child(auth.getCurrentUser().getUid()).child("mobile").setValue(t4.getText().toString());

                pd.dismiss();
                startActivity(new Intent(MainActivity6.this,MainActivity3.class));
                finish();


            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                startActivity(new Intent(MainActivity6.this,MainActivity5.class));
                finish();
            }
        });
getSupportActionBar().hide();
    }
    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data)
    {

        super.onActivityResult(requestCode,
                resultCode,
                data);

        // checking request code and result code
        // if request code is PICK_IMAGE_REQUEST and
        // resultCode is RESULT_OK
        // then set image in the image view
        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {

            // Get the Uri of data
            filePath = data.getData();
            try {

                // Setting image on image view using Bitmap
                Bitmap bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(
                                getContentResolver(),
                                filePath);
                imageView.setImageBitmap(bitmap);
            }

            catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }



}