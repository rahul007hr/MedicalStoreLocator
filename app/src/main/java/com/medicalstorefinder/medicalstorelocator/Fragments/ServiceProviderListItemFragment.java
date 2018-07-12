package com.medicalstorefinder.medicalstorelocator.Fragments;


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

import com.medicalstorefinder.medicalstorelocator.Constants.Constants;
import com.medicalstorefinder.medicalstorelocator.Constants.SharedPreference;
import com.medicalstorefinder.medicalstorelocator.Constants.Utilities;
import com.medicalstorefinder.medicalstorelocator.Models.ServiceProviderDetailsModel;
import com.medicalstorefinder.medicalstorelocator.R;

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
        btnSubmit= (Button)v1.findViewById(R.id.profile);
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
        new RetrieveFeedTask().execute();
        return v1;

    }
    @Override
    public void onListItemClick(ListView l, View v, final int position, long id) {

        android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(getContext(),R.style.AppCompatAlertDialogStyle );

        alertDialogBuilder.setTitle("Exit");
        alertDialogBuilder.setIcon(R.drawable.alert_dialog_warning);
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

            case R.id.profile:

                String itemname1,itemname2,itemname3,itemname4,itemname5,itemname6,itemname7,itemname8,itemname9,itemname10;
            if(adapter.getCount()>=1) {
                if (adapter.getCount() >= 1) {
                    itemname1 = adapter.getItem(0);
                } else {
                    itemname1 = "null";
                }
                if (adapter.getCount() >= 2) {
                    itemname2 = adapter.getItem(1);
                } else {
                    itemname2 = "null";
                }
                if (adapter.getCount() >= 3) {
                    itemname3 = adapter.getItem(2);
                } else {
                    itemname3 = "null";
                }
                if (adapter.getCount() >= 4) {
                    itemname4 = adapter.getItem(3);
                } else {
                    itemname4 = "null";
                }
                if (adapter.getCount() >= 5) {
                    itemname5 = adapter.getItem(4);
                } else {
                    itemname5 = "null";
                }
                if (adapter.getCount() >= 6) {
                    itemname6 = adapter.getItem(5);
                } else {
                    itemname6 = "null";
                }
                if (adapter.getCount() >= 7) {
                    itemname7 = adapter.getItem(6);
                } else {
                    itemname7 = "null";
                }
                if (adapter.getCount() >= 8) {
                    itemname8 = adapter.getItem(7);
                } else {
                    itemname8 = "null";
                }
                if (adapter.getCount() >= 9) {
                    itemname9 = adapter.getItem(8);
                } else {
                    itemname9 = "null";
                }
                if (adapter.getCount() >= 10) {
                    itemname10 = adapter.getItem(9);
                } else {
                    itemname10 = "null";
                }

                serviceProviderDetailsModel = new ServiceProviderDetailsModel();
                serviceProviderDetailsModel.setItemname1(itemname1);
                serviceProviderDetailsModel.setItemname2(itemname2);
                serviceProviderDetailsModel.setItemname3(itemname3);
                serviceProviderDetailsModel.setItemname4(itemname4);
                serviceProviderDetailsModel.setItemname5(itemname5);
                serviceProviderDetailsModel.setItemname6(itemname6);
                serviceProviderDetailsModel.setItemname7(itemname7);
                serviceProviderDetailsModel.setItemname8(itemname8);
                serviceProviderDetailsModel.setItemname9(itemname9);
                serviceProviderDetailsModel.setItemname10(itemname10);

                new Submit().execute();

            }else{
                Toast.makeText(getContext(), "Please Add Item", Toast.LENGTH_SHORT).show();

            }
                break;
        }
    }



    class Submit extends AsyncTask<String,String,String> {
        protected void onPreExecute() {
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            StringBuilder URL_CREATE_REQUEST = new StringBuilder(Constants.API_SubmitServiceProvidersList);

            SharedPreference sharedPreference = new SharedPreference();

            URL_CREATE_REQUEST.append("?Id=1");
            URL_CREATE_REQUEST.append("&pUserID=" + sharedPreference.getValue(getActivity().getApplicationContext(), Constants.PREF_ISAD, Constants.PREF_KEY_USER_Email));
            URL_CREATE_REQUEST.append("&pKey=" + sharedPreference.getValue(getActivity().getApplicationContext(), Constants.PREF_ISAD, Constants.PREF_KEY_USER_PASS));
            URL_CREATE_REQUEST.append("&itemname1=" + serviceProviderDetailsModel.getItemname1());
            URL_CREATE_REQUEST.append("&itemname2=" + serviceProviderDetailsModel.getItemname2());
            URL_CREATE_REQUEST.append("&itemname3=" + serviceProviderDetailsModel.getItemname3());
            URL_CREATE_REQUEST.append("&itemname4=" + serviceProviderDetailsModel.getItemname4());
            URL_CREATE_REQUEST.append("&itemname5=" + serviceProviderDetailsModel.getItemname5());
            URL_CREATE_REQUEST.append("&itemname6=" + serviceProviderDetailsModel.getItemname6());
            URL_CREATE_REQUEST.append("&itemname7=" + serviceProviderDetailsModel.getItemname7());
            URL_CREATE_REQUEST.append("&itemname8=" + serviceProviderDetailsModel.getItemname8());
            URL_CREATE_REQUEST.append("&itemname9=" + serviceProviderDetailsModel.getItemname9());
            URL_CREATE_REQUEST.append("&itemname10=" + serviceProviderDetailsModel.getItemname10());

            Utilities utilities = new Utilities(getActivity().getApplicationContext());
            return utilities.apiCall(URL_CREATE_REQUEST.toString());
        }

        public void onPostExecute(String response) {
            try {

                if (response.equals("NO_INTERNET")) {
                    Toast.makeText(getActivity().getApplicationContext(), "Check internet connection", Toast.LENGTH_LONG).show();
                } else if (response.equals("ERROR")) {
                    Toast.makeText(getActivity().getApplicationContext(), "Something went wrong...", Toast.LENGTH_LONG).show();
                } else {
                    if (response.equals("true")) {

                        new android.support.v7.app.AlertDialog.Builder(getContext(), R.style.AppCompatAlertDialogStyleSuccess)
                                .setTitle("Submit List")
                                .setIcon(R.drawable.alert_dialog_confirm)
                                .setMessage("Submited Successfully.....")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                }).show();



                    } else {
                        new android.support.v7.app.AlertDialog.Builder(getContext(), R.style.AppCompatAlertDialogStyleFailed)
                                .setTitle("Submit List")
                                .setIcon(R.drawable.alert_dialog_misdeed)
                                .setMessage("Error occurred in Submiting List...")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                }).show();
                    }
                }

            } catch (Exception e1) {
                Toast.makeText(getActivity().getApplicationContext(), "Something went wrong...", Toast.LENGTH_LONG).show();
                e1.fillInStackTrace();
            } finally {
                progressDialog.dismiss();
            }
        }
    }

    class RetrieveFeedTask extends AsyncTask<Void, Void, String> {

        protected void onPreExecute() {
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Void... urls) {

            Utilities utilities = new Utilities(  getActivity().getApplicationContext() );
            SharedPreference sharedPreference = new SharedPreference();

            StringBuilder URL_List= new StringBuilder(Constants.API_GetServiceProvidersList);
            URL_List.append("?Id=1");
            URL_List.append("&pUserID=" + sharedPreference.getValue(getActivity().getBaseContext(), Constants.PREF_ISAD, Constants.PREF_KEY_USER_Email));
            URL_List.append("&pKey=" + sharedPreference.getValue(getActivity().getBaseContext(), Constants.PREF_ISAD, Constants.PREF_KEY_USER_PASS));


            return utilities.apiCall(URL_List.toString());
        }

        protected void onPostExecute(String response) {

            try {



                if (response.equals("NO_INTERNET")) {
                    Toast.makeText(getActivity().getBaseContext(), "Check internet connection", Toast.LENGTH_LONG).show();
                } else if (response.equals("ERROR")) {

                    Toast.makeText(getActivity().getBaseContext(), "Somthing went wrong", Toast.LENGTH_LONG).show();
                } else {


                    JSONArray jsonarray = new JSONArray(response);

                    if (jsonarray.length() <= 0) {
                        Toast.makeText(getActivity().getBaseContext(), "No more record found.", Toast.LENGTH_SHORT).show();
                    }


                    for (int i = 0; i < jsonarray.length(); i++) {
                        ServiceProviderDetailsModel serviceProviderDetails = new ServiceProviderDetailsModel();
                        JSONObject json = jsonarray.getJSONObject(i);

                        serviceProviderDetails.setItemname1(json.getString("Itemname1"));
                        serviceProviderDetails.setItemname2(json.getString("Itemname2"));
                        serviceProviderDetails.setItemname3(json.getString("Itemname3"));
                        serviceProviderDetails.setItemname4(json.getString("Itemname4"));
                        serviceProviderDetails.setItemname5(json.getString("Itemname5"));
                        serviceProviderDetails.setItemname6(json.getString("Itemname6"));
                        serviceProviderDetails.setItemname7(json.getString("Itemname7"));
                        serviceProviderDetails.setItemname8(json.getString("Itemname8"));
                        serviceProviderDetails.setItemname9(json.getString("Itemname9"));
                        serviceProviderDetails.setItemname10(json.getString("Itemname10"));


                        if(!json.getString("Itemname1").equals("null"))
                         list.add(json.getString("Itemname1"));
                        if(!json.getString("Itemname2").equals("null"))
                            list.add(json.getString("Itemname2"));
                        if(!json.getString("Itemname3").equals("null"))
                            list.add(json.getString("Itemname3"));
                        if(!json.getString("Itemname4").equals("null"))
                            list.add(json.getString("Itemname4"));
                        if(!json.getString("Itemname5").equals("null"))
                            list.add(json.getString("Itemname5"));
                        if(!json.getString("Itemname6").equals("null"))
                            list.add(json.getString("Itemname6"));
                        if(!json.getString("Itemname7").equals("null"))
                            list.add(json.getString("Itemname7"));
                        if(!json.getString("Itemname8").equals("null"))
                            list.add(json.getString("Itemname8"));
                        if(!json.getString("Itemname9").equals("null"))
                            list.add(json.getString("Itemname9"));
                        if(!json.getString("Itemname10").equals("null"))
                            list.add(json.getString("Itemname10"));



                        listDetails.add(serviceProviderDetails);

                        adapter.notifyDataSetChanged();
                    }



                }
            }

            catch (Exception e1) {
                try {
                    Toast.makeText(getActivity().getApplicationContext(), "List is Empty", Toast.LENGTH_LONG).show();
                }catch (Exception e2) {

                }
            }
            finally {
                progressDialog.dismiss();
            }
        }
    }

}
