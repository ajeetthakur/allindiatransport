package com.tpnagar.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
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
public class AddServiceBranchFragment extends BaseContainerFragment {

    SharedPreferences prefs = null;
    ConnectionDetector cd;
    TextView txtfrom_city,txtfrom_state,textservices,citytextshow,save,viewlist;
    String stringstateIdfrom="",stringServiceId="";
    CharSequence[]  fromlistItem_State, fromlistItem_City,serviceItem;
    View rootView;
    LinearLayout lin_city_state;
    String stringBuilderIdGlobleCity="";
    ArrayList<Boolean> arrayListBoolean=new ArrayList<Boolean>();
    String id,City_Name,State_Name;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.add_service, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initXmlViews(view);
        RelativeLayout topbg=(RelativeLayout) getActivity().findViewById(R.id.topbg) ;
        id=getArguments().getString("id");



       // bundleserviceView.putString("City_Name",mList.get(position).getCity_Name());

       // bundleserviceView.putString("State_Name",mList.get(position).getState_Name());


        topbg.setVisibility(View.GONE);

    }
   @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();

        /*getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {

                        FragmentTransaction fragmentManager = getActivity().getSupportFragmentManager().beginTransaction();

                        fragmentManager.replace(R.id.frame_container, new MainFragment(),"item");
                        fragmentManager.addToBackStack("item");
                        fragmentManager.commit();
                        return true;
                    }
                }
                return false;
            }
        });*/
    }

    private void initXmlViews(View view) {
        cd = new ConnectionDetector(getActivity());

        prefs = getActivity().getSharedPreferences("com.tpnagar", MODE_PRIVATE);
        ((com.tpnagar.designdemo.MainActivity) getActivity())
                .getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#FFFFFF\">" +"Branch Add Services" + "</font>")));


        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        lin_city_state=(LinearLayout)view.findViewById(R.id.lin_city_state);
        txtfrom_state=(TextView) view.findViewById(R.id.txtto_state);
        txtfrom_city=(TextView) view.findViewById(R.id.txtto_city);
        textservices=(TextView) view.findViewById(R.id.textservices);
        citytextshow=(TextView) view.findViewById(R.id.citytextshow);

        City_Name=getArguments().getString("City_Name");
        State_Name=getArguments().getString("State_Name");

        TextView txtFrom_city=(TextView) view.findViewById(R.id.txtFrom_city);
        TextView  txtFrom_state=(TextView) view.findViewById(R.id.txtFrom_state);

        txtFrom_city.setText(City_Name);
        txtFrom_state.setText(State_Name);

        viewlist=(TextView) view.findViewById(R.id.viewlist);

        viewlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Bundle bundleserviceView=new Bundle();
                bundleserviceView.putString("id",id);
                AppController.ToComefromEdit="yes";
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

                ServicesListForBranchFragment searchListFragment=new ServicesListForBranchFragment();
                searchListFragment.setArguments(bundleserviceView);
                transaction.replace(R.id.frame_container, searchListFragment, "");
                transaction.addToBackStack(null);
                // transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                transaction.commit();


               // replaceFragmentTask(new ServicesListFragment());
            }
        });
        save=(TextView) view.findViewById(R.id.save);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (textservices.getText().toString() == null || textservices.getText().toString().equalsIgnoreCase("")
                        || textservices.getText().toString().equalsIgnoreCase("Service")) {

                    AppController.showToast(getActivity(), "Please choose Service");
                    return;

                }


                if (lin_city_state.getVisibility()==View.VISIBLE){

                    if (txtfrom_state.getText().toString() == null || txtfrom_state.getText().toString().equalsIgnoreCase("")
                            || txtfrom_state.getText().toString().equalsIgnoreCase("state")) {

                        AppController.showToast(getActivity(), "Please choose state");
                        return;

                    }

                    if (stringBuilderIdGlobleCity == null || stringBuilderIdGlobleCity.equalsIgnoreCase("")
                            ) {

                        AppController.showToast(getActivity(), "Please choose cities");
                        return;

                    }



                    }






                if (AppController.isInternetPresent(getActivity())) {

                    JSONObject obj = new JSONObject();

                    try {


                        obj.put("CompanyId", id);
                        obj.put("ServiceId", stringServiceId);
                        obj.put("StateId", stringstateIdfrom);
                        obj.put("Cities", stringBuilderIdGlobleCity);
                        obj.put("UserId", prefs.getString("Login_Id",""));

                        obj.put("Has_Destination", false);

                        obj.put("Id", "0");
                        obj.put("Notes", "");
                        obj.put("Disposition_Id", "0");


                        AppController.spinnerStart(getActivity());

                        Log.e("this is obj:::", obj.toString());


                        addserviceRequest(obj);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                } else {

                    AppController.showToast(getActivity(), "please check internet connection");
                }
            }
        });

        textservices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              showListService();
            }
        });
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
                if ( !s.equalsIgnoreCase("State")) {
                    fromshowListCity();
                }else {
                    AppController.popErrorMsg("Alert!","Please Select State before",getActivity());
                }

            }
        });



        JSONObject obj = new JSONObject();

        try {


            obj.put("serviceRequest", "");


            AppController.spinnerStart(getActivity());

            Log.e("this is obj:::", obj.toString());


            serviceRequest(obj);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        stasteApi();
    }

    private void showListService() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Select Service");

        builder.setItems(serviceItem,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        textservices.setText(serviceItem[item]);
                        stringServiceId = DataManager.getInstance()
                                .getGetComapnyServiceWrapperlist().get(item)
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

if(DataManager.getInstance()
        .getGetComapnyServiceWrapperlist().get(item)
        .getHas_Destination().equalsIgnoreCase("true")){
    citytextshow.setVisibility(View.VISIBLE);
    lin_city_state.setVisibility(View.VISIBLE);

        }else {
    citytextshow.setVisibility(View.GONE);
    lin_city_state.setVisibility(View.GONE);
        }

                    }
                });
        builder.create();
        builder.show();

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

        builder.setMultiChoiceItems(fromlistItem_City, null,
                new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which,
                                        boolean isChecked) {

                        if(isChecked && which == 0)
                        {
                        AlertDialog dialog1 = (AlertDialog) dialog;
                        ListView v = dialog1.getListView();
                        int i = 0;
                        while(i < fromlistItem_City.length) {
                            v.setItemChecked(i, true);
                            i++;
                        }
                    }}
                });

  /*      builder.setNeutralButton("Select All", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                ListView list = ((AlertDialog) dialog).getListView();
                for (int i=0; i < list.getCount(); i++) {
                    list.setItemChecked(i, true);
                }
            }
        });
*/
        builder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        ListView list = ((AlertDialog) dialog).getListView();
                        // make selected item in the comma seprated string
                        StringBuilder stringBuilderId = new StringBuilder();
                        StringBuilder stringBuilder = new StringBuilder();
                        for (int i = 0; i < list.getCount(); i++) {
                            boolean checked = list.isItemChecked(i);

                            if (checked) {
                                if (stringBuilder.length() > 0) stringBuilder.append(", ");
                                stringBuilder.append(list.getItemAtPosition(i));

                                ArrayList<CitiesOfStateWrapper>  getlist=DataManager.getInstance().getCitiesOfStateWrapper();



                            //    ArrayList<GetComapnyServiceWrapper>    getlist=DataManager.getInstance().getGetComapnyServiceWrapperlist();

                                for (int j = 0; j < getlist.size(); j++) {

                                    if(getlist.get(j).getCity_Name().equalsIgnoreCase(list.getItemAtPosition(i).toString())){

                                        if (stringBuilderId.length() > 0) stringBuilderId.append(", ");
                                        stringBuilderId.append(getlist.get(j).getId());
                                    }

                                }



                            }
                        }

                        /*Check string builder is empty or not. If string builder is not empty.
                          It will display on the screen.
                         */
                        if (stringBuilder.toString().trim().equals("")) {

                            citytextshow.setText("Cities");
                            stringBuilder.setLength(0);

                        } else {
                            stringBuilderIdGlobleCity=stringBuilderId.toString();
                            citytextshow.setText("Selected cities:- "+stringBuilder.toString().replace("Select All",""));
                        }
                    }
                });



 /*       boolean[] selected=arrayListBoolean.toArray(new boolean[][arrayListBoolean.size()]);

        builder.setMultiChoiceItems(fromlistItem_City, selected,
                new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {

                    }
                });*/

                builder.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                txtfrom_city.setText("City");
                            }
                        });

        AlertDialog alert = builder.create();
        alert.show();
      //  builder.create();
       // builder.show();

    }
    void stasteApi() {

        JSONObject obj = new JSONObject();

        try {


            obj.put("stasteApi", "");


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
    private void serviceCitiesOfStateRequest(JSONObject params) {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, Const.URL_GetCitiesOfMultiStates+stringstateIdfrom,
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
                arrayList1.add("Select All");
                try {
                    String StatusValue = response.getString("StatusValue");

                    if (StatusValue.equalsIgnoreCase("Success"))

                        arrayListBoolean=new ArrayList<Boolean>();
                    arrayListBoolean.add(false);
                    {
                        JSONArray jsonArray = response.getJSONArray("Data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            CitiesOfStateWrapper citiesOfStateWrapper = new CitiesOfStateWrapper();
                            JSONObject obj = jsonArray.getJSONObject(i);
                            citiesOfStateWrapper.setCity_Name(obj.getString("City_Name"));
                            citiesOfStateWrapper.setId(obj.getString("Id"));

                            arrayList1.add(obj.getString("City_Name"));
                            arrayListBoolean.add(false);

                            citiesOfStateWrapperlist.add(citiesOfStateWrapper);

                        }
                        DataManager.getInstance().setCitiesOfStateWrapper(citiesOfStateWrapperlist);
                        fromlistItem_City =arrayList1.toArray(new CharSequence[arrayList1.size()]);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                AppController.spinnerStop();

            }
        };
        return response;
    }



    private void serviceRequest(JSONObject params) {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, Const.URL_GetCompanyServiceList+prefs.getString("Company_Id","").trim(),
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
                            arrayListBoolean.add(false);
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


    private void addserviceRequest(JSONObject params) {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, Const.URL_addcompanyservice_multi_state_city,
                params, newResponseRequesrtaddserviceRequest(), eErrorListenerRequesrtaddserviceRequest()) {
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

    private Response.ErrorListener eErrorListenerRequesrtaddserviceRequest() {
        Response.ErrorListener response_error = new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("this is error", error.toString());


                AppController.spinnerStop();
            }
        };
        return response_error;
    }

    public Response.Listener<JSONObject> newResponseRequesrtaddserviceRequest() {
        Response.Listener<JSONObject> response = new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                ArrayList<GetComapnyServiceWrapper> getComapnyServiceWrapperlist = new ArrayList<GetComapnyServiceWrapper>();
                Log.e("ServiceListOnCityAn", response.toString());
                ArrayList<String> arrayList1 = new ArrayList<String>();
                try {
                    String StatusValue = response.getString("StatusValue");

                    if (StatusValue.equalsIgnoreCase("Success"))
                    {
                        txtfrom_city.setText("Cities");
                                txtfrom_state.setText("State");
                        textservices.setText("Service");
                                citytextshow.setText("Selected City");
                        lin_city_state.setVisibility(View.GONE);
                        citytextshow.setVisibility(View.GONE);

                        AppController.popErrorMsg("Alert!",response.getString("Desc"),getActivity());
                    }else {
                        AppController.popErrorMsg("Alert!",response.getString("Desc"),getActivity());

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                prefs.edit().putString("ProfileUpdate", "serviceadded").commit();
                AppController.spinnerStop();

            }
        };
        return response;
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
}
