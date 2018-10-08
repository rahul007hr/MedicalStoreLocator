package com.medicalstorefinder.medicalstoreslocatorss.Fragments;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.medicalstorefinder.medicalstoreslocatorss.Constants.Constants;
import com.medicalstorefinder.medicalstoreslocatorss.Constants.CustomToast;
import com.medicalstorefinder.medicalstoreslocatorss.Constants.SharedPreference;
import com.medicalstorefinder.medicalstoreslocatorss.Constants.Utilities;
import com.medicalstorefinder.medicalstoreslocatorss.R;

public class ChangePassword_Fragment extends Fragment implements OnClickListener {
	private static View view;

	private static EditText currentpassword,newpassword,confirmnewpassword;
	private static TextView submit;
	ProgressDialog progressDialog;
	String getNewPassword;

	public ChangePassword_Fragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.changepassword_layout, container,false);
		initViews();
		setListeners();

		progressDialog = new ProgressDialog(getActivity());
		progressDialog.setMessage("Loading...");
		progressDialog.setCancelable(false);
		progressDialog.setCanceledOnTouchOutside(false);

		return view;
	}

	private void initViews() {
		currentpassword = (EditText) view.findViewById(R.id.currentpassword);
		newpassword = (EditText) view.findViewById(R.id.newpassword);
		confirmnewpassword = (EditText) view.findViewById(R.id.confirmnewpassword);
		submit = (TextView) view.findViewById(R.id.forgot_button);
	}

	private void setListeners() {
		submit.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.forgot_button:
			submitButtonTask();
			break;
		}
	}

	private void submitButtonTask() {
		String getCurrentPassword = currentpassword.getText().toString();
		getNewPassword = newpassword.getText().toString();
		String getConfirmNewPassword = confirmnewpassword.getText().toString();

		if (getCurrentPassword.equals("") || getCurrentPassword.length() == 0) {
			new CustomToast().Show_Toast(getActivity(), view, "Please enter Current Password");
		}else if (getNewPassword.equals("") || getNewPassword.length() == 0) {
			new CustomToast().Show_Toast(getActivity(), view, "Please enter New Password");
		}else if (getConfirmNewPassword.equals("") || getConfirmNewPassword.length() == 0) {
			new CustomToast().Show_Toast(getActivity(), view, "Please Confirm New Password");
		}else if (!getConfirmNewPassword.equals(getNewPassword)) {
			new CustomToast().Show_Toast(getActivity(), view, "New Password and Confirm Password not Same");
		}

		/*else
			new ChangePassword().execute();*/
	}

	/*class ChangePassword extends AsyncTask<String,String,String> {
		protected void onPreExecute() {
			progressDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {

			StringBuilder URL_CREATE_REQUEST = new StringBuilder(Constants.API_ChangePassword);

			SharedPreference sharedPreference = new SharedPreference();
			URL_CREATE_REQUEST.append("?Id=1");
			URL_CREATE_REQUEST.append("&pUserID=" + sharedPreference.getValue(getActivity().getApplicationContext(), Constants.PREF_ISAD, Constants.PREF_KEY_USER_Email));
			URL_CREATE_REQUEST.append("&pKey=" + sharedPreference.getValue(getActivity().getApplicationContext(), Constants.PREF_ISAD, Constants.PREF_KEY_USER_PASS));
			URL_CREATE_REQUEST.append("&pNewPassword=" + getNewPassword);

			Utilities utilities = new Utilities(getActivity().getApplicationContext());
			return utilities.apiCall(URL_CREATE_REQUEST.toString());
		}

		public void onPostExecute(String response) {
			try {

				if (response.equals("NO_INTERNET")) {
					Toast.makeText(getActivity().getApplicationContext(), "Check internet connection", Toast.LENGTH_LONG).show();
				} else if (response.equals("ERROR")) {
					Toast.makeText(getActivity().getApplicationContext(), "Please try again later...", Toast.LENGTH_LONG).show();
				} else {

					if (response.equals("true")) {

						new android.support.v7.app.AlertDialog.Builder(getContext(), R.style.AppCompatAlertDialogStyleSuccess)
								.setTitle("Password Changed")
								.setIcon(R.drawable.alert_dialog_confirm)
								.setMessage("Password Change Successfully.....")
								.setPositiveButton("OK", new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int which) {
										dialog.cancel();
									}
								}).show();

						FragmentManager fragmentManager = getFragmentManager();
						ServiceProviderListFragment fragobj = new ServiceProviderListFragment();
						Class fragmentClass1 = null;
						Bundle bundle = new Bundle();
						String myMessage;
						FragmentTransaction xfragmentTransaction = fragmentManager.beginTransaction();

						xfragmentTransaction.replace(R.id.containerView, new Login_Fragment()).commit();
						fragmentClass1 = Login_Fragment.class;

					} else {
						new android.support.v7.app.AlertDialog.Builder(getContext(), R.style.AppCompatAlertDialogStyleFailed)
								.setTitle("Password Change Failed")
								.setIcon(R.drawable.alert_dialog_misdeed)
								.setMessage("Error occurred in Changing Password...")
								.setPositiveButton("OK", new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int which) {
										dialog.cancel();
									}
								}).show();
					}
				}

			} catch (Exception e1) {
				Toast.makeText(getActivity().getApplicationContext(), "Please try again later...", Toast.LENGTH_LONG).show();
				e1.fillInStackTrace();
			} finally {
				progressDialog.dismiss();
			}
		}
	}*/

}