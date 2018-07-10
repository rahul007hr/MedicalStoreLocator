package com.medicalstorefinder.medicalstorelocator.Fragments;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
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

import com.medicalstorefinder.medicalstorelocator.Activity.UserActivity;
import com.medicalstorefinder.medicalstorelocator.Constants.Constants;
import com.medicalstorefinder.medicalstorelocator.Constants.CustomToast;
import com.medicalstorefinder.medicalstorelocator.Constants.SharedPreference;
import com.medicalstorefinder.medicalstorelocator.Constants.Utilities;
import com.medicalstorefinder.medicalstorelocator.Constants.Utils1;
import com.medicalstorefinder.medicalstorelocator.Models.ApiUser;
import com.medicalstorefinder.medicalstorelocator.R;

import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Login_Fragment extends Fragment implements OnClickListener {
	private static View view;

	private static EditText emailid, password;
	private static Button loginButton;
	private static TextView forgotPassword, signUp;
	private static CheckBox show_hide_password;
	private static LinearLayout loginLayout;
	private static Animation shakeAnimation;
	private static FragmentManager fragmentManager;

	String getEmailId,getPassword;
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
		initViews();
		setListeners();
		progressDialog = new ProgressDialog(getContext());
		progressDialog.setMessage("Loading...");
		progressDialog.setCancelable(false);
		progressDialog.setCanceledOnTouchOutside(false);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			ActivityCompat.requestPermissions(getActivity(),
					new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE, Manifest.permission.CALL_PRIVILEGED, Manifest.permission.CALL_PHONE, Manifest.permission.READ_CONTACTS},
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

		emailid = (EditText) view.findViewById(R.id.login_emailid);
		password = (EditText) view.findViewById(R.id.login_password);
		loginButton = (Button) view.findViewById(R.id.loginBtn);
		forgotPassword = (TextView) view.findViewById(R.id.forgot_password);
		signUp = (TextView) view.findViewById(R.id.createAccount);
		show_hide_password = (CheckBox) view
				.findViewById(R.id.show_hide_password);
		loginLayout = (LinearLayout) view.findViewById(R.id.login_layout);

		shakeAnimation = AnimationUtils.loadAnimation(getActivity(),
				R.anim.shake);
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

			checkValidation();
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

			Utilities utilities = new Utilities(   getContext() );

			StringBuilder URL_IS_AUTHORISED = new StringBuilder(Constants.API_Account_IsAuthorised);
			URL_IS_AUTHORISED.append("?Id=1");
			URL_IS_AUTHORISED.append("&pppUserName=" + getEmailId );
			URL_IS_AUTHORISED.append("&pppPassword=" + getPassword );

			return utilities.apiCall( URL_IS_AUTHORISED.toString());
		}

		protected void onPostExecute(String response) {
			try {

				if(response.equals("NO_INTERNET")) {
					Toast.makeText(getContext(), "Check internet connection", Toast.LENGTH_LONG).show();
				}
				else if(response.equals("ERROR")) {
					Toast.makeText(getContext(), "Login failed, Please enter correct credentials.", Toast.LENGTH_LONG).show();
				}
				else
				{
					if(response.equals("false"))
					{
						Toast.makeText(getContext(), "Login failed, Please enter correct credentials.", Toast.LENGTH_LONG).show();
					}
					else {
						JSONObject jsonObject = new JSONObject(response);

						apiUser = new ApiUser();

						apiUser.setID(jsonObject.getInt("Id"));
						apiUser.setFull_Name(jsonObject.getString("Full_Name"));
						apiUser.setRegMobile(jsonObject.getString("Mobile_Number"));
						apiUser.setEmail(jsonObject.getString("Email_Id"));
						apiUser.setcStatus(jsonObject.getString("cStatus"));
						apiUser.setcType(jsonObject.getString("cType"));
						apiUser.setPasswords(jsonObject.getString("Passwords"));
						apiUser.setProfilePicUrl(jsonObject.getString("photo"));

							sharedPreference = new SharedPreference();

							sharedPreference.clearSharedPreference(getContext(), Constants.PREF_ISAD);

							sharedPreference.createSharedPreference(getActivity(), Constants.PREF_ISAD);

							sharedPreference.putValue(getContext(), Constants.PREF_ISAD, Constants.PREF_KEY_USER_ID, String.valueOf(apiUser.getID()));
							sharedPreference.putValue(getContext(), Constants.PREF_ISAD, Constants.PREF_KEY_USER_Email, getEmailId);
							sharedPreference.putValue(getContext(), Constants.PREF_ISAD, Constants.PREF_KEY_USER_PASS, getPassword);
							sharedPreference.putValue(getContext(), Constants.PREF_ISAD, Constants.PREF_KEY_USER_RegMobile, apiUser.getRegMobile());
							sharedPreference.putValue(getContext(), Constants.PREF_ISAD, Constants.PREF_KEY_USER_Email, apiUser.getEmail());
							sharedPreference.putValue(getContext(), Constants.PREF_ISAD, Constants.PREF_KEY_USER_NAME, apiUser.getFull_Name());
							sharedPreference.putValue(getContext(), Constants.PREF_ISAD, Constants.PREF_KEY_USER_ProfilePic, apiUser.getProfilePicUrl());

//							Toast.makeText(getActivity(), "Do Login.", Toast.LENGTH_SHORT).show();

							if (apiUser.getcType().equals("Admin")) {
								/*Intent myIntent = new Intent(getActivity(), AdminActivity.class);
								getActivity().startActivity(myIntent);*/

							} else if (apiUser.getcType().equals("User")) {

								Intent myIntent = new Intent(getActivity(), UserActivity.class);
								getActivity().startActivity(myIntent);

							} else if (apiUser.getcType().equals("Service Provider")) {

								/*Intent myIntent = new Intent(getActivity(), ServiceProviderActivity.class);
								getActivity().startActivity(myIntent);*/

							}
							getActivity().finish();
					}
				}

			} catch (Exception e) {
				Toast.makeText( getContext(), "Something went wrong...", Toast.LENGTH_LONG).show();
				e.printStackTrace();
			}
			finally {
				progressDialog.dismiss();
			}

		}
	}

	private void checkValidation() {

		getEmailId = emailid.getText().toString();
		getPassword = password.getText().toString();

		Pattern p = Pattern.compile(Utils1.regEx);

		Matcher m = p.matcher(getEmailId);

		if (getEmailId.equals("") || getEmailId.length() == 0
				|| getPassword.equals("") || getPassword.length() == 0) {
			loginLayout.startAnimation(shakeAnimation);
			new CustomToast().Show_Toast(getActivity(), view,"Enter both credentials.");
		}
		else if (!m.find())
			new CustomToast().Show_Toast(getActivity(), view,"Your Email Id is Invalid.");
		else {

			new AuthoriseUser().execute();

		}
	}
}