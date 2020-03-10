package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Home extends AppCompatActivity {

    private Button button1;
    private Button button2;
    private Button button3;
    private Button button4;
    private Button button5;
    private Button button6;
    private ImageView topImage;
    private String userID;
    private boolean firstTimeOnHome = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button1 = findViewById(R.id.eventButton1);
        button2 = findViewById(R.id.eventButton2);
        button3 = findViewById(R.id.eventButton3);
        button4 = findViewById(R.id.eventButton4);
        button5 = findViewById(R.id.eventButton5);
        button6 = findViewById(R.id.eventButton6);

        System.out.println(firstTimeOnHome);
        if (firstTimeOnHome){
            firstTimeOnHome = false;
            final Bundle intent = getIntent().getExtras();
            userID = intent.getString("userID");
        }

        //Initialize the Buttons
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //set home selected
        bottomNavigationView.setSelectedItemId(R.id.home);

        //set button selected
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.home:
                        return true;
                    case R.id.help:
                        Intent intent = new Intent(getBaseContext(),Help.class);
                        intent.putExtra("userID",userID);
                        intent.putExtra("eventID","1");
                        startActivity(intent);
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.about_us:
                        intent = new Intent(getBaseContext(),About.class);
                        intent.putExtra("userID",userID);
                        intent.putExtra("eventID","1");
                        startActivity(intent);
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.account:
                        intent = new Intent(getBaseContext(),Account.class);
                        intent.putExtra("userID",userID);
                        intent.putExtra("eventID","1");
                        startActivity(intent);
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

        //Button function
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), Events.class);
                intent.putExtra("eventID","1");
                intent.putExtra("userID",userID);
                startActivity(intent);
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), Events.class);
                intent.putExtra("eventID","2");
                intent.putExtra("userID",userID);
                startActivity(intent);
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), Events.class);
                intent.putExtra("eventID","3");
                intent.putExtra("userID",userID);
                startActivity(intent);
            }
        });
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), Events.class);
                intent.putExtra("eventID","4");
                intent.putExtra("userID",userID);
                startActivity(intent);
            }
        });
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), Events.class);
                intent.putExtra("eventID","5");
                intent.putExtra("userID",userID);
                startActivity(intent);
            }
        });
        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), Events.class);
                intent.putExtra("eventID","6");
                intent.putExtra("userID",userID);
                startActivity(intent);
            }
        });

    }
}
