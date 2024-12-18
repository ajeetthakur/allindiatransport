package com.tpnagar.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
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
import com.tpnagar.wrapper.DisposWrapper;
import com.tpnagar.wrapper.PostVehicleWrapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;


public class PostVehicleAdapter extends BaseAdapter {
    private Activity mContext;
    private ArrayList<PostVehicleWrapper> mList;
    private LayoutInflater mLayoutInflater = null;
    Dialog dialog;
    String disposId="";
    ListView mCompleteListView;
    int poslist,delpos=0;
    String Itemid;
    ArrayList<DisposWrapper> disposWrapperlist;
    public PostVehicleAdapter(Activity context, ArrayList<PostVehicleWrapper> list) {
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
            v = li.inflate(R.layout.custom_postvehicle_layout, null);
            viewHolder = new CompleteListViewHolder(v);
            v.setTag(viewHolder);
        } else {
            viewHolder = (CompleteListViewHolder) v.getTag();
        }


        viewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("Alert!").setMessage("Do you want delete this Vehicle")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                SharedPreferences prefs = mContext.getSharedPreferences("com.tpnagar", MODE_PRIVATE);
                                JSONObject obj = new JSONObject();

                                try {
                                    obj.put("Id",mList.get(position).getId());

                                    obj.put("UserId", prefs.getString("Login_Id", ""));
                                    AppController.spinnerStart(mContext);
                                    Log.e("this is obj:::", obj.toString());
                                    delpos=position;
                                    servicedeleteRequest(obj);

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

      viewHolder.vehicle_number.setText(mList.get(position).getVehicle_No());
        viewHolder.discription.setText(mList.get(position).getDescription());
        viewHolder.vehicle_details.setText(mList.get(position).getVehicle_Detail());

        String CName=mList.get(position).getCompName();
        CName=CName.toUpperCase();
        viewHolder.company_name.setText(CName);

        viewHolder.company_name.setTextColor(Color.RED);
        viewHolder.company_name.setTypeface(null, Typeface.BOLD);
       viewHolder.text_mail.setText(mList.get(position).getContact_Person());
        viewHolder.text_call.setText(mList.get(position).getMobile_No());
       // viewHolder.text_website.setText(mList.get(position).get());
        viewHolder.from.setText("From: "+mList.get(position).getFrom_City_Name()+"("+mList.get(position).getFrom_State_Name()+")");
   viewHolder.to.setText("To: "+mList.get(position).getTo_City_Name()+"("+mList.get(position).getTo_State_Name()+")");

        return v;
    }


class CompleteListViewHolder {
    TextView company_name,text_mail,text_call,text_website,from,to,vehicle_details,vehicle_number,discription;

ImageView delete;
    public CompleteListViewHolder(View base) {


        company_name = (TextView) base.findViewById(R.id.company_name);
        text_mail = (TextView) base.findViewById(R.id.text_mail);
        text_call = (TextView) base.findViewById(R.id.text_call);
        text_website = (TextView) base.findViewById(R.id.text_website);


        from = (TextView) base.findViewById(R.id.from);
        to = (TextView) base.findViewById(R.id.to);
        vehicle_details = (TextView) base.findViewById(R.id.vehicle_details);
        vehicle_number = (TextView) base.findViewById(R.id.vehicle_number);
        discription = (TextView) base.findViewById(R.id.discription);
        delete = (ImageView) base.findViewById(R.id.delete);

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

                mList.remove(delpos);
                notifyDataSetChanged();


                AppController.spinnerStop();

            }
        };
        return response;
    }


}