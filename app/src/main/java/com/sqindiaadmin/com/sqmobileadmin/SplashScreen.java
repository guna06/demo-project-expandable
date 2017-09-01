package com.sqindiaadmin.com.sqmobileadmin;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

/**
 * Created by Guna on 15-05-2017.
 */

public class SplashScreen extends Activity {
    public static String URL_VERSION = DataService.VERSION + "checkadminversion";
    private static int SPLASH_TIME_OUT = 3000;
    String str_check, version,currentVersion;
    SharedPreferences s_pref;
    SharedPreferences.Editor edit;


    /*public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash_screen);

        version = BuildConfig.VERSION_NAME;
        Log.e("tag", "get_app_status" + version);
        try {
            currentVersion = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            Log.e("tag", "get_playstore_version" + currentVersion);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(SplashScreen.this);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        str_check = sharedPreferences.getString("login_status", "");
        Log.e("tag", "check_str_status" + str_check);

        edit.commit();


        if (Util.Operations.isOnline(SplashScreen.this)) {
            new GetVersionCode().execute();

        } else {
            Toast.makeText(getApplicationContext(), "No Internet Connectivity", Toast.LENGTH_SHORT).show();
        }


    }




    private void falseMethod() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);


        // set dialog message
        alertDialogBuilder
                .setMessage("Hello, please update the app to continue")
                .setCancelable(false)
                .setPositiveButton("UPDATE", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                      /*  Intent browserIntent =
                                new Intent(Intent.ACTION_VIEW, Uri.parse("" +
                                        "market://details?id=com.sqindiaadmin.com.sqmobileadmin"));*/

                        Intent browserIntent =
                                new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + SplashScreen.this.getPackageName() + "&hl=it"));
                        //https://play.google.com/store/apps/details?id=Sqadmin
                        startActivity(browserIntent);
                        dialog.dismiss();
                        finish();

                    }
                })
                .setNegativeButton("NOT NOW", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        if (str_check.equals("true")) {
                            Log.e("tag", "G1");
                            Intent i = new Intent(SplashScreen.this, Admin_Dashboard.class);
                            startActivity(i);
                        } else if (str_check.equals("false")) {
                            Log.e("tag", "G2");
                            Intent i = new Intent(SplashScreen.this, AdminLogin.class);
                            startActivity(i);
                        } else {
                            Log.e("tag", "G3");
                            Intent i = new Intent(SplashScreen.this, AdminLogin.class);
                            startActivity(i);
                        }
                        dialog.dismiss();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();

    }



    private class GetVersionCode extends AsyncTask<Void, String, String> {
        @Override
        protected String doInBackground(Void... voids) {

            String newVersion = null;
            try {
                newVersion = Jsoup.connect("https://play.google.com/store/apps/details?id=" + SplashScreen.this.getPackageName() + "&hl=it")
                        .timeout(30000)
                        .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                        .referrer("http://www.google.com")
                        .get()
                        .select("div[itemprop=softwareVersion]")
                        .first()
                        .ownText();
                return newVersion;
            } catch (Exception e) {
                return newVersion;
            }
        }

        @Override
        protected void onPostExecute(String onlineVersion) {
            super.onPostExecute(onlineVersion);
            if (onlineVersion != null && !onlineVersion.isEmpty()) {
                if (Float.valueOf(currentVersion) < Float.valueOf(onlineVersion)) {
                    //show dialog
                    Log.e("tag", "Current version " + currentVersion + "playstore version " + onlineVersion);
                    falseMethod();
                }
                else
                {
                    new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

                        @Override
                        public void run() {

                            if (str_check.equals("true")) {
                                Log.e("tag","G1");
                                Intent i = new Intent(SplashScreen.this, Admin_Dashboard.class);
                                startActivity(i);
                            }
                            else if(str_check.equals("false"))
                            {
                                Log.e("tag","G2");
                                Intent i = new Intent(SplashScreen.this, AdminLogin.class);
                                startActivity(i);
                            }
                            else
                            {
                                Log.e("tag","G3");
                                Intent i = new Intent(SplashScreen.this, AdminLogin.class);
                                startActivity(i);
                            }

                            // close this activity
                            //finish();
                        }
                    }, SPLASH_TIME_OUT);

                }
                }
            }

        }
    }





































     /* private void trueMethod() {

        if (str_check.equals("true")) {
            Log.e("tag", "G1");
            Intent i = new Intent(SplashScreen.this, Admin_Dashboard.class);
            startActivity(i);
        } else if (str_check.equals("false")) {
            Log.e("tag", "G2");
            Intent i = new Intent(SplashScreen.this, AdminLogin.class);
            startActivity(i);
        } else {
            Log.e("tag", "G3");
            Intent i = new Intent(SplashScreen.this, AdminLogin.class);
            startActivity(i);
        }
    }*/





   /* private class CheckVersion extends AsyncTask<String, String, String> {
        String version;

        public CheckVersion(String version) {

            this.version = version;
        }

        protected void onPreExecute() {

            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {
            String json = "", jsonStr = "";
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("version", version);

                json = jsonObject.toString();

                return jsonStr = HttpUtils.makeRequest(URL_VERSION, json);
            } catch (Exception e) {
            }
            return null;

        }


        @Override
        protected void onPostExecute(String jsonStr) {
            Log.e("tag", "<-----result---->" + jsonStr);
            super.onPostExecute(jsonStr);

            try {
                JSONObject jo = new JSONObject(jsonStr);
                String status = jo.getString("status");
                Log.e("tag", "get_service_ststus" + status);

                s_pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                edit = s_pref.edit();
                edit.putString("version_status", status);
                edit.commit();

                if (status.equals("true")) {
                    Log.e("tag", "ggggg1");


                    trueMethod();

                    //Toast.makeText(getApplicationContext(), status, Toast.LENGTH_SHORT).show();


                } else {
                    Log.e("tag", "ggggg2");
                    falseMethod();

                    //Toast.makeText(getApplicationContext(), status, Toast.LENGTH_SHORT).show();

                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }*/