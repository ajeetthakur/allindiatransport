package com.tpnagar.fragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.RequiresApi;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.content.ContextCompat;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tpnagar.AppController;
import com.tpnagar.BaseContainerFragment;
import com.tpnagar.ConnectionDetector;
import com.tpnagar.Const;
import com.tpnagar.DataManager;
import com.tpnagar.R;
import com.tpnagar.adapter.BrokerSearchMainAdapter;
import com.tpnagar.wrapper.SearchCompanyWrapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;
import static com.tpnagar.AppController.BrokerStateFrom;
import static com.tpnagar.AppController.BrokerStateTo;
import static com.tpnagar.fragment.BrokarMainPaggerFragment.FromCityId;
import static com.tpnagar.fragment.BrokarMainPaggerFragment.Service_Id;
import static com.tpnagar.fragment.BrokarMainPaggerFragment.ToCityId;

public class BrokerListFragment extends BaseContainerFragment implements AdapterView.OnItemSelectedListener{
    SharedPreferences prefs = null;
    ConnectionDetector cd;
    String cityId="1",categoryId="1";
    ImageView transportal_directory,broker_directory,parcel_service,distance_calculator,post_goods,post_vehicle;
    private AutoCompleteTextView actv;
    String[] services;
    Spinner spinner;
    private boolean rootViewId;
    ImageButton search;
    ArrayList<String> city ;
    ArrayList<String> servicesaStrings = new ArrayList<String>();

    ListView listview;
    FragmentTransaction transaction;
    View rootView;
    TextView normal,rating_text;

    String  SearchText="";
    String  Location="";
    String FromCity,ToCity;
      ProgressDialog dialognew=null;
    TextView  vendor_text;
    String ToSateName,ToSateId,FromSateName,FromSateId;
  public String vanderListing="";
    JSONArray paramsMainResponse;

    ArrayList<SearchCompanyWrapper > searchCompanyWrapperlist=new ArrayList<SearchCompanyWrapper>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.brokerlistnewfor_only_brokers, container, false);
        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Click action
               getActivity().onBackPressed();
            }
        });

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initXmlViews(view);
        city=DataManager.getInstance().getCityBroker();
        transaction = getActivity().getSupportFragmentManager().beginTransaction();


    }

    private void initXmlViews(View view) {
        cd = new ConnectionDetector(getActivity());

        search=(ImageButton) view.findViewById(R.id.search);

        LinearLayout upr_lin=(LinearLayout) view.findViewById(R.id.upr_lin);
        upr_lin.setVisibility(View.VISIBLE);
        actv = (AutoCompleteTextView) view.findViewById(R.id.autoCompleteTextView1);
        actv.clearFocus();
        spinner = (Spinner) view.findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);

        prefs = getActivity().getSharedPreferences("com.tpnagar", MODE_PRIVATE);

RelativeLayout topbg=(RelativeLayout) view.findViewById(R.id.topbg);
        topbg.setVisibility(View.GONE);


        listview=(ListView) view.findViewById(R.id.listview);


        normal=(TextView) view.findViewById(R.id.normal);
                rating_text=(TextView) view.findViewById(R.id.rating_text);
          vendor_text=(TextView) view.findViewById(R.id.vendor_text);
        normal.setTextColor(Color.RED);
        normal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                normal.setTextColor(Color.RED);
                rating_text.setTextColor(Color.BLACK);
                vendor_text.setTextColor(Color.BLACK);
            }
        });

      /*  vendor_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                normal.setTextColor(Color.BLACK);
                rating_text.setTextColor(Color.BLACK);
                vendor_text.setTextColor(Color.RED);
            }
        });*/
        rating_text.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                rating_text.setTextColor(Color.RED);
                normal.setTextColor(Color.BLACK);
                vendor_text.setTextColor(Color.BLACK);
               // searchCompanyWrapperlist
                //searchCompanyWrapperlist.sort(new Ratingsort());

                ArrayList<SearchCompanyWrapper> getSearchCompanyBrokerWrapper;
                getSearchCompanyBrokerWrapper=DataManager.getInstance().getSearchCompanyBrokerWrapper();
                        Collections.sort(getSearchCompanyBrokerWrapper, Ratingsort);

                Collections.reverse(getSearchCompanyBrokerWrapper);
                BrokerSearchMainAdapter mListAdapter = new  BrokerSearchMainAdapter(getActivity(), getSearchCompanyBrokerWrapper ,transaction,Location, SearchText, FromCity,ToCity, FromCityId, ToCityId, Service_Id,0, ToSateName, ToSateId, FromSateName,FromSateId);
                listview.setAdapter(mListAdapter);

            }
        });



        normal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rating_text.setTextColor(Color.BLACK);
                normal.setTextColor(Color.RED);
                vendor_text.setTextColor(Color.BLACK);

                getTokenRequest(null);

            }
        });
        vendor_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rating_text.setTextColor(Color.BLACK);
                normal.setTextColor(Color.BLACK);
                vendor_text.setTextColor(Color.RED);

                vendorListRequest(paramsMainResponse);

            }
        });
        SearchText=getArguments().getString("SearchText");
        Location=getArguments().getString("Location");
        actv.setText(SearchText);






        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                  if(actv.getText()==null){
                      SearchText="";
                  }else {
                      SearchText=actv.getText().toString();
                  }
                  Location=spinner.getSelectedItem().toString();




            }
        });

        actv.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(s.length()>2)
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
        prefs = getActivity().getSharedPreferences("com.tpnagar", MODE_PRIVATE);
        if(prefs.getString("IsActive","").equalsIgnoreCase("yes"))
        {
            ((com.tpnagar.designdemo.MainActivity) getActivity())
                    .getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#FFFFFF\">" +"Search List" + "</font>")));

        }else {
            ((com.tpnagar.designdemo.MainActivityWithoutLogin) getActivity())
                    .getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#FFFFFF\">" +"Search List" + "</font>")));

        }


        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);


       // RandomCityList();
         city=DataManager.getInstance().getCityBroker();

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, city);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

        int pos=getArguments().getInt("spinnerId");
        spinner.setSelection(pos);


        SearchText=getArguments().getString("SearchText");
        Location=getArguments().getString("Location");


        ToSateName=getArguments().getString("ToSateName");
        ToSateId=getArguments().getString("ToSateId");
        FromSateName=getArguments().getString("FromSateName");
        FromSateId=getArguments().getString("FromSateId");
       TextView city_service_info=(TextView) view.findViewById(R.id.city_service_info);



        if(getArguments().getString("SearchText")!=null){
            SearchText=getArguments().getString("SearchText");
            AppController.FromCity=getArguments().getString("SearchText");
        }
        if(getArguments().getString("FromCity")!=null){
            FromCity=getArguments().getString("FromCity");
            AppController.FromCity=getArguments().getString("FromCity");
        }

        if(getArguments().getString("ToCity")!=null){
            ToCity=getArguments().getString("ToCity");
            AppController.ToCity=getArguments().getString("ToCity");
        }


        prefs = getActivity().getSharedPreferences("com.tpnagar", MODE_PRIVATE);

        city_service_info.setText( AppController.BrokerServiceName+", "+"From-> "+prefs.getString("BrokerCityFrom","")+"("+prefs.getString("BrokerStateFrom","")+") To-> "+prefs.getString("BrokerSearch_ToCity","")+"("+prefs.getString("BrokerStateTo","")+")");
        Log.e("Prem list Brok",""+ BrokerStateTo);

        Log.e("Prem list Brok",""+BrokerStateFrom);
        getTokenRequest(null);
        dialognew = ProgressDialog.show(getContext(), "", "Loading...", true);



    }

    public void requestWithSomeHttpHeaders() {

        FragmentManager fm = getFragmentManager();
        BrokarMainPaggerFragment fragm = (BrokarMainPaggerFragment)fm.findFragmentById(R.id.frame_container);
        fragm.updateTitleData(0,1,"BROKER");

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url="";

        ToSateName=prefs.getString("BrokerStateTo","");

        if(ToSateName==null){
            ToSateName="";
        }

        if(SearchText==null){
            SearchText="";
        }
       if(SearchText.length()>0){
           if(ToSateName.length()>0) {
               url = Const.URL_MainSearch+"brokercompany?fromcity=" + FromCity + "&tocity=" + ToCity + "&service=" + SearchText + "&tostate=" + ToSateName;
           }else {
               url = Const.URL_MainSearch+"brokercompany?fromcity="+FromCity+ "&service=" + SearchText + "&fromstate=" + FromSateName ;

           }
        }else {

           if(ToSateName.length()>0){
               url = Const.URL_MainSearch+"brokercompany?fromcity="+FromCity+"&tocity="+ToCity+"&tostate="+ToSateName;

           }else {
               url = Const.URL_MainSearch+"brokercompany?fromcity="+FromCity+"&tocity="+ToCity;

           }
        }
        url=url.replaceAll(" ", "%20");
        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {

                        if (dialognew != null) {
                            if (dialognew.isShowing()) {
                                dialognew.dismiss();
                            }
                        }
                        // response
                        Log.d("Response Prem", response);
                        ArrayList<SearchCompanyWrapper > searchCompanyWrapperlist=new ArrayList<SearchCompanyWrapper>();

                        Log.e("Prem response", response.toString());
                        try {
                            AppController.spinnerStop();

                            Log.e("Broker Search data", response.toString());
                            try {
                   /* if (response.has("success")) {

                        boolean StatusValue = response.getBoolean("success");

                        if(!StatusValue){

                            getTokenRequest(null);
                        }

                    }else {*/


                   JSONArray jsonArray = new JSONArray(response);

                                paramsMainResponse=jsonArray;
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    SearchCompanyWrapper searchCompanyWrapper=new SearchCompanyWrapper();
                                    JSONObject obj = jsonArray.getJSONObject(i);
                                    searchCompanyWrapper.set_id(obj.getString("_id"));
                                    searchCompanyWrapper.setCompany_Id(obj.getString("Company_Id"));
                                    searchCompanyWrapper.setService_Id(obj.getString("Service_Id"));
                                    searchCompanyWrapper.setCompany_Name(obj.getString("Company_Name"));
                                    searchCompanyWrapper.setCompany_Desc(obj.getString("Company_Desc"));
                                    searchCompanyWrapper.setContact_Person(obj.getString("Contact_Person"));
                                    searchCompanyWrapper.setAddress(obj.getString("Address"));
                                    searchCompanyWrapper.setCountry_Id(obj.getString("Country_Id"));
                                    searchCompanyWrapper.setState_Id(obj.getString("State_Id"));
                                    searchCompanyWrapper.setState_Name(obj.getString("State_Name"));
                                    searchCompanyWrapper.setCity_Id(obj.getString("City_Id"));
                                    searchCompanyWrapper.setWebsite(obj.getString("Website"));
                                    searchCompanyWrapper.setPin_Code(obj.getString("Pin_Code"));
                                    searchCompanyWrapper.setPrimary_Phone(obj.getString("Primary_Phone"));
                                    searchCompanyWrapper.setRating(obj.getString("Rating"));
                                    searchCompanyWrapper.setTPNagar_Trusted(obj.getString("TPNagar_Trusted"));
                                    // searchCompanyWrapper.setRating("2.5");
                                    searchCompanyWrapper.setCompany_Type_Id(obj.getString("Company_Type_Id"));
                                    searchCompanyWrapper.setCurrent_Status_Id(obj.getString("Current_Status_Id"));
                                    searchCompanyWrapper.setDestination_Cities(obj.getString("Destination_Cities"));

if (vanderListing.contains(obj.getString("Company_Id"))){
    searchCompanyWrapper.setIsmyVender(true);
}else {
    searchCompanyWrapper.setIsmyVender(false);
}
                                    searchCompanyWrapperlist.add(searchCompanyWrapper);

                                    // }


                                    FragmentManager fm = getFragmentManager();
                                    BrokarMainPaggerFragment fragm = (BrokarMainPaggerFragment)fm.findFragmentById(R.id.frame_container);
                                    fragm.updateTitleData(searchCompanyWrapperlist.size(),1,"BROKER");
                                    BrokarMainPaggerFragment.brokersCount=searchCompanyWrapperlist.size();
                                    DataManager.getInstance().setSearchCompanyBrokerWrapper(searchCompanyWrapperlist);
                                    BrokerSearchMainAdapter mListAdapter = new  BrokerSearchMainAdapter(getActivity(), searchCompanyWrapperlist ,transaction,Location, SearchText, FromCity,ToCity, FromCityId, ToCityId, Service_Id,0, ToSateName, ToSateId, FromSateName,FromSateId);
                                    listview.setAdapter(mListAdapter);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            BrokerSearchMainAdapter mListAdapter = new  BrokerSearchMainAdapter(getActivity(), searchCompanyWrapperlist ,transaction,Location, SearchText, FromCity,ToCity, FromCityId, ToCityId, Service_Id,0, ToSateName, ToSateId, FromSateName,FromSateId);
                            listview.setAdapter(mListAdapter);
                            try {

                                if (dialognew != null) {
                                    if (dialognew.isShowing()) {
                                        dialognew.dismiss();
                                    }
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        AppController.spinnerStop();
                        if (dialognew != null) {
                            if (dialognew.isShowing()) {
                                dialognew.dismiss();
                            }
                        }

                       // AppController.showToast(getActivity(),""+error.toString());
                        Log.d("ERROR","error => "+error.toString());
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();

                params.put("Content-Type", "application/json");
                prefs = getActivity().getSharedPreferences("com.tpnagar", MODE_PRIVATE);
                String token=prefs.getString("Token","");
                Log.e("token  ",token);
                params.put("x-access-token",token);

                return params;
            }
        };
        queue.add(postRequest);

    }


    private void getTokenRequest(JSONObject params) {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, Const.URL_GetToken,
                params, newFromCityResponsegettoken(), eErrorListenergettoken()) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("userid", "testapiresu");
                headers.put("pwd", "tsetapissapword");
                  return headers;
            }
        };
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppController.getInstance().addToRequestQueue(jsonObjReq, "URL_GetSearchResult");
    }

    private Response.ErrorListener eErrorListenergettoken() {
        Response.ErrorListener response_error = new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("this is error",error.toString());


                try {

                    if (dialognew != null) {
                        if (dialognew.isShowing()) {
                            dialognew.dismiss();
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        return response_error;
    }

    private Response.Listener<JSONObject> newFromCityResponsegettoken() {
        Response.Listener<JSONObject> response = new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {


                ArrayList<SearchCompanyWrapper > searchCompanyWrapperlist=new ArrayList<SearchCompanyWrapper>();
                Log.e("Search data", response.toString());
                try {
                    boolean StatusValue = response.getBoolean("success");

                    if (StatusValue)
                    {
                        String string = response.getString("token");
                        prefs.edit().putString("Token",string).commit();

                    //  requestWithSomeHttpHeaders();
                        JSONObject obj = new JSONObject();
                        try {
                            obj.put("Company_Id", prefs.getString("Company_Id",""));
                            vendorStringRequest(obj);

                        }catch (Exception e){

                        }


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        };
        return response;
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

    void ServiceListOnCityAndCategory(String editText){

        JSONObject obj = new JSONObject();
        try {
            obj.put("Password", "prem$123");
            //  AppController.spinnerStart(getActivity());

            Log.e("this is obj:::",obj.toString());


            ServiceListOnCityAndCategoryRequest(obj,editText);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void ServiceListOnCityAndCategoryRequest(JSONObject params,String editText) {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, Const.URL_SearchData+editText,
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

        AppController.getInstance().addToRequestQueue(jsonObjReq, "register");
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

                Log.e("ServiceListOnCityAn", response.toString());

                try {
                    String StatusValue = response.getString("StatusValue");
                    servicesaStrings = new ArrayList<String>();
                    if (StatusValue.equalsIgnoreCase("Success")) {
                        JSONArray jsonArray = response.getJSONArray("Data");
                        for (int i = 0; i < 20; i++) {

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

                spinner.setSelection(getArguments().getInt("spinnerId"));
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
    void RandomCityList(){

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


               // AppController.spinnerStop();
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


              //  AppController.spinnerStop();

            }
        };
        return response;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();


        AppController.POS=position;
        // Showing selected spinner item
      //  Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void onCall(String number) {
        int permissionCheck = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, 1);
        } else {
            startActivity(new Intent(Intent.ACTION_CALL).setData(Uri.parse("tel:"+ number)));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
     //   getTokenRequest(null);

       /* if(DataManager.getInstance().getSearchCompanyWrapper()!=null){

            listview=(ListView) rootView.findViewById(R.id.listview);
            if( DataManager.getInstance().getSearchCompanyWrapper().size()>0){
                BrokerSearchMainAdapter mListAdapter = new BrokerSearchMainAdapter(getActivity(), DataManager.getInstance().getSearchCompanyWrapper() ,transaction);
                listview.setAdapter(mListAdapter);
            }
        }*/

        TextView city_service_info=(TextView) rootView.findViewById(R.id.city_service_info);

        prefs = getActivity().getSharedPreferences("com.tpnagar", MODE_PRIVATE);

        city_service_info.setText( AppController.BrokerServiceName+", "+"From-> "+prefs.getString("BrokerCityFrom","")+"("+prefs.getString("BrokerStateFrom","")+") To-> "+prefs.getString("BrokerSearch_ToCity","")+"("+prefs.getString("BrokerStateTo","")+")");

        if(DataManager.getInstance().getSearchCompanyBrokerWrapper()!=null){

            if(DataManager.getInstance().getSearchCompanyBrokerWrapper().size()>0){
                listview=(ListView) rootView.findViewById(R.id.listview);
                BrokerSearchMainAdapter mListAdapter = new BrokerSearchMainAdapter(getActivity(), DataManager.getInstance().getSearchCompanyBrokerWrapper() ,transaction,Location, SearchText, FromCity,ToCity, FromCityId, ToCityId, Service_Id,0, ToSateName, ToSateId, FromSateName,FromSateId);

                listview.setAdapter(mListAdapter);


            }

        }
        Log.e("Prem lBrokerStateTo ",""+ BrokerStateTo);

        Log.e("Prem l BrokerStateFrom ",""+BrokerStateFrom);
    }


    private void vendorStringRequest(JSONObject params) {

        JsonObjectRequest  jsonObjReq = new JsonObjectRequest(Request.Method.POST, Const.URL_getcompanyvendor,
                params.toString(), vendorStringResponse(), eErrorListenervendorList()) {
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

    private void vendorListRequest(JSONArray params) {

        JsonObjectRequest  jsonObjReq = new JsonObjectRequest(Request.Method.POST, Const.URL_FilterVendorsFromSearchResult+"/"+prefs.getString("Company_Id",""),
                params.toString(), newvendorListResponseRequesrt(), eErrorListenervendorList()) {
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

    private Response.ErrorListener eErrorListenervendorList() {
        Response.ErrorListener response_error = new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("this is error",error.toString());


                // AppController.spinnerStop();
            }
        };
        return response_error;
    }

    private Response.Listener<JSONObject> newvendorListResponseRequesrt() {
        Response.Listener<JSONObject> response = new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                Log.e("response RandomCityList",response.toString());
               searchCompanyWrapperlist=new ArrayList<SearchCompanyWrapper>();

                try{
                    String StatusValue=response.getString("StatusValue");

                    if(StatusValue.equalsIgnoreCase("Success"))
                    {
                        JSONArray jsonArray = response.getJSONArray("Data");


                        try {
                            AppController.spinnerStop();

                            paramsMainResponse=jsonArray;
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    SearchCompanyWrapper searchCompanyWrapper=new SearchCompanyWrapper();



                                    JSONObject obj = jsonArray.getJSONObject(i);
                                    searchCompanyWrapper.set_id(obj.getString("_id"));
                                    searchCompanyWrapper.setCompany_Id(obj.getString("Company_Id"));
                                    searchCompanyWrapper.setService_Id(obj.getString("Service_Id"));
                                    searchCompanyWrapper.setCompany_Name(obj.getString("Company_Name"));
                                    searchCompanyWrapper.setCompany_Desc(obj.getString("Company_Desc"));
                                    searchCompanyWrapper.setContact_Person(obj.getString("Contact_Person"));
                                    searchCompanyWrapper.setAddress(obj.getString("Address"));
                                    searchCompanyWrapper.setCountry_Id(obj.getString("Country_Id"));
                                    searchCompanyWrapper.setState_Id(obj.getString("State_Id"));
                                    searchCompanyWrapper.setState_Name(obj.getString("State_Name"));
                                    searchCompanyWrapper.setCity_Id(obj.getString("City_Id"));
                                    searchCompanyWrapper.setWebsite(obj.getString("Website"));
                                    searchCompanyWrapper.setPin_Code(obj.getString("Pin_Code"));
                                    searchCompanyWrapper.setPrimary_Phone(obj.getString("Primary_Phone"));
                                    searchCompanyWrapper.setRating(obj.getString("Rating"));
                                    searchCompanyWrapper.setTPNagar_Trusted(obj.getString("TPNagar_Trusted"));
                                    // searchCompanyWrapper.setRating("2.5");
                                    searchCompanyWrapper.setCompany_Type_Id(obj.getString("Company_Type_Id"));
                                    searchCompanyWrapper.setCurrent_Status_Id(obj.getString("Current_Status_Id"));
                                    searchCompanyWrapper.setDestination_Cities(obj.getString("Destination_Cities"));

                                    if (vanderListing.contains(obj.getString("Company_Id"))){
                                        searchCompanyWrapper.setIsmyVender(true);
                                    }else {
                                        searchCompanyWrapper.setIsmyVender(false);
                                    }
                                    searchCompanyWrapperlist.add(searchCompanyWrapper);

                                    // }

                                   // DataManager.getInstance().setSearchCompanyBrokerWrapper(searchCompanyWrapperlist);
                                    BrokerSearchMainAdapter mListAdapter = new  BrokerSearchMainAdapter(getActivity(), searchCompanyWrapperlist ,transaction,Location, SearchText, FromCity,ToCity, FromCityId, ToCityId, Service_Id,0, ToSateName, ToSateId, FromSateName,FromSateId);
                                    listview.setAdapter(mListAdapter);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // Creating adapter for spinner
                BrokerSearchMainAdapter mListAdapter = new  BrokerSearchMainAdapter(getActivity(), searchCompanyWrapperlist ,transaction,Location, SearchText, FromCity,ToCity, FromCityId, ToCityId, Service_Id,0, ToSateName, ToSateId, FromSateName,FromSateId);
                listview.setAdapter(mListAdapter);


                //  AppController.spinnerStop();

            }
        };
        return response;
    }


    private Response.Listener<JSONObject> vendorStringResponse() {
        Response.Listener<JSONObject> response = new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                Log.e("response RandomCityList",response.toString());
                ArrayList<SearchCompanyWrapper > searchCompanyWrapperlist=new ArrayList<SearchCompanyWrapper>();

                try{
                    String StatusValue=response.getString("StatusValue");

                    if(StatusValue.equalsIgnoreCase("Success"))
                    {
                         vanderListing=response.getString("Data");
                        AppController.spinnerStop();



                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                requestWithSomeHttpHeaders();
                //  AppController.spinnerStop();

            }
        };
        return response;
    }

    public static Comparator<SearchCompanyWrapper> Ratingsort = new Comparator<SearchCompanyWrapper>() {

        @Override
        public int compare(SearchCompanyWrapper searchCompanyWrapper1, SearchCompanyWrapper searchCompanyWrapper2) {

          int one=  Integer.parseInt(searchCompanyWrapper1.getRating());
            int two=  Integer.parseInt(searchCompanyWrapper2.getRating());
            int rollno1 =one ;
          int rollno2 = two;

            /*For ascending order*/
           return rollno1 - rollno2;

        }

    };
}
