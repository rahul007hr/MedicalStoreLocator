package com.medicalstorefinder.mychemists.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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


import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.medicalstorefinder.mychemists.Constants.Constants;
import com.medicalstorefinder.mychemists.Constants.SharedPreference;
import com.medicalstorefinder.mychemists.Constants.Utilities;
import com.medicalstorefinder.mychemists.Constants.Utility;
import com.medicalstorefinder.mychemists.Fragments.AllNotificationsFragment;
import com.medicalstorefinder.mychemists.Fragments.ChooseOrderTypeFragment;
import com.medicalstorefinder.mychemists.Fragments.ReceivedOrderListFragment;
import com.medicalstorefinder.mychemists.GlideImageLoader;
import com.medicalstorefinder.mychemists.Models.ServiceProviderDetailsModel;
import com.medicalstorefinder.mychemists.R;
import com.medicalstorefinder.mychemists.SingleTouchImageViewFragment;

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

public class FirebaseMainActivity extends AppCompatActivity {
    ProgressDialog progressDialog;
    private TextView tvNotificationDetails;
    List<ServiceProviderDetailsModel> listDetails = new ArrayList<ServiceProviderDetailsModel>();
    private RecyclerView.Adapter adapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    String str;
    final String[] desc = new String[1];
    final String[] cost = new String[1];
    String orderId,customerId,medicalId;
    String strNotification;
    SharedPreference sharedPreference = new SharedPreference();
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_main);
        initControls();

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mToolbar.setTitle(getString(R.string.app_name));
            mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);

            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    /*AllNotificationsFragment ldf1 = new AllNotificationsFragment();

                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction xfragmentTransaction = fragmentManager.beginTransaction();
                    xfragmentTransaction.replace(R.id.containerView, ldf1).commit();*/



                    finish();
                }
            });
//        }



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

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mToolbar = (Toolbar) findViewById(R.id.toolbar);
//        }

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
        strNotification=String.valueOf(text);;
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
                    RequestOptions options = new RequestOptions()
                            .centerCrop()
                            .placeholder(R.drawable.profile_pic)
//                            .error(R.drawable.ic_pic_error)
                            .priority(Priority.HIGH);

                    new GlideImageLoader(holder.imageViews,
                            holder.spinner).load(NO_AVATAR_IMAGE_PATH+"no_avatar.jpg",options);
                }
//                    Glide.with(context).load(NO_AVATAR_IMAGE_PATH+tr.getMedicalProfileUrl()).into(holder.imageViews);


                tr.setStatus("Pending");
                switch (tr.getStatus()) {
                    case "Pending":
                        holder.vtxtStatus.setText("PENDING");
                        holder.vtxtStatus.setTextColor(getApplicationContext().getResources().getColor(R.color.primary_dark));
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

                imageViews.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        final ServiceProviderDetailsModel tr = listServiceProviderDetails.get(getAdapterPosition());

//                        Toast.makeText(getContext(),"hello",Toast.LENGTH_LONG).show();

                        if(tr.getImagepath()!=null && !tr.getImagepath().equalsIgnoreCase("") && !tr.getImagepath().equalsIgnoreCase
                                ("null")&& !tr.getImagepath().equalsIgnoreCase("no_avatar.jpg")) {
                            /*SingleTouchImageViewFragment ldf1 = new SingleTouchImageViewFragment();
                            Bundle args1 = new Bundle();
                            args1.putString("position1", String.valueOf(tr.getImagepath()));

                            ldf1.setArguments(args1);
                            FragmentManager fragmentManager = getSupportFragmentManager();
                            FragmentTransaction xfragmentTransaction = fragmentManager.beginTransaction();
                            xfragmentTransaction.replace(R.id.containerView1, ldf1).commit();*/
                        }else{
                            Toast.makeText( getBaseContext(), "Image Not Available", Toast.LENGTH_LONG).show();
                        }
                    }
                });


                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final ServiceProviderDetailsModel tr = listServiceProviderDetails.get(getAdapterPosition());
                        String addrs="";
                        if(tr.getImagepath()!=null && !tr.getImagepath().equalsIgnoreCase("") && !tr.getImagepath().equalsIgnoreCase
                                ("null")&& !tr.getImagepath().equalsIgnoreCase("no_avatar.jpg")) {
                            addrs="\n"+Html.fromHtml(getString(R.string.download));
                        }else{
                            addrs="";
                        }

                        String dstnc="";
                        if(tr.getKm()!=null && !tr.getKm().equalsIgnoreCase("") && !tr.getKm().equalsIgnoreCase("null")) {
                            dstnc="\n"+Html.fromHtml(getString(R.string.distances)) + tr.getKm() + " KM";
                        }else{
                            dstnc="";
                        }

                        String mdclcst="";
                        if(tr.getMedicalCost()!=null && !tr.getMedicalCost().equalsIgnoreCase("") && !tr.getMedicalCost().equalsIgnoreCase("null")&& !tr.getMedicalCost().equalsIgnoreCase("-")) {
                            mdclcst="\n"+Html.fromHtml(getString(R.string.medicalcost)) + tr.getMedicalCost() ;
                        }else{
                            mdclcst="";
                        }

                        String mdclrply="";
                        if(tr.getMedicalReply()!=null && !tr.getMedicalReply().equalsIgnoreCase("") && !tr.getMedicalReply().equalsIgnoreCase("null")&& !tr.getMedicalReply().equalsIgnoreCase("-")) {
                            mdclrply="\n"+Html.fromHtml(getString(R.string.medicaldescription)) + tr.getMedicalReply() ;
                        }else{
                            mdclrply="";
                        }

                        String cstmrDescription="";
                        if(tr.getDescription()!=null && !tr.getDescription().equalsIgnoreCase("") && !tr.getDescription().equalsIgnoreCase("null")&& !tr.getDescription().equalsIgnoreCase("-")) {
                            cstmrDescription="\n"+Html.fromHtml(getString(R.string.description)) + tr.getDescription() ;
                        }else{
                            cstmrDescription="";
                        }
                        String lStatus = "Pending";

                        final android.support.v7.app.AlertDialog.Builder alertDialogBuilder1 = new android.support.v7.app.AlertDialog.Builder(FirebaseMainActivity.this,R.style.AppCompatAlertDialogStyle );
                        final android.support.v7.app.AlertDialog.Builder alertDialogBuilder2 = new android.support.v7.app.AlertDialog.Builder(FirebaseMainActivity.this,R.style.AppCompatAlertDialogStyle );
                        android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(FirebaseMainActivity.this,R.style.AppCompatAlertDialogStyle );
                        alertDialogBuilder.setTitle(Html.fromHtml(getString(R.string.transactions)));
                        alertDialogBuilder.setMessage(
                        Html.fromHtml(getString(R.string.orderid)) + tr.getOrderid() +
                                cstmrDescription+
                                mdclcst +
                                mdclrply +
                                dstnc +
                                addrs );



                        //...............................

                        String description = (tr.getDescription()!=null &&
                                !tr.getDescription().equalsIgnoreCase("null"))?tr.getDescription():"-";

                        String medicalCost = (tr.getMedicalCost()!=null &&
                                !tr.getMedicalCost().equalsIgnoreCase("null") && !tr.getMedicalCost().equalsIgnoreCase(""))?tr.getMedicalCost():"-";

                        String medicalDescription = (tr.getMedicalReply()!=null &&
                                !tr.getMedicalReply().equalsIgnoreCase("null") && !tr.getMedicalReply().equalsIgnoreCase(""))?tr.getMedicalReply():"-";

//                        final android.support.v7.app.AlertDialog.Builder alertDialogBuilder1 = new android.support.v7.app.AlertDialog.Builder(getBaseContext(),R.style.AppCompatAlertDialogStyle );

//                        android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(getBaseContext(),R.style.AppCompatAlertDialogStyle );
                       /* alertDialogBuilder.setTitle(Html.fromHtml(getString(R.string.transactions)));
                        alertDialogBuilder.setMessage(
                                Html.fromHtml(getString(R.string.orderid))+ tr.getOrderid() +
                                        "\n"+Html.fromHtml(getString(R.string.description)) + description+
                                        "\n"+Html.fromHtml(getString(R.string.medicalcost)) + medicalCost +
                                        "\n"+Html.fromHtml(getString(R.string.medicaldescription)) +medicalDescription );*/

                        if(strNotification.contains("received") && (medicalCost==null || medicalCost.equalsIgnoreCase("null")
                                || medicalCost.equalsIgnoreCase("") || medicalCost.equalsIgnoreCase("-"))){



                            alertDialogBuilder.setPositiveButton("Accept",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {

                                            alertDialogBuilder2.show();
                                        }
                                    });

                        }else if(strNotification.contains("response") && (medicalCost!=null && !medicalCost.equalsIgnoreCase("null")
                                && !medicalCost.equalsIgnoreCase("") && !medicalCost.equalsIgnoreCase("-"))){
                       /* if (sharedPreference.getValue( getActivity(), Constants.PREF_USER_ROLE, Constants.PREF_USER_ROLE ).equalsIgnoreCase("customer")){

                        }else{*/
                            alertDialogBuilder.setPositiveButton("Accept",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {

                                            if (sharedPreference.getValue( getBaseContext(), Constants.PREF_USER_ROLE, Constants.PREF_USER_ROLE ).equalsIgnoreCase("customer")){
                                                new SendCostConfirmation().execute();
                                            }else{
                                                new SendFinalConfirmationFromMedicalToCustomer().execute();
                                            }
                                        }
                                    });
                        }
//                        }

                        if(tr.getImagepath()!=null && !tr.getImagepath().equalsIgnoreCase("") && !tr.getImagepath().equalsIgnoreCase("null")&& !tr.getImagepath().equalsIgnoreCase("no_avatar.jpg")) {
                            LinearLayout lv = new LinearLayout(getBaseContext());
                            LinearLayout.LayoutParams vp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
                            lv.setLayoutParams(vp);
//                        lv.setPadding(0,-40,0,0);
                            ImageView image = new ImageView(getBaseContext());
//                        LinearLayout.LayoutParams vp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
                            image.setLayoutParams(vp);
                            image.setMaxHeight(10);
                            image.setMaxWidth(10);
//                        image.setPadding(0,-30,0,0);
                            // other image settings
                            image.setImageDrawable(getResources().getDrawable(R.drawable.down));
                            lv.addView(image);

                            alertDialogBuilder.setView(lv);

                            image.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    String[] s = {tr.getImagepath(), tr.getOrderid()};
                                    if (tr.getImagepath() != null && !tr.getImagepath().equalsIgnoreCase("") && !tr.getImagepath().equalsIgnoreCase("null")) {
                                        boolean result = Utility.checkPermission(getBaseContext());
                                        if (result)
                                            new DownloadImage().execute(s);
                                    } else {
                                        Toast.makeText(getBaseContext(), "Image Not Available", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }

                        alertDialogBuilder.show();

                        if(strNotification.contains("received")){

                            LayoutInflater li = LayoutInflater.from(getBaseContext());
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

//                                            Toast.makeText(getContext(),userInput.getText()+"::"+userCost.getText(),Toast.LENGTH_LONG).show();

                                            desc[0] = String.valueOf(userInput.getText());
                                            cost[0] = String.valueOf(userCost.getText());
//                                            serviceProviderDetails.setOrderid(tr.getOrderid());


                                            /*if((desc[0].equalsIgnoreCase("null") || desc[0].equalsIgnoreCase(""))&& (cost[0].equalsIgnoreCase("null") || cost[0].equalsIgnoreCase("") || cost[0].equalsIgnoreCase("0.00")) ){
                                                Toast.makeText(getBaseContext(),"Please Enter Cost and Description",Toast.LENGTH_LONG).show();
                                            }else  if((desc[0].equalsIgnoreCase("null") || desc[0].equalsIgnoreCase("")) ){
                                                Toast.makeText(getBaseContext(),"Please Enter Description",Toast.LENGTH_LONG).show();
                                            }else*/  if((cost[0].equalsIgnoreCase("null") || cost[0].equalsIgnoreCase("") || cost[0].equalsIgnoreCase("0.00")) ){
                                                Toast.makeText(getBaseContext(),"Please Enter Cost",Toast.LENGTH_LONG).show();
                                            }else{
                                                new SendCostToCustomer().execute();
                                            }



                                        }
                                    });
                        }


                        //..............................






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
            Utilities utilities = new Utilities(getBaseContext());

            String s1,s2;

            s1=urls[0];
            s2=urls[1];

            return utilities.downloadImagesToSdCard(s1,s2);
        }

        protected void onPostExecute(File response) {
            progressDialog.dismiss();
            boolean result= Utility.checkPermission(getBaseContext());
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
        selectedOutputPath = fileName!=null ? fileName.getPath():"";
        Log.d("PhotoEditorSDK", "selected camera path " + selectedOutputPath);
        mypath = new File(selectedOutputPath);


        Bitmap b;
        //            b = BitmapFactory.decodeStream(new FileInputStream(mypath));

        /*Installation failed with message Failed to finalize session : INSTALL_FAILED_CONFLICTING_PROVIDER: Package couldn't be installed in /data/app/com.medicalstorefinder.mychemists-1: Can't install because provider name com.zoftino.android.fileprovider (in package com.medicalstorefinder.mychemists) is already used by com.medicalstorefinder.medicalstoreslocatorss.
            It is possible that this issue is resolved by uninstalling an existing version of the apk if it is present, and then re-installing.

            WARNING: Uninstalling will remove the application data!

            Do you want to uninstall the existing application?*/
        Intent intent = new Intent(Intent.ACTION_VIEW);

        Uri apkURI = FileProvider.getUriForFile(
                getBaseContext(),
                "com.zoftino.android.fileproviders", mypath);
        intent.setDataAndType(apkURI, "image/");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        startActivity(intent);

//            _context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(String.valueOf(mypath))));
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
//            SharedPreference sharedPreference = new SharedPreference();
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

//                        {"userid":"66","mainorderid":"ORD000061","cost":null,"medicalreply":null,"customerconfirm":"0","customeconfirmdatetime":"0000-00-00 00:00:00","medicalconfirm":"0","orderid":"61","latitude":"18.5889719","longitude":"73.8217155","description":"aaa","imagepath":"https:\/\/www.mychemist.net.in\/admin\/images\\\/65\/1540142957.jpg","address":"Dolly Enterprises,Sainath Nagar, Gokul Nagari, Pimple Gurav, Pimpri-Chinchwad, Maharashtra 411027, India","mobile":"9923651772","ordstatus":"Pending","created_at":"2018-11-25 13:05:13"}

                        String userId = sharedPreference.getValue( getBaseContext(), Constants.PREF_IS_USER, Constants.PREF_KEY_USER_ID );

                        JSONObject jsonObject = new JSONObject(jsonObject2.getString("result"));

                        //                            2018-09-22 03:48:19 remains
                        String s = jsonObject.getString("created_at");
//                        String s ="2018-09-22 03:48:19";
                        String[] s1 = s.split("\\s+");
                        String s2 = s1[0];

                        Date initDate = new SimpleDateFormat("yyyy-MM-dd").parse(s2);
                        SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
                        Date initTime = new SimpleDateFormat("hh:mm:ss").parse(s1[1]);
                        SimpleDateFormat timeFormatter = new SimpleDateFormat("hh:mm a");

                        String parsedDate = formatter.format(initDate) + " "+ timeFormatter.format(initTime);
                        parsedDate=parsedDate.toUpperCase();
                            if (listDetails.size() > 0) {
                                listDetails.clear();
                            }

                            ServiceProviderDetailsModel serviceProviderDetails1 = new ServiceProviderDetailsModel();

                            serviceProviderDetails1.setMedicalReply(jsonObject.getString("medicalreply").equalsIgnoreCase("null")?"":jsonObject.getString("medicalreply"));
                            serviceProviderDetails1.setOrderid(jsonObject.getString("mainorderid"));
                            serviceProviderDetails1.setDescription(jsonObject.getString("description").equalsIgnoreCase("null")?"":jsonObject.getString("description"));
                            serviceProviderDetails1.setCustomerId(jsonObject.getString("userid"));


                        /*String km =(jsonObject.getString("km"));
                        if(km==null || km.equalsIgnoreCase("null")||km.equalsIgnoreCase("")){
                            km="0.00";
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
                        }else{
                            DecimalFormat roundup = new DecimalFormat("#.##");
                            serviceProviderDetails1.setKm(Double.valueOf(roundup.format(Double.parseDouble(km))).toString());
                        }*/


//...............................
                        String medical = "";

                        if (sharedPreference.getValue( getBaseContext(), Constants.PREF_USER_ROLE, Constants.PREF_USER_ROLE ).equalsIgnoreCase("customer"))
                            medical = jsonObject.getString("medicalid")!=null?jsonObject.getString("medicalid"):"";
                        serviceProviderDetails1.setMedicalId(medical);
//...............................

                            serviceProviderDetails1.setMedicalCost(jsonObject.getString("cost").equalsIgnoreCase("null")?"":jsonObject.getString("cost"));
                            serviceProviderDetails1.setImagepath(jsonObject.getString("imagepath"));

//...............................
                            orderId = jsonObject.getString("orderid");
                            customerId = jsonObject.getString("userid");
                            medicalId = medical;

//...............................
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




    class SendFinalConfirmationFromMedicalToCustomer extends AsyncTask<Void, Void, String> {

        protected void onPreExecute() {
            progressDialog.show();
        }

        protected String doInBackground(Void... urls) {

            Utilities utilities = new Utilities(getBaseContext());

            String address = Constants.API_MEDICAL_FINAL_CONFIRMATION;
            Map<String, String> params = new HashMap<>();
            params.put("userid", customerId);//orderId  customerId
//            params.put("userid", "14");
            params.put("orderid", orderId);
            params.put("medicalid",  sharedPreference.getValue( getBaseContext(), Constants.PREF_IS_USER, Constants.PREF_KEY_USER_ID ));
//            params.put("medicalid", "23");

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
                        Toast.makeText(getBaseContext(), jsonObject2.getString("status"), Toast.LENGTH_LONG).show();

                        Intent myIntent = new Intent(getBaseContext(), UserActivity.class);
                        startActivity(myIntent);
                        finish();


                    }
                }

            } catch (Exception e) {
                Toast.makeText( getBaseContext(), "Please try again later...", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
            finally {
                progressDialog.dismiss();
            }

        }
    }


    class SendCostToCustomer extends AsyncTask<Void, Void, String> {

        protected void onPreExecute() {
//            progressDialog.show();
        }

        protected String doInBackground(Void... urls) {

            Utilities utilities = new Utilities(getBaseContext());

            String address = Constants.API_MEDICAL_RECEIVED_ORDER;
            Map<String, String> params = new HashMap<>();
            params.put("medicalid", sharedPreference.getValue( getBaseContext(), Constants.PREF_IS_USER, Constants.PREF_KEY_USER_ID));
            params.put("orderid", orderId);
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
                        Toast.makeText(getBaseContext(), jsonObject2.getString("result"), Toast.LENGTH_LONG).show();

                        Intent myIntent = new Intent(getBaseContext(), UserActivity.class);
                        startActivity(myIntent);
                        finish();
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


    class SendCostConfirmation extends AsyncTask<Void, Void, String> {

        protected void onPreExecute() {
            progressDialog.show();
        }

        protected String doInBackground(Void... urls) {

            Utilities utilities = new Utilities(getBaseContext());

            String address = Constants.API_MEDICAL_COST_RESPONCE_STATUS;
            Map<String, String> params = new HashMap<>();
            params.put("userid", customerId);
            params.put("orderid", orderId);
            params.put("medicalid",  medicalId);

            return utilities.apiCalls(address,params);

        }

        protected void onPostExecute(String response) {
            try {

                JSONObject jsonObject1 = new JSONObject(response);
                JSONObject jsonObject2 = new JSONObject(jsonObject1.getString("Content"));

//				{"Content":"{\"status\":\"success\",\"result\":{\"id\":\"28\",\"firstname\":\"Mangesh\",\"lastname\":\"Khalale\",\"shopname\":\"Mango\",\"email\":\"mangesh@gmail.com\",\"password\":\"111\",\"mobile\":\"8793377994\",\"address\":\"Pathardi Phata,Pathardi Phata, Nashik, Maharashtra, India\",\"latitude\":\"19.946922\",\"longitude\":\"73.7654367\",\"profilepic\":\"no_avatar.jpg\",\"role\":\"medical\",\"regdate\":\"2018-07-27 10:44:22\",\"status\":\"0\",\"deletestatus\":null,\"loginstatus\":\"1\",\"otp\":null}}","Message":"OK","Length":-1,"Type":"text\/html; charset=UTF-8"}
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
                        Toast.makeText(getBaseContext(), jsonObject2.getString("status"), Toast.LENGTH_LONG).show();

                        Intent myIntent = new Intent(getBaseContext(), CustomerActivity.class);
                        startActivity(myIntent);
                        finish();
//                        new RetrieveFeedTask1().execute();
                    }
                }

            } catch (Exception e) {
                Toast.makeText( getBaseContext(), "Please try again later...", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
            finally {
                progressDialog.dismiss();
            }

        }
    }






}