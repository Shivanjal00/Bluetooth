package com.example.kharchapani;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegistrationActivity extends AppCompatActivity {

    private EditText mEmail;
    private  EditText mPass;
    private Button btnReg;
    private TextView mSignin;


    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(RegistrationActivity.this,R.color.sky));
        }

        mAuth = FirebaseAuth.getInstance();

        registration();
    }

    private void registration(){

        mEmail = findViewById(R.id.email_reg);
        mPass = findViewById(R.id.password_reg);;
        btnReg = findViewById(R.id.btn_reg);
        mSignin = findViewById(R.id.signin_here);

        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mEmail.getText().toString().trim();
                String pass = mPass.getText().toString().trim();

                if(TextUtils.isEmpty(email) || TextUtils.isEmpty(pass)){
                    Toast.makeText(RegistrationActivity.this, "Please enter credentials", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(email)) {
                    mEmail.setError("Email is Requried !");
                    return;
                } else if (TextUtils.isEmpty(pass)) {
                    mPass.setError("Password is requried");
                    return;
                }
                mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            Toast.makeText(RegistrationActivity.this, "Registration Complete", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        }else {

                            Toast.makeText(RegistrationActivity.this, "Registration Failed !", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
        mSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
    }

}