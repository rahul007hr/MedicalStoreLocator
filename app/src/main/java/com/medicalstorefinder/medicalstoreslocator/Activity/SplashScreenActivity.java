package com.medicalstorefinder.medicalstoreslocator.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.medicalstorefinder.medicalstoreslocator.Constants.Constants;
import com.medicalstorefinder.medicalstoreslocator.Constants.SharedPreference;
import com.medicalstorefinder.medicalstoreslocator.Constants.Utilities;
import com.medicalstorefinder.medicalstoreslocator.Models.ApiUser;
import com.medicalstorefinder.medicalstoreslocator.R;

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

               /* Intent i = new Intent(getApplicationContext(), UserActivity.class);
                startActivity(i);
                finish();*/

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

                if (sharedPreference.isSPKeyExits(context, Constants.PREF_IS_USER, Constants.PREF_KEY_USER_Email) &&
                        sharedPreference.isSPKeyExits(context, Constants.PREF_IS_USER, Constants.PREF_KEY_USER_PHONE) &&
                        sharedPreference.isSPKeyExits(context, Constants.PREF_IS_USER, Constants.PREF_KEY_USER_PASS)) {

                    Utilities utilities = new Utilities(getApplicationContext());

                    String address = Constants.API_MEDICAL_LOGIN;
                    Map<String, String> params = new HashMap<>();
                    params.put("username", sharedPreference.getValue( context, Constants.PREF_IS_USER, Constants.PREF_KEY_USER_PHONE ));
                    params.put("password", sharedPreference.getValue(context, Constants.PREF_IS_USER, Constants.PREF_KEY_USER_PASS));
                    params.put("loginstatus", "1");

                    return utilities.apiCalls(address,params);
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

                    JSONObject jsonObject1 = new JSONObject(response);
                    JSONObject jsonObject2 = new JSONObject(jsonObject1.getString("Content"));
//


                    if (jsonObject2.getString("status").equalsIgnoreCase("error")) {
                        Toast.makeText(getBaseContext(), jsonObject2.getString("status"), Toast.LENGTH_LONG).show();
                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(i);
                        finish();
                    } else  if (jsonObject2.getString("status").equalsIgnoreCase("success")) {

                        Toast.makeText(getBaseContext(), jsonObject2.getString("status"), Toast.LENGTH_LONG).show();
                        JSONObject jsonObject = new JSONObject(response);
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
