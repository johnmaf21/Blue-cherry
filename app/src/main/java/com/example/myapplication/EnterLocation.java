package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class EnterLocation extends AppCompatActivity {
    private Button register;
    private EditText address1TF;
    private EditText address2TF;
    private EditText address3TF;
    private EditText cityTF;
    private EditText countryTF;
    private EditText countyTF;
    private EditText postcodeTF;
    private int highestID;
    private CollectionReference mCollRef= FirebaseFirestore.getInstance().collection("bc_Location");
    private CollectionReference mCollRef2= FirebaseFirestore.getInstance().collection("bc_Users");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_location);

        address1TF = (EditText) findViewById(R.id.address1TF);
        address2TF = (EditText) findViewById(R.id.address2TF);
        address3TF = (EditText) findViewById(R.id.address3TF);
        cityTF = (EditText) findViewById(R.id.cityTF);
        countryTF = (EditText) findViewById(R.id.countryTF);
        countyTF = (EditText) findViewById(R.id.countyTF);
        postcodeTF = (EditText) findViewById(R.id.postcodeTF);
        final Bundle intent = getIntent().getExtras();
        String[] names = intent.getString("name").split("\\s+");
        System.out.println(Arrays.toString(names)+" | "+intent.getString("email")+" | "+intent.getString("phoneNum")+" | "+intent.getString("password"));

        register = (Button) findViewById(R.id.completeRegister);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String address1 = address1TF.getText().toString();
                String address2 = ((address2TF.getText().toString().equals("") || address2TF.getText().toString() == null) ? null : address2TF.getText().toString());
                String address3 = ((address3TF.getText().toString().equals("") || address3TF.getText().toString() == null) ? null : address3TF.getText().toString());
                String city = cityTF.getText().toString();
                String country = ((countryTF.getText().toString().equals("") || countryTF.getText().toString() == null) ? null : countryTF.getText().toString());
                String county = ((countyTF.getText().toString().equals("") || countyTF.getText().toString() == null) ? null : countyTF.getText().toString());
                String postcode = postcodeTF.getText().toString();
                calcHighestID(address1,address2,address3,city,country,county,postcode,intent);

            }
        });
    }

    public void createAccount(String address1String, String address2String, String address3String, String cityString, String countryString, String countyString, String postcodeString, final Bundle intent){
        Map<String, Object> userLocation = new HashMap<>();
        userLocation.put("city", cityString);
        userLocation.put("county", countyString);
        userLocation.put("country", countryString);
        userLocation.put("firstaddressline", address1String);
        userLocation.put("secondddressline", address2String);
        userLocation.put("thirdaddressline", address3String);
        userLocation.put("postcode", postcodeString);

        final String locationID = String.valueOf(getHighestID()+1);
        System.out.println(locationID);

        mCollRef.document(locationID).set(userLocation).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("DocSnippets", "User Location Document successfully written!");
                String[] names = intent.getString("name").split("\\s+");
                System.out.println(Arrays.toString(names)+" "+intent.getString("email")+" "+intent.getString("phoneNum")+" "+intent.getString("password"));

                Map<String, Object> account = new HashMap<>();
                account.put("firstname", names[0]);
                account.put("secondname",names[1]);
                account.put("email",intent.getString("email"));
                account.put("phoneno", intent.getString("phoneNum"));
                account.put("password",intent.getString("password"));
                account.put("userlocationid",locationID);

                System.out.println(intent.getInt("highestID",0));
                mCollRef2.document(String.valueOf((intent.getInt("highestID",0)))).set(account).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("DocSnippets", "DocumentSnapshot successfully written!");
                        Intent intent = new Intent(EnterLocation.this, LoginActivity.class);
                        startActivity(intent);
                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("DocSnippets", "Error writing document", e);
                            }
                        });


            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("DocSnippets", "Error writing document", e);
                    }
                });



    }

    public void calcHighestID(final String address1String, final String address2String, final String address3String, final String cityString, final String countryString, final String countyString, final String postcodeString, final Bundle intent){
        mCollRef
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                highestID += 1;
                            }
                            createAccount(address1String,address2String,address3String,cityString,countryString,countyString,postcodeString,intent);


                        }
                    }
                });
    }
    public void setHighestID(int highestID){
        this.highestID = highestID;
    }
    public int getHighestID(){
        return highestID;
    }
}
