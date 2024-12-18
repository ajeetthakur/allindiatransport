package com.tpnagar.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

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
import com.tpnagar.wrapper.CitiesOfStateWrapper;
import com.tpnagar.wrapper.GetComapnyServiceWrapper;
import com.tpnagar.wrapper.StateWrapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;
import static com.tpnagar.fragment.BrokarMainPaggerFragment.SearchText;

/**
 * Created by admin on 03-03-2016.
 */
public class SearchBrokerFragment extends BaseContainerFragment implements AdapterView.OnItemSelectedListener{

    SharedPreferences prefs = null;
    ConnectionDetector cd;
   // private AutoCompleteTextView actv;
    String[] services;
   // Spinner spinner;
    private boolean rootViewId;
    ImageButton search;
    CharSequence[]  serviceItem;
    String stringServiceId="";
    ArrayList<String> city = new ArrayList<String>();
    ArrayList<String> servicesaStrings = new ArrayList<String>();
    String stringstateIdfrom="0",stringcityIdfrom="0",stringstateIdto="0",stringcityIdto="0",stringStateNameTo="",stringStateNameFrom="";
    CharSequence[]  fromlistItem_State, fromlistItem_City,tolistItem_City;/*,tolistItem_State, ;*/
    TextView txtfrom_state,txtfrom_city,txtto_state,txtto_city,text_brokerservice;
    View rootView;
    ToggleButton switchButton;
    EditText edit_search_text;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.broker_search, container, false);
        ((com.tpnagar.designdemo.MainActivity) getActivity())
                .getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#FFFFFF\">" +"Broker search" + "</font>")));
        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Click action
                RelativeLayout topbg=(RelativeLayout) getActivity().findViewById(R.id.topbg) ;

                topbg.setVisibility(View.VISIBLE);
                getActivity().onBackPressed();

            }
        });

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initXmlViews(view);
        brokerServicesRequest(null);
    }

    @Override
    public void onResume() {
        super.onResume();
        RelativeLayout topbg=(RelativeLayout) getActivity().findViewById(R.id.topbg) ;

        topbg.setVisibility(View.GONE);
    }

    private void initXmlViews(View view) {
        cd = new ConnectionDetector(getActivity());

        switchButton=(ToggleButton) view.findViewById(R.id.switchButton);

        edit_search_text=(EditText) view.findViewById(R.id.edit_search_text);
        txtfrom_state=(TextView) view.findViewById(R.id.txtfrom_state);
        txtfrom_city=(TextView) view.findViewById(R.id.txtfrom_city);
        txtto_state=(TextView) view.findViewById(R.id.txtto_state);
        txtto_city=(TextView) view.findViewById(R.id.txtto_city);
        text_brokerservice=(TextView)view.findViewById(R.id.text_brokerservice);




        switchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(switchButton.isChecked()){

                    txtfrom_state.setVisibility(View.GONE);
                    txtfrom_city.setVisibility(View.GONE);
                    txtto_state.setVisibility(View.GONE);
                    txtto_city.setVisibility(View.GONE);
                    text_brokerservice.setVisibility(View.GONE);

                    edit_search_text.setVisibility(View.VISIBLE);


                }else {
                    txtfrom_state.setVisibility(View.VISIBLE);
                    txtfrom_city.setVisibility(View.VISIBLE);
                    txtto_state.setVisibility(View.VISIBLE);
                    txtto_city.setVisibility(View.VISIBLE);
                    text_brokerservice.setVisibility(View.VISIBLE);

                    edit_search_text.setVisibility(View.GONE);

                }

            }
        });

        prefs = getActivity().getSharedPreferences("com.tpnagar", MODE_PRIVATE);

        //prefs.getString("BrokerSearch_Location",Location).commit();
        SearchText=prefs.getString("BrokerSearch_SearchText","");




       // stringcityIdto= prefs.getString("BrokerSearch_ToCityId","");
        stringServiceId= prefs.getString("BrokerSearch_Service_Id","");
      //  stringStateNameTo= prefs.getString("BrokerSearch_ToSateName","");
      //  stringstateIdto=prefs.getString("BrokerSearch_ToSateId","");

        stringStateNameFrom= prefs.getString("BrokerSearch_FromSateName","");
        stringstateIdfrom=prefs.getString("BrokerSearch_FromSateId","");
        stringcityIdfrom= prefs.getString("BrokerSearch_FromCityId","");


        if(stringstateIdto.length()>0){
            if(AppController.isInternetPresent(getActivity())){

                JSONObject obj = new JSONObject();

                try {
                    obj.put("LoginId", "");
                    AppController.spinnerStart(getActivity());
                    Log.e("this is obj:::", obj.toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                serviceCitiesOfStateToRequest(obj);
            }else {
                AppController.popErrorMsg("Alert!","Please Connect with working internet.",getActivity());

            }
        }

        if(stringstateIdfrom.length()>0){
            if(AppController.isInternetPresent(getActivity())){

                JSONObject obj = new JSONObject();

                try {
                    obj.put("LoginId", "");
                    AppController.spinnerStart(getActivity());
                    Log.e("this is obj:::", obj.toString());
                    serviceCitiesOfStateRequest(obj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }else {
                AppController.popErrorMsg("Alert!","Please Connect with working internet.",getActivity());

            }
        }



       /* if(prefs.getString("BrokerSearch_ToCity","").length()>0){
            txtto_city .setText(prefs.getString("BrokerSearch_ToCity",""));
        }*/

        if(prefs.getString("BrokerSearch_FromCity","").length()>0){
            txtfrom_city .setText(prefs.getString("BrokerSearch_FromCity",""));
        }

      /*  if(stringStateNameTo.length()>0){
            txtto_state.setText(stringStateNameTo);
        }*/

        if(stringStateNameFrom.length()>0){
            txtfrom_state.setText(stringStateNameFrom);
        }
        if(prefs.getString("BrokerSearch_SearchText","").length()>0){
            text_brokerservice.setText(SearchText);
        }

        text_brokerservice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showListService();
            }
        });
       // actv = (AutoCompleteTextView) view.findViewById(R.id.autoCompleteTextView1);
      //  actv.clearFocus();
        //spinner = (Spinner) view.findViewById(spinner);
       // spinner.setOnItemSelectedListener(this);




        txtfrom_state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(AppController.isInternetPresent(getActivity())){
                    fromshowListState();
                }else {
                    AppController.popErrorMsg("Alert!","Please Connect with working internet.",getActivity());

                }



            }
        });
        txtfrom_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s=txtfrom_state.getText().toString();
                txtfrom_city.getText().toString();
                if ( !s.equalsIgnoreCase("From State")) {
                    if(AppController.isInternetPresent(getActivity())){
                        fromshowListCity();
                    }else {
                        AppController.popErrorMsg("Alert!","Please Connect with working internet.",getActivity());

                    }

                }else {
                    AppController.popErrorMsg("Alert!","Please Select State before",getActivity());
                }

            }
        });
        txtto_state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(AppController.isInternetPresent(getActivity())){
                    toshowListState();
                }else {
                    AppController.popErrorMsg("Alert!","Please Connect with working internet.",getActivity());

                }

            }
        });
        txtto_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s=txtto_state.getText().toString();
                if ( !s.equalsIgnoreCase("To State")) {

                    if(AppController.isInternetPresent(getActivity())){
                        toshowListCity();
                    }else {
                        AppController.popErrorMsg("Alert!","Please Connect with working internet.",getActivity());

                    }

                }else {
                    AppController.popErrorMsg("Alert!","Please Select State before",getActivity());
                }

            }
        });


        if(AppController.isInternetPresent(getActivity())){
            stasteApi();

        }else {
            AppController.popErrorMsg("Alert!","Please Connect with working internet.",getActivity());

        }



        search=(ImageButton) view.findViewById(R.id.search);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(switchButton.isChecked()){
                    if (AppController.isInternetPresent(getActivity())) {


                        if (edit_search_text.getText().toString().isEmpty()) {

                            AppController.showToast(getActivity(), "please entere search text");
                            return;

                        }

                        switchButton.setChecked(false);

                     Bundle bundle = new Bundle();
                        bundle.putString("SearchTextOnly", edit_search_text.getText().toString());

                        BrokerListSearchByNameFragment searchListFragment = new BrokerListSearchByNameFragment();
                        searchListFragment.setArguments(bundle);
                        replaceFragmentTask(searchListFragment);
                    } else {
                        AppController.popErrorMsg("Alert!", "Please Connect with working internet.", getActivity());

                    }


                }else {

                    if (text_brokerservice.getText().toString() == null || text_brokerservice.getText().toString().equals("Broker Service")) {

                        AppController.showToast(getActivity(), "please select Broker Service");
                        return;

                    }
                    if (txtfrom_state.getText().toString() == null || txtfrom_state.getText().toString().equals("From State")) {

                        AppController.showToast(getActivity(), "please select From State");
                        return;

                    }
                    if (txtfrom_city.getText().toString() == null || txtfrom_city.getText().toString().equals("From City")) {

                        AppController.showToast(getActivity(), "please select From City");
                        return;

                    }
               /* if (txtto_state.getText().toString() == null || txtto_state.getText().toString().equals("To State")) {

                    AppController.showToast(getActivity(), "please select To State");
                    return;

                }*/
               /* if (txtto_city.getText().toString() == null || txtto_city.getText().toString().equals("To City")) {

                    AppController.showToast(getActivity(), "please select To City");
                    return;

                }*/


                    if (AppController.isInternetPresent(getActivity())) {
                        String SearchText = "";
                        String Location = "";

                        // spinner.getSelectedItemPosition();

                        if (text_brokerservice.getText() == null) {
                            SearchText = "";
                        } else {
                            SearchText = text_brokerservice.getText().toString();
                        }

                        AppController.BrokerServiceName = SearchText;
                        AppController.BrokerStateTo = txtto_state.getText().toString();
                        AppController.BrokerStateFrom = txtfrom_state.getText().toString();
                        //  prefs.edit().putString("BrokerCityTo",txtto_city.getText().toString()).commit();
                        // prefs.edit().putString("BrokerCityFrom",txtfrom_city.getText().toString()).commit();
                        prefs.edit().putString("BrokerStateTo", txtto_state.getText().toString()).commit();
                        prefs.edit().putString("BrokerStateFrom", txtfrom_state.getText().toString()).commit();
                        Log.e("Prem BrokerStateTo ", "" + stringStateNameTo);

                        Log.e("Prem BrokerStateFrom ", "" + stringStateNameFrom);
                        Bundle bundle = new Bundle();
                        bundle.putString("Location", Location);
                        bundle.putString("SearchText", SearchText);

                        bundle.putString("FromCity", txtfrom_city.getText().toString());

                        bundle.putString("ToCity", txtto_city.getText().toString());
                        bundle.putString("FromCityId", stringcityIdfrom);

                        bundle.putString("ToCityId", stringcityIdto);

                        bundle.putString("Service_Id", stringServiceId);
                        bundle.putInt("spinnerId", 0);


                        bundle.putString("ToSateName", stringStateNameTo);
                        bundle.putString("ToSateId", stringstateIdto);
                        bundle.putString("FromSateName", stringStateNameFrom);
                        bundle.putString("FromSateId", stringstateIdfrom);


                        prefs.edit().putString("BrokerSearch_Location", Location).commit();
                        prefs.edit().putString("BrokerSearch_SearchText", SearchText).commit();
                        prefs.edit().putString("BrokerSearch_Service_Id", stringServiceId).commit();


                        //FromCity
                        prefs.edit().putString("BrokerSearch_FromCity", txtfrom_city.getText().toString()).commit();
                        prefs.edit().putString("BrokerSearch_FromCityId", stringcityIdfrom).commit();


                        //ToCity
                        prefs.edit().putString("BrokerSearch_ToCity", txtto_city.getText().toString()).commit();
                        prefs.edit().putString("BrokerSearch_ToCityId", stringcityIdto).commit();

                        //ToSate
                        prefs.edit().putString("BrokerSearch_ToSateName", txtto_state.getText().toString()).commit();
                        prefs.edit().putString("BrokerSearch_ToSateId", stringstateIdto).commit();

                        //FromSate
                        prefs.edit().putString("BrokerSearch_FromSateName", txtfrom_state.getText().toString()).commit();
                        prefs.edit().putString("BrokerSearch_FromSateId", stringstateIdfrom).commit();
                        BrokarMainPaggerFragment searchListFragment = new BrokarMainPaggerFragment();
                        searchListFragment.setArguments(bundle);

                        if (AppController.isInternetPresent(getActivity())) {
                            replaceFragmentTask(searchListFragment);
                        } else {
                            AppController.popErrorMsg("Alert!", "Please Connect with working internet.", getActivity());

                        }


                    } else {

                        AppController.showToast(getActivity(), "please check internet connection");
                    }


                }

            }
        });
  /*      actv.addTextChangedListener(new TextWatcher() {

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
        });*/

        prefs = getActivity().getSharedPreferences("com.tpnagar", MODE_PRIVATE);

        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        RandomCityList();
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        // Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    private void replaceFragmentTask(Fragment fragment) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
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
        /*        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                        (getActivity(),android.R.layout.simple_list_item_1,services);
                actv.setAdapter(adapter);*/

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

                            DataManager.getInstance().setCityBroker(city);
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // Creating adapter for spinner
              //  ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, city);

                // Drop down layout style - list view with radio button
             //   dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                // attaching data adapter to spinner
              //  spinner.setAdapter(dataAdapter);


                AppController.spinnerStop();

            }
        };
        return response;
    }

    void stasteApi() {

        JSONObject obj = new JSONObject();

        try {


            obj.put("LoginId", "");


            AppController.spinnerStart(getActivity());

            Log.e("this is obj:::", obj.toString());


            serviceStatesOfCountryRequest(obj);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void serviceStatesOfCountryRequest(JSONObject params) {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, Const.URL_StatesOfCountry,
                params, newResponseRequesrt(), eErrorListenerRequesrt()) {
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

    private Response.ErrorListener eErrorListenerRequesrt() {
        Response.ErrorListener response_error = new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("this is error", error.toString());


                AppController.spinnerStop();
            }
        };
        return response_error;
    }

    public Response.Listener<JSONObject> newResponseRequesrt() {
        Response.Listener<JSONObject> response = new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                ArrayList<StateWrapper> stateWrappers = new ArrayList<StateWrapper>();
                Log.e("ServiceListOnCityAn", response.toString());
                ArrayList<String> arrayList1 = new ArrayList<String>();
                try {
                    String StatusValue = response.getString("StatusValue");

                    if (StatusValue.equalsIgnoreCase("Success"))


                    {
                        JSONArray jsonArray = response.getJSONArray("Data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            StateWrapper stateWrapper = new StateWrapper();
                            JSONObject obj = jsonArray.getJSONObject(i);
                            stateWrapper.setState_Name(obj.getString("State_Name"));
                            stateWrapper.setState_Code(obj.getString("State_Code"));
                            stateWrapper.setId(obj.getString("Id"));

                            arrayList1.add(obj.getString("State_Name"));
                            stateWrappers.add(stateWrapper);



                        }

                        DataManager.getInstance().setStateWrapper(stateWrappers);
                        fromlistItem_State = arrayList1
                                .toArray(new CharSequence[arrayList1.size()]);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                AppController.spinnerStop();

            }
        };
        return response;
    }


    private void fromshowListState() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Select State");

        builder.setItems(fromlistItem_State,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        txtfrom_state.setText(fromlistItem_State[item]);
                        txtfrom_city.setText("");
                        txtfrom_city.setHint("From City");

                        stringstateIdfrom = DataManager.getInstance()
                                .getStateWrapper().get(item)
                                .getId();
                        stringStateNameFrom=DataManager.getInstance()
                                .getStateWrapper().get(item)
                                .getState_Name();

                        JSONObject obj = new JSONObject();

                        try {
                            obj.put("LoginId", "");
                            AppController.spinnerStart(getActivity());
                            Log.e("this is obj:::", obj.toString());
                            serviceCitiesOfStateRequest(obj);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        System.out.println("item County " + item);
                    }
                });
        builder.create();
        builder.show();

    }

    private void fromshowListCity() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Select City");

        builder.setItems(fromlistItem_City,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        txtfrom_city.setText(fromlistItem_City[item]);

                        if(DataManager.getInstance()
                                .getCitiesOfStateWrapper()!=null){
                            stringcityIdfrom = DataManager.getInstance()
                                    .getCitiesOfStateWrapper().get(item)
                                    .getId();
                            prefs.edit().putString("BrokerCityFrom",fromlistItem_City[item].toString()).commit();
                        }


                        System.out.println("item txtfrom_city " + item);
                    }
                });
        builder.create();
        builder.show();

    }

    private void toshowListState() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Select State");

        builder.setItems(fromlistItem_State,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        txtto_state.setText(fromlistItem_State[item]);
                        txtto_city.setText("");
                        txtto_city.setHint("To City");
                        stringstateIdto = DataManager.getInstance()
                                .getStateWrapper().get(item)
                                .getId();

                        stringStateNameTo=DataManager.getInstance()
                                .getStateWrapper().get(item)
                                .getState_Name();



                        JSONObject obj = new JSONObject();

                        try {
                            obj.put("LoginId", "");
                            AppController.spinnerStart(getActivity());
                            Log.e("this is obj:::", obj.toString());
                            serviceCitiesOfStateToRequest(obj);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        System.out.println("item County " + item);
                    }
                });
        builder.create();
        builder.show();

    }

    private void toshowListCity() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Select City");

        builder.setItems(tolistItem_City,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        txtto_city.setText(tolistItem_City[item]);
                        stringcityIdto = DataManager.getInstance()
                                .getCitiesOfStateWrapperTO().get(item)
                                .getId();

                        System.out.println("item County " + item);
                    }
                });
        builder.create();
        builder.show();

    }

    private void serviceCitiesOfStateRequest(JSONObject params) {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, Const.URL_InnerSearchCity+stringstateIdfrom,
                params, newResponseRequesrtCitiesOfState(), eErrorListenerRequesrtCitiesOfState()) {
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

    private Response.ErrorListener eErrorListenerRequesrtCitiesOfState() {
        Response.ErrorListener response_error = new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("this is error", error.toString());


                AppController.spinnerStop();
            }
        };
        return response_error;
    }

    public Response.Listener<JSONObject> newResponseRequesrtCitiesOfState() {
        Response.Listener<JSONObject> response = new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                ArrayList<CitiesOfStateWrapper> citiesOfStateWrapperlist = new ArrayList<CitiesOfStateWrapper>();
                Log.e("ServiceListOnCityAn", response.toString());
                ArrayList<String> arrayList1 = new ArrayList<String>();
                try {
                    String StatusValue = response.getString("StatusValue");

                    if (StatusValue.equalsIgnoreCase("Success"))


                    {
                        JSONArray jsonArray = response.getJSONArray("Data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            CitiesOfStateWrapper citiesOfStateWrapper = new CitiesOfStateWrapper();
                            JSONObject obj = jsonArray.getJSONObject(i);
                            citiesOfStateWrapper.setCity_Name(obj.getString("City_Name"));
                            citiesOfStateWrapper.setId(obj.getString("Id"));

                            arrayList1.add(obj.getString("City_Name"));


                            citiesOfStateWrapperlist.add(citiesOfStateWrapper);

                        }
                        DataManager.getInstance().setCitiesOfStateWrapper(citiesOfStateWrapperlist);
                        fromlistItem_City = arrayList1
                                .toArray(new CharSequence[arrayList1.size()]);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                AppController.spinnerStop();

            }
        };
        return response;
    }


    private void serviceCitiesOfStateToRequest(JSONObject params) {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, Const.URL_CitiesOfState+stringstateIdto,
                params, newResponseRequesrtCitiesOfStateto(), eErrorListenerRequesrtCitiesOfStateto()) {
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

    private Response.ErrorListener eErrorListenerRequesrtCitiesOfStateto() {
        Response.ErrorListener response_error = new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("this is error", error.toString());


                AppController.spinnerStop();
            }
        };
        return response_error;
    }

    public Response.Listener<JSONObject> newResponseRequesrtCitiesOfStateto() {
        Response.Listener<JSONObject> response = new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                ArrayList<CitiesOfStateWrapper> citiesOfStateWrapperlist = new ArrayList<CitiesOfStateWrapper>();
                Log.e("ServiceListOnCityAn", response.toString());
                ArrayList<String> arrayList1 = new ArrayList<String>();
                try {
                    String StatusValue = response.getString("StatusValue");

                    if (StatusValue.equalsIgnoreCase("Success"))


                    {
                        JSONArray jsonArray = response.getJSONArray("Data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            CitiesOfStateWrapper citiesOfStateWrapper = new CitiesOfStateWrapper();
                            JSONObject obj = jsonArray.getJSONObject(i);
                            citiesOfStateWrapper.setCity_Name(obj.getString("City_Name"));
                            citiesOfStateWrapper.setId(obj.getString("Id"));

                            arrayList1.add(obj.getString("City_Name"));


                            citiesOfStateWrapperlist.add(citiesOfStateWrapper);

                        }


                        DataManager.getInstance().setCitiesOfStateWrapperTO(citiesOfStateWrapperlist);
                        tolistItem_City = arrayList1
                                .toArray(new CharSequence[arrayList1.size()]);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                AppController.spinnerStop();

            }
        };
        return response;
    }
    private void brokerServicesRequest(JSONObject params) {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, Const.URL_BrokerServices,
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
                            getComapnyServiceWrapperlist.add(getComapnyServiceWrapper);

                        }
                        DataManager.getInstance().setGetComapnyServiceWrapperlist(getComapnyServiceWrapperlist);
                        serviceItem =arrayList1.toArray(new CharSequence[arrayList1.size()]);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                AppController.spinnerStop();

            }
        };
        return response;
    }
    private void showListService() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Select Service");

        builder.setItems(serviceItem,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        text_brokerservice.setText(serviceItem[item]);
                        stringServiceId = DataManager.getInstance()
                                .getGetComapnyServiceWrapperlist().get(item)
                                .getId();




                    }
                });
        builder.create();
        builder.show();

    }
}
//http://api.tpnagar.com:5004/api/search/brokercompany?fromcity=na&service=har%20krishna&tostate=na&tocity=na
/*
Show vendor-:at company details page show vendor remove mobile and add city
        Short Data-:Vendor click make full design cilick of vendor (now its work only when we click on text)
        Company Search-: Broker Company search button in text pass numeric and alphabet
        Vendor List-: Show Hide Button as like on off button and make company name as capital latter
        Broker Search-: Show Mmassage no data found if no data there for exist line*/
