package com.medicalstorefinder.medicalstoreslocatorss.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.medicalstorefinder.medicalstoreslocatorss.Constants.Constants;
import com.medicalstorefinder.medicalstoreslocatorss.Constants.SharedPreference;
import com.medicalstorefinder.medicalstoreslocatorss.Constants.Utilities;
import com.medicalstorefinder.medicalstoreslocatorss.Fragments.CustomerRatingsFragment;
import com.medicalstorefinder.medicalstoreslocatorss.Fragments.MedicalResponseOfCostListFragment;
import com.medicalstorefinder.medicalstoreslocatorss.GlideImageLoader;
import com.medicalstorefinder.medicalstoreslocatorss.Models.ServiceProviderDetailsModel;
import com.medicalstorefinder.medicalstoreslocatorss.R;
import com.medicalstorefinder.medicalstoreslocatorss.SingleTouchImageViewFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.medicalstorefinder.medicalstoreslocatorss.Constants.Constants.NO_AVATAR_IMAGE_PATH;

public class FirebaseMainActivity extends AppCompatActivity {
    ProgressDialog progressDialog;
    private TextView tvNotificationDetails;
    List<ServiceProviderDetailsModel> listDetails = new ArrayList<ServiceProviderDetailsModel>();
    private RecyclerView.Adapter adapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    String str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_main);
        initControls();
        setNotificationData(getIntent().getExtras());

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        recyclerView = (RecyclerView) findViewById(R.id.rep_recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

    }
    private void initControls() {
        tvNotificationDetails = (TextView) findViewById(R.id.tvNotificationDetails);
        recyclerView = (RecyclerView) findViewById(R.id.rep_recyclerView);
    }
    private void setNotificationData(Bundle extras) {
        if (extras == null)
            return;
        StringBuilder text = new StringBuilder("");
//        text.append("Message Details:");
        text.append("\n");
        text.append("\n");
        if (extras.containsKey("keysMessageTitle")) {
//            text.append(extras.get("keysMessageTitle"));
            text.append(extras.get("keysMessageBody"));
        }
        text.append("\n");
        if (extras.containsKey("message")) {
            text.append("Message: ");
            text.append(extras.get("message"));
        }

        str = String.valueOf(text);
        String[] str1 = str.split("\\(");
        String[] str2 = str1[1].split("\\)");

        new Notifications().execute(str2);

    }






    public class ServiceProviderReportCardAdapter extends RecyclerView.Adapter<ServiceProviderReportCardAdapter.ViewHolder> {

        ServiceProviderReportCardAdapter serviceProviderReportCardAdapter = this;
        private Context context;

        List<ServiceProviderDetailsModel> listServiceProviderDetails;

        public ServiceProviderReportCardAdapter(Context context, List<ServiceProviderDetailsModel> listServiceProviderDetails) {
//            super();
            this.context = context;
            this.listServiceProviderDetails = listServiceProviderDetails;
        }

        @Override
        public ServiceProviderReportCardAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.received_order_card_item, parent, false);
            ServiceProviderReportCardAdapter.ViewHolder viewHolder = new ServiceProviderReportCardAdapter.ViewHolder(v);

            final Animation anim_record_item = AnimationUtils.loadAnimation(parent.getContext(), R.anim.swipe_down);
            viewHolder.itemView.startAnimation(anim_record_item);

//            linearLayoutTxCardItem = (LinearLayout) v.findViewById(R.id.linear_layout_tx_card_item);
            return viewHolder;
        }


        @Override
        public void onBindViewHolder(ServiceProviderReportCardAdapter.ViewHolder holder, int position) {
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


                }else if(!tr.getImagepath().equalsIgnoreCase("")&& tr.getImagepath()!=null) {
//                    Glide.with(context).load(NO_AVATAR_IMAGE_PATH+tr.getImagepath()).into(holder.imageViews);
//                    holder.imageViews.setImageResource(android.R.color.transparent);

                    RequestOptions options = new RequestOptions()
                            .centerCrop()
                            .placeholder(R.drawable.profile_pic)
//                            .error(R.drawable.ic_pic_error)
                            .priority(Priority.HIGH);

                    new GlideImageLoader(holder.imageViews,
                            holder.getProgressBar()).load(NO_AVATAR_IMAGE_PATH+tr.getImagepath(),options);

                }else{
                    Glide.with(context).load(R.drawable.profile_pic).into(holder.imageViews);
                }
//                    Glide.with(context).load(NO_AVATAR_IMAGE_PATH+tr.getMedicalProfileUrl()).into(holder.imageViews);


                tr.setStatus("Pending");
                switch (tr.getStatus()) {
                    case "Pending":
                        holder.vtxtStatus.setText("PENDING");
                        holder.vtxtStatus.setTextColor(getApplicationContext().getResources().getColor(R.color.tx_FAILURE));
//                        linearLayoutTxCardItem.setBackgroundColor(getActivity().getApplicationContext().getResources().getColor(R.color.bg_FAILURE));
                        holder.cardViewTxCardItem.setCardBackgroundColor(Color.parseColor("#ffffff"));
                        break;
                    case "Success":
                        holder.vtxtStatus.setText("SUCCESS");
                        holder.vtxtStatus.setTextColor(getApplicationContext().getResources().getColor(R.color.tx_SUCCESS));
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


                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final ServiceProviderDetailsModel tr = listServiceProviderDetails.get(getAdapterPosition());

                        String lStatus = "Pending";

                        final android.support.v7.app.AlertDialog.Builder alertDialogBuilder1 = new android.support.v7.app.AlertDialog.Builder(FirebaseMainActivity.this,R.style.AppCompatAlertDialogStyle );

                        android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(FirebaseMainActivity.this,R.style.AppCompatAlertDialogStyle );
                        alertDialogBuilder.setTitle(Html.fromHtml(getString(R.string.transactions)));
                        alertDialogBuilder.setMessage(
                                Html.fromHtml(getString(R.string.orderid)) + tr.getOrderid() +
                                        "\n"+Html.fromHtml(getString(R.string.description)) + tr.getDescription()+
                                        "\n"+Html.fromHtml(getString(R.string.medicalcost)) + tr.getMedicalCost() +
                                        "\n"+Html.fromHtml(getString(R.string.medicaldescription)) + tr.getMedicalReply());

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

    class Notifications extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
//            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... urls) {
            Utilities utilities = new Utilities(getBaseContext());

            String s1;

            s1=urls[0];
            String address;
            SharedPreference sharedPreference = new SharedPreference();
            if (sharedPreference.getValue( getBaseContext(), Constants.PREF_USER_ROLE, Constants.PREF_USER_ROLE ).equalsIgnoreCase("customer")) {
                address = Constants.API_SINGLE_NOTIFICATION;
            }else{
                address = Constants.API_SINGLE_NOTIFICATION_MEDICAL;
            }

            Map<String, String> params = new HashMap<>();
            params.put("userid", sharedPreference.getValue( getBaseContext(), Constants.PREF_IS_USER, Constants.PREF_KEY_USER_ID ));
            params.put("orderid",  s1);



            return utilities.apiCalls(address,params);
        }

        protected void onPostExecute(String response) {
            try {

                JSONObject jsonObject1 = new JSONObject(response);
                JSONObject jsonObject2 = new JSONObject(jsonObject1.getString("Content"));

                if(response.equals("NO_INTERNET")) {
                    Toast.makeText(getBaseContext(), "Check internet connection", Toast.LENGTH_LONG).show();
                }
                else if(jsonObject2.getString("status").equalsIgnoreCase("error")) {
                    Toast.makeText(getBaseContext(), jsonObject2.getString("message"), Toast.LENGTH_LONG).show();
                }
                else
                {
                    if(jsonObject2.getString("status").equalsIgnoreCase("error")) {
                        Toast.makeText(getBaseContext(), jsonObject2.getString("message"), Toast.LENGTH_LONG).show();
                    }
                    else if(jsonObject2.getString("status").equalsIgnoreCase("success")) {
//                        Toast.makeText(getBaseContext(), jsonObject2.getString("status"), Toast.LENGTH_LONG).show();
//remaining





                        JSONObject jsonObject = new JSONObject(jsonObject2.getString("result"));

                        //                            2018-09-22 03:48:19 remains
                        String s = jsonObject.getString("created_at");
//                        String s ="2018-09-22 03:48:19";
                        String[] s1 = s.split("\\s+");
                        String s2 = s1[0];

                        Date initDate = new SimpleDateFormat("yyyy-MM-dd").parse(s2);
                        SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
                        String parsedDate = formatter.format(initDate) + " "+ s1[1];

                            if (listDetails.size() > 0) {
                                listDetails.clear();
                            }

                            ServiceProviderDetailsModel serviceProviderDetails1 = new ServiceProviderDetailsModel();

                            serviceProviderDetails1.setMedicalReply(jsonObject.getString("medicalreply").equalsIgnoreCase("null")?"":jsonObject.getString("medicalreply"));
                            serviceProviderDetails1.setOrderid(jsonObject.getString("mainorderid"));
                            serviceProviderDetails1.setDescription(jsonObject.getString("description").equalsIgnoreCase("null")?"":jsonObject.getString("description"));
                            serviceProviderDetails1.setCustomerId(jsonObject.getString("userid"));
                            serviceProviderDetails1.setMedicalCost(jsonObject.getString("cost").equalsIgnoreCase("null")?"":jsonObject.getString("cost"));
                            serviceProviderDetails1.setImagepath(jsonObject.getString("imagepath"));

                            String string = jsonObject.getString("address");
                            String[] bits = string.split(",");
                            String lastWord = "";
                            if(bits.length>2)
                                lastWord = bits[bits.length - 3] + ", " + bits[bits.length - 2] + ", " + bits[bits.length - 1];

                            serviceProviderDetails1.setAddress(lastWord);
                            serviceProviderDetails1.setNotificationTime(parsedDate);

                            listDetails.add(serviceProviderDetails1);

                            adapter = new ServiceProviderReportCardAdapter(getBaseContext(), listDetails);
                            recyclerView.setAdapter(adapter);

                        tvNotificationDetails.setText(str);
                    }
                }

            } catch (Exception e) {
                Toast.makeText( getBaseContext(), "Please try again later...", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
            finally {
//                progressDialog.dismiss();
            }
        }
    }











}