package com.sqindiaadmin.com.sqmobileadmin;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Guna on 18-05-2017.
 */

class ExpandableListAdapter extends BaseExpandableListAdapter{

    HashMap<String, HashMap<String,String>> list_maps;
    List<String> ar_count;
    Context context;
    HashMap<String,String> map_datas=new HashMap<>();
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;
    public ExpandableListAdapter(Context context,HashMap<String, HashMap<String,String>> map, List<String> count) {
        this.context = context;

        this.list_maps = map;

        ar_count = count;
        Log.e("tag","ar_size "+ar_count.size());
        Log.e("tag","listmap_siz "+list_maps.size());
       // map_datas = new HashMap<>();
    }



    @Override
    public int getGroupCount() {
        return list_maps.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return null;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        Typeface tf = Typeface.createFromAsset(context.getAssets(), "helvatica.TTF");
        if (convertView == null) {

            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);
        }


        map_datas = list_maps.get(ar_count.get(groupPosition));
        //Log.e("tag","77777"+map_datas);
        String phone = map_datas.get("phone");
        String name = map_datas.get("username");
        String email = map_datas.get("email");



        Log.e("tag","12345"+name+email+phone);

        Log.e("tag",groupPosition+" child_count "+map_datas.size());

        TextView lblListSno = (TextView) convertView.findViewById(R.id.lblListSno);
        TextView lblListName = (TextView) convertView.findViewById(R.id.lblListName);
        TextView lblListMobile = (TextView) convertView.findViewById(R.id.lblListMobile);
        TextView lblListEmail = (TextView) convertView.findViewById(R.id.lblListEmail);
        //TextView lblListHeader = (TextView) convertView.findViewById(R.id.lblListMobile);

        lblListSno.setTypeface(tf);
        lblListName.setTypeface(tf);
        lblListMobile.setTypeface(tf);
        lblListEmail.setTypeface(tf);

        lblListSno.setText(String.valueOf(groupPosition+1));
        lblListName.setText(name);
        lblListMobile.setText(phone);
        lblListEmail.setText(email);


        return convertView;

    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

      Typeface tf = Typeface.createFromAsset(context.getAssets(), "helvatica.TTF");
        if (convertView == null) {

            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.sold_child_view, null);
        }


      map_datas = list_maps.get(ar_count.get(groupPosition));
        //Log.e("tag","77777"+map_datas);
        final String s_phone = map_datas.get("phone");
        final String s_name = map_datas.get("username");
        String s_dob=map_datas.get("dateofbirth");
        final String s_email = map_datas.get("email");
        final String shop_name=map_datas.get("shopname");
        final String s_rating=map_datas.get("rating");

        Log.e("tag","12345"+s_phone+s_name+s_email+shop_name);

        Log.e("tag",groupPosition+" child_count "+map_datas.size());

        TextView txt_h_name = (TextView) convertView.findViewById(R.id.txt_h_name);
        TextView txt_h_mobile = (TextView) convertView.findViewById(R.id.txt_h_mobile);
        TextView txt_h_dob = (TextView) convertView.findViewById(R.id.txt_h_dob);
        TextView txt_h_mail = (TextView) convertView.findViewById(R.id.txt_v_mail);
        ImageView img_call =(ImageView) convertView.findViewById(R.id.img_call) ;
        ImageView img_email =(ImageView) convertView.findViewById(R.id.img_email) ;
        ImageView img_comments =(ImageView) convertView.findViewById(R.id.img_comments) ;
        TextView txt_v_name = (TextView) convertView.findViewById(R.id.txt_v_name);
        TextView txt_v_mobile = (TextView) convertView.findViewById(R.id.txt_v_mobile);
        TextView txt_v_dob = (TextView) convertView.findViewById(R.id.txt_v_dob);
        TextView txt_v_mail = (TextView) convertView.findViewById(R.id.txt_v_mail);
        TextView txt_rating = (TextView) convertView.findViewById(R.id.txt_rating);

        txt_h_name.setTypeface(tf);
        txt_h_mobile.setTypeface(tf);
        txt_h_dob.setTypeface(tf);
        txt_h_mail.setTypeface(tf);

        txt_v_name.setTypeface(tf);
        txt_v_mobile.setTypeface(tf);
        txt_v_dob.setTypeface(tf);
        txt_v_mail.setTypeface(tf);
        txt_rating.setTypeface(tf);
        txt_v_name.setText(s_name);
        txt_v_mobile.setText(s_phone);
        txt_v_dob.setText(s_dob);
        txt_v_mail.setText(s_email);
        txt_rating.setText("("+s_rating+")");

        img_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                int permissionCheck = ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE);
                if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
                } else {


                    Intent phoneIntent = new Intent(Intent.ACTION_CALL);
                    phoneIntent.setData(Uri.parse("tel:"+s_phone));
                    phoneIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    try {
                        v.getContext().startActivity(phoneIntent);

                    } catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(v.getContext(), "yourActivity is not founded", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


        img_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", s_email, null));

                emailIntent.putExtra(Intent.EXTRA_SUBJECT, shop_name);
                emailIntent.putExtra(Intent.EXTRA_TEXT   , "Hi  "+s_name+"\n"+ "   We are happy to see you,");
                emailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(emailIntent);
            }
        });


        return convertView;

    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}

























//@@@@@@@@@@@@@@@@@@@@@@ CALL PHONE

/*
txt_make_call.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {
        resultp = data.get(position);
        str_owner_no = resultp.get("mobileno");
        int permissionCheck = ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
        ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
        } else {


        Intent phoneIntent = new Intent(Intent.ACTION_CALL);
        phoneIntent.setData(Uri.parse("tel:"+str_owner_no));
        phoneIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        try {
        v.getContext().startActivity(phoneIntent);

        } catch (android.content.ActivityNotFoundException ex) {
        Toast.makeText(v.getContext(), "yourActivity is not founded", Toast.LENGTH_SHORT).show();
        }
        }
        }
        });
*/




//@@@@@@@@@@@@@@@@@@@@@@@ SEND MAIL
 /*txt_send_mail.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
        "mailto", str_owner_mail, null));

        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "MCity");
        emailIntent.putExtra(Intent.EXTRA_TEXT   , "Hi  "+str_username+"\n"+ "   Iam interested in your Property. Please contact me.");
        emailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(emailIntent);

        }
        });*/