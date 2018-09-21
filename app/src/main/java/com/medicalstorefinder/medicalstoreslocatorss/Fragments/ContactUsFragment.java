package com.medicalstorefinder.medicalstoreslocatorss.Fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.medicalstorefinder.medicalstoreslocatorss.Activity.CustomerActivity;
import com.medicalstorefinder.medicalstoreslocatorss.Constants.Constants;
import com.medicalstorefinder.medicalstoreslocatorss.Constants.SharedPreference;
import com.medicalstorefinder.medicalstoreslocatorss.R;


public class ContactUsFragment extends Fragment {

    View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
try {
    v = inflater.inflate(R.layout.contact_us, container, false);
   /* Toolbar toolbar = (Toolbar) v.findViewById(R.id.toolbar_contact_us);
    ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

    CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) v.findViewById(R.id.collapsing_toolbar_contact_us);
    collapsingToolbar.setTitle("Contact Us");*/
}catch(Exception e){}
        String htmlAsString = getString(R.string.html_contact_us);

        WebView webView = (WebView) v.findViewById(R.id.webview_contact_us);
        webView.loadDataWithBaseURL(null, htmlAsString, "text/html", "utf-8", null);
        webView.setBackgroundColor(Color.TRANSPARENT);

        final android.support.v7.app.AlertDialog.Builder alertDialog = new android.support.v7.app.AlertDialog.Builder(getActivity(),R.style.AppCompatAlertDialogStyle);
        alertDialog.setTitle("Do you want to call?");

        final int currentapiVersion = android.os.Build.VERSION.SDK_INT;

        v.findViewById(R.id.btn_call1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                alertDialog.setMessage("9969707707");

                alertDialog.setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                alertDialog.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if (currentapiVersion <= android.os.Build.VERSION_CODES.LOLLIPOP){
                                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                                    callIntent.setData(Uri.parse("tel:9969707707"));
                                    startActivity(callIntent);
                                } else {
                                    Intent callIntent = new Intent(Intent.ACTION_DIAL);
                                    callIntent.setData(Uri.parse("tel:9969707707"));
                                    startActivity(callIntent);
                                }
                            }
                        });
                alertDialog.show();
            }
        });

        SharedPreference sharedPreference = new SharedPreference();
        if (sharedPreference.getValue( getActivity(), Constants.PREF_USER_ROLE, Constants.PREF_USER_ROLE ).equalsIgnoreCase("customer")) {
            CustomerActivity.navigation.setVisibility(View.GONE);
        }

         return v;
    }

}

