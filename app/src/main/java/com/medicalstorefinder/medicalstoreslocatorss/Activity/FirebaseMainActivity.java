package com.medicalstorefinder.medicalstoreslocatorss.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.medicalstorefinder.medicalstoreslocatorss.R;

public class FirebaseMainActivity extends AppCompatActivity {

    private TextView tvNotificationDetails;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_main);
        initControls();
        setNotificationData(getIntent().getExtras());
    }
    private void initControls() {
        tvNotificationDetails = (TextView) findViewById(R.id.tvNotificationDetails);
    }
    private void setNotificationData(Bundle extras) {
        if (extras == null)
            return;
        StringBuilder text = new StringBuilder("");
        text.append("Message Details:");
        text.append("\n");
        text.append("\n");
        if (extras.containsKey("keysMessageTitle")) {
            text.append(extras.get("keysMessageTitle"));
            text.append(extras.get("keysMessageBody"));
        }
        text.append("\n");
        if (extras.containsKey("message")) {
            text.append("Message: ");
            text.append(extras.get("message"));
        }
        tvNotificationDetails.setText(text);
    }
}