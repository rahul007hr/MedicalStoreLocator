package com.medicalstorefinder.medicalstoreslocator.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.medicalstorefinder.medicalstoreslocator.Activity.MainActivity;
import com.medicalstorefinder.medicalstoreslocator.Constants.Constants;
import com.medicalstorefinder.medicalstoreslocator.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChooseOrderTypeFragment extends Fragment {


    public ChooseOrderTypeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        final View view = inflater.inflate(R.layout.fragment_choose_order_type, container, false);

        Button newOrderBtn = (Button)view.findViewById(R.id.newOrder);
        Button oldOrderBtn = (Button)view.findViewById(R.id.oldOrder);



        newOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PostOrderFragment fragment2 = new PostOrderFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.containerView, fragment2);
                fragmentTransaction.commit();

            }
        });


        oldOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ServiceProviderListFragment fragment2 = new ServiceProviderListFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.containerView, fragment2);
                fragmentTransaction.commit();
            }
        });

        return view;
    }

}
