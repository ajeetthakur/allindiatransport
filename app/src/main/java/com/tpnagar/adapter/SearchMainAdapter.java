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
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
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
import com.tpnagar.fragment.CompanydetailsbypostFragment;
import com.tpnagar.wrapper.SearchCompanyWrapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import static android.content.Context.MODE_PRIVATE;


public class SearchMainAdapter extends BaseAdapter {
    private Activity mContext;
    private ArrayList<SearchCompanyWrapper> mList;
    private LayoutInflater mLayoutInflater = null;
    FragmentTransaction transaction;
    Dialog dialog;
    ListView mCompleteListView;
    ArrayList<String> strings;

    public SearchMainAdapter(Activity context, ArrayList<SearchCompanyWrapper> list, FragmentTransaction fragmentTransaction) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v = convertView;
        final int positionNew = position;
        final CompleteListViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater li = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = li.inflate(R.layout.custom_search_layout, null);
            viewHolder = new CompleteListViewHolder(v);
            v.setTag(viewHolder);
        } else {
            viewHolder = (CompleteListViewHolder) v.getTag();
        }
        viewHolder.details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                CompanydetailsbypostFragment companydetailsbypostFragment = new CompanydetailsbypostFragment();


                Bundle bundle = new Bundle();
                bundle.putString("id", mList.get(positionNew).getCompany_Id());
                companydetailsbypostFragment.setArguments(bundle);
                transaction.replace(R.id.frame_container, companydetailsbypostFragment, "");
                transaction.addToBackStack(null);
                // transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                transaction.commit();




       /*         Bundle bundle=new Bundle();
                bundle.putInt("id",positionNew);



                SearchDetailsFragment searchDetailsFragment=new SearchDetailsFragment();
                searchDetailsFragment.setArguments(bundle);
                transaction.replace(R.id.frame_container, searchDetailsFragment, "");
                transaction.addToBackStack(null);
                // transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                transaction.commit();*/

            }
        });

        String myString = mList.get(position).getCompany_Name();
        String upperString = myString.substring(0, 1).toUpperCase() + myString.substring(1);


        viewHolder.company_name.setText(upperString.toUpperCase());
        viewHolder.company_name.setTextColor(Color.RED);
        viewHolder.company_name.setTypeface(null, Typeface.BOLD);
        viewHolder.rate_number.setText(mList.get(position).getRating() + " Votes");
        // viewHolder.text_call.setText(mList.get(position).getPrimary_Phone());
        viewHolder.text_address.setText(mList.get(position).getAddress() + ", " + mList.get(position).getCity_Name().replace("null", "") + ", " + mList.get(position).getState_Name().replace("null", ""));
        viewHolder.ratingbar.setRating(Float.parseFloat(mList.get(position).getRating()));

        strings = new ArrayList<String>();

        String newAllNumber = mList.get(position).getPrimary_Phone().replaceAll("|", "~");

        String s = mList.get(position).getPrimary_Phone();


        String[] value_split = s.split(Pattern.quote("|"));




        strings = new ArrayList<String>();

        String substrMobile = value_split[0];
        String substrPhone = value_split[1];
        String substrPR = value_split[2];
        String[] substrMobileArray = substrMobile.split(Pattern.quote("-"));
        String[] substrPhoneArray = substrPhone.split(Pattern.quote("-"));
        String[] substrPRArray = substrPR.split(Pattern.quote("-"));


        /*if (substrMobileArray.length > 0) {
            viewHolder.text_call.setText(substrMobileArray[0].replace("M:", "").replace("NA", ""));
        } else if (substrPhoneArray.length > 0) {
            viewHolder.text_call.setText(substrPhoneArray[0].replace("P:", "").replace("NA", ""));

        } else {
            viewHolder.text_call.setText(substrPRArray[0].replace("PR:", "").replace("NA", ""));

        }*/

        String s1 = mList.get(position).getTPNagar_Trusted();

        Log.e("Tested", "" + s1);

        if (mList.get(position).getService_Type().equalsIgnoreCase("2")||mList.get(position).getService_Type().equalsIgnoreCase("3")) {
            viewHolder.verified.setVisibility(View.VISIBLE);
        } else {
            viewHolder.verified.setVisibility(View.GONE);
        }

/*if(mList.get(position).getTPNagar_Trusted().equalsIgnoreCase("1")){
    viewHolder.verified.setVisibility(View.VISIBLE);
}else {
    viewHolder.verified.setVisibility(View.GONE);
}*/
        viewHolder.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                strings = new ArrayList<String>();
                String s = mList.get(position).getPrimary_Phone();


                String[] value_split = s.split(Pattern.quote("|"));
                String substrMobile = value_split[0];
                String substrPhone = value_split[1];
                String substrPR = value_split[2];
                String[] substrMobileArray = substrMobile.split(Pattern.quote("-"));
                String[] substrPhoneArray = substrPhone.split(Pattern.quote("-"));
                String[] substrPRArray = substrPR.split(Pattern.quote("-"));


                /*if (substrMobileArray.length > 0) {
                    viewHolder.text_call.setText(substrMobileArray[0].replace("M:", "").replace("NA", ""));
                } else if (substrPhoneArray.length > 0) {
                    viewHolder.text_call.setText(substrPhoneArray[0].replace("P:", "").replace("NA", ""));

                } else {
                    viewHolder.text_call.setText(substrPRArray[0].replace("PR:", "").replace("NA", ""));

                }
*/
                if (substrMobileArray.length > 0) {

                    for (int i = 0; i < substrMobileArray.length; i++) {

                        String s1 = substrMobileArray[i].replace("M:", "").replace("NA", "");
                        if (s1.length() > 0) {
                            strings.add(s1);
                        }
                    }
                }
                if (substrPhoneArray.length > 0) {

                    for (int i = 0; i < substrPhoneArray.length; i++) {

                        String s1 = substrPhoneArray[i].replace("P:", "").replace("NA", "");
                        if (s1.length() > 0) {
                            strings.add(s1);
                        }

                    }
                }
                if (substrPRArray.length > 0) {

                    for (int i = 0; i < substrPRArray.length; i++) {

                        String s1 = substrPRArray[i].replace("PR:", "").replace("NA", "");
                        if (s1.length() > 0) {
                            strings.add(s1);
                        }


                    }
                }


                dialog = new Dialog(mContext, R.style.FullHeightDialog);
                // Include dialog.xml file
                dialog.setContentView(R.layout.dialoglist_phone);
                mCompleteListView = (ListView) dialog.findViewById(R.id.completeList);
                // PhoneDetailsAdapter mListAdapter = new PhoneDetailsAdapter(mContext, strings);
                // mCompleteListView.setAdapter(mListAdapter);
                TextView declineButton = (TextView) dialog.findViewById(R.id.declineButton);
                // if decline button is clicked, close the custom dialog
                declineButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Close dialog
                        dialog.dismiss();


                    }
                });
                dialog.show();
                PhoneDetailsAdapter mListAdapter = new PhoneDetailsAdapter(mContext, strings);
                mCompleteListView.setAdapter(mListAdapter);
          /*      mCompleteListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                        onCall(strings.get(i));

                    }
                });*/
            }
        });

        viewHolder.text_call.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                String s = mList.get(position).getPrimary_Phone();


                String[] value_split = s.split(Pattern.quote("|"));
                String substrMobile = value_split[0];
                String substrPhone = value_split[1];
                String substrPR = value_split[2];
                String[] substrMobileArray = substrMobile.split(Pattern.quote("-"));
                String[] substrPhoneArray = substrPhone.split(Pattern.quote("-"));
                String[] substrPRArray = substrPR.split(Pattern.quote("-"));

String call_num="";
               if (substrMobileArray.length > 0) {
                   call_num=substrMobileArray[0].replace("M:", "").replace("NA", "");
                } else if (substrPhoneArray.length > 0) {
                   call_num=substrPhoneArray[0].replace("P:", "").replace("NA", "");

                } else {
                   call_num=substrPRArray[0].replace("PR:", "").replace("NA", "");

                }

                onCall(call_num);
                return false;
            }
        });

        viewHolder.text_vendor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("Alert!").setMessage("Do you want add company as Vendor")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                JSONObject obj = new JSONObject();
                                SharedPreferences prefs = mContext.getSharedPreferences("com.tpnagar", MODE_PRIVATE);

                                try {
                                    obj.put("Company_Id", prefs.getString("Company_Id", ""));
                                    obj.put("Vendor_Id", mList.get(positionNew).getCompany_Id());
                                    obj.put("Login_Id", prefs.getString("Login_Id", ""));
                                    obj.put("App_Source", "2");


                                    AppController.spinnerStart(mContext);


                                    addVendorRequest(obj);


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
        // viewHolder.ratingbar.setRating(Float.parseFloat("2.5"));

        // Drawable progress = viewHolder.ratingbar.getProgressDrawable();
        // DrawableCompat.setTint(progress, Color.RED);
        return v;
    }


    class CompleteListViewHolder {
        ImageView sms, verified;
        TextView more, company_name, rate_number, text_call, text_address, details,text_vendor;
        RatingBar ratingbar;

        public CompleteListViewHolder(View base) {

            text_vendor= (TextView) base.findViewById(R.id.text_vendor);
            details = (TextView) base.findViewById(R.id.details);
            verified = (ImageView) base.findViewById(R.id.verified);
            sms = (ImageView) base.findViewById(R.id.sms);
            company_name = (TextView) base.findViewById(R.id.company_name);
            rate_number = (TextView) base.findViewById(R.id.rate_number);
            text_call = (TextView) base.findViewById(R.id.text_call);
            text_address = (TextView) base.findViewById(R.id.text_address);
            ratingbar = (RatingBar) base.findViewById(R.id.ratingbar);
            more = (TextView) base.findViewById(R.id.more);
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


    private void addVendorRequest(JSONObject params) {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, Const.URL_ADDVENDOR,
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

                        AppController.showToast(mContext, response.getString("Desc"));

                       // mContext.onBackPressed();

                    } else {
                        AppController.showToast(mContext, response.getString("Desc"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                AppController.spinnerStop();

            }
        };
        return response;
    }

}