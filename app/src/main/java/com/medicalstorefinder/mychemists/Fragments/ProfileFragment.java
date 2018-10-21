package com.medicalstorefinder.mychemists.Fragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.medicalstorefinder.mychemists.Constants.Constants;
import com.medicalstorefinder.mychemists.Constants.SharedPreference;
import com.medicalstorefinder.mychemists.Models.UserProfile;
import com.medicalstorefinder.mychemists.Constants.Utilities;
import com.medicalstorefinder.mychemists.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import it.sauronsoftware.ftp4j.FTPAbortedException;
import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferException;
import it.sauronsoftware.ftp4j.FTPDataTransferListener;
import it.sauronsoftware.ftp4j.FTPException;
import it.sauronsoftware.ftp4j.FTPIllegalReplyException;

public class ProfileFragment extends android.support.v4.app.Fragment {

    UserProfile userProfile;
    TextView txtvuserId,txtvuserName, txtvFirstName,txtvEmailID, txtvLastName,txtvMobileNumber,txtvLocation,txtvShopName;
    CircleImageView profileImage;
    Bitmap bitmap12;
    String newpic;
    Bitmap bitmap;
    ProgressDialog progressDialog;
    SharedPreference sharedPreference;
    public String res="";
    private static int RESULT_LOAD_IMAGE = 1;
    File f;
    Uri selectedImage;

   /* *//*********  work only for Dedicated IP ***********//*
    static final String FTP_HOST= "allegoryweb.com";

    *//*********  FTP USERNAME ***********//*
    static final String FTP_USER = "emedical@allegoryweb.com";

    *//*********  FTP PASSWORD ***********//*
    static final String FTP_PASS  ="11QCOX&3vzX!";*/


    /*********  work only for Dedicated IP ***********/
    static final String FTP_HOST= "ftp.mychemist.net.in";

    /*********  FTP USERNAME ***********/
    static final String FTP_USER = "mychemist";

    /*********  FTP PASSWORD ***********/
    static final String FTP_PASS  ="d1Y%9HFZpqle";


    String ff="";
    String picturePath="";
    String name;

    public ProfileFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.profile,null);

        Toolbar toolbar = (Toolbar) v.findViewById(R.id.toolbar_profile);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        sharedPreference = new SharedPreference();

        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) v.findViewById(R.id.collapsing_toolbar_profile);
        collapsingToolbar.setTitle("Profile");

        txtvuserId=(TextView)v.findViewById(R.id.userid);
        txtvuserName=(TextView)v.findViewById(R.id.uname);
        txtvFirstName =(TextView)v.findViewById(R.id.firstname);
        txtvEmailID=(TextView)v.findViewById(R.id.email);
        txtvLastName =(TextView)v.findViewById(R.id.lastName);
        txtvMobileNumber=(TextView)v.findViewById(R.id.regmobile);
        txtvLocation=(TextView)v.findViewById(R.id.location);
        txtvShopName=(TextView)v.findViewById(R.id.shopName);

        if (sharedPreference.getValue( getActivity(), Constants.PREF_USER_ROLE, Constants.PREF_USER_ROLE ).equalsIgnoreCase("customer")){
            txtvShopName.setVisibility(View.GONE);
        }else{
            txtvShopName.setVisibility(View.VISIBLE);
        }

//        txtvuserId.setText(sharedPreference.getValue(getActivity().getApplicationContext(), Constants.PREF_ISAD, Constants.PREF_KEY_USER_Email));
//        txtvuserName.setText(sharedPreference.getValue(getActivity().getApplicationContext(), Constants.PREF_ISAD, Constants.PREF_KEY_USER_NAME));

        profileImage=(CircleImageView)v.findViewById(R.id.profileImage);

        profileImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });


        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        new ProfileRetrievedData().execute();

        return v;
    }


    class ImageCompressionAsyncTask extends AsyncTask<String, Void, String>{
        private boolean fromGallery;

        public ImageCompressionAsyncTask(boolean fromGallery){
            this.fromGallery = fromGallery;
        }

        @Override
        protected String doInBackground(String... params) {
            String filePath = compressImage(params[0]);
            return filePath;
        }
        private String getRealPathFromURI(String contentURI) {
            Uri contentUri = Uri.parse(contentURI);
            Cursor cursor = getActivity().getContentResolver().query(contentUri, null, null, null, null);
            if (cursor == null) {
                return contentUri.getPath();
            } else {
                cursor.moveToFirst();
                int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                return cursor.getString(index);
            }
        }

        public String getFilename() {
            File file = new File(Environment.getExternalStorageDirectory().getPath(), "e-Medical/Images");
            if (!file.exists()) {
                file.mkdirs();
            }
            String uriSting = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");
            return uriSting;

        }

        public String compressImage(String imageUri) {

            String filePath = getRealPathFromURI(imageUri);
            Bitmap scaledBitmap = null;

            BitmapFactory.Options options = new BitmapFactory.Options();

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
            options.inJustDecodeBounds = true;
            Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

            int actualHeight = options.outHeight;
            int actualWidth = options.outWidth;

//      max Height and width values of the compressed image is taken as 816x612

            float maxHeight = 816.0f;
            float maxWidth = 612.0f;
            float imgRatio = actualWidth / actualHeight;
            float maxRatio = maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image

            if (actualHeight > maxHeight || actualWidth > maxWidth) {
                if (imgRatio < maxRatio) {
                    imgRatio = maxHeight / actualHeight;
                    actualWidth = (int) (imgRatio * actualWidth);
                    actualHeight = (int) maxHeight;
                } else if (imgRatio > maxRatio) {
                    imgRatio = maxWidth / actualWidth;
                    actualHeight = (int) (imgRatio * actualHeight);
                    actualWidth = (int) maxWidth;
                } else {
                    actualHeight = (int) maxHeight;
                    actualWidth = (int) maxWidth;

                }
            }

//      setting inSampleSize value allows to load a scaled down version of the original image

            options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

//      inJustDecodeBounds set to false to load the actual bitmap
            options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
            options.inPurgeable = true;
            options.inInputShareable = true;
            options.inTempStorage = new byte[16 * 1024];

            try {
//          load the bitmap from its path
                bmp = BitmapFactory.decodeFile(filePath, options);
            } catch (OutOfMemoryError exception) {
                exception.printStackTrace();

            }
            try {
                scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight,Bitmap.Config.ARGB_8888);
            } catch (OutOfMemoryError exception) {
                exception.printStackTrace();
            }

            float ratioX = actualWidth / (float) options.outWidth;
            float ratioY = actualHeight / (float) options.outHeight;
            float middleX = actualWidth / 2.0f;
            float middleY = actualHeight / 2.0f;

            Matrix scaleMatrix = new Matrix();
            scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

            Canvas canvas = new Canvas(scaledBitmap);
            canvas.setMatrix(scaleMatrix);
            canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

//      check the rotation of the image and display it properly
            ExifInterface exif;
            try {
                exif = new ExifInterface(filePath);

                int orientation = exif.getAttributeInt(
                        ExifInterface.TAG_ORIENTATION, 0);
                Log.d("EXIF", "Exif: " + orientation);
                Matrix matrix = new Matrix();
                if (orientation == 6) {
                    matrix.postRotate(90);
                    Log.d("EXIF", "Exif: " + orientation);
                } else if (orientation == 3) {
                    matrix.postRotate(180);
                    Log.d("EXIF", "Exif: " + orientation);
                } else if (orientation == 8) {
                    matrix.postRotate(270);
                    Log.d("EXIF", "Exif: " + orientation);
                }
                scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                        scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                        true);
            } catch (IOException e) {
                e.printStackTrace();
            }

            FileOutputStream out = null;
            String filename = getFilename();
            try {
                out = new FileOutputStream(filename);

//          write the compressed bitmap at the destination specified by filename.
                scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            return filename;

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            res=result;


            picturePath=res;
            String newstr = null;
            if (null != picturePath && picturePath.length() > 0 )
            {
                int endIndex = picturePath.lastIndexOf("/");
                if (endIndex != -1)
                {
                    newstr = picturePath.substring(endIndex+1); // not forgot to put check if(endIndex != -1)
                }
            }

            ff=newstr;

            f = new File(picturePath);

            profileImage.setImageBitmap(decodeBitmapFromPath(res));


            new uploadTask().execute();
        }
    }
    class uploadTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            client = new FTPClient();
            FileInputStream fis = null;

            try {

                try {
                    client.connect(FTP_HOST, 21);
                    client.login(FTP_USER, FTP_PASS);
                    client.setType(FTPClient.TYPE_BINARY);
                    client.setPassive(true);
                    client.noop();
                    String p = sharedPreference.getValue( getContext(), Constants.PREF_IS_USER, Constants.PREF_KEY_USER_ID );
                    boolean exist = checkDirectoryExists("/public_html/admin/images/"+p+"/");
                    if(!exist)
                        client.createDirectory("/public_html/admin/images/"+p+"/");
                    else
                        client.changeDirectory("/public_html/admin/images/"+p+"/");

                    try {
                        client.upload(f, new MyTransferListener());

                    } catch (FTPDataTransferException e) {
                        e.printStackTrace();
                    } catch (FTPAbortedException e) {
                        e.printStackTrace();
                    }

                } catch (FTPIllegalReplyException e) {
                    e.printStackTrace();
                } catch (FTPException e) {
                    e.printStackTrace();
                }



            } catch (IOException e) {
                e.printStackTrace();
            }

            String p = sharedPreference.getValue( getContext(), Constants.PREF_IS_USER, Constants.PREF_KEY_USER_ID );
            String fileToDelete = Constants.IMAGE_PATH + "public_html/admin/images/"+p+"/"+p+".jpg";
            try {
                client.deleteFile(fileToDelete);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (FTPIllegalReplyException e) {
                e.printStackTrace();
            } catch (FTPException e) {
                e.printStackTrace();
            }


            try {


                client.rename("/public_html/admin/images/"+p+"/"+ff, "/public_html/admin/images/"+p+"/"+p+ ".jpg");
            } catch (IOException e) {
                e.printStackTrace();
            } catch (FTPIllegalReplyException e) {
                e.printStackTrace();
            } catch (FTPException e) {
                e.printStackTrace();
            }

            return null;
        }

        public void onPostExecute(String response) {

           new updateProfile().execute();

        }



    }
    public int convertDipToPixels(float dips){
        Resources r = getResources();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dips, r.getDisplayMetrics());
    }
    FTPClient client=null;
    /*******  Used to file upload and show progress  **********/
    boolean checkDirectoryExists(String dirPath) throws IOException {
        int returnCode = 1;
        try {
            client.changeDirectory(dirPath);
        } catch (FTPIllegalReplyException e) {
            e.printStackTrace();
            returnCode = 0;
        } catch (FTPException e) {
            e.printStackTrace();
            returnCode = 0;
        }

        if (returnCode == 0) {
            return false;
        }
        return true;
    }
    public class MyTransferListener implements FTPDataTransferListener {

        public void started() {

            // Transfer started
            System.out.println(" Upload Started ...");
//            new DeleteImage().execute();

        }

        public void transferred(int length) {

            System.out.println(" transferred ..." + length);
        }

        public void completed() {


            System.out.println(" completed ..." );

        }

        public void aborted() {


            System.out.println(" aborted ..." );
        }

        public void failed() {

            System.out.println(" failed ..." );
        }

    }
    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height/ (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }

    public Bitmap decodeBitmapFromPath(String filePath){
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        scaledBitmap = BitmapFactory.decodeFile(filePath,options);

        options.inSampleSize = calculateInSampleSize(options, convertDipToPixels(150), convertDipToPixels(200));
        options.inDither = false;
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inJustDecodeBounds = false;

        scaledBitmap = BitmapFactory.decodeFile(filePath, options);
        return scaledBitmap;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE  && null != data) {
            selectedImage = data.getData();

            new ImageCompressionAsyncTask(true).execute(data.getDataString());
        }
    }
    private class LoadProfileImage extends AsyncTask<String, String, Bitmap> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        protected Bitmap doInBackground(String... args) {
            try {
                bitmap = BitmapFactory.decodeStream((InputStream)new URL(args[0]).getContent());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        protected void onPostExecute(Bitmap image) {
            if(image != null){
                profileImage.setImageBitmap(image);
            }
        }
    }
    class ProfileRetrievedData extends AsyncTask<String,String,String>
    {
        protected void onPreExecute() {
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... urls) {
            Utilities utilities = new Utilities(getContext());

            String address = Constants.API_MEDICAL_PROFILE_GET;
            Map<String, String> params = new HashMap<>();
            params.put("userid", sharedPreference.getValue( getActivity(), Constants.PREF_IS_USER, Constants.PREF_KEY_USER_ID ));

            return utilities.apiCalls(address,params);
        }

        public void onPostExecute(String response) {

            try {

                JSONObject jsonObject1 = new JSONObject(response);
                JSONObject jsonObject2 = new JSONObject(jsonObject1.getString("Content"));
                JSONObject jsonObject3 = new JSONObject(jsonObject2.getString("result"));

                if (response.equals("NO_INTERNET")) {
                    Toast.makeText(getActivity().getApplicationContext(), "Check internet connection", Toast.LENGTH_LONG).show();
                }
                else if(response.equals("ERROR") ) {
                    Toast.makeText( getActivity().getApplicationContext(), "Please try again later", Toast.LENGTH_LONG).show();
                }
                else {


//                    {"Content":"{\"status\":\"success\",\"
// result\":{\"id\":\"31\",\"firstname\":\"Mangesh\",\"lastname\":\"Khalale\",\"shopname\":\"Mango\",\"
// email\":\"mangeshkhalale9@gmail.com\",\"password\":\"111\",\"mobile\":\"8793377994\",\"
// address\":\"Yashoda Puram,Gokul Nagari, Pimple Gurav, Gokul Nagari, Pimple Gurav, Pimpri-Chinchwad, Maharashtra 411027, India\",\"latitude\":\"18.5894527\",\"longitude\":\"73.822031\",\"
// profilepic\":\"http:\\\/\\\/www.allegoryweb.com\\\/emedical\\\/images\\\/no_avatar.jpg\",\"role\":\"medical\",\"regdate\":\"2018-08-26 12:53:58\",\"status\":\"0\",\"deletestatus\":null,\"loginstatus\":\"1\",\"otp\":null,\"deviceid\":\"e0pB4flab6A:APA91bFuKe0-QHk4HwVq3oUJOiiBSMk6O6pmHmey0hExlGcLqzJhnoWHq3K-M711PbaiThEUGgqhR3m00FVmyP7Hw5gtXPlXEHrQrfiOY4VhEiwYK3nnX9N1mSgzR4GRABNMLlp4bgjI\"}}","Message":"OK","Length":-1,"Type":"text\/html; charset=UTF-8"}


//                    JSONObject jsonObject = new JSONObject(response);

                    userProfile = new UserProfile();

                    userProfile.setFirstName(jsonObject3.getString("firstname"));
                    userProfile.setLastName(jsonObject3.getString("lastname"));
                    userProfile.setShopName(jsonObject3.getString("shopname"));
                    userProfile.setEmail(jsonObject3.getString("email"));
                    userProfile.setRegMobile(jsonObject3.getString("mobile"));

                    String string = jsonObject3.getString("address");
                    String[] bits = string.split(",");
                    String lastWord = "";
                    if(bits.length>2)
                        lastWord = bits[bits.length - 3] + ", " + bits[bits.length - 2] + ", " + bits[bits.length - 1];

//                    serviceProviderDetails1.setAddress(lastWord);

                    userProfile.setLocation(string);
                    userProfile.setProfilePicUrl(jsonObject3.getString("profilepic"));


                    sharedPreference.putValue(getContext(), Constants.PREF_IS_USER, Constants.PREF_KEY_USER_Email, userProfile.getEmail());
                    sharedPreference.putValue(getContext(), Constants.PREF_IS_USER, Constants.PREF_KEY_USER_Address, userProfile.getLocation());
                    sharedPreference.putValue(getContext(), Constants.PREF_IS_USER, Constants.PREF_KEY_USER_SHOP_NAME, userProfile.getShopName());
                    sharedPreference.putValue(getContext(), Constants.PREF_IS_USER, Constants.PREF_KEY_USER_FIRST_NAME, userProfile.getFirstName());
                    sharedPreference.putValue(getContext(), Constants.PREF_IS_USER, Constants.PREF_KEY_USER_LAST_NAME, userProfile.getLastName());
                    sharedPreference.putValue(getContext(), Constants.PREF_IS_USER, Constants.PREF_KEY_USER_PHONE, userProfile.getRegMobile());
                    sharedPreference.putValue(getContext(), Constants.PREF_IS_USER, Constants.PREF_KEY_USER_LATITUDE, jsonObject3.getString("latitude"));
                    sharedPreference.putValue(getContext(), Constants.PREF_IS_USER, Constants.PREF_KEY_USER_LONGITUDE, jsonObject3.getString("longitude"));



                    txtvFirstName.setText(txtvFirstName.getText() + " - " + userProfile.getFirstName());
                    txtvLastName.setText(txtvLastName.getText() + " - " + userProfile.getLastName());
                    txtvEmailID.setText(txtvEmailID.getText() + " - " + userProfile.getEmail());
                    txtvMobileNumber.setText(txtvMobileNumber.getText() + " : " + userProfile.getRegMobile());
                    txtvLocation.setText(txtvLocation.getText() + " : " + userProfile.getLocation());
                    txtvShopName.setText(txtvShopName.getText() + " : " + userProfile.getShopName());

                    new LoadProfileImage().execute(userProfile.getProfilePicUrl());
                }
            } catch (JSONException e1) {
                Toast.makeText( getActivity().getApplicationContext(), "Please try again later", Toast.LENGTH_LONG).show();
            }
            finally {
                progressDialog.dismiss();
            }
        }
    }




    class updateProfile extends AsyncTask<String,String,String>
    {
        protected void onPreExecute() {
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... urls) {
            Utilities utilities = new Utilities(getContext());
            String p = sharedPreference.getValue( getContext(), Constants.PREF_IS_USER, Constants.PREF_KEY_USER_ID );

            String address = Constants.API_MEDICAL_PROFILE_UPDATE;
            Map<String, String> params = new HashMap<>();
            params.put("userid", sharedPreference.getValue( getActivity(), Constants.PREF_IS_USER, Constants.PREF_KEY_USER_ID ));
            params.put("firstname",  sharedPreference.getValue( getActivity(), Constants.PREF_IS_USER, Constants.PREF_KEY_USER_FIRST_NAME ));
            params.put("lastname", sharedPreference.getValue( getActivity(), Constants.PREF_IS_USER, Constants.PREF_KEY_USER_LAST_NAME ));
            params.put("shopname",  sharedPreference.getValue( getActivity(), Constants.PREF_IS_USER, Constants.PREF_KEY_USER_SHOP_NAME ));
            params.put("email", sharedPreference.getValue( getActivity(), Constants.PREF_IS_USER, Constants.PREF_KEY_USER_Email ));
            params.put("mobile",  sharedPreference.getValue( getActivity(), Constants.PREF_IS_USER, Constants.PREF_KEY_USER_PHONE ));
            params.put("latitude", sharedPreference.getValue( getActivity(), Constants.PREF_IS_USER, Constants.PREF_KEY_USER_LATITUDE ));
            params.put("longitude",  sharedPreference.getValue( getActivity(), Constants.PREF_IS_USER, Constants.PREF_KEY_USER_LONGITUDE ));
            params.put("address", sharedPreference.getValue( getActivity(), Constants.PREF_IS_USER, Constants.PREF_KEY_USER_Address ));
            params.put("profilepic", Constants.PROFILE_IMAGE_PATH +p+"/"+p+ ".jpg");




//            {"id":"31","firstname":"Mangesh","lastname":"Khalale","shopname":"Mango","email":"mangeshkhalale9@gmail.com","password":"111","mobile":"8793377994","address":"Yashoda Puram,Gokul Nagari, Pimple Gurav, Gokul Nagari, Pimple Gurav, Pimpri-Chinchwad, Maharashtra 411027, India","latitude":"18.5894527","longitude":"73.822031","profilepic":"http:\/\/www.allegoryweb.com\/emedical\/images\/no_avatar.jpg","role":"medical","regdate":"2018-08-26 12:53:58","status":"0","deletestatus":null,"loginstatus":"1","otp":null,"deviceid":"ea7mwaJJk0Y:APA91bEHez8UstteTiGuNWiLR8_UkPCrWagEQ1TlreXJ4afStcdn67Wmh-wgufleP-LG3Ghv4XLvn-tAWytLoFHMYeKctX_LUJdLA8gllqdmyLJWK_f17cFZnFp6jq3MCbZgLOONXwjM"}



            return utilities.apiCalls(address,params);
        }

        public void onPostExecute(String response) {

            try {

                JSONObject jsonObject1 = new JSONObject(response);
                JSONObject jsonObject2 = new JSONObject(jsonObject1.getString("Content"));
//                JSONObject jsonObject3 = new JSONObject(jsonObject2.getString("message"));

                if (response.equals("NO_INTERNET")) {
                    Toast.makeText(getActivity().getApplicationContext(), "Check internet connection", Toast.LENGTH_LONG).show();
                }
                else if(response.equals("ERROR") ) {
                    Toast.makeText( getActivity().getApplicationContext(), "Please try again later", Toast.LENGTH_LONG).show();
                }
                else if("success".equalsIgnoreCase(jsonObject2.getString("status"))){

                    Toast.makeText( getActivity().getApplicationContext(), jsonObject2.getString("message"), Toast.LENGTH_LONG).show();
                    String p = sharedPreference.getValue( getContext(), Constants.PREF_IS_USER, Constants.PREF_KEY_USER_ID );

                    new LoadProfileImage().execute(Constants.PROFILE_IMAGE_PATH + ""+p+"/"+p+ ".jpg");
                }
            } catch (JSONException e1) {
                Toast.makeText( getActivity().getApplicationContext(), "Please try again later", Toast.LENGTH_LONG).show();
            }
            finally {
                progressDialog.dismiss();
            }
        }
    }




}