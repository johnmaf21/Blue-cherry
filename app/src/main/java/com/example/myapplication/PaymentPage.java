package com.example.myapplication;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

public class PaymentPage extends AppCompatActivity {

    private EditText cardNo, cvc, expiry;
    private Button confirm, back;
    private int checkAll;
    private int highestID;
    private TextView total;
    private CollectionReference mCollRef= FirebaseFirestore.getInstance().collection("bc_Card");
    private CollectionReference mCollRef2= FirebaseFirestore.getInstance().collection("bc_Order");
    private String userID, eventID, totalPrice,quantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_page);
        final Bundle intent = getIntent().getExtras();
        eventID = intent.getString("eventID");
        userID = intent.getString("userID");
        totalPrice = intent.getString("totalPrice");
        quantity = intent.getString("ticketQuantity");

        //Initialize the Buttons
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //set button selected
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@Nonnull MenuItem menuItem) {
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
                        intent = new Intent(getBaseContext(),Account.class);
                        intent.putExtra("userID",userID);
                        intent.putExtra("eventID",eventID);
                        startActivity(intent);
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.events:
                        return true;

                }
                return false;
            }
        });

        //Initialise xml ID/variables
        //Edit Text
        cardNo = (EditText) findViewById(R.id.payEditTextCard);
        cvc = (EditText) findViewById(R.id.payEditTextCVC);
        expiry = (EditText) findViewById(R.id.payEditTextExpiry);
        //Text View
        //Buttons
        confirm = (Button) findViewById(R.id.payBtnConfirm);
        back = (Button) findViewById(R.id.payBtnBack);
        total = (TextView) findViewById(R.id.totalView);
        total.setText("£"+totalPrice);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateCardNumber() && validateCVC()) {
                    Intent intent = new Intent(getBaseContext(), Confirmation.class);
                    calcHighestID(cardNo,cvc,expiry);
                    intent.putExtra("userID",userID);
                    intent.putExtra("eventID",eventID);
                    intent.putExtra("totalPrice",totalPrice);
                    intent.putExtra("ticketQuantity",quantity);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(PaymentPage.this, "Please enter the correct information",Toast.LENGTH_SHORT).show();
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(PaymentPage.this, Events.class);
                    intent.putExtra("userID",userID);
                    intent.putExtra("eventID",eventID);
                    startActivity(intent);
            }
        });
    }

    //Validation methods
    private boolean validateCardNumber() {
        if (cardNo.getText().toString().length() == 13) {
            if(cardNo.getText().toString().matches("[0-9]+")){
                return true;
            }
        }
        Toast.makeText(PaymentPage.this, "Card Number must be 13 numbers long", Toast.LENGTH_SHORT).show();
        return false;
    }


    private boolean validateCVC() {
        if (cvc.getText().toString().length() == 3) {
            if(cardNo.getText().toString().matches("[0-9]+")){
                return true;
            }
        }
        Toast.makeText(PaymentPage.this, "Card Number must be 13 numbers long", Toast.LENGTH_SHORT).show();
        return false;
    }

    //This method is the incorrect method for
//    private boolean validateExpiry() {
//        if (cardNo.getTextSize() > 0 || cardNo.getTextSize() < 14) {
//            Intent intent = new Intent(PaymentPage.this, Home.class);
//            startActivity(intent);
//            return true;
//        }
//        else{textWrongInput.setText("Invalid Expriy");}
//        return false;
//    }

    private void addOrderToDatabase(EditText cardNo, EditText cvv, EditText expiry){
        Map<String, Object> payment = new HashMap<>();
        payment.put("cardnumber",cardNo.getText().toString());
        payment.put("cvv",cvv.getText().toString());
        payment.put("expirydate",expiry.getText().toString());
        final String newHighestID = String.valueOf(highestID);

        mCollRef.document(newHighestID).set(payment).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                System.out.println("successfully added payment details to database");
                calcHighestOrderID(newHighestID);

            }
        });
    }

    public void calcHighestID(final EditText cardNo, final EditText cvv, final EditText expiry){
        mCollRef
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@Nonnull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            highestID = task.getResult().size()+1;
                            addOrderToDatabase(cardNo,cvv,expiry);

                        }
                    }
                });
    }

    public void calcHighestOrderID(final String newHighestID){
        highestID = 0;
        mCollRef2
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@Nonnull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            highestID = task.getResult().size()+1;
                            Map<String, Object> order = new HashMap<>();
                            order.put("addtionalfee","0.30");
                            Date newDate = new Date();
                            order.put("dateofpurchase",(new Timestamp(newDate)));
                            order.put("eventid",eventID);
                            order.put("userid",userID);
                            order.put("ticketquantity",quantity);
                            order.put("paymentmethodid", newHighestID);
                            order.put("totalprice",totalPrice);

                            final String newOrderID = String.valueOf(highestID);
                            mCollRef2.document(newOrderID).set(order).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    System.out.println("successfully added a new order to the database");
                                }
                            });

                        }
                    }
                });
    }

}
