package com.tpnagar.adapter;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.tpnagar.AppController;
import com.tpnagar.Const;
import com.tpnagar.R;
import com.tpnagar.wrapper.RatingWrapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class RatingAdapter extends BaseAdapter {
    private Activity mContext;
    private ArrayList<RatingWrapper> mList;
    private LayoutInflater mLayoutInflater = null;
    Dialog dialog;
    String disposId = "";
    ListView mCompleteListView;
    int poslist;
    String Itemid;
    FragmentTransaction transaction;

    public RatingAdapter(Activity context, ArrayList<RatingWrapper> list, FragmentTransaction fragmentTransaction) {
        mContext = context;
        mList = list;
        transaction = fragmentTransaction;
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
    public View getView(final int position, final View convertView, ViewGroup parent) {
        View v = convertView;
        final int positionNew = position;
        final CompleteListViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater li = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = li.inflate(R.layout.custom_rating, null);
            viewHolder = new CompleteListViewHolder(v);
            v.setTag(viewHolder);
        } else {
            viewHolder = (CompleteListViewHolder) v.getTag();
        }
        String myString = mList.get(position).getCustomerName();
        String upperString = myString.substring(0, 1).toUpperCase() + myString.substring(1);


        viewHolder.company_name.setText(upperString.toUpperCase());
        viewHolder.company_name.setTextColor(Color.RED);
        viewHolder.company_name.setTypeface(null, Typeface.BOLD);
        viewHolder.rate_number.setText(mList.get(position).getRating() + " Votes");
        // viewHolder.text_call.setText(mList.get(position).getPrimary_Phone());
        viewHolder.ratingbar.setRating(Float.parseFloat(mList.get(position).getRating()));
        String myName = mList.get(position).getPhone();
        StringBuilder myNameSB = new StringBuilder(myName);

        if (myNameSB.length()>5){

            myNameSB.setCharAt(2, 'x');
            myNameSB.setCharAt(3, 'x');
            myNameSB.setCharAt(4, 'x');

        }
        if (myNameSB.length()>6){
            myNameSB.setCharAt(2, 'x');
            myNameSB.setCharAt(3, 'x');
            myNameSB.setCharAt(4, 'x');
            myNameSB.setCharAt(5, 'x');
            myNameSB.setCharAt(6, 'x');

        }
        if (myNameSB.length()>7){
            myNameSB.setCharAt(2, 'x');
            myNameSB.setCharAt(3, 'x');
            myNameSB.setCharAt(4, 'x');
            myNameSB.setCharAt(5, 'x');
            myNameSB.setCharAt(6, 'x');

        }
        if (myNameSB.length()>8){
            myNameSB.setCharAt(2, 'x');
            myNameSB.setCharAt(3, 'x');
            myNameSB.setCharAt(4, 'x');
            myNameSB.setCharAt(5, 'x');
            myNameSB.setCharAt(6, 'x');
            myNameSB.setCharAt(7, 'x');

        }
        /*if (myNameSB.length()>9){
            myNameSB.setCharAt(2, 'x');
            myNameSB.setCharAt(3, 'x');
            myNameSB.setCharAt(4, 'x');
            myNameSB.setCharAt(5, 'x');
            myNameSB.setCharAt(6, 'x');
            myNameSB.setCharAt(7, 'x');
            myNameSB.setCharAt(8, 'x');
        }*/

        viewHolder.text_call.setText(myNameSB);
        viewHolder.text_msg.setText(mList.get(position).getReview());
        viewHolder.text_msg.setText(mList.get(position).getReview());
        viewHolder.textDate.setText(parseDateToddMMyyyy(mList.get(position).getDatetext()));

        return v;
    }
    public void onCall(String number) {
        int permissionCheck = ContextCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(mContext, new String[]{Manifest.permission.CALL_PHONE}, 1);
        } else {
            mContext.startActivity(new Intent(Intent.ACTION_CALL).setData(Uri.parse("tel:" + number)));
        }
    }


    class CompleteListViewHolder {
        TextView  company_name, rate_number, text_call,text_msg,textDate;
        RatingBar ratingbar;

        public CompleteListViewHolder(View base) {



            company_name = (TextView) base.findViewById(R.id.company_name);
            rate_number = (TextView) base.findViewById(R.id.rate_number);
            text_call = (TextView) base.findViewById(R.id.text_call);
            text_msg = (TextView) base.findViewById(R.id.text_msg);
            ratingbar = (RatingBar) base.findViewById(R.id.ratingbar);
            textDate=  base.findViewById(R.id.textDate);

        }
    }


    private void hideShowRequest(JSONObject params) {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, Const.URL_HIDENSHOWCOMPANY,
                params, newResponseRequesrtpostedvehicledelete(), eErrorListenerRequesrtpostedvehicledelete()) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("Authorization", "ZSvbTgTr1x/AmfVm8FqlMA==");
                return headers;
            }
        };
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppController.getInstance().addToRequestQueue(jsonObjReq, "register");
    }

    private Response.ErrorListener eErrorListenerRequesrtpostedvehicledelete() {
        Response.ErrorListener response_error = new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("this is error", error.toString());


                AppController.spinnerStop();
            }
        };
        return response_error;
    }

    public Response.Listener<JSONObject> newResponseRequesrtpostedvehicledelete() {
        Response.Listener<JSONObject> response = new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {


                    String StatusValue = response.getString("StatusValue");

                    if (StatusValue.equalsIgnoreCase("Success")) {

                        AppController.showToast(mContext, "Vendor Status Changed successfully");

                        mContext.onBackPressed();

                    } else {

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                mList.remove(poslist);
                notifyDataSetChanged();


                AppController.spinnerStop();

            }
        };
        return response;
    }
    public String parseDateToddMMyyyy(String time) {
        String inputPattern = "dd-MM-yyyy HH:mm:ss";
        String outputPattern = "dd-MMM-yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }


}