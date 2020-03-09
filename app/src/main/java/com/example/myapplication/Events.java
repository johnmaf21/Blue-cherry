package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Events extends AppCompatActivity {
    private TextView evName, evDesc, evLocation, evPrice, evStartTime, evEndTime, evAvTickets;
    private Button purchaseBtn, backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        //Initialize the Buttons
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //set home selected
        bottomNavigationView.setSelectedItemId(R.id.events);

        //set button selected
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext()
                                ,Home.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.help:
                        startActivity(new Intent(getApplicationContext()
                                ,Help.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.about_us:
                        startActivity(new Intent(getApplicationContext()
                                ,About.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.account:
                        startActivity(new Intent(getApplicationContext()
                                ,Account.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.events:
                        return true;

                }
                return false;
            }
        });

        evName = (TextView) findViewById(R.id.eventName);
        evDesc = (TextView) findViewById(R.id.eventDescription);
        evPrice = (TextView) findViewById(R.id.eventPrice);
        evAvTickets = (TextView) findViewById(R.id.ticketsAvailable);
        evLocation = (TextView) findViewById(R.id.eventLocation);
        evStartTime = (TextView) findViewById(R.id.startTime);
        evEndTime = (TextView) findViewById(R.id.endTime);

    }

    }

