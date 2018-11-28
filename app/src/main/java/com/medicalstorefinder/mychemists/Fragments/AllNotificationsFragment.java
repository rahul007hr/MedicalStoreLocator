package com.medicalstorefinder.mychemists.Fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.medicalstorefinder.mychemists.Activity.CustomerActivity;
import com.medicalstorefinder.mychemists.Constants.Constants;
import com.medicalstorefinder.mychemists.Constants.SharedPreference;
import com.medicalstorefinder.mychemists.Constants.Utilities;
import com.medicalstorefinder.mychemists.Constants.Utility;
import com.medicalstorefinder.mychemists.GlideImageLoader;
import com.medicalstorefinder.mychemists.Models.ServiceProviderDetailsModel;
import com.medicalstorefinder.mychemists.R;
import com.medicalstorefinder.mychemists.SingleTouchImageViewFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.medicalstorefinder.mychemists.Constants.Constants.NO_AVATAR_IMAGE_PATH;


public class AllNotificationsFragment extends Fragment {

    ServiceProviderDetailsModel serviceProviderDetails = new ServiceProviderDetailsModel();
    ProgressDialog progressDialog;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    final String[] desc = new String[1];
    final String[] cost = new String[1];
    String orderId, customerId, medicalId;
    private ImageView imgRepNotFound;
    List<ServiceProviderDetailsModel> listDetails = new ArrayList<ServiceProviderDetailsModel>();
    String str;
    String strNotification;
    SharedPreference sharedPreference = new SharedPreference();


    public AllNotificationsFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_all_notifications, container, false);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        recyclerView = (RecyclerView) v.findViewById(R.id.rep_recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        imgRepNotFound = (ImageView) v.findViewById(R.id.img_rep_not_found);
        imgRepNotFound.setVisibility(View.GONE);
        if (sharedPreference.getValue(getActivity(), Constants.PREF_USER_ROLE, Constants.PREF_USER_ROLE).equalsIgnoreCase("customer")) {
            CustomerActivity.navigation.getMenu().findItem(R.id.chooseOrderType).setChecked(false);
            CustomerActivity.navigation.getMenu().findItem(R.id.postOrder).setEnabled(false);
            CustomerActivity.navigation.getMenu().findItem(R.id.NearbyServiceProviderList).setEnabled(false);

        }
        new RetrieveFeedTask1().execute();
        return v;

    }

    class RetrieveFeedTask1 extends AsyncTask<Void, Void, String> {

        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(Void... urls) {
            Utilities utilities = new Utilities(getContext());
            String address = Constants.API_ALL_NOTIFICATIONS;
            Map<String, String> params = new HashMap<>();
            params.put("userid", sharedPreference.getValue(getActivity(), Constants.PREF_IS_USER, Constants.PREF_KEY_USER_ID));
            return utilities.apiCalls(address, params);
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
                } else {
                    if (listDetails.size() > 0) {
                        listDetails.clear();
                    }
                    String userId = sharedPreference.getValue(getContext(), Constants.PREF_IS_USER, Constants.PREF_KEY_USER_ID);
                    JSONArray jsonarray = new JSONArray(jsonObject2.getString("result"));
                    if (jsonarray.length() <= 0) {
                        Toast.makeText(getActivity().getBaseContext(), "No more record found.", Toast.LENGTH_SHORT).show();
                    }
                    String listOfMedicalUsers = "";
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject jsonObject = jsonarray.getJSONObject(i);
                        try {
                            String s = jsonObject.getString("notifytime");
                            String[] s1 = s.split("\\s+");
                            String s2 = s1[0];
                            Date initDate = new SimpleDateFormat("yyyy-MM-dd").parse(s2);
                            SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
                            Date initTime = new SimpleDateFormat("hh:mm:ss").parse(s1[1]);
                            SimpleDateFormat timeFormatter = new SimpleDateFormat("hh:mm a");
                            String parsedDate = formatter.format(initDate) + " " + timeFormatter.format(initTime);
                            parsedDate = parsedDate.toUpperCase();
                            ServiceProviderDetailsModel serviceProviderDetails1 = new ServiceProviderDetailsModel();
                            serviceProviderDetails1.setNotification(jsonObject.getString("notification"));
                            serviceProviderDetails1.setNotificationTime(parsedDate);
                            serviceProviderDetails1.setImagepath(jsonObject.getString("imagepath"));
                            serviceProviderDetails1.setDescription(jsonObject.getString("description"));
                            listDetails.add(serviceProviderDetails1);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                    if (listDetails.size() <= 0) {
                        imgRepNotFound.setVisibility(View.VISIBLE);
                        final Animation animImgRecordNotFound = AnimationUtils.loadAnimation(getActivity(), R.anim.shake_interpolator);
                        imgRepNotFound.startAnimation(animImgRecordNotFound);

                    }
                    adapter = new ServiceProviderReportCardAdapter(getContext(), listDetails);
                    recyclerView.setAdapter(adapter);
                }
            } catch (Exception e1) {
                Toast.makeText(getActivity().getApplicationContext(), "Please try again later...", Toast.LENGTH_LONG).show();
            } finally {
            }
        }
    }


    public class ServiceProviderReportCardAdapter extends RecyclerView.Adapter<ServiceProviderReportCardAdapter.ViewHolder> {

        ServiceProviderReportCardAdapter serviceProviderReportCardAdapter = this;
        private Context context;
        private RetrieveFeedTask1 context1;

        List<ServiceProviderDetailsModel> listServiceProviderDetails;

        public ServiceProviderReportCardAdapter(Context context, List<ServiceProviderDetailsModel> listServiceProviderDetails) {
            this.context = context;
            this.listServiceProviderDetails = listServiceProviderDetails;
        }

        @Override
        public ServiceProviderReportCardAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.all_notifications_card_item, parent, false);
            ServiceProviderReportCardAdapter.ViewHolder viewHolder = new ServiceProviderReportCardAdapter.ViewHolder(v);
            final Animation anim_record_item = AnimationUtils.loadAnimation(parent.getContext(), R.anim.swipe_down);
            viewHolder.itemView.startAnimation(anim_record_item);
            return viewHolder;
        }


        @Override
        public void onBindViewHolder(ServiceProviderReportCardAdapter.ViewHolder holder, int position) {
            try {
                ServiceProviderDetailsModel tr = listServiceProviderDetails.get(position);
                holder.vtxtMessages.setText("Message : " + tr.getNotification());
                holder.vtxtDescription.setText("Description : " + tr.getDescription());
                holder.vtxtTime.setText("Date : " + tr.getNotificationTime());
                if (!tr.getImagepath().equalsIgnoreCase("") && tr.getImagepath() != null && !tr.getImagepath().equalsIgnoreCase("no_avatar.jpg")) {
                    RequestOptions options = new RequestOptions()
                            .centerCrop()
                            .placeholder(R.drawable.profile_pic)
                            .priority(Priority.HIGH);
                    new GlideImageLoader(holder.imageViews,
                            holder.getProgressBar()).load(tr.getImagepath(), options);

                } else if (!tr.getImagepath().equalsIgnoreCase("") && tr.getImagepath() != null) {
                    RequestOptions options = new RequestOptions()
                            .centerCrop()
                            .placeholder(R.drawable.profile_pic)
                            .priority(Priority.HIGH);
                    new GlideImageLoader(holder.imageViews,
                            holder.getProgressBar()).load(NO_AVATAR_IMAGE_PATH + tr.getImagepath(), options);

                } else {
                    RequestOptions options = new RequestOptions()
                            .centerCrop()
                            .placeholder(R.drawable.profile_pic)
                            .priority(Priority.HIGH);
                    new GlideImageLoader(holder.imageViews,
                            holder.spinner).load(NO_AVATAR_IMAGE_PATH + "no_avatar.jpg", options);
                }

            } catch (Exception e1) {
            }
        }

        @Override
        public int getItemCount() {
            return listServiceProviderDetails.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            public TextView vtxtMessages;
            public TextView vtxtTime;
            public TextView vtxtDescription;
            public CardView cardViewTxCardItem;
            public ImageView imageViews;
            public ProgressBar spinner;

            public ViewHolder(View itemView) {
                super(itemView);
                final View view = itemView;
                vtxtMessages = (TextView) itemView.findViewById(R.id.message);
                vtxtTime = (TextView) itemView.findViewById(R.id.time);
                vtxtDescription = (TextView) itemView.findViewById(R.id.description);
                imageViews = (ImageView) itemView.findViewById(R.id.image_View);
                spinner = (ProgressBar) itemView.findViewById(R.id.progressBar1);
                setProgressBar(spinner);
                cardViewTxCardItem = (CardView) itemView.findViewById(R.id.cardview_tx_card_item);
                imageViews.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final ServiceProviderDetailsModel tr = listServiceProviderDetails.get(getAdapterPosition());
                        if (tr.getImagepath() != null && !tr.getImagepath().equalsIgnoreCase("") && !tr.getImagepath().equalsIgnoreCase
                                ("null") && !tr.getImagepath().equalsIgnoreCase("no_avatar.jpg")) {
                            SingleTouchImageViewFragment ldf1 = new SingleTouchImageViewFragment();
                            Bundle args1 = new Bundle();
                            args1.putString("position1", String.valueOf(tr.getImagepath()));
                            ldf1.setArguments(args1);
                            getFragmentManager().beginTransaction().replace(R.id.containerView, ldf1, "C").addToBackStack(null).commit();
                        } else {
                            Toast.makeText(getContext(), "Image Not Available", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final ServiceProviderDetailsModel tr = listServiceProviderDetails.get(getAdapterPosition());
                        str = String.valueOf(tr.getNotification());
                        String[] str1 = str.split("\\(");
                        String[] str2 = str1[1].split("\\)");
                        strNotification = tr.getNotification();
                        new Notifications().execute(str2);

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
        }

        @Override
        protected String doInBackground(String... urls) {
            Utilities utilities = new Utilities(getContext());
            String s1;
            s1 = urls[0];
            String address;
            SharedPreference sharedPreference = new SharedPreference();
            if (sharedPreference.getValue(getContext(), Constants.PREF_USER_ROLE, Constants.PREF_USER_ROLE).equalsIgnoreCase("customer")) {
                address = Constants.API_SINGLE_NOTIFICATION;
            } else {
                address = Constants.API_SINGLE_NOTIFICATION_MEDICAL;
            }
            Map<String, String> params = new HashMap<>();
            params.put("userid", sharedPreference.getValue(getContext(), Constants.PREF_IS_USER, Constants.PREF_KEY_USER_ID));
            params.put("orderid", s1);
            return utilities.apiCalls(address, params);
        }

        protected void onPostExecute(String response) {
            try {
                String userId = sharedPreference.getValue(getContext(), Constants.PREF_IS_USER, Constants.PREF_KEY_USER_ID);
                JSONObject jsonObject1 = new JSONObject(response);
                JSONObject jsonObject2 = new JSONObject(jsonObject1.getString("Content"));
                if (response.equals("NO_INTERNET")) {
                    Toast.makeText(getContext(), "Check internet connection", Toast.LENGTH_LONG).show();
                } else if (jsonObject2.getString("status").equalsIgnoreCase("error")) {
                    Toast.makeText(getContext(), jsonObject2.getString("message"), Toast.LENGTH_LONG).show();
                } else {
                    if (jsonObject2.getString("status").equalsIgnoreCase("error")) {
                        Toast.makeText(getContext(), jsonObject2.getString("message"), Toast.LENGTH_LONG).show();
                    } else if (jsonObject2.getString("status").equalsIgnoreCase("success")) {
                        JSONObject jsonObject = new JSONObject(jsonObject2.getString("result"));
                        final ServiceProviderDetailsModel serviceProviderDetails1 = new ServiceProviderDetailsModel();
                        serviceProviderDetails1.setMedicalReply(jsonObject.getString("medicalreply"));
                        serviceProviderDetails1.setOrderid(jsonObject.getString("mainorderid"));
                        serviceProviderDetails1.setDescription(jsonObject.getString("description"));
                        serviceProviderDetails1.setCustomerId(jsonObject.getString("userid"));
                        String medical = "";
                        if (sharedPreference.getValue(getActivity(), Constants.PREF_USER_ROLE, Constants.PREF_USER_ROLE).equalsIgnoreCase("customer"))
                            medical = jsonObject.getString("medicalid") != null ? jsonObject.getString("medicalid") : "";
                        serviceProviderDetails1.setMedicalId(medical);
                        serviceProviderDetails1.setMedicalCost(jsonObject.getString("cost"));
                        serviceProviderDetails1.setImagepath(jsonObject.getString("imagepath"));
                        orderId = jsonObject.getString("orderid");
                        customerId = jsonObject.getString("userid");
                        medicalId = medical;
                        String string = jsonObject.getString("address");
                        String[] bits = string.split(",");
                        String lastWord = "";
                        if (bits.length > 2)
                            lastWord = bits[bits.length - 3] + ", " + bits[bits.length - 2] + ", " + bits[bits.length - 1];
                        serviceProviderDetails1.setAddress(lastWord);
                        String description = (serviceProviderDetails1.getDescription() != null &&
                                !serviceProviderDetails1.getDescription().equalsIgnoreCase("null") && !serviceProviderDetails1.getDescription().equalsIgnoreCase("")) ? serviceProviderDetails1.getDescription() : "-";
                        String medicalCost = (serviceProviderDetails1.getMedicalCost() != null &&
                                !serviceProviderDetails1.getMedicalCost().equalsIgnoreCase("null") && !serviceProviderDetails1.getMedicalCost().equalsIgnoreCase("")) ? serviceProviderDetails1.getMedicalCost() : "-";
                        String medicalDescription = (serviceProviderDetails1.getMedicalReply() != null &&
                                !serviceProviderDetails1.getMedicalReply().equalsIgnoreCase("null") && !serviceProviderDetails1.getMedicalReply().equalsIgnoreCase("")) ? serviceProviderDetails1.getMedicalReply() : "-";
                        String addrs = "";
                        if (serviceProviderDetails1.getImagepath() != null && !serviceProviderDetails1.getImagepath().equalsIgnoreCase("") && !serviceProviderDetails1.getImagepath().equalsIgnoreCase("null") && !serviceProviderDetails1.getImagepath().equalsIgnoreCase("no_avatar.jpg")) {
                            addrs = "\n" + Html.fromHtml(getString(R.string.download));
                        } else {
                            addrs = "";
                        }
                        String dstnc = "";
                        if (serviceProviderDetails1.getKm() != null && !serviceProviderDetails1.getKm().equalsIgnoreCase("") && !serviceProviderDetails1.getKm().equalsIgnoreCase("null")) {
                            dstnc = "\n" + Html.fromHtml(getString(R.string.distances)) + serviceProviderDetails1.getKm() + " KM";
                        } else {
                            dstnc = "";
                        }
                        String mdclcst = "";
                        if (medicalCost != null && !medicalCost.equalsIgnoreCase("") && !medicalCost.equalsIgnoreCase("null") && !medicalCost.equalsIgnoreCase("-")) {
                            mdclcst = "\n" + Html.fromHtml(getString(R.string.medicalcost)) + medicalCost;
                        } else {
                            mdclcst = "";
                        }
                        String mdclrply = "";
                        if (medicalDescription != null && !medicalDescription.equalsIgnoreCase("") && !medicalDescription.equalsIgnoreCase("null") && !medicalDescription.equalsIgnoreCase("-")) {
                            mdclrply = "\n" + Html.fromHtml(getString(R.string.medicaldescription)) + medicalDescription;
                        } else {
                            mdclrply = "";
                        }
                        String cstmrDescription = "";
                        if (description != null && !description.equalsIgnoreCase("") && !description.equalsIgnoreCase("null") && !description.equalsIgnoreCase("-")) {
                            cstmrDescription = "\n" + Html.fromHtml(getString(R.string.description)) + description;
                        } else {
                            cstmrDescription = "";
                        }
                        final android.support.v7.app.AlertDialog.Builder alertDialogBuilder1 = new android.support.v7.app.AlertDialog.Builder(getActivity(), R.style.AppCompatAlertDialogStyle);
                        final android.support.v7.app.AlertDialog.Builder alertDialogBuilder2 = new android.support.v7.app.AlertDialog.Builder(getActivity(), R.style.AppCompatAlertDialogStyle);
                        android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(getActivity(), R.style.AppCompatAlertDialogStyle);
                        alertDialogBuilder.setTitle(Html.fromHtml(getString(R.string.transactions)));
                        alertDialogBuilder.setMessage(
                                Html.fromHtml(getString(R.string.orderid)) + serviceProviderDetails1.getOrderid() +
                                        cstmrDescription +
                                        mdclcst +
                                        mdclrply +
                                        dstnc +
                                        addrs);
                        if (serviceProviderDetails1.getImagepath() != null && !serviceProviderDetails1.getImagepath().equalsIgnoreCase("") && !serviceProviderDetails1.getImagepath().equalsIgnoreCase("null") && !serviceProviderDetails1.getImagepath().equalsIgnoreCase("no_avatar.jpg")) {
                            LinearLayout lv = new LinearLayout(getActivity());
                            LinearLayout.LayoutParams vp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
                            lv.setLayoutParams(vp);
                            ImageView image = new ImageView(getActivity());
                            image.setLayoutParams(vp);
                            image.setMaxHeight(10);
                            image.setMaxWidth(10);
                            image.setImageDrawable(getResources().getDrawable(R.drawable.down));
                            lv.addView(image);
                            alertDialogBuilder.setView(lv);
                            image.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    String[] s = {serviceProviderDetails1.getImagepath(), serviceProviderDetails1.getOrderid()};
                                    if (serviceProviderDetails1.getImagepath() != null && !serviceProviderDetails1.getImagepath().equalsIgnoreCase("") && !serviceProviderDetails1.getImagepath().equalsIgnoreCase("null")) {
                                        boolean result = Utility.checkPermission(getContext());
                                        if (result)
                                            new DownloadImage().execute(s);
                                    } else {
                                        Toast.makeText(getContext(), "Image Not Available", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                        alertDialogBuilder.show();
                        if (strNotification.contains("received")) {
                            LayoutInflater li = LayoutInflater.from(getContext());
                            View promptsView = li.inflate(R.layout.custom_view_for_alert_dialogue, null);
                            alertDialogBuilder2.setTitle(Html.fromHtml(getString(R.string.transactions)));
                            alertDialogBuilder2.setView(promptsView);
                            final EditText userInput = (EditText) promptsView
                                    .findViewById(R.id.editTextDialogUserInput);
                            final EditText userCost = (EditText) promptsView
                                    .findViewById(R.id.editTextCost);
                            alertDialogBuilder2.setPositiveButton("Send",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            desc[0] = String.valueOf(userInput.getText());
                                            cost[0] = String.valueOf(userCost.getText());
                                            if ((cost[0].equalsIgnoreCase("null") || cost[0].equalsIgnoreCase("") || cost[0].equalsIgnoreCase("0.00"))) {
                                                Toast.makeText(getContext(), "Please Enter Cost", Toast.LENGTH_LONG).show();
                                            } else {
                                                new SendCostToCustomer().execute();
                                            }

                                        }
                                    });
                        }
                    }
                }

            } catch (Exception e) {
                Toast.makeText(getContext(), "Please try again later...", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            } finally {
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
            String s1, s2;
            s1 = urls[0];
            s2 = urls[1];
            return utilities.downloadImagesToSdCard(s1, s2);
        }

        protected void onPostExecute(File response) {
            progressDialog.dismiss();
            boolean result = Utility.checkPermission(getContext());
            if (response != null && result == true)
                viewimage(response);

        }
    }


    public void viewimage(File fileName) {
        File mypath = null;
        String selectedOutputPath = "";
        selectedOutputPath = fileName != null ? fileName.getPath() : "";
        Log.d("PhotoEditorSDK", "selected camera path " + selectedOutputPath);
        mypath = new File(selectedOutputPath);
        Bitmap b;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri apkURI = FileProvider.getUriForFile(
                getContext(),
                "com.zoftino.android.fileproviders", mypath);
        intent.setDataAndType(apkURI, "image/");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(intent);

    }


    class SendCostToCustomer extends AsyncTask<Void, Void, String> {

        protected void onPreExecute() {
        }

        protected String doInBackground(Void... urls) {
            Utilities utilities = new Utilities(getContext());
            String address = Constants.API_MEDICAL_RECEIVED_ORDER;
            Map<String, String> params = new HashMap<>();
            params.put("medicalid", sharedPreference.getValue(getContext(), Constants.PREF_IS_USER, Constants.PREF_KEY_USER_ID));
            params.put("orderid", orderId);
            params.put("description", desc[0] != null ? desc[0] : "");
            params.put("cost", cost[0] != null ? cost[0] : "");
            return utilities.apiCalls(address, params);

        }

        protected void onPostExecute(String response) {
            try {
                JSONObject jsonObject1 = new JSONObject(response);
                JSONObject jsonObject2 = new JSONObject(jsonObject1.getString("Content"));
                if (response.equals("NO_INTERNET")) {
                    Toast.makeText(getContext(), "Check internet connection", Toast.LENGTH_LONG).show();
                } else if (jsonObject2.getString("status").equalsIgnoreCase("error")) {
                    Toast.makeText(getContext(), jsonObject2.getString("message"), Toast.LENGTH_LONG).show();
                } else {
                    if (jsonObject2.getString("status").equalsIgnoreCase("error")) {
                        Toast.makeText(getContext(), jsonObject2.getString("message"), Toast.LENGTH_LONG).show();
                    } else if (jsonObject2.getString("status").equalsIgnoreCase("success")) {
                        Toast.makeText(getContext(), jsonObject2.getString("result"), Toast.LENGTH_LONG).show();
                    }
                }

            } catch (Exception e) {
                Toast.makeText(getContext(), "Please try again later...", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            } finally {
            }

        }
    }
}
