package com.medicalstorefinder.medicalstorelocator.Constants;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public final class Utilities {

    private Context _context;

    public Utilities(Context context){
        this._context = context;
    }

    public final boolean isInternetAvailable(){

        ConnectivityManager cm = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {

                return true;
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {

                return true;
            }
        }

        return false;
    }

    public final String apiCall(String pUrl) {

        if( ! isInternetAvailable() )
            return "NO_INTERNET";

        try {

            URL url = new URL(pUrl.replace(" ", "%20"));
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));

//            Log.i("url", String.valueOf(url));

            try{
                StringBuilder stringBuilder = new StringBuilder();
                String line;

                while((line = bufferedReader.readLine())!= null) {
                    stringBuilder.append(line);
                }
//                Log.i("responce",stringBuilder.toString());

                return stringBuilder.toString();

            } catch (Exception e) {
                return "ERROR";
            } finally{
                bufferedReader.close();
                httpURLConnection.disconnect();
            }
        }catch (Exception e) {
            return "ERROR";
        }
    }
}
