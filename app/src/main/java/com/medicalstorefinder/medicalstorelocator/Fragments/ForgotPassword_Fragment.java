package com.medicalstorefinder.medicalstorelocator.Fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.medicalstorefinder.medicalstorelocator.Activity.MainActivity;
import com.medicalstorefinder.medicalstorelocator.Constants.Constants;
import com.medicalstorefinder.medicalstorelocator.Constants.CustomToast;
import com.medicalstorefinder.medicalstorelocator.Constants.Utilities;
import com.medicalstorefinder.medicalstorelocator.Constants.Utils1;
import com.medicalstorefinder.medicalstorelocator.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ForgotPassword_Fragment extends Fragment implements OnClickListener {

	private static View view;
	private static EditText emailId;
	private static TextView submit, back;
	String getEmailId;

	public ForgotPassword_Fragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.forgotpassword_layout, container,
				false);
		initViews();
		setListeners();
		return view;
	}

	private void initViews() {
		emailId = (EditText) view.findViewById(R.id.registered_mobile_no);
		submit = (TextView) view.findViewById(R.id.forgot_button);
		back = (TextView) view.findViewById(R.id.backToLoginBtn);

	}

	private void setListeners() {
		back.setOnClickListener(this);
		submit.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.backToLoginBtn:

			new MainActivity().replaceLoginFragment();
			break;

		case R.id.forgot_button:

			submitButtonTask();
			break;

		}

	}

	private void submitButtonTask() {
		getEmailId = emailId.getText().toString();

		Pattern p = Pattern.compile(Utils1.regEx);

		Matcher m = p.matcher(getEmailId);

		if (getEmailId.equals("") || getEmailId.length() == 0)

			new CustomToast().Show_Toast(getActivity(), view,"Please enter your Email Id.");

		else if (!m.find())
			new CustomToast().Show_Toast(getActivity(), view,"Your Email Id is Invalid.");

		else

		new ForgotPassword().execute();
	}

	class ForgotPassword extends AsyncTask<String,String,String>
	{
		protected void onPreExecute() {

		}

		@Override
		protected String doInBackground(String... urls) {
			Utilities utilities = new Utilities( getContext() );

			StringBuilder URL_ACCOUNT_GETPROFILEINFO = new StringBuilder( Constants.API_Account_ForgotPassword );
			URL_ACCOUNT_GETPROFILEINFO.append("?Id=1");
			URL_ACCOUNT_GETPROFILEINFO.append("&pEmailId=" +  getEmailId);

			return utilities.apiCall( URL_ACCOUNT_GETPROFILEINFO.toString());
		}

		public void onPostExecute(String response) {

			try {

				if(response.equals("NO_INTERNET")) {
					Toast.makeText( getContext(), "Check internet connection", Toast.LENGTH_LONG).show();
				}
				else if(response.equals("ERROR") ) {
					Toast.makeText( getContext(), "Something went wrong", Toast.LENGTH_LONG).show();
				}else{
					Toast.makeText( getContext(), "Your Password sent successfully on your Email Id", Toast.LENGTH_LONG).show();

				}

			} catch (Exception e1) {
				Toast.makeText( getContext(), "Something went wrong", Toast.LENGTH_LONG).show();
			}
		}
	}
}