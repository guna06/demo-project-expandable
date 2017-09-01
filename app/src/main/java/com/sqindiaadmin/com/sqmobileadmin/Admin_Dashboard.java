package com.sqindiaadmin.com.sqmobileadmin;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.sloop.fonts.FontsManager;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by Guna on 16-05-2017.
 */

public class Admin_Dashboard extends Activity {
    String SOLD_COUNT = DataService.URL_ADMIN + "getsoldcount";

    String ENQUIRY_COUNT = DataService.URL_ADMIN + "getenquiredcount";
    LinearLayout lnr_cus_data,lnr_enquiry_data,lnr_fullcontacts,lnr_feedbacks,lnr_logout;
    ProgressBar progressBar;
    Dialog dialog2,dialog;
    String str_admin_token,str_admin_id;
    SharedPreferences sharedPreferences;
    TextView txt_count,txt_contact_count,txt_enquiry_count,txt_feedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_dashboard);

        lnr_cus_data=(LinearLayout)findViewById(R.id.lnr_cus_data);
        lnr_enquiry_data=(LinearLayout)findViewById(R.id.lnr_enquiry_data);
        lnr_fullcontacts=(LinearLayout)findViewById(R.id.lnr_fullcontacts);
        lnr_feedbacks=(LinearLayout)findViewById(R.id.lnr_frr);
        lnr_logout=(LinearLayout)findViewById(R.id.lnr_logout);
        txt_count=(TextView) findViewById(R.id.txt_count);
        txt_contact_count=(TextView) findViewById(R.id.txt_contact_count);
        txt_enquiry_count=(TextView) findViewById(R.id.txt_enquiry_count);
        txt_feedback=(TextView)findViewById(R.id.txt_feedback);
        FontsManager.initFormAssets(this, "helvatica.TTF");
        FontsManager.changeFonts(this);


        //*********************Loader ****************
        dialog2 = new Dialog(Admin_Dashboard.this);
        dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog2.setCancelable(false);
        dialog2.setContentView(R.layout.anim_loader);
        progressBar = (ProgressBar) dialog2.findViewById(R.id.loading_spinner);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        str_admin_id = sharedPreferences.getString("admin_id", "");
        Log.e("tag","#####################_id"+str_admin_id);

        str_admin_token = sharedPreferences.getString("admin_token", "");
        Log.e("tag","&&&&&&&&&&&&&&&&&_token"+str_admin_token);


        //@@@@@@@@@@@@@@@@@@@@@@@@ SOLD COUNT SERVICE @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
        if (Util.Operations.isOnline(Admin_Dashboard.this)) {
            new SoldCountAsync().execute();
        } else {
            Toast.makeText(getApplicationContext(), "No Internet Connectivity..Please Check", Toast.LENGTH_LONG).show();
        }


        //@@@@@@@@@@@@@@@@@@@@@@@@ ENQUIRY COUNT SERVICE @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
        if (Util.Operations.isOnline(Admin_Dashboard.this)) {
            new EnquiryCountAsync().execute();
        } else {
            Toast.makeText(getApplicationContext(), "No Internet Connectivity..Please Check", Toast.LENGTH_LONG).show();
        }

        lnr_cus_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lnr_cus_data.setBackgroundResource(R.drawable.blue_border);

                Intent cus_data=new Intent(getApplicationContext(),CustomerData.class);
                startActivity(cus_data);
                finish();

            }
        });

        lnr_enquiry_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lnr_enquiry_data.setBackgroundResource(R.drawable.blue_border);
                Intent cus_data=new Intent(getApplicationContext(),EnquiryData.class);
                startActivity(cus_data);
                finish();

            }
        });

        lnr_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitIcon();

            }
        });


        lnr_fullcontacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lnr_fullcontacts.setBackgroundResource(R.drawable.blue_border);
                Intent cus_data=new Intent(getApplicationContext(),FullContacts.class);
                startActivity(cus_data);
                finish();
            }
        });




        lnr_feedbacks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lnr_feedbacks.setBackgroundResource(R.drawable.blue_border);
                Intent feed_data=new Intent(getApplicationContext(),FeedbackActivity.class);
                startActivity(feed_data);
                finish();
               //Toast.makeText(getApplicationContext(),"Comming Soon",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void exitIcon() {

        LayoutInflater layoutInflater = LayoutInflater.from(Admin_Dashboard.this);
        View promptView = layoutInflater.inflate(R.layout.logout_dialog, null);
        final AlertDialog alertD = new AlertDialog.Builder(Admin_Dashboard.this).create();
        alertD.setCancelable(false);
        Window window = alertD.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final TextView head1 = (TextView) promptView.findViewById(R.id.head1);
        final Button logout = (Button) promptView.findViewById(R.id.logout);
        final Button cancel = (Button) promptView.findViewById(R.id.cancel);
        final LinearLayout close=(LinearLayout)promptView.findViewById(R.id.close);

        Typeface tf = Typeface.createFromAsset(getApplicationContext().getAssets(), "helvatica.TTF");
        head1.setTypeface(tf);
        logout.setTypeface(tf);
        cancel.setTypeface(tf);

        logout.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(Admin_Dashboard.this);
                SharedPreferences.Editor editor = shared.edit();
                editor.putString("login_status", "false");
                editor.commit();
                Intent intent_logout=new Intent(Admin_Dashboard.this,AdminLogin.class);
                startActivity(intent_logout);
                finish();

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertD.dismiss();
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertD.dismiss();
            }
        });
        alertD.setView(promptView);
        alertD.show();

    }


    @Override
    public void onBackPressed() {
        showExit();
    }

    private void showExit() {


        LayoutInflater layoutInflater = LayoutInflater.from(Admin_Dashboard.this);
        View promptView = layoutInflater.inflate(R.layout.exit_dialog, null);
        final android.app.AlertDialog alertD = new android.app.AlertDialog.Builder(Admin_Dashboard.this).create();
        alertD.setCancelable(false);
        Window window = alertD.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final TextView head1 = (TextView) promptView.findViewById(R.id.head1);
        final Button cancel = (Button) promptView.findViewById(R.id.cancel);
        final Button exit = (Button) promptView.findViewById(R.id.exit);
        final LinearLayout close=(LinearLayout)promptView.findViewById(R.id.close);

        Typeface tf = Typeface.createFromAsset(getApplicationContext().getAssets(), "helvatica.TTF");
        head1.setTypeface(tf);
        cancel.setTypeface(tf);
        exit.setTypeface(tf);

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Admin_Dashboard.super.onBackPressed();
                onRestart();
                Intent i1 = new Intent(Intent.ACTION_MAIN);
                i1.setAction(Intent.ACTION_MAIN);
                i1.addCategory(Intent.CATEGORY_HOME);
                i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i1);
                alertD.dismiss();
                finish();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertD.dismiss();
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertD.dismiss();
            }
        });
        alertD.setView(promptView);
        alertD.show();

    }


    //############ SOLD SERVICE
    private class SoldCountAsync extends AsyncTask<String,String,String> {


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
                return jsonStr = HttpUtils.makeRequest1(SOLD_COUNT, json,str_admin_id,str_admin_token);
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
                String count=jo.getString("count");
                Log.e("tag","0001"+count);





                if (status.equals("true")) {
                    txt_count.setText(count);
                    txt_contact_count.setText(count);
                    txt_feedback.setText(count);
                    Log.e("tag","0");


                } else {
                    Log.e("tag","1");
                    txt_count.setText("0");
                    txt_contact_count.setText("0");
                    txt_feedback.setText("0");
                    Toast.makeText(getApplicationContext(), "Please Check Network Connection", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }




    //############# ENQUIRY SERVICE
    private class EnquiryCountAsync extends AsyncTask<String,String,String> {


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
                return jsonStr = HttpUtils.makeRequest1(ENQUIRY_COUNT, json,str_admin_id,str_admin_token);
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
                String enquiry_count=jo.getString("count");
                Log.e("tag","0001"+enquiry_count);





                if (status.equals("true")) {
                    txt_enquiry_count.setText(enquiry_count);

                    Log.e("tag","0");


                } else {
                    Log.e("tag","1");
                    txt_enquiry_count.setText("0");

                    Toast.makeText(getApplicationContext(), "Please Check Network Connection", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

}

