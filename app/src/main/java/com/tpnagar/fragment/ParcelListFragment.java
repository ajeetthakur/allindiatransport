package com.tpnagar.fragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
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
import com.tpnagar.adapter.ParcelSearchMainAdapter;
import com.tpnagar.wrapper.SearchCompanyWrapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;
import static com.tpnagar.fragment.BrokarMainPaggerFragment.FromCityId;
import static com.tpnagar.fragment.BrokarMainPaggerFragment.FromSateId;
import static com.tpnagar.fragment.BrokarMainPaggerFragment.FromSateName;
import static com.tpnagar.fragment.BrokarMainPaggerFragment.Service_Id;
import static com.tpnagar.fragment.BrokarMainPaggerFragment.ToCityId;
import static com.tpnagar.fragment.BrokarMainPaggerFragment.ToSateId;
import static com.tpnagar.fragment.BrokarMainPaggerFragment.ToSateName;

public class ParcelListFragment extends BaseContainerFragment implements AdapterView.OnItemSelectedListener{
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
      ProgressDialog dialog=null;
    TextView  vendor_text;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.brokerlistnewfor_only_brokers, container, false);
   /*     FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Click action
                replaceFragmentTask(new SearchBrokerFragment());
            }
        });*/
        RelativeLayout  topbg=(RelativeLayout) getActivity().findViewById(R.id.topbg) ;

        topbg.setVisibility(View.GONE);
        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Click action
                getActivity().onBackPressed();
            }
        });
        requestWithSomeHttpHeaders();
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initXmlViews(view);
        city=DataManager.getInstance().getCity();
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

        normal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                normal.setTextColor(Color.RED);
                rating_text.setTextColor(Color.BLACK);
                vendor_text.setTextColor(Color.BLACK);
            }
        });

        vendor_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                normal.setTextColor(Color.BLACK);
                rating_text.setTextColor(Color.BLACK);
                vendor_text.setTextColor(Color.RED);
            }
        });
        rating_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rating_text.setTextColor(Color.RED);
                normal.setTextColor(Color.BLACK);
                vendor_text.setTextColor(Color.BLACK);

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
         city=DataManager.getInstance().getCity();

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

        getTokenRequest(null);

    }

    public void requestWithSomeHttpHeaders() {


        SearchText=getArguments().getString("SearchText");
        Location=getArguments().getString("Location");
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
        if(getArguments().getString("ToSateName")!=null){
            ToSateName=getArguments().getString("ToSateName");
        }


        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url="";
       // url = Const.URL_MainSearch+"brokercompany?fromcity="+FromCity+"&tocity="+ToCity+"&service=parcel service";

        url = Const.URL_MainSearch+"company?location=" + FromCity + "&city=" + ToCity + "&service=" + SearchText + "&state=" + ToSateName;


       /*if(SearchText.length()>0)
            url = "http://103.25.128.118:5001/api/search/brokercompany?fromcity="+FromCity+"&tocity="+ToCity;

            //  url = "http://103.25.128.118:5001/api/search/brokercompany?fromcity="+FromCity+"&tocity="+ToCity+"&service="+SearchText;
        }else {
            url = "http://103.25.128.118:5001/api/search/brokercompany?fromcity="+FromCity+"&tocity="+ToCity+"&service=parcel service";
        }*/

        url=url.replaceAll(" ", "%20");
        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>()
                {

                    @Override
                    public void onResponse(String response) {
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
                                    searchCompanyWrapperlist.add(searchCompanyWrapper);

                                    // }

                                }

                                DataManager.getInstance().setSearchCompanyBrokerWrapper(searchCompanyWrapperlist);
                                DataManager.getInstance().setSearchCompanyWrapper(searchCompanyWrapperlist);
                                ParcelSearchMainAdapter mListAdapter = new ParcelSearchMainAdapter(getActivity(), searchCompanyWrapperlist ,transaction,Location, SearchText, FromCity,ToCity, FromCityId, ToCityId, Service_Id,0, ToSateName, ToSateId, FromSateName,FromSateId);
                                listview.setAdapter(mListAdapter);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            try {

                                if (dialog != null) {
                                    if (dialog.isShowing()) {
                                        dialog.dismiss();
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

                    if (dialog != null) {
                        if (dialog.isShowing()) {
                            dialog.dismiss();
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

                      requestWithSomeHttpHeaders();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {

                    if (dialog != null) {
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }
                    }

                } catch (Exception e) {
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
       /// getTokenRequest(null);
       /* if(DataManager.getInstance().getSearchCompanyWrapper()!=null){

            listview=(ListView) rootView.findViewById(R.id.listview);
            if( DataManager.getInstance().getSearchCompanyWrapper().size()>0){
                BrokerSearchMainAdapter mListAdapter = new BrokerSearchMainAdapter(getActivity(), DataManager.getInstance().getSearchCompanyWrapper() ,transaction);
                listview.setAdapter(mListAdapter);
            }
        }*/




    }
}
