package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Confirmation extends AppCompatActivity {

    private TextView address;
    private TextView date;
    private TextView time;
    private TextView totalPrice;
    private TextView festivalName;
    private CollectionReference mCollRef = FirebaseFirestore.getInstance().collection("bc_Event");
    private CollectionReference mCollRef2 = FirebaseFirestore.getInstance().collection("bc_Location");
    private String eventID, userID, quantity,totalPriceStr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);
        BottomNavigationView navView = findViewById(R.id.bottom_navigation);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //set home selected
        bottomNavigationView.setSelectedItemId(R.id.events);



        //set button selected
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
        final Bundle intent2 = getIntent().getExtras();
        eventID = intent2.getString("eventID");
        userID = intent2.getString("userID");
        totalPriceStr = intent2.getString("totalPrice");
        quantity = intent2.getString("ticketQuantity");
        loadData();

    }

    public void loadData(){

        address = (TextView) findViewById(R.id.address);
        date = (TextView) findViewById(R.id.date);
        time = (TextView) findViewById(R.id.time);
        totalPrice = (TextView) findViewById(R.id.total);
        festivalName = (TextView) findViewById(R.id.festivalName);

        DocumentReference docRef = mCollRef.document(eventID);

        // Get the document, forcing the SDK to use the offline cache
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    // Document found in the offline cache
                    final DocumentSnapshot document = task.getResult();
                    Log.d("Caching", "Cached document data: " + document.getData());
//                  address.setText(document.get("firstlineaddress").toString());


                    System.out.println(document.getData());
                    System.out.println(document.get("starttime"));
                    Timestamp times = (Timestamp) document.get("starttime");
                    Date dates = times.toDate();

                    SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy");
                    String strDate = sdf.format(dates);
                    String startTime = (new SimpleDateFormat("HHmm")).format(dates);
                    Timestamp endTime = (Timestamp) document.get("endtime");
                    Date otherDate = endTime.toDate();
                    String strEndTime = (new SimpleDateFormat("HHmm")).format(otherDate);
                    time.setText(startTime+" till "+strEndTime);

                    date.setText(strDate);

//                  time.setText(document.get("startTime").toString());
                    totalPrice.setText("£" + totalPriceStr);
                    festivalName.setText(document.get("eventname").toString());
                    DocumentReference docRef2 = mCollRef2.document(document.get("eventlocationid").toString());
                    getLocation(docRef2);
                }
                else {
                    Log.d("Caching", "Cached get failed: ", task.getException());
                }
            }
        });
    }

    public void getLocation(DocumentReference df){

        df.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    final DocumentSnapshot document = task.getResult();
                    address.setText(document.get("firstaddressline").toString() +", "+document.get("secondaddressline").toString()+ ", "+ document.get("city").toString()+", "+ document.get("country").toString()+", "+ document.get("postcode").toString());

                }else{

                }

            }
        });
    } }




