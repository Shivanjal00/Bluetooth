package com.example.kharchapani;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegistrationActivity extends AppCompatActivity {

    private EditText mEmail;
    private  EditText mPass;
    private Button btnReg;
    private TextView mSignin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
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