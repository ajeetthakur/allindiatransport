package com.tpnagar.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.widget.PopupMenu;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
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
import com.tpnagar.BaseContainerFragment;
import com.tpnagar.ConnectionDetector;
import com.tpnagar.Const;
import com.tpnagar.R;
import com.tpnagar.adapter.NotificationCompanyAdapter;
import com.tpnagar.wrapper.GetComapnyServiceWrapper;
import com.tpnagar.wrapper.NotificationCompnay;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class NotificationCompanyListFragment extends BaseContainerFragment {
    SharedPreferences prefs = null;
    ConnectionDetector cd;
    ListView listview;
    FragmentTransaction transaction;
    View rootView;
    ImageView notification_icon;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.notification_list, container, false);


        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initXmlViews(view);
        transaction = getActivity().getSupportFragmentManager().beginTransaction();
        TextView count=getActivity().findViewById(R.id.count);
        count.setVisibility(View.GONE);

          notification_icon=getActivity().findViewById(R.id.notification_icon);

        notification_icon.setImageResource(R.drawable.ic_more_white);
        notification_icon.setVisibility(View.VISIBLE);
        notification_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {


                    PopupMenu popup = new PopupMenu(getActivity(), notification_icon);
                    //Inflating the Popup using xml file

                    if(!prefs.getBoolean("NotificationOnOff",true)){
                        popup.getMenu().add("Notification On");
                    }else {
                        popup.getMenu().add("Notification Off");
                    }

                    popup.getMenu().add("Block List");

                    // popup.getMenuInflater().inflate(R.menu.poupup_menu_reg_title, popup.getMenu());

                    //registering popup with OnMenuItemClickListener
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {


                            if (item.getTitle().toString().equalsIgnoreCase("Block List")) {

                                transaction.replace(R.id.frame_container, new NotificationBlockCompanyListFragment(), "");
                                transaction.addToBackStack(null);
                                // transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                                transaction.commit();

                            }


                            else  {

                                JSONObject obja = new JSONObject();

                                try {
                                    String RequestType = "";

                                    if (prefs.getBoolean("NotificationOnOff", true)) {
                                        RequestType = "0";
                                    } else {
                                        RequestType = "1";
                                    }

                                    String CompanyId = prefs.getString("Company_Id", "");
                                    obja.put("RequestType", RequestType);
                                    obja.put("CompanyId", CompanyId);


                                    AppController.spinnerStart(getActivity());

                                    Log.e("this is obj:::", obja.toString());


                                    noTificationOnOffRequest(obja);


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            return true;
                        }
                    });

                    popup.show();

                }catch (Exception e){

                }



            }
        });




    }

    @Override
    public void onStop() {
        super.onStop();

        notification_icon=getActivity().findViewById(R.id.notification_icon);

        notification_icon.setImageResource(R.drawable.ic_notifications_black_24dp);
    }

    private void initXmlViews(View view) {
        cd = new ConnectionDetector(getActivity());



        prefs = getActivity().getSharedPreferences("com.tpnagar", MODE_PRIVATE);



        listview=(ListView) view.findViewById(R.id.listview);


               prefs = getActivity().getSharedPreferences("com.tpnagar", MODE_PRIVATE);
        if(prefs.getString("IsActive","").equalsIgnoreCase("yes"))
        {
            ((com.tpnagar.designdemo.MainActivity) getActivity())
                    .getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#FFFFFF\">" +"Companies Notifications" + "</font>")));

        }else {
            ((com.tpnagar.designdemo.MainActivityWithoutLogin) getActivity())
                    .getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#FFFFFF\">" +"Companies Notifications" + "</font>")));

        }


        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        getNotificationList();



    }



    public void replaceFragmentTask(Fragment fragment) {

      /*  transaction.setCustomAnimations(
                R.anim.slide_in_from_right, R.anim.slide_out_to_left,
                R.anim.slide_in_from_left, R.anim.slide_out_to_right);*/
        transaction.replace(R.id.frame_container, fragment, "");
        transaction.addToBackStack(null);
        // transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.commit();
    }

    void getNotificationList(){

        JSONObject obj = new JSONObject();
        try {
            obj.put("Password", "prem$123");
            //  AppController.spinnerStart(getActivity());

            Log.e("this is obj:::",obj.toString());

       String Company_Id=prefs.getString("Company_Id","");
            String Login_Id=prefs.getString("Login_Id","");
            notificationListRequest(obj,Login_Id);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void notificationListRequest(JSONObject params,String Company_Id) {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, Const.URL_notificationlistbycompany+Company_Id,
                params, newFromCityResponseRequesrSearch(), eErrorListenerRequesrSearch()) {
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

        AppController.getInstance().addToRequestQueue(jsonObjReq, "notification");
    }

    private Response.ErrorListener eErrorListenerRequesrSearch() {
        Response.ErrorListener response_error = new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("this is error",error.toString());


              //  AppController.spinnerStop();
            }
        };
        return response_error;
    }

    private Response.Listener<JSONObject> newFromCityResponseRequesrSearch() {
        Response.Listener<JSONObject> response = new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                Log.e("NotificationList", response.toString());


                ArrayList<NotificationCompnay> notificationsWrappers=new ArrayList<>();

                try {
                    String StatusValue = response.getString("StatusValue");

                    if (StatusValue.equalsIgnoreCase("Success")) {
                        JSONArray jsonArray = response.getJSONArray("Data");
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject obj = jsonArray.getJSONObject(i);

                            NotificationCompnay notificationsWrapper=new NotificationCompnay();

                            notificationsWrapper.setCompanyId(obj.getString("CompanyId"));
                            notificationsWrapper.setCompanyName(obj.getString("CompanyName"));
                            notificationsWrapper.setBlocked(obj.getBoolean("isBlocked"));
                            notificationsWrapper.setUnReadMsgCount(obj.getInt("UnReadMsgCount"));

                            notificationsWrappers.add(notificationsWrapper);

                        }

                        NotificationCompanyAdapter mListAdapter = new NotificationCompanyAdapter(getActivity(), notificationsWrappers,transaction,NotificationCompanyListFragment.this);
                        listview.setAdapter(mListAdapter);



                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //  AppController.spinnerStop();

            }
        };
        return response;
    }


    private Response.ErrorListener eErrorListenerRequesrBlock() {
        Response.ErrorListener response_error = new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("this is error",error.toString());


                  AppController.spinnerStop();
            }
        };
        return response_error;
    }


    private Response.Listener<JSONObject> onResponseRequesrDeleteComanapy() {
        Response.Listener<JSONObject> response = new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                Log.e("NotificationList", response.toString());


                AppController.spinnerStop();
                ArrayList<NotificationCompnay> notificationsWrappers=new ArrayList<>();

                try {
                    String StatusValue = response.getString("StatusValue");

                    if (StatusValue.equalsIgnoreCase("Success")) {

                       // AppController.showToast(getActivity(),response.getString("Desc"));

                    }else {
                      //  AppController.showToast(getActivity(),response.getString("Desc"));

                    }

                    getNotificationList();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //  AppController.spinnerStop();

            }
        };
        return response;
    }
    private void noTificationOnOffRequest(JSONObject params) {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, Const.URL_onoff,
                params, newResponseRequesrtOnOff(), eErrorListenerRequesrBlock()) {
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
    public Response.Listener<JSONObject> newResponseRequesrtOnOff() {
        Response.Listener<JSONObject> response = new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                ArrayList<GetComapnyServiceWrapper> getComapnyServiceWrapperlist = new ArrayList<GetComapnyServiceWrapper>();
                Log.e("ServiceListOnCityAn", response.toString());
                ArrayList<String> arrayList1 = new ArrayList<String>();
                try {
                    String StatusValue = response.getString("StatusValue");

                    AppController.showToast(getActivity(),response.getString("Desc"));

                    if(prefs.getBoolean("NotificationOnOff",true)){
                        prefs.edit().putBoolean("NotificationOnOff",false).commit();
                    }else {
                        prefs.edit().putBoolean("NotificationOnOff",true).commit();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


                AppController.spinnerStop();

            }
        };
        return response;
    }
    public void notificationReadRequest(JSONObject params) {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, Const.URL_markasreadcompany,
                params, onResponseRequesrDeleteComanapy(), eErrorListenerRequesrSearch()) {
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

        AppController.getInstance().addToRequestQueue(jsonObjReq, "notification");
    }
}
