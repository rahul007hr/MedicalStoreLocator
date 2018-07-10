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
	private static EditText fullName, emailId, adharCardNumber,mobileNumber, location,
			password, confirmPassword;
	private static TextView login;
	private static Button signUpButton;
	private static CheckBox terms_conditions;
	private static RadioButton serviceProvider,user1;

	private static FragmentManager fragmentManager;
	public static String cType;
	public RadioGroup rg;
	public static Spinner sp;
	int pos;
	ApiUser user = new ApiUser();
	String date;
	File f;
	private static int RESULT_LOAD_IMAGE = 1;
	public String res="";
	Uri selectedImage;

	/*********  work only for Dedicated IP ***********/
	static final String FTP_HOST= "103.21.58.98";

	/*********  FTP USERNAME ***********/
	static final String FTP_USER = "paanwsqw";

	/*********  FTP PASSWORD ***********/
	static final String FTP_PASS  ="RahuL007#";

	String ff="";
	String picturePath="";
	String name;
	public Bitmap bm;

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

		bm = BitmapFactory.decodeResource(getResources(), R.drawable.sleep_icon);

		return view;
	}

	// Initialize all views
	private void initViews() {


		fullName = (EditText) view.findViewById(R.id.fullName);
		emailId = (EditText) view.findViewById(R.id.userEmailId);
		mobileNumber = (EditText) view.findViewById(R.id.mobileNumber);
		adharCardNumber = (EditText) view.findViewById(R.id.adharnumber);
		location = (EditText) view.findViewById(R.id.location);
		password = (EditText) view.findViewById(R.id.password);
		confirmPassword = (EditText) view.findViewById(R.id.confirmPassword);
		serviceProvider=(RadioButton)view.findViewById(R.id.email_id);
		user1=(RadioButton)view.findViewById(R.id.user);
		signUpButton = (Button) view.findViewById(R.id.signUpBtn);
		login = (TextView) view.findViewById(R.id.already_user);
		terms_conditions = (CheckBox) view.findViewById(R.id.terms_conditions);


		rg = (RadioGroup) view.findViewById(R.id.location1);
		sp = (Spinner) view.findViewById(R.id.service_provider_type);

	}

	// Set Listeners
	private void setListeners() {
		signUpButton.setOnClickListener(this);
		login.setOnClickListener(this);



		rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {

				pos = rg.indexOfChild(view.findViewById(checkedId));
				if(pos==0){
					cType="Service Provider";
				}else{
					cType="User";
				}
				switch (pos) {
					case 0:
						sp.setVisibility(View.VISIBLE);
						List<String> list = new ArrayList<String>();
						list.add("Paanwala");
						list.add("Electrician");
						list.add("Painter");
						list.add("Kamwalibai");
						list.add("Garage");
						list.add("Furniture");
						list.add("Auto");
						list.add("Mess");
						list.add("Pastecontrol");
						list.add("Tempo");
						list.add("Plumber");
						list.add("Travel");
						list.add("Interior");
						list.add("Construction");
						list.add("Medicals");
						list.add("Grocery");
						list.add("Salon");

						ArrayAdapter<String> adp = new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, list);
						sp.setAdapter(adp);
						break;

					case 1:
						sp.setVisibility(View.GONE);
						break;
				}

			}

			});



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
			user.setcType(cType);
			if(cType=="Service Provider"){
				user.setType_of_Service(sp.getSelectedItem().toString());
				user.setActivated("Not Activated");
			}else{
				user.setType_of_Service("Null");
				user.setActivated("Activated");
			}

			user.setcStatus("Online");
			user.setProfilePicUrl("null");
			user.setAdhar_Card_Number(adharCardNumber.getText().toString());



			break;

		case R.id.already_user:

			// Replace login fragment
//			new MainActivity().replaceLoginFragment();
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
		String getAdharCardNumber=adharCardNumber.getText().toString();
		name=getEmailId;
		// Pattern match for email id
		Pattern p = Pattern.compile(Utils1.regEx);
		Matcher m = p.matcher(getEmailId);

		// Check if all strings are null or not
		if (getFullName.equals("") || getFullName.length() == 0
				|| getEmailId.equals("") || getEmailId.length() == 0
				|| getAdharCardNumber.equals("") || getEmailId.length() == 0
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
					new uploadTask().execute();
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

	private String getRealPathFromURI(String contentURI) {
		Uri contentUri = Uri.parse(contentURI);
		Cursor cursor = getContext().getContentResolver().query(contentUri, null, null, null, null);
		if (cursor == null) {
			return contentUri.getPath();
		} else {
			cursor.moveToFirst();
			int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
			return cursor.getString(index);
		}
	}

	public boolean saveBitmapToFile(File dir, String fileName, Bitmap bm,
									Bitmap.CompressFormat format, int quality) {

		File imageFile = new File(dir,fileName);

		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(imageFile);

			bm.compress(format,quality,fos);

			fos.close();

			return true;
		}
		catch (IOException e) {
			Log.e("app",e.getMessage());
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
		return false;
	}

	class uploadTask extends AsyncTask<String, Void, String> {

		protected void onPreExecute() {
			progressDialog.show();
		}
		protected void onPostExecute(String response) {

			try {


			}
			catch (Exception e1) {
			}
			finally {
				progressDialog.dismiss();
			}
		}


		@Override
		protected String doInBackground(String... params) {
			FTPClient client = new FTPClient();
			FileInputStream fis = null;


			File dir = new File(Environment.getExternalStorageDirectory() + File.separator + "drawable");

			boolean doSave = true;
			if (!dir.exists()) {
				doSave = dir.mkdirs();
			}

			if (doSave) {
				saveBitmapToFile(dir,"profile.png",bm,Bitmap.CompressFormat.PNG,100);
			}
			else {
				Log.e("app","Couldn't create target directory.");
			}

			f	=	new File(Environment.getExternalStorageDirectory() + File.separator + "drawable/profile.png");
			picturePath=Environment.getExternalStorageDirectory() + File.separator + "drawable/profile.png";
			String newstr = null;
			if (null != picturePath && picturePath.length() > 0 )
			{
				int endIndex = picturePath.lastIndexOf("/");
				if (endIndex != -1)
				{
					newstr = picturePath.substring(endIndex+1);
				}
			}

			ff=newstr;


			try {

				try {
					client.connect(FTP_HOST, 21);
					client.login(FTP_USER, FTP_PASS);
					client.setType(FTPClient.TYPE_BINARY);
					client.setPassive(true);
					client.noop();
					client.changeDirectory("/httpdocs/uploads/");
					try {
						client.upload(f, new MyTransferListener());

					} catch (FTPDataTransferException e) {
						e.printStackTrace();
					} catch (FTPAbortedException e) {
						e.printStackTrace();
					}

				} catch (FTPIllegalReplyException e) {
					e.printStackTrace();
				} catch (FTPException e) {
					e.printStackTrace();
				}

			} catch (IOException e) {
				e.printStackTrace();
			}

			String fileToDelete = "/httpdocs/uploads/"+name + ".jpg";
			try {
				client.deleteFile(fileToDelete);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (FTPIllegalReplyException e) {
				e.printStackTrace();
			} catch (FTPException e) {
				e.printStackTrace();
			}

			try {
				client.rename("/httpdocs/uploads/"+ff, "/httpdocs/uploads/"+name + ".jpg");
			} catch (IOException e) {
				e.printStackTrace();
			} catch (FTPIllegalReplyException e) {
				e.printStackTrace();
			} catch (FTPException e) {
				e.printStackTrace();
			}

			return null;
		}
	}

	public class MyTransferListener implements FTPDataTransferListener {

		public void started() {

//            btn.setVisibility(View.GONE);
			// Transfer started
//            Toast.makeText(getBaseContext(), " Upload Started ...", Toast.LENGTH_SHORT).show();
			System.out.println(" Upload Started ...");
//            new DeleteImage().execute();

		}

		public void transferred(int length) {

			System.out.println(" transferred ..." + length);
		}

		public void completed() {


			System.out.println(" completed ..." );

		}

		public void aborted() {


			System.out.println(" aborted ..." );
		}

		public void failed() {

			System.out.println(" failed ..." );
		}

	}


}
