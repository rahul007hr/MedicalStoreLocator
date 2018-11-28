package com.medicalstorefinder.mychemists.Fragments;


import android.app.ProgressDialog;
import android.content.Context;
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
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.medicalstorefinder.mychemists.Activity.CustomerActivity;
import com.medicalstorefinder.mychemists.Constants.Constants;
import com.medicalstorefinder.mychemists.Constants.SharedPreference;
import com.medicalstorefinder.mychemists.Constants.Utilities;
import com.medicalstorefinder.mychemists.GlideImageLoader;
import com.medicalstorefinder.mychemists.Models.ServiceProviderDetailsModel;
import com.medicalstorefinder.mychemists.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.medicalstorefinder.mychemists.Constants.Constants.NO_AVATAR_IMAGE_PATH;


public class ServiceProviderListFragment extends Fragment implements View.OnClickListener {

    ProgressDialog progressDialog;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private SeekBar volumeControl = null;
    TextView distanceTxt, medicalStoreCntTxt;
    int progressChanged1 = 2;

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
        postOrderBtn = (Button) v.findViewById(R.id.postOrderBtn);
        imgRepNotFound = (ImageView) v.findViewById(R.id.img_rep_not_found);
        imgRepNotFound.setVisibility(View.GONE);
        postOrderBtn.setOnClickListener(this);
        volumeControl = (SeekBar) v.findViewById(R.id.volume_bar);
        distanceTxt = (TextView) v.findViewById(R.id.distanceTxt);
        medicalStoreCntTxt = (TextView) v.findViewById(R.id.medicalStoreCntTxt);
        CustomerActivity.navigation.getMenu().findItem(R.id.NearbyServiceProviderList).setEnabled(true);
        CustomerActivity.navigation.getMenu().findItem(R.id.NearbyServiceProviderList).setChecked(true);
        new RetrieveFeedTask1().execute();
        volumeControl.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {


            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int progressChanged = progress + 10;
                if (progress <= 19) {
                    progressChanged = progress + 10;
                } else {
                    progressChanged = progress;
                }
                progressChanged = progressChanged / 10;
                progressChanged1 = progressChanged;
                distanceTxt.setText("Select Range of Medical Store : " + progressChanged + " Km");
                medicalStoreCntTxt.setVisibility(View.VISIBLE);
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                new RetrieveFeedTask1().execute();

            }
        });
        return v;
    }

    @Override
    public void onClick(View view) {
        new PostOrder().execute();

    }


    class RetrieveFeedTask1 extends AsyncTask<Void, Void, String> {

        protected void onPreExecute() {
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Void... urls) {
            Utilities utilities = new Utilities(getContext());
            String address = Constants.API_GET_NEARBY;
            Map<String, String> params = new HashMap<>();
            params.put("latitude", sharedPreference.getValue(getContext(), Constants.PREF_USER_ORDER_Latitude, Constants.PREF_USER_ORDER_Latitude));
            params.put("longitude", sharedPreference.getValue(getContext(), Constants.PREF_USER_ORDER_Longitude, Constants.PREF_USER_ORDER_Longitude));
            params.put("radius", String.valueOf(progressChanged1));
            params.put("userid", sharedPreference.getValue(getActivity(), Constants.PREF_IS_USER, Constants.PREF_KEY_USER_ID));
            return utilities.apiCalls(address, params);
        }

        protected void onPostExecute(String response) {
            try {
                JSONObject jsonObject1 = new JSONObject(response);
                JSONObject jsonObject2 = new JSONObject(jsonObject1.getString("Content"));
                imgRepNotFound.setVisibility(View.GONE);
                postOrderBtn.setVisibility(View.VISIBLE);
                if (response.equals("NO_INTERNET")) {
                    Toast.makeText(getActivity().getBaseContext(), "Check internet connection", Toast.LENGTH_LONG).show();
                } else if (jsonObject2.getString("status").equalsIgnoreCase("error")) {
                    imgRepNotFound.setVisibility(View.VISIBLE);
                    postOrderBtn.setVisibility(View.GONE);
                    final Animation animImgRecordNotFound = AnimationUtils.loadAnimation(getActivity(), R.anim.shake_interpolator);
                    imgRepNotFound.startAnimation(animImgRecordNotFound);
                } else {
                    if (listDetails.size() > 0) {
                        listDetails.clear();
                    }
                    JSONArray jsonarray = new JSONArray(jsonObject2.getString("result"));
                    if (jsonarray.length() <= 0) {
                        postOrderBtn.setVisibility(View.GONE);
                        Toast.makeText(getActivity().getBaseContext(), "No more record found.", Toast.LENGTH_SHORT).show();
                    }
                    String listOfMedicalUsers = "";
                    String listOfKm = "";
                    for (int i = 0; i < jsonarray.length(); i++) {
                        ServiceProviderDetailsModel serviceProviderDetails = new ServiceProviderDetailsModel();
                        JSONObject jsonObject = jsonarray.getJSONObject(i);
                        serviceProviderDetails.setOrderstatus(jsonObject.getString("loginstatus"));
                        serviceProviderDetails.setLocation(jsonObject.getString("distance"));
                        serviceProviderDetails.setImagepath(jsonObject.getString("profilepic"));
                        serviceProviderDetails.setShopName(jsonObject.getString("shopname"));
                        serviceProviderDetails.setLoginStatus(jsonObject.getString("loginstatus"));
                        String rt = (jsonObject.getString("rating") != null && !jsonObject.getString("rating").equalsIgnoreCase("null")) ? jsonObject.getString("rating") : "0";
                        serviceProviderDetails.setRating(Float.valueOf(rt));
                        if (listOfMedicalUsers.equalsIgnoreCase("")) {
                            listOfMedicalUsers = jsonObject.getString("id");
                        } else {
                            listOfMedicalUsers += "," + jsonObject.getString("id");
                        }
                        if (listOfKm.equalsIgnoreCase("")) {
                            listOfKm = jsonObject.getString("distance") + "-" + jsonObject.getString("id");
                        } else {
                            listOfKm += "," + jsonObject.getString("distance") + "-" + jsonObject.getString("id");
                        }
                        medicalStoreCntTxt.setVisibility(View.VISIBLE);
                        medicalStoreCntTxt.setText("Medical Stores : " + (i + 1));
                        listDetails.add(serviceProviderDetails);
                    }
                    sharedPreference.clearSharedPreference(getActivity(), Constants.PREF_SERVICE_PROVIDER_IDS);
                    sharedPreference.createSharedPreference(getActivity(), Constants.PREF_SERVICE_PROVIDER_IDS);
                    sharedPreference.putValue(getActivity(), Constants.PREF_SERVICE_PROVIDER_IDS, Constants.PREF_SERVICE_PROVIDER_IDS, listOfMedicalUsers);
                    sharedPreference.putValue(getActivity(), Constants.DISTANCE, Constants.DISTANCE, listOfKm);
                    if (listDetails.size() <= 0) {
                        imgRepNotFound.setVisibility(View.VISIBLE);
                        final Animation animImgRecordNotFound = AnimationUtils.loadAnimation(getActivity(), R.anim.shake_interpolator);
                        imgRepNotFound.startAnimation(animImgRecordNotFound);
                        postOrderBtn.setVisibility(View.GONE);
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
        private ServiceProviderListFragment.RetrieveFeedTask1 context1;

        List<ServiceProviderDetailsModel> listServiceProviderDetails;

        public ServiceProviderReportCardAdapter(Context context, List<ServiceProviderDetailsModel> listServiceProviderDetails) {
            this.context = context;
            this.listServiceProviderDetails = listServiceProviderDetails;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.service_provider_card_item_with_ratings, parent, false);
            ViewHolder viewHolder = new ViewHolder(v);
            final Animation anim_record_item = AnimationUtils.loadAnimation(parent.getContext(), R.anim.swipe_down);
            viewHolder.itemView.startAnimation(anim_record_item);
            return viewHolder;
        }


        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            try {
                ServiceProviderDetailsModel tr = listServiceProviderDetails.get(position);
                holder.vtxtLocation.setText("Shop Name : " + tr.getShopName());
                String date = (tr.getNotificationTime() != null && !tr.getNotificationTime().equalsIgnoreCase("null")) ? "Date : " + tr.getNotificationTime() : "";
                Float ratings = (tr.getRating() != null) ? tr.getRating() : 0;
                holder.vtxtTime.setText(date);
                holder.rb.setRating(ratings);
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
                String status = (tr.getLoginStatus() != null && !tr.getLoginStatus().equalsIgnoreCase("null")) ? tr.getLoginStatus() : "0";
                if (status.equalsIgnoreCase("1")) {
                    status = "Available";
                } else {
                    status = "Unavailable";
                }
                switch (status) {
                    case "Unavailable":
                        holder.vtxtStatus.setText("UNAVAILABLE");
                        holder.vtxtStatus.setTextColor(getActivity().getApplicationContext().getResources().getColor(R.color.tx_FAILURE));
                        holder.cardViewTxCardItem.setCardBackgroundColor(Color.parseColor("#ffffff"));
                        break;
                    case "Available":
                        holder.vtxtStatus.setText("AVAILABLE");
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
            public String s, s1;
            public ProgressBar spinner;
            public TextView vtxtTime;
            public RatingBar rb;


            public ViewHolder(View itemView) {
                super(itemView);
                final View view = itemView;
                vtxtLocation = (TextView) itemView.findViewById(R.id.location);
                vtxtTime = (TextView) itemView.findViewById(R.id.time);
                vtxtStatus = (TextView) itemView.findViewById(R.id.status);
                imageViews = (ImageView) itemView.findViewById(R.id.image_View);
                rb = (RatingBar) itemView.findViewById(R.id.ratingBar);
                cardViewTxCardItem = (CardView) itemView.findViewById(R.id.cardview_tx_card_item);
                spinner = (ProgressBar) itemView.findViewById(R.id.progressBar1);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ServiceProviderDetailsModel tr = listServiceProviderDetails.get(getAdapterPosition());
                        s = tr.getEmailId();
                        s1 = tr.getPassword();
                        _TransactionId = tr.ID;
                        String lStatus = "";
                        android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(getActivity(), R.style.AppCompatAlertDialogStyle);
                        alertDialogBuilder.setTitle(Html.fromHtml(getString(R.string.transactions)));
                        alertDialogBuilder.setMessage(
                                Html.fromHtml(getString(R.string.emailids)) + tr.getEmailId() +
                                        "\n\n" + Html.fromHtml(getString(R.string.mobileno)) + tr.getCustomerNo() + "\n" +
                                        "\n" + Html.fromHtml(getString(R.string.deliverydate)) + tr.getDeliveryDate() +
                                        "\n" + Html.fromHtml(getString(R.string.servicetype)) + tr.getServiceTypeName() +
                                        "\n" + Html.fromHtml(getString(R.string.locations)) + tr.getLocation());
                    }

                });

            }
        }
    }

    class PostOrder extends AsyncTask<Void, Void, String> {

        protected void onPreExecute() {
        }

        protected String doInBackground(Void... urls) {
            Utilities utilities = new Utilities(getContext());
            String address = Constants.API_POST_ORDER;
            Map<String, String> params = new HashMap<>();
            params.put("userid", sharedPreference.getValue(getContext(), Constants.PREF_IS_USER, Constants.PREF_KEY_USER_ID));
            params.put("latitude", sharedPreference.getValue(getActivity(), Constants.PREF_USER_ORDER_Latitude, Constants.PREF_USER_ORDER_Latitude));
            params.put("longitude", sharedPreference.getValue(getActivity(), Constants.PREF_USER_ORDER_Longitude, Constants.PREF_USER_ORDER_Longitude));
            params.put("address", sharedPreference.getValue(getActivity(), Constants.PREF_USER_ORDER_getAddress, Constants.PREF_USER_ORDER_getAddress));
            params.put("description", sharedPreference.getValue(getActivity(), Constants.PREF_USER_ORDER_Description, Constants.PREF_USER_ORDER_Description));
            params.put("imagepath", sharedPreference.getValue(getActivity(), Constants.PREF_USER_ORDER_ImagePath, Constants.PREF_USER_ORDER_ImagePath));
            params.put("medicalids", sharedPreference.getValue(getActivity(), Constants.PREF_SERVICE_PROVIDER_IDS, Constants.PREF_SERVICE_PROVIDER_IDS));
            params.put("km", sharedPreference.getValue(getActivity(), Constants.DISTANCE, Constants.DISTANCE));
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
                        Toast.makeText(getContext(), jsonObject2.getString("status"), Toast.LENGTH_LONG).show();
                        ChooseOrderTypeFragment fragment2 = new ChooseOrderTypeFragment();
                        FragmentManager fragmentManager = getFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.containerView, fragment2);
                        fragmentTransaction.commit();
                        JSONObject jsonObject = new JSONObject(jsonObject2.getString("result"));
                        sharedPreference.putValue(getContext(), Constants.PREF_IS_USER, Constants.PREF_KEY_ORDER_ID, jsonObject.getString("orderid"));

                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
            }

        }
    }

}
