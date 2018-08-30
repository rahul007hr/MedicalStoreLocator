package com.medicalstorefinder.medicalstoreslocatorss.Fragments;


import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.medicalstorefinder.medicalstoreslocatorss.Constants.Constants;
import com.medicalstorefinder.medicalstoreslocatorss.Constants.SharedPreference;
import com.medicalstorefinder.medicalstoreslocatorss.Constants.Utilities;
import com.medicalstorefinder.medicalstoreslocatorss.GlideImageLoader;
import com.medicalstorefinder.medicalstoreslocatorss.Models.ServiceProviderDetailsModel;
import com.medicalstorefinder.medicalstoreslocatorss.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.medicalstorefinder.medicalstoreslocatorss.Constants.Constants.NO_AVATAR_IMAGE_PATH;


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
                        ServiceProviderDetailsModel serviceProviderDetails1 = new ServiceProviderDetailsModel();
                        JSONObject jsonObject = jsonarray.getJSONObject(i);
//           {"userid":"28","mainorderid":"ORD000011","cost":null,
// "medicalreply":null,"customerconfirm":"0","customeconfirmdatetime":"0000-00-00 00:00:00",
// "medicalconfirm":"1","orderid":"13","latitude":"18.5894527","longitude":"73.822031","description":"ddf",
// "imagepath":"http:\/\/www.allegoryweb.com\/emedical\/\/images\/28\/1534096979745.jpg",
// "address":"Yashoda Puram,Gokul Nagari, Pimple Gurav, Gokul Nagari, Pimple Gurav, Pimpri-Chinchwad, Maharashtra 411027, India",
// "mobile":"8793377995","ordstatus":"Pending","created_at":"2018-08-12 11:04:23"}
                        String listOfOrderIds = sharedPreference.getValue( getContext(), Constants.PREF_IS_USER, Constants.PREF_KEY_ORDER_ID_LIST);

                        if(!listOfOrderIds.contains((jsonObject.getString("orderid")).toString())
                                &&( jsonObject.getString("cost").equalsIgnoreCase("")
                                || jsonObject.getString("cost")==null || jsonObject.getString("cost").equalsIgnoreCase("null"))) {

                            serviceProviderDetails1.setOrderMainId(jsonObject.getString("mainorderid"));
                            serviceProviderDetails1.setMedicalCost(jsonObject.getString("cost"));
//                            serviceProviderDetails1.set(jsonObject.getString("customerconfirm"));
//                            serviceProviderDetails1.setMedicalCost(jsonObject.getString("medicalconfirm"));

                            serviceProviderDetails1.setOrderid(jsonObject.getString("orderid"));
                            serviceProviderDetails1.setDescription(jsonObject.getString("description"));
                            serviceProviderDetails1.setImagepath(jsonObject.getString("imagepath"));
                            serviceProviderDetails1.setOrderstatus(jsonObject.getString("ordstatus"));
                            serviceProviderDetails1.setAddress(jsonObject.getString("address"));


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

                            listDetails.add(serviceProviderDetails1);
                        }
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
//                    Glide.with(context).load(tr.getImagepath()).into(holder.imageViews);
//                    holder.imageViews.setImageResource(android.R.color.transparent);

                    RequestOptions options = new RequestOptions()
                            .centerCrop()
                            .placeholder(R.drawable.profile_pic)
//                            .error(R.drawable.ic_pic_error)
                            .priority(Priority.HIGH);

                    new GlideImageLoader(holder.imageViews,
                            holder.spinner).load(tr.getImagepath(),options);

                }else{
//                    Glide.with(context).load(NO_AVATAR_IMAGE_PATH+tr.getImagepath()).into(holder.imageViews);
//                    holder.imageViews.setImageResource(android.R.color.transparent);


                    RequestOptions options = new RequestOptions()
                            .centerCrop()
                            .placeholder(R.drawable.profile_pic)
//                            .error(R.drawable.ic_pic_error)
                            .priority(Priority.HIGH);

                    new GlideImageLoader(holder.imageViews,
                            holder.spinner).load(NO_AVATAR_IMAGE_PATH+tr.getImagepath(),options);

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
            public ProgressBar spinner;
//            public String s,s1;



            public ViewHolder(View itemView) {
                super(itemView);

                final View view = itemView;
                vtxtLocation = (TextView) itemView.findViewById(R.id.location);
                vtxtStatus = (TextView) itemView.findViewById(R.id.status);
                imageViews=(ImageView)itemView.findViewById(R.id.image_View);
//                vtxtViewDetails = (TextView) itemView.findViewById(R.id.recharge_details);
                cardViewTxCardItem = (CardView) itemView.findViewById(R.id.cardview_tx_card_item);
                spinner = (ProgressBar)itemView.findViewById(R.id.progressBar1);

                imageViews.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getContext(),"hi",Toast.LENGTH_LONG).show();
                    }
                });

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
                                         "Order Id : " + tr.getOrderMainId() +
                                        "\n\nDescription : " + tr.getDescription() +
                                        "\n\nStatus : " + tr.getOrderstatus()+
                                        "\n\nDownload Prescription : " );

                        LinearLayout lv = new LinearLayout(getActivity());
                        LinearLayout.LayoutParams vp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
                        lv.setLayoutParams(vp);
                        ImageView image = new ImageView(getActivity());
//                        LinearLayout.LayoutParams vp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
                        image.setLayoutParams(vp);
                        image.setMaxHeight(10);
                        image.setMaxWidth(10);

                        // other image settings
                        image.setImageDrawable(getResources().getDrawable(R.drawable.alert_dialog_confirm));
                        lv.addView(image);

                        alertDialogBuilder.setView(lv);

                        image.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

//                                Toast.makeText(getActivity(), "hi", Toast.LENGTH_SHORT).show();
                               /* File direct = new File(Environment.getExternalStorageDirectory()
                                        + "/AnhsirkDasarp");

                                if (!direct.exists()) {
                                    direct.mkdirs();
                                }

                                DownloadManager mgr = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);

                                Uri downloadUri = Uri.parse(tr.getImagepath());
                                DownloadManager.Request request = new DownloadManager.Request(
                                        downloadUri);

                                request.setAllowedNetworkTypes(
                                        DownloadManager.Request.NETWORK_WIFI
                                                | DownloadManager.Request.NETWORK_MOBILE)
                                        .setAllowedOverRoaming(false).setTitle("Demo")
                                        .setDescription("Something useful. No, really.")
                                        .setDestinationInExternalPublicDir("/AnhsirkDasarp", "fileName.jpg");

                                mgr.enqueue(request);*/

                               String[] s = {tr.getImagepath(),tr.getOrderid()};

                                new DownloadImage().execute(s);



                            }
                        });

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
                                            serviceProviderDetails.setOrderid(tr.getOrderid());
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

    class DownloadImage extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... urls) {
            Utilities utilities = new Utilities(getContext());

            String s1,s2;

            s1=urls[0];
            s2=urls[1];

            return utilities.downloadImagesToSdCard(s1,s2);
        }

        protected void onPostExecute(String response) {
            progressDialog.dismiss();
            viewimage(response);

        }
    }
    public void viewimage(String fileName)
    {
//        String path = serialnumber+".png";
        File mypath =null;
        String selectedOutputPath = "";

        File mediaStorageDir = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "e-Medicine");
        // Create a storage directory if it does not exist

        // Create a media file name
        selectedOutputPath = mediaStorageDir.getPath() + File.separator + fileName;
        Log.d("PhotoEditorSDK", "selected camera path " + selectedOutputPath);
        mypath = new File(selectedOutputPath);


        Bitmap b;
        //            b = BitmapFactory.decodeStream(new FileInputStream(mypath));


        Intent intent = new Intent(Intent.ACTION_VIEW);

        Uri apkURI = FileProvider.getUriForFile(
                getContext(),
                getContext().getApplicationContext()
                        .getPackageName() + ".provider", mypath);
        intent.setDataAndType(apkURI, "image/");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        startActivity(intent);

//            _context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(String.valueOf(mypath))));
    }


    class SendCostToCustomer extends AsyncTask<Void, Void, String> {

        protected void onPreExecute() {
//            progressDialog.show();
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
                        Toast.makeText(getContext(), jsonObject2.getString("result"), Toast.LENGTH_LONG).show();

//                        JSONObject jsonObject = new JSONObject(jsonObject2.getString("result"));
//						JSONObject jsonObject = jsonarray.getJSONObject(1);

                        String listOfOrderIds = sharedPreference.getValue( getContext(), Constants.PREF_IS_USER, Constants.PREF_KEY_ORDER_ID_LIST);

                        if(listOfOrderIds.equalsIgnoreCase("")) {
                            listOfOrderIds = serviceProviderDetails.getOrderid();
                        }else{
                            listOfOrderIds += ","+serviceProviderDetails.getOrderid();
                        }
                        sharedPreference.putValue(getActivity(), Constants.PREF_SERVICE_PROVIDER_IDS, Constants.PREF_KEY_ORDER_ID_LIST,listOfOrderIds);

                        new RetrieveFeedTask1().execute();
                    }
                }

            } catch (Exception e) {
                Toast.makeText( getContext(), "Please try again later...", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
            finally {
//                progressDialog.dismiss();
            }

        }
    }



    }
