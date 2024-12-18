package com.tpnagar.fragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
import android.widget.ListView;

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
import com.tpnagar.adapter.BrokerSearchOnlyAdapter;
import com.tpnagar.wrapper.SearchCompanyWrapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class BrokerListSearchByNameFragment extends BaseContainerFragment implements AdapterView.OnItemSelectedListener{
    SharedPreferences prefs = null;
    ConnectionDetector cd;


    ArrayList<String> servicesaStrings = new ArrayList<String>();

    ListView listview;
    FragmentTransaction transaction;
    View rootView;


      ProgressDialog dialognew=null;

    String SearchTextOnly;
    JSONArray paramsMainResponse;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.broker_list_only_name, container, false);
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

        transaction = getActivity().getSupportFragmentManager().beginTransaction();


    }

    private void initXmlViews(View view) {
        cd = new ConnectionDetector(getActivity());



        prefs = getActivity().getSharedPreferences("com.tpnagar", MODE_PRIVATE);


        listview=(ListView) view.findViewById(R.id.listview);

        prefs = getActivity().getSharedPreferences("com.tpnagar", MODE_PRIVATE);
        if(prefs.getString("IsActive","").equalsIgnoreCase("yes"))
        {
            ((com.tpnagar.designdemo.MainActivity) getActivity())
                    .getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#FFFFFF\">" +"Broker Search by name List" + "</font>")));

        }else {
            ((com.tpnagar.designdemo.MainActivityWithoutLogin) getActivity())
                    .getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#FFFFFF\">" +"Broker Search by name List" + "</font>")));

        }


        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        SearchTextOnly=getArguments().getString("SearchTextOnly");

        getTokenRequest(null);
        dialognew = ProgressDialog.show(getContext(), "", "Loading...", true);



    }

    public void requestWithSomeHttpHeaders() {

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url="";



        //http://api.tpnagar.com:5004/api/search/brokercompany?fromcity=na&service=har%20krishna&tostate=na&tocity=na

        url = Const.URL_MainSearch+"brokercompany?fromcity=na"+"&service="+SearchTextOnly+"&tostate=na&tocity=na";
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


                                    searchCompanyWrapperlist.add(searchCompanyWrapper);

                                    // }

                                    DataManager.getInstance().setSearchCompanyBrokerWrapper(searchCompanyWrapperlist);
                                    BrokerSearchOnlyAdapter mListAdapter = new  BrokerSearchOnlyAdapter(getActivity(), searchCompanyWrapperlist ,transaction);
                                    listview.setAdapter(mListAdapter);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            BrokerSearchOnlyAdapter mListAdapter = new  BrokerSearchOnlyAdapter(getActivity(), searchCompanyWrapperlist ,transaction);
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

                      requestWithSomeHttpHeaders();

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
