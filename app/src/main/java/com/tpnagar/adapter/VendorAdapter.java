package com.tpnagar.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.tpnagar.AppController;
import com.tpnagar.Const;
import com.tpnagar.R;
import com.tpnagar.fragment.CompanydetailsbypostFragment;
import com.tpnagar.fragment.VendorListFragment;
import com.tpnagar.wrapper.DisposWrapper;
import com.tpnagar.wrapper.VendorCompanyWrapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;


public class VendorAdapter extends BaseAdapter {
    private Activity mContext;
    private ArrayList<VendorCompanyWrapper> mList;
    private LayoutInflater mLayoutInflater = null;
    Dialog dialog;
    String disposId = "";
    ListView mCompleteListView;
    int poslist;
    String Itemid;
    FragmentTransaction transaction;
    ArrayList<DisposWrapper> disposWrapperlist;

   VendorListFragment vendorListFragment;

    public VendorAdapter(Activity context, ArrayList<VendorCompanyWrapper> list, FragmentTransaction fragmentTransaction,VendorListFragment mvendorListFragment) {
        mContext = context;
        mList = list;
       vendorListFragment =mvendorListFragment;
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
            v = li.inflate(R.layout.custom_vendor_layout, null);
            viewHolder = new CompleteListViewHolder(v);
            v.setTag(viewHolder);
        } else {
            viewHolder = (CompleteListViewHolder) v.getTag();
        }
        viewHolder.city_name.setText(mList.get(position).getCity_Name());
        viewHolder.company_mobile.setText(mList.get(position).getMobileNo());
        viewHolder.company_name.setText(mList.get(position).getCompanyName().toUpperCase());
        if (!mList.get(position).getHideByCompany()) {
           // viewHolder.hide_show.setText("Show");
            viewHolder.hide_show.setChecked(true);


        } else {
           // viewHolder.hide_show.setText("Hide");
            viewHolder.hide_show.setChecked(false);
        }

        viewHolder.relall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CompanydetailsbypostFragment companydetailsbypostFragment = new CompanydetailsbypostFragment();


                Bundle bundle = new Bundle();
                bundle.putString("id", ""+mList.get(position).getCompanyId());
                companydetailsbypostFragment.setArguments(bundle);
                transaction.replace(R.id.frame_container, companydetailsbypostFragment, "");
                transaction.addToBackStack(null);
                // transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                transaction.commit();

            }
        });



        viewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("Alert!").setMessage("Do you want Delete This Vendor")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                JSONObject obj = new JSONObject();
                                try {
                                    SharedPreferences prefs = mContext.getSharedPreferences("com.tpnagar", MODE_PRIVATE);
/*
                                    "id":"6",
                                            "Action":"1",
                                            "Login_Id":"5124",
                                            "App_Source":"2"*/
                                    String Action;
                                    if (mList.get(position).getHideByVendor()) {
                                        Action = "0";
                                    } else {
                                        Action = "1";
                                    }
                                    obj.put("id", mList.get(position).getId());

                                    obj.put("App_Source", "2");
                                    obj.put("Login_Id", prefs.getString("Login_Id", ""));


                                    AppController.spinnerStart(mContext);

                                    deleteRequest(obj);


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


        viewHolder.hide_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                JSONObject obj = new JSONObject();
                try {
                    SharedPreferences prefs = mContext.getSharedPreferences("com.tpnagar", MODE_PRIVATE);
/*
                                    "id":"6",
                                            "Action":"1",
                                            "Login_Id":"5124",
                                            "App_Source":"2"*/
                    String Action;
                    if (mList.get(position).getHideByCompany()) {
                        Action = "0";
                    } else {
                        Action = "1";
                    }
                    obj.put("id", mList.get(position).getId());
                    obj.put("Action", Action);
                    obj.put("App_Source", "2");
                    obj.put("Login_Id", prefs.getString("Login_Id", ""));


                    AppController.spinnerStart(mContext);

                    hideShowRequest(obj);


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });

        /* viewHolder.hide_show.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {




              //  Snackbar.make(buttonView, "Switch state checked "+isChecked, Snackbar.LENGTH_LONG)
                   //     .setAction("ACTION",null).show();
            }
        });*/

       /* viewHolder.hide_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("Alert!").setMessage("Do you want " + viewHolder.hide_show.getText().toString() + "This Vendor")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                JSONObject obj = new JSONObject();
                                try {
                                    SharedPreferences prefs = mContext.getSharedPreferences("com.tpnagar", MODE_PRIVATE);
*//*
                                    "id":"6",
                                            "Action":"1",
                                            "Login_Id":"5124",
                                            "App_Source":"2"*//*
                                    String Action;
                                    if (mList.get(position).getHideByCompany()) {
                                        Action = "0";
                                    } else {
                                        Action = "1";
                                    }
                                    obj.put("id", mList.get(position).getId());
                                    obj.put("Action", Action);
                                    obj.put("App_Source", "2");
                                    obj.put("Login_Id", prefs.getString("Login_Id", ""));


                                    AppController.spinnerStart(mContext);

                                    hideShowRequest(obj);


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
        });*/


        return v;
    }


    class CompleteListViewHolder {
        TextView company_mobile,
                city_name,
                company_name,
                delete;
        RelativeLayout relall;
        ToggleButton hide_show;

        public CompleteListViewHolder(View base) {

            relall= (RelativeLayout) base.findViewById(R.id.relall);
            city_name = (TextView) base.findViewById(R.id.city_name);
            company_mobile = (TextView) base.findViewById(R.id.company_mobile);
            company_name = (TextView) base.findViewById(R.id.company_name);
            hide_show = (ToggleButton) base.findViewById(R.id.hide_show);
            delete= (TextView) base.findViewById(R.id.delete);

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

                       // mContext.onBackPressed();
                         vendorListFragment.updateMethod();

                    } else {

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

              //  mList.remove(poslist);
               // notifyDataSetChanged();


                AppController.spinnerStop();

            }
        };
        return response;
    }
    private void deleteRequest(JSONObject params) {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, Const.URL_DELETEVENDOR,
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

}