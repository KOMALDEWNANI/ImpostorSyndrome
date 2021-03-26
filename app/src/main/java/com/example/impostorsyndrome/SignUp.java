package com.example.impostorsyndrome;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class SignUp extends AppCompatActivity {

    EditText etName,etEmail,etPwd,etCnfPwd;
    Button btnSignup;
    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.signup);

        etName = findViewById(R.id.et_name);
        etName= findViewById(R.id.et_email);
        etPwd = findViewById(R.id.et_pwd);
        etCnfPwd = findViewById(R.id.et_cnf_pwd);
        btnSignup = findViewById(R.id.btn_signup);

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etName.getText().toString().equals("admin") && etEmail.getText().toString().equals("admin@email.com") && etPwd.getText().toString().equals("admin") && etCnfPwd.getText().toString().equals("admin")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(
                            SignUp.this
                    );
                    builder.setIcon(R.drawable.ic_check);
                    builder.setTitle("Login Successfully");
                    builder.setMessage("Welcome to Android Coding");

                    builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
            }else {
                    Toast.makeText(getApplicationContext(),"Wrong Credentials , Pls Try Again",Toast.LENGTH_SHORT).show();
                }
        }
    });
};
}
