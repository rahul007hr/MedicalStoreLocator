package com.medicalstorefinder.mychemists.Fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.medicalstorefinder.mychemists.Constants.Constants;
import com.medicalstorefinder.mychemists.Constants.SharedPreference;
import com.medicalstorefinder.mychemists.Constants.Utilities;
import com.medicalstorefinder.mychemists.GlideImageLoader;
import com.medicalstorefinder.mychemists.Models.ServiceProviderDetailsModel;
import com.medicalstorefinder.mychemists.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.medicalstorefinder.mychemists.Constants.Constants.NO_AVATAR_IMAGE_PATH;


public class ServiceProviderListUsingOrderStatusFragment extends Fragment {


    ProgressDialog progressDialog;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private SeekBar volumeControl = null;
    TextView distanceTxt;
    int progressChanged1 = 0;
    String strtext = "";
    private ImageView imgRepNotFound;
    String myValue;
    StringBuilder URL_Report;

    List<ServiceProviderDetailsModel> listDetails = new ArrayList<ServiceProviderDetailsModel>();

    SharedPreference sharedPreference = new SharedPreference();
    String cap;
    int _TransactionId = -1;
    ImageView imageView;
    Button postOrderBtn;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_service_provider_list, null);
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
        postOrderBtn = (Button) v.findViewById(R.id.postOrderBtn);
        postOrderBtn.setVisibility(View.GONE);
        volumeControl = (SeekBar) v.findViewById(R.id.volume_bar);
        volumeControl.setVisibility(View.GONE);
        distanceTxt = (TextView) v.findViewById(R.id.distanceTxt);
        distanceTxt.setVisibility(View.GONE);
        strtext = getArguments().getString("key");
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
            String address = Constants.API_SERVICE_PROVIDER_LIST_USING_ORDER_STATUS;
            Map<String, String> params = new HashMap<>();
            params.put("userid", sharedPreference.getValue(getActivity(), Constants.PREF_IS_USER, Constants.PREF_KEY_USER_ID));
            params.put("status", strtext);
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
                    String listOfMedicalUsers = "";
                    for (int i = 0; i < jsonarray.length(); i++) {
                        ServiceProviderDetailsModel serviceProviderDetails = new ServiceProviderDetailsModel();
                        JSONObject jsonObject = jsonarray.getJSONObject(i);
                        String s = jsonObject.getString("createddate");
                        String[] s1 = s.split("\\s+");
                        String s2 = s1[0];
                        Date initDate = new SimpleDateFormat("yyyy-MM-dd").parse(s2);
                        SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
                        Date initTime = new SimpleDateFormat("hh:mm:ss").parse(s1[1]);
                        SimpleDateFormat timeFormatter = new SimpleDateFormat("hh:mm a");
                        String parsedDate = formatter.format(initDate) + " " + timeFormatter.format(initTime);
                        parsedDate = parsedDate.toUpperCase();
                        serviceProviderDetails.setOrderid(jsonObject.getString("orderid"));
                        serviceProviderDetails.setDescription(jsonObject.getString("description"));
                        serviceProviderDetails.setImagepath(jsonObject.getString("imagepath"));
                        String string = jsonObject.getString("address");
                        String[] bits = string.split(",");
                        String lastWord = "";
                        if (bits.length > 2)
                            lastWord = bits[bits.length - 3] + ", " + bits[bits.length - 2] + ", " + bits[bits.length - 1];
                        serviceProviderDetails.setAddress(lastWord);
                        serviceProviderDetails.setNotificationTime(parsedDate);
                        serviceProviderDetails.setLatitude(jsonObject.getString("latitude"));
                        serviceProviderDetails.setLongitude(jsonObject.getString("longitude"));
                        serviceProviderDetails.setMobile(jsonObject.getString("mobile"));
                        serviceProviderDetails.setCreateddate(jsonObject.getString("createddate"));
                        serviceProviderDetails.setOrderstatus(jsonObject.getString("orderstatus"));
                        listDetails.add(serviceProviderDetails);
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
                progressDialog.dismiss();
            }
        }
    }

    public class ServiceProviderReportCardAdapter extends RecyclerView.Adapter<ServiceProviderReportCardAdapter.ViewHolder> {

        ServiceProviderReportCardAdapter serviceProviderReportCardAdapter = this;
        private Context context;
        private ServiceProviderListUsingOrderStatusFragment.RetrieveFeedTask1 context1;

        List<ServiceProviderDetailsModel> listServiceProviderDetails;

        public ServiceProviderReportCardAdapter(Context context, List<ServiceProviderDetailsModel> listServiceProviderDetails) {
            this.context = context;
            this.listServiceProviderDetails = listServiceProviderDetails;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.service_provider_card_item, parent, false);
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
                switch (tr.getOrderstatus()) {
                    case "Canceled":
                        holder.vtxtStatus.setText("CANCELED");
                        holder.vtxtStatus.setTextColor(getActivity().getApplicationContext().getResources().getColor(R.color.tx_FAILURE));
                        holder.cardViewTxCardItem.setCardBackgroundColor(Color.parseColor("#ffffff"));
                        break;
                    case "Completed":
                        holder.vtxtStatus.setText("COMPLETED");
                        holder.vtxtStatus.setTextColor(getActivity().getApplicationContext().getResources().getColor(R.color.tx_SUCCESS));
                        holder.cardViewTxCardItem.setCardBackgroundColor(Color.parseColor("#ffffff"));
                        break;
                    case "On Hold":
                        holder.vtxtStatus.setText("ON HOLD");
                        holder.vtxtStatus.setTextColor(getActivity().getApplicationContext().getResources().getColor(R.color.primary_dark));
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
            public TextView vtxtTime;

            public ViewHolder(View itemView) {
                super(itemView);
                final View view = itemView;
                vtxtLocation = (TextView) itemView.findViewById(R.id.location);
                vtxtTime = (TextView) itemView.findViewById(R.id.time);
                vtxtStatus = (TextView) itemView.findViewById(R.id.status);
                imageViews = (ImageView) itemView.findViewById(R.id.image_View);
                cardViewTxCardItem = (CardView) itemView.findViewById(R.id.cardview_tx_card_item);
                spinner = (ProgressBar) itemView.findViewById(R.id.progressBar1);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final ServiceProviderDetailsModel tr = listServiceProviderDetails.get(getAdapterPosition());
                        String lStatus = "Success";
                        switch (tr.getOrderstatus()) {
                            case "Pending":
                                lStatus = "PENDING";
                                break;
                            case "Success":
                                lStatus = "SUCCESS";
                                break;

                        }
                        android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(getActivity(), R.style.AppCompatAlertDialogStyle);
                        alertDialogBuilder.setTitle(Html.fromHtml(getString(R.string.transactions)));
                        alertDialogBuilder.setMessage(
                                Html.fromHtml(getString(R.string.orderid)) + tr.getOrderid() +
                                        "\n" + Html.fromHtml(getString(R.string.description)) + tr.getDescription() +
                                        "\n" + Html.fromHtml(getString(R.string.address)) + tr.getAddress() +
                                        "\n\n" + Html.fromHtml(getString(R.string.mobileno)) + tr.getMobile() + "\n" +
                                        "\n" + Html.fromHtml(getString(R.string.createddate)) + tr.getNotificationTime());
                        final String imagePath = tr.getImagepath();
                        final String descriptionss = tr.getDescription();
                        if (lStatus.equalsIgnoreCase("SUCCESS")) {
                            alertDialogBuilder.setPositiveButton("Create Order",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            Bundle bundle = new Bundle();
                                            bundle.putString("imagePath", imagePath);
                                            bundle.putString("description", descriptionss);
                                            PostOrderFragment fragment2 = new PostOrderFragment();
                                            fragment2.setArguments(bundle);
                                            FragmentManager fragmentManager = getFragmentManager();
                                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                            fragmentTransaction.replace(R.id.containerView, fragment2);
                                            fragmentTransaction.addToBackStack(null);
                                            fragmentTransaction.commit();

                                        }
                                    });
                        }
                        alertDialogBuilder.show();
                    }

                });

            }
        }
    }
}
