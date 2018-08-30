package com.medicalstorefinder.medicalstoreslocatorss.Activity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alimuzaffar.lib.pin.PinEntryEditText;
import com.google.firebase.iid.FirebaseInstanceId;
import com.medicalstorefinder.medicalstoreslocatorss.Constants.Constants;
import com.medicalstorefinder.medicalstoreslocatorss.Constants.SharedPreference;
import com.medicalstorefinder.medicalstoreslocatorss.Constants.Utilities;
import com.medicalstorefinder.medicalstoreslocatorss.Models.ApiUser;
import com.medicalstorefinder.medicalstoreslocatorss.R;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class OtpVerificationActivity extends AppCompatActivity {
    PinEntryEditText pinEntry;
    ProgressDialog progressDialog;
    ApiUser apiUser;
    SharedPreference sharedPreference = new SharedPreference();
    TextView resendOTP;
    TextView txtTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification);

        Button verifyOTP = (Button)findViewById(R.id.otpVerifyBtn);
        resendOTP = (TextView)findViewById(R.id.otpResendBtn);
        pinEntry = (PinEntryEditText) findViewById(R.id.txt_pin_entry);
        TextView txtMobileNo=(TextView)findViewById(R.id.txtMobileNo);
        txtTimer=(TextView)findViewById(R.id.txtTimer);

        txtMobileNo.setText("Please Enter OTP Sent On \n +91 "+(sharedPreference.getValue( getBaseContext(), Constants.PREF_IS_USER, Constants.PREF_KEY_USER_PHONE )));
        reverseTimer(120,txtTimer);
//        pinEntry.setText("123456");
      /*  progressDialog = new ProgressDialog(getApplicationContext());
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);*/

        verifyOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pinEntry != null) {
                    new AuthoriseOTP().execute();
                }else{
                    Toast.makeText(OtpVerificationActivity.this, "Please Enter OTP", Toast.LENGTH_SHORT).show();
                    pinEntry.setText(null);
                }

            }
        });

        resendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AuthoriseCustomer().execute();

            }
        });

           /* pinEntry.setOnPinEnteredListener(new PinEntryEditText.OnPinEnteredListener() {
                @Override
                public void onPinEntered(CharSequence str) {
                    *//*if (str.toString().equals("1234")) {
                        Toast.makeText(OtpVerificationActivity.this, "SUCCESS", Toast.LENGTH_SHORT).show();

                        Intent splashIntent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(splashIntent);
                        finish();

                    } else {
                        Toast.makeText(OtpVerificationActivity.this, "FAIL", Toast.LENGTH_SHORT).show();
                        pinEntry.setText(null);
                    }*//*



                }
            });*/


    }

    @Override
    protected void onResume() {
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter("otp"));
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent myIntent = new Intent(getBaseContext(), MainActivity.class);
        startActivity(myIntent);
        finish();
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if(intent.getAction().equalsIgnoreCase("otp")){
                final String message = intent.getStringExtra("message");
                pinEntry.setText(message);
            }

        }
    };




    class AuthoriseOTP extends AsyncTask<Void, Void, String> {

        protected void onPreExecute() {
//            progressDialog.show();
        }

        protected String doInBackground(Void... urls) {

            Utilities utilities = new Utilities(getBaseContext());

            String address = Constants.API_VERIFY_OTP;
            Map<String, String> params = new HashMap<>();
            params.put("mobile", sharedPreference.getValue( getBaseContext(), Constants.PREF_IS_USER, Constants.PREF_KEY_USER_PHONE ));
            params.put("otp", pinEntry.getText().toString());

            return utilities.apiCalls(address,params);

        }

        protected void onPostExecute(String response) {
            try {

                if(response.equals("NO_INTERNET")) {
                    Toast.makeText(getBaseContext(), "Check internet connection", Toast.LENGTH_LONG).show();
                }
                else if(response.equalsIgnoreCase("ERROR")) {
                    Toast.makeText(getBaseContext(), "Please enter registered mobile number", Toast.LENGTH_LONG).show();
                }
                else
                {

                    JSONObject jsonObject1 = new JSONObject(response);
                    JSONObject jsonObject2 = new JSONObject(jsonObject1.getString("Content"));
//                    JSONObject jsonObject = new JSONObject(jsonObject2.getString("message"));

                    if(jsonObject2.getString("status").equalsIgnoreCase("error"))
                    {
                        Toast.makeText(getBaseContext(), jsonObject2.getString("message"), Toast.LENGTH_LONG).show();
                    }
                    else {
                        JSONObject jsonObject = new JSONObject(jsonObject2.getString("message"));

                        apiUser = new ApiUser();

                        apiUser.setID(jsonObject.getInt("id"));
                        apiUser.setFirst_Name(jsonObject.getString("firstname"));
                        apiUser.setLast_Name(jsonObject.getString("lastname"));
                        apiUser.setRegMobile(jsonObject.getString("mobile"));
                        apiUser.setAddress(jsonObject.getString("address"));
                        apiUser.setShop_Name(jsonObject.getString("shopname"));
                        apiUser.setEmail(jsonObject.getString("email"));
                        apiUser.setUserRole(jsonObject.getString("role"));
						apiUser.setPasswords( pinEntry.getText().toString());
//						apiUser.setProfilePicUrl(jsonObject.getString("photo"));

//						sharedPreference = new SharedPreference();

                        sharedPreference.putValue(getBaseContext(), Constants.PREF_IS_USER, Constants.PREF_KEY_USER_ID, String.valueOf(apiUser.getID()));
                        sharedPreference.putValue(getBaseContext(), Constants.PREF_IS_USER, Constants.PREF_KEY_USER_Email, apiUser.getEmail());
                        sharedPreference.putValue(getBaseContext(), Constants.PREF_IS_USER, Constants.PREF_KEY_USER_Address, apiUser.getAddress());
                        sharedPreference.putValue(getBaseContext(), Constants.PREF_IS_USER, Constants.PREF_KEY_USER_SHOP_NAME, apiUser.getShop_Name());
                        sharedPreference.putValue(getBaseContext(), Constants.PREF_IS_USER, Constants.PREF_KEY_USER_FIRST_NAME, apiUser.getFirst_Name());
                        sharedPreference.putValue(getBaseContext(), Constants.PREF_IS_USER, Constants.PREF_KEY_USER_LAST_NAME, apiUser.getLast_Name());
                        sharedPreference.putValue(getBaseContext(), Constants.PREF_IS_USER, Constants.PREF_KEY_USER_PHONE, apiUser.getRegMobile());
                        sharedPreference.putValue(getBaseContext(), Constants.PREF_IS_USER, Constants.PREF_USER_ROLE, apiUser.getUserRole());
                        sharedPreference.putValue(getBaseContext(), Constants.PREF_IS_USER, Constants.PREF_KEY_USER_PASS, apiUser.getPasswords());
//							sharedPreference.putValue(getContext(), Constants.PREF_IS_USER, Constants.PREF_KEY_USER_ProfilePic, apiUser.getProfilePicUrl());

                        Toast.makeText(getBaseContext(), "Login Success", Toast.LENGTH_SHORT).show();

                        Intent myIntent = new Intent(getBaseContext(), CustomerActivity.class);
                        startActivity(myIntent);
                        finish();
                    }
                }

            } catch (Exception e) {
                Toast.makeText( getBaseContext(), "Please try again later...", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
            finally {
//                progressDialog.dismiss();
            }

        }
    }
    class AuthoriseCustomer extends AsyncTask<Void, Void, String> {
        String token = "";
        protected void onPreExecute() {
            token = FirebaseInstanceId.getInstance().getToken();
            while(token == null)//this is used to get firebase token until its null so it will save you from null pointer exeption
            {
                token = FirebaseInstanceId.getInstance().getToken();
            }
        }

        protected String doInBackground(Void... urls) {

            Utilities utilities = new Utilities(getBaseContext());

            String address = Constants.API_CUSTOMER_LOGIN;
            Map<String, String> params = new HashMap<>();
            params.put("mobile", sharedPreference.getValue( getBaseContext(), Constants.PREF_IS_USER, Constants.PREF_KEY_USER_PHONE ));
            params.put("deviceid", token);
            return utilities.apiCalls(address,params);

        }

        protected void onPostExecute(String response) {
            try {


                if(response.equals("NO_INTERNET")) {
                    Toast.makeText(getBaseContext(), "Check internet connection", Toast.LENGTH_LONG).show();
                }
                else if(response.equals("ERROR")) {
                    Toast.makeText(getBaseContext(), "Please enter registered mobile number", Toast.LENGTH_LONG).show();
                }
                else
                {
                    if(response.equals("false"))
                    {
                        Toast.makeText(getBaseContext(), "Please enter registered mobile number", Toast.LENGTH_LONG).show();
                    }
                    else {
                        JSONObject jsonObject1 = new JSONObject(response);
                        JSONObject jsonObject2 = new JSONObject(jsonObject1.getString("Content"));
                        String s = jsonObject2.getString("status");
                        if(s.equalsIgnoreCase("success")){
                            Toast.makeText(getBaseContext(), jsonObject2.getString("message"), Toast.LENGTH_LONG).show();
                            resendOTP.setVisibility(View.GONE);
                            reverseTimer(120,txtTimer);
                        }

                    }
                }

            } catch (Exception e) {
                Toast.makeText( getBaseContext(), "Please try again later...", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
            finally {

            }

        }
    }

    public void reverseTimer(int Seconds,final TextView tv){

        new CountDownTimer(Seconds* 1000+1000, 1000) {

            public void onTick(long millisUntilFinished) {
                resendOTP.setVisibility(View.GONE);
                int seconds = (int) (millisUntilFinished / 1000);
                int minutes = seconds / 60;
                seconds = seconds % 60;
                tv.setText("" + String.format("%02d", minutes)
                        + ":" + String.format("%02d", seconds));
            }

            public void onFinish() {
//                tv.setText("Completed");
                resendOTP.setVisibility(View.VISIBLE);
            }
        }.start();
    }

}
