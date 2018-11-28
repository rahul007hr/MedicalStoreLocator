package com.medicalstorefinder.mychemists.Fragments;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.medicalstorefinder.mychemists.Activity.CustomerActivity;
import com.medicalstorefinder.mychemists.Constants.Utility;
import com.medicalstorefinder.mychemists.R;


public class ChooseOrderTypeFragment extends Fragment {

    private String userChoosenTask;

    public ChooseOrderTypeFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_choose_order_type, container, false);
        Button newOrderBtn = (Button) view.findViewById(R.id.newOrder);
        Button oldOrderBtn = (Button) view.findViewById(R.id.oldOrder);
        CustomerActivity.navigation.setVisibility(View.VISIBLE);
        CustomerActivity.navigation.getMenu().findItem(R.id.chooseOrderType).setChecked(true);
        CustomerActivity.navigation.getMenu().findItem(R.id.postOrder).setEnabled(false);
        CustomerActivity.navigation.getMenu().findItem(R.id.NearbyServiceProviderList).setEnabled(false);
        newOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PostOrderFragment fragment2 = new PostOrderFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.containerView, fragment2);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });
        oldOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectPreviousOrderType();

            }
        });
        return view;
    }

    private void selectPreviousOrderType() {
        final CharSequence[] items = {"Completed Orders", "Canceled Orders", "On Hold Orders"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Select Previous Order Type..");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(getContext());
                if (items[item].equals("Completed Orders")) {
                    userChoosenTask = "Completed Orders";
                    if (result) {
                        Bundle bundle = new Bundle();
                        bundle.putString("key", "Completed");
                        ServiceProviderListUsingOrderStatusFragment fragment2 = new ServiceProviderListUsingOrderStatusFragment();
                        fragment2.setArguments(bundle);
                        FragmentManager fragmentManager = getFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.containerView, fragment2);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }
                } else if (items[item].equals("Canceled Orders")) {
                    userChoosenTask = "Canceled Orders";
                    if (result) {
                        Bundle bundle = new Bundle();
                        bundle.putString("key", "Canceled");
                        ServiceProviderListUsingOrderStatusFragment fragment2 = new ServiceProviderListUsingOrderStatusFragment();
                        fragment2.setArguments(bundle);
                        FragmentManager fragmentManager = getFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.containerView, fragment2);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }
                } else if (items[item].equals("On Hold Orders")) {
                    userChoosenTask = "On Hold Orders";
                    if (result) {
                        Bundle bundle = new Bundle();
                        bundle.putString("key", "On Hold");
                        ServiceProviderListUsingOrderStatusFragment fragment2 = new ServiceProviderListUsingOrderStatusFragment();
                        fragment2.setArguments(bundle);
                        FragmentManager fragmentManager = getFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.containerView, fragment2);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }
                }
            }
        });
        builder.show();
    }

}
