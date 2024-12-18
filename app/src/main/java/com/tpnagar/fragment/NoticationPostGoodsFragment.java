package com.tpnagar.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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
public class NoticationPostGoodsFragment extends BaseContainerFragment {
    private int splashTimeOut = 3000;
    private LinearLayout splashLayout;
    SharedPreferences prefs = null;
    ConnectionDetector cd;
    ImageButton save;
    CharSequence[]  serviceItem;
    String stringstateIdfrom="1",stringcityIdfrom="1",stringstateIdto="1",stringcityIdto="1";
    CharSequence[]  fromlistItem_State, fromlistItem_City,tolistItem_City;/*,tolistItem_State, ;*/
   EditText edit_mobile,edit_companyname,edit_contactperson,edit_contactno,edit_needvehicle,edit_goodsdetails,txtDesc,edit_email,edit_Freight;

  TextView txtfrom_state,txtfrom_city,txtto_state,txtto_city,text_brokerservice;
    //String txtfrom_stateID="1",txtfrom_cityID="1",txtto_stateID="1",txtto_cityID="1";
    View rootView;
    String stringServiceId="";
    LinearLayout lin_city_state;
    ArrayList<Boolean> arrayListBoolean=new ArrayList<Boolean>();

    CheckBox checkBox_notification;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.postgoods_search, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initXmlViews(view);


    }

    private void initXmlViews(View view) {
        cd = new ConnectionDetector(getActivity());
        prefs = getActivity().getSharedPreferences("com.tpnagar", MODE_PRIVATE);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        edit_email=(EditText) view.findViewById(R.id.edit_email);
        checkBox_notification=view.findViewById(R.id.checkbox_notification);
        checkBox_notification.setVisibility(View.VISIBLE);

        edit_companyname=(EditText) view.findViewById(R.id.edit_companyname);
        edit_contactperson=(EditText) view.findViewById(R.id.edit_contactperson);
        edit_contactno=(EditText) view.findViewById(R.id.edit_contactno);
        edit_needvehicle=(EditText) view.findViewById(R.id.edit_needvehicle);
        edit_goodsdetails=(EditText) view.findViewById(R.id.edit_goodsdetails);
        txtDesc=(EditText) view.findViewById(R.id.txtDesc);
        edit_mobile=(EditText) view.findViewById(R.id.edit_mobile);

        edit_Freight=(EditText) view.findViewById(R.id.edit_Freight);
        txtfrom_state=(TextView) view.findViewById(R.id.txtfrom_state);
        txtfrom_city=(TextView) view.findViewById(R.id.txtfrom_city);
        txtto_state=(TextView) view.findViewById(R.id.txtto_state);
        txtto_city=(TextView) view.findViewById(R.id.txtto_city);
        text_brokerservice=(TextView) view.findViewById(R.id.text_brokerservice);
        text_brokerservice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showListService();
            }
        });


        brokerServiceWithoutSuppliersRequest(null);
        String myString=prefs.getString("Company_Name", "");
        String upperString = myString.substring(0,1).toUpperCase() + myString.substring(1);

        edit_companyname.setText(upperString.toUpperCase());
        edit_companyname.setTextColor(Color.RED);
        edit_companyname.setEnabled(false);
        edit_email.setText(prefs.getString("Email",""));

        String[] strings=prefs.getString("Mobile_No","").split(",");

        //edit_contactno.setText(strings[0]);
        edit_contactperson.setText(prefs.getString("Contact_Person",""));

        edit_mobile.setText(strings[0]);

    // getActivity().getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#FFFFFF\">" +"Post Googs" + "</font>")));
        ((com.tpnagar.designdemo.MainActivity) getActivity())
                .getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#FFFFFF\">" +"POST GOODS" + "</font>")));

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

        stasteApi();

        save = (ImageButton) view.findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (edit_companyname.getEditableText().toString() == null || edit_companyname.getEditableText().toString().equals("")) {

                    AppController.showToast(getActivity(), "please enter Company Name");
                    return;

                }

                if (edit_contactperson.getEditableText().toString() == null || edit_contactperson.getEditableText().toString().equals("")) {

                    AppController.showToast(getActivity(), "please enter Contact Person");
                    return;

                }
        /*        if (edit_contactno.getEditableText().toString() == null || edit_contactno.getEditableText().toString().equals("")) {

                    AppController.showToast(getActivity(), "please enter Contact No");
                    return;

                }*/
             /*   if (edit_needvehicle.getEditableText().toString() == null || edit_needvehicle.getEditableText().toString().equals("")) {

                    AppController.showToast(getActivity(), "please enter Need Vehicle");
                    return;

                }*/
             /*   if (edit_goodsdetails.getEditableText().toString() == null || edit_goodsdetails.getEditableText().toString().equals("")) {

                    AppController.showToast(getActivity(), "please enter Good Details");
                    return;

                }*/
                if (edit_mobile.getEditableText().toString() == null || edit_mobile.getEditableText().toString().equals("")) {

                    AppController.showToast(getActivity(), "please enter Mobile No");
                    return;

                }

                if (edit_Freight.getEditableText().toString() == null || edit_Freight.getEditableText().toString().equals("")) {

                   // AppController.showToast(getActivity(), "please enter Freight");
                    //return;
                    edit_Freight.setText("0");
                }


                if (txtfrom_state.getText().toString() == null || txtfrom_state.getText().toString().equalsIgnoreCase("")|| txtfrom_state.getText().toString().equals("From state")) {

                    AppController.showToast(getActivity(), "please enter From State");
                    return;

                }
                if (txtfrom_city.getText().toString() == null || txtfrom_city.getText().toString().equalsIgnoreCase("")|| txtfrom_city.getText().toString().equals("From City")) {

                    AppController.showToast(getActivity(), "please enter From City");
                    return;

                }
                if (txtto_state.getText().toString() == null || txtto_state.getText().toString().equalsIgnoreCase("")|| txtto_state.getText().toString().equals("To state")) {

                    AppController.showToast(getActivity(), "please enter To State");
                    return;

                }
                if (txtto_city.getText().toString() == null || txtto_city.getText().toString().equalsIgnoreCase("")|| txtto_city.getText().toString().equals("To City")) {

                    AppController.showToast(getActivity(), "please enter To City");
                    return;

                }

                if (txtDesc.getEditableText().toString() == null || txtDesc.getEditableText().toString().equals("")) {

                    AppController.showToast(getActivity(), "please enter Description");
                    return;

                }

                if (edit_email.getEditableText().toString() == null ) {

                    edit_email.setText("");


                }





                if (AppController.isInternetPresent(getActivity())) {
                    addpostedgoodsApi();

                } else {

                    AppController.showToast(getActivity(), "please check internet connection");
                }


            }
        });

        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);


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
    void addpostedgoodsApi(){

        JSONObject obj = new JSONObject();


     /*   {
            "Company_Id": 236,
                "Contact_Person": "Ravi Kant",
                "Phone_No": "9898989898",
                "Mobile_No": "9898989898",
                "Freight": 1000,
                "Service_Id": "17",
                "Description": "Car to Delhi",
                "From_State": 1,
                "From_City": 1,
                "To_State": 35,
                "To_City": 947,
                "UserId": 1
        }*/


        try {

            if (checkBox_notification.isChecked()){
                obj.put("Send_Notification_To", "2");
            }else {
                obj.put("Send_Notification_To", "1");
            }


            obj.put("Company_Id", prefs.getString("Company_Id",""));
            obj.put("Contact_Person", edit_contactperson.getText().toString());
            obj.put("Phone_No",edit_contactno.getText().toString() );

            obj.put("Mobile_No", edit_mobile.getText().toString());
            obj.put("Need_Vehicle",true);
            obj.put("Goods_Detail", edit_goodsdetails.getText().toString());
            obj.put("Description", txtDesc.getText().toString());
            obj.put("From_State", stringstateIdfrom);
            obj.put("From_City", stringcityIdfrom);
            obj.put("To_State", stringstateIdto);
            obj.put("To_City",  stringcityIdto);
            obj.put("Email",  edit_email.getText().toString());
            obj.put("Freight",  edit_Freight.getText().toString());

            obj.put("Service_Id",  stringServiceId); //Need to be change

            obj.put("IsActive", true);
            obj.put("UserId", prefs.getString("Login_Id",""));

            AppController.spinnerStart(getActivity());

            Log.e("this is obj:::",obj.toString());


            addpostedgoodsRequest(obj);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void addpostedgoodsRequest(JSONObject params) {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, Const.URL_addpostedgoods,
                params, addpostedgoodsRequesrtupdate(), eErrorListenerRequesrtupdate()) {
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

    private Response.ErrorListener eErrorListenerRequesrtupdate() {
        Response.ErrorListener response_error = new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("this is error",error.toString());

                AppController.popErrorMsg("Error",error.toString(),getActivity());

                AppController.spinnerStop();
            }
        };
        return response_error;
    }

    private Response.Listener<JSONObject> addpostedgoodsRequesrtupdate() {
        Response.Listener<JSONObject> response = new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                Log.e("response ",response.toString());

                AppController.spinnerStop();
                try{
                    String StatusValue=response.getString("StatusValue");

                    if(StatusValue.equalsIgnoreCase("Success"))
                    {

                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle("Alert!").setMessage(response.getString("Desc"))
                                .setPositiveButton("ok", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();

                                         edit_mobile.setText("");
                                                 edit_companyname.setText("");
                                        edit_contactperson.setText("");
                                                edit_contactno.setText("");
                                        edit_needvehicle.setText("");
                                                edit_goodsdetails.setText("");
                                        txtDesc.setText("");


                                         txtfrom_state.setText("From State");
                                        txtfrom_city.setText("From City");
                                                txtto_state.setText("To State");
                                        txtto_city.setText("To City");
                                    }
                                });

                        AlertDialog alert = builder.create();
                        alert.show();





                    }else {
                        AppController.popErrorMsg("Error",response.getString("Desc"),getActivity());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        };
        return response;
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

    private void brokerServiceWithoutSuppliersRequest(JSONObject params) {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, Const.URL_BrokerServiceWithoutSuppliers,
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
