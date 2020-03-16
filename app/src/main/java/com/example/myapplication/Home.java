package com.example.myapplication;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import javax.annotation.Nonnull;

public class Home extends AppCompatActivity {
    //Base buttons
    private Button accountBtn, signoutBtn, faqBtn, aboutBtn;

    //Event buttons
    private Button button1, button2, button3, button4, button5, button6;

    private ImageView topImage;
    private String userID;
    private boolean firstTimeOnHome = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialise base buttons
        signoutBtn = findViewById(R.id.signoutButton);


        //Initialise event buttons
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
            public boolean onNavigationItemSelected(@Nonnull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.home:
                        return true;
                    case R.id.events:
                        Intent intent = new Intent(getBaseContext(),Events.class);
                        intent.putExtra("userID",userID);
                        intent.putExtra("eventID","1");
                        startActivity(intent);
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.help:
                        intent = new Intent(getBaseContext(),Help.class);
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

        //Button functions
        signoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                startActivity(intent);
            }
        });


        //Event buttons
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), Events.class);
                intent.putExtra("eventID","6");
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
                intent.putExtra("eventID","1");
                intent.putExtra("userID",userID);
                startActivity(intent);
            }
        });

        VideoView videoView = findViewById(R.id.video_views);
        String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.vodeo;
        Uri uri = Uri.parse(videoPath);
        videoView.setVideoURI(uri);

        MediaController mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(videoView);

    }
}
