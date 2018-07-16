package com.medicalstorefinder.medicalstorelocator.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.alimuzaffar.lib.pin.PinEntryEditText;
import com.medicalstorefinder.medicalstorelocator.Constants.Constants;
import com.medicalstorefinder.medicalstorelocator.Constants.SharedPreference;
import com.medicalstorefinder.medicalstorelocator.Constants.Utilities;
import com.medicalstorefinder.medicalstorelocator.Models.ApiUser;
import com.medicalstorefinder.medicalstorelocator.R;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class OtpVerificationActivity extends AppCompatActivity {
    PinEntryEditText pinEntry;
    ProgressDialog progressDialog;
    ApiUser apiUser;
    SharedPreference sharedPreference = new SharedPreference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification);

        Button verifyOTP = (Button)findViewById(R.id.otpVerifyBtn);
        pinEntry = (PinEntryEditText) findViewById(R.id.txt_pin_entry);

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
//						apiUser.setPasswords(jsonObject.getString("password"));
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

//							sharedPreference.putValue(getContext(), Constants.PREF_IS_USER, Constants.PREF_KEY_USER_ProfilePic, apiUser.getProfilePicUrl());

                        Toast.makeText(getBaseContext(), "Login Success", Toast.LENGTH_SHORT).show();

                        Intent myIntent = new Intent(getBaseContext(), CustomerActivity.class);
                        getBaseContext().startActivity(myIntent);
                        finish();
                    }
                }

            } catch (Exception e) {
                Toast.makeText( getBaseContext(), "Something went wrong...", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
            finally {
//                progressDialog.dismiss();
            }

        }
    }


}
