package com.medicalstorefinder.mychemists.Fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.medicalstorefinder.mychemists.Constants.Utility;
import com.medicalstorefinder.mychemists.SingleTouchImageViewFragment;
import com.medicalstorefinder.mychemists.Activity.CustomerActivity;
import com.medicalstorefinder.mychemists.Constants.Constants;
import com.medicalstorefinder.mychemists.Constants.SharedPreference;
import com.medicalstorefinder.mychemists.Constants.Utilities;
import com.medicalstorefinder.mychemists.GlideImageLoader;
import com.medicalstorefinder.mychemists.Models.ServiceProviderDetailsModel;
import com.medicalstorefinder.mychemists.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.medicalstorefinder.mychemists.Constants.Constants.NO_AVATAR_IMAGE_PATH;


/**
 * A simple {@link Fragment} subclass.
 */
public class MedicalResponseOfCostListFragment extends Fragment  {

//    int _PageNo = 1;
ServiceProviderDetailsModel serviceProviderDetails = new ServiceProviderDetailsModel();
    ProgressDialog progressDialog;
    final String[] desc = new String[1];
    final String[] cost = new String[1];
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private SeekBar volumeControl = null;
    TextView distanceTxt;
    int progressChanged1 = 0;

//    private Button btnReportLoad;
    private ImageView imgRepNotFound;
    String myValue;
    StringBuilder URL_Report;

    List<ServiceProviderDetailsModel> listDetails = new ArrayList<ServiceProviderDetailsModel>();

    SharedPreference sharedPreference = new SharedPreference();
    String cap;
    int _TransactionId = -1;
    ImageView imageView;
    String strtext="";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_received_order_list,null);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        recyclerView = (RecyclerView) v.findViewById(R.id.rep_recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        imageView=(ImageView)v.findViewById(R.id.image_View);

        imgRepNotFound = (ImageView) v.findViewById(R.id.img_rep_not_found);
        imgRepNotFound.setVisibility(View.GONE);


        volumeControl = (SeekBar) v.findViewById(R.id.volume_bar);
        volumeControl.setVisibility(View.GONE);
        distanceTxt=(TextView)v.findViewById(R.id.distanceTxt);
        distanceTxt.setVisibility(View.GONE);

        strtext = getArguments().getString("key");

        if (sharedPreference.getValue( getActivity(), Constants.PREF_USER_ROLE, Constants.PREF_USER_ROLE ).equalsIgnoreCase("customer")){
//            CustomerActivity.navigation.setVisibility(View.GONE);
//            getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

            CustomerActivity.navigation.getMenu().findItem(R.id.chooseOrderType).setChecked(false);
            CustomerActivity.navigation.getMenu().findItem(R.id.postOrder).setEnabled(false);
            CustomerActivity.navigation.getMenu().findItem(R.id.NearbyServiceProviderList).setEnabled(false);

             if(strtext.equalsIgnoreCase("Pending Delivery Customer"))
                new OutForDelivery().execute();
             else  if(strtext.equalsIgnoreCase("Completed"))
                 new RetrieveFeedTaskCompletedCustomer().execute();
             else  if(strtext.equalsIgnoreCase("Canceled"))
                 new RetrieveFeedTaskCanceledCustomer().execute();
             else  if(strtext.equalsIgnoreCase("Hold"))
                 new RetrieveFeedTaskOnHoldCustomer().execute();
             else
                 new RetrieveFeedTask1().execute();

        }else{
            if(strtext.equalsIgnoreCase("Pending Delivery"))
                new RetrieveFeedTask3().execute();
            else  if(strtext.equalsIgnoreCase("Completed"))
                new RetrieveFeedTaskCompleted().execute();
            else  if(strtext.equalsIgnoreCase("Canceled"))
                new RetrieveFeedTaskCanceled().execute();
            else  if(strtext.equalsIgnoreCase("Hold"))
                new RetrieveFeedTaskOnHold().execute();
            else
                new RetrieveFeedTask2().execute();

        }
        return v;
    }


   /* @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.report_load_btn:

                new RetrieveFeedTask1().execute();

                break;
        }
    }*/


    class RetrieveFeedTask1 extends AsyncTask<Void, Void, String> {

        protected void onPreExecute() {
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Void... urls) {

            Utilities utilities = new Utilities(getContext());

            String address = Constants.API_SERVICE_PROVIDER_LIST_USING_ORDER_STATUS;
            Map<String, String> params = new HashMap<>();
            params.put("userid", sharedPreference.getValue( getActivity(), Constants.PREF_IS_USER, Constants.PREF_KEY_USER_ID ));
//            params.put("userid", "14");
            params.put("status", "Pending");

            return utilities.apiCalls(address,params);
        }

        protected void onPostExecute(String response) {
            try {
                JSONObject jsonObject1 = new JSONObject(response);
                JSONObject jsonObject2 = new JSONObject(jsonObject1.getString("Content"));
                imgRepNotFound.setVisibility(View.GONE);

                if (response.equals("NO_INTERNET")) {
                    Toast.makeText(getActivity().getBaseContext(), "Check internet connection", Toast.LENGTH_LONG).show();
                } else if (jsonObject2.getString("status").equalsIgnoreCase("error")) {
                    imgRepNotFound.setVisibility(View.VISIBLE);

                    final Animation animImgRecordNotFound = AnimationUtils.loadAnimation(getActivity(), R.anim.shake_interpolator);
                    imgRepNotFound.startAnimation(animImgRecordNotFound);

//                    btnReportLoad.setVisibility(View.GONE);
//                    Toast.makeText(getActivity().getBaseContext(), "Somthing went wrong", Toast.LENGTH_LONG).show();
                } else {


                    if (listDetails.size() > 0) {
                       listDetails.clear();
                    }


                    JSONArray jsonarray = new JSONArray(jsonObject2.getString("result"));
                    if (jsonarray.length() <= 0) {
//                        btnReportLoad.setVisibility(View.GONE);
                        Toast.makeText(getActivity().getBaseContext(), "No more record found.", Toast.LENGTH_SHORT).show();
                    }

                    String listOfMedicalUsers="";

                    for (int i = 0; i < jsonarray.length(); i++) {

                        JSONObject jsonObject = jsonarray.getJSONObject(i);

                        //                            2018-09-22 03:48:19
                        String s = jsonObject.getString("createddate");
//                        String s ="2018-09-22 03:48:19";
                        String[] s1 = s.split("\\s+");
                        String s2 = s1[0];

                        Date initDate = new SimpleDateFormat("yyyy-MM-dd").parse(s2);
                        SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
                        String parsedDate = formatter.format(initDate) + " "+ s1[1];

                        JSONObject jsonObjectMedicalids = null;
                        try {
                            JSONObject json = new JSONObject(jsonObject.getString("medicalids"));
                            Iterator<String> temp = json.keys();
                            while (temp.hasNext()) {
                                String key = temp.next();
                                Object value = json.get(key);

                                jsonObjectMedicalids= new JSONObject((String.valueOf(value)));
//                                {"id":"11","userid":"14","orderid":"ORD000009","latitude":"12.1516516541","longitude":"15.65656565","description":"test order","imagepath":"http:\/\/googel.com","address":"NAshik","mobile":"7412589630"

                                if(jsonObjectMedicalids.getString("medicalconfirm").equalsIgnoreCase("0")
                                        &&jsonObjectMedicalids.getString("customerconfirm").equalsIgnoreCase("0")
                                        && !jsonObjectMedicalids.getString("cost").equalsIgnoreCase("")
                                        && jsonObjectMedicalids.getString("cost")!=(null)
                                        && !jsonObjectMedicalids.getString("cost").equalsIgnoreCase("null")) {

                                    ServiceProviderDetailsModel serviceProviderDetails1 = new ServiceProviderDetailsModel();
                                    serviceProviderDetails1.setCustomerId(jsonObject.getString("userid"));
                                    serviceProviderDetails1.setOrderid(jsonObject.getString("orderid"));
                                    serviceProviderDetails1.setOrderMainId(jsonObject.getString("id"));
                                    serviceProviderDetails1.setDescription(jsonObject.getString("description"));

                                    String string = jsonObject.getString("address");
                                    String[] bits = string.split(",");
                                    String lastWord = "";
                                    if(bits.length>2)
                                        lastWord = bits[bits.length - 3] + ", " + bits[bits.length - 2] + ", " + bits[bits.length - 1];

                                    serviceProviderDetails1.setAddress(lastWord);


//                                    serviceProviderDetails1.setAddress(jsonObject.getString("address"));
                                    serviceProviderDetails1.setNotificationTime(parsedDate);
                                    serviceProviderDetails1.setStatus("Pending");

                                    serviceProviderDetails1.setMedicalId(jsonObjectMedicalids.getString("medicalid"));
                                    serviceProviderDetails1.setMedicalReply(jsonObjectMedicalids.getString("medicalreply"));
                                    serviceProviderDetails1.setMedicalCost(jsonObjectMedicalids.getString("cost"));
                                    serviceProviderDetails1.setMedicalProfileUrl(jsonObjectMedicalids.getString("medicalurl"));
                                    serviceProviderDetails1.setImagepath(jsonObject.getString("imagepath"));


                                    listDetails.add(serviceProviderDetails1);
                                }


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }



                    if (listDetails.size() <= 0) {
                        imgRepNotFound.setVisibility(View.VISIBLE);

                        final Animation animImgRecordNotFound = AnimationUtils.loadAnimation(getActivity(), R.anim.shake_interpolator);
                        imgRepNotFound.startAnimation(animImgRecordNotFound);
//                        btnReportLoad.setVisibility(View.GONE);
                    }
                    if (listDetails.size() > 0) {
                        adapter = new ServiceProviderReportCardAdapter(getContext(), listDetails);
                        recyclerView.setAdapter(adapter);
                    }
                }
            }

            catch (Exception e1) {
                Toast.makeText( getActivity().getApplicationContext(), "Please try again later...", Toast.LENGTH_LONG).show();
            }
            finally {
                progressDialog.dismiss();
            }
        }
    }




    class RetrieveFeedTask2 extends AsyncTask<Void, Void, String> {

        protected void onPreExecute() {
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Void... urls) {

            Utilities utilities = new Utilities(getContext());

            String address = Constants.API_RECEIVED_ORDER_STATUS;
            Map<String, String> params = new HashMap<>();
            params.put("userid", sharedPreference.getValue( getActivity(), Constants.PREF_IS_USER, Constants.PREF_KEY_USER_ID ));
//            params.put("userid", "21");
            params.put("status", "Pending");

            return utilities.apiCalls(address,params);
        }

        protected void onPostExecute(String response) {
            try {
                JSONObject jsonObject1 = new JSONObject(response);
                JSONObject jsonObject2 = new JSONObject(jsonObject1.getString("Content"));
                imgRepNotFound.setVisibility(View.GONE);

                if (response.equals("NO_INTERNET")) {
                    Toast.makeText(getActivity().getBaseContext(), "Check internet connection", Toast.LENGTH_LONG).show();
                } else if (jsonObject2.getString("status").equalsIgnoreCase("error")) {
                    imgRepNotFound.setVisibility(View.VISIBLE);

                    final Animation animImgRecordNotFound = AnimationUtils.loadAnimation(getActivity(), R.anim.shake_interpolator);
                    imgRepNotFound.startAnimation(animImgRecordNotFound);

//                    btnReportLoad.setVisibility(View.GONE);
//                    Toast.makeText(getActivity().getBaseContext(), "Somthing went wrong", Toast.LENGTH_LONG).show();
                } else {


                    if (listDetails.size() > 0) {
                        listDetails.clear();
                    }


                    JSONArray jsonarray = new JSONArray(jsonObject2.getString("result"));
                    if (jsonarray.length() <= 0) {
//                        btnReportLoad.setVisibility(View.GONE);
                        Toast.makeText(getActivity().getBaseContext(), "No more record found.", Toast.LENGTH_SHORT).show();
                    }

                    String listOfMedicalUsers="";

                    for (int i = 0; i < jsonarray.length(); i++) {

                        JSONObject jsonObject = jsonarray.getJSONObject(i);

                        //                            2018-09-22 03:48:19
                        String s = jsonObject.getString("created_at");
//                        String s ="2018-09-22 03:48:19";
                        String[] s1 = s.split("\\s+");
                        String s2 = s1[0];

                        Date initDate = new SimpleDateFormat("yyyy-MM-dd").parse(s2);
                        SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
                        String parsedDate = formatter.format(initDate) + " "+ s1[1];


                        try {

                            if(( jsonObject.getString("customerconfirm").equalsIgnoreCase("1")
                                    && !jsonObject.getString("medicalconfirm").equalsIgnoreCase("1"))) {

                                ServiceProviderDetailsModel serviceProviderDetails1 = new ServiceProviderDetailsModel();
                                serviceProviderDetails1.setLatitude(jsonObject.getString("latitude"));
                                serviceProviderDetails1.setLongitude(jsonObject.getString("longitude"));
                                serviceProviderDetails1.setOrderMainId(jsonObject.getString("orderid"));
                                serviceProviderDetails1.setMedicalCost(jsonObject.getString("cost"));
                                serviceProviderDetails1.setMedicalReply(jsonObject.getString("medicalreply"));
                                serviceProviderDetails1.setOrderid(jsonObject.getString("mainorderid"));
                                serviceProviderDetails1.setDescription(jsonObject.getString("description"));
                                serviceProviderDetails1.setImagepath(jsonObject.getString("imagepath"));

                                String string = jsonObject.getString("address");
                                String[] bits = string.split(",");
                                String lastWord = "";
                                if(bits.length>2)
                                    lastWord = bits[bits.length - 3] + ", " + bits[bits.length - 2] + ", " + bits[bits.length - 1];

                                serviceProviderDetails1.setAddress(lastWord);

//                                serviceProviderDetails1.setAddress(jsonObject.getString("address"));
                                serviceProviderDetails1.setMobile(jsonObject.getString("mobile"));
                                serviceProviderDetails1.setOrderstatus(jsonObject.getString("orderstatus"));
                                serviceProviderDetails1.setCustomerId(jsonObject.getString("userid"));
                                serviceProviderDetails1.setNotificationTime(parsedDate);
                                serviceProviderDetails1.setStatus("Pending");
                                serviceProviderDetails1.setMedicalId(sharedPreference.getValue(getActivity(), Constants.PREF_IS_USER, Constants.PREF_KEY_USER_ID));

                                listDetails.add(serviceProviderDetails1);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }



                    if (listDetails.size() <= 0) {
                        imgRepNotFound.setVisibility(View.VISIBLE);

                        final Animation animImgRecordNotFound = AnimationUtils.loadAnimation(getActivity(), R.anim.shake_interpolator);
                        imgRepNotFound.startAnimation(animImgRecordNotFound);

//                        btnReportLoad.setVisibility(View.GONE);
                    }
                    if (listDetails.size() > 0) {
                        adapter = new ServiceProviderReportCardAdapter(getContext(), listDetails);
                        recyclerView.setAdapter(adapter);
                    }
                }
            }

            catch (Exception e1) {
                Toast.makeText( getActivity().getApplicationContext(), "Please try again later...", Toast.LENGTH_LONG).show();
            }
            finally {
                progressDialog.dismiss();
            }
        }
    }


    class RetrieveFeedTask3 extends AsyncTask<Void, Void, String> {

        protected void onPreExecute() {
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Void... urls) {

            Utilities utilities = new Utilities(getContext());

            String address = Constants.API_RECEIVED_ORDER_STATUS;
            Map<String, String> params = new HashMap<>();
            params.put("userid", sharedPreference.getValue( getActivity(), Constants.PREF_IS_USER, Constants.PREF_KEY_USER_ID ));
//            params.put("userid", "21");
            params.put("status", "Pending");

            return utilities.apiCalls(address,params);
        }

        protected void onPostExecute(String response) {
            try {
                JSONObject jsonObject1 = new JSONObject(response);
                JSONObject jsonObject2 = new JSONObject(jsonObject1.getString("Content"));
                imgRepNotFound.setVisibility(View.GONE);

                if (response.equals("NO_INTERNET")) {
                    Toast.makeText(getActivity().getBaseContext(), "Check internet connection", Toast.LENGTH_LONG).show();
                } else if (jsonObject2.getString("status").equalsIgnoreCase("error")) {
                    imgRepNotFound.setVisibility(View.VISIBLE);

                    final Animation animImgRecordNotFound = AnimationUtils.loadAnimation(getActivity(), R.anim.shake_interpolator);
                    imgRepNotFound.startAnimation(animImgRecordNotFound);

//                    btnReportLoad.setVisibility(View.GONE);
//                    Toast.makeText(getActivity().getBaseContext(), "Somthing went wrong", Toast.LENGTH_LONG).show();
                } else {


                    if (listDetails.size() > 0) {
                        listDetails.clear();
                    }


                    JSONArray jsonarray = new JSONArray(jsonObject2.getString("result"));
                    if (jsonarray.length() <= 0) {
//                        btnReportLoad.setVisibility(View.GONE);
                        Toast.makeText(getActivity().getBaseContext(), "No more record found.", Toast.LENGTH_SHORT).show();
                    }

                    String listOfMedicalUsers="";

                    for (int i = 0; i < jsonarray.length(); i++) {

                        JSONObject jsonObject = jsonarray.getJSONObject(i);

                        //                            2018-09-22 03:48:19
                        String s = jsonObject.getString("created_at");
//                        String s ="2018-09-22 03:48:19";
                        String[] s1 = s.split("\\s+");
                        String s2 = s1[0];

                        Date initDate = new SimpleDateFormat("yyyy-MM-dd").parse(s2);
                        SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
                        String parsedDate = formatter.format(initDate) + " "+ s1[1];


                        try {

                            if(( jsonObject.getString("customerconfirm").equalsIgnoreCase("1")
                                    && jsonObject.getString("medicalconfirm").equalsIgnoreCase("1")
                                    && jsonObject.getString("orderstatus").equalsIgnoreCase("Pending"))) {

                                ServiceProviderDetailsModel serviceProviderDetails1 = new ServiceProviderDetailsModel();
                                serviceProviderDetails1.setLatitude(jsonObject.getString("latitude"));
                                serviceProviderDetails1.setLongitude(jsonObject.getString("longitude"));
                                serviceProviderDetails1.setOrderMainId(jsonObject.getString("orderid"));
                                serviceProviderDetails1.setMedicalCost(jsonObject.getString("cost"));
                                serviceProviderDetails1.setMedicalReply(jsonObject.getString("medicalreply"));
                                serviceProviderDetails1.setOrderid(jsonObject.getString("mainorderid"));
                                serviceProviderDetails1.setDescription(jsonObject.getString("description"));
                                serviceProviderDetails1.setImagepath(jsonObject.getString("imagepath"));

                                String string = jsonObject.getString("address");
                                String[] bits = string.split(",");
                                String lastWord = "";
                                if(bits.length>2)
                                    lastWord = bits[bits.length - 3] + ", " + bits[bits.length - 2] + ", " + bits[bits.length - 1];

                                serviceProviderDetails1.setAddress(lastWord);

//                                serviceProviderDetails1.setAddress(jsonObject.getString("address"));
                                serviceProviderDetails1.setMobile(jsonObject.getString("mobile"));
                                serviceProviderDetails1.setOrderstatus(jsonObject.getString("orderstatus"));
                                serviceProviderDetails1.setCustomerId(jsonObject.getString("userid"));
                                serviceProviderDetails1.setNotificationTime(parsedDate);
                                serviceProviderDetails1.setStatus("Pending");
                                serviceProviderDetails1.setMedicalId(sharedPreference.getValue(getActivity(), Constants.PREF_IS_USER, Constants.PREF_KEY_USER_ID));

                                listDetails.add(serviceProviderDetails1);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    if (listDetails.size() <= 0) {
                        imgRepNotFound.setVisibility(View.VISIBLE);

                        final Animation animImgRecordNotFound = AnimationUtils.loadAnimation(getActivity(), R.anim.shake_interpolator);
                        imgRepNotFound.startAnimation(animImgRecordNotFound);

//                        btnReportLoad.setVisibility(View.GONE);
                    }
                    if (listDetails.size() > 0) {
                        adapter = new ServiceProviderReportCardAdapter(getContext(), listDetails);
                        recyclerView.setAdapter(adapter);
                    }
                }
            }

            catch (Exception e1) {
                Toast.makeText( getActivity().getApplicationContext(), "Please try again later...", Toast.LENGTH_LONG).show();
            }
            finally {
                progressDialog.dismiss();
            }
        }
    }

//*************  Adapter Class*************//

    public class ServiceProviderReportCardAdapter extends RecyclerView.Adapter<ServiceProviderReportCardAdapter.ViewHolder> {

        ServiceProviderReportCardAdapter serviceProviderReportCardAdapter = this;
        private Context context;
        private MedicalResponseOfCostListFragment.RetrieveFeedTask1 context1;

        List<ServiceProviderDetailsModel> listServiceProviderDetails;

        public ServiceProviderReportCardAdapter(Context context, List<ServiceProviderDetailsModel> listServiceProviderDetails) {
//            super();
            this.context = context;
            this.listServiceProviderDetails = listServiceProviderDetails;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.received_order_card_item, parent, false);
            ViewHolder viewHolder = new ViewHolder(v);

            final Animation anim_record_item = AnimationUtils.loadAnimation(parent.getContext(), R.anim.swipe_down);
            viewHolder.itemView.startAnimation(anim_record_item);

//            linearLayoutTxCardItem = (LinearLayout) v.findViewById(R.id.linear_layout_tx_card_item);
            return viewHolder;
        }


        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            try {

                ServiceProviderDetailsModel tr = listServiceProviderDetails.get(position);
                holder.vtxtLocation.setText("Location : "+tr.getAddress());
                holder.vtxtTime.setText("Date : "+tr.getNotificationTime());
                if(!tr.getImagepath().equalsIgnoreCase("")&& tr.getImagepath()!=null && !tr.getImagepath().equalsIgnoreCase("no_avatar.jpg")) {
//                    Glide.with(context).load(tr.getImagepath()).into(holder.imageViews);
//                    holder.imageViews.setImageResource(android.R.color.transparent);


                    RequestOptions options = new RequestOptions()
                            .centerCrop()
                            .placeholder(R.drawable.profile_pic)
//                            .error(R.drawable.ic_pic_error)
                            .priority(Priority.HIGH);

                    new GlideImageLoader(holder.imageViews,
                            holder.getProgressBar()).load(tr.getImagepath(),options);


                }/*else if(!tr.getImagepath().equalsIgnoreCase("")&& tr.getImagepath()!=null) {
//                    Glide.with(context).load(NO_AVATAR_IMAGE_PATH+tr.getImagepath()).into(holder.imageViews);
//                    holder.imageViews.setImageResource(android.R.color.transparent);

                RequestOptions options = new RequestOptions()
                        .centerCrop()
                        .placeholder(R.drawable.profile_pic)
//                            .error(R.drawable.ic_pic_error)
                        .priority(Priority.HIGH);

                new GlideImageLoader(holder.imageViews,
                        holder.getProgressBar()).load(NO_AVATAR_IMAGE_PATH+tr.getImagepath(),options);

            }*/else{
                    RequestOptions options = new RequestOptions()
                            .centerCrop()
                            .placeholder(R.drawable.profile_pic)
//                            .error(R.drawable.ic_pic_error)
                            .priority(Priority.HIGH);

                    new GlideImageLoader(holder.imageViews,
                            holder.spinner).load(NO_AVATAR_IMAGE_PATH+"no_avatar.jpg",options);
            }
//                    Glide.with(context).load(NO_AVATAR_IMAGE_PATH+tr.getMedicalProfileUrl()).into(holder.imageViews);


//                tr.setStatus("pending");
                switch (tr.getStatus()) {
                    case "Pending":
                        holder.vtxtStatus.setText("PENDING");
                        holder.vtxtStatus.setTextColor(getActivity().getApplicationContext().getResources().getColor(R.color.tx_FAILURE));
//                        linearLayoutTxCardItem.setBackgroundColor(getActivity().getApplicationContext().getResources().getColor(R.color.bg_FAILURE));
                        holder.cardViewTxCardItem.setCardBackgroundColor(Color.parseColor("#ffffff"));
                        break;
                    case "Completed":
                        holder.vtxtStatus.setText("COMPLETED");
                        holder.vtxtStatus.setTextColor(getActivity().getApplicationContext().getResources().getColor(R.color.tx_SUCCESS));
//                        linearLayoutTxCardItem.setBackgroundColor(getActivity().getApplicationContext().getResources().getColor(R.color.bg_SUCCESS));
                        holder.cardViewTxCardItem.setCardBackgroundColor(Color.parseColor("#ffffff"));
                        break;

                    case "Canceled":
                        holder.vtxtStatus.setText("CANCELED");
                        holder.vtxtStatus.setTextColor(getActivity().getApplicationContext().getResources().getColor(R.color.tx_SUCCESS));
//                        linearLayoutTxCardItem.setBackgroundColor(getActivity().getApplicationContext().getResources().getColor(R.color.bg_SUCCESS));
                        holder.cardViewTxCardItem.setCardBackgroundColor(Color.parseColor("#ffffff"));
                        break;


                    case "Hold":
                        holder.vtxtStatus.setText("ON HOLD");
                        holder.vtxtStatus.setTextColor(getActivity().getApplicationContext().getResources().getColor(R.color.tx_SUCCESS));
//                        linearLayoutTxCardItem.setBackgroundColor(getActivity().getApplicationContext().getResources().getColor(R.color.bg_SUCCESS));
                        holder.cardViewTxCardItem.setCardBackgroundColor(Color.parseColor("#ffffff"));
                        break;

                }
            } catch (Exception e1)
            {
            }
        }

        @Override
        public int getItemCount() {   return listServiceProviderDetails.size();   }

        class ViewHolder extends RecyclerView.ViewHolder {

            public TextView vtxtLocation;
            public TextView vtxtStatus;
            public CardView cardViewTxCardItem;
            public ImageView imageViews;
            public ProgressBar spinner;
            public TextView vtxtTime;
//            public String s,s1;



            public ViewHolder(View itemView) {
                super(itemView);

                final View view = itemView;
                vtxtLocation = (TextView) itemView.findViewById(R.id.location);
                vtxtTime = (TextView) itemView.findViewById(R.id.time);
                vtxtStatus = (TextView) itemView.findViewById(R.id.status);
                imageViews=(ImageView)itemView.findViewById(R.id.image_View);

//                vtxtViewDetails = (TextView) itemView.findViewById(R.id.recharge_details);
                spinner = (ProgressBar)itemView.findViewById(R.id.progressBar1);
                setProgressBar(spinner);
                cardViewTxCardItem = (CardView) itemView.findViewById(R.id.cardview_tx_card_item);


                imageViews.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        final ServiceProviderDetailsModel tr = listServiceProviderDetails.get(getAdapterPosition());

//                        Toast.makeText(getContext(),"hello",Toast.LENGTH_LONG).show();
                        SingleTouchImageViewFragment ldf1 = new SingleTouchImageViewFragment();
                        Bundle args1 = new Bundle();
                        args1.putString("position1", String.valueOf(tr.getImagepath()));

                        ldf1.setArguments(args1);

                        getFragmentManager().beginTransaction().replace(R.id.containerView, ldf1,"C").addToBackStack(null).commit();
                    }
                });

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final ServiceProviderDetailsModel tr = listServiceProviderDetails.get(getAdapterPosition());


                        serviceProviderDetails.setCustomerId(tr.getCustomerId());
                        serviceProviderDetails.setOrderMainId(tr.getOrderMainId());
                        serviceProviderDetails.setMedicalId(tr.getMedicalId());

                       /* s=tr.getEmailId();
                        s1=tr.getPassword();*/

//                        _TransactionId = tr.ID;

                        String lStatus = "";
                        switch ( tr.getStatus()) {
                            case "Pending":
                                lStatus = "PENDING";
                                break;
                            case "Completed":
                                lStatus = "COMPLETED";
                                break;
                            case "Canceled":
                                lStatus = "CANCELED";
                                break;

                            case "Hold":
                                lStatus = "ON HOLD";
                                break;

                        }

                        final android.support.v7.app.AlertDialog.Builder alertDialogBuilder1 = new android.support.v7.app.AlertDialog.Builder(getActivity(),R.style.AppCompatAlertDialogStyle );

                        android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(getActivity(),R.style.AppCompatAlertDialogStyle );
                        alertDialogBuilder.setTitle(Html.fromHtml(getString(R.string.transactions)));


                        if(strtext.equalsIgnoreCase("Pending Delivery")){
                            alertDialogBuilder.setMessage(
                                    Html.fromHtml(getString(R.string.description)) + tr.getOrderid() +
                                            "\n"+Html.fromHtml(getString(R.string.description)) + tr.getDescription()+
                                            "\n"+Html.fromHtml(getString(R.string.medicalcost)) + tr.getMedicalCost() +
                                            "\n"+Html.fromHtml(getString(R.string.medicaldescription)) + tr.getMedicalReply() +
                                            "\n"+Html.fromHtml(getString(R.string.mobileno)) + tr.getMobile() +

//                                        "\n\nStatus : " + tr.getOrderstatus());
                                            "\n"+Html.fromHtml(getString(R.string.download)) );
                        }else{
                            alertDialogBuilder.setMessage(
                                    Html.fromHtml(getString(R.string.orderid)) + tr.getOrderid() +
                                            "\n"+Html.fromHtml(getString(R.string.description)) + tr.getDescription()+
                                            "\n"+Html.fromHtml(getString(R.string.medicalcost)) + tr.getMedicalCost() +
                                            "\n"+Html.fromHtml(getString(R.string.medicaldescription)) + tr.getMedicalReply() +

//                                        "\n\nStatus : " + tr.getOrderstatus());
                                            "\n"+Html.fromHtml(getString(R.string.download)) );
                        }



                        sharedPreference.putValue( getActivity(), Constants.PREF_IS_USER, Constants.PREF_KEY_ORDER_ID,tr.getOrderid() );

                        LinearLayout lv = new LinearLayout(getActivity());
                        LinearLayout.LayoutParams vp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
                        lv.setLayoutParams(vp);
                        ImageView image = new ImageView(getActivity());
//                        LinearLayout.LayoutParams vp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
                        image.setLayoutParams(vp);
                        image.setMaxHeight(10);
                        image.setMaxWidth(10);

                        // other image settings
                        image.setImageDrawable(getResources().getDrawable(R.drawable.down));
                        lv.addView(image);

                        alertDialogBuilder.setView(lv);

                        image.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                String[] s = {tr.getImagepath(),tr.getOrderid()};

                                if(tr.getImagepath()!=null && !tr.getImagepath().equalsIgnoreCase("") && !tr.getImagepath().equalsIgnoreCase("null")){

                                    boolean result= Utility.checkPermission(getContext());
                                    if(result)
                                    new DownloadImage().execute(s);
                                }else{
                                    Toast.makeText( getContext(), "Image Not Available", Toast.LENGTH_LONG).show();
                                }


                            }
                        });

                        if(lStatus.equalsIgnoreCase("Pending")){

                            if(strtext.equalsIgnoreCase("Pending Delivery")){

                                alertDialogBuilder.setPositiveButton("Track",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {

                                              Uri.Builder builder = new Uri.Builder();
                                                builder.scheme("https")
                                                        .authority("www.google.com").appendPath("maps").appendPath("dir").appendPath("").appendQueryParameter("api", "1")
                                                        .appendQueryParameter("destination", tr.getLatitude() + "," + tr.getLongitude());
                                                String url = builder.build().toString();
                                                Log.d("Directions", url);
                                                Intent i = new Intent(Intent.ACTION_VIEW);
                                                i.setData(Uri.parse(url));
                                                startActivity(i);

                                            }
                                        });

                                alertDialogBuilder.setNeutralButton("Order Delivered",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {

                                               new DeliveredOrCancelled().execute("1");

                                            }
                                        });

                                alertDialogBuilder.setNegativeButton("Canceled",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {

                                                new DeliveredOrCancelled().execute("0");

                                            }
                                        });

                            }else if(strtext.equalsIgnoreCase("Pending Delivery Customer")){

                                alertDialogBuilder.setNeutralButton("Order Delivered",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {

                                                new DeliveredOrCancelledCustomer().execute("1");

                                            }
                                        });

                                alertDialogBuilder.setNegativeButton("Canceled",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {

                                                new DeliveredOrCancelledCustomer().execute("0");

                                            }
                                        });

                            }else{
                                alertDialogBuilder.setPositiveButton("Accept",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {

                                                if (sharedPreference.getValue( getActivity(), Constants.PREF_USER_ROLE, Constants.PREF_USER_ROLE ).equalsIgnoreCase("customer")){
                                                    new SendCostConfirmation().execute();
                                                }else{
                                                    new SendFinalConfirmationFromMedicalToCustomer().execute();
                                                }

                                            }
                                        });
                            }

                        }
                        alertDialogBuilder.show();
                    }

                });

            }

            public ProgressBar getProgressBar() {
                return spinner;
            }

            public void setProgressBar(ProgressBar progressBar) {
                spinner = progressBar;
            }

        }
    }

    class DownloadImage extends AsyncTask<String, Void, File> {

        protected void onPreExecute() {
            progressDialog.show();
        }

        @Override
        protected File doInBackground(String... urls) {
            Utilities utilities = new Utilities(getContext());

            String s1,s2;

            s1=urls[0];
            s2=urls[1];

            return utilities.downloadImagesToSdCard(s1,s2);
        }

        protected void onPostExecute(File response) {
            progressDialog.dismiss();
            boolean result= Utility.checkPermission(getContext());
            if(response!=null && result==true)
            viewimage(response);

        }
    }

    public void viewimage(File fileName)
    {
//        String path = serialnumber+".png";
        File mypath =null;
        String selectedOutputPath = "";

       /* File mediaStorageDir = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "My-Chemist");*/
        // Create a storage directory if it does not exist

        // Create a media file name
        selectedOutputPath = fileName != null ? fileName.getPath() :"";

        Toast.makeText(getContext(),"Image is stored in "+selectedOutputPath,Toast.LENGTH_LONG).show();

        Log.d("PhotoEditorSDK", "selected camera path " + selectedOutputPath);
        mypath = new File(selectedOutputPath);


        Bitmap b;
        //            b = BitmapFactory.decodeStream(new FileInputStream(mypath));


        Intent intent = new Intent(Intent.ACTION_VIEW);

        Uri apkURI = FileProvider.getUriForFile(
                getContext(),
                "com.zoftino.android.fileproviders", mypath);
        intent.setDataAndType(apkURI, "image/");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        startActivity(intent);

//            _context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(String.valueOf(mypath))));
    }

    class SendCostConfirmation extends AsyncTask<Void, Void, String> {

        protected void onPreExecute() {
            progressDialog.show();
        }

        protected String doInBackground(Void... urls) {

            Utilities utilities = new Utilities(getContext());

            String address = Constants.API_MEDICAL_COST_RESPONCE_STATUS;
            Map<String, String> params = new HashMap<>();
            params.put("userid", serviceProviderDetails.getCustomerId());
            params.put("orderid", serviceProviderDetails.getOrderMainId());
            params.put("medicalid",  serviceProviderDetails.getMedicalId());

            return utilities.apiCalls(address,params);

        }

        protected void onPostExecute(String response) {
            try {

                JSONObject jsonObject1 = new JSONObject(response);
                JSONObject jsonObject2 = new JSONObject(jsonObject1.getString("Content"));

//				{"Content":"{\"status\":\"success\",\"result\":{\"id\":\"28\",\"firstname\":\"Mangesh\",\"lastname\":\"Khalale\",\"shopname\":\"Mango\",\"email\":\"mangesh@gmail.com\",\"password\":\"111\",\"mobile\":\"8793377994\",\"address\":\"Pathardi Phata,Pathardi Phata, Nashik, Maharashtra, India\",\"latitude\":\"19.946922\",\"longitude\":\"73.7654367\",\"profilepic\":\"no_avatar.jpg\",\"role\":\"medical\",\"regdate\":\"2018-07-27 10:44:22\",\"status\":\"0\",\"deletestatus\":null,\"loginstatus\":\"1\",\"otp\":null}}","Message":"OK","Length":-1,"Type":"text\/html; charset=UTF-8"}
                if(response.equals("NO_INTERNET")) {
                    Toast.makeText(getContext(), "Check internet connection", Toast.LENGTH_LONG).show();
                }
                else if(jsonObject2.getString("status").equalsIgnoreCase("error")) {
                    Toast.makeText(getContext(), jsonObject2.getString("message"), Toast.LENGTH_LONG).show();
                }
                else
                {
                    if(jsonObject2.getString("status").equalsIgnoreCase("error")) {
                        Toast.makeText(getContext(), jsonObject2.getString("message"), Toast.LENGTH_LONG).show();
                    }
                    else if(jsonObject2.getString("status").equalsIgnoreCase("success")) {
                        Toast.makeText(getContext(), jsonObject2.getString("status"), Toast.LENGTH_LONG).show();

                        new RetrieveFeedTask1().execute();
                    }
                }

            } catch (Exception e) {
                Toast.makeText( getContext(), "Please try again later...", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
            finally {
                progressDialog.dismiss();
            }

        }
    }

    class SendFinalConfirmationFromMedicalToCustomer extends AsyncTask<Void, Void, String> {

        protected void onPreExecute() {
            progressDialog.show();
        }

        protected String doInBackground(Void... urls) {

            Utilities utilities = new Utilities(getContext());

            String address = Constants.API_MEDICAL_FINAL_CONFIRMATION;
            Map<String, String> params = new HashMap<>();
            params.put("userid", serviceProviderDetails.getCustomerId());
//            params.put("userid", "14");
            params.put("orderid", serviceProviderDetails.getOrderMainId());
            params.put("medicalid",  serviceProviderDetails.getMedicalId());
//            params.put("medicalid", "23");

            return utilities.apiCalls(address,params);

        }

        protected void onPostExecute(String response) {
            try {

                JSONObject jsonObject1 = new JSONObject(response);
                JSONObject jsonObject2 = new JSONObject(jsonObject1.getString("Content"));

                if(response.equals("NO_INTERNET")) {
                    Toast.makeText(getContext(), "Check internet connection", Toast.LENGTH_LONG).show();
                }
                else if(jsonObject2.getString("status").equalsIgnoreCase("error")) {
                    Toast.makeText(getContext(), jsonObject2.getString("message"), Toast.LENGTH_LONG).show();
                }
                else
                {
                    if(jsonObject2.getString("status").equalsIgnoreCase("error")) {
                        Toast.makeText(getContext(), jsonObject2.getString("message"), Toast.LENGTH_LONG).show();
                    }
                    else if(jsonObject2.getString("status").equalsIgnoreCase("success")) {
                        Toast.makeText(getContext(), jsonObject2.getString("status"), Toast.LENGTH_LONG).show();

                        new RetrieveFeedTask2().execute();
                    }
                }

            } catch (Exception e) {
                Toast.makeText( getContext(), "Please try again later...", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
            finally {
                progressDialog.dismiss();
            }

        }
    }


    class DeliveredOrCancelled extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
            progressDialog.show();
        }

        protected String doInBackground(String... urls) {

            Utilities utilities = new Utilities(getContext());
//remaining

            String s1;

            s1=urls[0];


            String address = Constants.API_UPDATE_ORDER_STATUS;
            Map<String, String> params = new HashMap<>();
            params.put("userid", sharedPreference.getValue( getActivity(), Constants.PREF_IS_USER, Constants.PREF_KEY_USER_ID ));
            params.put("tblorderid", serviceProviderDetails.getOrderMainId());
            params.put("orderid",  sharedPreference.getValue( getActivity(), Constants.PREF_IS_USER, Constants.PREF_KEY_ORDER_ID ));
            params.put("isdeliverd", s1);
            params.put("type", "medical");



            return utilities.apiCalls(address,params);

        }

        protected void onPostExecute(String response) {
            try {

                JSONObject jsonObject1 = new JSONObject(response);
                JSONObject jsonObject2 = new JSONObject(jsonObject1.getString("Content"));

                if(response.equals("NO_INTERNET")) {
                    Toast.makeText(getContext(), "Check internet connection", Toast.LENGTH_LONG).show();
                }
                else if(jsonObject2.getString("status").equalsIgnoreCase("error")) {
                    Toast.makeText(getContext(), jsonObject2.getString("message"), Toast.LENGTH_LONG).show();
                }
                else
                {
                    if(jsonObject2.getString("status").equalsIgnoreCase("error")) {
                        Toast.makeText(getContext(), jsonObject2.getString("message"), Toast.LENGTH_LONG).show();
                    }
                    else if(jsonObject2.getString("status").equalsIgnoreCase("success")) {
                        Toast.makeText(getContext(), jsonObject2.getString("status"), Toast.LENGTH_LONG).show();
                    }
                }

            } catch (Exception e) {
                Toast.makeText( getContext(), "Please try again later...", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
            finally {
                progressDialog.dismiss();
            }
        }
    }

    class OutForDelivery extends AsyncTask<Void, Void, String> {

        protected void onPreExecute() {
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Void... urls) {

            Utilities utilities = new Utilities(getContext());

            String address = Constants.API_SERVICE_PROVIDER_LIST_USING_ORDER_STATUS;
            Map<String, String> params = new HashMap<>();
            params.put("userid", sharedPreference.getValue( getActivity(), Constants.PREF_IS_USER, Constants.PREF_KEY_USER_ID ));
//            params.put("userid", "21");
            params.put("status", "Pending");

            return utilities.apiCalls(address,params);
        }

        protected void onPostExecute(String response) {
            try {
                JSONObject jsonObject1 = new JSONObject(response);
                JSONObject jsonObject2 = new JSONObject(jsonObject1.getString("Content"));
                imgRepNotFound.setVisibility(View.GONE);

                if (response.equals("NO_INTERNET")) {
                    Toast.makeText(getActivity().getBaseContext(), "Check internet connection", Toast.LENGTH_LONG).show();
                } else if (jsonObject2.getString("status").equalsIgnoreCase("error")) {
                    imgRepNotFound.setVisibility(View.VISIBLE);

                    final Animation animImgRecordNotFound = AnimationUtils.loadAnimation(getActivity(), R.anim.shake_interpolator);
                    imgRepNotFound.startAnimation(animImgRecordNotFound);
//                    btnReportLoad.setVisibility(View.GONE);
//                    Toast.makeText(getActivity().getBaseContext(), "Somthing went wrong", Toast.LENGTH_LONG).show();
                } else {


                    if (listDetails.size() > 0) {
                        listDetails.clear();
                    }


                    JSONArray jsonarray = new JSONArray(jsonObject2.getString("result"));
                    if (jsonarray.length() <= 0) {
//                        btnReportLoad.setVisibility(View.GONE);
                        Toast.makeText(getActivity().getBaseContext(), "No more record found.", Toast.LENGTH_SHORT).show();
                    }

                    String listOfMedicalUsers="";

                    /*for (int i = 0; i < jsonarray.length(); i++) {

                        JSONObject jsonObject = jsonarray.getJSONObject(i);

                        try {

                            if(( jsonObject.getString("customerconfirm").equalsIgnoreCase("1")
                                    && jsonObject.getString("medicalconfirm").equalsIgnoreCase("1")
                                    && jsonObject.getString("ordstatus").equalsIgnoreCase("Pending"))) {

                                ServiceProviderDetailsModel serviceProviderDetails1 = new ServiceProviderDetailsModel();
                                serviceProviderDetails1.setLatitude(jsonObject.getString("latitude"));
                                serviceProviderDetails1.setLongitude(jsonObject.getString("longitude"));
                                serviceProviderDetails1.setOrderMainId(jsonObject.getString("orderid"));
                                serviceProviderDetails1.setMedicalCost(jsonObject.getString("cost"));
                                serviceProviderDetails1.setMedicalReply(jsonObject.getString("medicalreply"));
                                serviceProviderDetails1.setOrderid(jsonObject.getString("mainorderid"));
                                serviceProviderDetails1.setDescription(jsonObject.getString("description"));
                                serviceProviderDetails1.setImagepath(jsonObject.getString("imagepath"));
                                serviceProviderDetails1.setAddress(jsonObject.getString("address"));
                                serviceProviderDetails1.setMobile(jsonObject.getString("mobile"));
                                serviceProviderDetails1.setOrderstatus(jsonObject.getString("ordstatus"));
                                serviceProviderDetails1.setCustomerId(jsonObject.getString("userid"));
                                serviceProviderDetails1.setMedicalId(sharedPreference.getValue(getActivity(), Constants.PREF_IS_USER, Constants.PREF_KEY_USER_ID));

                                listDetails.add(serviceProviderDetails1);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }*/

                    for (int i = 0; i < jsonarray.length(); i++) {

                        JSONObject jsonObject = jsonarray.getJSONObject(i);

                        //                            2018-09-22 03:48:19



                        JSONObject jsonObjectMedicalids = null;
                        try {
                            JSONObject json = new JSONObject(jsonObject.getString("medicalids"));
                            Iterator<String> temp = json.keys();
                            while (temp.hasNext()) {
                                String key = temp.next();
                                Object value = json.get(key);

                                jsonObjectMedicalids= new JSONObject((String.valueOf(value)));
//                                {"id":"11","userid":"14","orderid":"ORD000009","latitude":"12.1516516541","longitude":"15.65656565","description":"test order","imagepath":"http:\/\/googel.com","address":"NAshik","mobile":"7412589630"
                                String s = jsonObject.getString("createddate");
//                        String s ="2018-09-22 03:48:19";
                                String[] s1 = s.split("\\s+");
                                String s2 = s1[0];

                                Date initDate = new SimpleDateFormat("yyyy-MM-dd").parse(s2);
                                SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
                                String parsedDate = formatter.format(initDate) + " "+ s1[1];
                                if(jsonObjectMedicalids.getString("medicalconfirm").equalsIgnoreCase("1")
                                        &&jsonObjectMedicalids.getString("customerconfirm").equalsIgnoreCase("1")) {

                                    ServiceProviderDetailsModel serviceProviderDetails1 = new ServiceProviderDetailsModel();
                                    serviceProviderDetails1.setCustomerId(jsonObject.getString("userid"));
                                    serviceProviderDetails1.setOrderid(jsonObject.getString("orderid"));
                                    serviceProviderDetails1.setOrderMainId(jsonObject.getString("id"));
                                    serviceProviderDetails1.setDescription(jsonObject.getString("description"));

                                    String string = jsonObject.getString("address");
                                    String[] bits = string.split(",");
                                    String lastWord = "";
                                    if(bits.length>2)
                                        lastWord = bits[bits.length - 3] + ", " + bits[bits.length - 2] + ", " + bits[bits.length - 1];

                                    serviceProviderDetails1.setAddress(lastWord);

//                                    serviceProviderDetails1.setAddress(jsonObject.getString("address"));
                                    serviceProviderDetails1.setNotificationTime(parsedDate);
                                    serviceProviderDetails1.setStatus("Pending");

                                    serviceProviderDetails1.setMedicalId(jsonObjectMedicalids.getString("medicalid"));
                                    serviceProviderDetails1.setMedicalReply(jsonObjectMedicalids.getString("medicalreply"));
                                    serviceProviderDetails1.setMedicalCost(jsonObjectMedicalids.getString("cost"));
                                    serviceProviderDetails1.setMedicalProfileUrl(jsonObjectMedicalids.getString("medicalurl"));
                                    serviceProviderDetails1.setImagepath(jsonObject.getString("imagepath"));


                                    listDetails.add(serviceProviderDetails1);
                                }


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }

                    if (listDetails.size() <= 0) {
                        imgRepNotFound.setVisibility(View.VISIBLE);

                        final Animation animImgRecordNotFound = AnimationUtils.loadAnimation(getActivity(), R.anim.shake_interpolator);
                        imgRepNotFound.startAnimation(animImgRecordNotFound);

//                        btnReportLoad.setVisibility(View.GONE);
                    }
                    if (listDetails.size() > 0) {
                        adapter = new ServiceProviderReportCardAdapter(getContext(), listDetails);
                        recyclerView.setAdapter(adapter);
                    }
                }
            }

            catch (Exception e1) {
                Toast.makeText( getActivity().getApplicationContext(), "Please try again later...", Toast.LENGTH_LONG).show();
            }
            finally {
                progressDialog.dismiss();
            }
        }
    }



    class DeliveredOrCancelledCustomer extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
            progressDialog.show();
        }

        protected String doInBackground(String... urls) {

            Utilities utilities = new Utilities(getContext());
//remaining

            String s1;

            s1=urls[0];


            String address = Constants.API_UPDATE_ORDER_STATUS;
            Map<String, String> params = new HashMap<>();
            params.put("userid", sharedPreference.getValue( getActivity(), Constants.PREF_IS_USER, Constants.PREF_KEY_USER_ID ));
            params.put("tblorderid", serviceProviderDetails.getOrderMainId());
            params.put("orderid",  sharedPreference.getValue( getActivity(), Constants.PREF_IS_USER, Constants.PREF_KEY_ORDER_ID ));
            params.put("isdeliverd", s1);
            params.put("type", "customer");



            return utilities.apiCalls(address,params);

        }

        protected void onPostExecute(String response) {
            try {

                JSONObject jsonObject1 = new JSONObject(response);
                JSONObject jsonObject2 = new JSONObject(jsonObject1.getString("Content"));

                if(response.equals("NO_INTERNET")) {
                    Toast.makeText(getContext(), "Check internet connection", Toast.LENGTH_LONG).show();
                }
                else if(jsonObject2.getString("status").equalsIgnoreCase("error")) {
                    Toast.makeText(getContext(), jsonObject2.getString("message"), Toast.LENGTH_LONG).show();
                }
                else
                {
                    if(jsonObject2.getString("status").equalsIgnoreCase("error")) {
                        Toast.makeText(getContext(), jsonObject2.getString("message"), Toast.LENGTH_LONG).show();
                    }
                    else if(jsonObject2.getString("status").equalsIgnoreCase("success")) {
                        Toast.makeText(getContext(), jsonObject2.getString("status"), Toast.LENGTH_LONG).show();
//remaining
                        sharedPreference.putValue(getContext(), Constants.PREF_IS_USER, Constants.PREF_KEY_MEDICAL_ID, serviceProviderDetails.getMedicalId());


                        CustomerRatingsFragment fragment2 = new CustomerRatingsFragment();
                        FragmentManager fragmentManager = getFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.containerView, fragment2);
                        fragmentTransaction.commit();

                    }
                }

            } catch (Exception e) {
                Toast.makeText( getContext(), "Please try again later...", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
            finally {
                progressDialog.dismiss();
            }

        }
    }

    //...........................History................................

    class RetrieveFeedTaskCompleted extends AsyncTask<Void, Void, String> {

        protected void onPreExecute() {
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Void... urls) {

            Utilities utilities = new Utilities(getContext());

            String address = Constants.API_RECEIVED_ORDER_STATUS;
            Map<String, String> params = new HashMap<>();
            params.put("userid", sharedPreference.getValue( getActivity(), Constants.PREF_IS_USER, Constants.PREF_KEY_USER_ID ));
//            params.put("userid", "21");
            params.put("status", "Completed");

            return utilities.apiCalls(address,params);
        }

        protected void onPostExecute(String response) {
            try {
                JSONObject jsonObject1 = new JSONObject(response);
                JSONObject jsonObject2 = new JSONObject(jsonObject1.getString("Content"));
                imgRepNotFound.setVisibility(View.GONE);

                if (response.equals("NO_INTERNET")) {
                    Toast.makeText(getActivity().getBaseContext(), "Check internet connection", Toast.LENGTH_LONG).show();
                } else if (jsonObject2.getString("status").equalsIgnoreCase("error")) {
                    imgRepNotFound.setVisibility(View.VISIBLE);

                    final Animation animImgRecordNotFound = AnimationUtils.loadAnimation(getActivity(), R.anim.shake_interpolator);
                    imgRepNotFound.startAnimation(animImgRecordNotFound);

//                    btnReportLoad.setVisibility(View.GONE);
//                    Toast.makeText(getActivity().getBaseContext(), "Somthing went wrong", Toast.LENGTH_LONG).show();
                } else {


                    if (listDetails.size() > 0) {
                        listDetails.clear();
                    }


                    JSONArray jsonarray = new JSONArray(jsonObject2.getString("result"));
                    if (jsonarray.length() <= 0) {
//                        btnReportLoad.setVisibility(View.GONE);
                        Toast.makeText(getActivity().getBaseContext(), "No more record found.", Toast.LENGTH_SHORT).show();
                    }

                    String listOfMedicalUsers="";

                    for (int i = 0; i < jsonarray.length(); i++) {

                        JSONObject jsonObject = jsonarray.getJSONObject(i);


                        //                            2018-09-22 03:48:19
                        String s = jsonObject.getString("created_at");
//                        String s ="2018-09-22 03:48:19";
                        String[] s1 = s.split("\\s+");
                        String s2 = s1[0];

                        Date initDate = new SimpleDateFormat("yyyy-MM-dd").parse(s2);
                        SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
                        String parsedDate = formatter.format(initDate) + " "+ s1[1];

                        try {

                            if(( jsonObject.getString("customerconfirm").equalsIgnoreCase("1")
                                    && jsonObject.getString("medicalconfirm").equalsIgnoreCase("1")
                                    && jsonObject.getString("orderstatus").equalsIgnoreCase("Completed"))) {

                                ServiceProviderDetailsModel serviceProviderDetails1 = new ServiceProviderDetailsModel();
                                serviceProviderDetails1.setLatitude(jsonObject.getString("latitude"));
                                serviceProviderDetails1.setLongitude(jsonObject.getString("longitude"));
                                serviceProviderDetails1.setOrderMainId(jsonObject.getString("orderid"));
                                serviceProviderDetails1.setMedicalCost(jsonObject.getString("cost"));
                                serviceProviderDetails1.setMedicalReply(jsonObject.getString("medicalreply"));
                                serviceProviderDetails1.setOrderid(jsonObject.getString("mainorderid"));
                                serviceProviderDetails1.setNotificationTime(parsedDate);
                                serviceProviderDetails1.setStatus("Completed");
                                serviceProviderDetails1.setDescription(jsonObject.getString("description"));
                                serviceProviderDetails1.setImagepath(jsonObject.getString("imagepath"));

                                String string = jsonObject.getString("address");
                                String[] bits = string.split(",");
                                String lastWord = "";
                                if(bits.length>2)
                                    lastWord = bits[bits.length - 3] + ", " + bits[bits.length - 2] + ", " + bits[bits.length - 1];

                                serviceProviderDetails1.setAddress(lastWord);
//                                serviceProviderDetails1.setAddress(jsonObject.getString("address"));
                                serviceProviderDetails1.setMobile(jsonObject.getString("mobile"));
                                serviceProviderDetails1.setOrderstatus(jsonObject.getString("orderstatus"));
                                serviceProviderDetails1.setCustomerId(jsonObject.getString("userid"));
                                serviceProviderDetails1.setMedicalId(sharedPreference.getValue(getActivity(), Constants.PREF_IS_USER, Constants.PREF_KEY_USER_ID));

                                listDetails.add(serviceProviderDetails1);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    if (listDetails.size() <= 0) {
                        imgRepNotFound.setVisibility(View.VISIBLE);

                        final Animation animImgRecordNotFound = AnimationUtils.loadAnimation(getActivity(), R.anim.shake_interpolator);
                        imgRepNotFound.startAnimation(animImgRecordNotFound);

//                        btnReportLoad.setVisibility(View.GONE);
                    }
                    if (listDetails.size() > 0) {
                        adapter = new ServiceProviderReportCardAdapter(getContext(), listDetails);
                        recyclerView.setAdapter(adapter);
                    }
                }
            }

            catch (Exception e1) {
                Toast.makeText( getActivity().getApplicationContext(), "Please try again later...", Toast.LENGTH_LONG).show();
            }
            finally {
                progressDialog.dismiss();
            }
        }
    }

    class RetrieveFeedTaskCanceled extends AsyncTask<Void, Void, String> {

        protected void onPreExecute() {
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Void... urls) {

            Utilities utilities = new Utilities(getContext());

            String address = Constants.API_RECEIVED_ORDER_STATUS;
            Map<String, String> params = new HashMap<>();
            params.put("userid", sharedPreference.getValue( getActivity(), Constants.PREF_IS_USER, Constants.PREF_KEY_USER_ID ));
//            params.put("userid", "21");
            params.put("status", "Canceled");

            return utilities.apiCalls(address,params);
        }

        protected void onPostExecute(String response) {
            try {
                JSONObject jsonObject1 = new JSONObject(response);
                JSONObject jsonObject2 = new JSONObject(jsonObject1.getString("Content"));
                imgRepNotFound.setVisibility(View.GONE);

                if (response.equals("NO_INTERNET")) {
                    Toast.makeText(getActivity().getBaseContext(), "Check internet connection", Toast.LENGTH_LONG).show();
                } else if (jsonObject2.getString("status").equalsIgnoreCase("error")) {
                    imgRepNotFound.setVisibility(View.VISIBLE);

                    final Animation animImgRecordNotFound = AnimationUtils.loadAnimation(getActivity(), R.anim.shake_interpolator);
                    imgRepNotFound.startAnimation(animImgRecordNotFound);

//                    btnReportLoad.setVisibility(View.GONE);
//                    Toast.makeText(getActivity().getBaseContext(), "Somthing went wrong", Toast.LENGTH_LONG).show();
                } else {


                    if (listDetails.size() > 0) {
                        listDetails.clear();
                    }


                    JSONArray jsonarray = new JSONArray(jsonObject2.getString("result"));
                    if (jsonarray.length() <= 0) {
//                        btnReportLoad.setVisibility(View.GONE);
                        Toast.makeText(getActivity().getBaseContext(), "No more record found.", Toast.LENGTH_SHORT).show();
                    }

                    String listOfMedicalUsers="";

                    for (int i = 0; i < jsonarray.length(); i++) {

                        JSONObject jsonObject = jsonarray.getJSONObject(i);


                        //                            2018-09-22 03:48:19
                        String s = jsonObject.getString("created_at");
//                        String s ="2018-09-22 03:48:19";
                        String[] s1 = s.split("\\s+");
                        String s2 = s1[0];

                        Date initDate = new SimpleDateFormat("yyyy-MM-dd").parse(s2);
                        SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
                        String parsedDate = formatter.format(initDate) + " "+ s1[1];

                        try {

                            if(( jsonObject.getString("customerconfirm").equalsIgnoreCase("1")
                                    && jsonObject.getString("medicalconfirm").equalsIgnoreCase("1")
                                    && jsonObject.getString("orderstatus").equalsIgnoreCase("Canceled"))) {

                                ServiceProviderDetailsModel serviceProviderDetails1 = new ServiceProviderDetailsModel();
                                serviceProviderDetails1.setLatitude(jsonObject.getString("latitude"));
                                serviceProviderDetails1.setLongitude(jsonObject.getString("longitude"));
                                serviceProviderDetails1.setOrderMainId(jsonObject.getString("orderid"));
                                serviceProviderDetails1.setMedicalCost(jsonObject.getString("cost"));
                                serviceProviderDetails1.setMedicalReply(jsonObject.getString("medicalreply"));
                                serviceProviderDetails1.setOrderid(jsonObject.getString("mainorderid"));
                                serviceProviderDetails1.setDescription(jsonObject.getString("description"));
                                serviceProviderDetails1.setImagepath(jsonObject.getString("imagepath"));

                                String string = jsonObject.getString("address");
                                String[] bits = string.split(",");
                                String lastWord = "";
                                if(bits.length>2)
                                    lastWord = bits[bits.length - 3] + ", " + bits[bits.length - 2] + ", " + bits[bits.length - 1];

                                serviceProviderDetails1.setAddress(lastWord);

//                                serviceProviderDetails1.setAddress(jsonObject.getString("address"));
                                serviceProviderDetails1.setNotificationTime(parsedDate);
                                serviceProviderDetails1.setStatus("Canceled");
                                serviceProviderDetails1.setMobile(jsonObject.getString("mobile"));
                                serviceProviderDetails1.setOrderstatus(jsonObject.getString("orderstatus"));
                                serviceProviderDetails1.setCustomerId(jsonObject.getString("userid"));
                                serviceProviderDetails1.setMedicalId(sharedPreference.getValue(getActivity(), Constants.PREF_IS_USER, Constants.PREF_KEY_USER_ID));

                                listDetails.add(serviceProviderDetails1);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    if (listDetails.size() <= 0) {
                        imgRepNotFound.setVisibility(View.VISIBLE);

                        final Animation animImgRecordNotFound = AnimationUtils.loadAnimation(getActivity(), R.anim.shake_interpolator);
                        imgRepNotFound.startAnimation(animImgRecordNotFound);

//                        btnReportLoad.setVisibility(View.GONE);
                    }
                    if (listDetails.size() > 0) {
                        adapter = new ServiceProviderReportCardAdapter(getContext(), listDetails);
                        recyclerView.setAdapter(adapter);
                    }
                }
            }

            catch (Exception e1) {
                Toast.makeText( getActivity().getApplicationContext(), "Please try again later...", Toast.LENGTH_LONG).show();
            }
            finally {
                progressDialog.dismiss();
            }
        }
    }


    class RetrieveFeedTaskOnHold extends AsyncTask<Void, Void, String> {

        protected void onPreExecute() {
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Void... urls) {

            Utilities utilities = new Utilities(getContext());

            String address = Constants.API_RECEIVED_ORDER_STATUS;
            Map<String, String> params = new HashMap<>();
            params.put("userid", sharedPreference.getValue( getActivity(), Constants.PREF_IS_USER, Constants.PREF_KEY_USER_ID ));
//            params.put("userid", "21");
            params.put("status", "On Hold");

            return utilities.apiCalls(address,params);
        }

        protected void onPostExecute(String response) {
            try {
                JSONObject jsonObject1 = new JSONObject(response);
                JSONObject jsonObject2 = new JSONObject(jsonObject1.getString("Content"));
                imgRepNotFound.setVisibility(View.GONE);

                if (response.equals("NO_INTERNET")) {
                    Toast.makeText(getActivity().getBaseContext(), "Check internet connection", Toast.LENGTH_LONG).show();
                } else if (jsonObject2.getString("status").equalsIgnoreCase("error")) {
                    imgRepNotFound.setVisibility(View.VISIBLE);

                    final Animation animImgRecordNotFound = AnimationUtils.loadAnimation(getActivity(), R.anim.shake_interpolator);
                    imgRepNotFound.startAnimation(animImgRecordNotFound);

//                    btnReportLoad.setVisibility(View.GONE);
//                    Toast.makeText(getActivity().getBaseContext(), "Somthing went wrong", Toast.LENGTH_LONG).show();
                } else {


                    if (listDetails.size() > 0) {
                        listDetails.clear();
                    }


                    JSONArray jsonarray = new JSONArray(jsonObject2.getString("result"));
                    if (jsonarray.length() <= 0) {
//                        btnReportLoad.setVisibility(View.GONE);
                        Toast.makeText(getActivity().getBaseContext(), "No more record found.", Toast.LENGTH_SHORT).show();
                    }

                    String listOfMedicalUsers="";

                    for (int i = 0; i < jsonarray.length(); i++) {

                        JSONObject jsonObject = jsonarray.getJSONObject(i);

                        //                            2018-09-22 03:48:19
                        String s = jsonObject.getString("created_at");
//                        String s ="2018-09-22 03:48:19";
                        String[] s1 = s.split("\\s+");
                        String s2 = s1[0];

                        Date initDate = new SimpleDateFormat("yyyy-MM-dd").parse(s2);
                        SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
                        String parsedDate = formatter.format(initDate) + " "+ s1[1];


                        try {

                            if(( jsonObject.getString("customerconfirm").equalsIgnoreCase("1")
                                    && jsonObject.getString("medicalconfirm").equalsIgnoreCase("1")
                                    && jsonObject.getString("orderstatus").equalsIgnoreCase("On Hold"))) {

                                ServiceProviderDetailsModel serviceProviderDetails1 = new ServiceProviderDetailsModel();
                                serviceProviderDetails1.setLatitude(jsonObject.getString("latitude"));
                                serviceProviderDetails1.setLongitude(jsonObject.getString("longitude"));
                                serviceProviderDetails1.setOrderMainId(jsonObject.getString("orderid"));
                                serviceProviderDetails1.setMedicalCost(jsonObject.getString("cost"));
                                serviceProviderDetails1.setMedicalReply(jsonObject.getString("medicalreply"));
                                serviceProviderDetails1.setOrderid(jsonObject.getString("mainorderid"));
                                serviceProviderDetails1.setDescription(jsonObject.getString("description"));
                                serviceProviderDetails1.setImagepath(jsonObject.getString("imagepath"));

                                String string = jsonObject.getString("address");
                                String[] bits = string.split(",");
                                String lastWord = "";
                                if(bits.length>2)
                                    lastWord = bits[bits.length - 3] + ", " + bits[bits.length - 2] + ", " + bits[bits.length - 1];

                                serviceProviderDetails1.setAddress(lastWord);

//                                serviceProviderDetails1.setAddress(jsonObject.getString("address"));
                                serviceProviderDetails1.setNotificationTime(parsedDate);
                                serviceProviderDetails1.setStatus("Hold");
                                serviceProviderDetails1.setMobile(jsonObject.getString("mobile"));
                                serviceProviderDetails1.setOrderstatus(jsonObject.getString("orderstatus"));
                                serviceProviderDetails1.setCustomerId(jsonObject.getString("userid"));
                                serviceProviderDetails1.setMedicalId(sharedPreference.getValue(getActivity(), Constants.PREF_IS_USER, Constants.PREF_KEY_USER_ID));

                                listDetails.add(serviceProviderDetails1);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    if (listDetails.size() <= 0) {
                        imgRepNotFound.setVisibility(View.VISIBLE);

                        final Animation animImgRecordNotFound = AnimationUtils.loadAnimation(getActivity(), R.anim.shake_interpolator);
                        imgRepNotFound.startAnimation(animImgRecordNotFound);

//                        btnReportLoad.setVisibility(View.GONE);
                    }
                    if (listDetails.size() > 0) {
                        adapter = new ServiceProviderReportCardAdapter(getContext(), listDetails);
                        recyclerView.setAdapter(adapter);
                    }
                }
            }

            catch (Exception e1) {
                Toast.makeText( getActivity().getApplicationContext(), "Please try again later...", Toast.LENGTH_LONG).show();
            }
            finally {
                progressDialog.dismiss();
            }
        }
    }

    //................................Customer History.........................

    class RetrieveFeedTaskCompletedCustomer extends AsyncTask<Void, Void, String> {

        protected void onPreExecute() {
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Void... urls) {

            Utilities utilities = new Utilities(getContext());

            String address = Constants.API_SERVICE_PROVIDER_LIST_USING_ORDER_STATUS;
            Map<String, String> params = new HashMap<>();
            params.put("userid", sharedPreference.getValue( getActivity(), Constants.PREF_IS_USER, Constants.PREF_KEY_USER_ID ));
//            params.put("userid", "21");
            params.put("status", "Completed");

            return utilities.apiCalls(address,params);
        }

        protected void onPostExecute(String response) {
            try {
                JSONObject jsonObject1 = new JSONObject(response);
                JSONObject jsonObject2 = new JSONObject(jsonObject1.getString("Content"));
                imgRepNotFound.setVisibility(View.GONE);

                if (response.equals("NO_INTERNET")) {
                    Toast.makeText(getActivity().getBaseContext(), "Check internet connection", Toast.LENGTH_LONG).show();
                } else if (jsonObject2.getString("status").equalsIgnoreCase("error")) {
                    imgRepNotFound.setVisibility(View.VISIBLE);

                    final Animation animImgRecordNotFound = AnimationUtils.loadAnimation(getActivity(), R.anim.shake_interpolator);
                    imgRepNotFound.startAnimation(animImgRecordNotFound);
//                    btnReportLoad.setVisibility(View.GONE);
//                    Toast.makeText(getActivity().getBaseContext(), "Somthing went wrong", Toast.LENGTH_LONG).show();
                } else {


                    if (listDetails.size() > 0) {
                        listDetails.clear();
                    }


                    JSONArray jsonarray = new JSONArray(jsonObject2.getString("result"));
                    if (jsonarray.length() <= 0) {
//                        btnReportLoad.setVisibility(View.GONE);
                        Toast.makeText(getActivity().getBaseContext(), "No more record found.", Toast.LENGTH_SHORT).show();
                    }

                    String listOfMedicalUsers="";



                    for (int i = 0; i < jsonarray.length(); i++) {

                        JSONObject jsonObject = jsonarray.getJSONObject(i);

                        //                            2018-09-22 03:48:19
                        String s = jsonObject.getString("createddate");
//                        String s ="2018-09-22 03:48:19";
                        String[] s1 = s.split("\\s+");
                        String s2 = s1[0];

                        Date initDate = new SimpleDateFormat("yyyy-MM-dd").parse(s2);
                        SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
                        String parsedDate = formatter.format(initDate) + " "+ s1[1];


                        JSONObject jsonObjectMedicalids = null;
                        try {
                            JSONObject json = new JSONObject(jsonObject.getString("medicalids"));
                            Iterator<String> temp = json.keys();
                            while (temp.hasNext()) {
                                String key = temp.next();
                                Object value = json.get(key);

                                jsonObjectMedicalids= new JSONObject((String.valueOf(value)));
//                                {"id":"11","userid":"14","orderid":"ORD000009","latitude":"12.1516516541","longitude":"15.65656565","description":"test order","imagepath":"http:\/\/googel.com","address":"NAshik","mobile":"7412589630"

                                if(jsonObjectMedicalids.getString("medicalconfirm").equalsIgnoreCase("1")
                                        &&jsonObjectMedicalids.getString("customerconfirm").equalsIgnoreCase("1")) {

                                    ServiceProviderDetailsModel serviceProviderDetails1 = new ServiceProviderDetailsModel();
                                    serviceProviderDetails1.setCustomerId(jsonObject.getString("userid"));
                                    serviceProviderDetails1.setOrderid(jsonObject.getString("orderid"));
                                    serviceProviderDetails1.setOrderMainId(jsonObject.getString("id"));
                                    serviceProviderDetails1.setDescription(jsonObject.getString("description"));

                                    String string = jsonObject.getString("address");
                                    String[] bits = string.split(",");
                                    String lastWord = "";
                                    if(bits.length>2)
                                        lastWord = bits[bits.length - 3] + ", " + bits[bits.length - 2] + ", " + bits[bits.length - 1];

                                    serviceProviderDetails1.setAddress(lastWord);

//                                    serviceProviderDetails1.setAddress(jsonObject.getString("address"));
                                    serviceProviderDetails1.setNotificationTime(parsedDate);
                                    serviceProviderDetails1.setStatus("Completed");

                                    serviceProviderDetails1.setMedicalId(jsonObjectMedicalids.getString("medicalid"));
                                    serviceProviderDetails1.setMedicalReply(jsonObjectMedicalids.getString("medicalreply"));
                                    serviceProviderDetails1.setMedicalCost(jsonObjectMedicalids.getString("cost"));
                                    serviceProviderDetails1.setMedicalProfileUrl(jsonObjectMedicalids.getString("medicalurl"));
                                    serviceProviderDetails1.setImagepath(jsonObject.getString("imagepath"));


                                    listDetails.add(serviceProviderDetails1);
                                }


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }

                    if (listDetails.size() <= 0) {
                        imgRepNotFound.setVisibility(View.VISIBLE);

                        final Animation animImgRecordNotFound = AnimationUtils.loadAnimation(getActivity(), R.anim.shake_interpolator);
                        imgRepNotFound.startAnimation(animImgRecordNotFound);

//                        btnReportLoad.setVisibility(View.GONE);
                    }
                    if (listDetails.size() > 0) {
                        adapter = new ServiceProviderReportCardAdapter(getContext(), listDetails);
                        recyclerView.setAdapter(adapter);
                    }
                }
            }

            catch (Exception e1) {
                Toast.makeText( getActivity().getApplicationContext(), "Please try again later...", Toast.LENGTH_LONG).show();
            }
            finally {
                progressDialog.dismiss();
            }
        }
    }


    class RetrieveFeedTaskCanceledCustomer extends AsyncTask<Void, Void, String> {

        protected void onPreExecute() {
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Void... urls) {

            Utilities utilities = new Utilities(getContext());

            String address = Constants.API_SERVICE_PROVIDER_LIST_USING_ORDER_STATUS;
            Map<String, String> params = new HashMap<>();
            params.put("userid", sharedPreference.getValue( getActivity(), Constants.PREF_IS_USER, Constants.PREF_KEY_USER_ID ));
//            params.put("userid", "21");
            params.put("status", "Canceled");

            return utilities.apiCalls(address,params);
        }

        protected void onPostExecute(String response) {
            try {
                JSONObject jsonObject1 = new JSONObject(response);
                JSONObject jsonObject2 = new JSONObject(jsonObject1.getString("Content"));
                imgRepNotFound.setVisibility(View.GONE);

                if (response.equals("NO_INTERNET")) {
                    Toast.makeText(getActivity().getBaseContext(), "Check internet connection", Toast.LENGTH_LONG).show();
                } else if (jsonObject2.getString("status").equalsIgnoreCase("error")) {
                    imgRepNotFound.setVisibility(View.VISIBLE);

                    final Animation animImgRecordNotFound = AnimationUtils.loadAnimation(getActivity(), R.anim.shake_interpolator);
                    imgRepNotFound.startAnimation(animImgRecordNotFound);
//                    btnReportLoad.setVisibility(View.GONE);
//                    Toast.makeText(getActivity().getBaseContext(), "Somthing went wrong", Toast.LENGTH_LONG).show();
                } else {


                    if (listDetails.size() > 0) {
                        listDetails.clear();
                    }


                    JSONArray jsonarray = new JSONArray(jsonObject2.getString("result"));
                    if (jsonarray.length() <= 0) {
//                        btnReportLoad.setVisibility(View.GONE);
                        Toast.makeText(getActivity().getBaseContext(), "No more record found.", Toast.LENGTH_SHORT).show();
                    }

                    String listOfMedicalUsers="";

                    /*for (int i = 0; i < jsonarray.length(); i++) {

                        JSONObject jsonObject = jsonarray.getJSONObject(i);

                        try {

                            if(( jsonObject.getString("customerconfirm").equalsIgnoreCase("1")
                                    && jsonObject.getString("medicalconfirm").equalsIgnoreCase("1")
                                    && jsonObject.getString("ordstatus").equalsIgnoreCase("Pending"))) {

                                ServiceProviderDetailsModel serviceProviderDetails1 = new ServiceProviderDetailsModel();
                                serviceProviderDetails1.setLatitude(jsonObject.getString("latitude"));
                                serviceProviderDetails1.setLongitude(jsonObject.getString("longitude"));
                                serviceProviderDetails1.setOrderMainId(jsonObject.getString("orderid"));
                                serviceProviderDetails1.setMedicalCost(jsonObject.getString("cost"));
                                serviceProviderDetails1.setMedicalReply(jsonObject.getString("medicalreply"));
                                serviceProviderDetails1.setOrderid(jsonObject.getString("mainorderid"));
                                serviceProviderDetails1.setDescription(jsonObject.getString("description"));
                                serviceProviderDetails1.setImagepath(jsonObject.getString("imagepath"));
                                serviceProviderDetails1.setAddress(jsonObject.getString("address"));
                                serviceProviderDetails1.setMobile(jsonObject.getString("mobile"));
                                serviceProviderDetails1.setOrderstatus(jsonObject.getString("ordstatus"));
                                serviceProviderDetails1.setCustomerId(jsonObject.getString("userid"));
                                serviceProviderDetails1.setMedicalId(sharedPreference.getValue(getActivity(), Constants.PREF_IS_USER, Constants.PREF_KEY_USER_ID));

                                listDetails.add(serviceProviderDetails1);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }*/

                    for (int i = 0; i < jsonarray.length(); i++) {

                        JSONObject jsonObject = jsonarray.getJSONObject(i);

                        //                            2018-09-22 03:48:19
                        String s = jsonObject.getString("createddate");
//                        String s ="2018-09-22 03:48:19";
                        String[] s1 = s.split("\\s+");
                        String s2 = s1[0];

                        Date initDate = new SimpleDateFormat("yyyy-MM-dd").parse(s2);
                        SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
                        String parsedDate = formatter.format(initDate) + " "+ s1[1];


                        JSONObject jsonObjectMedicalids = null;
                        try {
                            JSONObject json = new JSONObject(jsonObject.getString("medicalids"));
                            Iterator<String> temp = json.keys();
                            while (temp.hasNext()) {
                                String key = temp.next();
                                Object value = json.get(key);

                                jsonObjectMedicalids= new JSONObject((String.valueOf(value)));
//                                {"id":"11","userid":"14","orderid":"ORD000009","latitude":"12.1516516541","longitude":"15.65656565","description":"test order","imagepath":"http:\/\/googel.com","address":"NAshik","mobile":"7412589630"

                                if(jsonObjectMedicalids.getString("medicalconfirm").equalsIgnoreCase("1")
                                        &&jsonObjectMedicalids.getString("customerconfirm").equalsIgnoreCase("1")) {

                                    ServiceProviderDetailsModel serviceProviderDetails1 = new ServiceProviderDetailsModel();
                                    serviceProviderDetails1.setCustomerId(jsonObject.getString("userid"));
                                    serviceProviderDetails1.setOrderid(jsonObject.getString("orderid"));
                                    serviceProviderDetails1.setOrderMainId(jsonObject.getString("id"));
                                    serviceProviderDetails1.setDescription(jsonObject.getString("description"));

                                    String string = jsonObject.getString("address");
                                    String[] bits = string.split(",");
                                    String lastWord = "";
                                    if(bits.length>2)
                                        lastWord = bits[bits.length - 3] + ", " + bits[bits.length - 2] + ", " + bits[bits.length - 1];

                                    serviceProviderDetails1.setAddress(lastWord);

//                                    serviceProviderDetails1.setAddress(jsonObject.getString("address"));
                                    serviceProviderDetails1.setNotificationTime(parsedDate);
                                    serviceProviderDetails1.setStatus("Canceled");

                                    serviceProviderDetails1.setMedicalId(jsonObjectMedicalids.getString("medicalid"));
                                    serviceProviderDetails1.setMedicalReply(jsonObjectMedicalids.getString("medicalreply"));
                                    serviceProviderDetails1.setMedicalCost(jsonObjectMedicalids.getString("cost"));
                                    serviceProviderDetails1.setMedicalProfileUrl(jsonObjectMedicalids.getString("medicalurl"));
                                    serviceProviderDetails1.setImagepath(jsonObject.getString("imagepath"));


                                    listDetails.add(serviceProviderDetails1);
                                }


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }

                    if (listDetails.size() <= 0) {
                        imgRepNotFound.setVisibility(View.VISIBLE);

                        final Animation animImgRecordNotFound = AnimationUtils.loadAnimation(getActivity(), R.anim.shake_interpolator);
                        imgRepNotFound.startAnimation(animImgRecordNotFound);

//                        btnReportLoad.setVisibility(View.GONE);
                    }
                    if (listDetails.size() > 0) {
                        adapter = new ServiceProviderReportCardAdapter(getContext(), listDetails);
                        recyclerView.setAdapter(adapter);
                    }
                }
            }

            catch (Exception e1) {
                Toast.makeText( getActivity().getApplicationContext(), "Please try again later...", Toast.LENGTH_LONG).show();
            }
            finally {
                progressDialog.dismiss();
            }
        }
    }



    class RetrieveFeedTaskOnHoldCustomer extends AsyncTask<Void, Void, String> {

        protected void onPreExecute() {
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Void... urls) {

            Utilities utilities = new Utilities(getContext());

            String address = Constants.API_SERVICE_PROVIDER_LIST_USING_ORDER_STATUS;
            Map<String, String> params = new HashMap<>();
            params.put("userid", sharedPreference.getValue( getActivity(), Constants.PREF_IS_USER, Constants.PREF_KEY_USER_ID ));
//            params.put("userid", "21");
            params.put("status", "On Hold");

            return utilities.apiCalls(address,params);
        }

        protected void onPostExecute(String response) {
            try {
                JSONObject jsonObject1 = new JSONObject(response);
                JSONObject jsonObject2 = new JSONObject(jsonObject1.getString("Content"));
                imgRepNotFound.setVisibility(View.GONE);

                if (response.equals("NO_INTERNET")) {
                    Toast.makeText(getActivity().getBaseContext(), "Check internet connection", Toast.LENGTH_LONG).show();
                } else if (jsonObject2.getString("status").equalsIgnoreCase("error")) {
                    imgRepNotFound.setVisibility(View.VISIBLE);

                    final Animation animImgRecordNotFound = AnimationUtils.loadAnimation(getActivity(), R.anim.shake_interpolator);
                    imgRepNotFound.startAnimation(animImgRecordNotFound);
//                    btnReportLoad.setVisibility(View.GONE);
//                    Toast.makeText(getActivity().getBaseContext(), "Somthing went wrong", Toast.LENGTH_LONG).show();
                } else {


                    if (listDetails.size() > 0) {
                        listDetails.clear();
                    }


                    JSONArray jsonarray = new JSONArray(jsonObject2.getString("result"));
                    if (jsonarray.length() <= 0) {
//                        btnReportLoad.setVisibility(View.GONE);
                        Toast.makeText(getActivity().getBaseContext(), "No more record found.", Toast.LENGTH_SHORT).show();
                    }

                    String listOfMedicalUsers="";

                    /*for (int i = 0; i < jsonarray.length(); i++) {

                        JSONObject jsonObject = jsonarray.getJSONObject(i);

                        try {

                            if(( jsonObject.getString("customerconfirm").equalsIgnoreCase("1")
                                    && jsonObject.getString("medicalconfirm").equalsIgnoreCase("1")
                                    && jsonObject.getString("ordstatus").equalsIgnoreCase("Pending"))) {

                                ServiceProviderDetailsModel serviceProviderDetails1 = new ServiceProviderDetailsModel();
                                serviceProviderDetails1.setLatitude(jsonObject.getString("latitude"));
                                serviceProviderDetails1.setLongitude(jsonObject.getString("longitude"));
                                serviceProviderDetails1.setOrderMainId(jsonObject.getString("orderid"));
                                serviceProviderDetails1.setMedicalCost(jsonObject.getString("cost"));
                                serviceProviderDetails1.setMedicalReply(jsonObject.getString("medicalreply"));
                                serviceProviderDetails1.setOrderid(jsonObject.getString("mainorderid"));
                                serviceProviderDetails1.setDescription(jsonObject.getString("description"));
                                serviceProviderDetails1.setImagepath(jsonObject.getString("imagepath"));
                                serviceProviderDetails1.setAddress(jsonObject.getString("address"));
                                serviceProviderDetails1.setMobile(jsonObject.getString("mobile"));
                                serviceProviderDetails1.setOrderstatus(jsonObject.getString("ordstatus"));
                                serviceProviderDetails1.setCustomerId(jsonObject.getString("userid"));
                                serviceProviderDetails1.setMedicalId(sharedPreference.getValue(getActivity(), Constants.PREF_IS_USER, Constants.PREF_KEY_USER_ID));

                                listDetails.add(serviceProviderDetails1);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }*/

                    for (int i = 0; i < jsonarray.length(); i++) {

                        JSONObject jsonObject = jsonarray.getJSONObject(i);

                        //                            2018-09-22 03:48:19
                        String s = jsonObject.getString("createddate");
//                        String s ="2018-09-22 03:48:19";
                        String[] s1 = s.split("\\s+");
                        String s2 = s1[0];

                        Date initDate = new SimpleDateFormat("yyyy-MM-dd").parse(s2);
                        SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
                        String parsedDate = formatter.format(initDate) + " "+ s1[1];


                        JSONObject jsonObjectMedicalids = null;
                        try {
                            JSONObject json = new JSONObject(jsonObject.getString("medicalids"));
                            Iterator<String> temp = json.keys();
                            while (temp.hasNext()) {
                                String key = temp.next();
                                Object value = json.get(key);

                                jsonObjectMedicalids= new JSONObject((String.valueOf(value)));
//                                {"id":"11","userid":"14","orderid":"ORD000009","latitude":"12.1516516541","longitude":"15.65656565","description":"test order","imagepath":"http:\/\/googel.com","address":"NAshik","mobile":"7412589630"

                                if(jsonObjectMedicalids.getString("medicalconfirm").equalsIgnoreCase("1")
                                        &&jsonObjectMedicalids.getString("customerconfirm").equalsIgnoreCase("1")) {

                                    ServiceProviderDetailsModel serviceProviderDetails1 = new ServiceProviderDetailsModel();
                                    serviceProviderDetails1.setCustomerId(jsonObject.getString("userid"));
                                    serviceProviderDetails1.setOrderid(jsonObject.getString("orderid"));
                                    serviceProviderDetails1.setOrderMainId(jsonObject.getString("id"));
                                    serviceProviderDetails1.setDescription(jsonObject.getString("description"));

                                    String string = jsonObject.getString("address");
                                    String[] bits = string.split(",");
                                    String lastWord = "";
                                    if(bits.length>2)
                                        lastWord = bits[bits.length - 3] + ", " + bits[bits.length - 2] + ", " + bits[bits.length - 1];

                                    serviceProviderDetails1.setAddress(lastWord);

//                                    serviceProviderDetails1.setAddress(jsonObject.getString("address"));
                                    serviceProviderDetails1.setNotificationTime(parsedDate);
                                    serviceProviderDetails1.setStatus("Hold");

                                    serviceProviderDetails1.setMedicalId(jsonObjectMedicalids.getString("medicalid"));
                                    serviceProviderDetails1.setMedicalReply(jsonObjectMedicalids.getString("medicalreply"));
                                    serviceProviderDetails1.setMedicalCost(jsonObjectMedicalids.getString("cost"));
                                    serviceProviderDetails1.setMedicalProfileUrl(jsonObjectMedicalids.getString("medicalurl"));
                                    serviceProviderDetails1.setImagepath(jsonObject.getString("imagepath"));


                                    listDetails.add(serviceProviderDetails1);
                                }


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }

                    if (listDetails.size() <= 0) {
                        imgRepNotFound.setVisibility(View.VISIBLE);

                        final Animation animImgRecordNotFound = AnimationUtils.loadAnimation(getActivity(), R.anim.shake_interpolator);
                        imgRepNotFound.startAnimation(animImgRecordNotFound);

//                        btnReportLoad.setVisibility(View.GONE);
                    }
                    if (listDetails.size() > 0) {
                        adapter = new ServiceProviderReportCardAdapter(getContext(), listDetails);
                        recyclerView.setAdapter(adapter);
                    }
                }
            }

            catch (Exception e1) {
                Toast.makeText( getActivity().getApplicationContext(), "Please try again later...", Toast.LENGTH_LONG).show();
            }
            finally {
                progressDialog.dismiss();
            }
        }
    }



}
