package com.medicalstorefinder.medicalstoreslocatorss.Fragments;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.medicalstorefinder.medicalstoreslocatorss.Constants.Constants;
import com.medicalstorefinder.medicalstoreslocatorss.Constants.SharedPreference;
import com.medicalstorefinder.medicalstoreslocatorss.Constants.Utilities;
import com.medicalstorefinder.medicalstoreslocatorss.Models.ServiceProviderDetailsModel;
import com.medicalstorefinder.medicalstoreslocatorss.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ServiceProviderListItemFragment extends ListFragment implements View.OnClickListener {


    public ServiceProviderListItemFragment() {
        // Required empty public constructor
    }

    /** Items entered by the user is stored in this ArrayList variable */
    ArrayList<String> list = new ArrayList<String>();

    /** Declaring an ArrayAdapter to set items to ListView */
    ArrayAdapter<String> adapter;
    EditText edit,editCost;
    Button btn,btnSubmit;

    ProgressDialog progressDialog;
    ServiceProviderDetailsModel serviceProviderDetailsModel;
    List<ServiceProviderDetailsModel> listDetails = new ArrayList<ServiceProviderDetailsModel>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
    View v1=inflater.inflate(R.layout.slider_1, container, false);
        /** Reference to the button of the layout main.xml */
        btn = (Button)v1.findViewById(R.id.btnAdd);
//        btnSubmit= (Button)v1.findViewById(R.id.profile);
        edit = (EditText) v1.findViewById(R.id.txtItem);
        editCost = (EditText) v1.findViewById(R.id.txtItemCost);


        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        /** Defining the ArrayAdapter to set items to ListView */
        adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, list);

        /** Defining a click event listener for the button "Add" */

        /** Setting the event listener for the add button */
        btn.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        /** Setting the adapter to the ListView */
        setListAdapter(adapter);
        return v1;
    }
    @Override
    public void onListItemClick(ListView l, View v, final int position, long id) {

        android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(getContext(),R.style.AppCompatAlertDialogStyle );

        alertDialogBuilder.setTitle("Exit");
        alertDialogBuilder
                .setMessage("Are you sure you want to Remove?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        list.remove(position);
                        adapter.notifyDataSetChanged();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnAdd:
                String s=edit.getText().toString();
                String s1=editCost.getText().toString();
                if (!s.equals("") && !s1.equals("")) {
                    if(list.size()<10) {
                        list.add(s + "  -  "+s1 + "  Rs");
                        edit.setText("");
                        editCost.setText("");
                        adapter.notifyDataSetChanged();
                    }else{
                        Toast.makeText(getContext(), "Item List is Full", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    if(s.equals("") && s1.equals("")){
                        Toast.makeText(getContext(), "Please Enter Item And Item Cost", Toast.LENGTH_SHORT).show();
                    }
                    else if(s.equals("")) {
                        Toast.makeText(getContext(), "Please Enter Item", Toast.LENGTH_SHORT).show();
                    }
                    else if(s1.equals("")){
                        Toast.makeText(getContext(), "Please Enter Item Cost", Toast.LENGTH_SHORT).show();
                    }
                }

                break;
        }
    }

}
