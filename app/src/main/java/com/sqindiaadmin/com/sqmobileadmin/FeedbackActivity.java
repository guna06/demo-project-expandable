package com.sqindiaadmin.com.sqmobileadmin;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.DashPathEffect;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.sloop.fonts.FontsManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Guna on 01-06-2017.
 */

public class FeedbackActivity extends Activity{
    String GET_FULL_CONTACTS = DataService.URL_ADMIN + "getsoldcustomers";
    String FILTER_CUSTOMER = DataService.URL_ADMIN + "getsoldcontactsratingfromname";
    ProgressBar progressBar;
    HashMap<String, String> map;
    Dialog dialog2;
    FeedbackAdapter feedback_adapter;
    String str_admin_id,str_admin_token;
    SharedPreferences sharedPreferences;
    LinearLayout lnr_empty;
    ListView listview;
    ArrayList<HashMap<String, String>> feedadapt;
    ArrayList<String> arryList = new ArrayList<String>();
    LinearLayout back_arrow;
    String AutoCompleteTextViewValue;
    AutoCompleteTextView simpleAutoCompleteTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback_activity);

        FontsManager.initFormAssets(this, "helvatica.TTF");
        FontsManager.changeFonts(this);
        listview=(ListView)findViewById(R.id.listview);
        lnr_empty=(LinearLayout)findViewById(R.id.lnr_empty);
        lnr_empty.setVisibility(View.GONE);
        feedadapt=new ArrayList<>();
        dialog2 = new Dialog(FeedbackActivity.this);
        dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog2.setCancelable(false);
        dialog2.setContentView(R.layout.anim_loader);
        progressBar = (ProgressBar) dialog2.findViewById(R.id.loading_spinner);
        back_arrow=(LinearLayout)findViewById(R.id.back_arrow);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        str_admin_id = sharedPreferences.getString("admin_id", "");
        Log.e("tag","#####################_id"+str_admin_id);

        str_admin_token = sharedPreferences.getString("admin_token", "");
        Log.e("tag","&&&&&&&&&&&&&&&&&_token"+str_admin_token);
        if (Util.Operations.isOnline(FeedbackActivity.this)) {
            new CustomerFullContacts().execute();
        } else {
            Toast.makeText(getApplicationContext(), "No Internet Connectivity..Please Check", Toast.LENGTH_LONG).show();
        }

        simpleAutoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.simpleAutoCompleteTextView);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arryList);

        simpleAutoCompleteTextView.setAdapter(adapter);
        simpleAutoCompleteTextView.setThreshold(1);//start searching from 1 character
        simpleAutoCompleteTextView.setAdapter(adapter);


        simpleAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                 AutoCompleteTextViewValue = simpleAutoCompleteTextView.getText().toString();
                 Log.e("tag","123"+AutoCompleteTextViewValue);

                if (Util.Operations.isOnline(FeedbackActivity.this)) {
                    new FilterCustomer().execute();
                } else {
                    Toast.makeText(getApplicationContext(), "No Internet Connectivity..Please Check", Toast.LENGTH_LONG).show();
                }
            }
        });



        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back=new Intent(getApplicationContext(),Admin_Dashboard.class);
                startActivity(back);
                finish();

            }
        });
    }

    //FILTER CUSTOMER

    private class FilterCustomer extends AsyncTask<String,String,String> {

        protected void onPreExecute() {
            super.onPreExecute();
            dialog2.show();
        }




        @Override
        protected String doInBackground(String... strings) {


            String json = "", jsonStr;
            try {
                Log.e("tag","5");
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("username",AutoCompleteTextViewValue);
                json = jsonObject.toString();
                return jsonStr = HttpUtils.makeRequest1(FILTER_CUSTOMER, json,str_admin_id,str_admin_token);
            } catch (Exception e) {
            }
            return null;
        }


        @Override
        protected void onPostExecute(String jsonStr) {
            Log.e("tag", "contact data" + jsonStr);
            dialog2.dismiss();

            super.onPostExecute(jsonStr);
            try {
                JSONObject jo = new JSONObject(jsonStr);
                String status = jo.getString("status");
                Log.e("tag", "<-----Status----->" + status);
                if (status.equals("true")) {
                    JSONArray data = jo.getJSONArray("message");

                    Log.e("tag","checking"+data);

                    if (data.length() > 0)
                    {
                        feedadapt.clear();
                        Log.e("tag","1");
                        for (int i1 = 0; i1 < data.length(); i1++) {
                            Log.e("tag", "message_length"+data.length());


                            JSONObject jsonObject = data.getJSONObject(i1);

                            JSONObject sold = jsonObject.getJSONObject("sold");




                                    map.put("rating", sold.getString("rating"));
                                    Log.e("tag", "01" + sold.getString("rating"));
                                    map.put("phone", sold.getString("phone"));
                                    Log.e("tag", "02" + sold.getString("phone"));
                                    map.put("username", sold.getString("username"));
                                    Log.e("tag", "03" + sold.getString("username"));


                                    feedadapt.add(map);
                                    Log.e("tag", "CONTACT_LIST"+feedadapt.size());




                        }

                        //}
                        Log.e("tag","siz: "+feedadapt.size());
                        Log.e("tag","siasdz: "+arryList.size());
                        feedback_adapter = new FeedbackAdapter(FeedbackActivity.this, feedadapt);
                        listview.setAdapter(feedback_adapter);




                    }
                    else
                    {
                        lnr_empty.setVisibility(View.GONE);
                    }
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }



    //SERVICE FOR ALL SHOP
    private class CustomerFullContacts extends AsyncTask<String,String,String> {

        protected void onPreExecute() {
            super.onPreExecute();
            dialog2.show();
        }




        @Override
        protected String doInBackground(String... strings) {


            String json = "", jsonStr;
            try {
                Log.e("tag","5");
                JSONObject jsonObject = new JSONObject();
                json = jsonObject.toString();
                return jsonStr = HttpUtils.makeRequest1(GET_FULL_CONTACTS, json,str_admin_id,str_admin_token);
            } catch (Exception e) {
            }
            return null;
        }


        @Override
        protected void onPostExecute(String jsonStr) {
            Log.e("tag", "contact data" + jsonStr);
            dialog2.dismiss();

            super.onPostExecute(jsonStr);
            try {
                JSONObject jo = new JSONObject(jsonStr);
                String status = jo.getString("status");
                Log.e("tag", "<-----Status----->" + status);
                if (status.equals("true")) {
                    JSONArray data = jo.getJSONArray("message");

                    Log.e("tag","checking"+data);

                    if (data.length() > 0)
                    {

                        Log.e("tag","1");
                        for (int i1 = 0; i1 < data.length(); i1++) {
                            Log.e("tag", "message_length"+data.length());


                            JSONObject jsonObject = data.getJSONObject(i1);

                            JSONArray sold_arr = jsonObject.getJSONArray("sold");
                            Log.e("tag", "sold_length" + sold_arr.length());

                           /* if (sold_arr.length() ==0)
                            {
                                contactadapt.clear();
                                listview.setAdapter(null);
                                lnr_empty.setVisibility(View.VISIBLE);
                            }
                            else
                            {
                                contactadapt.clear();
                                lnr_empty.setVisibility(View.GONE);

*/
                            if(sold_arr.length()>0)
                            {
                                for(int r =0;r<sold_arr.length();r++)
                                {
                                    map = new HashMap<String, String>();
                                    JSONObject sold =sold_arr.getJSONObject(r);

                                    map.put("posted", sold.getString("posted"));
                                    Log.e("tag", "01" + sold.getString("posted"));
                                    map.put("rating", sold.getString("rating"));
                                    Log.e("tag", "02" + sold.getString("rating"));
                                    map.put("dateofbirth", sold.getString("dateofbirth"));
                                    Log.e("tag", "03" + sold.getString("dateofbirth"));
                                    map.put("phone", sold.getString("phone"));
                                    Log.e("tag", "05" + sold.getString("phone"));
                                    map.put("email", sold.getString("email"));
                                    Log.e("tag", "04" + sold.getString("email"));
                                    map.put("username", sold.getString("username"));
                                    Log.e("tag", "05" + sold.getString("username"));

                                    arryList.add(sold.getString("username"));
                                    Log.e("tag","<--------------------ARRAY LIST------------->"+arryList);

                                    feedadapt.add(map);
                                    Log.e("tag", "CONTACT_LIST"+feedadapt.size());
                                }

                            }

                        }

                        //}
                        Log.e("tag","siz: "+feedadapt.size());
                        Log.e("tag","siasdz: "+arryList.size());
                        feedback_adapter = new FeedbackAdapter(FeedbackActivity.this, feedadapt);
                        listview.setAdapter(feedback_adapter);




                    }
                    else
                    {
                        lnr_empty.setVisibility(View.GONE);
                    }
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent back=new Intent(getApplicationContext(),Admin_Dashboard.class);
        startActivity(back);
        finish();
    }
}
