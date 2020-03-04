package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class Register extends AppCompatActivity {

    private String name;
    private String email;
    private String password;
    private String phoneNum;
    private Button finishRegister;
    private EditText nameTF;
    private EditText emailTF;
    private EditText passwordTF;
    private EditText phoneNumTF;
    private int highestID = 0;
    private CollectionReference mCollRef= FirebaseFirestore.getInstance().collection("bc_Users");

    public int getHighestID(){
        return highestID;
    }

    public void setHighestID(int highestID) {
        this.highestID = highestID;
        System.out.println(this.highestID);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        calcHighestID();

    }

    public void calcHighestID(){
        mCollRef
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                highestID+=1;
                            }
                            emailTF = (EditText) findViewById(R.id.register_email3);
                            passwordTF = (EditText) findViewById(R.id.register_password);
                            nameTF = (EditText) findViewById(R.id.register_name);
                            phoneNumTF = (EditText) findViewById(R.id.phoneno);


                            finishRegister = (Button) findViewById(R.id.openLocationPage);

                            finishRegister.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    email = emailTF.getText().toString();
                                    name = nameTF.getText().toString();
                                    password = passwordTF.getText().toString();
                                    phoneNum = phoneNumTF.getText().toString();
                                    Intent intent = new Intent(getBaseContext(), EnterLocation.class);


                                    System.out.println("--------------------------- "+getHighestID()+" -----------------------");
                                    intent.putExtra("email",email);
                                    intent.putExtra("password",password);
                                    intent.putExtra("highestID", highestID+1);
                                    intent.putExtra("name",name);
                                    intent.putExtra("phoneNum",phoneNum);
                                    startActivity(intent);
                                }
                            });
                    }
                }
    });
}


}
