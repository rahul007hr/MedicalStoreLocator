package com.medicalstorefinder.mychemists.Fragments;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.medicalstorefinder.mychemists.Activity.CustomerActivity;
import com.medicalstorefinder.mychemists.BuildConfig;
import com.medicalstorefinder.mychemists.Constants.Constants;
import com.medicalstorefinder.mychemists.Constants.SharedPreference;
import com.medicalstorefinder.mychemists.R;

public class AboutUsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.about_us, container, false);
        String htmlAsString = "About Us Intro";
        WebView webView = (WebView) v.findViewById(R.id.webview_about_us);
        webView.setBackgroundColor(Color.TRANSPARENT);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("https://mychemist.net.in/app/intro.html");
        TextView companyName = (TextView) v.findViewById(R.id.companyName);
        companyName.setMovementMethod(LinkMovementMethod.getInstance());
        TextView policy = (TextView) v.findViewById(R.id.policy);
        TextView aboutUsEmail = (TextView) v.findViewById(R.id.aboutUsEmail);
        policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PolicyFragment fragment2 = new PolicyFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.containerView, fragment2);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        aboutUsEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                String[] recepient = {"mychemist247@gmail.com"};
                Intent callIntent = new Intent(Intent.ACTION_SEND);
                callIntent.setData(Uri.parse("mailto:"));
                callIntent.setType("message/rfc822");
                callIntent.putExtra(Intent.EXTRA_EMAIL, recepient);
                startActivity(Intent.createChooser(callIntent, "Choose Application To Send Email"));
            }
        });
        TextView version = (TextView) v.findViewById(R.id.version);
        String versionName = BuildConfig.VERSION_NAME;
        version.setText("Version " + versionName);
        SharedPreference sharedPreference = new SharedPreference();
        if (sharedPreference.getValue(getActivity(), Constants.PREF_USER_ROLE, Constants.PREF_USER_ROLE).equalsIgnoreCase("customer")) {
            CustomerActivity.navigation.setVisibility(View.GONE);
        }
        return v;
    }

}