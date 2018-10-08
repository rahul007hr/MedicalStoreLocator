package com.medicalstorefinder.medicalstoreslocatorss.Fragments;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.medicalstorefinder.medicalstoreslocatorss.Activity.CustomerActivity;
import com.medicalstorefinder.medicalstoreslocatorss.BuildConfig;
import com.medicalstorefinder.medicalstoreslocatorss.Constants.Constants;
import com.medicalstorefinder.medicalstoreslocatorss.Constants.SharedPreference;
import com.medicalstorefinder.medicalstoreslocatorss.R;

public class AboutUsFragment extends Fragment  {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.about_us, container, false);
      /*  Toolbar toolbar = (Toolbar) v.findViewById(R.id.toolbar_about_us);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) v.findViewById(R.id.collapsing_toolbar_about);
        collapsingToolbar.setTitle("About Us");*/

        String htmlAsString ="About Us Intro";
//        String htmlAsString = getString(R.string.html_about_us1);

        WebView webView = (WebView) v.findViewById(R.id.webview_about_us);
//        webView.loadDataWithBaseURL(null, htmlAsString, "text/html", "utf-8", null);
        webView.setBackgroundColor(Color.TRANSPARENT);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("https://mychemist.net.in/app/intro.html");
        //https://mychemist.net.in/app/intro.html


        TextView companyName = (TextView) v.findViewById(R.id.companyName);
        companyName.setMovementMethod(LinkMovementMethod.getInstance());

        TextView policy = (TextView) v.findViewById(R.id.policy);
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

       /* TextView policy = (TextView) v.findViewById(R.id.companyName);
        companyName.setMovementMethod(LinkMovementMethod.getInstance());*/

        TextView version = (TextView) v.findViewById(R.id.version);

        String versionName = BuildConfig.VERSION_NAME;
        version.setText("Version " + versionName);
        SharedPreference sharedPreference = new SharedPreference();
        if (sharedPreference.getValue( getActivity(), Constants.PREF_USER_ROLE, Constants.PREF_USER_ROLE ).equalsIgnoreCase("customer")) {
            CustomerActivity.navigation.setVisibility(View.GONE);
        }

        return v;
    }

}