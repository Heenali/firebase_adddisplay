package com.example.heenali.fire;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    // Declaring Button object.
    Button SubmitButton, ShowButton;

    // Declaring EditText object.
    EditText NameEditText, PhoneNumberEditText;

    // Declaring String variable ( In which we are storing firebase server URL ).
    public static final String Firebase_Server_URL = "https://august-selfie-frame.firebaseio.com/";

    // Declaring String variables to store name & phone number get from EditText.
    String NameHolder, NumberHolder;

    // // Declaring TextView object.
    TextView ShowDataTextView ;

    // Declaring Firebase object.
    Firebase firebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Adding MainActivity context into Firebase context.
        Firebase.setAndroidContext(MainActivity.this);

        // Passing firebase Server URL into firebase object.
        firebase = new Firebase(Firebase_Server_URL);

        // Adding ID'S .
        SubmitButton = (Button)findViewById(R.id.submit);

        ShowButton = (Button)findViewById(R.id.show);

        ShowDataTextView = (TextView)findViewById(R.id.showData);

        NameEditText = (EditText)findViewById(R.id.name);

        PhoneNumberEditText = (EditText)findViewById(R.id.phone_number);

        // Adding Click listener to Submit button.
        SubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Declaring student class object.
                Ads student = new Ads();

                // Calling function to Get data from EditText and store into string variables.
                GetDataFromEditText();

                // Adding student name into student class object.
                student.setAug15_Banner_Id(NameHolder);
                student.setAug15_InterstitialAds_Id(NumberHolder);
                student.setLordshiv_Banner_Id(NameHolder);
                student.setLordshiv_Interstitial_Id(NumberHolder);

                // Passing student phone number and name into firebase object to add into database.
                firebase.child("Ads").setValue(student);

                // Showing toast message after data inserted.
                Toast.makeText(MainActivity.this,"Data Inserted Successfully", Toast.LENGTH_LONG).show();

            }
        });

        // Adding click listener to Show data button.
        ShowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Adding addValueEventListener method on firebase object.
                firebase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot MainSnapshot) {

                        for (DataSnapshot SubSnapshot : MainSnapshot.getChildren()) {

                            Ads student = SubSnapshot.getValue(Ads.class);

                            // Adding name and phone number of student into string that is coming from server.
                            String ShowDataString = "Name : "+student.getAug15_Banner_Id()+"\nPhone Number : "+student.getAug15_InterstitialAds_Id()+"\n\n";

                            // Apply complete string variable into TextView.
                            ShowDataTextView.setText(ShowDataString);
                        }
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
                        System.out.println("Data Access Failed" + firebaseError.getMessage());
                    }
                });

            }
        });
    }

    public void GetDataFromEditText(){

        NameHolder = NameEditText.getText().toString().trim();

        NumberHolder = PhoneNumberEditText.getText().toString().trim();

    }
}
