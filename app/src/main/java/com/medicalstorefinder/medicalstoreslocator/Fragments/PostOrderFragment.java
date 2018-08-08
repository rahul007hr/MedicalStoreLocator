package com.medicalstorefinder.medicalstoreslocator.Fragments;


import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.medicalstorefinder.medicalstoreslocator.Activity.MainActivity;
import com.medicalstorefinder.medicalstoreslocator.Activity.UserActivity;
import com.medicalstorefinder.medicalstoreslocator.Constants.Constants;
import com.medicalstorefinder.medicalstoreslocator.Constants.CustomToast;
import com.medicalstorefinder.medicalstoreslocator.Constants.SharedPreference;
import com.medicalstorefinder.medicalstoreslocator.Constants.Utilities;
import com.medicalstorefinder.medicalstoreslocator.Constants.Utility;
import com.medicalstorefinder.medicalstoreslocator.Constants.Utils1;
import com.medicalstorefinder.medicalstoreslocator.Geofencing;
import com.medicalstorefinder.medicalstoreslocator.Models.ApiUser;
import com.medicalstorefinder.medicalstoreslocator.Models.PostOrders;
import com.medicalstorefinder.medicalstoreslocator.PlaceListAdapter;
import com.medicalstorefinder.medicalstoreslocator.Provider.PlaceContract;
import com.medicalstorefinder.medicalstoreslocator.R;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class PostOrderFragment extends Fragment implements View.OnClickListener,GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener  {

    PostOrders postOrders = new PostOrders();
    private ImageView profile_img;
    private String userChoosenTask;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 2;
    private static final int PLACE_PICKER_REQUEST = 1;
    public static final String TAG = MainActivity.class.getSimpleName();
    SharedPreference sharedPreference;

    private PlaceListAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private GoogleApiClient mClient;
    private Geofencing mGeofencing;
    private static Button addLocationBtn;
    private static View view;

    EditText descriptionEdtxt;

    public PostOrderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_post_order, container, false);
        try {
//            setContentView(R.layout.activity_main);
            profile_img = (ImageView) view.findViewById(R.id.imageView);
            Button postOrderBtn=(Button)view.findViewById(R.id.postOrderBtn);
            descriptionEdtxt=(EditText)view.findViewById(R.id.description);

            postOrderBtn.setOnClickListener(this);
            profile_img.setOnClickListener(this);

            fragmentManager = getActivity().getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();

//            setUITEXT();
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

            case R.id.postOrderBtn:
                checkValidation();



                String title = ((TextView) mRecyclerView.findViewHolderForAdapterPosition(0).itemView.findViewById(R.id.name_text_view)).getText().toString();
                String address = ((TextView) mRecyclerView.findViewHolderForAdapterPosition(0).itemView.findViewById(R.id.address_text_view)).getText().toString();
                String latLongitude = ((TextView) mRecyclerView.findViewHolderForAdapterPosition(0).itemView.findViewById(R.id.latLong_text_view)).getText().toString();

                String answer = latLongitude.substring(latLongitude.indexOf("(")+1,latLongitude.indexOf(")"));
                String[] s = answer.split(",");
                String lat = s[0];
                String longi = s[1];

                postOrders.setPicUrl("This Field Temporary Blank");
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
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }

    }

    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library","Cancel" };
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
                } else if (items[item].equals("Choose from Library")) {
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
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
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



    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

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
        profile_img.setImageBitmap(thumbnail);
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm=null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getActivity().getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        profile_img.setImageBitmap(bm);
    }

    // Check Validation Method
    private void checkValidation() {


        // Get all edittext texts
        String getImagePath = "This Field Temporary Blank";
        String getDescription = descriptionEdtxt.getText().toString();

        String title = ((TextView) mRecyclerView.findViewHolderForAdapterPosition(0).itemView.findViewById(R.id.name_text_view)).getText().toString();
        String address = ((TextView) mRecyclerView.findViewHolderForAdapterPosition(0).itemView.findViewById(R.id.address_text_view)).getText().toString();
        String latLongitude = ((TextView) mRecyclerView.findViewHolderForAdapterPosition(0).itemView.findViewById(R.id.latLong_text_view)).getText().toString();

        String answer = latLongitude.substring(latLongitude.indexOf("(")+1,latLongitude.indexOf(")"));
        String[] s = answer.split(",");
        String lat = s[0];
        String longi = s[1];

        String getLatitude =lat;
        String getLongitude =longi;

        String getAddress =title +","+address;
        // Check if all strings are null or not
        if (getImagePath.equals("") || getImagePath.length() == 0
                || getDescription.equals("") || getDescription.length() == 0
                || getLatitude.equals("") || getLatitude.length() == 0
                || getLongitude.equals("") || getLongitude.length() == 0
                || getAddress.equals("") || getAddress.length() == 0)

            new CustomToast().Show_Toast(getActivity(), view,
                    "All fields are required.");

            // Else do signup or do your stuff
        else {

            if (sharedPreference.isSPKeyExits(getActivity(), Constants.PREF_SERVICE_PROVIDER_IDS, Constants.PREF_SERVICE_PROVIDER_IDS)){
                if(!sharedPreference.getValue( getActivity(), Constants.PREF_SERVICE_PROVIDER_IDS, Constants.PREF_SERVICE_PROVIDER_IDS ).equalsIgnoreCase("")){
                    new PostOrder().execute();
                }else{
                    Toast.makeText(getContext(),"No Medical Store Found in Selected Range",Toast.LENGTH_LONG).show();
                }
            }else{
                Toast.makeText(getContext(),"No Medical Store Found in Selected Range",Toast.LENGTH_LONG).show();
            }


        }
    }

    class PostOrder extends AsyncTask<Void, Void, String> {

        protected void onPreExecute() {

        }

        protected String doInBackground(Void... urls) {
            sharedPreference = new SharedPreference();
            Utilities utilities = new Utilities(getContext());

            String address = Constants.API_POST_ORDER;
            Map<String, String> params = new HashMap<>();
            params.put("userid", sharedPreference.getValue( getContext(), Constants.PREF_IS_USER, Constants.PREF_KEY_USER_ID));
            params.put("latitude", postOrders.getLatitude());
            params.put("longitude", postOrders.getLongitude());
            params.put("address", postOrders.getAddress());
            params.put("description", postOrders.getDescription());
            params.put("imagepath", postOrders.getPicUrl());

            return utilities.apiCalls(address,params);

        }

        protected void onPostExecute(String response) {
            try {

                JSONObject jsonObject1 = new JSONObject(response);
                JSONObject jsonObject2 = new JSONObject(jsonObject1.getString("Content"));


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

                       /* apiUser = new ApiUser();

                        apiUser.setID(jsonObject.getInt("id"));
                        apiUser.setFirst_Name(jsonObject.getString("firstname"));
                        apiUser.setLast_Name(jsonObject.getString("lastname"));
                        apiUser.setRegMobile(jsonObject.getString("mobile"));
                        apiUser.setAddress(jsonObject.getString("address"));
                        apiUser.setShop_Name(jsonObject.getString("shopname"));
                        apiUser.setEmail(jsonObject.getString("email"));
                        apiUser.setPasswords(jsonObject.getString("password"));
                        apiUser.setUserRole(jsonObject.getString("role"));*/
//						apiUser.setProfilePicUrl(jsonObject.getString("photo"));



                       /* sharedPreference.clearSharedPreference(getContext(), Constants.PREF_IS_USER);
                        sharedPreference.createSharedPreference(getActivity(), Constants.PREF_IS_USER);

                        sharedPreference.putValue(getContext(), Constants.PREF_IS_USER, Constants.PREF_KEY_USER_ID, String.valueOf(apiUser.getID()));
                        sharedPreference.putValue(getContext(), Constants.PREF_IS_USER, Constants.PREF_KEY_USER_Email, apiUser.getEmail());
                        sharedPreference.putValue(getContext(), Constants.PREF_IS_USER, Constants.PREF_KEY_USER_PASS, getPassword);
                        sharedPreference.putValue(getContext(), Constants.PREF_IS_USER, Constants.PREF_KEY_USER_PHONE, getMobileNo);
                        sharedPreference.putValue(getContext(), Constants.PREF_IS_USER, Constants.PREF_KEY_USER_Address, apiUser.getAddress());
                        sharedPreference.putValue(getContext(), Constants.PREF_IS_USER, Constants.PREF_KEY_USER_SHOP_NAME, apiUser.getShop_Name());
                        sharedPreference.putValue(getContext(), Constants.PREF_IS_USER, Constants.PREF_KEY_USER_FIRST_NAME, apiUser.getFirst_Name());
                        sharedPreference.putValue(getContext(), Constants.PREF_IS_USER, Constants.PREF_KEY_USER_LAST_NAME, apiUser.getLast_Name());
                        sharedPreference.putValue(getContext(), Constants.PREF_IS_USER, "medica", apiUser.getUserRole());
//							sharedPreference.putValue(getContext(), Constants.PREF_IS_USER, Constants.PREF_KEY_USER_ProfilePic, apiUser.getProfilePicUrl());
*/
                        /*Intent myIntent = new Intent(getActivity(), UserActivity.class);
                        getActivity().startActivity(myIntent);
                        getActivity().finish();*/

                        Fragment fragment = null;
                        Class fragmentClass1 = null;
                        FragmentTransaction xfragmentTransaction = fragmentManager.beginTransaction();
                        xfragmentTransaction.replace(R.id.containerView,  new ServiceProviderListFragment()).commit();
                        fragmentClass1 = ServiceProviderListFragment.class;

                        Toast.makeText( getContext(), "Success", Toast.LENGTH_LONG).show();
                    }
                }

            } catch (Exception e) {
                Toast.makeText( getContext(), "Something went wrong...", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
            finally {
            }

        }
    }



}

