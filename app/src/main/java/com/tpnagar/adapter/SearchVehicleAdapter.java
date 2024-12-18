package com.tpnagar.adapter;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
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
import com.tpnagar.fragment.CompanydetailsbypostFragment;
import com.tpnagar.wrapper.DisposWrapper;
import com.tpnagar.wrapper.PostVehicleWrapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;


public class SearchVehicleAdapter extends BaseAdapter {
    private FragmentActivity mContext;
    private ArrayList<PostVehicleWrapper> mList;
    private LayoutInflater mLayoutInflater = null;
    Dialog dialog;
    String disposId="";
    ListView mCompleteListView;
    int poslist;
    String Itemid;
    ArrayList<DisposWrapper> disposWrapperlist;
    public SearchVehicleAdapter(FragmentActivity context, ArrayList<PostVehicleWrapper> list) {
        mContext = context;
        mList = list;
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
       CompleteListViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater li = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = li.inflate(R.layout.custom_postvehicle_layout_search, null);
            viewHolder = new CompleteListViewHolder(v);
            v.setTag(viewHolder);
        } else {
            viewHolder = (CompleteListViewHolder) v.getTag();
        }


      /*  viewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                JSONObject obj = new JSONObject();

                try {
                    obj.put("LoginId", "");
                    AppController.spinnerStart(mContext);
                    Log.e("this is obj:::", obj.toString());

                    poslist=position;
                    Itemid=mList.get(position).getId();
                    servicedisposRequest(obj);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });*/


        String myString=mList.get(position).getCompName().toString().toUpperCase();
        //String upperString = myString.substring(0,1).toUpperCase() + myString.substring(1);



        viewHolder.company_name.setTextColor(Color.RED);
        viewHolder.company_name.setTypeface(null, Typeface.BOLD);
        viewHolder.company_name.setText(myString);
        //viewHolder.company_name.setText(mList.get(position).getCompName());
     viewHolder.vehicle_number.setText(mList.get(position).getVehicle_No());
        viewHolder.discription.setText(mList.get(position).getDescription());
        viewHolder.vehicle_details.setText(mList.get(position).getVehicle_Detail());

       viewHolder.text_c_p.setText(mList.get(position).getContact_Person());
        viewHolder.text_call.setText(mList.get(position).getMobile_No());
        viewHolder.text_call.setTypeface(null, Typeface.BOLD);
        viewHolder.date_text.setText(mList.get(position).getCreated_On().substring(0,10));
       // viewHolder.text_c_p.setText(mList.get(position).getContact_Person());
        viewHolder.text_call.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                onCall(mList.get(position).getMobile_No());
                return false;
            }
        });
        viewHolder.company_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle=new Bundle();


                bundle.putString("id",mList.get(position).getCompany_Id());



                CompanydetailsbypostFragment companydetailsbypostFragment=new CompanydetailsbypostFragment();
                companydetailsbypostFragment.setArguments(bundle);
                replaceFragmentTask(companydetailsbypostFragment);
            }
        });

        viewHolder.from.setText("From: "+mList.get(position).getFrom_City_Name()+"("+mList.get(position).getFrom_State_Name()+")");
   viewHolder.to.setText("To: "+mList.get(position).getTo_City_Name()+"("+mList.get(position).getTo_State_Name()+")");

        return v;
    }


class CompleteListViewHolder {
    TextView date_text,text_c_p,company_name,text_call,text_website,from,to,vehicle_details,vehicle_number,discription;

    ImageView delete;
    public CompleteListViewHolder(View base) {


        company_name = (TextView) base.findViewById(R.id.company_name);
        text_c_p = (TextView) base.findViewById(R.id.text_c_p);
        text_call = (TextView) base.findViewById(R.id.text_call);
        text_website = (TextView) base.findViewById(R.id.text_website);
        date_text = (TextView) base.findViewById(R.id.date_text);

        from = (TextView) base.findViewById(R.id.from);
        to = (TextView) base.findViewById(R.id.to);
        vehicle_details = (TextView) base.findViewById(R.id.vehicle_details);
        vehicle_number = (TextView) base.findViewById(R.id.vehicle_number);
        discription = (TextView) base.findViewById(R.id.discription);
        delete = (ImageView) base.findViewById(R.id.delete);
        company_name.setVisibility(View.VISIBLE);
        delete.setVisibility(View.GONE);

    }
    }


    private void servicedisposRequest(JSONObject params ) {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, Const.URL_Get_Disposition_List+"2",
                params, newResponseRequesrtpostedvehiclelist(), eErrorListenerRequesrtpostedvehiclelist()) {
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

    private Response.ErrorListener eErrorListenerRequesrtpostedvehiclelist() {
        Response.ErrorListener response_error = new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("this is error", error.toString());


                AppController.spinnerStop();
            }
        };
        return response_error;
    }

    public Response.Listener<JSONObject> newResponseRequesrtpostedvehiclelist() {
        Response.Listener<JSONObject> response = new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
              disposWrapperlist = new ArrayList<DisposWrapper>();
                Log.e("ServiceListOnCityAn", response.toString());

                try {
                    String StatusValue = response.getString("StatusValue");

                    if (StatusValue.equalsIgnoreCase("Success"))


                    {
                        JSONArray jsonArray = response.getJSONArray("Data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            DisposWrapper disposWrapper = new DisposWrapper();
                            JSONObject obj = jsonArray.getJSONObject(i);
                            disposWrapper.setDisp_Category(obj.getString("Disp_Category"));
                            disposWrapper.setDisp_Name(obj.getString("Disp_Name"));
                            disposWrapper.setId(obj.getString("Id"));


                            disposWrapperlist.add(disposWrapper);

                        }

                        // Create custom dialog object
                        dialog = new Dialog(mContext,R.style.FullHeightDialog);
                        // Include dialog.xml file
                        dialog.setContentView(R.layout.dialoglist_dispos);
                        mCompleteListView = (ListView) dialog.findViewById(R.id.completeList);


                        TextView textupr= (TextView) dialog.findViewById(R.id.textupr);
                        textupr.setText("Reason for deletion?");

                        // Set dialog title
                       // dialog.setTitle("Are you want delete Vehicle");

                        mCompleteListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                                disposId=disposWrapperlist.get(pos).getId();
                            }
                        });
                        dialog.show();




                        TextView declineButton = (TextView) dialog.findViewById(R.id.declineButton);
                        // if decline button is clicked, close the custom dialog
                        declineButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // Close dialog
                                dialog.dismiss();
                            }
                        });

                        TextView deleteButton = (TextView) dialog.findViewById(R.id.delete);
                        // if decline button is clicked, close the custom dialog
                        deleteButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // Close dialog
if(disposId.length()>0) {
    SharedPreferences prefs = mContext.getSharedPreferences("com.tpnagar", MODE_PRIVATE);
    dialog.dismiss();
    JSONObject obj = new JSONObject();
    String snote = "";
    EditText note = (EditText) dialog.findViewById(R.id.note);
    if (note.getText().toString().length() > 0) {
        snote = note.getText().toString();
    } else {
        snote = "";
    }
    AppController.spinnerStart(mContext);
    try {
        obj.put("Id", Itemid);
        obj.put("Disposition_Id", disposId);
        obj.put("Notes", snote);
        obj.put("UserId", prefs.getString("Login_Id", ""));
        AppController.spinnerStart(mContext);
        Log.e("this is obj:::", obj.toString());
        servicedeleteRequest(obj);

    } catch (JSONException e) {
        e.printStackTrace();
    }

}else {
    AppController.showToast(mContext,"Please select a reason for delete");
}

                            }
                        });

                        DisposAdapter mListAdapter = new DisposAdapter(mContext, disposWrapperlist);
                        mCompleteListView.setAdapter(mListAdapter);
                        //  DataManager.getInstance().setCitiesOfStateWrapper(citiesOfStateWrapperlist);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                AppController.spinnerStop();

            }
        };
        return response;
    }


    private void servicedeleteRequest(JSONObject params) {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, Const.URL_deletepostedvehicle,
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

                if (StatusValue.equalsIgnoreCase("Success")){

                    AppController.showToast(mContext,"Vehicle Delete successfully");

                }else {

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
    public void onCall(String number) {
        int permissionCheck = ContextCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(mContext, new String[]{Manifest.permission.CALL_PHONE}, 1);
        } else {
            mContext.startActivity(new Intent(Intent.ACTION_CALL).setData(Uri.parse("tel:"+ number)));
        }
    }
    private void replaceFragmentTask(Fragment fragment) {
        FragmentTransaction transaction = mContext.getSupportFragmentManager().beginTransaction();
      /*  transaction.setCustomAnimations(
                R.anim.slide_in_from_right, R.anim.slide_out_to_left,
                R.anim.slide_in_from_left, R.anim.slide_out_to_right);*/
        transaction.replace(R.id.frame_container, fragment, "");
        transaction.addToBackStack(null);
        // transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.commit();
    }
}