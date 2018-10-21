package com.medicalstorefinder.mychemists.Fragments;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.medicalstorefinder.mychemists.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PolicyFragment extends Fragment {


    public PolicyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_policy, container, false);

        WebView webView = (WebView) v.findViewById(R.id.webview_policy);
//        webView.loadDataWithBaseURL(null, htmlAsString, "text/html", "utf-8", null);
        webView.setBackgroundColor(Color.TRANSPARENT);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("https://mychemist.net.in/app/policy.html");

        return v;
    }

}
