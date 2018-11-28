package com.medicalstorefinder.mychemists.Activity;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.medicalstorefinder.mychemists.BuildConfig;
import com.medicalstorefinder.mychemists.Constants.Constants;
import com.medicalstorefinder.mychemists.Constants.SharedPreference;
import com.medicalstorefinder.mychemists.Constants.Utilities;
import com.medicalstorefinder.mychemists.Models.ApiUser;
import com.medicalstorefinder.mychemists.R;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SplashScreenActivity extends AppCompatActivity {

    private static final int SPLASH_SCREEN_TIMEOUT = 2000;

    SharedPreference sharedPreference;
    Activity context = this;
    ApiUser apiUser;
    FragmentManager fragmentManager;
    String datas = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreference = new SharedPreference();
        setContentView(R.layout.activity_splash_screen);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.get("keys") != null) {
            datas = bundle.get("keys").toString();
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                new CheckIsStableVersion().execute();

            }
        }, SPLASH_SCREEN_TIMEOUT);
        ImageView logo = (ImageView) findViewById(R.id.logo);
        ObjectAnimator animation = ObjectAnimator.ofFloat(logo, "rotationY", 0.0f, 360f);
        animation.setDuration(3600);
        animation.setRepeatCount(ObjectAnimator.INFINITE);
        animation.setInterpolator(new AccelerateDecelerateInterpolator());
        animation.start();


    }


    class CheckIsStableVersion extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... urls) {
            try {
                Utilities utilities = new Utilities(getApplicationContext());
                String address = Constants.API_CHECK_STABLE_VERSION;
                Map<String, String> params = new HashMap<>();
                params.put("username", "bnk");
                return utilities.apiCalls(address, params);
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
                if (!datas.equalsIgnoreCase("")) {
                    Intent intent = new Intent(getApplicationContext(), FirebaseMainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("keys", datas);
                    startActivity(intent);
                    finish();

                } else {
                    try {
                        JSONObject jsonObject1 = new JSONObject(response);
                        JSONObject jsonObject2 = new JSONObject(jsonObject1.getString("Content"));
                        JSONObject jsonObject = new JSONObject(jsonObject2.getString("message"));
                        String versionName = BuildConfig.VERSION_NAME;
                        if ((int) (Float.parseFloat(jsonObject.getString("version"))) <= ((int) (Float.parseFloat(versionName)))) {
                            if (userRole.equalsIgnoreCase("customer")) {
                                new AuthoriseOTP().execute();
                            } else {
                                new LoginTask().execute();
                            }
                        } else {
                            new AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle)
                                    .setTitle("New Updates Available").setIcon(R.drawable.alert_dialog_warning)
                                    .setMessage("New upgrades are available, Please upgrade App to continue...!!")
                                    .setPositiveButton("confirm", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent playstore = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.medicalstorefinder.mychemists"));
                                            startActivity(playstore);
                                            finish();
                                        }
                                    }).setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(getApplication(), "Please Update App !!", Toast.LENGTH_LONG).show();
                                    dialog.dismiss();
                                    finish();
                                }
                            }).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();

                    } finally {
                    }

                }
            }
        }
    }

    class AuthoriseOTP extends AsyncTask<Void, Void, String> {

        protected void onPreExecute() {
        }

        protected String doInBackground(Void... urls) {
            Utilities utilities = new Utilities(getApplication());
            String pass = sharedPreference.getValue(context, Constants.PREF_IS_USER, Constants.PREF_KEY_USER_PASS);
            String address = Constants.API_VERIFY_OTP;
            Map<String, String> params = new HashMap<>();
            params.put("mobile", sharedPreference.getValue(getApplication(), Constants.PREF_IS_USER, Constants.PREF_KEY_USER_PHONE));
            params.put("otp", pass);
            return utilities.apiCalls(address, params);

        }

        protected void onPostExecute(String response) {
            try {
                if (response.equals("NO_INTERNET")) {
                    Toast.makeText(getApplication(), "Check internet connection", Toast.LENGTH_LONG).show();
                } else if (response.equals("ERROR")) {
                    Toast.makeText(getApplication(), "Please enter registered mobile number", Toast.LENGTH_LONG).show();
                } else {
                    if (response.equals("false")) {
                        Toast.makeText(getApplication(), "Please enter registered mobile number", Toast.LENGTH_LONG).show();
                    } else {
                        JSONObject jsonObject1 = new JSONObject(response);
                        JSONObject jsonObject2 = new JSONObject(jsonObject1.getString("Content"));
                        JSONObject jsonObject = new JSONObject(jsonObject2.getString("message"));
                        apiUser = new ApiUser();
                        apiUser.setID(jsonObject.getInt("id"));
                        apiUser.setFirst_Name(jsonObject.getString("firstname"));
                        apiUser.setLast_Name(jsonObject.getString("lastname"));
                        apiUser.setRegMobile(jsonObject.getString("mobile"));
                        String string = jsonObject.getString("address");
                        String[] bits = string.split(",");
                        String lastWord = "";
                        if (bits.length > 2)
                            lastWord = bits[bits.length - 3] + ", " + bits[bits.length - 2] + ", " + bits[bits.length - 1];
                        apiUser.setAddress(lastWord);
                        apiUser.setShop_Name(jsonObject.getString("shopname"));
                        apiUser.setEmail(jsonObject.getString("email"));
                        apiUser.setUserRole(jsonObject.getString("role"));
                        sharedPreference.putValue(getApplication(), Constants.PREF_IS_USER, Constants.PREF_KEY_USER_ID, String.valueOf(apiUser.getID()));
                        sharedPreference.putValue(getApplication(), Constants.PREF_IS_USER, Constants.PREF_KEY_USER_Email, apiUser.getEmail());
                        sharedPreference.putValue(getApplication(), Constants.PREF_IS_USER, Constants.PREF_KEY_USER_Address, apiUser.getAddress());
                        sharedPreference.putValue(getApplication(), Constants.PREF_IS_USER, Constants.PREF_KEY_USER_SHOP_NAME, apiUser.getShop_Name());
                        sharedPreference.putValue(getApplication(), Constants.PREF_IS_USER, Constants.PREF_KEY_USER_FIRST_NAME, apiUser.getFirst_Name());
                        sharedPreference.putValue(getApplication(), Constants.PREF_IS_USER, Constants.PREF_KEY_USER_LAST_NAME, apiUser.getLast_Name());
                        sharedPreference.putValue(getApplication(), Constants.PREF_IS_USER, Constants.PREF_KEY_USER_PHONE, apiUser.getRegMobile());
                        sharedPreference.putValue(getApplication(), Constants.PREF_IS_USER, Constants.PREF_USER_ROLE, apiUser.getUserRole());
                        Toast.makeText(getApplication(), "Login Success", Toast.LENGTH_SHORT).show();
                        Intent myIntent = new Intent(getApplication(), CustomerActivity.class);
                        startActivity(myIntent);
                        finish();
                    }
                }

            } catch (Exception e) {
                Toast.makeText(getApplication(), "Please Login Again", Toast.LENGTH_LONG).show();
                e.printStackTrace();
                Intent myIntent = new Intent(getApplication(), MainActivity.class);
                startActivity(myIntent);
                finish();
            } finally {
            }

        }
    }


    class LoginTask extends AsyncTask<String, String, String> {

        String token = "";

        protected void onPreExecute() {
            token = FirebaseInstanceId.getInstance().getToken();
            while (token == null) {
                token = FirebaseInstanceId.getInstance().getToken();
            }
        }

        @Override
        protected String doInBackground(String... urls) {
            try {
                if (sharedPreference.isSPKeyExits(context, Constants.PREF_IS_USER, Constants.PREF_KEY_USER_Email) &&
                        sharedPreference.isSPKeyExits(context, Constants.PREF_IS_USER, Constants.PREF_KEY_USER_PHONE) &&
                        sharedPreference.isSPKeyExits(context, Constants.PREF_IS_USER, Constants.PREF_KEY_USER_PASS)) {
                    Utilities utilities = new Utilities(getApplicationContext());
                    String address = Constants.API_MEDICAL_LOGIN;
                    Map<String, String> params = new HashMap<>();
                    params.put("username", sharedPreference.getValue(context, Constants.PREF_IS_USER, Constants.PREF_KEY_USER_PHONE));
                    params.put("password", sharedPreference.getValue(context, Constants.PREF_IS_USER, Constants.PREF_KEY_USER_PASS));
                    params.put("loginstatus", "1");
                    params.put("deviceid", token);
                    return utilities.apiCalls(address, params);
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
                    if (jsonObject2.getString("status").equalsIgnoreCase("error")) {
                        Toast.makeText(getBaseContext(), jsonObject2.getString("status"), Toast.LENGTH_LONG).show();
                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(i);
                        finish();
                    } else if (jsonObject2.getString("status").equalsIgnoreCase("success")) {
                        Toast.makeText(getBaseContext(), "Login Success", Toast.LENGTH_LONG).show();
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
