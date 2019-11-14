package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mikhaellopez.circularimageview.CircularImageView;


import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.UUID;

import gun0912.tedimagepicker.builder.TedImagePicker;
import gun0912.tedimagepicker.builder.listener.OnSelectedListener;
import mehdi.sakout.fancybuttons.FancyButton;

public class Signup_activity extends AppCompatActivity {
    EditText email, password;
    FancyButton signup,login;
    FirebaseAuth firebaseAuth;
    ProgressBar pg;
    LinearLayout img;
    CircularImageView usr_img;

    FirebaseStorage storage;
    Uri profile_image;
    StorageReference storageReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_activity);
        setup();
        load_from_instanceState(savedInstanceState);
        onClickListeners();

    }
    public void setup()
    {
        login = findViewById(R.id.btn_login);
        pg=findViewById(R.id.pg_bar);
        img=findViewById(R.id.upload_image);
        usr_img=findViewById(R.id.usr_image);
        firebaseAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        signup=findViewById(R.id.btn_signup);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
    }
    
    public void load_from_instanceState(Bundle savedInstanceState)
    {
        if (savedInstanceState != null) {
            String email_text = savedInstanceState.getString("email");
            String pass_text = savedInstanceState.getString("password");
            email.setText(email_text);
            password.setText(pass_text);

        }
    }
    
    public void onClickListeners()
    {
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Signup_activity.this, Login_activity.class));
                Signup_activity.this.finish();
            }
        });

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                img_picker();
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                signup_validation();
            }
        });
    }
    
    public void img_picker()
    {
        TedImagePicker.with(this).start(new OnSelectedListener() {
                    @Override
                    public void onSelected(@NonNull Uri uri) {
                        usr_img.setImageURI(null);
                        usr_img.setImageURI(uri);
                        profile_image=uri;
                    }
                });

    }
    public void signup_validation()
    {
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
                String editText_eml = email.getText().toString();
                String editText_passwd = password.getText().toString();

                if (editText_eml.isEmpty()) {
                    email.setError("Provide your Email first!");
                    email.requestFocus();
                }
                else if (editText_passwd.isEmpty()) {
                    password.setError("Set your password");
                    password.requestFocus();
                }
                else if (editText_eml.isEmpty()
                        && editText_passwd.isEmpty()) {
                    Toast.makeText(Signup_activity.this, "Fields Empty!", Toast.LENGTH_SHORT).show();
                }
                else if (!(editText_eml.isEmpty()
                        && editText_passwd.isEmpty())) {
                    if(profile_image!=null)
                    {
                        upload_image(editText_eml,editText_passwd);
                    }
                    else
                    {
                        signup_method(editText_eml,editText_passwd);
                    }

                } else {
                    Toast.makeText(Signup_activity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    
    public void upload_image(final String editText_eml, final String editText_passwd)
    {

            final ProgressDialog progressDialog =
                    new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref =
                    storageReference
                            .child("profile_images/"+ editText_eml);

            ref.putFile(profile_image)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(Signup_activity.this, "Image Uploaded", Toast.LENGTH_SHORT).show();
                            signup_method(editText_eml,editText_passwd);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(Signup_activity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });

    }
    public void signup_method(String editText_eml,String editText_passwd)
    {
        email.setEnabled(false);
        password.setEnabled(false);
        login.setEnabled(false);
        signup.setEnabled(false);
        img.setEnabled(false);
        pg.setVisibility(View.VISIBLE);
        firebaseAuth
                .createUserWithEmailAndPassword(editText_eml, editText_passwd)
                .addOnCompleteListener(Signup_activity.this, new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {

                if (!task.isSuccessful()) {

                    Toast.makeText(Signup_activity.this.getApplicationContext(),
                            "SignUp unsuccessful: " + task.getException().getMessage(),
                            Toast.LENGTH_SHORT).show();

                    email.setEnabled(true);
                    password.setEnabled(true);
                    login.setEnabled(true);
                    signup.setEnabled(true);
                    img.setEnabled(true);
                    pg.setVisibility(View.GONE);

                } else {

                    startActivity(new Intent(Signup_activity.this, MainActivity.class));
                    pg.setVisibility(View.GONE);
                    Signup_activity.this.finish();
                }
            }
        });
    }

    @Override
    protected void onSaveInstanceState(final Bundle outState) {

        super.onSaveInstanceState(outState);

        outState.putString("email",email.getText().toString());
        outState.putString("password",password.getText().toString());

    }
}
