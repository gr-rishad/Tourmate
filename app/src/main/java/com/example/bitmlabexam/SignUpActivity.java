package com.example.bitmlabexam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity {

    private EditText emailAddressET,passwordET,confirmPasswordET,firstNameET,lastNameET;
    private Button signUpBtn;
    private String email,password,confirmPassword,firstName,lastName;
    private Uri uri;
    private ImageView imageView;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private ProgressDialog progressDialog;
    private SharepreferenceHandlerC handlerC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

      final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

      handlerC= new SharepreferenceHandlerC(SignUpActivity.this);
        init();

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email= emailAddressET.getText().toString();
                password= passwordET.getText().toString();
                confirmPassword= confirmPasswordET.getText().toString();
                firstName= firstNameET.getText().toString();
                lastName= lastNameET.getText().toString();


                System.out.println("password length is"+password.length());
                System.out.println("irm  Conpassword length is"+confirmPassword.length());



                if (email.matches("")){
                    Toast.makeText(SignUpActivity.this, "Enter Email Address", Toast.LENGTH_SHORT).show();
                    return ;
                }else if ( !email.matches(emailPattern)){
                    Toast.makeText(SignUpActivity.this, "Enter Valid Email Address", Toast.LENGTH_SHORT).show();
                    return ;
                }
                else if (password.matches("")){
                    Toast.makeText(SignUpActivity.this, "Enter Your Password", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (password.length() <=5){
                    Toast.makeText(SignUpActivity.this, " Password length less than 6", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (confirmPassword.matches("")){
                    Toast.makeText(SignUpActivity.this, "Enter Confirm Password", Toast.LENGTH_SHORT).show();
                    return;
                }else if(confirmPassword.length() <= 5 ){
                    Toast.makeText(SignUpActivity.this, "Confirm Password less than 6", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (firstName.matches("")){
                    Toast.makeText(SignUpActivity.this, "Enter First Name", Toast.LENGTH_SHORT).show();
                    return;
                }else if (lastName.matches("")) {
                    Toast.makeText(SignUpActivity.this, "Enter Last Name", Toast.LENGTH_SHORT).show();
                    return;
                }

                else{
                    if (password.matches(confirmPassword)){

                             progressDialog.setTitle("Please wait..");
                             progressDialog.setCanceledOnTouchOutside(false);
                             progressDialog.show();

                             signUp(email,password,confirmPassword,firstName,lastName);
                        }else {

                             Toast.makeText(SignUpActivity.this, "pass not matches", Toast.LENGTH_SHORT).show();
                             return;
                         }

                    }

                }
        });


    }

    private void signUp(final String email, final String password, final String confirmPassword, final String firstName, final String lastName) {

        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){

                    String userId= firebaseAuth.getCurrentUser().getUid();
                    handlerC.saveUserId(userId);
                    DatabaseReference dataRef= databaseReference.child("tourmate").child("users").child(userId);

                    HashMap<String,Object> userInfo= new HashMap<>();
                    userInfo.put("email",email);
                    userInfo.put("password",password);
                    userInfo.put("confirmPassword",confirmPassword);
                    userInfo.put("firstName",firstName);
                    userInfo.put("lastName",lastName);

                    dataRef.setValue(userInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            progressDialog.dismiss();
                            if (task.isSuccessful()){

                                startActivity(new Intent(SignUpActivity.this,SignInActivity.class));
                            }
                            else{

                                Toast.makeText(SignUpActivity.this, "wrong", Toast.LENGTH_SHORT).show();
                            }
                        }

                    });

                }else{
                    progressDialog.dismiss();
                    Toast.makeText(SignUpActivity.this, "Something Went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }




    private void init() {

       emailAddressET= findViewById(R.id.emailAddressId);
       passwordET= findViewById(R.id.passwordId);
       confirmPasswordET= findViewById(R.id.confirmPasswordId);
       firstNameET= findViewById(R.id.firstNameId);
       lastNameET=findViewById(R.id.lastNameId);

       signUpBtn= findViewById(R.id.signUpButtonId);


       progressDialog = new ProgressDialog(this);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference();
    }
}
