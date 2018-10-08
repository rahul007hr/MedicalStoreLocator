package com.medicalstorefinder.medicalstoreslocatorss.Fragments;


import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.PermissionRequest;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.medicalstorefinder.medicalstoreslocatorss.Activity.CustomerActivity;
import com.medicalstorefinder.medicalstoreslocatorss.Activity.MainActivity;
import com.medicalstorefinder.medicalstoreslocatorss.Constants.Constants;
import com.medicalstorefinder.medicalstoreslocatorss.Constants.CustomToast;
import com.medicalstorefinder.medicalstoreslocatorss.Constants.SharedPreference;
import com.medicalstorefinder.medicalstoreslocatorss.Constants.Utility;
import com.medicalstorefinder.medicalstoreslocatorss.Geofencing;
import com.medicalstorefinder.medicalstoreslocatorss.ImagePicker;
import com.medicalstorefinder.medicalstoreslocatorss.Models.PostOrders;
import com.medicalstorefinder.medicalstoreslocatorss.Adapter.PlaceListAdapter;
import com.medicalstorefinder.medicalstoreslocatorss.Provider.PlaceContract;
import com.medicalstorefinder.medicalstoreslocatorss.Provider.PlaceDbHelper;
import com.medicalstorefinder.medicalstoreslocatorss.R;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import it.sauronsoftware.ftp4j.FTPAbortedException;
import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferException;
import it.sauronsoftware.ftp4j.FTPDataTransferListener;
import it.sauronsoftware.ftp4j.FTPException;
import it.sauronsoftware.ftp4j.FTPIllegalReplyException;

import static android.app.Activity.RESULT_OK;
import static com.medicalstorefinder.medicalstoreslocatorss.Constants.Constants.DOMAIN_NAME;
import static com.medicalstorefinder.medicalstoreslocatorss.Constants.Constants.IMAGE_PATH;

/**
 * A simple {@link Fragment} subclass.
 */
public class PostOrderFragment extends Fragment implements View.OnClickListener,GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    PostOrders postOrders = new PostOrders();
    private ImageView profile_img;
    private String userChoosenTask;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 2;
    private static final int PLACE_PICKER_REQUEST = 1;
    public static final String TAG = MainActivity.class.getSimpleName();
    SharedPreference sharedPreference;
    String ts="";
    private PlaceListAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private GoogleApiClient mClient;
    ProgressDialog progressDialog;
    private Geofencing mGeofencing;
    private static Button addLocationBtn;
    private static View view;
    Bitmap bitmap;
    File f;
    private static int RESULT_LOAD_IMAGE = 1;
    public String res="";
    Uri selectedImage;

  /*  *//*********  work only for Dedicated IP ***********//*
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
    EditText descriptionEdtxt;
    String imagePath,description;

    public PostOrderFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_post_order, container, false);
        try {
            profile_img = (ImageView) view.findViewById(R.id.imageView);
            descriptionEdtxt=(EditText)view.findViewById(R.id.description);
            Button postOrderNextBtn=(Button)view.findViewById(R.id.postOrderNextBtn);

            postOrderNextBtn.setOnClickListener(this);
            profile_img.setOnClickListener(this);

            fragmentManager = getActivity().getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            sharedPreference = new SharedPreference();

            CustomerActivity.navigation.getMenu().findItem(R.id.postOrder).setEnabled(true);
            CustomerActivity.navigation.getMenu().findItem(R.id.postOrder).setChecked(true);
            CustomerActivity.navigation.getMenu().findItem(R.id.NearbyServiceProviderList).setEnabled(false);

            progressDialog = new ProgressDialog(getContext());
            progressDialog.setMessage("Uploading Image...");
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);

            PlaceDbHelper  mPlaceDbHelper = new PlaceDbHelper(getContext());
            SQLiteDatabase db = mPlaceDbHelper.getWritableDatabase();
            db.delete(PlaceContract.PlaceEntry.TABLE_NAME, null, null);
//            db.execSQL("DROP TABLE IF EXISTS places");
            db.close();
//            setUITEXT();
            sharedPreference.putValue(getContext(), Constants.PREF_USER_ORDER_ImagePath, Constants.PREF_USER_ORDER_ImagePath,"");
            Bundle bundle = this.getArguments();

            if(bundle != null){


                imagePath = bundle.getString("imagePath");
                description = bundle.getString("description");
                sharedPreference.putValue(getContext(), Constants.PREF_USER_ORDER_ImagePath, Constants.PREF_USER_ORDER_ImagePath,imagePath);
                if(!imagePath.equalsIgnoreCase("")) {
                    Picasso.with(getContext())
                            .load(imagePath)
                            .into(profile_img);

                }
                descriptionEdtxt.setText(description);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        mRecyclerView = (RecyclerView) view.findViewById(R.id.address);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new PlaceListAdapter(getActivity(), null);
        mRecyclerView.setAdapter(mAdapter);

        addLocationBtn=(Button)view.findViewById(R.id.addLocationBtn);
        addLocationBtn.setOnClickListener(this);

        mClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(getActivity(), this)
                .build();

        mGeofencing = new Geofencing(getActivity(), mClient);
        mGeofencing.registerAllGeofences();

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.postOrderNextBtn:
                checkValidation();

                Integer itemCount = mRecyclerView.getAdapter().getItemCount();

                String title = itemCount!=0?((TextView) mRecyclerView.findViewHolderForAdapterPosition(0).itemView.findViewById(R.id.name_text_view)).getText().toString():"";
                String address = itemCount!=0?((TextView) mRecyclerView.findViewHolderForAdapterPosition(0).itemView.findViewById(R.id.address_text_view)).getText().toString():"";
                String latLongitude = itemCount!=0?((TextView) mRecyclerView.findViewHolderForAdapterPosition(0).itemView.findViewById(R.id.latLong_text_view)).getText().toString():"";
                String answer = latLongitude!=""?latLongitude.substring(latLongitude.indexOf("(")+1,latLongitude.indexOf(")")):"";

                String lat = "";
                String longi ="";
                if(!answer.equalsIgnoreCase("")) {
                    String[] s = answer.split(",");
                    lat = s[0];
                    longi = s[1];
                }

                postOrders.setPicUrl("");
                postOrders.setDescription(descriptionEdtxt.getText().toString());
                postOrders.setAddress(title +","+address);
                postOrders.setLatitude(lat);
                postOrders.setLongitude(longi);

                break;

           case R.id.imageView:
                selectImage();
                break;

            case R.id.addLocationBtn:

                if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getContext(), getString(R.string.need_location_permission_message), Toast.LENGTH_LONG).show();
                    return;
                }
                try {

                    PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                    Intent i = builder.build(getActivity());
                    startActivityForResult(i, PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    Log.e(TAG, String.format("GooglePlayServices Not Available [%s]", e.getMessage()));
                } catch (GooglePlayServicesNotAvailableException e) {
                    Log.e(TAG, String.format("GooglePlayServices Not Available [%s]", e.getMessage()));
                } catch (Exception e) {
                    Log.e(TAG, String.format("PlacePicker Exception: %s", e.getMessage()));
                }
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        mClient.stopAutoManage(getActivity());
        mClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        mClient.stopAutoManage(getActivity());
        mClient.disconnect();
    }

    @Override
    public void onConnected(@Nullable Bundle connectionHint) {
        refreshPlacesData();
        Log.i(TAG, "API Client Connection Successful!");
    }

    @Override
    public void onConnectionSuspended(int cause) {
        Log.i(TAG, "API Client Connection Suspended!");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult result) {
        Log.e(TAG, "API Client Connection Failed!");
    }

    public void refreshPlacesData() {
        Uri uri = PlaceContract.PlaceEntry.CONTENT_URI;
        Cursor data = getActivity().getContentResolver().query(
                uri,
                null,
                null,
                null,
                null);

        if (data == null || data.getCount() == 0) return;
        List<String> guids = new ArrayList<String>();
        while (data.moveToNext()) {
            guids.add(data.getString(data.getColumnIndex(PlaceContract.PlaceEntry.COLUMN_PLACE_ID)));
        }

        PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi.getPlaceById(mClient,
                guids.toArray(new String[guids.size()]));
        placeResult.setResultCallback(new ResultCallback<PlaceBuffer>() {
            int count=0;
            @Override
            public void onResult(@NonNull PlaceBuffer places) {
                mAdapter.swapPlaces(places);
                mGeofencing.updateGeofencesList(places);
                mGeofencing.registerAllGeofences();
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == PLACE_PICKER_REQUEST && resultCode == RESULT_OK) {
            Place place = PlacePicker.getPlace(getContext(), data);
            if (place == null) {
                Log.i(TAG, "No place selected");
                return;
            }

            String placeID = place.getId();

            // Insert a new place into DB
            ContentValues contentValues = new ContentValues();
            contentValues.put(PlaceContract.PlaceEntry.COLUMN_PLACE_ID, placeID);
            getActivity().getContentResolver().delete(PlaceContract.PlaceEntry.CONTENT_URI,null,null);
            getActivity().getContentResolver().insert(PlaceContract.PlaceEntry.CONTENT_URI, contentValues);
            // Get live data information
            refreshPlacesData();
        }

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA){

                Bitmap bitmap = ImagePicker.getImageFromResult(getActivity(), resultCode, data);
//                mProfileBase64 = Utils.convertBitmapToBase64(bitmap);
                profile_img.setImageBitmap(bitmap);


                onCaptureImageResult(bitmap);
            }
//            selectedImage = data.getData();

        }

    }

    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Gallery","Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result= Utility.checkPermission(getContext());
                if (items[item].equals("Take Photo")) {
                    userChoosenTask ="Take Photo";
                    if(result)
                        cameraIntent();
                } else if (items[item].equals("Choose from Gallery")) {
                    userChoosenTask ="Choose from Gallery";
                    if(result)
                        galleryIntent();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent() {
        Intent i = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, SELECT_FILE);
    }

    private void cameraIntent() {
//        private void pickProfileImage() {
            Intent chooseImagePlanIntent = ImagePicker.getPickImageIntent(getContext());
            startActivityForResult(chooseImagePlanIntent, REQUEST_CAMERA);
//        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if(userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {
                    //code for deny
                }
                break;
        }
    }

    private void onCaptureImageResult(Bitmap thumbnail) {
//        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        Long tsLong = System.currentTimeMillis()/1000;
        ts = tsLong.toString()+".jpg";
        File destination = new File(Environment.getExternalStorageDirectory(),ts);

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

            f = new File(String.valueOf(destination));
            new uploadTask().execute();
//        new ImageCompressionAsyncTask(true).execute(String.valueOf(destination));

        profile_img.setImageBitmap(thumbnail);
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm=null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getActivity().getApplicationContext().getContentResolver(), data.getData());

                int dataSize=0;
                Uri uri  = data.getData();
                try {
                    InputStream fileInputStream=getActivity().getContentResolver().openInputStream(uri);
                    dataSize = fileInputStream.available();
                } catch (Exception e) {
                    e.printStackTrace();
                }
//                new ImageCompressionAsyncTask(true).execute(data.getDataString());
            if(dataSize>10240000)
                new ImageCompressionAsyncTask(true).execute(data.getDataString());
            else {
                String filePath = getRealPathFromURI1(String.valueOf(uri));
                String s = filePath.substring(filePath.indexOf("/storage")+1);
                s.trim();

                f = new File(s);
                ts = f.getName();
                new uploadTask().execute();
            }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        profile_img.setImageBitmap(bm);
    }
    public String getRealPathFromURI1(String contentURI) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = null;
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }
    }
    // Check Validation Method
    private void checkValidation() {


        // Get all edittext texts
        String getDescription = descriptionEdtxt.getText().toString();
        Integer itemCount = mRecyclerView.getAdapter().getItemCount();

        String title = itemCount != 0 ? ((TextView) mRecyclerView.findViewHolderForAdapterPosition(0).itemView.findViewById(R.id.name_text_view)).getText().toString() : "";
        String address = itemCount != 0 ? ((TextView) mRecyclerView.findViewHolderForAdapterPosition(0).itemView.findViewById(R.id.address_text_view)).getText().toString() : "";
        String latLongitude = itemCount != 0 ? ((TextView) mRecyclerView.findViewHolderForAdapterPosition(0).itemView.findViewById(R.id.latLong_text_view)).getText().toString() : "";
        String answer = latLongitude != "" ? latLongitude.substring(latLongitude.indexOf("(") + 1, latLongitude.indexOf(")")) : "";

        String lat = "";
        String longi = "";

        if (!answer.equalsIgnoreCase("")) {
            String[] s = answer.split(",");
            lat = s[0];
            longi = s[1];
        }
        String getLatitude = lat;
        String getLongitude = longi;
        String getAddress = "";
        if (!title.equalsIgnoreCase("") && !address.equalsIgnoreCase("")) {
            getAddress = title + "," + address;
        }
        // Check if all strings are null or not
        if (getDescription.equals("") || getDescription.length() == 0){

            new CustomToast().Show_Toast(getActivity(), view,
                    "Description is required.");
        }else if (getAddress.equals("") || getAddress.length() == 0){

            new CustomToast().Show_Toast(getActivity(), view,
                    "Address is required.");
         }else {
            sharedPreference.putValue(getActivity(), Constants.PREF_USER_ORDER_Description, Constants.PREF_USER_ORDER_Description,getDescription);
            sharedPreference.putValue(getActivity(), Constants.PREF_USER_ORDER_Latitude, Constants.PREF_USER_ORDER_Latitude,getLatitude);
            sharedPreference.putValue(getActivity(), Constants.PREF_USER_ORDER_Longitude, Constants.PREF_USER_ORDER_Longitude,getLongitude);
            sharedPreference.putValue(getActivity(), Constants.PREF_USER_ORDER_getAddress, Constants.PREF_USER_ORDER_getAddress,getAddress);

            ServiceProviderListFragment fragment2 = new ServiceProviderListFragment();
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.containerView, fragment2);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
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
            Cursor cursor = null;
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
            filePath = filePath.substring(filePath.indexOf("/storage")+1);
            filePath.trim();
            Bitmap scaledBitmap = null;

            BitmapFactory.Options options = new BitmapFactory.Options();

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
            options.inJustDecodeBounds = false;
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
                scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

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
            ts = f.getName();
            profile_img.setImageBitmap(decodeBitmapFromPath(res));


            new uploadTask().execute();
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

    FTPClient client=null;
    class uploadTask extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {

            progressDialog.show();
        }

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
                    String p = sharedPreference.getValue( getActivity(), Constants.PREF_IS_USER, Constants.PREF_KEY_USER_ID );

                    boolean exist1 = checkDirectoryExists("/public_html/admin/images/");
                    if(!exist1)
                        client.createDirectory("/public_html/admin/images/");


                    boolean exist = checkDirectoryExists("/public_html/admin/images/"+p+"/");
                    if(!exist)
                    client.createDirectory("/public_html/admin/images/"+p+"/");
                    else
                    client.changeDirectory("/public_html/admin/images/"+p+"/");
                    sharedPreference.putValue(getContext(), Constants.PREF_USER_ORDER_ImagePath, Constants.PREF_USER_ORDER_ImagePath,DOMAIN_NAME+String.valueOf("admin/images/"+p+"/"+ff+ts));
                    try {
//                        client.setAutoNoopTimeout(7000);
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
            return "true";
        }

        protected void onPostExecute(String response) {
            if(response.equalsIgnoreCase("true")){
                progressDialog.dismiss();
            }

        }


    }

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
//            Toast.makeText(getBaseContext(), " Upload Started ...", Toast.LENGTH_SHORT).show();
            System.out.println(" Upload Started ...");
//            progressDialog.show();
        }

        public void transferred(int length) {
            System.out.println(" transferred ..." + length);
        }

        public void completed() {
//            progressDialog.dismiss();
            System.out.println(" completed ..." );
        }

        public void aborted() {
            System.out.println(" aborted ..." );
        }

        public void failed() {

            System.out.println(" failed ..." );
        }

    }

    public int convertDipToPixels(float dips){
        Resources r = getResources();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dips, r.getDisplayMetrics());
    }
}

