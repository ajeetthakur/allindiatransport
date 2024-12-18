package com.tpnagar.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import androidx.fragment.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
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
import com.tpnagar.fragment.VendorAllowedBranchListFragment;
import com.tpnagar.wrapper.DisposWrapper;
import com.tpnagar.wrapper.GetallowedbranchWrapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class VendorAllowedBranchAdapter extends BaseAdapter {
    private Activity mContext;
    private ArrayList<GetallowedbranchWrapper> mList;
    private LayoutInflater mLayoutInflater = null;
    Dialog dialog;
    String disposId = "";
    ListView mCompleteListView;
    int poslist;
    String Itemid;
    FragmentTransaction transaction;
    ArrayList<DisposWrapper> disposWrapperlist;
    VendorAllowedBranchListFragment mVendorAllowedBranchListFragment;

    public VendorAllowedBranchAdapter(Activity context, ArrayList<GetallowedbranchWrapper> list, FragmentTransaction fragmentTransaction, VendorAllowedBranchListFragment vendorAllowedBranchListFragment) {
        mContext = context;
        mList = list;
        mVendorAllowedBranchListFragment= vendorAllowedBranchListFragment;
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
            v = li.inflate(R.layout.item_row_allowed, null);
            viewHolder = new CompleteListViewHolder(v);
            v.setTag(viewHolder);
        } else {
            viewHolder = (CompleteListViewHolder) v.getTag();
        }
        viewHolder.name_branch.setText(mList.get(position).getBranchName());


        if(mList.get(position).isStatus()){
            viewHolder.checkBox.setImageResource(R.drawable.ic_check_box_black_24dp);
        }else {
            viewHolder.checkBox.setImageResource(R.drawable.ic_check_box_outline_blank_black_24dp);
        }


        viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mList.get(position).isStatus()){

                    viewHolder.checkBox.setImageResource(R.drawable.ic_check_box_outline_blank_black_24dp);

                    mVendorAllowedBranchListFragment.changeArraylist(position,false);

                }else {
                    viewHolder.checkBox.setImageResource(R.drawable.ic_check_box_black_24dp);
                    mVendorAllowedBranchListFragment.changeArraylist(position,true);


                }
            }
        });


        return v;
    }


    class CompleteListViewHolder {
        TextView name_branch;
        ImageView checkBox;

        public CompleteListViewHolder(View base) {


            name_branch = (TextView) base.findViewById(R.id.name_branch);
            checkBox = (ImageView) base.findViewById(R.id.checkbox);



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


}