package com.sqindiaadmin.com.sqmobileadmin;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.sloop.fonts.FontsManager;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Guna on 20-05-2017.
 */

public class FullContacts extends Activity{
    String GET_FULL_CONTACTS = DataService.URL_ADMIN + "getsoldcustomers";
    String GET_CONTACTS = DataService.URL_ADMIN + "getsoldcontactsbyshopname";
    ListView listview;
    SharedPreferences sharedPreferences;
    String str_admin_id,str_admin_token;
    ProgressBar progressBar;
    MContactAdapter contact_adapter;
    Dialog dialog2;
    LinearLayout back_arrow,lnr_empty;
    HashMap<String, String> map;
    MaterialSpinner spinner;
    ArrayList<HashMap<String, String>> contactadapt;
    private static final String[] SHOP_NAME = {
            "Choose Shop","SQIndia Hardware", "Lenovo Showroom - Guduvanchery","Lenovo Showroom - Chengalpattu","SQIndia Mobiles - Guduvanchery","SQIndia Mobiles - Urapakkam"

    };
    String spin_shop;
    Button btn_filter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_contacts);

        listview=(ListView)findViewById(R.id.listview);
        btn_filter=(Button)findViewById(R.id.btn_filter);
        FontsManager.initFormAssets(this, "helvatica.TTF");
        FontsManager.changeFonts(this);

        dialog2 = new Dialog(FullContacts.this);
        dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog2.setCancelable(false);
        dialog2.setContentView(R.layout.anim_loader);
        progressBar = (ProgressBar) dialog2.findViewById(R.id.loading_spinner);

        back_arrow=(LinearLayout) findViewById(R.id.back_arrow);
        spinner = (MaterialSpinner) findViewById(R.id.spinner);
        lnr_empty=(LinearLayout)findViewById(R.id.lnr_empty);
        spinner.setItems(SHOP_NAME);
        lnr_empty.setVisibility(View.GONE);
        contactadapt=new ArrayList<>();

        if (Util.Operations.isOnline(FullContacts.this)) {
            new CustomerFullContacts().execute();
        } else {
            Toast.makeText(getApplicationContext(), "No Internet Connectivity..Please Check", Toast.LENGTH_LONG).show();
        }
        //showcmt();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        str_admin_id = sharedPreferences.getString("admin_id", "");
        Log.e("tag","#####################_id"+str_admin_id);

        str_admin_token = sharedPreferences.getString("admin_token", "");
        Log.e("tag","&&&&&&&&&&&&&&&&&_token"+str_admin_token);


        btn_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spin_shop=spinner.getText().toString();
                if(spin_shop.equals("Choose Shop"))
                {
                    Toast.makeText(getApplicationContext(), "Hi Admin. Please choose any Shop", Toast.LENGTH_LONG).show();

                }
                else
                {
                    if (Util.Operations.isOnline(FullContacts.this)) {
                        new CustomerContacts().execute();
                    } else {
                        Toast.makeText(getApplicationContext(), "No Internet Connectivity..Please Check", Toast.LENGTH_LONG).show();
                    }
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

    private void showcmt() {

        LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
        View promptView = layoutInflater.inflate(R.layout.comments_dialog, null);
        final android.app.AlertDialog alertD = new android.app.AlertDialog.Builder(FullContacts.this).create();
        alertD.setCancelable(false);
        Window window = alertD.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final TextView head1 = (TextView) promptView.findViewById(R.id.head1);
        final TextView txt_content = (TextView) promptView.findViewById(R.id.txt_content);

        final Button ok = (Button) promptView.findViewById(R.id.ok);
        final LinearLayout close=(LinearLayout)promptView.findViewById(R.id.close);

        Typeface tf = Typeface.createFromAsset(FullContacts.this.getAssets(), "helvatica.TTF");
        head1.setTypeface(tf);
        txt_content.setTypeface(tf);
        ok.setTypeface(tf);
        head1.setText("Info");
        txt_content.setText("Hi Admin. Please Select any one of the Shop");

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertD.dismiss();
            }
        });
        alertD.setView(promptView);
        alertD.show();
    }



    //SERVICE FOR ALL SHOP
    private class CustomerFullContacts extends AsyncTask<String,String,String>{

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



                                   contactadapt.add(map);
                                   Log.e("tag", "CONTACT_LIST"+contactadapt.size());
                               }

                           }

                            }

                        //}
                        contact_adapter = new MContactAdapter(FullContacts.this, contactadapt);
                        listview.setAdapter(contact_adapter);


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




    //SERVICE FOR EACH SHOP
    private class CustomerContacts extends AsyncTask<String,String,String>{

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
                jsonObject.accumulate("shopname", spin_shop);
                json = jsonObject.toString();
                return jsonStr = HttpUtils.makeRequest1(GET_CONTACTS, json,str_admin_id,str_admin_token);
            } catch (Exception e) {
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

                    Log.e("tag","checking"+data);

                    if (data.length() > 0)
                    {

                        Log.e("tag","1");
                        for (int i1 = 0; i1 < data.length(); i1++) {
                            Log.e("tag", "2");


                            JSONObject jsonObject = data.getJSONObject(i1);

                            JSONArray sold_arr = jsonObject.getJSONArray("sold");
                            Log.e("tag", "11111111" + sold_arr);

                            if (sold_arr.length() ==0)
                            {
                                contactadapt.clear();
                                lnr_empty.setVisibility(View.VISIBLE);
                            }
                            else
                            {
                                contactadapt.clear();
                                lnr_empty.setVisibility(View.GONE);
                                for(int r =0;r<sold_arr.length();r++){
                                    map = new HashMap<String, String>();
                                    JSONObject sold =sold_arr.getJSONObject(r);
                                    map.put("dateofbirth", sold.getString("dateofbirth"));
                                    Log.e("tag", "01" + sold.getString("dateofbirth"));
                                    map.put("phone", sold.getString("phone"));
                                    Log.e("tag", "02" + sold.getString("phone"));
                                    map.put("email", sold.getString("email"));
                                    Log.e("tag", "03" + sold.getString("email"));
                                    map.put("username", sold.getString("username"));
                                    Log.e("tag", "04" + sold.getString("username"));
                                    contactadapt.add(map);
                                    Log.e("tag", "CONTACT_LIST"+contactadapt.size());
                                }
                            }

                        }


                        contact_adapter = new MContactAdapter(FullContacts.this, contactadapt);
                        listview.setAdapter(contact_adapter);
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
