package com.medicalstorefinder.medicalstoreslocatorss.Fragments;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.medicalstorefinder.medicalstoreslocatorss.Activity.CustomerActivity;
import com.medicalstorefinder.medicalstoreslocatorss.Activity.OtpVerificationActivity;
import com.medicalstorefinder.medicalstoreslocatorss.Activity.UserActivity;
import com.medicalstorefinder.medicalstoreslocatorss.Constants.Constants;
import com.medicalstorefinder.medicalstoreslocatorss.Constants.CustomToast;
import com.medicalstorefinder.medicalstoreslocatorss.Constants.SharedPreference;
import com.medicalstorefinder.medicalstoreslocatorss.Constants.Utilities;
import com.medicalstorefinder.medicalstoreslocatorss.Constants.Utils1;
import com.medicalstorefinder.medicalstoreslocatorss.Models.ApiUser;
import com.medicalstorefinder.medicalstoreslocatorss.R;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.medicalstorefinder.medicalstoreslocatorss.Constants.Constants.PROFILE_IMAGE_PATH;

public class Login_Fragment extends Fragment implements OnClickListener {
	private static View view;

	private static EditText mobile, password;
	private static Button loginButton;
	private static TextView forgotPassword, signUp;
	private static CheckBox show_hide_password;
	private static LinearLayout loginLayout;
	private static Animation shakeAnimation;
	private static FragmentManager fragmentManager;

	String getMobileNo,getPassword;
	ApiUser apiUser;
	SharedPreference sharedPreference;
	ProgressDialog progressDialog;
	private static final int REQUEST_PERMISSIONS = 20;
	public Login_Fragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.login_layout, container, false);
		sharedPreference = new SharedPreference();
		initViews();
		setListeners();
		progressDialog = new ProgressDialog(getContext());
		progressDialog.setMessage("Loading...");
		progressDialog.setCancelable(false);
		progressDialog.setCanceledOnTouchOutside(false);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			ActivityCompat.requestPermissions(getActivity(),
					new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE, Manifest.permission.CALL_PRIVILEGED, Manifest.permission.CALL_PHONE, Manifest.permission.READ_CONTACTS, Manifest.permission.ACCESS_FINE_LOCATION},
					1);
		}
		return view;
	}
	@Override
	public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
		switch (requestCode) {
			case 1: {

				// If request is cancelled, the result arrays are empty.
				if (grantResults.length > 0
						&& grantResults[0] == PackageManager.PERMISSION_GRANTED) {

					// permission was granted, yay! Do the
					// contacts-related task you need to do.
				} else {

					// permission denied, boo! Disable the
					// functionality that depends on this permission.
					Toast.makeText(getContext(), "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
				}
				return;
			}

			// other 'case' lines to check for other
			// permissions this app might request
		}
	}

	private void initViews() {
		fragmentManager = getActivity().getSupportFragmentManager();

		mobile = (EditText) view.findViewById(R.id.login_mobile_no);
		password = (EditText) view.findViewById(R.id.login_password);
		loginButton = (Button) view.findViewById(R.id.loginBtn);
		forgotPassword = (TextView) view.findViewById(R.id.forgot_password);
		signUp = (TextView) view.findViewById(R.id.createAccount);
		show_hide_password = (CheckBox) view
				.findViewById(R.id.show_hide_password);
		loginLayout = (LinearLayout) view.findViewById(R.id.login_layout);

		shakeAnimation = AnimationUtils.loadAnimation(getActivity(),
				R.anim.shake);
		if (sharedPreference.getValue( getActivity(), Constants.PREF_USER_ROLE, Constants.PREF_USER_ROLE ).equalsIgnoreCase("customer")){
			loginButton.setText("Send OTP");
			forgotPassword.setVisibility(View.GONE);
			show_hide_password.setVisibility(View.GONE);
			password.setVisibility(View.GONE);
			signUp.setVisibility(View.GONE);
		}else{
			loginButton.setText("LOGIN");
			forgotPassword.setVisibility(View.VISIBLE);
			show_hide_password.setVisibility(View.VISIBLE);
			password.setVisibility(View.VISIBLE);
			signUp.setVisibility(View.VISIBLE);
		}

	}

	private void setListeners() {
		loginButton.setOnClickListener(this);
		forgotPassword.setOnClickListener(this);
		signUp.setOnClickListener(this);

		show_hide_password
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton button,
							boolean isChecked) {

						if (isChecked) {

							show_hide_password.setText(R.string.hide_pwd);

							password.setInputType(InputType.TYPE_CLASS_TEXT);
							password.setTransformationMethod(HideReturnsTransformationMethod
									.getInstance());
						} else {
							show_hide_password.setText(R.string.show_pwd);

							password.setInputType(InputType.TYPE_CLASS_TEXT
									| InputType.TYPE_TEXT_VARIATION_PASSWORD);
							password.setTransformationMethod(PasswordTransformationMethod
									.getInstance());// hide password
						}
					}
				});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.loginBtn:
			if (sharedPreference.getValue( getActivity(), Constants.PREF_USER_ROLE, Constants.PREF_USER_ROLE ).equalsIgnoreCase("customer")){

				checkValidationForCustomer();

				Intent myIntent = new Intent(getActivity(), OtpVerificationActivity.class);
				getActivity().startActivity(myIntent);
				getActivity().finish();

			}else{
				checkValidation();
			}
			/*Uri.Builder builder = new Uri.Builder();
			builder.scheme("https")
					.authority("www.google.com").appendPath("maps").appendPath("dir").appendPath("").appendQueryParameter("api", "1")
					.appendQueryParameter("destination", 18.590388700000002 + "," + 73.8204423);
			String url = builder.build().toString();
			Log.d("Directions", url);
			Intent i = new Intent(Intent.ACTION_VIEW);
			i.setData(Uri.parse(url));
			startActivity(i);*/

			break;

		case R.id.forgot_password:

			fragmentManager
					.beginTransaction()
					.setCustomAnimations(R.anim.right_enter, R.anim.left_out)
					.replace(R.id.frameContainer,
							new ForgotPassword_Fragment(),
							Utils1.ForgotPassword_Fragment).commit();
			break;
		case R.id.createAccount:

			fragmentManager
					.beginTransaction()
					.setCustomAnimations(R.anim.right_enter, R.anim.left_out)
					.replace(R.id.frameContainer, new SignUp_Fragment(),
							Utils1.SignUp_Fragment).commit();
			break;
		}
	}

	class AuthoriseUser extends AsyncTask<Void, Void, String> {

		protected void onPreExecute() {
			progressDialog.show();
		}

		protected String doInBackground(Void... urls) {

			Utilities utilities = new Utilities(getContext());

			String address = Constants.API_MEDICAL_LOGIN;
			Map<String, String> params = new HashMap<>();
			params.put("username", getMobileNo);
			params.put("password", getPassword);
			params.put("loginstatus", "1");

			return utilities.apiCalls(address,params);

		}

		protected void onPostExecute(String response) {
			try {

				JSONObject jsonObject1 = new JSONObject(response);
				JSONObject jsonObject2 = new JSONObject(jsonObject1.getString("Content"));

//				{"Content":"{\"status\":\"success\",\"result\":{\"id\":\"28\",\"firstname\":\"Mangesh\",\"lastname\":\"Khalale\",\"shopname\":\"Mango\",\"email\":\"mangesh@gmail.com\",\"password\":\"111\",\"mobile\":\"8793377994\",\"address\":\"Pathardi Phata,Pathardi Phata, Nashik, Maharashtra, India\",\"latitude\":\"19.946922\",\"longitude\":\"73.7654367\",\"profilepic\":\"no_avatar.jpg\",\"role\":\"medical\",\"regdate\":\"2018-07-27 10:44:22\",\"status\":\"0\",\"deletestatus\":null,\"loginstatus\":\"1\",\"otp\":null}}","Message":"OK","Length":-1,"Type":"text\/html; charset=UTF-8"}
				if(response.equals("NO_INTERNET")) {
					Toast.makeText(getContext(), "Check internet connection", Toast.LENGTH_LONG).show();
				}
				else if(jsonObject2.getString("status").equalsIgnoreCase("error")) {
					Toast.makeText(getContext(), jsonObject2.getString("message"), Toast.LENGTH_LONG).show();
				}
				else
				{
					if(jsonObject2.getString("status").equalsIgnoreCase("error")) {
						Toast.makeText(getContext(), jsonObject2.getString("message"), Toast.LENGTH_LONG).show();
					}
					else if(jsonObject2.getString("status").equalsIgnoreCase("success")) {
						Toast.makeText(getContext(), jsonObject2.getString("status"), Toast.LENGTH_LONG).show();

						JSONObject jsonObject = new JSONObject(jsonObject2.getString("result"));
//						JSONObject jsonObject = jsonarray.getJSONObject(1);

						apiUser = new ApiUser();

						apiUser.setID(jsonObject.getInt("id"));
						apiUser.setFirst_Name(jsonObject.getString("firstname"));
						apiUser.setLast_Name(jsonObject.getString("lastname"));
						apiUser.setRegMobile(jsonObject.getString("mobile"));
						apiUser.setAddress(jsonObject.getString("address"));
						apiUser.setShop_Name(jsonObject.getString("shopname"));
						apiUser.setEmail(jsonObject.getString("email"));
						apiUser.setPasswords(jsonObject.getString("password"));
						apiUser.setUserRole(jsonObject.getString("role"));
						String s = PROFILE_IMAGE_PATH+jsonObject.getInt("id")+"/"+jsonObject.getInt("id")+".jpg";

						apiUser.setProfilePicUrl(s);



							sharedPreference.clearSharedPreference(getContext(), Constants.PREF_IS_USER);
							sharedPreference.createSharedPreference(getActivity(), Constants.PREF_IS_USER);

							sharedPreference.putValue(getContext(), Constants.PREF_IS_USER, Constants.PREF_KEY_USER_ID, String.valueOf(apiUser.getID()));
							sharedPreference.putValue(getContext(), Constants.PREF_IS_USER, Constants.PREF_KEY_USER_Email, apiUser.getEmail());
							sharedPreference.putValue(getContext(), Constants.PREF_IS_USER, Constants.PREF_KEY_USER_PASS, getPassword);
							sharedPreference.putValue(getContext(), Constants.PREF_IS_USER, Constants.PREF_KEY_USER_PHONE, getMobileNo);
							sharedPreference.putValue(getContext(), Constants.PREF_IS_USER, Constants.PREF_KEY_USER_Address, apiUser.getAddress());
							sharedPreference.putValue(getContext(), Constants.PREF_IS_USER, Constants.PREF_KEY_USER_SHOP_NAME, apiUser.getShop_Name());
							sharedPreference.putValue(getContext(), Constants.PREF_IS_USER, Constants.PREF_KEY_USER_FIRST_NAME, apiUser.getFirst_Name());
							sharedPreference.putValue(getContext(), Constants.PREF_IS_USER, Constants.PREF_KEY_USER_LAST_NAME, apiUser.getLast_Name());
							sharedPreference.putValue(getContext(), Constants.PREF_IS_USER, "medica", apiUser.getUserRole());
							sharedPreference.putValue(getContext(), Constants.PREF_IS_USER, Constants.PREF_KEY_USER_ProfilePic, apiUser.getProfilePicUrl());

								Intent myIntent = new Intent(getActivity(), UserActivity.class);
								startActivity(myIntent);
								getActivity().finish();
					}
				}

			} catch (Exception e) {
				Toast.makeText( getContext(), "Please try again later...", Toast.LENGTH_LONG).show();
				e.printStackTrace();
			}
			finally {
				progressDialog.dismiss();
			}

		}
	}

	private void checkValidation() {

		getMobileNo = mobile.getText().toString();
		getPassword = password.getText().toString();

		if (getMobileNo.equals("") || getMobileNo.length() == 0
				|| getPassword.equals("") || getPassword.length() == 0) {
			loginLayout.startAnimation(shakeAnimation);
			new CustomToast().Show_Toast(getActivity(), view,"Enter both credentials.");
		}
		else {
			new AuthoriseUser().execute();
		}
	}

	private void checkValidationForCustomer() {

		getMobileNo = mobile.getText().toString();
		getPassword = sharedPreference.getValue(getActivity(), Constants.PREF_IS_USER, Constants.PREF_KEY_USER_PASS);

		if (getMobileNo.equals("") || getMobileNo.length() == 0) {
			loginLayout.startAnimation(shakeAnimation);
			new CustomToast().Show_Toast(getActivity(), view,"Enter Mobile Number");
		}else if (getPassword.equals("") || getPassword.length() == 0) {
			new AuthoriseCustomer().execute();
		}else {
			new AuthoriseOTP().execute();
		}
	}
	class AuthoriseOTP extends AsyncTask<Void, Void, String> {

		protected void onPreExecute() {
//            progressDialog.show();
		}

		protected String doInBackground(Void... urls) {

			Utilities utilities = new Utilities(getActivity());

			String address = Constants.API_VERIFY_OTP;
			Map<String, String> params = new HashMap<>();
			params.put("mobile", sharedPreference.getValue( getActivity(), Constants.PREF_IS_USER, Constants.PREF_KEY_USER_PHONE ));
			params.put("otp", getPassword);

			return utilities.apiCalls(address,params);

		}

		protected void onPostExecute(String response) {
			try {

				if(response.equals("NO_INTERNET")) {
					Toast.makeText(getActivity(), "Check internet connection", Toast.LENGTH_LONG).show();
				}
				else if(response.equals("ERROR")) {
					Toast.makeText(getActivity(), "Please enter registered mobile number", Toast.LENGTH_LONG).show();
				}
				else
				{
					if(response.equals("false"))
					{
						Toast.makeText(getActivity(), "Please enter registered mobile number", Toast.LENGTH_LONG).show();
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
						apiUser.setPasswords( getPassword);
//						apiUser.setProfilePicUrl(jsonObject.getString("photo"));

//						sharedPreference = new SharedPreference();

						sharedPreference.putValue(getActivity(), Constants.PREF_IS_USER, Constants.PREF_KEY_USER_ID, String.valueOf(apiUser.getID()));
						sharedPreference.putValue(getActivity(), Constants.PREF_IS_USER, Constants.PREF_KEY_USER_Email, apiUser.getEmail());
						sharedPreference.putValue(getActivity(), Constants.PREF_IS_USER, Constants.PREF_KEY_USER_Address, apiUser.getAddress());
						sharedPreference.putValue(getActivity(), Constants.PREF_IS_USER, Constants.PREF_KEY_USER_SHOP_NAME, apiUser.getShop_Name());
						sharedPreference.putValue(getActivity(), Constants.PREF_IS_USER, Constants.PREF_KEY_USER_FIRST_NAME, apiUser.getFirst_Name());
						sharedPreference.putValue(getActivity(), Constants.PREF_IS_USER, Constants.PREF_KEY_USER_LAST_NAME, apiUser.getLast_Name());
						sharedPreference.putValue(getActivity(), Constants.PREF_IS_USER, Constants.PREF_KEY_USER_PHONE, apiUser.getRegMobile());
						sharedPreference.putValue(getActivity(), Constants.PREF_IS_USER, Constants.PREF_USER_ROLE, apiUser.getUserRole());
						sharedPreference.putValue(getActivity(), Constants.PREF_IS_USER, Constants.PREF_KEY_USER_PASS, apiUser.getPasswords());
//							sharedPreference.putValue(getContext(), Constants.PREF_IS_USER, Constants.PREF_KEY_USER_ProfilePic, apiUser.getProfilePicUrl());

						Toast.makeText(getActivity(), "Login Success", Toast.LENGTH_SHORT).show();

						Intent myIntent = new Intent(getActivity(), CustomerActivity.class);
						startActivity(myIntent);
						getActivity().finish();
					}
				}

			} catch (Exception e) {
				Toast.makeText( getActivity(), "Please try again later...", Toast.LENGTH_LONG).show();
				e.printStackTrace();
			}
			finally {
//                progressDialog.dismiss();
			}

		}
	}

	class AuthoriseCustomer extends AsyncTask<Void, Void, String> {

		protected void onPreExecute() {
			progressDialog.show();
		}

		protected String doInBackground(Void... urls) {

			Utilities utilities = new Utilities(getContext());

			String address = Constants.API_CUSTOMER_LOGIN;
			Map<String, String> params = new HashMap<>();
			params.put("mobile", getMobileNo);

			return utilities.apiCalls(address,params);

		}

		protected void onPostExecute(String response) {
			try {

				JSONObject jsonObject1 = new JSONObject(response);
				JSONObject jsonObject2 = new JSONObject(jsonObject1.getString("Content"));

				if(response.equals("NO_INTERNET")) {
					Toast.makeText(getContext(), "Check internet connection", Toast.LENGTH_LONG).show();
				}
				else if(jsonObject2.getString("status").equalsIgnoreCase("error"))
				{
					Toast.makeText(getContext(), jsonObject2.getString("message"), Toast.LENGTH_LONG).show();
				}
				else
				{
					if(jsonObject2.getString("status").equalsIgnoreCase("error"))
					{
						Toast.makeText(getContext(), jsonObject2.getString("message"), Toast.LENGTH_LONG).show();
					}
					else {
						String s = jsonObject2.getString("status");
					if(s.equalsIgnoreCase("success")){
						Intent myIntent = new Intent(getActivity(), OtpVerificationActivity.class);
						startActivity(myIntent);
						getActivity().finish();
//						sharedPreference.putValue(getContext(), Constants.PREF_IS_USER, Constants.PREF_KEY_USER_ID, String.valueOf(apiUser.getID()));
						sharedPreference.putValue(getContext(), Constants.PREF_IS_USER, Constants.PREF_KEY_USER_PHONE, getMobileNo);
						Toast.makeText(getContext(), jsonObject2.getString("message"), Toast.LENGTH_LONG).show();
					}

					}
				}

			} catch (Exception e) {
				Toast.makeText( getContext(), "Please try again later...", Toast.LENGTH_LONG).show();
				e.printStackTrace();
			}
			finally {
				progressDialog.dismiss();
			}

		}
	}

}