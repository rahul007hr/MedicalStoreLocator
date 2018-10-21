package com.medicalstorefinder.mychemists.Fragments;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.medicalstorefinder.mychemists.Constants.Constants;
import com.medicalstorefinder.mychemists.Constants.SharedPreference;
import com.medicalstorefinder.mychemists.Models.ServiceProviderDetailsModel;
import com.medicalstorefinder.mychemists.Constants.Utilities;
import com.medicalstorefinder.mychemists.R;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class CustomerRatingsFragment extends Fragment {

    ProgressDialog progressDialog;
    RatingBar mRatingBar;
    TextView mRatingScale;
    EditText mFeedback;

    SharedPreference sharedPreference;

    public CustomerRatingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View v = inflater.inflate(R.layout.fragment_customer_ratings, container, false);

        mRatingBar = (RatingBar) v.findViewById(R.id.ratingBar);
        mRatingScale = (TextView) v.findViewById(R.id.tvRatingScale);
        mFeedback = (EditText) v.findViewById(R.id.etFeedback);
        Button mSendFeedback = (Button) v.findViewById(R.id.btnSubmit);

        sharedPreference = new SharedPreference();
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        mRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                mRatingScale.setText(String.valueOf(v));
                switch ((int) ratingBar.getRating()) {
                    case 1:
                        mRatingScale.setText("Very bad");
                        break;
                    case 2:
                        mRatingScale.setText("Need some improvement");
                        break;
                    case 3:
                        mRatingScale.setText("Good");
                        break;
                    case 4:
                        mRatingScale.setText("Great");
                        break;
                    case 5:
                        mRatingScale.setText("Awesome. I love it");
                        break;
                    default:
                        mRatingScale.setText("");
                }
            }
        });

        mSendFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(String.valueOf(mRatingBar.getRating()).equalsIgnoreCase("0.0")){
                    Toast.makeText(getContext(), "Please Give Ratings To Medical Store", Toast.LENGTH_SHORT).show();
                }else{
                    new Ratings().execute();
                }

                }
//            }
        });

        return v;
    }
    class Ratings extends AsyncTask<Void, Void, String> {

        protected void onPreExecute() {
            progressDialog.show();
        }

        protected String doInBackground(Void... urls) {

            Utilities utilities = new Utilities(getContext());

            ServiceProviderDetailsModel serviceProviderDetails = new ServiceProviderDetailsModel();

            String address = Constants.API_RATINGS;
            Map<String, String> params = new HashMap<>();
            params.put("fromuserid", sharedPreference.getValue( getActivity(), Constants.PREF_IS_USER, Constants.PREF_KEY_USER_ID ));
//            params.put("fromuserid", "14");

            params.put("touserid", sharedPreference.getValue( getActivity(), Constants.PREF_IS_USER, Constants.PREF_KEY_MEDICAL_ID ));
//            params.put("touserid", "23");

            params.put("feedback",  mFeedback.getText().toString());
            params.put("ratings", String.valueOf(mRatingBar.getRating()));


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
//                        Toast.makeText(getContext(), jsonObject2.getString("status"), Toast.LENGTH_LONG).show();

                        mFeedback.setText("");
                        mRatingBar.setRating(0);
                        Toast.makeText(getActivity(), "Thank you for sharing your feedback", Toast.LENGTH_SHORT).show();

                        ChooseOrderTypeFragment fragment2 = new ChooseOrderTypeFragment();
                        FragmentManager fragmentManager = getFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.containerView, fragment2);
                        fragmentTransaction.commit();

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
