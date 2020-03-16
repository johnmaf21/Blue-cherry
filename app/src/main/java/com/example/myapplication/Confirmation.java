package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Nonnull;

public class Confirmation extends AppCompatActivity {

    private TextView address;
    private TextView date;
    private TextView totalPrice;
    private TextView festivalName;
    private ImageView eventImage;
    private TextView quantityTV;
    private CollectionReference mCollRef = FirebaseFirestore.getInstance().collection("bc_Event");
    private CollectionReference mCollRef2 = FirebaseFirestore.getInstance().collection("bc_Location");
    private FirebaseStorage storage = FirebaseStorage.getInstance();
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


        eventImage = findViewById(R.id.imageView6);
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
        final Bundle intent2 = getIntent().getExtras();
        eventID = intent2.getString("eventID");
        userID = intent2.getString("userID");
        totalPriceStr = intent2.getString("totalPrice");
        quantity = intent2.getString("ticketQuantity");
        loadData();
        loadPic();

    }

    public void loadData(){

        address = (TextView) findViewById(R.id.address);
        date = (TextView) findViewById(R.id.date);
        totalPrice = (TextView) findViewById(R.id.total);
        festivalName = (TextView) findViewById(R.id.festivalName);
        quantityTV = (TextView) findViewById(R.id.numOfTickets);

        DocumentReference docRef = mCollRef.document(eventID);

        // Get the document, forcing the SDK to use the offline cache
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@Nonnull Task<DocumentSnapshot> task) {
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
                    String startTime = (new SimpleDateFormat("H:mm a")).format(dates);
                    Timestamp endTime = (Timestamp) document.get("endtime");
                    Date otherDate = endTime.toDate();
                    String strEndTime = (new SimpleDateFormat("H:mm a")).format(otherDate);
                    quantityTV.setText(quantity);

                    date.setText(strDate+"\n"+startTime+" till "+strEndTime);

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
            public void onComplete(@Nonnull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    final DocumentSnapshot document = task.getResult();
                    address.setText(document.get("firstaddressline").toString() +", "+document.get("secondaddressline").toString()+ ", "+ document.get("city").toString()+", "+ document.get("country").toString()+", "+ document.get("postcode").toString());
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




