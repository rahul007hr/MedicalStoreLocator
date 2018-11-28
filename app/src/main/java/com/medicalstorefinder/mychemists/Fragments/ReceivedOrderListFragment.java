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
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.medicalstorefinder.mychemists.Constants.Constants;
import com.medicalstorefinder.mychemists.Constants.SharedPreference;
import com.medicalstorefinder.mychemists.Constants.Utilities;
import com.medicalstorefinder.mychemists.Constants.Utility;
import com.medicalstorefinder.mychemists.GlideImageLoader;
import com.medicalstorefinder.mychemists.Models.ServiceProviderDetailsModel;
import com.medicalstorefinder.mychemists.R;
import com.medicalstorefinder.mychemists.SingleTouchImageViewFragment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.medicalstorefinder.mychemists.Constants.Constants.NO_AVATAR_IMAGE_PATH;


public class ReceivedOrderListFragment extends Fragment {

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

    private ImageView imgRepNotFound;
    String myValue;
    StringBuilder URL_Report;

    List<ServiceProviderDetailsModel> listDetails = new ArrayList<ServiceProviderDetailsModel>();

    SharedPreference sharedPreference = new SharedPreference();
    String cap;
    int _TransactionId = -1;
    ImageView imageView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_received_order_list, null);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        recyclerView = (RecyclerView) v.findViewById(R.id.rep_recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        imageView = (ImageView) v.findViewById(R.id.image_View);
        imgRepNotFound = (ImageView) v.findViewById(R.id.img_rep_not_found);
        imgRepNotFound.setVisibility(View.GONE);
        volumeControl = (SeekBar) v.findViewById(R.id.volume_bar);
        volumeControl.setVisibility(View.GONE);
        distanceTxt = (TextView) v.findViewById(R.id.distanceTxt);
        distanceTxt.setVisibility(View.GONE);
        new RetrieveFeedTask1().execute();
        return v;
    }


    class RetrieveFeedTask1 extends AsyncTask<Void, Void, String> {

        protected void onPreExecute() {
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Void... urls) {
            Utilities utilities = new Utilities(getContext());
            String address = Constants.API_RECEIVED_ORDER_STATUS;
            Map<String, String> params = new HashMap<>();
            params.put("userid", sharedPreference.getValue(getActivity(), Constants.PREF_IS_USER, Constants.PREF_KEY_USER_ID));
            params.put("status", "Pending");
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
                    JSONArray jsonarray = new JSONArray(jsonObject2.getString("result"));
                    if (jsonarray.length() <= 0) {
                        Toast.makeText(getActivity().getBaseContext(), "No more record found.", Toast.LENGTH_SHORT).show();
                    }
                    String userId = sharedPreference.getValue(getActivity(), Constants.PREF_IS_USER, Constants.PREF_KEY_USER_ID);
                    String listOfMedicalUsers = "";
                    for (int i = 0; i < jsonarray.length(); i++) {
                        ServiceProviderDetailsModel serviceProviderDetails1 = new ServiceProviderDetailsModel();
                        JSONObject jsonObject = jsonarray.getJSONObject(i);
                        String s = jsonObject.getString("created_at");
                        String[] s1 = s.split("\\s+");
                        String s2 = s1[0];
                        Date initDate = new SimpleDateFormat("yyyy-MM-dd").parse(s2);
                        SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
                        Date initTime = new SimpleDateFormat("hh:mm:ss").parse(s1[1]);
                        SimpleDateFormat timeFormatter = new SimpleDateFormat("hh:mm a");
                        String parsedDate = formatter.format(initDate) + " " + timeFormatter.format(initTime);
                        parsedDate = parsedDate.toUpperCase();
                        String listOfOrderIds = sharedPreference.getValue(getContext(), Constants.PREF_IS_USER, Constants.PREF_KEY_ORDER_ID_LIST);
                        if (!listOfOrderIds.contains((jsonObject.getString("orderid")).toString())
                                && (jsonObject.getString("cost").equalsIgnoreCase("")
                                || jsonObject.getString("cost") == null || jsonObject.getString("cost").equalsIgnoreCase("null"))) {
                            serviceProviderDetails1.setOrderMainId(jsonObject.getString("mainorderid"));
                            serviceProviderDetails1.setMedicalCost(jsonObject.getString("cost"));
                            serviceProviderDetails1.setOrderid(jsonObject.getString("orderid"));
                            serviceProviderDetails1.setDescription(jsonObject.getString("description"));
                            serviceProviderDetails1.setImagepath(jsonObject.getString("imagepath"));
                            serviceProviderDetails1.setOrderstatus(jsonObject.getString("orderstatus"));
                            String km = (jsonObject.getString("km"));
                            if (km == null || km.equalsIgnoreCase("null") || km.equalsIgnoreCase("")) {
                                km = "0.00";
                            }
                            if (km.toLowerCase().contains("-")) {
                                String[] kmList = km.split(",");
                                for (int k = 0; k < kmList.length; k++) {
                                    if (kmList[k].toLowerCase().contains(userId.toLowerCase())) {
                                        String[] kms = kmList[k].split("-");
                                        DecimalFormat roundup = new DecimalFormat("#.##");
                                        serviceProviderDetails1.setKm(Double.valueOf(roundup.format(Double.parseDouble(kms[0]))).toString());
                                    }
                                }
                            } else {
                                DecimalFormat roundup = new DecimalFormat("#.##");
                                serviceProviderDetails1.setKm(Double.valueOf(roundup.format(Double.parseDouble(km))).toString());
                            }
                            String string = jsonObject.getString("address");
                            String[] bits = string.split(",");
                            String lastWord = "";
                            if (bits.length > 2)
                                lastWord = bits[bits.length - 3] + ", " + bits[bits.length - 2] + ", " + bits[bits.length - 1];
                            serviceProviderDetails1.setAddress(lastWord);
                            serviceProviderDetails1.setNotificationTime(parsedDate);
                            listDetails.add(serviceProviderDetails1);
                        }
                    }
                    if (listDetails.size() <= 0) {
                        imgRepNotFound.setVisibility(View.VISIBLE);
                        final Animation animImgRecordNotFound = AnimationUtils.loadAnimation(getActivity(), R.anim.shake_interpolator);
                        imgRepNotFound.startAnimation(animImgRecordNotFound);
                    }
                    if (listDetails.size() > 0) {
                        adapter = new ServiceProviderReportCardAdapter(getContext(), listDetails);
                        recyclerView.setAdapter(adapter);
                    }
                }
            } catch (Exception e1) {
                Toast.makeText(getActivity().getApplicationContext(), "Please try again later...", Toast.LENGTH_LONG).show();
            } finally {
                progressDialog.dismiss();
            }
        }
    }

    public class ServiceProviderReportCardAdapter extends RecyclerView.Adapter<ServiceProviderReportCardAdapter.ViewHolder> {

        ServiceProviderReportCardAdapter serviceProviderReportCardAdapter = this;
        private Context context;
        private ReceivedOrderListFragment.RetrieveFeedTask1 context1;

        List<ServiceProviderDetailsModel> listServiceProviderDetails;

        public ServiceProviderReportCardAdapter(Context context, List<ServiceProviderDetailsModel> listServiceProviderDetails) {
            this.context = context;
            this.listServiceProviderDetails = listServiceProviderDetails;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.received_order_card_items, parent, false);
            ViewHolder viewHolder = new ViewHolder(v);
            final Animation anim_record_item = AnimationUtils.loadAnimation(parent.getContext(), R.anim.swipe_down);
            viewHolder.itemView.startAnimation(anim_record_item);
            return viewHolder;
        }


        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            try {
                ServiceProviderDetailsModel tr = listServiceProviderDetails.get(position);
                holder.vtxtLocation.setText("Location : " + tr.getAddress());
                holder.vtxtTime.setText("Date : " + tr.getNotificationTime());
                holder.km.setText("Distance : " + tr.getKm() + " KM");
                if (!tr.getImagepath().equalsIgnoreCase("") && tr.getImagepath() != null && !tr.getImagepath().equalsIgnoreCase("no_avatar.jpg")) {
                    RequestOptions options = new RequestOptions()
                            .centerCrop()
                            .placeholder(R.drawable.profile_pic)
                            .priority(Priority.HIGH);
                    new GlideImageLoader(holder.imageViews,
                            holder.spinner).load(tr.getImagepath(), options);

                } else if (!tr.getImagepath().equalsIgnoreCase("") && tr.getImagepath() != null) {
                    RequestOptions options = new RequestOptions()
                            .centerCrop()
                            .placeholder(R.drawable.profile_pic)
                            .priority(Priority.HIGH);
                    new GlideImageLoader(holder.imageViews,
                            holder.spinner).load(NO_AVATAR_IMAGE_PATH + tr.getImagepath(), options);

                } else {
                    RequestOptions options = new RequestOptions()
                            .centerCrop()
                            .placeholder(R.drawable.profile_pic)
                            .priority(Priority.HIGH);
                    new GlideImageLoader(holder.imageViews,
                            holder.spinner).load(NO_AVATAR_IMAGE_PATH + "no_avatar.jpg", options);
                }
                tr.setStatus("Pending");
                switch (tr.getStatus()) {
                    case "Pending":
                        holder.vtxtStatus.setText("PENDING");
                        holder.vtxtStatus.setTextColor(getActivity().getApplicationContext().getResources().getColor(R.color.primary_dark));
                        holder.cardViewTxCardItem.setCardBackgroundColor(Color.parseColor("#ffffff"));
                        break;
                    case "success":
                        holder.vtxtStatus.setText("SUCCESS");
                        holder.vtxtStatus.setTextColor(getActivity().getApplicationContext().getResources().getColor(R.color.tx_SUCCESS));
                        holder.cardViewTxCardItem.setCardBackgroundColor(Color.parseColor("#ffffff"));
                        break;

                }
            } catch (Exception e1) {
            }
        }

        @Override
        public int getItemCount() {
            return listServiceProviderDetails.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            public TextView vtxtLocation;
            public TextView vtxtStatus;
            public CardView cardViewTxCardItem;
            public ImageView imageViews;
            public ProgressBar spinner;
            public TextView vtxtTime, km;

            public ViewHolder(View itemView) {
                super(itemView);
                final View view = itemView;
                vtxtLocation = (TextView) itemView.findViewById(R.id.location);
                vtxtTime = (TextView) itemView.findViewById(R.id.time);
                km = (TextView) itemView.findViewById(R.id.km);
                vtxtStatus = (TextView) itemView.findViewById(R.id.status);
                imageViews = (ImageView) itemView.findViewById(R.id.image_View);
                cardViewTxCardItem = (CardView) itemView.findViewById(R.id.cardview_tx_card_item);
                spinner = (ProgressBar) itemView.findViewById(R.id.progressBar1);
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
                        String lStatus = "Pending";
                        switch (tr.getOrderstatus()) {
                            case "Pending":
                                lStatus = "PENDING";
                                break;
                            case "Success":
                                lStatus = "SUCCESS";
                                break;

                        }
                        String addrs = "";
                        if (tr.getImagepath() != null && !tr.getImagepath().equalsIgnoreCase("") && !tr.getImagepath().equalsIgnoreCase
                                ("null") && !tr.getImagepath().equalsIgnoreCase("no_avatar.jpg")) {
                            addrs = "\n" + Html.fromHtml(getString(R.string.download));
                        } else {
                            addrs = "";
                        }
                        String dstnc = "";
                        if (tr.getKm() != null && !tr.getKm().equalsIgnoreCase("") && !tr.getKm().equalsIgnoreCase("null")) {
                            dstnc = "\n" + Html.fromHtml(getString(R.string.distances)) + tr.getKm() + " KM";
                        } else {
                            dstnc = "";
                        }
                        String mdclcst = "";
                        if (tr.getMedicalCost() != null && !tr.getMedicalCost().equalsIgnoreCase("") && !tr.getMedicalCost().equalsIgnoreCase("null") && !tr.getMedicalCost().equalsIgnoreCase("-")) {
                            mdclcst = "\n" + Html.fromHtml(getString(R.string.medicalcost)) + tr.getMedicalCost();
                        } else {
                            mdclcst = "";
                        }
                        String mdclrply = "";
                        if (tr.getMedicalReply() != null && !tr.getMedicalReply().equalsIgnoreCase("") && !tr.getMedicalReply().equalsIgnoreCase("null") && !tr.getMedicalReply().equalsIgnoreCase("-")) {
                            mdclrply = "\n" + Html.fromHtml(getString(R.string.medicaldescription)) + tr.getMedicalReply();
                        } else {
                            mdclrply = "";
                        }
                        String cstmrDescription = "";
                        if (tr.getDescription() != null && !tr.getDescription().equalsIgnoreCase("") && !tr.getDescription().equalsIgnoreCase("null") && !tr.getDescription().equalsIgnoreCase("-")) {
                            cstmrDescription = "\n" + Html.fromHtml(getString(R.string.description)) + tr.getDescription();
                        } else {
                            cstmrDescription = "";
                        }
                        final android.support.v7.app.AlertDialog.Builder alertDialogBuilder1 = new android.support.v7.app.AlertDialog.Builder(getActivity(), R.style.AppCompatAlertDialogStyle);
                        android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(getActivity(), R.style.AppCompatAlertDialogStyle);
                        alertDialogBuilder.setTitle(Html.fromHtml(getString(R.string.transactions)));
                        alertDialogBuilder.setMessage(
                                Html.fromHtml(getString(R.string.orderid)) + tr.getOrderMainId() +
                                        cstmrDescription +
                                        mdclcst +
                                        mdclrply +
                                        dstnc +
                                        addrs);
                        if (tr.getImagepath() != null && !tr.getImagepath().equalsIgnoreCase("") && !tr.getImagepath().equalsIgnoreCase("null") && !tr.getImagepath().equalsIgnoreCase("no_avatar.jpg")) {
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
                                    String[] s = {tr.getImagepath(), tr.getOrderid()};
                                    if (tr.getImagepath() != null && !tr.getImagepath().equalsIgnoreCase("") && !tr.getImagepath().equalsIgnoreCase("null")) {
                                        boolean result = Utility.checkPermission(getContext());
                                        if (result)
                                            new DownloadImage().execute(s);
                                    } else {
                                        Toast.makeText(getContext(), "Image Not Available", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                        if (lStatus.equalsIgnoreCase("Pending")) {
                            alertDialogBuilder.setPositiveButton("Accept",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            alertDialogBuilder1.show();
                                        }
                                    });
                        }
                        alertDialogBuilder.show();
                        LayoutInflater li = LayoutInflater.from(context);
                        View promptsView = li.inflate(R.layout.custom_view_for_alert_dialogue, null);
                        alertDialogBuilder1.setTitle(Html.fromHtml(getString(R.string.transactions)));
                        alertDialogBuilder1.setView(promptsView);
                        final EditText userInput = (EditText) promptsView
                                .findViewById(R.id.editTextDialogUserInput);
                        final EditText userCost = (EditText) promptsView
                                .findViewById(R.id.editTextCost);
                        if (lStatus.equalsIgnoreCase("Pending")) {
                            alertDialogBuilder1.setPositiveButton("Send",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            desc[0] = String.valueOf(userInput.getText());
                                            cost[0] = String.valueOf(userCost.getText());
                                            serviceProviderDetails.setOrderid(tr.getOrderid());
                                            if ((cost[0].equalsIgnoreCase("null") || cost[0].equalsIgnoreCase("") || cost[0].equalsIgnoreCase("0.00"))) {
                                                Toast.makeText(getContext(), "Please Enter Cost", Toast.LENGTH_LONG).show();
                                            } else {
                                                new SendCostToCustomer().execute();
                                            }

                                        }
                                    });
                        }
                    }

                });
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
            params.put("orderid", serviceProviderDetails.getOrderid());
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
                        String listOfOrderIds = sharedPreference.getValue(getContext(), Constants.PREF_IS_USER, Constants.PREF_KEY_ORDER_ID_LIST);
                        if (listOfOrderIds.equalsIgnoreCase("")) {
                            listOfOrderIds = serviceProviderDetails.getOrderid();
                        } else {
                            listOfOrderIds += "," + serviceProviderDetails.getOrderid();
                        }
                        sharedPreference.putValue(getActivity(), Constants.PREF_SERVICE_PROVIDER_IDS, Constants.PREF_KEY_ORDER_ID_LIST, listOfOrderIds);
                        new RetrieveFeedTask1().execute();
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
