package com.sqindiaadmin.com.sqmobileadmin;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.jaredrummler.materialspinner.MaterialSpinner;
import com.sloop.fonts.FontsManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Guna on 16-05-2017.
 */

public class DemoData extends Activity {

    String GET_SHOP = DataService.URL_ADMIN + "getsoldcustomersbyshopname";
    String ALL_ENQUIRY_LIST = DataService.URL_ADMIN + "getsoldcustomers";
    LinearLayout back_arrow,lnr_empty;
    ProgressBar progressBar;
    ExpandableListAdapter_enquiry listAdapter;
    ExpandableListView expListView;
    Dialog dialog2;
    HashMap<String, String> map;
    HashMap<String, String> map1;
    ArrayList<String> ar_name;
    ArrayList<String> ar_shop;
    ArrayList<String> ar_count;
    Button btn__enquiry_filter;
    HashMap<String, HashMap<String,String>> newlistchild;
    ArrayList<HashMap<String, String>> filter_customeradapt;
    ArrayList<String> newcount;

    ArrayList<HashMap<String, String>> customeradapt;
    MaterialSpinner spinner;
    String str_admin_token, str_admin_id, get_shop;
    SharedPreferences sharedPreferences;
    HashMap<String, HashMap<String, String>> list_childName;
    HashMap<String, List<String>> list_childPhone;
    HashMap<String, List<String>> list_childEmail;
  /*  private static final String[] SHOP_NAME = {
            "SQIndia Hardware", "SQIndia Mobiles - Guduvanchery", "SQIndia Mobiles - Urappakkam", "Lenovo Showroom - Guduvanchery", "Lenovo Showroom - Chengalpattu"

    };
*/

    private String[] SHOP_NAME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enquiry_data);

        FontsManager.initFormAssets(this, "helvatica.TTF");
        FontsManager.changeFonts(this);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        btn__enquiry_filter = (Button) findViewById(R.id.btn__enquiry_filter);
        lnr_empty=(LinearLayout)findViewById(R.id.lnr_empty);
        str_admin_id = sharedPreferences.getString("admin_id", "");
        Log.e("tag", "#####################_id" + str_admin_id);

        str_admin_token = sharedPreferences.getString("admin_token", "");
        Log.e("tag", "&&&&&&&&&&&&&&&&&_token" + str_admin_token);

        spinner = (MaterialSpinner) findViewById(R.id.spinner);
        expListView = (ExpandableListView) findViewById(R.id.expListView);

        customeradapt = new ArrayList<>();
        ar_name = new ArrayList<>();
        ar_shop = new ArrayList<>();
        ar_count = new ArrayList<>();
        newcount = new ArrayList<>();
        filter_customeradapt = new ArrayList<>();
        newlistchild = new HashMap<String,HashMap<String,String>>();
        list_childName = new HashMap<String, HashMap<String, String>>();
        list_childPhone = new HashMap<String, List<String>>();
        list_childEmail = new HashMap<String, List<String>>();

        dialog2 = new Dialog(DemoData.this);
        dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog2.setCancelable(false);
        dialog2.setContentView(R.layout.anim_loader);
        progressBar = (ProgressBar) dialog2.findViewById(R.id.loading_spinner);
        back_arrow = (LinearLayout) findViewById(R.id.back_arrow);

        lnr_empty.setVisibility(View.GONE);
        //###################### spinner click listener ######################
        btn__enquiry_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                get_shop = spinner.getText().toString();
                Log.e("tag", "123" + get_shop);
                if (get_shop.equals("Choose Shop")) {
                    Toast.makeText(getApplicationContext(), "Please Choose Any Shop ", Toast.LENGTH_LONG).show();
                } else {
                    new GET_SHOPS_ASYNC().execute();
                }

            }
        });

        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                //Snackbar.make(view, "Clicked " + item, Snackbar.LENGTH_LONG).show();
            }
        });
        spinner.setOnNothingSelectedListener(new MaterialSpinner.OnNothingSelectedListener() {

            @Override
            public void onNothingSelected(MaterialSpinner spinner) {
                //Snackbar.make(spinner, "Nothing selected", Snackbar.LENGTH_LONG).show();
            }
        });

        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iii = new Intent(getApplicationContext(), Admin_Dashboard.class);
                startActivity(iii);
                finish();
            }
        });

        //new GET_SHOPS().execute();


        if (Util.Operations.isOnline(DemoData.this)) {
            new CustomerLists().execute();
        } else {
            Toast.makeText(getApplicationContext(), "No Internet Connectivity..Please Check", Toast.LENGTH_LONG).show();
        }
    }


    private class CustomerLists extends AsyncTask<String, String, String> {
        @Override

        protected void onPreExecute() {
            super.onPreExecute();
            dialog2.show();
        }

        protected String doInBackground(String... params) {

            String json = "", jsonStr = "";
            String id = "";

            try {


                JSONObject jsonObject = new JSONObject();
                json = jsonObject.toString();

                return jsonStr = HttpUtils.makeRequest1(ALL_ENQUIRY_LIST, json, str_admin_id, str_admin_token);
            } catch (Exception e) {
                Log.e("InputStream", e.getLocalizedMessage());
            }
            return null;
        }


        @Override
        protected void onPostExecute(String jsonStr) {
            Log.e("tag", "whole data" + jsonStr);
            dialog2.dismiss();


            super.onPostExecute(jsonStr);
            try {

                JSONObject jo = new JSONObject(jsonStr);


                String status = jo.getString("status");
                Log.e("tag", "<-----Status----->" + status);

                if (status.equals("true")) {
                    JSONArray data = jo.getJSONArray("message");

                    Log.e("tag", "<-----DATA LENGTH----->" + data.length());
                    Log.e("tag", "<-----DATA----->" + data);
                    if (data.length() > 0) {
                        Log.e("tag", "1");
                        for (int i1 = 0; i1 < data.length(); i1++) {
                            Log.e("tag", "2");


                            JSONObject jsonObject = data.getJSONObject(i1);
                            // map = new HashMap<>();
                            // map.put("_id", jsonObject.getString("_id"));
                            // Log.e("tag", "01" + jsonObject.getString("_id"));
                            //  map.put("shopname", jsonObject.getString("shopname"));
                            //Log.e("tag", "02" + jsonObject.getString("shopname"));
                            String shp = jsonObject.getString("shopname");
                            ar_shop.add(shp);
                            JSONArray customerdetails = jsonObject.getJSONArray("enquiry");
                            Log.e("tag", "userdetails" + customerdetails);

                            List<String> ar__list_name = new ArrayList<String>();
                            List<String> ar__list_phone = new ArrayList<String>();
                            List<String> ar_list_email = new ArrayList<String>();

                            for (int u = 0; u < customerdetails.length(); u++) {

                                JSONObject user_obj = customerdetails.getJSONObject(u);
                                map = new HashMap<String, String>();
                                map.put("posted", user_obj.getString("posted"));
                                Log.e("tag", "03" + user_obj.getString("posted"));
                                map.put("rating", user_obj.getString("rating"));
                                Log.e("tag", "04" + user_obj.getString("rating"));
                                map.put("dateofbirth", user_obj.getString("dateofbirth"));
                                Log.e("tag", "05" + user_obj.getString("dateofbirth"));
                                map.put("phone", user_obj.getString("phone"));
                                Log.e("tag", "05" + user_obj.getString("phone"));
                                map.put("email", user_obj.getString("email"));
                                Log.e("tag", "06" + user_obj.getString("email"));
                                map.put("username", user_obj.getString("username"));
                                Log.e("tag", "07" + user_obj.getString("username"));
                                map.put("_id", user_obj.getString("_id"));
                                Log.e("tag", "08" + user_obj.getString("_id"));
                                map.put("shopname", shp);

                                ar__list_name.add(user_obj.getString("username"));
                                ar__list_phone.add(user_obj.getString("phone"));
                                ar_list_email.add(user_obj.getString("email"));

                               /* Log.e("tag","NAMES"+ar__list_name);
                                Log.e("tag","PHONE"+ar__list_phone);
                                Log.e("tag","Email"+ar_list_email);*/

                                list_childName.put(String.valueOf(i1) + String.valueOf(u), map);
                                ar_count.add(String.valueOf(i1) + String.valueOf(u));

                            }


                            customeradapt.add(map);
                            Log.e("tag", "CONTACT_LIST" + customeradapt);

                        }
                        SHOP_NAME = new String[ar_shop.size() + 1];

                        SHOP_NAME[0] = "Choose Shop";
                        for (int i = 0; i < ar_shop.size(); i++) {
                            SHOP_NAME[i + 1] = ar_shop.get(i);
                        }

                        spinner.setItems(SHOP_NAME);
                        listAdapter = new ExpandableListAdapter_enquiry(DemoData.this, list_childName, ar_count);

                        // setting list adapter
                        expListView.setAdapter(listAdapter);

                    } else {

                        //Toast.makeText(getApplicationContext(), "Sorry, No relevent data available... ", Toast.LENGTH_LONG).show();
                    }
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private class GET_SHOPS_ASYNC extends AsyncTask<String,String,String> {





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
                jsonObject.accumulate("shopname", get_shop);
                json = jsonObject.toString();
                return jsonStr = HttpUtils.makeRequest1(GET_SHOP, json,str_admin_id,str_admin_token);
            } catch (Exception e) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(String jsonStr) {
            super.onPostExecute(jsonStr);
            dialog2.dismiss();
            Log.e("tag","6");


            try {
                JSONObject jo = new JSONObject(jsonStr);
                String status = jo.getString("status");


                if (status.equals("true")) {
                    Log.e("tag","1");

                    JSONObject jsonObject = jo.getJSONObject("message");
                    Log.e("tag", "<-----DATA LENGTH----->" + jsonObject.length());


                    JSONArray shop_list = jsonObject.getJSONArray("sold");
                    Log.e("tag","DATATTTTTTTTTT"+shop_list.length());
                    if (shop_list.length() ==0)
                    {
                        newlistchild.clear();
                        newcount.clear();
                        lnr_empty.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        lnr_empty.setVisibility(View.GONE);
                        newlistchild.clear();
                        newcount.clear();
                        for(int r =0;r<shop_list.length();r++){

                            JSONObject shop_obj =shop_list.getJSONObject(r);
                            map1 = new HashMap<String, String>();
                            map1.put("posted", shop_obj.getString("posted"));
                            Log.e("tag", "03" + shop_obj.getString("posted"));
                            map1.put("rating", shop_obj.getString("rating"));
                            Log.e("tag", "04" + shop_obj.getString("rating"));
                            map1.put("dateofbirth", shop_obj.getString("dateofbirth"));
                            Log.e("tag", "05" + shop_obj.getString("dateofbirth"));
                            map1.put("phone", shop_obj.getString("phone"));
                            Log.e("tag", "05" + shop_obj.getString("phone"));
                            map1.put("email", shop_obj.getString("email"));
                            Log.e("tag", "06" + shop_obj.getString("email"));
                            map1.put("username", shop_obj.getString("username"));
                            Log.e("tag", "07" + shop_obj.getString("username"));
                            map1.put("_id", shop_obj.getString("_id"));
                            Log.e("tag", "08" + shop_obj.getString("_id"));


                            newlistchild.put(String.valueOf(r),map1);
                            newcount.add(String.valueOf(r));
                        }

                        filter_customeradapt.add(map1);
                        Log.e("tag", "LISTSSSSSSSSSSSSS" + filter_customeradapt);
                    }
                }


                listAdapter = new ExpandableListAdapter_enquiry(DemoData.this,newlistchild,newcount);

                // setting list adapter
                expListView.setAdapter(listAdapter);


             /*   } else
                {
                    lnr_empty.setVisibility(View.VISIBLE);
                    //Toast.makeText(getApplicationContext(), "Sorry, No relevent data available... ", Toast.LENGTH_LONG).show();
                }
*/
            }


            catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent iii = new Intent(getApplicationContext(), Admin_Dashboard.class);
        startActivity(iii);
        finish();
    }


































/*

private class GET_SHOPS extends AsyncTask<String,String,String> {



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
            return jsonStr = HttpUtils.makeRequest1(GET_SHOP, json,str_admin_id,str_admin_token);
        } catch (Exception e) {
        }
        return null;
    }

    @Override
    protected void onPostExecute(String jsonStr) {
        super.onPostExecute(jsonStr);
        dialog2.dismiss();
        Log.e("tag","6");


        try {
            JSONObject jo = new JSONObject(jsonStr);
            String status = jo.getString("status");





            if (status.equals("true")) {
                Log.e("tag","1");
                JSONArray data = jo.getJSONArray("message");
                Log.e("tag", "<-----DATA LENGTH----->" + data.length());
                Log.e("tag", "<-----DATA----->" + data);
                if (data.length() > 0)
                {
                    Log.e("tag","1");
                    for (int i1 = 0; i1 < data.length(); i1++) {
                        Log.e("tag", "2");


                        JSONObject jsonObject = data.getJSONObject(i1);
                        map = new HashMap<String, String>();
                        map.put("_id", jsonObject.getString("_id"));
                        Log.e("tag", "01" + jsonObject.getString("_id"));
                        map.put("shopname", jsonObject.getString("shopname"));
                        Log.e("tag", "02" + jsonObject.getString("shopname"));



                    }
                }
            } else {
                Log.e("tag","1");
                Toast.makeText(getApplicationContext(), "Please Check", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}*/
}