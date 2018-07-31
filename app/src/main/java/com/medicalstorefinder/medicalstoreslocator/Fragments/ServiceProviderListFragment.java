package com.medicalstorefinder.medicalstoreslocator.Fragments;


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
import com.medicalstorefinder.medicalstoreslocator.Constants.Constants;
import com.medicalstorefinder.medicalstoreslocator.Constants.SharedPreference;
import com.medicalstorefinder.medicalstoreslocator.Constants.Utilities;
import com.medicalstorefinder.medicalstoreslocator.Models.ServiceProviderDetailsModel;
import com.medicalstorefinder.medicalstoreslocator.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.medicalstorefinder.medicalstoreslocator.Constants.Constants.DOMAIN_NAME;


/**
 * A simple {@link Fragment} subclass.
 */
public class ServiceProviderListFragment extends Fragment  {

    int _PageNo = 1;

    ProgressDialog progressDialog;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;

//    private Button btnReportLoad;
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

        View v = inflater.inflate(R.layout.fragment_service_provider_list,null);

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

        new RetrieveFeedTask1().execute();

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

            String address = Constants.API_GET_NEARBY;
            Map<String, String> params = new HashMap<>();
            params.put("latitude", "18.6079083");
            params.put("longitude", "73.821156");
            params.put("radius", "1");
            params.put("userid", "6");

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





                    JSONArray jsonarray = new JSONArray(jsonObject2.getString("result"));
                    if (jsonarray.length() <= 0) {
//                        btnReportLoad.setVisibility(View.GONE);
                        Toast.makeText(getActivity().getBaseContext(), "No more record found.", Toast.LENGTH_SHORT).show();
                    }


                    for (int i = 0; i < jsonarray.length(); i++) {
                        ServiceProviderDetailsModel serviceProviderDetails = new ServiceProviderDetailsModel();
                        JSONObject jsonObject = jsonarray.getJSONObject(i);
//                        JSONObject jsonObject = new JSONObject(jsonObject2.getString("result"));
//                        serviceProviderDetails.setID(json.getInt("Id"));
//                        jsonObject.getString("firstname")
                        serviceProviderDetails.setStatus(jsonObject.getString("loginstatus"));
                        serviceProviderDetails.setLocation(jsonObject.getString("distance"));
                        serviceProviderDetails.setImage_link(DOMAIN_NAME+jsonObject.getString("profilepic"));


                       /* serviceProviderDetails.setID(i);
                        serviceProviderDetails.setEmailId(("Email_Id"));
                        serviceProviderDetails.setCustomerNo(("Mobile_Number"));
                        serviceProviderDetails.setFullName(("Full_Name"));

                        serviceProviderDetails.setStatus(("cStatus"));
                        serviceProviderDetails.setServiceTypeName(("cType"));
                        serviceProviderDetails.setLocation(("Location"));
                        serviceProviderDetails.setPassword(("Passwords"));
                        serviceProviderDetails.setImage_link("https://thumbs.dreamstime.com/z/smile-emoticons-thumbs-up-isolated-60753634.jpg");
*/

                        listDetails.add(serviceProviderDetails);
                    }

                    if (listDetails.size() <= 0) {
                        imgRepNotFound.setVisibility(View.VISIBLE);

                        final Animation animImgRecordNotFound = AnimationUtils.loadAnimation(getActivity(), R.anim.shake_interpolator);
                        imgRepNotFound.startAnimation(animImgRecordNotFound);

//                        btnReportLoad.setVisibility(View.GONE);
                    }

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

//*************  Adapter Class*************//

    public class ServiceProviderReportCardAdapter extends RecyclerView.Adapter<ServiceProviderReportCardAdapter.ViewHolder> {

        ServiceProviderReportCardAdapter serviceProviderReportCardAdapter = this;
        private Context context;
        private ServiceProviderListFragment.RetrieveFeedTask1 context1;

        List<ServiceProviderDetailsModel> listServiceProviderDetails;

        public ServiceProviderReportCardAdapter(Context context, List<ServiceProviderDetailsModel> listServiceProviderDetails) {
//            super();
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

//            linearLayoutTxCardItem = (LinearLayout) v.findViewById(R.id.linear_layout_tx_card_item);
            return viewHolder;
        }


        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            try {

                ServiceProviderDetailsModel tr = listServiceProviderDetails.get(position);
                holder.vtxtLocation.setText("Location : "+tr.getLocation());
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

            public TextView vtxtLocation;
            public TextView vtxtStatus;
            public CardView cardViewTxCardItem;
            public ImageView imageViews;
            public String s,s1;



            public ViewHolder(View itemView) {
                super(itemView);

                final View view = itemView;
                vtxtLocation = (TextView) itemView.findViewById(R.id.location);
                vtxtStatus = (TextView) itemView.findViewById(R.id.status);
                imageViews=(ImageView)itemView.findViewById(R.id.image_View);
//                vtxtViewDetails = (TextView) itemView.findViewById(R.id.recharge_details);
                cardViewTxCardItem = (CardView) itemView.findViewById(R.id.cardview_tx_card_item);

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

                           /* alertDialogBuilder.setPositiveButton("Purchase",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            ServiceProviderSpinnerFragment ldf = new ServiceProviderSpinnerFragment ();
                                            Bundle args = new Bundle();
                                            args.putString("YourKey", s);
                                            args.putString("YourKey1", s1);
                                            ldf.setArguments(args);
                                            getFragmentManager().beginTransaction().replace(R.id.containerView, ldf).commit();
                                        }
                                    });*/
                        }
                        alertDialogBuilder.show();
                    }

                });

            }
        }
    }



}
