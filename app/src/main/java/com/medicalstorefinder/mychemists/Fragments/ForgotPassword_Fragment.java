package com.medicalstorefinder.mychemists.Fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.medicalstorefinder.mychemists.Activity.MainActivity;
import com.medicalstorefinder.mychemists.Constants.Constants;
import com.medicalstorefinder.mychemists.Constants.CustomToast;
import com.medicalstorefinder.mychemists.Constants.Utilities;
import com.medicalstorefinder.mychemists.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ForgotPassword_Fragment extends Fragment implements OnClickListener {

    private static View view;
    private static EditText emailId;
    private static Button submit, back;
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
        submit = (Button) view.findViewById(R.id.forgot_button);
        back = (Button) view.findViewById(R.id.backToLoginBtn);

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
        if (getEmailId.equals("") || getEmailId.length() == 0)
            new CustomToast().Show_Toast(getActivity(), view, "Please enter your Mobile no / Email Id.");
        else
            new ForgotPassword().execute();
    }

    class ForgotPassword extends AsyncTask<String, String, String> {
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... urls) {
            Utilities utilities = new Utilities(getContext());
            String address = Constants.API_MEDICAL_FORGOT_PASSWORD;
            Map<String, String> params = new HashMap<>();
            params.put("username", getEmailId);
            return utilities.apiCalls(address, params);
        }

        public void onPostExecute(String response) {
            try {
                JSONObject jsonObject1 = new JSONObject(response);
                JSONObject jsonObject2 = new JSONObject(jsonObject1.getString("Content"));
                if (response.equals("NO_INTERNET")) {
                    Toast.makeText(getContext(), "Check internet connection", Toast.LENGTH_LONG).show();
                } else if (jsonObject2.getString("status").equalsIgnoreCase("error")) {
                    Toast.makeText(getActivity(), jsonObject2.getString("message"), Toast.LENGTH_LONG).show();
                } else {
                    if (jsonObject2.getString("status").equalsIgnoreCase("error")) {
                        Toast.makeText(getActivity(), jsonObject2.getString("message"), Toast.LENGTH_LONG).show();
                    } else {
                        if (jsonObject2.getString("status").equalsIgnoreCase("success")) {
                            Toast.makeText(getActivity(), jsonObject2.getString("result"), Toast.LENGTH_LONG).show();
                            new MainActivity().replaceLoginFragment();
                        } else
                            Toast.makeText(getActivity(), jsonObject2.getString("message"), Toast.LENGTH_LONG).show();
                    }
                }
            } catch (JSONException e1) {
                Toast.makeText(getContext(), "Please try again later...", Toast.LENGTH_LONG).show();
                e1.printStackTrace();
            }
        }
    }
}