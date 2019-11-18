package com.example.bitmlabexam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignInActivity extends AppCompatActivity {

    private EditText emailET,passwordET;
    private Button loginBtn;
    private TextView signUpTV;
    private String email,password;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);


        init();


        signUpTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SignInActivity.this,SignUpActivity.class);
                startActivity(intent);
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email= emailET.getText().toString();
                password= passwordET.getText().toString();

                if (email.matches("")){
                    Toast.makeText(SignInActivity.this, "Enter Email Address", Toast.LENGTH_SHORT).show();
                    return;
                } else if (password.matches("")){
                    Toast.makeText(SignInActivity.this, "Enter Password", Toast.LENGTH_SHORT).show();
                    return;
                }else{

                    progressDialog.setTitle("Please Wait...");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();
                    signUP(email,password);
                }
            }
        });
    }

    private void signUP(String email, String password) {


        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                progressDialog.dismiss();

                if (task.isSuccessful()){
                    Intent intent= new Intent(SignInActivity.this,MainActivity.class);
                    startActivity(intent);

                }else {
                    Toast.makeText(SignInActivity.this, "Sorry ! Try Again", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void init() {

        emailET= findViewById(R.id.emailId);
        passwordET= findViewById(R.id.passwordId);
        loginBtn= findViewById(R.id.loginButtonId);

        signUpTV= findViewById(R.id.signUpTextId);

        firebaseAuth= FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
    }
}
