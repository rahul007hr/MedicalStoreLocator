package com.medicalstorefinder.medicalstorelocator.Fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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
public class ServiceProviderListFragment extends Fragment implements View.OnClickListener  {

    int _PageNo = 1;

    ProgressDialog progressDialog;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;

    private Button btnReportLoad;
    private ImageView imgRepNotFound;
    String myValue;
    StringBuilder URL_Report;

    List<ServiceProviderDetailsModel> listDetails = new ArrayList<ServiceProviderDetailsModel>();

    SharedPreference sharedPreference = new SharedPreference();
    String cap;
    int _TransactionId = -1;
    ImageView imageView;

//    LinearLayout linearLayoutTxCardItem;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_service_provider_list,null);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        recyclerView = (RecyclerView) v.findViewById(R.id.rep_recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        Toolbar toolbar=(Toolbar)v.findViewById(R.id.toolbar);

//        imageView=(ImageView)v.findViewById(R.id.profile);


        btnReportLoad=(Button) v.findViewById(R.id.report_load_btn);
        btnReportLoad.setOnClickListener(this);

        imgRepNotFound = (ImageView) v.findViewById(R.id.img_rep_not_found);
        imgRepNotFound.setVisibility(View.GONE);

        Bundle bundle = this.getArguments();
        myValue = bundle.getString("message");
        cap = myValue.substring(0, 1).toUpperCase() + myValue.substring(1);
        toolbar.setTitle(cap);

//        String strtext = getArguments().getString("Paanwala");

        new RetrieveFeedTask1().execute();

        return v;
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.report_load_btn:

                new RetrieveFeedTask1().execute();

                break;
        }
    }


    class RetrieveFeedTask1 extends AsyncTask<Void, Void, String> {

        protected void onPreExecute() {
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Void... urls) {

            Utilities utilities = new Utilities(  getActivity().getApplicationContext() );
            SharedPreference sharedPreference = new SharedPreference();

            URL_Report= new StringBuilder(Constants.API_GetServiceProviders);
            URL_Report.append("?Id=1");
            URL_Report.append("&pUserId=" + sharedPreference.getValue(getActivity().getBaseContext(), Constants.PREF_ISAD, Constants.PREF_KEY_USER_Email));
            URL_Report.append("&pKey=" + sharedPreference.getValue(getActivity().getBaseContext(), Constants.PREF_ISAD, Constants.PREF_KEY_USER_PASS));
            URL_Report.append("&cType=" + cap );
            URL_Report.append("&page=" + _PageNo );

            return utilities.apiCall(URL_Report.toString());
        }

        protected void onPostExecute(String response) {

            try {

                imgRepNotFound.setVisibility(View.GONE);

                if (response.equals("NO_INTERNET")) {
                    Toast.makeText(getActivity().getBaseContext(), "Check internet connection", Toast.LENGTH_LONG).show();
                } else if (response.equals("ERROR")) {
                    imgRepNotFound.setVisibility(View.VISIBLE);

                    final Animation animImgRecordNotFound = AnimationUtils.loadAnimation(getActivity(), R.anim.shake_interpolator);
                    imgRepNotFound.startAnimation(animImgRecordNotFound);

                    btnReportLoad.setVisibility(View.GONE);
                    Toast.makeText(getActivity().getBaseContext(), "Somthing went wrong", Toast.LENGTH_LONG).show();
                } else {


                    JSONArray jsonarray = new JSONArray(response);

                    if (jsonarray.length() <= 0) {
                        btnReportLoad.setVisibility(View.GONE);
                        Toast.makeText(getActivity().getBaseContext(), "No more record found.", Toast.LENGTH_SHORT).show();
                    }


                    for (int i = 0; i < jsonarray.length(); i++) {
                        ServiceProviderDetailsModel serviceProviderDetails = new ServiceProviderDetailsModel();
                        JSONObject json = jsonarray.getJSONObject(i);

                        serviceProviderDetails.setID(json.getInt("Id"));
                        serviceProviderDetails.setEmailId(json.getString("Email_Id"));
                        serviceProviderDetails.setCustomerNo(json.getString("Mobile_Number"));
                        serviceProviderDetails.setFullName(json.getString("Full_Name"));

                        serviceProviderDetails.setStatus(json.getString("cStatus"));
                        serviceProviderDetails.setServiceTypeName(json.getString("cType"));
                        serviceProviderDetails.setLocation(json.getString("Location"));
                        serviceProviderDetails.setPassword(json.getString("Passwords"));
                        serviceProviderDetails.setImage_link("http://www.emedical.com/uploads/"+json.getString("Email_Id")+".jpg");




                        listDetails.add(serviceProviderDetails);
                    }

                    if (listDetails.size() <= 0) {
                        imgRepNotFound.setVisibility(View.VISIBLE);

                        final Animation animImgRecordNotFound = AnimationUtils.loadAnimation(getActivity(), R.anim.shake_interpolator);
                        imgRepNotFound.startAnimation(animImgRecordNotFound);

                        btnReportLoad.setVisibility(View.GONE);
                    }

                    if (listDetails.size() % 5 != 0) {
                        btnReportLoad.setVisibility(View.GONE);
                    }

                    _PageNo++;

                    adapter = new ServiceProviderReportCardAdapter(getContext(),listDetails);
                    recyclerView.setAdapter(adapter);
                }
            }

            catch (Exception e1) {
                Toast.makeText( getActivity().getApplicationContext(), "Something went wrong...", Toast.LENGTH_LONG).show();
            }
            finally {
                progressDialog.dismiss();
            }
        }
    }

//.......................

//*************  Adapter Class*************//

    public class ServiceProviderReportCardAdapter extends RecyclerView.Adapter<ServiceProviderReportCardAdapter.ViewHolder> {

        ServiceProviderReportCardAdapter serviceProviderReportCardAdapter = this;
        private Context context;
        private RetrieveFeedTask1 context1;

        List<ServiceProviderDetailsModel> listServiceProviderDetails;

        public ServiceProviderReportCardAdapter(Context context, List<ServiceProviderDetailsModel> listServiceProviderDetails) {
//            super();
            this.context = context;
            this.listServiceProviderDetails = listServiceProviderDetails;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.slider_1, parent, false);
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
                holder.vtxtEmail_Id.setText("Email : "+tr.getEmailId());
                holder.vtxtLocation.setText("Location : "+tr.getLocation());
                holder.vtxtCustomerNo.setText("Mobile No : "+tr.getCustomerNo());
                holder.vtxtFullName.setText("Name : "+tr.getFullName());
                holder.vtxtServiceType.setText("Type : "+tr.getServiceTypeName());
                Glide.with(context).load(tr.getImage_link()).into(holder.imageViews);

                switch (tr.getStatus()) {
                    case "Offline":
                        holder.vtxtStatus.setText("OFFLINE");
                        holder.vtxtStatus.setTextColor(getActivity().getApplicationContext().getResources().getColor(R.color.tx_FAILURE));
//                        linearLayoutTxCardItem.setBackgroundColor(getActivity().getApplicationContext().getResources().getColor(R.color.bg_FAILURE));
                        holder.cardViewTxCardItem.setCardBackgroundColor(Color.parseColor("#ffffff"));
                        break;
                    case "Online":
                        holder.vtxtStatus.setText("ONLINE");
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

            public TextView vtxtFullName;
            public TextView vtxtEmail_Id;
            public TextView vtxtLocation;
            public TextView vtxtCustomerNo;
            public TextView vtxtServiceType;
            public TextView vtxtStatus;
            public TextView vtxtViewDetails;
            public CardView cardViewTxCardItem;
            public ImageView imageViews;
            public String s,s1;



            public ViewHolder(View itemView) {
                super(itemView);

                final View view = itemView;
//                vtxtFullName = (TextView) itemView.findViewById(R.id.profile);
//                vtxtCustomerNo = (TextView) itemView.findViewById(R.id.profile);
                vtxtEmail_Id = (TextView) itemView.findViewById(R.id.email);
//                vtxtServiceType = (TextView) itemView.findViewById(R.id.profile);
                vtxtLocation = (TextView) itemView.findViewById(R.id.shop_name);
//                vtxtStatus = (TextView) itemView.findViewById(R.id.profile);
//                imageViews=(ImageView)itemView.findViewById(R.id.profile);
//                vtxtViewDetails = (TextView) itemView.findViewById(R.id.recharge_details);
//                cardViewTxCardItem = (CardView) itemView.findViewById(R.id.profile);

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        ServiceProviderDetailsModel tr = listServiceProviderDetails.get(getAdapterPosition());
                        s=tr.getEmailId();
                        s1=tr.getPassword();

                        _TransactionId = tr.ID;

                        String lStatus = "";
                        switch ( tr.getStatus()) {
                            case "Offline":
                                lStatus = "OFFLINE";
                                break;
                            case "Online":
                                lStatus = "ONLINE";
                                break;

                        }


                        android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(getActivity(),R.style.AppCompatAlertDialogStyle );
                        alertDialogBuilder.setTitle("Transaction Details : ");
                        alertDialogBuilder.setIcon(R.drawable.alert_dialog_warning);
                        alertDialogBuilder.setMessage(
                                        "Email Id : " + tr.getEmailId() +
                                        "\n\nMobile No. : " + tr.getCustomerNo() +
                                        "\n\nDelivery_Date : " + tr.getDeliveryDate() +
                                        "\n\nService Type : " + tr.getServiceTypeName() +
                                        "\n\nLocation : " + tr.getLocation() +
                                        "\n\nStatus : " + lStatus);





                        if(lStatus.equals("ONLINE")){

                        alertDialogBuilder.setPositiveButton("Purchase",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                       /* ServiceProviderSpinnerFragment ldf = new ServiceProviderSpinnerFragment ();
                                        Bundle args = new Bundle();
                                        args.putString("YourKey", s);
                                        args.putString("YourKey1", s1);
                                        ldf.setArguments(args);
                                        getFragmentManager().beginTransaction().replace(R.id.containerView, ldf).commit();*/
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
