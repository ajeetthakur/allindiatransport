package com.tpnagar.adapter;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.tpnagar.AppController;
import com.tpnagar.R;
import com.tpnagar.fragment.NotificationsListFragment;
import com.tpnagar.wrapper.NotificationsWrapper;
import com.tpnagar.wrapper.PostGoodsWrapper;
import com.tpnagar.wrapper.PostVehicleWrapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;


public class NotificationAdapter extends BaseAdapter {
    private Activity mContext;
    private ArrayList<NotificationsWrapper> mList;
    private LayoutInflater mLayoutInflater = null;
    Dialog dialog;
    String disposId="";
    ListView mCompleteListView;
    int poslist;
    String Itemid;
    FragmentTransaction transaction;
    NotificationsListFragment notificationsListFragment;
    public NotificationAdapter(Activity context, ArrayList<NotificationsWrapper> list, FragmentTransaction fragmentTransaction, NotificationsListFragment notificationsListFragment) {
        mContext = context;
        mList = list;
        this.notificationsListFragment=notificationsListFragment;
        transaction=fragmentTransaction;
        mLayoutInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int pos) {
        return mList.get(pos);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v = convertView;
        final int positionNew=position;
       final CompleteListViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater li = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = li.inflate(R.layout.item_notification, null);
            viewHolder = new CompleteListViewHolder(v);
            v.setTag(viewHolder);
        } else {
            viewHolder = (CompleteListViewHolder) v.getTag();
        }

        if (position>0){
            if(position==0){
                viewHolder.date.setVisibility(View.VISIBLE);
            }else if (mList.get(position).getStrdate().equalsIgnoreCase(mList.get(position-1).getStrdate())){
                viewHolder.date.setVisibility(View.GONE);
            }else {
                viewHolder.date.setVisibility(View.VISIBLE);
            }

        }
        viewHolder.date.setText(mList.get(position).getStrdate());
        viewHolder.time.setText(mList.get(position).getStrtime());

        if(mList.get(position).getMsgType().equalsIgnoreCase("1")){
            viewHolder.vehi_number_text.setText("Need Vehicle:");


            PostGoodsWrapper postGoodsWrapper=mList.get(position).getPostGoodsWrapper();
            viewHolder.vehicle_number.setText(postGoodsWrapper.getGoods_Detail());
            viewHolder. type_of_message.setText("Goods Posted");
            viewHolder.company_name.setText(postGoodsWrapper.getContact_Person().toUpperCase());
           // viewHolder.company_name.setTextColor(Color.RED);
          //  viewHolder.company_name.setTypeface(null, Typeface.BOLD);
            viewHolder.discription.setText(postGoodsWrapper.getDescription());
            viewHolder.from.setText("From: "+postGoodsWrapper.getFrom_City_Name()+"("+postGoodsWrapper.getFrom_State_Name()+")");
            viewHolder.to.setText("To: "+postGoodsWrapper.getTo_City_Name()+"("+postGoodsWrapper.getTo_State_Name()+")");
            viewHolder.text_call.setText(postGoodsWrapper.getMobile_Nod());
           // viewHolder.need_vehicle.setText(postGoodsWrapper.getNeed_Vehicle());
            viewHolder.vehicle_details.setText(postGoodsWrapper.getFreight());
            viewHolder.service_text.setText("Freight Rate");
            viewHolder.text_mail.setText(postGoodsWrapper.getEmail());


        }else {
            PostVehicleWrapper postVehicleWrapper=mList.get(position).getPostVehicleWrapper();
            viewHolder.vehicle_number.setText(postVehicleWrapper.getVehicle_No());
            viewHolder.discription.setText(postVehicleWrapper.getDescription());
            viewHolder.vehicle_details.setText(postVehicleWrapper.getVehicle_Detail());
            viewHolder. type_of_message.setText("Vehicle Posted");
            String CName=postVehicleWrapper.getContact_Person();
            CName=CName.toUpperCase();
            viewHolder.company_name.setText(CName);

            //viewHolder.company_name.setTextColor(Color.RED);
           // viewHolder.company_name.setTypeface(null, Typeface.BOLD);
            viewHolder.text_mail.setText(postVehicleWrapper.getContact_Person());
            viewHolder.text_call.setText(postVehicleWrapper.getMobile_No());
            // viewHolder.text_website.setText(mList.get(position).get());
            viewHolder.from.setText("From: "+postVehicleWrapper.getFrom_City_Name()+"("+postVehicleWrapper.getFrom_State_Name()+")");
            viewHolder.to.setText("To: "+postVehicleWrapper.getTo_City_Name()+"("+postVehicleWrapper.getTo_State_Name()+")");

        }
        viewHolder.text_call.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                onCall(viewHolder.text_call.getText().toString());
                return false;
            }
        });


        viewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("Alert!").setMessage("Do you want Delete?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                JSONObject obj = new JSONObject();
                                try {
                                    SharedPreferences prefs = mContext.getSharedPreferences("com.tpnagar", MODE_PRIVATE);

                                    // String Login_Id=prefs.getString("Login_Id","");

                                    String CompanyId=prefs.getString("Company_Id","");
                                    String Login_Id=prefs.getString("Login_Id","");
                                    obj.put("LoginId",Login_Id );
                                    obj.put("id", mList.get(position).getId());
                                    obj.put("CompanyId", CompanyId);



                                    AppController.spinnerStart(mContext);

                                    notificationsListFragment.notificationDeleteRequest(obj);


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });
                AlertDialog alert = builder.create();
                alert.show();


                }
        });

        return v;
    }


    class CompleteListViewHolder {
        TextView service_text,type_of_message,date,time,vehi_number_text,company_name,text_mail,text_call,text_website,from,to,vehicle_details,vehicle_number,discription;
        ImageView delete;
        public CompleteListViewHolder(View base) {
            type_of_message= (TextView) base.findViewById(R.id.type_of_message);
            date = (TextView) base.findViewById(R.id.date);
            time = (TextView) base.findViewById(R.id.time);
            company_name = (TextView) base.findViewById(R.id.contact_person);
            text_mail = (TextView) base.findViewById(R.id.text_mail);
            text_call = (TextView) base.findViewById(R.id.text_call);
            text_website = (TextView) base.findViewById(R.id.text_website);
            service_text= (TextView) base.findViewById(R.id.service_text);
            vehi_number_text= (TextView) base.findViewById(R.id.vehi_number_text);
            from = (TextView) base.findViewById(R.id.from);
            to = (TextView) base.findViewById(R.id.to);
            vehicle_details = (TextView) base.findViewById(R.id.vehicle_details);
            vehicle_number = (TextView) base.findViewById(R.id.vehicle_number);
            discription = (TextView) base.findViewById(R.id.discription);
            delete = (ImageView) base.findViewById(R.id.delete);


        }
    }


    public void onCall(String number) {
        int permissionCheck = ContextCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(mContext, new String[]{Manifest.permission.CALL_PHONE}, 1);
        } else {
            mContext.startActivity(new Intent(Intent.ACTION_CALL).setData(Uri.parse("tel:" + number)));
        }
    }


}