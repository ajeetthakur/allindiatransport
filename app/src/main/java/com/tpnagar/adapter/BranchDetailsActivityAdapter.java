package com.tpnagar.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import androidx.fragment.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
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
import com.tpnagar.SearchDetailsBranchNewActivity;
import com.tpnagar.wrapper.BranchWrapper;
import com.tpnagar.wrapper.DisposWrapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;


public class BranchDetailsActivityAdapter extends BaseAdapter {
    private FragmentActivity mContext;
    private ArrayList<BranchWrapper> mList;
    private LayoutInflater mLayoutInflater = null;
    Dialog dialog;
    String disposId="";
    ListView mCompleteListView;
    int poslist;
    String Itemid;
    boolean isActivity=false;
    ArrayList<DisposWrapper> disposWrapperlist;
    public BranchDetailsActivityAdapter(FragmentActivity context, ArrayList<BranchWrapper> list) {
        mContext = context;
        mList = list;
        mLayoutInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    public BranchDetailsActivityAdapter(FragmentActivity context, ArrayList<BranchWrapper> list, boolean yes) {
        mContext = context;
        mList = list;
        mLayoutInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        isActivity=true;
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
            v = li.inflate(R.layout.custom_branch_layout, null);
            viewHolder = new CompleteListViewHolder(v);
            v.setTag(viewHolder);
        } else {
            viewHolder = (CompleteListViewHolder) v.getTag();
        }

        viewHolder.contact_person.setText(mList.get(position).getContact_Person());
        String strings=mList.get(position).getMobile_No();
        String[] item = strings.split(",");
        viewHolder.mobile.setText(item[item.length-1]);
        viewHolder.city.setText(mList.get(position).getCity_Name());
        viewHolder.state.setText(mList.get(position).getState_Name());
        viewHolder. detail.setVisibility(View.VISIBLE);
        viewHolder. settingImage.setVisibility(View.GONE);

        if (mList.get(position).getMainCompany()){
            viewHolder.typofBranch.setText("HEAD OFFICE");
            viewHolder.rel_main.setBackgroundColor(Color.RED);
            viewHolder.city.setTextColor(Color.WHITE);
            viewHolder.state.setTextColor(Color.WHITE);
            viewHolder.contact_person.setTextColor(Color.WHITE);
            viewHolder.mobile.setTextColor(Color.WHITE);
            viewHolder.typofBranch.setVisibility(View.VISIBLE);
            viewHolder.settingImage.setImageResource(R.drawable.ic_more_white);
        }else if(mList.get(position).getCanEditBranch().equalsIgnoreCase("true")){
            viewHolder.rel_main.setBackgroundColor(Color.RED);
            viewHolder.city.setTextColor(Color.WHITE);
            viewHolder.state.setTextColor(Color.WHITE);
            viewHolder.contact_person.setTextColor(Color.WHITE);
            viewHolder.mobile.setTextColor(Color.WHITE);
            viewHolder.settingImage.setImageResource(R.drawable.ic_more_white);
            viewHolder.typofBranch.setText("ADMIN");
            viewHolder.typofBranch.setVisibility(View.VISIBLE);

        } else {
            viewHolder.typofBranch.setVisibility(View.GONE);
            viewHolder.city.setTextColor(Color.BLACK);
            viewHolder.state.setTextColor(Color.BLACK);
            viewHolder.contact_person.setTextColor(Color.BLACK);
            viewHolder.mobile.setTextColor(Color.BLACK);
            viewHolder.rel_main.setBackgroundColor(Color.WHITE);
            viewHolder.settingImage.setImageResource(R.drawable.ic_more_black);
        }


        viewHolder.detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                    Intent intent=new Intent(mContext, SearchDetailsBranchNewActivity.class);
                    intent.putExtra("id", positionNew);
                    mContext.startActivity(intent);


                /*JSONObject obj = new JSONObject();

                try {
                    obj.put("LoginId", "");
                    AppController.spinnerStart(mContext);
                    Log.e("this is obj:::", obj.toString());

                    poslist=position;
                    Itemid=mList.get(position).getId();
                    servicedisposRequest(obj);


                } catch (JSONException e) {
                    e.printStackTrace();
                }*/
            }
        });


       // viewHolder.rate_number.setText(mList.get(position).getRating());
       // viewHolder.text_call.setText(mList.get(position).getPrimary_Phone());
      //  viewHolder.text_address.setText(mList.get(position).getAddress());
      //  viewHolder.ratingbar.setRating(Float.parseFloat(mList.get(position).getRating()));

        return v;
    }


    class CompleteListViewHolder {
        TextView contact_person,mobile,state,city,detail,typofBranch;
        RelativeLayout rel_main;
        ImageView settingImage;

        public CompleteListViewHolder(View base) {


            detail=  base.findViewById(R.id.detail);
            settingImage=  base.findViewById(R.id.settingImage);
            rel_main=  base.findViewById(R.id.rel_main);
            contact_person = base.findViewById(R.id.contact_person);
            mobile =  base.findViewById(R.id.mobile);
            state = base.findViewById(R.id.state);
            city = base.findViewById(R.id.city);
            detail = base.findViewById(R.id.detail);
            typofBranch= base.findViewById(R.id.typofBranch);
           // view = (TextView) base.findViewById(R.id.view);
          //  delete.setText("Details");
           /// view.setVisibility(View.GONE);


        }
    }

    private void servicedisposRequest(JSONObject params ) {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, Const.URL_Get_Disposition_List+"5",
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
                        //dialog.setTitle("Are you want delete Vehicle");

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

                                      /*  "CompanyId": 1,
                                                "ServiceId": "sample string 2",
                                                "StateId": "sample string 3",
                                                "Cities": "sample string 4",
                                                "Disposition_Id": 5,
                                                "Notes": "sample string 6",
                                                "UserId": 1
                                        */

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

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, Const.URL_deletebranch,
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


}