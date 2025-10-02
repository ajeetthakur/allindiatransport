package com.tpnagar.fragment;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import com.tpnagar.wrapper.GetComapnyServiceWrapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class MainFragment extends BaseContainerFragment {
    SharedPreferences prefs = null;
    ConnectionDetector cd;
    String cityId="1",categoryId="1";
    ImageView transportal_directory,broker_directory,parcel_service,distance_calculator,post_goods,post_vehicle;
    AutoCompleteTextView spinner_autocompleCity;

    AutoCompleteTextView actv;

    String[] services;


    private boolean rootViewId;
    ImageButton search;
    CharSequence[]  serviceItem;
    ArrayList<String> city = new ArrayList<String>();
    ArrayList<String> cityid= new ArrayList<String>();
    ArrayList<String> servicesaStrings = new ArrayList<String>();
    String cityidForLoc="";
    ImageView dropdown;
View rootView;
    RelativeLayout rel_noti;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_main, container, false);
         spinner_autocompleCity=(AutoCompleteTextView) getActivity().findViewById(R.id.spinner_autocompleCity);

         actv=(AutoCompleteTextView) getActivity().findViewById(R.id.autoCompleteTextView1);

     final ImageView notification_icon=getActivity().findViewById(R.id.notification_icon);

        prefs = getActivity().getSharedPreferences("com.tpnagar", MODE_PRIVATE);
if(notification_icon!=null){
    notification_icon.setVisibility(View.VISIBLE);
    TextView count=getActivity().findViewById(R.id.count);
    count.setVisibility(View.VISIBLE);
    rel_noti=getActivity().findViewById(R.id.rel_noti);
    rel_noti.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            RelativeLayout     topbg=(RelativeLayout) getActivity().findViewById(R.id.topbg) ;
            topbg.setVisibility(View.GONE);
            replaceFragmentTask(new NotificationCompanyListFragment());

        }
    });

    // Log.e("UnReadMsgqqq",""+prefs.getInt("UnReadMsg",0));

    count.setText(""+prefs.getString("UnReadMsg","0"));



  //  BadgeUtils.setBadge(getActivity().getApplicationContext(),12);
  //  AppIcon.setBadge(getActivity().getApplicationContext(),12);
}

       // setHasOptionsMenu(true);
        //for 1.1.4+
    /* notification_icon.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {


        PopupMenu popup = new PopupMenu(getActivity(), notification_icon);
        //Inflating the Popup using xml file

        if(!prefs.getBoolean("NotificationOnOff",true)){
            popup.getMenu().add("Notification On");
        }else {
            popup.getMenu().add("Notification Off");
        }

        // popup.getMenuInflater().inflate(R.menu.poupup_menu_reg_title, popup.getMenu());

        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {

                JSONObject obja = new JSONObject();

                try {
                    String RequestType="";

                    if(prefs.getBoolean("NotificationOnOff",true)){
                        RequestType="0";
                    }else {
                        RequestType="1";
                    }

                    String  CompanyId=prefs.getString("Company_Id","");
                    obja.put("RequestType", RequestType);
                    obja.put("CompanyId", CompanyId);


                    AppController.spinnerStart(getActivity());

                    Log.e("this is obj:::", obja.toString());


                    noTificationOnOffRequest(obja);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return true;
            }
        });

        popup.show();
    }
});*/


        if(prefs.getString("IsActive","").equalsIgnoreCase("yes"))
        {
            ((com.tpnagar.designdemo.MainActivity) getActivity())
                    .getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#FFFFFF\">" +"Dashboard" + "</font>")));
        }else {
            ((com.tpnagar.designdemo.MainActivityWithoutLogin) getActivity())
                    .getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#FFFFFF\">" +"Dashboard" + "</font>")));
        }
        View viewnew = getActivity().getCurrentFocus();
        if (viewnew != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(viewnew.getWindowToken(), 0);
        }

      //  getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        hideKeyboard(getActivity());

        return rootView;
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();

        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {


                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setCancelable(false);
                        builder.setTitle("Alert!");
                        builder.setMessage("Are you want to exit from Tpnagar?")
                                .setCancelable(true)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        getActivity().finish();
                                    }
                                });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                dialog.cancel();
                            }
                        });

                        AlertDialog alert = builder.create();
                        alert.show();



                        return true;
                    }
                }
                return false;
            }
        });
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initXmlViews(view);


    }
    private void replaceFragmentTask(Fragment fragment) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
      /*  transaction.setCustomAnimations(
                R.anim.slide_in_from_right, R.anim.slide_out_to_left,
                R.anim.slide_in_from_left, R.anim.slide_out_to_right);*/
        transaction.replace(R.id.frame_container, fragment, "");
        transaction.addToBackStack("Search");
        // transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.commit();
    }
    private void initXmlViews(View view) {
        cd = new ConnectionDetector(getActivity());
        dropdown=(ImageView) view.findViewById(R.id.dropdown);
        search=(ImageButton) view.findViewById(R.id.search);
        transportal_directory=(ImageView) view.findViewById(R.id.transportal_directory);
        broker_directory=(ImageView) view.findViewById(R.id.broker_directory);
        parcel_service=(ImageView)view.findViewById(R.id.parcel_service);
        distance_calculator=(ImageView) view.findViewById(R.id.distance_calculator);
        post_goods=(ImageView) view.findViewById(R.id.post_goods);
        post_vehicle=(ImageView) view.findViewById(R.id.post_vehicle);


        prefs = getActivity().getSharedPreferences("com.tpnagar", MODE_PRIVATE);


        JSONObject obj = new JSONObject();

        try {


            obj.put("LoginId", "");


            AppController.spinnerStart(getActivity());

            Log.e("this is obj:::",obj.toString());


            getLoginDetailJsonObjectRequest(obj);


        } catch (JSONException e) {
            e.printStackTrace();
        }



        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String  SearchText="";
                String  Location="";





                Location=spinner_autocompleCity.getText().toString();
                SearchText=actv.getText().toString();

                if(actv.getText().toString()!=null){


                    if(actv.getText().toString().length()>3){


                        //cityidForLoc=cityid.get(spinner.getSelectedItemPosition());

                        Bundle bundle=new Bundle();
                        bundle.putString("Location",Location);
                        bundle.putString("SearchText",SearchText);
                        bundle.putInt("spinnerId",0);



                        SearchListFragment searchListFragment=new SearchListFragment();
                        searchListFragment.setArguments(bundle);


                        replaceFragmentTask(searchListFragment);

                    }else{
                        AppController.showToast(getActivity(), "please enter service completely");

                    }
                }


            }
        });
        transportal_directory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                rel_noti.setVisibility(View.GONE);

                        replaceFragmentTask(new TransportalDirectoryFragment());
            }
        });



        JSONObject obja = new JSONObject();

        try {


            obj.put("serviceRequest", "");


            AppController.spinnerStart(getActivity());

            Log.e("this is obj:::", obja.toString());


            serviceRequest(obja);


        } catch (JSONException e) {
            e.printStackTrace();
        }




      /*  actv.setOnTouchListener(new View.OnTouchListener() {

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View paramView, MotionEvent paramMotionEvent) {
                // TODO Auto-generated method stub

                JSONObject obj = new JSONObject();

                try {


                    obj.put("serviceRequest", "");


                    AppController.spinnerStart(getActivity());

                    Log.e("this is obj:::", obj.toString());


                    serviceRequest(obj);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                actv.showDropDown();
                actv.requestFocus();
                return false;
            }
        });*/
 /*       actv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject obj = new JSONObject();

                try {


                    obj.put("serviceRequest", "");


                    AppController.spinnerStart(getActivity());

                    Log.e("this is obj:::", obj.toString());


                    serviceRequest(obj);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });*/





        broker_directory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(prefs.getString("Login_Id","").length()>0)
                {


                    if(prefs.getString("Broker_Search_Enabled","").equalsIgnoreCase("true")){

                        RelativeLayout  topbg=(RelativeLayout) getActivity().findViewById(R.id.topbg) ;

                        topbg.setVisibility(View.GONE);
                        replaceFragmentTask(new SearchBrokerFragment());
                    }else{
                        AppController.popErrorMsg("Error","Broker search not enabled for you",getActivity());

                    }

                }
                else {
                    AppController.popErrorMsg("Error","Please Login for use this Service",getActivity());

                }
            }
        });





        {
            cd = new ConnectionDetector(getActivity());

            search=(ImageButton) view.findViewById(R.id.search);
            transportal_directory=(ImageView) view.findViewById(R.id.transportal_directory);
            broker_directory=(ImageView) view.findViewById(R.id.broker_directory);
            parcel_service=(ImageView)view.findViewById(R.id.parcel_service);
            distance_calculator=(ImageView) view.findViewById(R.id.distance_calculator);
            post_goods=(ImageView) view.findViewById(R.id.post_goods);
            post_vehicle=(ImageView) view.findViewById(R.id.post_vehicle);

            distance_calculator.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String uri = String.format(Locale.ENGLISH, "geo:%f,%f", 28.7041, 77.1025);
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    getActivity().startActivity(intent);
                }
            });

            prefs = getActivity().getSharedPreferences("com.tpnagar", MODE_PRIVATE);


            JSONObject objaa = new JSONObject();

            try {


                obja.put("LoginId", "");


                AppController.spinnerStart(getActivity());

                Log.e("this is obj:::",objaa.toString());


                getLoginDetailJsonObjectRequest(objaa);


            } catch (JSONException e) {
                e.printStackTrace();
            }

            parcel_service.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    RelativeLayout  topbg=(RelativeLayout) getActivity().findViewById(R.id.topbg) ;

                    topbg.setVisibility(View.GONE);

                    String  SearchText="Parcel Service";
                    String  Location="";

                   /* if(actv.getText()==null){
                        SearchText="";
                    }else {
                        SearchText=actv.getText().toString();
                    }*/
                    Location=spinner_autocompleCity.getText().toString();


                    Bundle bundle=new Bundle();
                    bundle.putString("Location",Location);
                    bundle.putString("SearchText",SearchText);
                    bundle.putInt("spinnerId",0);

                    SearchParcelServiceFragment searchListFragment=new SearchParcelServiceFragment();
                    searchListFragment.setArguments(bundle);


                    replaceFragmentTask(searchListFragment);

                }
            });


            transportal_directory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    String  SearchText="";
                    String  Location="";

                     if(actv.getText()==null){
                        SearchText="";
                    }else {
                        SearchText=actv.getText().toString();
                    }

                    Location="";
                    try {
                        Location=spinner_autocompleCity.getText().toString();
                    }
                    catch (Exception e){

                    }



                    Bundle bundle=new Bundle();
                    bundle.putString("Location",Location);
                    bundle.putString("SearchText",SearchText);
                    bundle.putInt("spinnerId",0);

                    TransportalDirectoryFragmentNew transportalDirectoryFragment=new TransportalDirectoryFragmentNew();
                    transportalDirectoryFragment.setArguments(bundle);


                    replaceFragmentTask(transportalDirectoryFragment);


                }
            });
            post_goods.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(prefs.getString("Login_Id","").length()>0)

                    { AppController.isPageone=true;
                        replaceFragmentTask(new PosMainPaggerFragment());
                        RelativeLayout  topbg=(RelativeLayout) getActivity().findViewById(R.id.topbg) ;

                        topbg.setVisibility(View.GONE);

                    }else {
                        AppController.popErrorMsg("Error","Please Login for use this Service",getActivity());

                    }


                }
            });

            actv.addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    if(s.length()>0)
                    {
                        if(cd.isConnectingToInternet())

                        {
                            ServiceListOnCityAndCategory(s.toString());
                            // ;

                        }else {
                            AppController.popErrorMsg("Alert!", "please check internet connection",getActivity());

                        }
                    }

                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            post_vehicle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(prefs.getString("Login_Id","").length()>0)
                    {
                        //RelativeLayout  topbg=(RelativeLayout) getActivity().findViewById(R.id.topbg) ;

                        //topbg.setVisibility(View.GONE);


                        AppController.isPageone=false;
                        replaceFragmentTask(new PosMainPaggerFragment());
                        RelativeLayout  topbg=(RelativeLayout) getActivity().findViewById(R.id.topbg) ;

                        topbg.setVisibility(View.GONE);
                    }
                    else {
                        AppController.popErrorMsg("Error","Please Login for use this Service",getActivity());

                    }
                }
            });

     /*       broker_directory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(prefs.getString("Login_Id","").length()>0)
                    {
                        RelativeLayout  topbg=(RelativeLayout) getActivity().findViewById(R.id.topbg) ;

                        topbg.setVisibility(View.GONE);


                        replaceFragmentTask(new SearchBrokerFragment());
                    }
                    else {
                        AppController.popErrorMsg("Error","Please Login for use this Service",getActivity());

                    }
                }
            });*/



            hideKeyboard(getActivity());

        }

        //RandomCityList();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

    }
    void ServiceListOnCityAndCategory(String editText){

        JSONObject obj = new JSONObject();
        try {
            obj.put("SearchText", editText);
            obj.put("Location", cityidForLoc);
          //  AppController.spinnerStart(getActivity());

            Log.e("this is obj:::",obj.toString());


            ServiceListOnCityAndCategoryRequest(obj,editText);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void ServiceListOnCityAndCategoryRequest(JSONObject params,String editText) {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, Const.URL_SearchData,
                params, newFromCityResponseRequesr(), eErrorListenerRequesr()) {
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

    private Response.ErrorListener eErrorListenerRequesr() {
        Response.ErrorListener response_error = new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("this is error",error.toString());


                AppController.spinnerStop();
            }
        };
        return response_error;
    }

    private Response.Listener<JSONObject> newFromCityResponseRequesr() {
        Response.Listener<JSONObject> response = new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                Log.e("ServiceListOnCityAn", response.toString());

                try {
                    String StatusValue = response.getString("StatusValue");
                    servicesaStrings = new ArrayList<String>();
                    if (StatusValue.equalsIgnoreCase("Success")) {
                        JSONArray jsonArray = response.getJSONArray("Data");
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject obj = jsonArray.getJSONObject(i);

                            servicesaStrings.add(obj.getString("Data"));

                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (servicesaStrings != null ) {
                    services = servicesaStrings.toArray(new String[0]);


            }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>
                        (getActivity(),android.R.layout.simple_list_item_1,services);
                actv.setAdapter(adapter);

          /*     actv.clearFocus();
                getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                if (actv != null) {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(actv.getWindowToken(), 0);
                }*/
              //  AppController.spinnerStop();

            }
        };
        return response;
    }

    @Override
    public void onResume() {
        super.onResume();
        RelativeLayout  topbg=(RelativeLayout) getActivity().findViewById(R.id.topbg) ;

        final ImageView notification_icon=getActivity().findViewById(R.id.notification_icon);
        if(notification_icon!=null)
        notification_icon.setImageResource(R.drawable.ic_notifications_black_24dp);
        if(rel_noti!=null)
        rel_noti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RelativeLayout     topbg=(RelativeLayout) getActivity().findViewById(R.id.topbg) ;
                topbg.setVisibility(View.GONE);
                replaceFragmentTask(new NotificationCompanyListFragment());

            }
        });

        topbg.setVisibility(View.VISIBLE);
        AppController.getInstance().trackScreenView("Main Fragment");
        try {

        }catch (Exception e){

        }

    }

  /*  void RandomCityList(){

        JSONObject obj1 = new JSONObject();

        try {

            obj1.put("Password", "prem$123");
            AppController.spinnerStart(getActivity());

            Log.e("this is obj:::",obj1.toString());


            RandomCityListRequest(obj1);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void RandomCityListRequest(JSONObject params) {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, Const.URL_RandomCityList,
                params, newRandomCityListResponseRequesr(), eErrorListenerRequesrRandomCityList()) {
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

    private Response.ErrorListener eErrorListenerRequesrRandomCityList() {
        Response.ErrorListener response_error = new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("this is error",error.toString());


                AppController.spinnerStop();
            }
        };
        return response_error;
    }

    private Response.Listener<JSONObject> newRandomCityListResponseRequesr() {
        Response.Listener<JSONObject> response = new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                Log.e("response RandomCityList",response.toString());

                try{
String StatusValue=response.getString("StatusValue");

                    if(StatusValue.equalsIgnoreCase("Success"))
                    {
                        JSONArray jsonArray = response.getJSONArray("Data");
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject obj = jsonArray.getJSONObject(i);

                            city.add(obj.getString("City_Name"));
                            cityid.add(obj.getString("Id"));

                            if(i==0){
                                cityidForLoc=obj.getString("Id");
                            }
                            DataManager.getInstance().setCity(city);

                            DataManager.getInstance().setCityIds(cityid);
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // Creating adapter for spinner
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, city);

                // Drop down layout style - list view with radio button
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                // attaching data adapter to spinner
                spinner.setAdapter(dataAdapter);


                AppController.spinnerStop();


                prefs = getActivity().getSharedPreferences("com.tpnagar", MODE_PRIVATE);
                if(prefs.getString("IsActive","").equalsIgnoreCase("yes")){

                if(!(prefs.getString("Company_Type_Id", "").length()>0)){

                    FragmentTransaction fragmentManager1 = getActivity().getSupportFragmentManager().beginTransaction();

                    fragmentManager1.replace(R.id.frame_container, new BusniessDetailsFragment(),"item");
                    fragmentManager1.addToBackStack("item");
                    fragmentManager1.commit();

                }}

            }
        };
        return response;
    }
*/
    private void getLoginDetailJsonObjectRequest(JSONObject params) {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, Const.URL_GetLoginDetail+prefs.getString("Login_Id",""),
                params, newFromCityResponseRequesrLoginDetail(), eErrorListenerRequesrLoginDetail()) {
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

        AppController.getInstance().addToRequestQueue(jsonObjReq, "validatelogin");
    }

    private Response.ErrorListener eErrorListenerRequesrLoginDetail() {
        Response.ErrorListener response_error = new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("this is error",error.toString());


                AppController.spinnerStop();
            }
        };
        return response_error;
    }

    private Response.Listener<JSONObject> newFromCityResponseRequesrLoginDetail() {
        Response.Listener<JSONObject> response = new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                Log.e("response ",response.toString());

                AppController.spinnerStop();
                try {
                    String StatusValue = response.getString("StatusValue");

                    if (StatusValue.equalsIgnoreCase("Success"))
                    {
                        JSONObject jsonObject=response.getJSONObject("Data");

                        String email=jsonObject.getString("Login_Email");
                        String phone=jsonObject.getString("Login_Phone");
                        String pwd=jsonObject.getString("Pwd");

                        TextView  txtemail=(TextView) getActivity().findViewById(R.id.txtemail) ;
                        TextView phone1=(TextView) getActivity().findViewById(R.id.Phone) ;
                        TextView comp_name=(TextView) getActivity().findViewById(R.id.comp_name) ;
                        prefs = getActivity().getSharedPreferences("com.tpnagar", MODE_PRIVATE);

//                        comp_name.setText(prefs.getString("Company_Name",""));
                   /*    String myString=prefs.getString("Company_Name", "");
                        String upperString = myString.substring(0,1).toUpperCase() + myString.substring(1);

                        txtUserName.setText(upperString);*/

                        if(email!=null){
                            txtemail.setText(email);
                        }
                        if(phone!=null){
                            phone1.setText(phone);
                        }
                        if(pwd!=null){
                           // txtUserName.setText(pwd);
                        }



                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };
        return response;
    }
    private void serviceRequest(JSONObject params) {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, "http://services.tpnagar.co.in/api/V1/service/ContractorServices/0",
                params, newResponseRequesrtservice(), eErrorListenerRequesrtservice()) {
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

    private Response.ErrorListener eErrorListenerRequesrtservice() {
        Response.ErrorListener response_error = new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("this is error", error.toString());


                AppController.spinnerStop();
            }
        };
        return response_error;
    }

    public Response.Listener<JSONObject> newResponseRequesrtservice() {
        Response.Listener<JSONObject> response = new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                ArrayList<GetComapnyServiceWrapper> getComapnyServiceWrapperlist = new ArrayList<GetComapnyServiceWrapper>();
                Log.e("ServiceListOnCityAn", response.toString());
                ArrayList<String> arrayList1 = new ArrayList<String>();
                try {
                    String StatusValue = response.getString("StatusValue");

                    if (StatusValue.equalsIgnoreCase("Success"))

                    // arrayListBoolean=new ArrayList<Boolean>();


                    {
                        JSONArray jsonArray = response.getJSONArray("Data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            GetComapnyServiceWrapper getComapnyServiceWrapper = new GetComapnyServiceWrapper();
                            JSONObject obj = jsonArray.getJSONObject(i);
                            getComapnyServiceWrapper.setService_Name(obj.getString("Service_Name"));
                            getComapnyServiceWrapper.setId(obj.getString("Id"));
                            getComapnyServiceWrapper.setHas_Destination(""+obj.getBoolean("Has_Destination"));
                            getComapnyServiceWrapper.setService_Type(obj.getString("Service_Type"));

                            // arrayList1.add(obj.getString("City_Name"));
                           // arrayListBoolean.add(false);
                            arrayList1.add(obj.getString("Service_Name"));
                            // arrayList1.add(obj.getString("Service_Name"));
                            servicesaStrings.add(obj.getString("Service_Name"));
                           // arrayList1.add(obj.getString("Service_Name"));
                            getComapnyServiceWrapperlist.add(getComapnyServiceWrapper);

                        }
                        if (servicesaStrings != null ) {
                            services = servicesaStrings.toArray(new String[0]);
                        }

                        serviceItem =arrayList1.toArray(new CharSequence[arrayList1.size()]);
                       /* ArrayAdapter<String> adapter = new ArrayAdapter<String>
                                (getActivity(),android.R.layout.simple_list_item_1,services);
                        actv.setAdapter(adapter);*/
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                AppController.spinnerStop();

            }
        };
        return response;
    }

    private void noTificationOnOffRequest(JSONObject params) {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, Const.URL_onoff,
                params, newResponseRequesrtOnOff(), eErrorListenerRequesrtservice()) {
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

}
