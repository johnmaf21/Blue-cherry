package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.w3c.dom.Text;

import java.util.Date;

public class PaymentPage extends AppCompatActivity {

    private EditText name, cardNo, cvc, expiry;
    private TextView textWrongInput;
    private Button confirm, back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_page);

        //Initialize the Buttons
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //set button selected
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext()
                                ,Home.class));
                        return true;
                    case R.id.help:
                        startActivity(new Intent(getApplicationContext()
                                ,Help.class));
                        overridePendingTransition(0,0);
                        System.out.println("help working");
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
                }
                return false;
            }
        });

        //Initialise xml ID/variables
        //Edit Text
        name = (EditText) findViewById(R.id.payEditTextName);
        cardNo = (EditText) findViewById(R.id.payEditTextCard);
        cvc = (EditText) findViewById(R.id.payEditTextCVC);
        expiry = (EditText) findViewById(R.id.payEditTextExpiry);
        //Text View
        textWrongInput = (TextView) findViewById(R.id.payTextViewWrong);
        //Buttons
        confirm = (Button) findViewById(R.id.payBtnConfirm);
        back = (Button) findViewById(R.id.payBtnBack);
    }

    private boolean validateCardNumber() {
        if (cardNo.getTextSize() > 0 || cardNo.getTextSize() <= 14) {
            Intent intent = new Intent(PaymentPage.this, Home.class);
            startActivity(intent);
            return true;
        }
        else{textWrongInput.setText("Invalid Card Number");}
        return false;
    }

    private boolean validateCVC() {
        if (cvc.getTextSize() > 0 || cvc.getTextSize() <= 3) {
            Intent intent = new Intent(PaymentPage.this, Home.class);
            startActivity(intent);
            return true;
        }
        else{textWrongInput.setText("Invalid CVC Number");}
        return false;
    }

    //The Expiry date is bound to completely change ignore this method and the expiry implemented in the XML
//    private boolean validateExpiry() {
//        if (cardNo.getTextSize() > 0 || cardNo.getTextSize() < 14) {
//            Intent intent = new Intent(PaymentPage.this, Home.class);
//            startActivity(intent);
//            return true;
//        }
//        else{textWrongInput.setText("Invalid Expriy");}
//        return false;
//    }
}
