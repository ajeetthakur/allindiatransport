package com.tpnagar.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
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
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.tpnagar.AppController;
import com.tpnagar.BaseContainerFragment;
import com.tpnagar.ConnectionDetector;
import com.tpnagar.Const;
import com.tpnagar.DataManager;
import com.tpnagar.R;
import com.tpnagar.adapter.SearchVehicleAdapter;
import com.tpnagar.wrapper.PostVehicleWrapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class VehicleAvailbleListFragment extends BaseContainerFragment implements AdapterView.OnItemSelectedListener{
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
    TextView rating_text,normal,vendor_text;

    String  SearchText="";
    String  Location="",Service_Id="";
    String FromCity,ToCity,FromCityId,ToCityId;
    String ToSateName,ToSateId,FromSateName,FromSateId;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.brokerlistnew, container, false);
        LinearLayout upr_lin=(LinearLayout) rootView.findViewById(R.id.upr_lin);
        upr_lin.setVisibility(View.GONE);
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

        normal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                normal.setTextColor(Color.RED);
                rating_text.setTextColor(Color.BLACK);
            }
        });
        rating_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rating_text.setTextColor(Color.RED);
                normal.setTextColor(Color.BLACK);

            }
        });

        ToSateName=getArguments().getString("ToSateName");
        ToSateId=getArguments().getString("ToSateId");
        FromSateName=getArguments().getString("FromSateName");
        FromSateId=getArguments().getString("FromSateId");


        SearchText=getArguments().getString("SearchText");
        Location=getArguments().getString("Location");
        Service_Id=getArguments().getString("Service_Id");
        FromCityId=getArguments().getString("FromCityId");
        ToCityId=getArguments().getString("ToCityId");
        actv.setText(SearchText);


        if(getArguments().getString("FromCity")!=null){
           FromCity=getArguments().getString("FromCity");
        }

        if(getArguments().getString("ToCity")!=null){
             ToCity=getArguments().getString("ToCity");
        }
        String FromCity,ToCity;



        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                  if(actv.getText()==null){
                      SearchText="";
                  }else {
                      SearchText=actv.getText().toString();
                  }
                  Location=spinner.getSelectedItem().toString();
                searchApi();



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
   /*     ArrayList<PostVehicleWrapper> postVehicleWrapperlist=new ArrayList<PostVehicleWrapper>();

        PostVehicleWrapper postVehicleWrapper=new PostVehicleWrapper();
        postVehicleWrapper.setCompName("Company Name");
        postVehicleWrapperlist.add(postVehicleWrapper);
        SearchVehicleAdapter mListAdapter = new SearchVehicleAdapter(getActivity(), postVehicleWrapperlist);
        listview.setAdapter(mListAdapter);*/

        if(AppController.isInternetPresent(getActivity())){
            searchApi();
        }else {
            AppController.popErrorMsg("Alert!", "please check internet connection",getActivity());

        }


    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SearchText=getArguments().getString("SearchText");
        Location=getArguments().getString("Location");



    }


    void searchApi(){
        JSONObject obj = new JSONObject();

        try {

            FromCityId=getArguments().getString("FromCityId");
            ToCityId=getArguments().getString("ToCityId");
            obj.put("From_City", FromCityId);

            if ( ToCityId!=null){

                if (ToCityId.equalsIgnoreCase("")){
                    ToCityId="0";
                }
            }else {
                ToCityId="0";
            }
            obj.put("To_City", ToCityId);
            obj.put("Service_Id", Service_Id);
            ToSateId=getArguments().getString("ToSateId");
            obj.put("To_State", ToSateId);
            AppController.spinnerStart(getActivity());

            Log.e("this is obj:::", obj.toString());

            FragmentManager fm = getFragmentManager();
            BrokarMainPaggerFragment fragm = (BrokarMainPaggerFragment)fm.findFragmentById(R.id.frame_container);
            fragm.updateTitleData(0,2,"VEHICLES AVAILABLE");


            searchRequest(obj);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

   /* public void searchRequestPost(String url, final Map<String, String> json) {
       AppController.spinnerStart(getActivity());

        StringRequest post = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ArrayList<SearchCompanyWrapper > searchCompanyWrapperlist=new ArrayList<SearchCompanyWrapper>();
                Log.d("searchRequestPost", response.toString());
                try {
                    AppController.spinnerStop();


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
                       // searchCompanyWrapper.setRating(obj.getString("Rating"));
                        searchCompanyWrapper.setRating("2.5");
                        searchCompanyWrapper.setCompany_Type_Id(obj.getString("Company_Type_Id"));
                        searchCompanyWrapper.setCurrent_Status_Id(obj.getString("Current_Status_Id"));
                        searchCompanyWrapperlist.add(searchCompanyWrapper);

                    }
                    SearchMainAdapter mListAdapter = new SearchMainAdapter(getActivity(), searchCompanyWrapperlist);
                    listview.setAdapter(mListAdapter);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("onErrorResponse ", error.toString());
              AppController.popErrorMsg("Error!",error.toString(),getActivity());

               AppController.spinnerStop();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = json;

                return map;
            }
        };
        post.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppController.getInstance().getRequestQueue().add(post);

    }
*/

   private void searchRequest(JSONObject params) {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, Const.URL_getpostedvehicles,
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

       AppController.getInstance().addToRequestQueue(jsonObjReq, "URL_GetSearchResult");
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
                AppController.spinnerStop();

                ArrayList<PostVehicleWrapper> postVehicleWrapperlist=new ArrayList<PostVehicleWrapper>();
                Log.e("Search data", response.toString());
                try {
                    String StatusValue = response.getString("StatusValue");

                    if (StatusValue.equalsIgnoreCase("Success"))


                    {
                        JSONArray jsonArray = response.getJSONArray("Data");
                        for (int i = 0; i < jsonArray.length(); i++) {

                            PostVehicleWrapper postVehicleWrapper = new PostVehicleWrapper();
                            JSONObject obj = jsonArray.getJSONObject(i);
                            postVehicleWrapper.setVehicle_Detail(obj.getString("Service_Name"));
                            postVehicleWrapper.setVehicle_No(obj.getString("Vehicle_No"));
                            postVehicleWrapper.setCompany_Id(obj.getString("Company_Id"));
                            postVehicleWrapper.setContact_Person(obj.getString("Contact_Person"));
                            postVehicleWrapper.setTo_City_Name(obj.getString("To_City_Name"));
                            postVehicleWrapper.setTo_State_Name(obj.getString("To_State_Name"));
                            postVehicleWrapper.setFrom_City_Name(obj.getString("From_City_Name"));
                            postVehicleWrapper.setFrom_State_Name(obj.getString("From_State_Name"));
                            postVehicleWrapper.setTo_City(obj.getString("To_City"));
                            postVehicleWrapper.setTo_State(obj.getString("To_State"));
                            postVehicleWrapper.setFrom_State(obj.getString("From_State"));
                            postVehicleWrapper.setFrom_City(obj.getString("From_City"));

                            postVehicleWrapper.setPhone_No(obj.getString("Phone_No"));
                            postVehicleWrapper.setMobile_No(obj.getString("Mobile_No"));
                            postVehicleWrapper.setId(obj.getString("Id"));

                            postVehicleWrapper.setEmail(obj.getString("Email"));
                            postVehicleWrapper.setCompName(obj.getString("CompName"));
                            postVehicleWrapper.setContact_Person(obj.getString("Contact_Person"));
                            postVehicleWrapper.setDescription(obj.getString("Description"));
                            postVehicleWrapper.setCreated_On(obj.getString("Created_On"));


                            postVehicleWrapperlist.add(postVehicleWrapper);

                        }

                        FragmentManager fm = getFragmentManager();
                        BrokarMainPaggerFragment fragm = (BrokarMainPaggerFragment)fm.findFragmentById(R.id.frame_container);
                        fragm.updateTitleData(postVehicleWrapperlist.size(),2,"VEHICLES AVAILABLE");
                        BrokarMainPaggerFragment.vehiclesCount=postVehicleWrapperlist.size();
                        SearchVehicleAdapter mListAdapter = new SearchVehicleAdapter(getActivity(), postVehicleWrapperlist);
                        listview.setAdapter(mListAdapter);

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


                AppController.spinnerStop();
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
}
