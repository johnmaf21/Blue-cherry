package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.Source;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Events extends AppCompatActivity {
    private TextView eventNameTV;
    private TextView ticketsAvailableTV;
    private TextView addressTV;
    private TextView dateTimeTV;
    private EditText quantityET;
    private TextView priceTV;
    private TextView eventDescription;
    private Button buyButton;
    private TextView totalPrice;
    private CollectionReference mCollRef= FirebaseFirestore.getInstance().collection("bc_Event");
    private CollectionReference mCollRef2 = FirebaseFirestore.getInstance().collection("bc_Location");
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private ImageView eventImage;
    private String eventID;
    private String userID;
    private Button backBtt;
    private int originalAvailibility;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        buyButton = findViewById(R.id.buyBttn);
        eventImage = findViewById(R.id.imageView12);

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
        final Bundle intent = getIntent().getExtras();
        eventID = intent.getString("eventID");
        userID = intent.getString("userID");
        loadData();
        loadPic();

        buyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("do something 1");
                if (quantityET.getText().toString().equals("0") || quantityET.getText().toString().equals("")||!(quantityET.getText().toString().matches("[0-9]+"))){
                    Toast.makeText(Events.this, "Quantity must be a whole number bigger than 0", Toast.LENGTH_SHORT).show();
                }else{
                    Map<String, Object> edit = new HashMap<>();
                    edit.put("ticketsavailable",ticketsAvailableTV.getText().toString());
                    mCollRef.document(userID).set(edit, SetOptions.merge());
                    Intent intent = new Intent(getBaseContext(), PaymentPage.class);
                    intent.putExtra("totalPrice",(totalPrice.getText().toString()).substring(1));
                    intent.putExtra("userID",userID);
                    intent.putExtra("eventID",eventID);
                    intent.putExtra("ticketQuantity", quantityET.getText().toString());
                    startActivity(intent);
                }

            }
        });

        quantityET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(quantityET.getText().toString().matches("[0-9]+")){
                System.out.println(quantityET.getText().toString());
                System.out.println(ticketsAvailableTV.getText().toString());
                    if((Integer.parseInt(quantityET.getText().toString())<=Integer.parseInt(ticketsAvailableTV.getText().toString())) || (Integer.parseInt(quantityET.getText().toString())<11)){

                        if(quantityET.getText().toString().equals("0")|| quantityET.getText() == null){
                            totalPrice.setText("£0.00");
                            ticketsAvailableTV.setText(String.valueOf(originalAvailibility));
                        }else{
                            double originalPrice =Double.parseDouble(priceTV.getText().toString().substring(1));
                            int quantity = Integer.parseInt(quantityET.getText().toString());
                            String finalPrice = String.valueOf((originalPrice*quantity)+0.30);
                            totalPrice.setText("£"+finalPrice);
                            ticketsAvailableTV.setText(String.valueOf(originalAvailibility-quantity));
                        }
                    }else{
                        Toast.makeText(Events.this, "You cannot buy more than 10 tickets at once and you can't buy more tickets than the venue capacity", Toast.LENGTH_SHORT).show();
                        totalPrice.setText("£0.00");
                        ticketsAvailableTV.setText(String.valueOf(originalAvailibility));
                    }

                }else{
                    Toast.makeText(Events.this, "Quantity must be a whole number bigger than 0", Toast.LENGTH_SHORT).show();
                    totalPrice.setText("£0.00");
                    ticketsAvailableTV.setText(String.valueOf(originalAvailibility));
                }
            }
        });

        backBtt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(),Home.class);
                intent.putExtra("userID",userID);
                intent.putExtra("eventID",eventID);
                startActivity(intent);
            }
        });
    }

    public void loadData(){

        eventNameTV = findViewById(R.id.festivalName);
        ticketsAvailableTV = findViewById(R.id.eventPriceTV);
        addressTV = findViewById(R.id.eventLocationTV);
        dateTimeTV = findViewById(R.id.eventDateTimeTV2);
        quantityET = findViewById(R.id.chosenQuantityTV);
        priceTV = findViewById(R.id.eventPriceTV2);
        totalPrice = findViewById(R.id.totalTV2);
        backBtt = findViewById(R.id.eventBackBttn);
        buyButton = findViewById(R.id.buyBttn);
        eventDescription = findViewById(R.id.eventDescriptionTV);

        DocumentReference docRef = mCollRef.document(eventID);

        // Get the document, forcing the SDK to use the offline cache
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    // Document found in the offline cache
                    final DocumentSnapshot document = task.getResult();
                    Log.d("Caching", "Cached document data: " + document.getData());

                    Timestamp times = (Timestamp) document.get("starttime");
                    Date dates = times.toDate();
                    SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy");
                    String strDate = sdf.format(dates);
                    String startTime = (new SimpleDateFormat("HHmm")).format(dates);

                    Timestamp endTime = (Timestamp) document.get("endtime");
                    Date otherDate = endTime.toDate();
                    String strEndTime = (new SimpleDateFormat("HHmm")).format(otherDate);

                    dateTimeTV.setText(strDate+"\n"+startTime+" to "+strEndTime);

                    priceTV.setText("£" + document.get("eventprice").toString());
                    eventNameTV.setText(document.get("eventname").toString());

                    ticketsAvailableTV.setText(document.get("ticketsavailable").toString());
                    originalAvailibility = Integer.parseInt(document.get("ticketsavailable").toString());
                    eventDescription.setText(document.get("eventdescription").toString());

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
                    addressTV.setText(document.get("firstaddressline").toString() +", "+document.get("secondaddressline").toString()+ ", "+ document.get("city").toString()+", "+ document.get("country").toString()+", "+ document.get("postcode").toString());
                }else{

                }

            }
        });
    }

    public void loadPic(){
        StorageReference event1 = storage.getReferenceFromUrl("https://firebasestorage.googleapis.com/v0/b/funbook-bfd76.appspot.com/o/1.jpg?alt=media&token=2159a54b-a7ad-4ff4-81cf-a71bb6a22d08");
        StorageReference event2 = storage.getReferenceFromUrl("https://firebasestorage.googleapis.com/v0/b/funbook-bfd76.appspot.com/o/2.jpg?alt=media&token=174a8b5e-4017-4c95-9921-b0c4ada5c8aa");
        StorageReference event3 = storage.getReferenceFromUrl("https://firebasestorage.googleapis.com/v0/b/funbook-bfd76.appspot.com/o/3.jpg?alt=media&token=23095b80-52fa-49ae-9b56-90b0ab0835d2");
        StorageReference event4 = storage.getReferenceFromUrl("https://firebasestorage.googleapis.com/v0/b/funbook-bfd76.appspot.com/o/4.jpg?alt=media&token=5b614c08-6934-43d4-b5b0-d29460e5e957");
        StorageReference event5 = storage.getReferenceFromUrl("https://firebasestorage.googleapis.com/v0/b/funbook-bfd76.appspot.com/o/5.jpg?alt=media&token=52a709dd-9366-4a71-820c-7566c5218dad");
        StorageReference event6 = storage.getReferenceFromUrl("https://firebasestorage.googleapis.com/v0/b/funbook-bfd76.appspot.com/o/6.jpg?alt=media&token=e19fc2d3-3a34-406a-8858-14283bf2c88b");
        if (eventID.equals("1")){
                Glide.with(this)
                        .using(new FirebaseImageLoader())
                        .load(event1)
                        .into(eventImage);}
        else if(eventID.equals("2")){
                Glide.with(this)
                        .using(new FirebaseImageLoader())
                        .load(event2)
                        .into(eventImage);}
            else if(eventID.equals("3")){
                Glide.with(this)
                        .using(new FirebaseImageLoader())
                        .load(event3)
                        .into(eventImage);}
            else if(eventID.equals("4")){
                Glide.with(this)
                        .using(new FirebaseImageLoader())
                        .load(event4)
                        .into(eventImage);}
            else if(eventID.equals("5")){
                Glide.with(this)
                        .using(new FirebaseImageLoader())
                        .load(event5)
                        .into(eventImage);}
            else{
                Glide.with(this)
                        .using(new FirebaseImageLoader())
                        .load(event6)
                        .into(eventImage);}


    }

}

