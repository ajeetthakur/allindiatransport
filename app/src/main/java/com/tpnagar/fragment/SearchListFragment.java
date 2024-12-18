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
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.content.ContextCompat;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
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
import com.tpnagar.adapter.SearchMainAdapter;
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

public class SearchListFragment extends BaseContainerFragment implements AdapterView.OnItemSelectedListener{
    SharedPreferences prefs = null;
    ConnectionDetector cd;
    String cityId="1",categoryId="1";
    ImageView transportal_directory,broker_directory,parcel_service,distance_calculator,post_goods,post_vehicle;

    String[] services;
    private boolean rootViewId;
    ImageButton search;
    ArrayList<String> city ;
    ArrayList<String> servicesaStrings = new ArrayList<String>();
    CharSequence[]  serviceItem;
    ListView listview;
    FragmentTransaction transaction;
    View rootView;
    TextView trusted,rating_text;
    String  SearchText="";
    String  Location="";
    String FromCity,ToCity;
    ArrayList<String> cityid;
    AutoCompleteTextView   spinner_autocompleCity,actvnew;
    ArrayList<SearchCompanyWrapper> getSearchCompanyBrokerWrapperFIRST;
    ProgressDialog dialognew=null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.searchlistnew, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initXmlViews(view);

       RelativeLayout rel_noti=getActivity().findViewById(R.id.rel_noti);
       if (rel_noti!=null)
        rel_noti.setVisibility(View.GONE);

        city=DataManager.getInstance().getCity();
        cityid=DataManager.getInstance().getCityIds();
        transaction = getActivity().getSupportFragmentManager().beginTransaction();


    }

boolean isFirstTime=true;
    private void initXmlViews(View view) {
        cd = new ConnectionDetector(getActivity());



        search=(ImageButton) view.findViewById(R.id.search);


           actvnew = (AutoCompleteTextView) getActivity().findViewById(R.id.autoCompleteTextView1);

        spinner_autocompleCity = (AutoCompleteTextView) getActivity().findViewById(R.id.spinner_autocompleCity);


        prefs = getActivity().getSharedPreferences("com.tpnagar", MODE_PRIVATE);


        listview=(ListView) view.findViewById(R.id.listview);

        trusted=(TextView) view.findViewById(R.id.trusted);
                rating_text=(TextView) view.findViewById(R.id.rating_text);

        trusted.setTextColor(Color.RED);
        rating_text.setTextColor(Color.BLACK);

        trusted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isFirstTime){
                    isFirstTime=true;
                    trusted.setTextColor(Color.RED);
                    rating_text.setTextColor(Color.BLACK);

                    Collections.reverse(getSearchCompanyBrokerWrapperFIRST);
                    SearchMainAdapter mListAdapter = new SearchMainAdapter(getActivity(),getSearchCompanyBrokerWrapperFIRST ,transaction);
                    listview.setAdapter(mListAdapter);
                }


            }
        });
        rating_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isFirstTime){
                    rating_text.setTextColor(Color.RED);
                    trusted.setTextColor(Color.BLACK);
                    isFirstTime=false;
                    ArrayList<SearchCompanyWrapper> getSearchCompanyBrokerWrapper;
                    getSearchCompanyBrokerWrapper=getSearchCompanyBrokerWrapperFIRST;

                    ArrayList<SearchCompanyWrapper> getSearchCompanyBrokerWrappernew=getSearchCompanyBrokerWrapper;
                    Collections.sort(getSearchCompanyBrokerWrappernew, Ratingsort);
                    Collections.reverse(getSearchCompanyBrokerWrappernew);


                    SearchMainAdapter mListAdapter = new SearchMainAdapter(getActivity(), getSearchCompanyBrokerWrappernew ,transaction);
                    listview.setAdapter(mListAdapter);
                }


            }
        });
        SearchText=getArguments().getString("SearchText");
        Location=getArguments().getString("Location");
        actvnew.setText(SearchText);


        if(getArguments().getString("FromCity")!=null){
           FromCity=getArguments().getString("FromCity");
        }

        if(getArguments().getString("ToCity")!=null){
             ToCity=getArguments().getString("ToCity");
        }


        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                  if(actvnew.getText()==null){
                      SearchText="";
                  }else {
                      SearchText=actvnew.getText().toString();
                  }
                  Location=spinner_autocompleCity.getText().toString();
                searchApi();



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



            city=DataManager.getInstance().getCity();
        searchApi();

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


          //  bundle.putString("Location","");
          // bundle.putString("SearchText","");
            AutoCompleteTextView    spinner_autocompleCity = (AutoCompleteTextView) getActivity().findViewById(R.id.spinner_autocompleCity);


            obj.put("SearchText",SearchText );
            obj.put("Location", Location);
            obj.put("FromState", "");

            if(FromCity!=null){
                obj.put("FromCity", FromCity);
            }else {
                obj.put("FromCity", "");
            }
            if(ToCity!=null){
                obj.put("ToCity", ToCity);
            }else {
                obj.put("ToCity", "");
            }
            obj.put("ToState", "");


            getTokenRequest(null);
           dialognew = ProgressDialog.show(getContext(), "", "Loading...", true);



            Log.e("this is obj:::", obj.toString());

//PremPrajapat
           // searchRequest(obj);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void requestWithSomeHttpHeaders() {

        try{


            RequestQueue queue = Volley.newRequestQueue(getActivity());


                String url = Const.URL_MainSearch+"company?location=" + Location +"&service=" + SearchText;

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


                                        if(jsonArray.length()>0){
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            SearchCompanyWrapper searchCompanyWrapper=new SearchCompanyWrapper();



                                            JSONObject obj = jsonArray.getJSONObject(i);
                                            searchCompanyWrapper.set_id(obj.getString("_id"));
                                            searchCompanyWrapper.setCompany_Id(obj.getString("Company_Id"));
                                            searchCompanyWrapper.setService_Id(obj.getString("Service_Id"));
                                            searchCompanyWrapper.setCompany_Name(obj.getString("Company_Name"));


                                            searchCompanyWrapper.setService_Type(obj.getString("Service_Type"));
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

                                            getSearchCompanyBrokerWrapperFIRST=searchCompanyWrapperlist;

                                            // }
                                            if(searchCompanyWrapperlist!=null){

                                                if(searchCompanyWrapperlist.size()>0){
                                                    transaction = getActivity().getSupportFragmentManager().beginTransaction();

                                                    listview=(ListView) rootView.findViewById(R.id.listview);
                                                    SearchMainAdapter mListAdapter = new SearchMainAdapter(getActivity(), searchCompanyWrapperlist ,transaction);
                                                    listview.setAdapter(mListAdapter);

                                                }

                                            }
                                            DataManager.getInstance().setSearchCompanyWrapper(searchCompanyWrapperlist);

                                        }}
                                        else {

                                            ArrayList<SearchCompanyWrapper > searchCompanyWrapperlistempty=new ArrayList<SearchCompanyWrapper>();


                                            SearchMainAdapter mListAdapter = new SearchMainAdapter(getActivity(), searchCompanyWrapperlistempty ,transaction);
                                            listview.setAdapter(mListAdapter);
                                            AppController.popErrorMsg("Alert!","Data not found ",getActivity());

                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
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






        }catch (Exception e){
            AppController.spinnerStop();
        }




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
      //  jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(80 * 1000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        jsonObjReq.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
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

                        requestWithSomeHttpHeaders();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        };
        return response;
    }



   /* private void searchRequest(JSONObject params) {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, Const.URL_GetSearchResult,
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

                if (progressDoalog != null) {
                    if (progressDoalog.isShowing()) {
                        progressDoalog.dismiss();
                    }
                }
            }
        };
        return response_error;
    }

    private Response.Listener<JSONObject> newFromCityResponseRequesr() {
        Response.Listener<JSONObject> response = new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                if (progressDoalog != null) {
                    if (progressDoalog.isShowing()) {
                        progressDoalog.dismiss();
                    }
                }
                ArrayList<SearchCompanyWrapper > searchCompanyWrapperlist=new ArrayList<SearchCompanyWrapper>();
                Log.e("Search data", response.toString());
                try {
                    String StatusValue = response.getString("StatusValue");

                    if (StatusValue.equalsIgnoreCase("Success"))


                    {
                        JSONArray jsonArray = response.getJSONArray("Data");
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

                            searchCompanyWrapper.setTPNagar_Trusted(obj.getString("TPNagar_Trusted"));
                            searchCompanyWrapper.setRating(obj.getString("Rating"));
                           // searchCompanyWrapper.setRating("2.5");
                            searchCompanyWrapper.setCompany_Type_Id(obj.getString("Company_Type_Id"));
                            searchCompanyWrapper.setCurrent_Status_Id(obj.getString("Current_Status_Id"));
                            searchCompanyWrapperlist.add(searchCompanyWrapper);

                        }




                        if(searchCompanyWrapperlist!=null){

                            if(searchCompanyWrapperlist.size()>0){
                                transaction = getActivity().getSupportFragmentManager().beginTransaction();

                                listview=(ListView) rootView.findViewById(R.id.listview);
                                SearchMainAdapter mListAdapter = new SearchMainAdapter(getActivity(), searchCompanyWrapperlist ,transaction);
                                listview.setAdapter(mListAdapter);

                            }

                        }
                        DataManager.getInstance().setSearchCompanyWrapper(searchCompanyWrapperlist);
                        if (progressDoalog != null) {
                            if (progressDoalog.isShowing()) {
                                progressDoalog.dismiss();
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };
        return response;
    }*/
    public void replaceFragmentTask(Fragment fragment) {

      /*  transaction.setCustomAnimations(
                R.anim.slide_in_from_right, R.anim.slide_out_to_left,
                R.anim.slide_in_from_left, R.anim.slide_out_to_right);*/
        transaction.replace(R.id.frame_container, fragment, "");
        transaction.addToBackStack(null);
        // transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.commit();
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
        /*if(actv.getText()==null){
            SearchText="";
        }else {
            SearchText=actv.getText().toString();
        }
        Location=spinner.getSelectedItem().toString();
        searchApi();*/
      /*  if(DataManager.getInstance().getSearchCompanyWrapper()!=null){

            if(DataManager.getInstance().getSearchCompanyWrapper().size()>0){
                listview=(ListView) rootView.findViewById(R.id.listview);
                SearchMainAdapter mListAdapter = new SearchMainAdapter(getActivity(), DataManager.getInstance().getSearchCompanyWrapper() ,transaction);
                listview.setAdapter(mListAdapter);


            }

        }*/
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
