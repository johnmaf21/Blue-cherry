package com.example.myapplication;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


public class LoginActivity extends AppCompatActivity {

    private EditText name;
    private EditText password;
    private Button login;
    private Button forgotPassword;
    private Button register;
    private SessionManager session;
    private CollectionReference mCollRef= FirebaseFirestore.getInstance().collection("bc_Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        name = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.btnLogin);
        forgotPassword = (Button) findViewById(R.id.openForgotPasswordPage);
        register = (Button) findViewById(R.id.openRegister);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String emailText = name.getText().toString();
                String passwordText = password.getText().toString();
                if (!emailText.isEmpty() && !passwordText.isEmpty()){
                    validate(emailText,passwordText);
                    Toast.makeText(LoginActivity.this, "Either email or password is incorrect. Try again", Toast.LENGTH_SHORT).show();

                }else{
                    Toast.makeText(LoginActivity.this, "Please enter an email and a password", Toast.LENGTH_SHORT).show();
                }

            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgotPassword.class);
                startActivity(intent);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, Register.class);
                startActivity(intent);
            }
        });

    }

    private void validate(final String userName, final String userPassword) {
        mCollRef
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                System.out.println(document.getId()+" "+document.getData().toString());
                                if (document.get("email").toString().equals(userName) && document.get("password").toString().equals(userPassword)){
                                    Intent intent = new Intent(getBaseContext(), Home.class);
                                    intent.putExtra("userID",document.getId());
                                    startActivity(intent);
                                }
                            }
                        } else {
                            System.out.println("Error getting documents");
                        }
                    }
                });

    }
}
