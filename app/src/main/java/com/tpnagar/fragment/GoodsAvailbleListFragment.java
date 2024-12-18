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
import com.tpnagar.adapter.SearchGoodsAdapter;
import com.tpnagar.wrapper.PostGoodsWrapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;
import static com.tpnagar.fragment.BrokarMainPaggerFragment.Service_Id;

public class GoodsAvailbleListFragment extends BaseContainerFragment implements AdapterView.OnItemSelectedListener{
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

        FromCityId=getArguments().getString("FromCityId");
        ToCityId=getArguments().getString("ToCityId");
        Service_Id=getArguments().getString("Service_Id");


        SearchText=getArguments().getString("SearchText");
        Location=getArguments().getString("Location");
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

   /*     PostGoodsWrapper postGoodsWrapper=new PostGoodsWrapper();
        postGoodsWrapper.setCompName("Company_Name");
        ArrayList<PostGoodsWrapper> postGoodsWrapperlist=new ArrayList<PostGoodsWrapper>();


        postGoodsWrapperlist.add(postGoodsWrapper);
        SearchGoodsAdapter mListAdapter = new SearchGoodsAdapter(getActivity(), postGoodsWrapperlist);
        listview.setAdapter(mListAdapter);*/
        if(AppController.isInternetPresent(getActivity())){
            searchApi();
        }else {
            AppController.popErrorMsg("Alert!", "please check internet connection",getActivity());

        }



    }



    void searchApi(){
        JSONObject obj = new JSONObject();

        try {


          //  bundle.putString("Location","");
          // bundle.putString("SearchText","");
          //  obj.put("From_City", FromCityId);
           // obj.put("To_City", ToCityId);
            FromCityId=getArguments().getString("FromCityId");

            ToCityId=getArguments().getString("ToCityId");
            ToSateId=getArguments().getString("ToSateId");
            obj.put("To_State", ToSateId);
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



            AppController.spinnerStart(getActivity());

            Log.e("this is obj:::", obj.toString());


            FragmentManager fm = getFragmentManager();
            BrokarMainPaggerFragment fragm = (BrokarMainPaggerFragment)fm.findFragmentById(R.id.frame_container);
            fragm.updateTitleData(0,0,"GOODS AVAILABLE");



            searchRequest(obj);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



   private void searchRequest(JSONObject params) {


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, Const.URL_getpostedgoods,
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

                ArrayList<PostGoodsWrapper> postGoodsWrapperlist=new ArrayList<PostGoodsWrapper>();
                Log.e("Search data", response.toString());
                try {
                    String StatusValue = response.getString("StatusValue");

                    if (StatusValue.equalsIgnoreCase("Success"))


                    {
                        JSONArray jsonArray = response.getJSONArray("Data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                           /* {
                                "Id": 17,
                                    "Company_Id": 236,
                                    "Contact_Person": "Contact Person",
                                    "CompName": "null",
                                    "Email": "email@gmaiil.com",
                                    "Phone_No": "phoneno",
                                    "Mobile_No": "9999999999",
                                    "Need_Vehicle": false,
                                    "Goods_Detail": "goods details",
                                    "Description": "descc",
                                    "From_State": 11,
                                    "From_State_Name": "Jharkhand",
                                    "From_City": 218,
                                    "From_City_Name": "Jamtara",
                                    "To_State": 14,
                                    "To_State_Name": "Madhya Pradesh",
                                    "To_City": 281,
                                    "To_City_Name": "Datia",
                                    "IsActive": false,
                                    "Active_Till": "0001-01-01T00:00:00",
                                    "Disposition_Id": 0,
                                    "Notes": null,
                                    "UserId": 0,
                                    "Version": null,
                                    "Tenant": null,
                                    "ActivityId": null*/

                            PostGoodsWrapper postGoodsWrapper=new PostGoodsWrapper();

                            JSONObject obj = jsonArray.getJSONObject(i);
                            postGoodsWrapper.setId(obj.getString("Id"));
                            postGoodsWrapper.setFreight(obj.getString("Freight"));
                            postGoodsWrapper.setCompany_Id(obj.getString("Company_Id"));
                            postGoodsWrapper.setCompName(obj.getString("CompName"));
                            postGoodsWrapper.setEmail(obj.getString("Email"));
                            postGoodsWrapper.setPhone_No(obj.getString("Phone_No"));
                            postGoodsWrapper.setMobile_Nod(obj.getString("Mobile_No"));
                            postGoodsWrapper.setGoods_Detail(obj.getString("Service_Name"));
                            postGoodsWrapper.setDescription(obj.getString("Description"));
                            postGoodsWrapper.setFrom_State(obj.getString("From_State"));
                            postGoodsWrapper.setFrom_State_Name(obj.getString("From_State_Name"));
                            postGoodsWrapper.setFrom_City(obj.getString("From_City"));
                            postGoodsWrapper.setTo_State_Name(obj.getString("To_State_Name"));
                            postGoodsWrapper.setTo_State(obj.getString("To_State"));
                            postGoodsWrapper.setTo_State_Name(obj.getString("To_State_Name"));
                            postGoodsWrapper.setTo_City(obj.getString("To_City"));
                            postGoodsWrapper.setTo_City_Name(obj.getString("To_City_Name"));
                            postGoodsWrapper.setMobile_Nod(obj.getString("Mobile_No"));
                            postGoodsWrapper.setContact_Person(obj.getString("Contact_Person"));
                            postGoodsWrapper.setIsActive(""+obj.getBoolean("IsActive"));

                            postGoodsWrapper.setCreated_On(obj.getString("Created_On"));


                            postGoodsWrapperlist.add(postGoodsWrapper);
                        }
                        FragmentManager fm = getFragmentManager();
                        BrokarMainPaggerFragment fragm = (BrokarMainPaggerFragment)fm.findFragmentById(R.id.frame_container);
                        fragm.updateTitleData(postGoodsWrapperlist.size(),0,"GOODS AVAILABLE");
                        BrokarMainPaggerFragment.goodsCount=postGoodsWrapperlist.size();
                        SearchGoodsAdapter mListAdapter = new SearchGoodsAdapter(getActivity(), postGoodsWrapperlist);
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
