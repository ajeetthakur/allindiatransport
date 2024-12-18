package com.tpnagar.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tpnagar.AppController;
import com.tpnagar.R;
import com.tpnagar.fragment.NotificationCompanyListFragment;
import com.tpnagar.fragment.NotificationsListFragment;
import com.tpnagar.wrapper.NotificationCompnay;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import me.leolin.shortcutbadger.ShortcutBadger;


public class NotificationCompanyAdapter extends BaseAdapter {
    private Activity mContext;
    private ArrayList<NotificationCompnay> mList;
    private LayoutInflater mLayoutInflater = null;
    Dialog dialog;
    String disposId="";
    int poslist;
    String Itemid;
    FragmentTransaction transaction;
    NotificationCompanyListFragment  notificationCompanyListFragment;
    public NotificationCompanyAdapter(Activity context, ArrayList<NotificationCompnay> list, FragmentTransaction fragmentTransaction, NotificationCompanyListFragment  notificationCompanyListFragment  ) {
        mContext = context;
        mList = list;
        this.notificationCompanyListFragment=notificationCompanyListFragment;
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
            v = li.inflate(R.layout.item_notification_comoany, null);
            viewHolder = new CompleteListViewHolder(v);
            v.setTag(viewHolder);
        } else {
            viewHolder = (CompleteListViewHolder) v.getTag();
        }
      //  viewHolder.message_date.setText(mList.get(position).getCrDate());

      //  viewHolder.message_date.setTextColor(Color.RED);

       // viewHolder.message_discription.setText(mList.get(position).getMessage());
        viewHolder.message_title.setText(mList.get(position).getCompanyName().toUpperCase());


        if (mList.get(position).getUnReadMsgCount()==0){
            viewHolder.count.setVisibility(View.GONE);
        }else {
            viewHolder.count.setVisibility(View.VISIBLE);

            viewHolder.count.setText(""+mList.get(position).getUnReadMsgCount());
        }





        /*if(!mList.get(position).isBlocked()){
            viewHolder.block.setText("Block");
        }else {
            viewHolder.block.setText("UnBlock");
        }
*/
        viewHolder.lin_row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences prefs = mContext.getSharedPreferences("com.tpnagar", mContext.MODE_PRIVATE);
                int badgeCount = Integer.parseInt(prefs.getString("UnReadMsg","0"));

              int newCount= badgeCount -mList.get(position).getUnReadMsgCount();

                prefs.edit().putString("UnReadMsg",""+newCount).commit();
                ShortcutBadger.applyCount(mContext.getApplicationContext(), newCount); //for 1.1.4+


                JSONObject obj = new JSONObject();
                try {


                    String Login_Id=prefs.getString("Login_Id","");
                    obj.put("Login_Id",Login_Id );
                    obj.put("Company_Id", mList.get(position).getCompanyId());


                    AppController.spinnerStart(mContext);

                    notificationCompanyListFragment.notificationReadRequest(obj);


                } catch (JSONException e) {
                    e.printStackTrace();
                }




                Bundle bundle=new Bundle();
                bundle.putString("Name",mList.get(position).getCompanyName());
                bundle.putString("id",mList.get(position).getCompanyId());
                bundle.putBoolean("isBlocked",mList.get(position).isBlocked());
                NotificationsListFragment editBranchFragment=new NotificationsListFragment();
                editBranchFragment.setArguments(bundle);
                transaction.replace(R.id.frame_container, editBranchFragment, "");
                transaction.addToBackStack(null);
                // transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                transaction.commit();
                }
        });

      /*  viewHolder.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PopupMenu popup = new PopupMenu(mContext, viewHolder.more);
                //Inflating the Popup using xml file

                if(!mList.get(position).isBlocked()){
                    popup.getMenu().add("Block");
                }else {
                    popup.getMenu().add("UnBlock");
            }
                popup.getMenu().add("Delete");

                // popup.getMenuInflater().inflate(R.menu.poupup_menu_reg_title, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {

                        if (item.getTitle().toString().equalsIgnoreCase("Delete")){

                            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                            builder.setTitle("Alert!").setMessage("Do you want Delete?")
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            JSONObject obj = new JSONObject();
                                            try {
                                                SharedPreferences prefs = mContext.getSharedPreferences("com.tpnagar", mContext.MODE_PRIVATE);

                                                String Login_Id=prefs.getString("Login_Id","");
                                                obj.put("LoginId",Login_Id );
                                                obj.put("CompanyId", mList.get(position).getCompanyId());


                                                AppController.spinnerStart(mContext);

                                                notificationCompanyListFragment.notificationDeleteRequest(obj);


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

                        }else{
                            if(!mList.get(position).isBlocked()){
                                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                                builder.setTitle("Alert!").setMessage("Do you want Block this comapany?")
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                                JSONObject obj = new JSONObject();
                                                try {
                                                    SharedPreferences prefs = mContext.getSharedPreferences("com.tpnagar", mContext.MODE_PRIVATE);

                                                    String Company_Id=prefs.getString("Company_Id","");
                                                    obj.put("CompanyId",Company_Id );
                                                    obj.put("BlockedCompanyId", mList.get(position).getCompanyId());
                                                    obj.put("BlockingNote", "");


                                                    AppController.spinnerStart(mContext);

                                                    notificationCompanyListFragment.notificationBlockRequest(obj,true);


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
                            }else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                                builder.setTitle("Alert!").setMessage("Do you want Unblock this comapany?")
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                                JSONObject obj = new JSONObject();
                                                try {
                                                    SharedPreferences prefs = mContext.getSharedPreferences("com.tpnagar", mContext.MODE_PRIVATE);

                                                    String Company_Id=prefs.getString("Company_Id","");
                                                    obj.put("CompanyId",Company_Id );
                                                    obj.put("BlockedCompanyId", mList.get(position).getCompanyId());
                                                    obj.put("UnBlockingNote", "");


                                                    AppController.spinnerStart(mContext);

                                                    notificationCompanyListFragment.notificationBlockRequest(obj,false);


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
                        }

                        return true;
                    }
                });

                popup.show();

            }
        });
*/



        return v;
    }


    class CompleteListViewHolder {
        TextView message_title,count;
        RelativeLayout lin_row;

ImageView image_profile;
        public CompleteListViewHolder(View base) {

            count=base.findViewById(R.id.count);
            image_profile=base.findViewById(R.id.image_profile);
            message_title = (TextView) base.findViewById(R.id.message_title);
           // block = (TextView) base.findViewById(R.id.block);
          //  delete = (TextView) base.findViewById(R.id.delete);
            lin_row=base.findViewById(R.id.lin_row);



        }
    }





}