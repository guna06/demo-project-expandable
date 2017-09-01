package com.sqindiaadmin.com.sqmobileadmin;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;


@SuppressWarnings("deprecation")

public class HttpUtils {

    public static final String TAG = "tagHttpUtils";
    String s1 = "";

    public static String makeRequest(String url, String json) {
        Log.e("tag", "URL-->" + url);
        Log.e("tag", "input-->" + json);
        JSONObject jss = new JSONObject();
        try {
            HttpPost httpPost = new HttpPost(url);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            httpPost.setEntity(new StringEntity(json));
            HttpResponse httpResponse = new DefaultHttpClient().execute(httpPost);


            InputStream inputStream = httpResponse.getEntity().getContent();
            if (inputStream != null) {
                String result = convertInputStreamToString(inputStream);
                Log.e("tag", "output-->" + result);
                return result;
            }


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }





    public static String makeRequest1(String url, String json, String id,String token) {
        Log.e(TAG, "URL-->" + url);
        Log.e(TAG, "input-->" + json);
        Log.e("tag", token);


        try {
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(new StringEntity(json));
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            httpPost.setHeader("id",id);
            httpPost.setHeader("token", token);
            //text/html
            HttpResponse httpResponse = new DefaultHttpClient().execute(httpPost);

            // receive response as inputStream
            InputStream inputStream = httpResponse.getEntity().getContent();
            // convert inputstream to string
            if (inputStream != null) {
                String result = convertInputStreamToString(inputStream);
                Log.e(TAG, "output-->" + result);
                return result;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }


    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>Header adding multiple parameter>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();

        System.out.println(" OUTPUT -->" + result);


        return result;

    }

}
