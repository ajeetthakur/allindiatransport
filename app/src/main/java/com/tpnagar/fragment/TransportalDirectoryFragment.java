package com.tpnagar.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.text.Editable;
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
import android.widget.ListView;
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
import com.tpnagar.wrapper.GetComapnyServiceWrapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class TransportalDirectoryFragment extends BaseContainerFragment implements AdapterView.OnItemSelectedListener{


    ArrayList<String> cityid = new ArrayList<String>();
    String cityidForLoc;
    private AutoCompleteTextView actv;
    String[] services;
    Spinner spinner,spinnerGlobel;
    private boolean rootViewId;
    ImageButton search;
    ArrayList<String> city = new ArrayList<String>();
    ArrayList<String> servicesaStrings = new ArrayList<String>();
String CitySetedId="";
    Dialog dialog;
    CharSequence[]  serviceItem;
    ListView mCompleteListView;
    ArrayList<String> strings;

    SharedPreferences prefs = null;
    ConnectionDetector cd;
    String cityId="1",categoryId="1";
    ImageView transport_contractor,air_corgo,refrigerated_container,truck_sales,sea_cargo,car_carriers,packers_movers,
            craner,insurance_companies,manufacturers_and_suppliers,tipper_dumper,trailer_transport_company,tyres_suppliers,warehouse;


    View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.transportal_direct, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initXmlViews(view);


    }

    private void initXmlViews(View view) {
        cd = new ConnectionDetector(getActivity());




        search=(ImageButton) view.findViewById(R.id.search);
        actv = (AutoCompleteTextView) view.findViewById(R.id.autoCompleteTextView1);
        actv.clearFocus();

        spinnerGlobel = (Spinner) getActivity().findViewById(R.id.spinner);

       String str_globle_Location=spinnerGlobel.getSelectedItem().toString();
        ArrayList<String> stringscity= DataManager.getInstance().getCity();
        ArrayList<String> stringscityid= DataManager.getInstance().getCityIds();

        DataManager.getInstance().setCityIds(cityid);
        for (int i = 0; i <stringscity.size() ; i++) {

            if(str_globle_Location.equalsIgnoreCase(stringscity.get(i)))
            {
                CitySetedId= stringscityid.get(i);

            }
        }
        
        spinner = (Spinner) view.findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);

        actv.setText(getArguments().getString("SearchText"));



        transport_contractor=(ImageView) view.findViewById(R.id.transport_contractor);
        air_corgo=(ImageView) view.findViewById(R.id.air_corgo);
        refrigerated_container=(ImageView) view.findViewById(R.id.refrigerated_container);

        truck_sales=(ImageView) view.findViewById(R.id.truck_sales);
        sea_cargo=(ImageView) view.findViewById(R.id.sea_cargo);

        car_carriers=(ImageView) view.findViewById(R.id.car_carriers);

        packers_movers=(ImageView) view.findViewById(R.id.packers_movers);
        craner=(ImageView) view.findViewById(R.id.craner);
        insurance_companies =(ImageView) view.findViewById(R.id.insurance_companies);
        manufacturers_and_suppliers =(ImageView) view.findViewById(R.id.manufacturers_and_suppliers);
        tipper_dumper =(ImageView) view.findViewById(R.id.tipper_dumper);
        trailer_transport_company =(ImageView) view.findViewById(R.id.trailer_transport_company);
        tyres_suppliers =(ImageView) view.findViewById(R.id.tyres_suppliers);
        warehouse =(ImageView) view.findViewById(R.id.warehouse);

        prefs = getActivity().getSharedPreferences("com.tpnagar", MODE_PRIVATE);





        craner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String  SearchText="Cranes On Rental";
                String  Location="";


                Location=spinner.getSelectedItem().toString();



                Bundle bundle=new Bundle();
                bundle.putString("Location",Location);
                bundle.putString("SearchText",SearchText);
                bundle.putInt("spinnerId",spinner.getSelectedItemPosition());
              //  spinner.setSelection(getArguments().getInt("spinnerId"));

                SearchListFragment searchListFragment=new SearchListFragment();
                searchListFragment.setArguments(bundle);


                replaceFragmentTask(searchListFragment);
            }
        });


        packers_movers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String  SearchText="Packers and Movers";
                String  Location="";


                Location=spinner.getSelectedItem().toString();



                Bundle bundle=new Bundle();
                bundle.putString("Location",Location);
                bundle.putString("SearchText",SearchText);
                bundle.putInt("spinnerId",spinner.getSelectedItemPosition());
             //   spinner.setSelection(getArguments().getInt("spinnerId"));

                SearchListFragment searchListFragment=new SearchListFragment();
                searchListFragment.setArguments(bundle);


                replaceFragmentTask(searchListFragment);
            }
        });

        car_carriers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String  SearchText="Car Carriers";
                String  Location="";


                Location=spinner.getSelectedItem().toString();



                Bundle bundle=new Bundle();
                bundle.putString("Location",Location);
                bundle.putString("SearchText",SearchText);
                bundle.putInt("spinnerId",spinner.getSelectedItemPosition());
            //    spinner.setSelection(getArguments().getInt("spinnerId"));

                SearchListFragment searchListFragment=new SearchListFragment();
                searchListFragment.setArguments(bundle);


                replaceFragmentTask(searchListFragment);
            }
        });


        sea_cargo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String  SearchText="Sea Cargo Services";
                String  Location="";


                Location=spinner.getSelectedItem().toString();



                Bundle bundle=new Bundle();
                bundle.putString("Location",Location);
                bundle.putString("SearchText",SearchText);
                bundle.putInt("spinnerId",spinner.getSelectedItemPosition());
               // spinner.setSelection(getArguments().getInt("spinnerId"));

                SearchListFragment searchListFragment=new SearchListFragment();
                searchListFragment.setArguments(bundle);


                replaceFragmentTask(searchListFragment);
            }
        });

        truck_sales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String  SearchText="Truck Sales";
                String  Location="";


                Location=spinner.getSelectedItem().toString();



                Bundle bundle=new Bundle();
                bundle.putString("Location",Location);
                bundle.putString("SearchText",SearchText);
                bundle.putInt("spinnerId",spinner.getSelectedItemPosition());
              //  spinner.setSelection(getArguments().getInt("spinnerId"));

                SearchListFragment searchListFragment=new SearchListFragment();
                searchListFragment.setArguments(bundle);


                replaceFragmentTask(searchListFragment);
            }
        });

        refrigerated_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String  SearchText="Refrigerated Containers";
                String  Location="";


                Location=spinner.getSelectedItem().toString();



                Bundle bundle=new Bundle();
                bundle.putString("Location",Location);
                bundle.putString("SearchText",SearchText);
                bundle.putInt("spinnerId",spinner.getSelectedItemPosition());
              //  spinner.setSelection(getArguments().getInt("spinnerId"));

                SearchListFragment searchListFragment=new SearchListFragment();
                searchListFragment.setArguments(bundle);


                replaceFragmentTask(searchListFragment);
            }
        });

        air_corgo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String  SearchText="Air Cargo Services";
                String  Location="";


                Location=spinner.getSelectedItem().toString();



                Bundle bundle=new Bundle();
                bundle.putString("Location",Location);
                bundle.putString("SearchText",SearchText);
                bundle.putInt("spinnerId",spinner.getSelectedItemPosition());
               // spinner.setSelection(getArguments().getInt("spinnerId"));

                SearchListFragment searchListFragment=new SearchListFragment();
                searchListFragment.setArguments(bundle);


                replaceFragmentTask(searchListFragment);
            }
        });



        transport_contractor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String  SearchText="Transport Contractors";
                String  Location="";


                Location=spinner.getSelectedItem().toString();



                Bundle bundle=new Bundle();
                bundle.putString("Location",Location);
                bundle.putString("SearchText",SearchText);
                bundle.putInt("spinnerId",spinner.getSelectedItemPosition());


                SearchListFragment searchListFragment=new SearchListFragment();
                searchListFragment.setArguments(bundle);


                replaceFragmentTask(searchListFragment);
            }
        });
        insurance_companies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String  SearchText="Insurance Companies";
                String  Location="";


                Location=spinner.getSelectedItem().toString();



                Bundle bundle=new Bundle();
                bundle.putString("Location",Location);
                bundle.putString("SearchText",SearchText);
                bundle.putInt("spinnerId",spinner.getSelectedItemPosition());


                SearchListFragment searchListFragment=new SearchListFragment();
                searchListFragment.setArguments(bundle);


                replaceFragmentTask(searchListFragment);
            }
        });
        manufacturers_and_suppliers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String  SearchText="Manufacturers And Suppliers";
                String  Location="";


                Location=spinner.getSelectedItem().toString();



                Bundle bundle=new Bundle();
                bundle.putString("Location",Location);
                bundle.putString("SearchText",SearchText);
                bundle.putInt("spinnerId",spinner.getSelectedItemPosition());


                SearchListFragment searchListFragment=new SearchListFragment();
                searchListFragment.setArguments(bundle);


                replaceFragmentTask(searchListFragment);
            }
        });
        tipper_dumper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String  SearchText="Tipper Dumper Service";
                String  Location="";


                Location=spinner.getSelectedItem().toString();



                Bundle bundle=new Bundle();
                bundle.putString("Location",Location);
                bundle.putString("SearchText",SearchText);
                bundle.putInt("spinnerId",spinner.getSelectedItemPosition());


                SearchListFragment searchListFragment=new SearchListFragment();
                searchListFragment.setArguments(bundle);


                replaceFragmentTask(searchListFragment);
            }
        });
        trailer_transport_company.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String  SearchText="Trailer Transport Company";
                String  Location="";


                Location=spinner.getSelectedItem().toString();



                Bundle bundle=new Bundle();
                bundle.putString("Location",Location);
                bundle.putString("SearchText",SearchText);
                bundle.putInt("spinnerId",spinner.getSelectedItemPosition());


                SearchListFragment searchListFragment=new SearchListFragment();
                searchListFragment.setArguments(bundle);


                replaceFragmentTask(searchListFragment);
            }
        });
        tyres_suppliers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String  SearchText="Tyres Suppliers";
                String  Location="";


                Location=spinner.getSelectedItem().toString();



                Bundle bundle=new Bundle();
                bundle.putString("Location",Location);
                bundle.putString("SearchText",SearchText);
                bundle.putInt("spinnerId",spinner.getSelectedItemPosition());


                SearchListFragment searchListFragment=new SearchListFragment();
                searchListFragment.setArguments(bundle);


                replaceFragmentTask(searchListFragment);
            }
        });




        warehouse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String  SearchText="Warehouse";
                String  Location="";


                Location=spinner.getSelectedItem().toString();



                Bundle bundle=new Bundle();
                bundle.putString("Location",Location);
                bundle.putString("SearchText",SearchText);
                bundle.putInt("spinnerId",spinner.getSelectedItemPosition());


                SearchListFragment searchListFragment=new SearchListFragment();
                searchListFragment.setArguments(bundle);


                replaceFragmentTask(searchListFragment);
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String  SearchText="";
                String  Location="";

                if(actv.getText()==null){
                    SearchText="";
                }else {
                    SearchText=actv.getText().toString();
                }
                Location=spinner.getSelectedItem().toString();



                Bundle bundle=new Bundle();
                bundle.putString("Location",Location);
                bundle.putString("SearchText",SearchText);
                bundle.putInt("spinnerId",spinner.getSelectedItemPosition());
              //  spinner.setSelection(getArguments().getInt("spinnerId"));

                SearchListFragment searchListFragment=new SearchListFragment();
                searchListFragment.setArguments(bundle);


                replaceFragmentTask(searchListFragment);
            }
        });

        ImageView dropdown=(ImageView) rootView.findViewById(R.id.dropdown);
        dropdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showListService();
            }
        });


        actv.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(s.length()>2)
                {
                    if(cd.isConnectingToInternet())

                    {
                        cityidForLoc=cityid.get(spinner.getSelectedItemPosition());
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

        cityid = DataManager.getInstance().getCityIds();
        city= DataManager.getInstance().getCity();

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, city);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

        int pos=getArguments().getInt("spinnerId");
        spinner.setSelection(pos);

//

        prefs = getActivity().getSharedPreferences("com.test", MODE_PRIVATE);

        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        JSONObject obja = new JSONObject();

        try {


            obja.put("serviceRequest", "");


            AppController.spinnerStart(getActivity());

            Log.e("this is obj:::", obja.toString());


            serviceRequest(obja);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

    }

    private void replaceFragmentTask(Fragment fragment) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
      /*  transaction.setCustomAnimations(
                R.anim.slide_in_from_right, R.anim.slide_out_to_left,
                R.anim.slide_in_from_left, R.anim.slide_out_to_right);*/
        transaction.replace(R.id.frame_container, fragment, "");
        transaction.addToBackStack("search");
        // transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.commit();
    }


    void ServiceListOnCityAndCategory(String editText) {

        JSONObject obj = new JSONObject();
        try {
            obj.put("SearchText", editText);
            obj.put("Location", cityidForLoc);

            Log.e("this is obj:::", obj.toString());


            ServiceListOnCityAndCategoryRequest(obj, editText);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void ServiceListOnCityAndCategoryRequest(JSONObject params, String editText) {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, Const.URL_SearchData,
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
                Log.e("this is error", error.toString());


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
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject obj = jsonArray.getJSONObject(i);

                            servicesaStrings.add(obj.getString("Data"));

                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (servicesaStrings != null) {
                    services = servicesaStrings.toArray(new String[0]);
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>
                        (getActivity(), android.R.layout.simple_list_item_1, services);
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

            //    spinner.setSelection(getArguments().getInt("spinnerId"));


                AppController.spinnerStop();

            }
        };
        return response;
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

                      //  TextView txtUserName=(TextView) getActivity().findViewById(R.id.txtUserName) ;
                        TextView  txtemail=(TextView) getActivity().findViewById(R.id.txtemail) ;
                        TextView phone1=(TextView) getActivity().findViewById(R.id.Phone) ;

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
    private void showListService() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Select Service");

        builder.setItems(serviceItem,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        actv.setText(serviceItem[item]);
                      /*  stringServiceId = DataManager.getInstance()
                                .getGetComapnyServiceWrapperlist().get(item)
                                .getId();*/




                    }
                });
        builder.create();
        builder.show();

    }
}

