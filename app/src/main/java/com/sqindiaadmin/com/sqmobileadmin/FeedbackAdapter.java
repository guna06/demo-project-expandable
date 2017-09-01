package com.sqindiaadmin.com.sqmobileadmin;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by Admin on 21-12-2016.
 */

public class FeedbackAdapter extends BaseAdapter {

    Context context;
    LayoutInflater inflater;
    ArrayList<HashMap<String, String>> data;
    HashMap<String, String> resultp_garage = new HashMap<String, String>();
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;
    String[] time2;
    String str_date,str_month,merge_current_date,str_year,str_day;

    public FeedbackAdapter(Context context, ArrayList<HashMap<String, String>> arraylist) {
        this.context = context;
        data = arraylist;
    }
    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        TextView lblListSno, lblListName;
        ImageView btn_call,btn_email,birthday,star1,star2,star3,star4,star5;
        final String str_name,str_no,str_mail,str_bd,str_rating;

        Typeface tf = Typeface.createFromAsset(context.getAssets(), "helvatica.TTF");
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.feedback_adapter, parent, false);
        resultp_garage = data.get(position);


        lblListSno = (TextView) itemView.findViewById(R.id.lblListSno);
        lblListName = (TextView) itemView.findViewById(R.id.lblListName);
        star1=(ImageView) itemView.findViewById(R.id.star1);
        star2=(ImageView) itemView.findViewById(R.id.star2);
        star3=(ImageView) itemView.findViewById(R.id.star3);
        star4=(ImageView) itemView.findViewById(R.id.star4);
        star5=(ImageView) itemView.findViewById(R.id.star5);
        btn_call=(ImageView) itemView.findViewById(R.id.btn_call);
        btn_email = (ImageView) itemView.findViewById(R.id.btn_email);
        birthday=(ImageView) itemView.findViewById(R.id.birthday);


        //lblListSno.setTypeface(tf);
        lblListName.setTypeface(tf);
        int serial_no=position+1;
        Log.e("tag","serial_no"+serial_no);
        //lblListSno.setText(serial_no);
        //lblListSno.setText(Integer.toString(serial_no));
        str_name=resultp_garage.get("username");
        str_no=resultp_garage.get("phone");
        str_mail=resultp_garage.get("email");
        str_bd=resultp_garage.get("dateofbirth");
        str_rating=resultp_garage.get("rating");
        Log.e("tag","feedback_value"+str_rating);

        if(str_rating.equals("Poor"))
        {
            star1.setBackgroundResource(R.mipmap.star_gold_feedback);
        }

            else if(str_rating.equals("Average"))
        {
            star1.setBackgroundResource(R.mipmap.star_gold_feedback);
            star2.setBackgroundResource(R.mipmap.star_gold_feedback);

        }

                else if(str_rating.equals("Medium"))
        {
            star1.setBackgroundResource(R.mipmap.star_gold_feedback);
            star2.setBackgroundResource(R.mipmap.star_gold_feedback);
            star3.setBackgroundResource(R.mipmap.star_gold_feedback);
        }

                    else if(str_rating.equals("Good"))
        {
            star1.setBackgroundResource(R.mipmap.star_gold_feedback);
            star2.setBackgroundResource(R.mipmap.star_gold_feedback);
            star3.setBackgroundResource(R.mipmap.star_gold_feedback);
            star4.setBackgroundResource(R.mipmap.star_gold_feedback);
        }

                        else if(str_rating.equals("Excellent"))
        {
            star1.setBackgroundResource(R.mipmap.star_gold_feedback);
            star2.setBackgroundResource(R.mipmap.star_gold_feedback);
            star3.setBackgroundResource(R.mipmap.star_gold_feedback);
            star4.setBackgroundResource(R.mipmap.star_gold_feedback);
            star5.setBackgroundResource(R.mipmap.star_gold_feedback);

        }

        Log.e("tag","values"+str_name+str_no+str_mail+str_bd);

        lblListName.setText(str_name);


        //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
        //***************** get Current Date and Time *****************************
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("MM/dd");
        String formattedDate = df.format(c.getTime());
        Log.e("tag", "current_date" + formattedDate);
        Log.e("tag", "birthday value" + str_bd);
        //******************* split date and time *************************
        //2017/05/16 T 19:49 DATE FORMAT

        if(str_bd.equals("Not Mentioned"))
        {
            Log.e("tag","nothing to show");
        }
        else
        {
            time2 = str_bd.split("/");
            Log.e("tag", "splitting value" + time2.length);

            str_year = time2[0];
            str_month = time2[1];
            str_day = time2[2];
            merge_current_date = str_month+"/"+str_day;
            Log.e("tag", "date value----------------------->" + formattedDate+merge_current_date);



            if(formattedDate.equals(merge_current_date))
            {
                birthday.setBackgroundResource(R.mipmap.cake);
            }
            else
            {
                //
            }
        }








        btn_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int permissionCheck = ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE);
                if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
                } else {


                    Intent phoneIntent = new Intent(Intent.ACTION_CALL);
                    phoneIntent.setData(Uri.parse("tel:"+str_no));
                    phoneIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    try {
                        v.getContext().startActivity(phoneIntent);

                    } catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(v.getContext(), "yourActivity is not founded", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });




        return itemView;
    }

}
