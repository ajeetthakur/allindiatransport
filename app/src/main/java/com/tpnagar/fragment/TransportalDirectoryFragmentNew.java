package com.tpnagar.fragment;

import android.app.Dialog;
import android.content.Context;
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
import android.widget.AutoCompleteTextView;
import android.widget.GridView;
import android.widget.ImageButton;

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
import com.tpnagar.adapter.GridServicesAdapter;
import com.tpnagar.wrapper.ServiceShowWrapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class TransportalDirectoryFragmentNew extends BaseContainerFragment implements AdapterView.OnItemSelectedListener{

    ArrayList<ServiceShowWrapper> serviceShowWrappers=new ArrayList<ServiceShowWrapper>();

    ArrayList<String> cityid = new ArrayList<String>();
    String cityidForLoc;
    private AutoCompleteTextView actv;
    String[] services;
    //Spinner spinnerGlobel;
    private boolean rootViewId;
    ImageButton search;
   // ArrayList<String> city = new ArrayList<String>();
  //  ArrayList<String> servicesaStrings = new ArrayList<String>();
    String CitySetedId="";
    Dialog dialog;
    CharSequence[]  serviceItem;
    GridView gridView;
    ArrayList<String> strings;

    SharedPreferences prefs = null;
    ConnectionDetector cd;
    String cityId="1",categoryId="1";

    ArrayList<String> stringscity;
    ArrayList<String> stringscityid;
    View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.transport_directory_new, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initXmlViews(view);


    }


    private void initXmlViews(View view) {
        cd = new ConnectionDetector(getActivity());



        gridView=(GridView)  view.findViewById(R.id.gridView);
        stringscity= DataManager.getInstance().getCity();
        stringscityid= DataManager.getInstance().getCityIds();
        // Location

        getArguments().getString("Location");
        AutoCompleteTextView   spinner_autocompleCity = (AutoCompleteTextView) getActivity().findViewById(R.id.spinner_autocompleCity);
        spinner_autocompleCity.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(s.length()>2){

                    for (int i = 0; i <stringscityid.size() ; i++) {

                        if(s.toString().equalsIgnoreCase(stringscity.get(i)))
                        {
                            CitySetedId= stringscityid.get(i);

                            JSONObject obja = new JSONObject();

                            try {


                                obja.put("serviceRequest", "");


                                AppController.spinnerStart(getActivity());

                                Log.e("this is obj:::", obja.toString());


                                RandomCityTOServicesRequest(obja);


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
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


        for (int i = 0; i <stringscity.size()-1 ; i++) {

           if(getArguments().getString("Location").equalsIgnoreCase(stringscity.get(i)))
            {
                CitySetedId= stringscityid.get(i);
                JSONObject obja = new JSONObject();

                try {


                    obja.put("serviceRequest", "");


                    AppController.spinnerStart(getActivity());




                    RandomCityTOServicesRequest(obja);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String  Location="";
                AutoCompleteTextView    spinner_autocompleCity = (AutoCompleteTextView) getActivity().findViewById(R.id.spinner_autocompleCity);


                Location=spinner_autocompleCity.getText().toString();
                Bundle bundle=new Bundle();
               bundle.putString("Location",Location);
                bundle.putString("SearchText",serviceShowWrappers.get(position).getService_Name());
              //  bundle.putInt("spinnerId",spinnerGlobel.getSelectedItemPosition());
                //  spinner.setSelection(getArguments().getInt("spinnerId"));

                SearchListFragment searchListFragment=new SearchListFragment();
                searchListFragment.setArguments(bundle);


                replaceFragmentTask(searchListFragment);
            }
        });

   /*     craner.setOnClickListener(new View.OnClickListener() {
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
*/







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


            RandomCityTOServicesRequest(obja);


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
        transaction.addToBackStack(null);
        // transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.commit();
    }




    private void RandomCityTOServicesRequest(JSONObject params) {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, Const.URL_ServiceListOnCityAndCategoryNew+CitySetedId+"/1",
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
                    serviceShowWrappers=new ArrayList<ServiceShowWrapper>();
                    if(StatusValue.equalsIgnoreCase("Success"))
                    {
                        JSONArray jsonArray = response.getJSONArray("Data");
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject obj = jsonArray.getJSONObject(i);
                            ServiceShowWrapper serviceShowWrapper=new ServiceShowWrapper();
                            serviceShowWrapper.setService_Name(obj.getString("Service_Name"));
                            serviceShowWrapper.setService_Desc(obj.getString("Service_Desc"));

                            serviceShowWrapper.setService_Icon(obj.getString("Service_Icon"));
                            serviceShowWrappers.add(serviceShowWrapper);
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                gridView.setAdapter(new GridServicesAdapter(getActivity(),serviceShowWrappers));

                AppController.spinnerStop();

            }
        };
        return response;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String str_globle_Location = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
      // Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();

       // String str_globle_Location=spinnerGlobel.getSelectedItem().toString();
        ArrayList<String> stringscity= DataManager.getInstance().getCity();
        ArrayList<String> stringscityid= DataManager.getInstance().getCityIds();


        for (int i = 0; i <stringscity.size() ; i++) {

            if(str_globle_Location.equalsIgnoreCase(stringscity.get(i)))
            {
                CitySetedId= stringscityid.get(i);

            }
        }

        JSONObject obja = new JSONObject();

        try {


            obja.put("serviceRequest", "");


            AppController.spinnerStart(getActivity());

            Log.e("this is obj:::", obja.toString());


            RandomCityTOServicesRequest(obja);


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


}

