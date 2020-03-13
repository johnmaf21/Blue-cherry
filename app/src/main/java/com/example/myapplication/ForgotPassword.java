package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

public class ForgotPassword extends AppCompatActivity {

    private Button back;
    private EditText emailET;
    private Button changePassword;
    private CollectionReference mCollRef= FirebaseFirestore.getInstance().collection("bc_Users");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        back = (Button) findViewById(R.id.eventBackBttn3);
        emailET = (EditText) findViewById(R.id.register_email4);
        changePassword = (Button) findViewById(R.id.button);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForgotPassword.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!emailET.getText().toString().isEmpty()){
                    mCollRef
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            System.out.println(document.getId()+" "+document.getData().toString());
                                            if (document.get("email").toString().equals(emailET.getText().toString())){
                                                Intent intent = new Intent(getBaseContext(), Account.class);
                                                intent.putExtra("eventID","1");
                                                intent.putExtra("userID",document.getId());
                                                startActivity(intent);
                                            }
                                        }
                                    } else {
                                        System.out.println("Error getting documents");
                                    }
                                }
                            });
                }else{
                    Toast.makeText(ForgotPassword.this, "Please enter a email", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
}
