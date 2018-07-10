package com.medicalstorefinder.medicalstorelocator.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import com.medicalstorefinder.medicalstorelocator.BuildConfig;
import com.medicalstorefinder.medicalstorelocator.R;

public class AboutUsFragment extends Fragment  {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.about_us, container, false);
        Toolbar toolbar = (Toolbar) v.findViewById(R.id.toolbar_about_us);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) v.findViewById(R.id.collapsing_toolbar_about);
        collapsingToolbar.setTitle("About Us");

        String htmlAsString = getString(R.string.html_about_us1);

        WebView webView = (WebView) v.findViewById(R.id.webview_about_us);
        webView.loadDataWithBaseURL(null, htmlAsString, "text/html", "utf-8", null);
        webView.setBackgroundColor(Color.TRANSPARENT);


        TextView version = (TextView) v.findViewById(R.id.version);

        String versionName = BuildConfig.VERSION_NAME;
        version.setText("Version " + versionName);

        return v;
    }

}