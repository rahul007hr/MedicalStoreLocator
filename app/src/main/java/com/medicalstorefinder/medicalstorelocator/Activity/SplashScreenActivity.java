package com.medicalstorefinder.medicalstorelocator.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.medicalstorefinder.medicalstorelocator.Constants.Constants;
import com.medicalstorefinder.medicalstorelocator.Constants.SharedPreference;
import com.medicalstorefinder.medicalstorelocator.Constants.Utilities;
import com.medicalstorefinder.medicalstorelocator.Models.ApiUser;
import com.medicalstorefinder.medicalstorelocator.R;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SplashScreenActivity extends AppCompatActivity {

    private static final int SPLASH_SCREEN_TIMEOUT = 2000;

    SharedPreference sharedPreference;
    Activity context = this;
    ApiUser apiUser;
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash_screen);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                new CheckIsStableVersion().execute();

            }
        },SPLASH_SCREEN_TIMEOUT);
    }


    class CheckIsStableVersion extends AsyncTask<String,String,String> {
        @Override
        protected String doInBackground(String... urls) {
            try {
                Utilities utilities = new Utilities(getApplicationContext());

                String address = Constants.API_CHECK_STABLE_VERSION;
                Map<String, String> params = new HashMap<>();
                params.put("username", "bnk");
                // params.put("password", "bnk123");

                return utilities.apiCalls(address,params);
            } catch (Exception e) {
                e.printStackTrace();
                return "ERROR";
            }
        }

        public void onPostExecute(String response) {
            if (response.equals("NO_INTERNET")) {
                Toast.makeText(getBaseContext(), "Check internet connection", Toast.LENGTH_LONG).show();
            } else if (response.equals("ERROR")) {

                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
                finish();
            } else {
                        new LoginTask().execute();
            }
        }
    }

    class LoginTask extends AsyncTask<String,String,String> {
        @Override
        protected String doInBackground(String... urls) {
            try {
                sharedPreference = new SharedPreference();

                if (sharedPreference.isSPKeyExits(context, Constants.PREF_IS_USER, Constants.PREF_KEY_USER_ID) &&
                        sharedPreference.isSPKeyExits(context, Constants.PREF_IS_USER, Constants.PREF_KEY_USER_PHONE) &&
                        sharedPreference.isSPKeyExits(context, Constants.PREF_IS_USER, Constants.PREF_KEY_USER_PASS)) {
                    Utilities utilities = new Utilities(getApplicationContext());

                    StringBuilder URL_IS_AUTHORISED = new StringBuilder(Constants.API_Account_IsAuthorised);
                    URL_IS_AUTHORISED.append("?Id=1");
                    URL_IS_AUTHORISED.append("&pppUserName=" + sharedPreference.getValue( context, Constants.PREF_ISAD, Constants.PREF_KEY_USER_Email ) );
                    URL_IS_AUTHORISED.append("&pppPassword=" + sharedPreference.getValue(context, Constants.PREF_ISAD, Constants.PREF_KEY_USER_PASS));

                    return utilities.apiCall(URL_IS_AUTHORISED.toString());
                } else {
                    return "ERROR";
                }

            } catch (Exception e) {
                e.printStackTrace();
                return "ERROR";
            }
        }

        public void onPostExecute(String response) {
            if (response.equals("NO_INTERNET")) {
                Toast.makeText(getBaseContext(), "Check internet connection", Toast.LENGTH_LONG).show();
            } else if (response.equals("ERROR")) {

                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
                finish();
            } else {
                try {

                    if (response.equals("false")) {
                        Toast.makeText(getBaseContext(), "Login failed, Please enter correct credentials...", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(i);
                        finish();
                    } else {
                        JSONObject jsonObject = new JSONObject(response);

                        apiUser = new ApiUser();

                        apiUser.setID(jsonObject.getInt("Id"));
                        apiUser.setRegMobile(jsonObject.getString("Mobile_Number"));
                        apiUser.setEmail(jsonObject.getString("Email_Id"));
                        apiUser.setcType(jsonObject.getString("cType"));

                        Intent splashIntent = new Intent(getApplicationContext(), UserActivity.class);
                        startActivity(splashIntent);
                        finish();
                    }

                } catch (Exception e) {
                    Toast.makeText(getBaseContext(), "Something went wrong.", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        }
    }

}
