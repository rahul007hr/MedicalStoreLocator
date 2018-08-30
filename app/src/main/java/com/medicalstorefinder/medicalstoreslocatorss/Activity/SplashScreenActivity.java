package com.medicalstorefinder.medicalstoreslocatorss.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.medicalstorefinder.medicalstoreslocatorss.Constants.Constants;
import com.medicalstorefinder.medicalstoreslocatorss.Constants.SharedPreference;
import com.medicalstorefinder.medicalstoreslocatorss.Constants.Utilities;
import com.medicalstorefinder.medicalstoreslocatorss.Models.ApiUser;
import com.medicalstorefinder.medicalstoreslocatorss.R;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SplashScreenActivity extends AppCompatActivity {

    private static final int SPLASH_SCREEN_TIMEOUT = 2000;

    SharedPreference sharedPreference;
    Activity context = this;
    ApiUser apiUser;
    FragmentManager fragmentManager;
    String datas="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreference = new SharedPreference();
        setContentView(R.layout.activity_splash_screen);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.get("keys")!=null) {


            //here can get notification message
            datas = bundle.get("keys").toString();

        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

/*                Intent i = new Intent(getApplicationContext(), CustomerActivity.class);
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
                String userRole = sharedPreference.getValue(getApplication(), Constants.PREF_IS_USER, Constants.PREF_USER_ROLE);

                if(!datas.equalsIgnoreCase("")){


                    Intent intent = new Intent(getApplicationContext(), FirebaseMainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("keys", datas);
                   /* PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0 *//* Request code *//*, intent,
                            PendingIntent.FLAG_ONE_SHOT);*/

                    startActivity(intent);
                    finish();

                }else{

                    if(true){
                        if(userRole.equalsIgnoreCase("customer")){
                            new AuthoriseOTP().execute();
                        }else{
                            new LoginTask().execute();
                        }
                    }else {

                        new AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle)
                                .setTitle("New Updates Available").setIcon(R.drawable.alert_dialog_warning)
                                .setMessage("New upgrades are available, Please upgrade App to continue...!!")
                                .setPositiveButton("confirm", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent playstore = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=app.cricket_accessories.com.cricketaccessories"));
                                        startActivity(playstore);
                                    }
                                }).setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
                    }


                }





            }
        }
    }

    class AuthoriseOTP extends AsyncTask<Void, Void, String> {

        protected void onPreExecute() {
//            progressDialog.show();
        }

        protected String doInBackground(Void... urls) {

            Utilities utilities = new Utilities(getApplication());
            String pass = sharedPreference.getValue(context, Constants.PREF_IS_USER, Constants.PREF_KEY_USER_PASS);
            String address = Constants.API_VERIFY_OTP;
            Map<String, String> params = new HashMap<>();
            params.put("mobile", sharedPreference.getValue( getApplication(), Constants.PREF_IS_USER, Constants.PREF_KEY_USER_PHONE ));
            params.put("otp", pass);
//            params.put("deviceid", sharedPreference.getValue(getBaseContext(), Constants.PREF_IS_USER, Constants.PREF_FIREBASE_USER_TOKEN));

            return utilities.apiCalls(address,params);

        }

        protected void onPostExecute(String response) {
            try {

                if(response.equals("NO_INTERNET")) {
                    Toast.makeText(getApplication(), "Check internet connection", Toast.LENGTH_LONG).show();
                }
                else if(response.equals("ERROR")) {
                    Toast.makeText(getApplication(), "Please enter registered mobile number", Toast.LENGTH_LONG).show();
                }
                else
                {
                    if(response.equals("false"))
                    {
                        Toast.makeText(getApplication(), "Please enter registered mobile number", Toast.LENGTH_LONG).show();
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
//						apiUser.setPasswords( pinEntry.getText().toString());
//						apiUser.setProfilePicUrl(jsonObject.getString("photo"));

//						sharedPreference = new SharedPreference();

                        sharedPreference.putValue(getApplication(), Constants.PREF_IS_USER, Constants.PREF_KEY_USER_ID, String.valueOf(apiUser.getID()));
                        sharedPreference.putValue(getApplication(), Constants.PREF_IS_USER, Constants.PREF_KEY_USER_Email, apiUser.getEmail());
                        sharedPreference.putValue(getApplication(), Constants.PREF_IS_USER, Constants.PREF_KEY_USER_Address, apiUser.getAddress());
                        sharedPreference.putValue(getApplication(), Constants.PREF_IS_USER, Constants.PREF_KEY_USER_SHOP_NAME, apiUser.getShop_Name());
                        sharedPreference.putValue(getApplication(), Constants.PREF_IS_USER, Constants.PREF_KEY_USER_FIRST_NAME, apiUser.getFirst_Name());
                        sharedPreference.putValue(getApplication(), Constants.PREF_IS_USER, Constants.PREF_KEY_USER_LAST_NAME, apiUser.getLast_Name());
                        sharedPreference.putValue(getApplication(), Constants.PREF_IS_USER, Constants.PREF_KEY_USER_PHONE, apiUser.getRegMobile());
                        sharedPreference.putValue(getApplication(), Constants.PREF_IS_USER, Constants.PREF_USER_ROLE, apiUser.getUserRole());
//						sharedPreference.putValue(getBaseContext(), Constants.PREF_IS_USER, Constants.PREF_KEY_USER_PASS, apiUser.getPasswords());
//							sharedPreference.putValue(getContext(), Constants.PREF_IS_USER, Constants.PREF_KEY_USER_ProfilePic, apiUser.getProfilePicUrl());

                        Toast.makeText(getApplication(), "Login Success", Toast.LENGTH_SHORT).show();

                        Intent myIntent = new Intent(getApplication(), CustomerActivity.class);
                        startActivity(myIntent);
                        finish();
                    }
                }

            } catch (Exception e) {
                Toast.makeText( getApplication(), "Please Login Again", Toast.LENGTH_LONG).show();
                e.printStackTrace();
                Intent myIntent = new Intent(getApplication(), MainActivity.class);
                startActivity(myIntent);
                finish();
            }
            finally {
//                progressDialog.dismiss();
            }

        }
    }


    class LoginTask extends AsyncTask<String,String,String> {
        @Override
        protected String doInBackground(String... urls) {
            try {


                if (sharedPreference.isSPKeyExits(context, Constants.PREF_IS_USER, Constants.PREF_KEY_USER_Email) &&
                        sharedPreference.isSPKeyExits(context, Constants.PREF_IS_USER, Constants.PREF_KEY_USER_PHONE) &&
                        sharedPreference.isSPKeyExits(context, Constants.PREF_IS_USER, Constants.PREF_KEY_USER_PASS)) {

                    Utilities utilities = new Utilities(getApplicationContext());

                    String token = FirebaseInstanceId.getInstance().getToken();
                    while(token == null)//this is used to get firebase token until its null so it will save you from null pointer exeption
                    {
                        token = FirebaseInstanceId.getInstance().getToken();
                    }


                    String address = Constants.API_MEDICAL_LOGIN;
                    Map<String, String> params = new HashMap<>();
                    params.put("username", sharedPreference.getValue( context, Constants.PREF_IS_USER, Constants.PREF_KEY_USER_PHONE ));
                    params.put("password", sharedPreference.getValue(context, Constants.PREF_IS_USER, Constants.PREF_KEY_USER_PASS));
                    params.put("loginstatus", "1");
                    params.put("deviceid", token);

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
                    Toast.makeText(getBaseContext(), "Please try again later.", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        }
    }

}
