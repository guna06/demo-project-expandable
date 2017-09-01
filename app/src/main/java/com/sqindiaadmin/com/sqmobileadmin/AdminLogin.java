package com.sqindiaadmin.com.sqmobileadmin;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
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

public class AdminLogin extends Activity {

    String ADMIN_URL = DataService.URL_ADMIN + "login";
    EditText edt_user,edt_pwd;
    Button btn_login;
    String str_user,str_pwd,str_version;
    ProgressBar progressBar;
    Dialog dialog2,dialog;
    SharedPreferences s_pref;
    SharedPreferences.Editor edit;
    TextView txt_version;
    Typeface tff;
    TextInputLayout input_layout_uname,input_layout_pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_login);

        FontsManager.initFormAssets(this, "helvatica.TTF");
        FontsManager.changeFonts(this);

        input_layout_uname=(TextInputLayout)findViewById(R.id.input_layout_uname);
        input_layout_pwd=(TextInputLayout)findViewById(R.id.input_layout_pwd);
        edt_user=(EditText) findViewById(R.id.edt_uname);
        edt_pwd=(EditText) findViewById(R.id.edt_pwd);
        tff=Typeface.createFromAsset(getApplicationContext().getAssets(), "helvatica.TTF");
        input_layout_uname.setTypeface(tff);
        input_layout_pwd.setTypeface(tff);


        //*********************Loader ****************
        dialog2 = new Dialog(AdminLogin.this);
        dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog2.setCancelable(false);
        dialog2.setContentView(R.layout.anim_loader);
        progressBar = (ProgressBar) dialog2.findViewById(R.id.loading_spinner);

        s_pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        btn_login=(Button)findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                str_user=edt_user.getText().toString();
                str_pwd=edt_pwd.getText().toString();


            /*    str_user="sqadmin";
                str_pwd="sqmobiles";*/

                btn_login.setBackgroundResource(R.drawable.submit_bg_press);
                btn_login.setTextColor(Color.parseColor("#FFFFFF"));


                if(Util.Operations.isOnline(AdminLogin.this)) {

                    if (!str_user.equals("") && !str_pwd.equals("")) {

                        new Admin_Creditential(str_user, str_pwd).execute();

                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Invalid Fields..", Toast.LENGTH_LONG).show();
                    }
                }
                else
                {

                    Toast.makeText(getApplicationContext(), "No Internet Connectivity!", Toast.LENGTH_LONG).show();
                }

            }
        });




    }


    private class Admin_Creditential extends AsyncTask<String,String,String> {

        String str_user,str_pwd;

        public Admin_Creditential(String str_user, String str_pwd) {
            this.str_user=str_user;
            this.str_pwd=str_pwd;}

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
                jsonObject.accumulate("username", str_user);
                jsonObject.accumulate("password", str_pwd);
                json = jsonObject.toString();
                return jsonStr = HttpUtils.makeRequest(ADMIN_URL, json);
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
                String id=jo.getString("id");
                Log.e("tag","0001"+id);
                String token = jo.getString("token");
                Log.e("tag","0002"+token);
                String username= jo.getString("username");




                if (status.equals("true")) {
                    Log.e("tag","1");

                   s_pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                     edit = s_pref.edit();
                    edit.putString("admin_id", id);
                    Log.e("tag","1111"+id);
                    edit.putString("admin_token", token);
                    Log.e("tag","2222"+token);
                    edit.putString("admin_username",username);
                    edit.putString("login_status", "true");
                    edit.commit();
                    //Toast.makeText(getApplicationContext(), status, Toast.LENGTH_LONG).show();
                    Intent candidate=new Intent(AdminLogin.this,Admin_Dashboard.class);
                    //candidate.putExtra("session_token", token);
                    startActivity(candidate);
                   // finish();


                } else {
                    Log.e("tag","1");
                    Toast.makeText(getApplicationContext(), "Please Check", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onBackPressed() {
        showExit();
    }

    private void showExit() {


        LayoutInflater layoutInflater = LayoutInflater.from(AdminLogin.this);
        View promptView = layoutInflater.inflate(R.layout.exit_dialog, null);
        final android.app.AlertDialog alertD = new android.app.AlertDialog.Builder(AdminLogin.this).create();
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
                AdminLogin.super.onBackPressed();
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

}