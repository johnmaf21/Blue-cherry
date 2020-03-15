package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class Account extends AppCompatActivity {

    private int instruction;
    private String eventID;
    private String userID;
    private String locationID;
    private TextView firstName;
    private TextView lastName;
    private TextView phoneNum;
    private TextView emailAddress;
    private EditText editText1;
    private EditText editText2;
    private Button cancel;
    private Button accept;
    private Button changePassword;
    private Button changeEmail;
    private Button changePhone;
    private Button openOrders;
    private CollectionReference mCollRef = FirebaseFirestore.getInstance().collection("bc_Users");




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        final Bundle intent2 = getIntent().getExtras();
        eventID = intent2.getString("eventID");
        userID = intent2.getString("userID");


        //Initialize the NavBar
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //set selected
        bottomNavigationView.setSelectedItemId(R.id.account);

        //set botton selected
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.home:
                        Intent intent = new Intent(getBaseContext(),Home.class);
                        intent.putExtra("userID",userID);
                        intent.putExtra("eventID",eventID);
                        startActivity(intent);
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.help:
                        intent = new Intent(getBaseContext(),Help.class);
                        intent.putExtra("userID",userID);
                        intent.putExtra("eventID",eventID);
                        startActivity(intent);
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.about_us:
                        intent = new Intent(getBaseContext(),About.class);
                        intent.putExtra("userID",userID);
                        intent.putExtra("eventID",eventID);
                        startActivity(intent);
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.account:
                        return true;
                    case R.id.events:
                        intent = new Intent(getBaseContext(),Events.class);
                        intent.putExtra("userID",userID);
                        intent.putExtra("eventID",eventID);
                        startActivity(intent);
                        overridePendingTransition(0,0);
                        return true;

                }
                return false;
            }
        });



        //initialize TextFields and buttons
        firstName = findViewById(R.id.accFirstNameTextView);
        lastName = findViewById(R.id.accLastNameTextView);
        emailAddress = findViewById(R.id.accEmailTextView);
        phoneNum = findViewById(R.id.accPhoneTextView);

        changeEmail = findViewById(R.id.accEmailButton);
        changePhone = findViewById(R.id.accPhoneButton);
        changePassword = findViewById(R.id.accPasswordButton);
        editText1 = findViewById(R.id.accEditText1);
        editText2 = findViewById(R.id.accEditText2);
        cancel = findViewById(R.id.accCancelButton);
        accept = findViewById(R.id.accAcceptButton);
        openOrders = findViewById(R.id.viewOrders);

        hideComponents();

        DocumentReference docRef = mCollRef.document(userID);
        // Get the document, forcing the SDK to use the offline cache
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    // Document found in the offline cache
                    final DocumentSnapshot document = task.getResult();
                    firstName.setText(document.get("firstname").toString());
                    lastName.setText(document.get("secondname").toString());
                    phoneNum.setText(document.get("phoneno").toString());
                    emailAddress.setText(document.get("email").toString());
                    locationID = document.get("userlocationid").toString();

                }
            }
        });

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                instruction = 1;
                editText1.setHint("New password");
                editText2.setHint("Confirm new password");
                editText1.setVisibility(View.VISIBLE);
                editText2.setVisibility(View.VISIBLE);
                accept.setVisibility(View.VISIBLE);
                cancel.setVisibility(View.VISIBLE);
                editText1.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                editText2.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }});


        changePhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                instruction = 2;
                editText1.setHint("New phone number");
                editText2.setHint("Confirm new phone number");
                editText1.setVisibility(View.VISIBLE);
                editText2.setVisibility(View.VISIBLE);
                accept.setVisibility(View.VISIBLE);
                cancel.setVisibility(View.VISIBLE);
            }});


        changeEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                instruction = 3;
                editText1.setHint("New email address");
                editText2.setHint("Confirm new email");
                editText1.setVisibility(View.VISIBLE);
                editText2.setVisibility(View.VISIBLE);
                accept.setVisibility(View.VISIBLE);
                cancel.setVisibility(View.VISIBLE);
            }});


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideComponents();
            }});

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> edit = new HashMap<>();
                if (editText1.getText().toString().equals(editText2.getText().toString())) {
                    if (instruction == 1 && isSame()) {
                        edit.put("password",editText1.getText().toString());
                        mCollRef.document(userID).set(edit, SetOptions.merge());
                        hideComponents();
                    } else if (instruction == 2 && isSame()){
                        phoneNum.setText(editText2.getText().toString());
                        edit.put("phoneno",editText2.getText().toString());
                        mCollRef.document(userID).set(edit,SetOptions.merge());
                        hideComponents();
                    }else if (instruction == 3 && isSame()){
                        emailAddress.setText(editText2.getText().toString());
                        edit.put("email",editText2.getText().toString());
                        mCollRef.document(userID).set(edit,SetOptions.merge());
                        hideComponents();
                    }
                }else {
                    System.out.println("incorrect input");
                    Error error = new Error("incorrect input");
                }
            }});

        openOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(),Orders.class);
                intent.putExtra("userID",userID);
                intent.putExtra("eventID",eventID);
                startActivity(intent);
            }
        });

    }

    public void hideComponents(){
        editText1.setVisibility(View.INVISIBLE);
        editText2.setVisibility(View.INVISIBLE);
        accept.setVisibility(View.INVISIBLE);
        cancel.setVisibility(View.INVISIBLE);
    }
    public boolean isSame(){
        if ((!editText1.getText().toString().isEmpty())&&(!editText2.getText().toString().isEmpty())){
            if(editText1.getText().toString().equals(editText2.getText().toString())){
                return true;
            }else{
                Toast.makeText(Account.this,"Box 1 needs to be the same as box 2",Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(Account.this,"Please fill in both boxes",Toast.LENGTH_SHORT).show();
        }
        return false;
    }
}
