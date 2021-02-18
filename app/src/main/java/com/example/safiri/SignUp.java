package com.example.safiri;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.safiri.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class SignUp extends AppCompatActivity implements View.OnClickListener {

    private ActivitySignUpBinding mSignUpBinding;
    private String mUsername;
    private String mPassword;
    private String mEmail;
    private FirebaseAuth mFirebaseAuth;
    private ProgressDialog mProgressDialog;
    private String mPasswordConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSignUpBinding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(mSignUpBinding.getRoot());
        mSignUpBinding.signInText.setOnClickListener(this);
        mSignUpBinding.signUpButton.setOnClickListener(this);
    }
    public void signUpCredentials(){
        mUsername = mSignUpBinding.signUpUsername.getText().toString().trim();
        mPassword = mSignUpBinding.signUpPassword.getText().toString().trim();
        mEmail = mSignUpBinding.signUpEmail.getText().toString().toLowerCase().trim();
        mPasswordConfirm = mSignUpBinding.signUpPasswordConfirm.getText().toString().trim();
        validate();
    }
    private void validate(){
        if(mUsername.isEmpty() || mUsername.length()<4){
            mSignUpBinding.signUpUsername.setError("atleast 4 characters");
            mSignUpBinding.signUpUsername.requestFocus();
            return;
        }
        else if(mEmail.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(mEmail).matches()){
            mSignUpBinding.signUpEmail.setError("provide a valid email");
            mSignUpBinding.signUpEmail.requestFocus();
            return;
        }else if(mPassword.isEmpty() || mPassword.length()<6){
            mSignUpBinding.signUpPassword.setError("atleast 6 characters");
            mSignUpBinding.signUpPassword.requestFocus();
            return;
        }else if(mPasswordConfirm.isEmpty() || !mPasswordConfirm.equals(mPassword)){
            mSignUpBinding.signUpPasswordConfirm.setError("password don't match");
            mSignUpBinding.signUpPasswordConfirm.requestFocus();
            return;
        }
        else {
            registerUser();
            Toast.makeText(this, "try", Toast.LENGTH_SHORT).show();
            mProgressDialog.dismiss();
        }

    }

    private void registerUser() {
        mFirebaseAuth = FirebaseAuth.getInstance();
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle("Registration");
        mProgressDialog.setMessage("Please Wait");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();

        mFirebaseAuth.createUserWithEmailAndPassword(mEmail,mPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    finish();
                    Toast.makeText(SignUp.this, "registered", Toast.LENGTH_SHORT).show();
                    Intent homeIntent = new Intent(SignUp.this,Home.class);
                    homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(homeIntent);
                }else{
                    Toast.makeText(SignUp.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public void signInText(){
        Intent intent = new Intent(SignUp.this,MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sign_in_text:
                signInText();
                break;
            case R.id.sign_up_button:
                signUpCredentials();
                break;
        }

    }
}