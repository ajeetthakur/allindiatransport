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
import android.widget.AutoCompleteTextView;
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

/**
 * Created by admin on 03-03-2016.
 */
public class SearchParcelServiceFragment extends BaseContainerFragment implements AdapterView.OnItemSelectedListener{
    CharSequence[]  serviceItemnew;
    SharedPreferences prefs = null;
    ConnectionDetector cd;
    String[] services;
   // Spinner spinner;
    private boolean rootViewId;
    ImageButton search;
    CharSequence[]  serviceItem;
    String stringServiceId="";
    ArrayList<String> city = new ArrayList<String>();
    ArrayList<String> servicesaStrings = new ArrayList<String>();
    String stringstateIdfrom="1",stringcityIdfrom="1",stringstateIdto="1",stringcityIdto="1";
    CharSequence[]  fromlistItem_State, fromlistItem_City,tolistItem_City;/*,tolistItem_State, ;*/
    TextView txtfrom_state,txtfrom_city,txtto_state,txtto_city,text_brokerservice;
    View rootView;
    String stringStateNameTo="",stringStateNameFrom="",stringCityNameFrom="",stringCityNameTo="";

    ToggleButton switchButton;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.broker_search, container, false);

        prefs = getActivity().getSharedPreferences("com.tpnagar", MODE_PRIVATE);
        if(prefs.getString("IsActive","").equalsIgnoreCase("yes"))
        {
            ((com.tpnagar.designdemo.MainActivity) getActivity())
                    .getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#FFFFFF\">" +"Parcel service search" + "</font>")));

        }else {
            ((com.tpnagar.designdemo.MainActivityWithoutLogin) getActivity())
                    .getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#FFFFFF\">" +"Parcel service search" + "</font>")));

        }

        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Click action
                getActivity().onBackPressed();
            }
        });
        brokerServicesRequest(null);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initXmlViews(view);


    }
    private void newshowListService() {
         serviceItemnew=DataManager.getInstance().getServiceItem();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Select Service");

        builder.setItems(serviceItemnew,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {

                        AutoCompleteTextView  actvnew = (AutoCompleteTextView) getActivity().findViewById(R.id.autoCompleteTextView1);

                        actvnew.dismissDropDown();
                        actvnew.setAdapter(null);
                        actvnew.setText(serviceItemnew[item].toString());
                        if(serviceItemnew[item].toString().equalsIgnoreCase("parcel service")){

                        }
                        else {


                            getActivity().onBackPressed();
                        }
                      /*  stringServiceId = DataManager.getInstance()
                                .getGetComapnyServiceWrapperlist().get(item)
                                .getId();*/




                    }
                });
        builder.create();
        builder.show();

    }

    private void initXmlViews(View view) {
        cd = new ConnectionDetector(getActivity());

        txtfrom_state=(TextView) view.findViewById(R.id.txtfrom_state);
        txtfrom_city=(TextView) view.findViewById(R.id.txtfrom_city);
        txtto_state=(TextView) view.findViewById(R.id.txtto_state);
        txtto_city=(TextView) view.findViewById(R.id.txtto_city);
        switchButton=(ToggleButton) view.findViewById(R.id.switchButton);
        switchButton.setVisibility(View.GONE);

        text_brokerservice=(TextView)view.findViewById(R.id.text_brokerservice);

      text_brokerservice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newshowListService();
            }
        });

                text_brokerservice.setVisibility(View.VISIBLE);
        text_brokerservice.setText("Parcel service");
        prefs = getActivity().getSharedPreferences("com.tpnagar", MODE_PRIVATE);


        txtfrom_state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromshowListState();
            }
        });
        txtfrom_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s=txtfrom_state.getText().toString();
                if ( !s.equalsIgnoreCase("From State")) {
                    fromshowListCity();
                }else {
                    AppController.popErrorMsg("Alert!","Please Select State before",getActivity());
                }

            }
        });
        txtto_state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toshowListState();
            }
        });
        txtto_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s=txtto_state.getText().toString();
                if ( !s.equalsIgnoreCase("To State")) {
                    toshowListCity();
                }else {
                    AppController.popErrorMsg("Alert!","Please Select State before",getActivity());
                }

            }
        });




        stringStateNameFrom= prefs.getString("ParcelSearch_FromSateName","");
        stringstateIdfrom=prefs.getString("ParcelSearch_FromSateId","");
        stringcityIdfrom= prefs.getString("ParcelSearch_FromCityId","");


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

        if(prefs.getString("ParcelSearch_FromCity","").length()>0){
            txtfrom_city .setText(prefs.getString("ParcelSearch_FromCity",""));
        }

      /*  if(stringStateNameTo.length()>0){
            txtto_state.setText(stringStateNameTo);
        }*/

        if(stringStateNameFrom.length()>0){
            txtfrom_state.setText(stringStateNameFrom);
        }



        stasteApi();

        search=(ImageButton) view.findViewById(R.id.search);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (txtfrom_state.getText().toString() == null || txtfrom_state.getText().toString().equals("From State")) {

                    AppController.showToast(getActivity(), "please select From State");
                    return;

                }
                if (txtfrom_city.getText().toString() == null || txtfrom_city.getText().toString().equals("From City")) {

                    AppController.showToast(getActivity(), "please select From City");
                    return;

                }
                if (txtto_state.getText().toString() == null || txtto_state.getText().toString().equals("To State")) {

                    AppController.showToast(getActivity(), "please select To State");
                    return;

                }
                if (txtto_city.getText().toString() == null || txtto_city.getText().toString().equals("To City")) {

                    AppController.showToast(getActivity(), "please select To City");
                    return;

                }



                if (AppController.isInternetPresent(getActivity())) {
                    String  SearchText="";
                    String  Location="";

                    // spinner.getSelectedItemPosition();

                    if(text_brokerservice.getText()==null){
                        SearchText="";
                    }else {
                        SearchText=text_brokerservice.getText().toString();
                    }


                    Bundle bundle=new Bundle();
                    bundle.putString("Location","");
                    bundle.putString("SearchText",SearchText);
                    bundle.putString("FromCity",txtfrom_city.getText().toString());

                    bundle.putString("ToCity",txtto_city.getText().toString());
                    bundle.putString("FromCityId",stringcityIdfrom);

                    bundle.putString("ToCityId",stringcityIdto);

                    bundle.putString("ToCityId",stringcityIdto);

                    bundle.putString("Service_Id",  stringServiceId);
                    bundle.putInt("spinnerId",0);

                    bundle.putString("ToSateName",txtto_state.getText().toString());


                    RelativeLayout topbg=(RelativeLayout) getActivity().findViewById(R.id.topbg) ;

                    topbg.setVisibility(View.GONE);



                    //FromCity ParcelSearch_FromCity,ParcelSearch_ToCity,ParcelSearch_ToSateName,ParcelSearch_FromSateName
                    prefs.edit().putString("ParcelSearch_FromCity", txtfrom_city.getText().toString()).commit();
                    prefs.edit().putString("ParcelSearch_FromCityId", stringcityIdfrom).commit();


                    //ToCity
                    prefs.edit().putString("ParcelSearch_ToCity", txtto_city.getText().toString()).commit();
                    prefs.edit().putString("ParcelSearch_ToCityId", stringcityIdto).commit();

                    //ToSate
                    prefs.edit().putString("ParcelSearch_ToSateName", txtto_state.getText().toString()).commit();
                    prefs.edit().putString("ParcelSearch_ToSateId", stringstateIdto).commit();

                    //FromSate
                    prefs.edit().putString("ParcelSearch_FromSateName", txtfrom_state.getText().toString()).commit();
                    prefs.edit().putString("ParcelSearch_FromSateId", stringstateIdfrom).commit();

                    ParcelListFragment searchListFragment=new ParcelListFragment();
                    searchListFragment.setArguments(bundle);


                    replaceFragmentTask(searchListFragment);


                } else {

                    AppController.showToast(getActivity(), "please check internet connection");
                }



             /*  BrokarMainPaggerFragment searchListFragment=new BrokarMainPaggerFragment();
                  searchListFragment.setArguments(bundle);


                replaceFragmentTask(searchListFragment);*/
            }
        });
/*        actv.addTextChangedListener(new TextWatcher() {

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

                            DataManager.getInstance().setCity(city);
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
                        stringstateIdfrom = DataManager.getInstance()
                                .getStateWrapper().get(item)
                                .getId();


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
                        stringcityIdfrom = DataManager.getInstance()
                                .getCitiesOfStateWrapper().get(item)
                                .getId();
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
                        stringstateIdto = DataManager.getInstance()
                                .getStateWrapper().get(item)
                                .getId();


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

        builder.setItems(fromlistItem_City,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        txtto_city.setText(fromlistItem_City[item]);
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

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, Const.URL_CitiesOfState+stringstateIdfrom,
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
