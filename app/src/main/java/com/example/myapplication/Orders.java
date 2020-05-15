package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.api.Distribution;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Orders extends AppCompatActivity {

    private String userID;
    private String eventID;
    private LinearLayout ordersList;
    private List<Button> orders = new ArrayList<>();
    private String orderID;
    private Button back;
    private CollectionReference mCollRef= FirebaseFirestore.getInstance().collection("bc_Order");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        final Bundle intent2 = getIntent().getExtras();
        eventID = intent2.getString("eventID");
        userID = intent2.getString("userID");

        ordersList = findViewById(R.id.ordersList);
        back = findViewById(R.id.backButton);

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

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), Account.class);
                intent.putExtra("eventID",eventID);
                intent.putExtra("userID",userID);
                startActivity(intent);
            }
        });
        addOrders();
    }

    public void addOrders(){
        mCollRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @SuppressLint("ResourceType")
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (final QueryDocumentSnapshot document : task.getResult()) {
                        System.out.println(document.getId()+" "+document.getData().toString());
                        if(document.get("userid").toString().equals(userID)){
                            Timestamp times = (Timestamp) document.get("dateofpurchase");
                            Date dates = times.toDate();

                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                            String strDate = sdf.format(dates);
                            Button newButton = new Button(Orders.this);
                            System.out.println(ordersList.getWidth());
                            System.out.println(ordersList.getHeight());
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                    ordersList.getWidth(),
                                    LinearLayout.LayoutParams.WRAP_CONTENT
                            );
                            params.setMargins(0,0,0,8);
                            newButton.setLayoutParams(params);
                            newButton.setText("Ticket ID: "+document.getId()+ "\n\nPurchased: "+strDate);
                            newButton.setTextColor(Color.parseColor("#024174"));
                            newButton.setTextAppearance(R.font.montserrat);
                            newButton.setAllCaps(false);
                            newButton.setBackgroundColor(Color.parseColor("#FFFFE0"));
                            newButton.setId(Integer.parseInt(document.getId()));
                            newButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    orderID = String.valueOf(v.getId());
                                    System.out.println("it actually opened button "+v.getId());
                                    Intent intent = new Intent(getBaseContext(), Ticket.class);
                                    intent.putExtra("userID",userID);
                                    intent.putExtra("orderID",orderID);
                                    intent.putExtra("eventID",eventID);
                                    startActivity(intent);
                                }
                            });
                            orders.add(newButton);
                            ordersList.addView(newButton);
                        }
                    }
                    if (orders.size()==0){
                        TextView emptyText = new TextView(Orders.this);
                        emptyText.setText("You have made no orders");
                        emptyText.setTextSize(25);
                        ordersList.addView(emptyText);
                    }
                } else {
                    System.out.println("Error getting documents");
                }
            }
        });
    }
}
