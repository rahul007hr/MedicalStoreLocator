package com.medicalstorefinder.medicalstorelocator.Fragments;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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

import com.medicalstorefinder.medicalstorelocator.Activity.MainActivity;
import com.medicalstorefinder.medicalstorelocator.Constants.Constants;
import com.medicalstorefinder.medicalstorelocator.Constants.CustomToast;
import com.medicalstorefinder.medicalstorelocator.Constants.SharedPreference;
import com.medicalstorefinder.medicalstorelocator.Constants.Utilities;
import com.medicalstorefinder.medicalstorelocator.Constants.Utils1;
import com.medicalstorefinder.medicalstorelocator.Models.ApiUser;
import com.medicalstorefinder.medicalstorelocator.R;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUp_Fragment extends Fragment implements OnClickListener {
	private static View view;
	private static EditText firstName,lastName, emailId, mobileNumber, shopName,address,
			password, confirmPassword;
	private static TextView login;
	private static Button signUpButton;
	private static CheckBox terms_conditions;

	private static FragmentManager fragmentManager;
	int pos;
	ApiUser user = new ApiUser();

	public String res="";

	ProgressDialog progressDialog;

	public SignUp_Fragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.signup_layout, container, false);
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
		emailId = (EditText) view.findViewById(R.id.userEmailId);
		mobileNumber = (EditText) view.findViewById(R.id.mobileNumber);
		shopName = (EditText) view.findViewById(R.id.shop_name);
		address = (EditText) view.findViewById(R.id.address);
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

			user.setFirst_Name(firstName.getText().toString());
			user.setLast_Name(lastName.getText().toString());
			user.setShop_Name(shopName.getText().toString());
			user.setEmail(emailId.getText().toString());
			user.setRegMobile(mobileNumber.getText().toString());
			user.setAddress(address.getText().toString());
			user.setPasswords(password.getText().toString());
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
			String getEmailId = emailId.getText().toString();
			String getMobileNumber = mobileNumber.getText().toString();
			String getAddress = address.getText().toString();
			String getShopName = shopName.getText().toString();
			String getPassword = password.getText().toString();
			String getConfirmPassword = confirmPassword.getText().toString();
			// Pattern match for email id
			Pattern p = Pattern.compile(Utils1.regEx);
			Matcher m = p.matcher(getEmailId);

			// Check if all strings are null or not
			if (getFirstName.equals("") || getFirstName.length() == 0
					|| getLastName.equals("") || getLastName.length() == 0
					|| getEmailId.equals("") || getEmailId.length() == 0
					|| getMobileNumber.equals("") || getMobileNumber.length() == 0
					|| getAddress.equals("") || getAddress.length() == 0
					|| getShopName.equals("") || getShopName.length() == 0
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

			String address = Constants.API_SIGN_UP;
			Map<String, String> params = new HashMap<>();
			params.put("firstname", user.getFirst_Name());
			params.put("lastname", user.getLast_Name());
			params.put("shopname", user.getShop_Name());
			params.put("email", user.getEmail());
			params.put("password", user.getPasswords());
			params.put("mobile", user.getRegMobile());
			params.put("address", user.getAddress());

			return utilities.apiCalls(address,params);
		}

		public void onPostExecute(String response) {

			try {
				JSONObject jsonObject1 = new JSONObject(response);
				JSONObject jsonObject2 = new JSONObject(jsonObject1.getString("Content"));



				if(response.equalsIgnoreCase("ERROR") ) {
//                    Toast.makeText(getContext(), "Something went wrong config 2", Toast.LENGTH_LONG).show();
				}else if (jsonObject2.getString("status").equalsIgnoreCase("error")) {
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

//                Toast.makeText( getContext(), "Something went wrong config 3", Toast.LENGTH_LONG).show();

			}
			finally {
				progressDialog.dismiss();

			}

		}
	}
}
