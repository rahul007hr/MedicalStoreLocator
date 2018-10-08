package com.medicalstorefinder.medicalstoreslocatorss.Fragments;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.medicalstorefinder.medicalstoreslocatorss.Activity.MainActivity;
import com.medicalstorefinder.medicalstoreslocatorss.Adapter.PlaceListAdapter;
import com.medicalstorefinder.medicalstoreslocatorss.Constants.Constants;
import com.medicalstorefinder.medicalstoreslocatorss.Constants.CustomToast;
import com.medicalstorefinder.medicalstoreslocatorss.Constants.Utilities;
import com.medicalstorefinder.medicalstoreslocatorss.Constants.Utils1;
import com.medicalstorefinder.medicalstoreslocatorss.Geofencing;
import com.medicalstorefinder.medicalstoreslocatorss.Models.ApiUser;
import com.medicalstorefinder.medicalstoreslocatorss.Provider.PlaceContract;
import com.medicalstorefinder.medicalstoreslocatorss.Provider.PlaceDbHelper;
import com.medicalstorefinder.medicalstoreslocatorss.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.app.Activity.RESULT_OK;

public class SignUp_Customer_Fragment extends Fragment implements OnClickListener{

	public static final String TAG = MainActivity.class.getSimpleName();

	private static View view;
	private static EditText firstName,lastName, mobileNumber;
	private static TextView login;
	private static Button signUpButton;
	private static CheckBox terms_conditions;

	private static FragmentManager fragmentManager;
	ApiUser user = new ApiUser();

	ProgressDialog progressDialog;

	public SignUp_Customer_Fragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.signup_customer_layout, container, false);
		initViews();

		progressDialog = new ProgressDialog(getActivity());
		progressDialog.setMessage("Loading...");
		progressDialog.setCancelable(false);
		progressDialog.setCanceledOnTouchOutside(false);
		setListeners();

		return view;
	}

	// Initialize all views
	private void initViews() {
		firstName = (EditText) view.findViewById(R.id.firstName);
		lastName = (EditText) view.findViewById(R.id.lastName);
		mobileNumber = (EditText) view.findViewById(R.id.mobileNumber);
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

			user.setFirst_Name(firstName.getText().toString());
			user.setLast_Name(lastName.getText().toString());
			user.setRegMobile(mobileNumber.getText().toString());

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
			String getFirstName = firstName.getText().toString();
			String getLastName = lastName.getText().toString();
			String getMobileNumber = mobileNumber.getText().toString();

			// Check if all strings are null or not
			if (getFirstName.equals("") || getFirstName.length() == 0
					|| getLastName.equals("") || getLastName.length() == 0
					|| getMobileNumber.equals("") || getMobileNumber.length() == 0)

				new CustomToast().Show_Toast(getActivity(), view,
						"All fields are required.");
				// Make sure user should check Terms and Conditions checkbox
			else if (!terms_conditions.isChecked())
				new CustomToast().Show_Toast(getActivity(), view,
						"Please select Terms and Conditions.");
				// Else do signup or do your stuff
			else
				new SetSignup().execute();

	}

	public final class SetSignup extends AsyncTask<String,String,String>
	{
		protected void onPreExecute() {
				progressDialog.show();
		}

		@Override
		protected String doInBackground(String... urls) {

			Utilities utilities = new Utilities(getContext());

			String address = Constants.API_SIGN_UP_CUSTOMER;
			Map<String, String> params = new HashMap<>();
			params.put("mobile", user.getRegMobile());
			params.put("firstname", user.getFirst_Name());
			params.put("lastname", user.getLast_Name());

			return utilities.apiCalls(address,params);
		}

		public void onPostExecute(String response) {

			try {
				JSONObject jsonObject1 = new JSONObject(response);
				JSONObject jsonObject2 = new JSONObject(jsonObject1.getString("Content"));



				if(response.equalsIgnoreCase("ERROR") ) {
//                    Toast.makeText(getContext(), "Please try again later config 2", Toast.LENGTH_LONG).show();
				}else if (jsonObject2.getString("status").equalsIgnoreCase("error")) {
					Toast.makeText(getContext(), jsonObject2.getString("message"), Toast.LENGTH_LONG).show();
				}
				else if (jsonObject2.getString("status").equalsIgnoreCase("success")) {
                    Toast.makeText(getContext(), jsonObject2.getString("message"), Toast.LENGTH_LONG).show();
					fragmentManager = getActivity().getSupportFragmentManager();

					fragmentManager
							.beginTransaction()
							.setCustomAnimations(R.anim.right_enter, R.anim.left_out)
							.replace(R.id.frameContainer, new Login_Fragment(),
									Utils1.Login_Fragment).commit();

				}

			} catch (Exception e1) {

//                Toast.makeText( getContext(), "Please try again later config 3", Toast.LENGTH_LONG).show();

			}
			finally {
				progressDialog.dismiss();

			}

		}
	}
}
