package com.tpnagar.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.tpnagar.adapter.NotificationAdapter;
import com.tpnagar.wrapper.NotificationCompnay;
import com.tpnagar.wrapper.NotificationsWrapper;
import com.tpnagar.wrapper.PostGoodsWrapper;
import com.tpnagar.wrapper.PostVehicleWrapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class NotificationsListFragment extends BaseContainerFragment {
    SharedPreferences prefs = null;
    ConnectionDetector cd;
    ListView listview;
    FragmentTransaction transaction;
    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.notification_list, container, false);

        final ImageView notification_icon = getActivity().findViewById(R.id.notification_icon);

        notification_icon.setImageResource(R.drawable.ic_more_white);
        notification_icon.setVisibility(View.VISIBLE);
        notification_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PopupMenu popup = new PopupMenu(getActivity(), notification_icon);
                //Inflating the Popup using xml file

                if (!getArguments().getBoolean("isBlocked")) {
                    popup.getMenu().add("Block");
                } else {
                    popup.getMenu().add("UnBlock");
                }
                popup.getMenu().add("Delete");
                popup.getMenu().add("Company Details");

                // popup.getMenuInflater().inflate(R.menu.poupup_menu_reg_title, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {

                        if (item.getTitle().toString().equalsIgnoreCase("Delete")) {

                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setTitle("Alert!").setMessage("Do you want Delete?")
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            JSONObject obj = new JSONObject();
                                            try {
                                                SharedPreferences prefs = getActivity().getSharedPreferences("com.tpnagar", getActivity().MODE_PRIVATE);

                                                String Login_Id = prefs.getString("Login_Id", "");
                                                obj.put("LoginId", Login_Id);
                                                obj.put("CompanyId", getArguments().getString("id"));


                                                AppController.spinnerStart(getActivity());

                                                onDeleteRequestComapnay(obj);


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

                        } else if(item.getTitle().toString().equalsIgnoreCase("Company Details")){
                            CompanydetailsbypostFragment companydetailsbypostFragment = new CompanydetailsbypostFragment();


                            Bundle bundle = new Bundle();
                            bundle.putString("id", ""+getArguments().getString("id"));
                            companydetailsbypostFragment.setArguments(bundle);
                            transaction.replace(R.id.frame_container, companydetailsbypostFragment, "");
                            transaction.addToBackStack(null);
                            // transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                            transaction.commit();
                        }

                        else {
                            if (!getArguments().getBoolean("isBlocked")) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                builder.setTitle("Alert!").setMessage("Do you want Block this comapany?")
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                                JSONObject obj = new JSONObject();
                                                try {
                                                    SharedPreferences prefs = getActivity().getSharedPreferences("com.tpnagar", getActivity().MODE_PRIVATE);

                                                    String Company_Id = prefs.getString("Company_Id", "");
                                                    obj.put("CompanyId", Company_Id);
                                                    obj.put("BlockedCompanyId", getArguments().getString("id"));
                                                    obj.put("BlockingNote", "");


                                                    AppController.spinnerStart(getActivity());

                                                    notificationBlockRequest(obj, true);


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
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                builder.setTitle("Alert!").setMessage("Do you want Unblock this comapany?")
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                                JSONObject obj = new JSONObject();
                                                try {
                                                    SharedPreferences prefs = getActivity().getSharedPreferences("com.tpnagar", getActivity().MODE_PRIVATE);

                                                    String Company_Id = prefs.getString("Company_Id", "");
                                                    obj.put("CompanyId", Company_Id);
                                                    obj.put("BlockedCompanyId", getArguments().getString("id"));
                                                    obj.put("UnBlockingNote", "");


                                                    AppController.spinnerStart(getActivity());

                                                    notificationBlockRequest(obj, false);


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


        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initXmlViews(view);
        transaction = getActivity().getSupportFragmentManager().beginTransaction();


    }

    private void initXmlViews(View view) {
        cd = new ConnectionDetector(getActivity());


        prefs = getActivity().getSharedPreferences("com.tpnagar", MODE_PRIVATE);


        listview = (ListView) view.findViewById(R.id.listview);


        prefs = getActivity().getSharedPreferences("com.tpnagar", MODE_PRIVATE);
        if (prefs.getString("IsActive", "").equalsIgnoreCase("yes")) {
            ((com.tpnagar.designdemo.MainActivity) getActivity())
                    .getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#FFFFFF\">" + getArguments().getString("Name").toUpperCase() + "</font>")));

        } else {
            ((com.tpnagar.designdemo.MainActivityWithoutLogin) getActivity())
                    .getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#FFFFFF\">" + getArguments().getString("Name").toUpperCase() + "</font>")));

        }


        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        getNotificationListByDateShow();


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

    void getNotificationListByDateShow() {

        JSONObject obj = new JSONObject();
        try {
            obj.put("Password", "prem$123");
            //  AppController.spinnerStart(getActivity());

            Log.e("this is obj:::", obj.toString());

            String Company_Id = prefs.getString("Company_Id", "");
            String Login_Id = prefs.getString("Login_Id", "");
            notificationListRequest(obj, Login_Id, getArguments().getString("id"));


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void notificationListRequest(JSONObject params, String Login_Id, String Company_Id_ofnotification) {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, Const.URL_notificationsofcompany + Login_Id + "/" + Company_Id_ofnotification,
                params, onResponseNotificationList(), eErrorListenerRequest()) {
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

    private Response.ErrorListener eErrorListenerRequest() {
        Response.ErrorListener response_error = new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("this is error", error.toString());


                AppController.spinnerStop();
            }
        };
        return response_error;
    }

    private Response.Listener<JSONObject> onResponseNotificationList() {
        Response.Listener<JSONObject> response = new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                Log.e("NotificationList", response.toString());


                ArrayList<NotificationsWrapper> notificationsWrappers = new ArrayList<>();

                try {
                    String StatusValue = response.getString("StatusValue");

                    if (StatusValue.equalsIgnoreCase("Success")) {
                        JSONArray jsonArray = response.getJSONArray("Data");
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject obj = jsonArray.getJSONObject(i);

                            NotificationsWrapper notificationsWrapper = new NotificationsWrapper();

                            notificationsWrapper.setTitle(obj.getString("title"));
                            notificationsWrapper.setMessage(obj.getString("message"));
                            notificationsWrapper.setMsgType(obj.getString("msgType"));
                            notificationsWrapper.setRecordId(obj.getString("recordId"));

                            notificationsWrapper.setCrDate(obj.getString("crDate"));

                            notificationsWrapper.setStrdate(parseDate(obj.getString("crDate")));
                            notificationsWrapper.setStrtime(parseTime(obj.getString("crDate")));

                            notificationsWrapper.setId(obj.getInt("id"));
                            notificationsWrapper.setIsRead(obj.getString("IsRead"));

                            notificationsWrapper.setCompanyId(obj.getString("CompanyId"));
                            notificationsWrapper.setCompanyName(obj.getString("CompanyName"));

                            if (!obj.isNull("Vehicle")) {

                                PostVehicleWrapper postVehicleWrapper = new PostVehicleWrapper();
                                JSONObject objvhecle = obj.getJSONObject("Vehicle");
                                postVehicleWrapper.setVehicle_Detail(objvhecle.getString("Service_Name"));
                                postVehicleWrapper.setVehicle_No(objvhecle.getString("Vehicle_No"));
                                postVehicleWrapper.setContact_Person(objvhecle.getString("Contact_Person"));
                                postVehicleWrapper.setTo_City_Name(objvhecle.getString("To_City_Name"));
                                postVehicleWrapper.setTo_State_Name(objvhecle.getString("To_State_Name"));
                                postVehicleWrapper.setFrom_City_Name(objvhecle.getString("From_City_Name"));
                                postVehicleWrapper.setFrom_State_Name(objvhecle.getString("From_State_Name"));
                                postVehicleWrapper.setPhone_No(objvhecle.getString("Phone_No"));
                                postVehicleWrapper.setMobile_No(objvhecle.getString("Mobile_No"));
                                postVehicleWrapper.setEmail(objvhecle.getString("Email"));
                                postVehicleWrapper.setCompName(objvhecle.getString("CompName"));
                                postVehicleWrapper.setDescription(objvhecle.getString("Description"));

                                notificationsWrapper.setPostVehicleWrapper(postVehicleWrapper);


                            }
                            if (!obj.isNull("Goods")) {

                                PostGoodsWrapper postGoodsWrapper = new PostGoodsWrapper();
                                JSONObject objGoods = obj.getJSONObject("Goods");
                                postGoodsWrapper.setGoods_Detail(objGoods.getString("Service_Name"));
                                postGoodsWrapper.setContact_Person(objGoods.getString("Contact_Person"));
                                postGoodsWrapper.setTo_City_Name(objGoods.getString("To_City_Name"));
                                postGoodsWrapper.setTo_State_Name(objGoods.getString("To_State_Name"));
                                postGoodsWrapper.setFrom_City_Name(objGoods.getString("From_City_Name"));
                                postGoodsWrapper.setFrom_State_Name(objGoods.getString("From_State_Name"));

                                postGoodsWrapper.setFreight(""+objGoods.getInt("Freight"));

                                postGoodsWrapper.setDescription(objGoods.getString("Description"));
                                //  postGoodsWrapper.setGoods_Detail(obj.getString("Goods_Detail"));
                                //    postGoodsWrapper.setNeed_Vehicle(""+obj.getBoolean("Need_Vehicle"));

                                postGoodsWrapper.setCompName(objGoods.getString("CompName"));
                                postGoodsWrapper.setEmail(objGoods.getString("Email"));
                                postGoodsWrapper.setId(objGoods.getString("Id"));
                                postGoodsWrapper.setPhone_No(objGoods.getString("Phone_No"));
                                postGoodsWrapper.setMobile_Nod(objGoods.getString("Mobile_No"));
                                notificationsWrapper.setPostGoodsWrapper(postGoodsWrapper);
                            }

                            notificationsWrappers.add(notificationsWrapper);

                        }

                        NotificationAdapter mListAdapter = new NotificationAdapter(getActivity(), notificationsWrappers, transaction, NotificationsListFragment.this);
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

    public void notificationDeleteRequest(JSONObject params) {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, Const.URL_deletebyid,
                params, onResponseRequesrDelete(), eErrorListenerRequest()) {
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


    private Response.Listener<JSONObject> onResponseRequesrDelete() {
        Response.Listener<JSONObject> response = new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                Log.e("NotificationList", response.toString());


                AppController.spinnerStop();
                ArrayList<NotificationCompnay> notificationsWrappers = new ArrayList<>();

                try {
                    String StatusValue = response.getString("StatusValue");
                    getNotificationListByDateShow();

                    if (StatusValue.equalsIgnoreCase("Success")) {

                        AppController.showToast(getActivity(), response.getString("Desc"));

                    } else {
                        AppController.showToast(getActivity(), response.getString("Desc"));

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //  AppController.spinnerStop();

            }
        };
        return response;
    }

    public void notificationBlockRequest(JSONObject params, boolean ISblock) {
        String URL = Const.URL_blocknotification;

        if (ISblock) {
            URL = Const.URL_blocknotification;
        } else {
            URL = Const.URL_unblocknotification;
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, URL,
                params, onResponseRequesrBlock(), eErrorListenerRequesrBlock()) {
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

    private Response.ErrorListener eErrorListenerRequesrBlock() {
        Response.ErrorListener response_error = new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("this is error", error.toString());


                AppController.spinnerStop();
            }
        };
        return response_error;
    }

    private Response.Listener<JSONObject> onResponseRequesrBlock() {
        Response.Listener<JSONObject> response = new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                Log.e("NotificationList", response.toString());

                AppController.spinnerStop();

                ArrayList<NotificationCompnay> notificationsWrappers = new ArrayList<>();

                try {
                    String StatusValue = response.getString("StatusValue");

                    if (StatusValue.equalsIgnoreCase("Success")) {

                        AppController.showToast(getActivity(), response.getString("Desc"));
                        getActivity().onBackPressed();
                    } else {
                        AppController.showToast(getActivity(), response.getString("Desc"));

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //  AppController.spinnerStop();

            }
        };
        return response;
    }

    public void onDeleteRequestComapnay(JSONObject params) {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, Const.URL_deletebycompanyid,
                params, onResponseRequesrDeleteComanapy(), eErrorListenerRequesrBlock()) {
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


    private Response.Listener<JSONObject> onResponseRequesrDeleteComanapy() {
        Response.Listener<JSONObject> response = new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                Log.e("NotificationList", response.toString());


                AppController.spinnerStop();
                ArrayList<NotificationCompnay> notificationsWrappers = new ArrayList<>();

                try {
                    String StatusValue = response.getString("StatusValue");

                    if (StatusValue.equalsIgnoreCase("Success")) {

                        AppController.showToast(getActivity(), response.getString("Desc"));
                        getActivity().onBackPressed();

                    } else {
                        AppController.showToast(getActivity(), response.getString("Desc"));

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //  AppController.spinnerStop();

            }
        };
        return response;
    }

    public String dateFormatchange(String input) {

        String output = "";
        SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
       // System.out.println(format1.format(d1));


        return output;
    }

    public String parseDate(String time) {
        String inputPattern = "dd-MM-yyyy HH:mm:ss";
        String outputPattern = "dd-MMM";
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
    public String parseTime(String time) {
        String inputPattern = "dd-MM-yyyy HH:mm:ss";
        String outputPattern = "hh:mm a";
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
