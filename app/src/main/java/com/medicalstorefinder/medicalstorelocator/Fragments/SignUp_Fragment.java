package com.medicalstorefinder.medicalstorelocator.Fragments;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.medicalstorefinder.medicalstorelocator.Activity.MainActivity;
import com.medicalstorefinder.medicalstorelocator.Constants.Constants;
import com.medicalstorefinder.medicalstorelocator.Constants.CustomToast;
import com.medicalstorefinder.medicalstorelocator.Constants.Utilities;
import com.medicalstorefinder.medicalstorelocator.Constants.Utils1;
import com.medicalstorefinder.medicalstorelocator.Models.ApiUser;
import com.medicalstorefinder.medicalstorelocator.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import it.sauronsoftware.ftp4j.FTPAbortedException;
import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferException;
import it.sauronsoftware.ftp4j.FTPDataTransferListener;
import it.sauronsoftware.ftp4j.FTPException;
import it.sauronsoftware.ftp4j.FTPIllegalReplyException;

public class SignUp_Fragment extends Fragment implements OnClickListener {
	private static View view;
	private static EditText fullName, emailId, lastName,mobileNumber, location,
			password, confirmPassword;
	private static TextView login;
	private static Button signUpButton;
	private static CheckBox terms_conditions;

	private static FragmentManager fragmentManager;
	int pos;
	ApiUser user = new ApiUser();
	String date;
	public String res="";
	String name;

	ProgressDialog progressDialog;

	public SignUp_Fragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.signup_layout, container, false);
		initViews();
		date= new SimpleDateFormat("dd/MM/yyyy kk:mm:ss", Locale.getDefault()).format(Calendar.getInstance().getTime());

		progressDialog = new ProgressDialog(getActivity());
		progressDialog.setMessage("Loading...");
		progressDialog.setCancelable(false);
		progressDialog.setCanceledOnTouchOutside(false);
		setListeners();

		return view;
	}

	// Initialize all views
	private void initViews() {
		fullName = (EditText) view.findViewById(R.id.firstName);
		emailId = (EditText) view.findViewById(R.id.userEmailId);
		mobileNumber = (EditText) view.findViewById(R.id.mobileNumber);
		lastName = (EditText) view.findViewById(R.id.lastName);
		location = (EditText) view.findViewById(R.id.shop_name);
		password = (EditText) view.findViewById(R.id.password);
		confirmPassword = (EditText) view.findViewById(R.id.confirmPassword);
		signUpButton = (Button) view.findViewById(R.id.signUpBtn);
		login = (TextView) view.findViewById(R.id.already_user);
		terms_conditions = (CheckBox) view.findViewById(R.id.terms_conditions);

	}

	// Set Listeners
	private void setListeners() {
		signUpButton.setOnClickListener(this);
		login.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.signUpBtn:

// Call checkValidation method
			checkValidation();

			user.setFull_Name(fullName.getText().toString());
			user.setEmail(emailId.getText().toString());
			user.setRegMobile(mobileNumber.getText().toString());
			user.setLocation(location.getText().toString());
			user.setPasswords(password.getText().toString());

			user.setcStatus("Online");
			user.setProfilePicUrl("null");
			user.setAdhar_Card_Number(lastName.getText().toString());
			break;

		case R.id.already_user:

			// Replace login fragment
			new MainActivity().replaceLoginFragment();
			break;
		}

	}

	// Check Validation Method
	private void checkValidation() {

		// Get all edittext texts
		String getFullName = fullName.getText().toString();
		String getEmailId = emailId.getText().toString();
		String getMobileNumber = mobileNumber.getText().toString();
		String getLocation = location.getText().toString();
		String getPassword = password.getText().toString();
		String getConfirmPassword = confirmPassword.getText().toString();
		String getLastName=lastName.getText().toString();
		name=getEmailId;
		// Pattern match for email id
		Pattern p = Pattern.compile(Utils1.regEx);
		Matcher m = p.matcher(getEmailId);

		// Check if all strings are null or not
		if (getFullName.equals("") || getFullName.length() == 0
				|| getEmailId.equals("") || getEmailId.length() == 0
				|| getLastName.equals("") || getEmailId.length() == 0
				|| getMobileNumber.equals("") || getMobileNumber.length() == 0
				|| getLocation.equals("") || getLocation.length() == 0
				|| getPassword.equals("") || getPassword.length() == 0
				|| getConfirmPassword.equals("")
				|| getConfirmPassword.length() == 0)

			new CustomToast().Show_Toast(getActivity(), view,
					"All fields are required.");

		// Check if email id valid or not
		else if (!m.find())
			new CustomToast().Show_Toast(getActivity(), view,
					"Your Email Id is Invalid.");

		// Check if both password should be equal
		else if (!getConfirmPassword.equals(getPassword))
			new CustomToast().Show_Toast(getActivity(), view,
					"Both password doesn't match.");

		// Make sure user should check Terms and Conditions checkbox
		else if (!terms_conditions.isChecked())
			new CustomToast().Show_Toast(getActivity(), view,
					"Please select Terms and Conditions.");

		// Else do signup or do your stuff
		else
//			Toast.makeText(getActivity(), "Do SignUp.", Toast.LENGTH_SHORT)
//					.show();


		new SetSignup().execute();


	}


	public final class SetSignup extends AsyncTask<String,String,String>
	{



		protected void onPreExecute() {
			try {

				progressDialog.show();


			} catch (Exception e1) {

//                Toast.makeText( getContext(), "Something went wrong config 1", Toast.LENGTH_LONG).show();

			}
			finally {

			}

		}

		@Override
		protected String doInBackground(String... urls) {
			StringBuilder URL_SETUSERINFO = new StringBuilder( Constants.API_SetUserInfo );
//            Log.d(String.valueOf(URL_SETCONFIGURATIONINFO), "doInBackground: ");

			URL_SETUSERINFO.append("?Id=1");
			URL_SETUSERINFO.append("&Full_Name=" +user.getFull_Name());
			URL_SETUSERINFO.append("&Email_Id=" +user.getEmail());
			URL_SETUSERINFO.append("&Mobile_Number=" +user.getRegMobile());
			URL_SETUSERINFO.append("&Location=" +user.getLocation());
			URL_SETUSERINFO.append("&Passwords=" +user.getPasswords());
			URL_SETUSERINFO.append("&cType=" +user.getcType());
			URL_SETUSERINFO.append("&Type_of_Service=" +user.getType_of_Service());
			URL_SETUSERINFO.append("&cStatus=" +user.getcStatus());
			URL_SETUSERINFO.append("&Activated=" +user.getActivated());
			URL_SETUSERINFO.append("&photo=" +user.getProfilePicUrl());
			URL_SETUSERINFO.append("&Adhar_Card_Number=" +user.getAdhar_Card_Number());
			URL_SETUSERINFO.append("&Created_Date=" +date);

			Log.d(String.valueOf(URL_SETUSERINFO), "doInBackground: ");

			Utilities utilities = new Utilities( getContext() );
			return utilities.apiCall( URL_SETUSERINFO.toString());

//			return null;
		}

		public void onPostExecute(String response) {

			try {

				if(response.equals("ERROR") ) {
//                    Toast.makeText(getContext(), "Something went wrong config 2", Toast.LENGTH_LONG).show();
				}else if(response.equals("false") ) {
                    Toast.makeText(getContext(), "Email ID Already Exist", Toast.LENGTH_LONG).show();
				}
				else {
					Log.d(response, "onPostExecute:hey hi ");
//                    Toast.makeText(getContext(), "Settings Saved", Toast.LENGTH_LONG).show();
					fragmentManager = getActivity().getSupportFragmentManager();

					fragmentManager
							.beginTransaction()
							.setCustomAnimations(R.anim.right_enter, R.anim.left_out)
							.replace(R.id.frameContainer, new Login_Fragment(),
									Utils1.Login_Fragment).commit();

				}

			} catch (Exception e1) {

//                Toast.makeText( getContext(), "Something went wrong config 3", Toast.LENGTH_LONG).show();

			}
			finally {
				progressDialog.dismiss();

			}

		}
	}
}
