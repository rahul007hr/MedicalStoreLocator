package com.medicalstorefinder.mychemists.Constants;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.Map;

public final class Utilities {

    private Context _context;

    public Utilities(Context context) {
        this._context = context;
    }

    public final boolean isInternetAvailable() {
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


    public final String apiCalls(String pUrl, Map<String, String> params) {
        if (!isInternetAvailable())
            return "NO_INTERNET";
        HttpURLConnection urlConnection;
        String requestBody;
        Uri.Builder builder = new Uri.Builder();
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
            if (urlConnection.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) {
                inputStream = urlConnection.getInputStream();
            } else {
                inputStream = urlConnection.getErrorStream();
            }
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String temp, response = "";
            while ((temp = bufferedReader.readLine()) != null) {
                response += temp;
            }
            jsonObject.put("Content", response);
            jsonObject.put("Message", urlConnection.getResponseMessage());
            jsonObject.put("Length", urlConnection.getContentLength());
            jsonObject.put("Type", urlConnection.getContentType());
            return jsonObject.toString();
        } catch (UnsupportedEncodingException e) {
            return "ERROR";
        } catch (IOException e) {
            return "ERROR";
        } catch (JSONException e) {
            return "ERROR";
        }

    }


    public final String apiCall(String pUrl) {
        if (!isInternetAvailable())
            return "NO_INTERNET";
        try {
            URL url = new URL(pUrl.replace(" ", "%20"));
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            try {
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                return stringBuilder.toString();

            } catch (Exception e) {
                return "ERROR";
            } finally {
                bufferedReader.close();
                httpURLConnection.disconnect();
            }
        } catch (Exception e) {
            return "ERROR";
        }
    }


    public final File downloadImagesToSdCard(String downloadUrl, String imageName) {
        File destination = null;
        try {
            URL url = new URL(downloadUrl);
            URLConnection ucon = url.openConnection();
            InputStream inputStream = null;
            HttpURLConnection httpConn = (HttpURLConnection) ucon;
            httpConn.setRequestMethod("GET");
            httpConn.connect();
            inputStream = httpConn.getInputStream();
            if (httpConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = httpConn.getInputStream();
            }
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            File myDir = new File(Environment.getExternalStorageDirectory(), "My-Chemist");
            if (!myDir.exists()) {
                myDir.mkdir();

            }
            File myDir1 = new File(Environment.getExternalStorageDirectory(), "My-Chemist/Downloaded Prescriptions");
            if (!myDir1.exists()) {
                myDir1.mkdir();

            }
            destination = new File(Environment.getExternalStorageDirectory(), "My-Chemist/Downloaded Prescriptions/" + imageName + ".jpg");
            FileOutputStream fo = null;
            try {
                destination.createNewFile();
                fo = new FileOutputStream(destination);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            int totalSize = httpConn.getContentLength();
            int downloadedSize = 0;
            byte[] buffer = new byte[1024];
            int bufferLength = 0;
            while ((bufferLength = inputStream.read(buffer)) > 0) {
                try {
                    fo.write(buffer, 0, bufferLength);
                    downloadedSize += bufferLength;
                    Log.i("Progress:", "downloadedSize:" + downloadedSize + "totalSize:" + totalSize);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            fo.close();
            Log.d("test", "Image Saved in sdcard..");

        } catch (IOException io) {
            io.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return destination;
    }

    private static boolean isSDCARDMounted() {
        String status = Environment.getExternalStorageState();
        return status.equals(Environment.MEDIA_MOUNTED);
    }
}
