package com.medicalstorefinder.medicalstoreslocator.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.medicalstorefinder.medicalstoreslocator.Constants.Constants;
import com.medicalstorefinder.medicalstoreslocator.Constants.SharedPreference;
import com.medicalstorefinder.medicalstoreslocator.Constants.Utilities;
import com.medicalstorefinder.medicalstoreslocator.Fragments.AboutUsFragment;
import com.medicalstorefinder.medicalstoreslocator.Fragments.ChooseOrderTypeFragment;
import com.medicalstorefinder.medicalstoreslocator.Fragments.ContactUsFragment;
import com.medicalstorefinder.medicalstoreslocator.Fragments.MainFragment;
import com.medicalstorefinder.medicalstoreslocator.Fragments.PostOrderFragment;
import com.medicalstorefinder.medicalstoreslocator.Fragments.ServiceProviderListFragment;
import com.medicalstorefinder.medicalstoreslocator.R;


import org.json.JSONObject;

import java.io.File;
import java.io.FileDescriptor;
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

public class CustomerActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

   /* TextView txtvUserName;
    TextView txtvRegisteredMobileNo;
    TextView txtvEmail;

    String username;*/
   CircleImageView profileImage;
    ActionBarDrawerToggle actionBarDrawerToggle;
    //CircleImageView profileImage;

    ImageView iconBalance;

    View v=null;

    Bitmap bitmap;

    SharedPreference sharedPreference;
    File f;
    private static int RESULT_LOAD_IMAGE = 1;
    public String res="";
    Uri selectedImage;
    public static BottomNavigationView navigation;

    /*********  work only for Dedicated IP ***********/
    static final String FTP_HOST= "allegoryweb.com";

    /*********  FTP USERNAME ***********/
    static final String FTP_USER = "emedical@allegoryweb.com";

    /*********  FTP PASSWORD ***********/
    static final String FTP_PASS  ="11QCOX&3vzX!";

    String ff="";
    String picturePath="";
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);

//             getWindow().setBackgroundDrawableResource(R.drawable.alertdialog_background);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        //Setup the DrawerLayout and NavigationView
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        navigationView = (NavigationView) findViewById(R.id.nav) ;

        // inflating the TabFragment as the first Fragment
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        //fragmentTransaction.replace(R.id.containerView,new RechargeTabFragment()).commit();

        v = navigationView.getHeaderView(0);
       /* txtvUserName=(TextView) v.findViewById(R.id.user_id);
        txtvRegisteredMobileNo=(TextView) v.findViewById(R.id.reg_mobile);
        txtvEmail=(TextView) v.findViewById(R.id.email);*/
        profileImage=(CircleImageView)v.findViewById(R.id.drawer_header_profile_pic);
        profileImage.setVisibility(View.GONE);
        SharedPreference sharedPreference = new SharedPreference();

        //loading the default fragment
        loadFragment(new ChooseOrderTypeFragment());

        //getting bottom navigation view and attaching the listener
        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);
//        username=(sharedPreference.getValue(getApplicationContext(), Constants.PREF_ISAD, Constants.PREF_KEY_USER_Email));

      /*  profileImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });*/

//        name=sharedPreference.getValue(getApplicationContext(), Constants.PREF_ISAD, Constants.PREF_KEY_USER_Email);

//        txtvUserName.setText(sharedPreference.getValue(getApplicationContext(), Constants.PREF_ISAD, Constants.PREF_KEY_USER_NAME));
//        txtvRegisteredMobileNo.setText(sharedPreference.getValue(getApplicationContext(), Constants.PREF_ISAD, Constants.PREF_KEY_USER_RegMobile));
//        txtvEmail.setText(sharedPreference.getValue(getApplicationContext(), Constants.PREF_ISAD, Constants.PREF_KEY_USER_Email));

//        String ProfilePicUrl = sharedPreference.getValue(getApplicationContext(), Constants.PREF_ISAD, Constants.PREF_KEY_USER_ProfilePic);
//        new LoadProfileImage().execute(ProfilePicUrl.replace("~", Constants.DOMAIN_NAME));

        // profileImage=(CircleImageView)v.findViewById(R.id.drawer_header_profile_pic);

        // SharedPreference sharedPreference = new SharedPreference();



//        String ProfilePicUrl = sharedPreference.getValue( getBaseContext(), Constants.PREF_ISRE, Constants.PREF_KEY_USER_ProfilePic );
//        new LoadProfileImage().execute(ProfilePicUrl.replace("~", Constants.DOMAIN_NAME));


//        Animation for balance icon
//        iconBalance = (ImageView) findViewById(R.id.balance_tool);
//        Animation animIconBalance = AnimationUtils.loadAnimation(this,R.anim.flip_grow);
//        animIconBalance.setRepeatCount(Animation.INFINITE);
//        iconBalance.setAnimation(animIconBalance);
//        iconBalance.setAnimation(animIconBalance);
//
//        iconBalance.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(MainActivity.this, WallateBalanceActivity.class));
//            }
//        });

        navigationView.getMenu().findItem(R.id.makeOrder).setChecked(true);

//            MainFragment fragobj = new MainFragment();
        Fragment fragment = null;
        Class fragmentClass1 = null;
        Intent intent = null;
        Bundle bundle = new Bundle();
        String myMessage;
        drawerLayout.closeDrawers();
        FragmentTransaction xfragmentTransaction = fragmentManager.beginTransaction();

//            myMessage = "User";
//            bundle.putString("message", myMessage );
//            fragobj.setArguments(bundle);

//            xfragmentTransaction.replace(R.id.containerView, fragobj).commit();
//            fragmentClass1 = MainFragment.class;



        //Setup click events on the Navigation View Items.
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                if (menuItem.getItemId()>0) {
                    navigationView.getMenu().findItem(R.id.makeOrder).setChecked(false);
                }

//                    ServiceProviderListFragment fragobj = new ServiceProviderListFragment();
                Fragment fragment = null;
                Class fragmentClass1 = null;
                Intent intent = null;
                Bundle bundle = new Bundle();
//                    String myMessage;
                drawerLayout.closeDrawers();
                FragmentTransaction xfragmentTransaction = fragmentManager.beginTransaction();

                switch (menuItem.getItemId()) {
//|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||

                    case R.id.makeOrder:

                            MainFragment fragobj1 = new MainFragment();


                            xfragmentTransaction.replace(R.id.containerView,  new ChooseOrderTypeFragment()).commit();
                            fragmentClass1 = ChooseOrderTypeFragment.class;
                        return true;

                    case R.id.renewOrder:

                        MainFragment fragobj2 = new MainFragment();


                        xfragmentTransaction.replace(R.id.containerView,  new ServiceProviderListFragment()).commit();
                        fragmentClass1 = ServiceProviderListFragment.class;

                        return true;


                    case R.id.monthlyReport:
//                            xfragmentTransaction.replace(R.id.containerView, new UserHistryFragment()).commit();
//                            fragmentClass1 = UserHistryFragment.class;
                        return true;

                        /*case R.id.profile:
                            xfragmentTransaction.replace(R.id.containerView, new ProfileFragment()).commit();
                            fragmentClass1 = ProfileFragment.class;
                            return true;*/
//
                    case R.id.about_us:
                        xfragmentTransaction.replace(R.id.containerView, new AboutUsFragment()).commit();
                        fragmentClass1 = AboutUsFragment.class;
                        return true;
//
                    case R.id.contact_us:
                        xfragmentTransaction.replace(R.id.containerView, new ContactUsFragment()).commit();
                        fragmentClass1 = ContactUsFragment.class;
                        return true;
//|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
                    case R.id.logout:


                          new Logout().execute();

                        SharedPreference sharedPreference = new SharedPreference();
                        sharedPreference.clearSharedPreference(getBaseContext(), Constants.PREF_IS_USER);

                        Intent i = new Intent(getBaseContext(), MainActivity.class);
                        startActivity(i);

                        finish();
                        return true;
                }
                try {
                    fragment = (Fragment) fragmentClass1.newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return true;
            }

        });


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,R.string.app_name, R.string.app_name) {

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                InputMethodManager inputMethodManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                InputMethodManager inputMethodManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

            }
        };
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {

        try {

            android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);

            alertDialogBuilder.setTitle("Exit");
            alertDialogBuilder.setIcon(R.drawable.alert_dialog_warning);
            alertDialogBuilder
                    .setMessage("Are you sure you want to exit?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            CustomerActivity.super.onBackPressed();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        } catch (Exception e) {

        }
    }
    //-------------Touch to hide Keyboard----------------------------
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        boolean handleReturn = super.dispatchTouchEvent(ev);
        View view = getCurrentFocus();

        int x = (int) ev.getX();
        int y = (int) ev.getY();

        if(view instanceof EditText){
            View innerView = getCurrentFocus();

            if (ev.getAction() == MotionEvent.ACTION_UP && !getLocationOnScreen((EditText) innerView).contains(x, y)) {
                InputMethodManager input = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                input.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
            }
        }

        return handleReturn;
    }

    protected Rect getLocationOnScreen(EditText mEditText) {
        Rect mRect = new Rect();
        int[] location = new int[2];

        mEditText.getLocationOnScreen(location);

        mRect.left = location[0];
        mRect.top = location[1];
        mRect.right = location[0] + mEditText.getWidth();
        mRect.bottom = location[1] + mEditText.getHeight();

        return mRect;
    }

    //--------------------------------------------------

    private class LoadProfileImage extends AsyncTask<String, String, Bitmap> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        protected Bitmap doInBackground(String... args) {
            try {
                bitmap = BitmapFactory.decodeStream((InputStream) new URL(args[0]).getContent());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        protected void onPostExecute(Bitmap image) {
            if(image != null){
//                profileImage.setImageBitmap(image);
            }
        }
    }
    class Logout extends AsyncTask<String,String,String>
    {
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... urls) {
            Utilities utilities = new Utilities(getBaseContext());

            String address = Constants.API_MEDICAL_LOGIN;
            Map<String, String> params = new HashMap<>();
            params.put("username", sharedPreference.getValue( getBaseContext(), Constants.PREF_IS_USER, Constants.PREF_KEY_USER_PHONE ));
            params.put("role", sharedPreference.getValue( getBaseContext(), Constants.PREF_USER_ROLE, Constants.PREF_USER_ROLE ));
            params.put("loginstatus", "0");

            return utilities.apiCalls(address,params);
        }

        public void onPostExecute(String response) {

            try{


                JSONObject jsonObject1 = new JSONObject(response);
                JSONObject jsonObject2 = new JSONObject(jsonObject1.getString("Content"));


                if(response.equals("NO_INTERNET")) {
                    Toast.makeText(getBaseContext(), "Check internet connection", Toast.LENGTH_LONG).show();
                }
                else if(jsonObject2.getString("status").equalsIgnoreCase("error")) {
                    Toast.makeText(getBaseContext(), jsonObject2.getString("message"), Toast.LENGTH_LONG).show();
                }
                else {
                    if (jsonObject2.getString("status").equalsIgnoreCase("error")) {
                        Toast.makeText(getBaseContext(), jsonObject2.getString("message"), Toast.LENGTH_LONG).show();
                    } else if (jsonObject2.getString("status").equalsIgnoreCase("success")) {
                        Toast.makeText(getBaseContext(), jsonObject2.getString("status"), Toast.LENGTH_LONG).show();
                    }
                }

            } catch (Exception e) {
                Toast.makeText( getBaseContext(), "Something went wrong...", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }

        }
    }
    /*******  Used to file upload and show progress  **********/

    class uploadTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
/*            FTPClient client = new FTPClient();
            FileInputStream fis = null;

            try {

                try {
                    client.connect(FTP_HOST, 21);
                    client.login(FTP_USER, FTP_PASS);
                    client.setType(FTPClient.TYPE_BINARY);
                    client.setPassive(true);
                    client.noop();
                    client.changeDirectory("/public_html/emedical/images/");
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


            String fileToDelete = "/public_html/emedical/images/"+"name" + ".jpg";
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
                client.rename("/public_html/emedical/images/"+"ff", "/public_html/emedical/images/"+"name" + ".jpg");
            } catch (IOException e) {
                e.printStackTrace();
            } catch (FTPIllegalReplyException e) {
                e.printStackTrace();
            } catch (FTPException e) {
                e.printStackTrace();
            }*/

            return null;
        }
    }


    public class MyTransferListener implements FTPDataTransferListener {

        public void started() {

//            btn.setVisibility(View.GONE);
            // Transfer started
//            Toast.makeText(getBaseContext(), " Upload Started ...", Toast.LENGTH_SHORT).show();
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


    public int convertDipToPixels(float dips){
        Resources r = getResources();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dips, r.getDisplayMetrics());
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
            Cursor cursor = getContentResolver().query(contentUri, null, null, null, null);
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

//            profileImage.setImageBitmap(decodeBitmapFromPath(res));


            new uploadTask().execute();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            selectedImage = data.getData();

            new ImageCompressionAsyncTask(true).execute(data.getDataString());
        }
    }

    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getApplicationContext().getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;

        switch (item.getItemId()) {

            case R.id.chooseOrderType:
                fragment = new ChooseOrderTypeFragment();
                break;

            case R.id.postOrder:
                fragment = new PostOrderFragment();
                break;

            case R.id.NearbyServiceProviderList:
                fragment = new ServiceProviderListFragment();
                break;

            case R.id.serviceProviderResponceList:
//                fragment = new ProfileFragment();
                break;
        }

        return loadFragment(fragment);
    }

    public boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.containerView, fragment)
                    .commit();
            return true;
        }
        return false;
    }
    //.....................................................................


}