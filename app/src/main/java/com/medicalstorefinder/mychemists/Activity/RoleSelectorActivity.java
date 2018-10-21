package com.medicalstorefinder.mychemists.Activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.medicalstorefinder.mychemists.Constants.Constants;
import com.medicalstorefinder.mychemists.Constants.SharedPreference;
import com.medicalstorefinder.mychemists.R;

public class RoleSelectorActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_role_selector);


        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.container, new PlaceholderFragment()).commit();
        }

    }
    public static class PlaceholderFragment extends Fragment{
        SharedPreference sharedPreference;

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.fragment_user_role_selector, container, false);

            Button serviceProviderBtn = (Button)rootView.findViewById(R.id.serviceProviderSelected);
            Button userBtn = (Button)rootView.findViewById(R.id.userSelected);


            sharedPreference = new SharedPreference();

            sharedPreference.clearSharedPreference(getActivity(), Constants.PREF_USER_ROLE);
            sharedPreference.createSharedPreference(getActivity(), Constants.PREF_USER_ROLE);


            serviceProviderBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sharedPreference.putValue(getActivity(), Constants.PREF_USER_ROLE, Constants.PREF_USER_ROLE,"medical");

                    Intent intent = new Intent(getContext(), MainActivity.class);
                        startActivity(intent);
                        getActivity().finish();

                }
            });

            userBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    sharedPreference.putValue(getActivity(), Constants.PREF_USER_ROLE, Constants.PREF_USER_ROLE,"customer");

                    Intent intent = new Intent(getContext(), MainActivity.class);
                        startActivity(intent);
                        getActivity().finish();

                }
            });





            return rootView;
        }


    }

}
