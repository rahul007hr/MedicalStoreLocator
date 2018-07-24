package com.medicalstorefinder.medicalstoreslocator.Constants;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;

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


    public final String apiCalls(String pUrl,Map<String, String> params) {

        if( ! isInternetAvailable() )
            return "NO_INTERNET";

        HttpURLConnection urlConnection;
        String requestBody;
        Uri.Builder builder = new Uri.Builder();

        // encode parameters
        Iterator entries = params.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry entry = (Map.Entry) entries.next();
            builder.appendQueryParameter(entry.getKey().toString(), entry.getValue().toString());
            entries.remove();
        }
        requestBody = builder.build().getEncodedQuery();

        try {
            URL url = null;

            url = new URL(pUrl);

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            OutputStream outputStream = new BufferedOutputStream(urlConnection.getOutputStream());
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "utf-8"));
            writer.write(requestBody);
            writer.flush();
            writer.close();
            outputStream.close();

            JSONObject jsonObject = new JSONObject();
            InputStream inputStream;
            // get stream
            if (urlConnection.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) {
                inputStream = urlConnection.getInputStream();
            } else {
                inputStream = urlConnection.getErrorStream();
            }
            // parse stream
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String temp, response = "";
            while ((temp = bufferedReader.readLine()) != null) {
                response += temp;
            }
            // put into JSONObject
            jsonObject.put("Content", response);
            jsonObject.put("Message", urlConnection.getResponseMessage());
            jsonObject.put("Length", urlConnection.getContentLength());
            jsonObject.put("Type", urlConnection.getContentType());

            return jsonObject.toString();
        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
            return "ERROR";
        } catch (IOException e) {
//            e.printStackTrace();
            return "ERROR";
        } catch (JSONException e) {
//            return e.toString();
            return "ERROR";
        }

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
