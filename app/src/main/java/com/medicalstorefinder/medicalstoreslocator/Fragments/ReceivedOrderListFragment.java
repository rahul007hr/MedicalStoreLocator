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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
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

import static com.medicalstorefinder.medicalstoreslocator.Constants.Constants.NO_AVATAR_IMAGE_PATH;


/**
 * A simple {@link Fragment} subclass.
 */
public class ReceivedOrderListFragment extends Fragment  {

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

            String address = Constants.API_RECEIVED_ORDER_STATUS;
            Map<String, String> params = new HashMap<>();
            params.put("userid", sharedPreference.getValue( getActivity(), Constants.PREF_IS_USER, Constants.PREF_KEY_USER_ID ));
//            params.put("userid", "21");
            params.put("status", "pending");

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
//                        JSONObject jsonObject = new JSONObject(jsonObject2.getString("result"));
//                        serviceProviderDetails.setID(json.getInt("Id"));
//                        jsonObject.getString("firstname")

//                        {"orderid":"11","latitude":"12.1516516541","longitude":"15.65656565","description":"test order","imagepath":"http:\/\/googel.com","address":"NAshik","mobile":"7412589630","ordstatus":"Pending","created_at":"2018-08-09 09:12:25"}

                        serviceProviderDetails.setOrderid(jsonObject.getString("orderid"));
                        serviceProviderDetails.setDescription(jsonObject.getString("description"));
                        serviceProviderDetails.setImagepath(jsonObject.getString("imagepath"));
                        serviceProviderDetails.setOrderstatus(jsonObject.getString("ordstatus"));
                        serviceProviderDetails.setAddress(jsonObject.getString("address"));


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
        private ReceivedOrderListFragment.RetrieveFeedTask1 context1;

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

                if(!tr.getImagepath().equalsIgnoreCase("")&& tr.getImagepath()!=null && !tr.getImagepath().equalsIgnoreCase("no_avatar.jpg")) {
                    Glide.with(context).load(tr.getImagepath()).into(holder.imageViews);
                    holder.imageViews.setImageResource(android.R.color.transparent);
                }else{
                    Glide.with(context).load(NO_AVATAR_IMAGE_PATH+tr.getImagepath()).into(holder.imageViews);
                }

                tr.setStatus("pending");
                switch (tr.getStatus()) {
                    case "pending":
                        holder.vtxtStatus.setText("PENDING");
                        holder.vtxtStatus.setTextColor(getActivity().getApplicationContext().getResources().getColor(R.color.tx_FAILURE));
//                        linearLayoutTxCardItem.setBackgroundColor(getActivity().getApplicationContext().getResources().getColor(R.color.bg_FAILURE));
                        holder.cardViewTxCardItem.setCardBackgroundColor(Color.parseColor("#ffffff"));
                        break;
                    case "success":
                        holder.vtxtStatus.setText("SUCCESS");
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
//            public String s,s1;



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

                        final ServiceProviderDetailsModel tr = listServiceProviderDetails.get(getAdapterPosition());
                       /* s=tr.getEmailId();
                        s1=tr.getPassword();*/

//                        _TransactionId = tr.ID;

                        String lStatus = "pending";
                        switch ( tr.getOrderstatus()) {
                            case "Pending":
                                lStatus = "PENDING";
                                break;
                            case "success":
                                lStatus = "SUCCESS";
                                break;

                        }

                        final android.support.v7.app.AlertDialog.Builder alertDialogBuilder1 = new android.support.v7.app.AlertDialog.Builder(getActivity(),R.style.AppCompatAlertDialogStyle );

                        android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(getActivity(),R.style.AppCompatAlertDialogStyle );
                        alertDialogBuilder.setTitle("Transaction Details : ");
                        alertDialogBuilder.setIcon(R.drawable.alert_dialog_warning);
                        alertDialogBuilder.setMessage(
                                         "Order Id : " + tr.getOrderid() +
                                        "\n\nDescription : " + tr.getDescription() +
                                        "\n\nStatus : " + tr.getOrderstatus());


                        if(lStatus.equalsIgnoreCase("pending")){

                            alertDialogBuilder.setPositiveButton("Accept",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {

                                            alertDialogBuilder1.show();

//                                            PostOrderFragment ldf = new PostOrderFragment ();
//                                            Bundle args = new Bundle();
//                                            args.putString("YourKey", s);
//                                            args.putString("YourKey1", s1);
//                                            ldf.setArguments(args);
//                                            getFragmentManager().beginTransaction().replace(R.id.containerView, ldf).commit();
                                        }
                                    });
                        }
                        alertDialogBuilder.show();




                        LayoutInflater li = LayoutInflater.from(context);
                        View promptsView = li.inflate(R.layout.custom_view_for_alert_dialogue, null);


                        alertDialogBuilder1.setTitle("Transaction Details : ");
                        alertDialogBuilder1.setIcon(R.drawable.alert_dialog_warning);
                       /* alertDialogBuilder1.setMessage(
                                "Order Id1 : " + tr.getOrderid() +
                                        "\n\nDescription1 : " + tr.getDescription() +
                                        "\n\nStatus1 : " + tr.getOrderstatus());*/
                        alertDialogBuilder1.setView(promptsView);

                        final EditText userInput = (EditText) promptsView
                                .findViewById(R.id.editTextDialogUserInput);

                        final EditText userCost = (EditText) promptsView
                                .findViewById(R.id.editTextCost);

                        if(lStatus.equalsIgnoreCase("pending")){

                            alertDialogBuilder1.setPositiveButton("Send",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {

//                                            Toast.makeText(getContext(),userInput.getText()+"::"+userCost.getText(),Toast.LENGTH_LONG).show();

                                            desc[0] = String.valueOf(userInput.getText());
                                            cost[0] = String.valueOf(userCost.getText());

                                            new SendCostToCustomer().execute();

//                                            PostOrderFragment ldf = new PostOrderFragment ();
//                                            Bundle args = new Bundle();
//                                            args.putString("YourKey", s);
//                                            args.putString("YourKey1", s1);
//                                            ldf.setArguments(args);
//                                            getFragmentManager().beginTransaction().replace(R.id.containerView, ldf).commit();
                                        }
                                    });
                        }






                    }

                });

            }
        }
    }

    class SendCostToCustomer extends AsyncTask<Void, Void, String> {

        protected void onPreExecute() {
            progressDialog.show();
        }

        protected String doInBackground(Void... urls) {

            Utilities utilities = new Utilities(getContext());

            String address = Constants.API_MEDICAL_RECEIVED_ORDER;
            Map<String, String> params = new HashMap<>();
            params.put("medicalid", sharedPreference.getValue( getContext(), Constants.PREF_IS_USER, Constants.PREF_KEY_USER_ID));
            params.put("orderid", serviceProviderDetails.getOrderid());
            params.put("description", desc[0]!=null?desc[0]:"");
            params.put("cost", cost[0]!=null?cost[0]:"");

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

                        JSONObject jsonObject = new JSONObject(jsonObject2.getString("result"));
//						JSONObject jsonObject = jsonarray.getJSONObject(1);


                    }
                }

            } catch (Exception e) {
                Toast.makeText( getContext(), "Something went wrong...", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
            finally {
                progressDialog.dismiss();
            }

        }
    }

}
