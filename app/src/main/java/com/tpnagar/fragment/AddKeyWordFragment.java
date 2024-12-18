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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListView;
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
import com.tpnagar.wrapper.GetKeywordWrapper;

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
public class AddKeyWordFragment extends BaseContainerFragment {

    SharedPreferences prefs = null;
    ConnectionDetector cd;
    TextView textservice,textkeyword,keywordtextshow,save,viewlist;
    String stringCompany_Service_Id="",stringService_Keyword_Id="",stringKeyword ="",stringserviceId="";
    CharSequence[]  keywordItem,serviceItem;
    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.add_keyword, container, false);
        return rootView;
    }
  @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();

        getView().setOnKeyListener(new View.OnKeyListener() {
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
        });
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initXmlViews(view);


    }

    private void initXmlViews(View view) {
        cd = new ConnectionDetector(getActivity());

        prefs = getActivity().getSharedPreferences("com.tpnagar", MODE_PRIVATE);
        ((com.tpnagar.designdemo.MainActivity) getActivity())
                .getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#FFFFFF\">" +"Add Keywords" + "</font>")));


        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        textservice=(TextView) view.findViewById(R.id.textservice);
        textkeyword=(TextView) view.findViewById(R.id.textkeyword);
        keywordtextshow=(TextView) view.findViewById(R.id.keywordtextshow);

        viewlist=(TextView) view.findViewById(R.id.viewlist);

        viewlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragmentTask(new KeywordsListFragment());
            }
        });

        save=(TextView) view.findViewById(R.id.save);





        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               /* if (textservice.getText().toString() == null || textservice.getText().toString().equalsIgnoreCase("")
                        || textservice.getText().toString().equalsIgnoreCase("Service")) {

                    AppController.showToast(getActivity(), "Please Choose Service");
                    return;

                }*/

                if (AppController.isInternetPresent(getActivity())) {

                    JSONObject obj = new JSONObject();

                    try {

                       // obj.put("Company_Service_Id", stringCompany_Service_Id);
                      //  obj.put("Service_Keyword_Id", stringService_Keyword_Id);
                        obj.put("Keyword", stringCompany_Service_Id);
                        obj.put("UserId", prefs.getString("Login_Id",""));


                        AppController.spinnerStart(getActivity());

                        Log.e("this is obj:::", obj.toString());


                        addCompanyServiceKeywordRequest(obj);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                } else {

                    AppController.showToast(getActivity(), "please check internet connection");
                }
            }
        });

        textservice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showListService();
            }
        });

        JSONObject obj = new JSONObject();

        try {
            obj.put("LoginId", "");
            AppController.spinnerStart(getActivity());
            Log.e("this is obj:::", obj.toString());
            getKeywordRequest(obj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        textkeyword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                String s=textservice.getText().toString();
                KeywordshowList();

//                if ( !s.equalsIgnoreCase("Service")) {
//                    KeywordshowList();
//                }else {
//                    AppController.popErrorMsg("Alert!","Please Select Service before",getActivity());
//                }




            }
        });



      /*  JSONObject obj = new JSONObject();

        try {


            obj.put("serviceRequest", "");


            AppController.spinnerStart(getActivity());

            Log.e("this is obj:::", obj.toString());


            serviceRequest(obj);


        } catch (JSONException e) {
            e.printStackTrace();
        }*/

    }

    private void showListService() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Select Service");

        builder.setItems(serviceItem,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        textservice.setText(serviceItem[item]);
                        stringserviceId = DataManager.getInstance()
                                .getGetComapnyServiceWrapperlist().get(item)
                                .getId();
                        JSONObject obj = new JSONObject();

                        try {
                            obj.put("LoginId", "");
                            AppController.spinnerStart(getActivity());
                            Log.e("this is obj:::", obj.toString());
                            getKeywordRequest(obj);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        System.out.println("item County " + item);


                    }
                });
        builder.create();
        builder.show();

    }



    private void KeywordshowList() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Select Keywords");

        builder.setMultiChoiceItems(keywordItem, null,
                new DialogInterface.OnMultiChoiceClickListener() {
                    public void onClick(DialogInterface dialog, int item, boolean isChecked) {
                        Log.i("Dialogos", "Opción elegida: " + keywordItem[item]);
                    }
                });



        builder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        ListView list = ((AlertDialog) dialog).getListView();
                        // make selected item in the comma seprated string

                        StringBuilder stringBuilderstringService_Keyword_Id = new StringBuilder();
                        StringBuilder stringBuilderId = new StringBuilder();
                        StringBuilder stringBuilder = new StringBuilder();
                        for (int i = 0; i < list.getCount(); i++) {
                            boolean checked = list.isItemChecked(i);

                            if (checked) {
                                if (stringBuilder.length() > 0) stringBuilder.append(", ");
                                stringBuilder.append(list.getItemAtPosition(i));

                                ArrayList<GetKeywordWrapper>  getlist=DataManager.getInstance().getGetKeywordWrappers();


                                for (int j = 0; j < getlist.size(); j++) {

                                    if(getlist.get(j).getKeyword().equalsIgnoreCase(list.getItemAtPosition(i).toString())){

                                       // if (stringBuilderId.length() > 0) stringBuilderId.append(", ");
                                        //stringBuilderId.append(getlist.get(i).getCompany_Service_Id());

                                        if (stringBuilderId.length() > 0) stringBuilderId.append("~");
                                        stringBuilderId.append(getlist.get(i).getCompany_Service_Id()+"~"+getlist.get(i).getService_Keyword_Id()+"~"+getlist.get(i).getKeyword());

                                        if (stringBuilderstringService_Keyword_Id.length() > 0) stringBuilderstringService_Keyword_Id.append(", ");
                                        stringBuilderstringService_Keyword_Id.append(getlist.get(i).getService_Keyword_Id());
                                    }

                                }



                            }
                        }

/*
                       // StringBuilder stringBuilderCompany_Service_Id = new StringBuilder();

                       // StringBuilder stringBuilderstringService_Keyword_Id = new StringBuilder();
                       // StringBuilder stringBuilderstringKeyword= new StringBuilder();


                        StringBuilder stringBuilder = new StringBuilder();
                        for (int i = 0; i < list.getCount(); i++) {
                            boolean checked = list.isItemChecked(i);

                            if (checked) {
                                if (stringBuilder.length() > 0) stringBuilder.append(", ");
                                stringBuilder.append(list.getItemAtPosition(i));

                                ArrayList<GetKeywordWrapper>    getlist=DataManager.getInstance().getGetKeywordWrappers();

                                for (int j = 0; j < getlist.size(); j++) {

                                    if(getlist.get(j).getKeyword().equalsIgnoreCase(list.getItemAtPosition(i).toString())){

                                        if (stringBuilderCompany_Service_Id.length() > 0) stringBuilderCompany_Service_Id.append(", ");
                                        stringBuilderCompany_Service_Id.append(getlist.get(i).getCompany_Service_Id());

                                           if (stringBuilderstringService_Keyword_Id.length() > 0) stringBuilderstringService_Keyword_Id.append(", ");
                                           stringBuilderstringService_Keyword_Id.append(getlist.get(i).getService_Keyword_Id());
                                    }

                                }



                            }
                        }*/

                        /*Check string builder is empty or not. If string builder is not empty.
                          It will display on the screen.
                         */
                        if (stringBuilder.toString().trim().equals("")) {

                            keywordtextshow.setText("Selected Keywords:-");
                            stringBuilder.setLength(0);

                        } else {


                            stringCompany_Service_Id=stringBuilderId.toString();
                            stringService_Keyword_Id=stringBuilderstringService_Keyword_Id.toString();
                            stringKeyword  =stringBuilder.toString();

                            keywordtextshow.setText("Selected Keywords:- "+stringBuilder);
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
                        keywordtextshow.setText("Keyword");
                    }
                });


        builder.create();
        builder.show();

    }



    private void getKeywordRequest(JSONObject params) {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, Const.URL_GetKeyword+prefs.getString("Company_Id","")+"/0",
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
                ArrayList<GetKeywordWrapper> getKeywordWrapperlist = new ArrayList<GetKeywordWrapper>();
                Log.e("GetKeywordWrapper", response.toString());
                ArrayList<String> arrayList1 = new ArrayList<String>();
                try {
                    String StatusValue = response.getString("StatusValue");

                    if (StatusValue.equalsIgnoreCase("Success"))

                    {
                        JSONArray jsonArray = response.getJSONArray("Data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            GetKeywordWrapper getKeywordWrapper = new GetKeywordWrapper();
                            JSONObject obj = jsonArray.getJSONObject(i);
                            getKeywordWrapper.setKeyword(obj.getString("Keyword"));
                            getKeywordWrapper.setCompany_Service_Id(obj.getString("Company_Service_Id"));
                            getKeywordWrapper.setService_Keyword_Id(obj.getString("Service_Keyword_Id"));
                            getKeywordWrapper.setIsActive(""+obj.getBoolean("IsActive"));



                            arrayList1.add(obj.getString("Keyword"));
                            //arrayListBoolean.add(false);

                            getKeywordWrapperlist.add(getKeywordWrapper);

                        }
                        DataManager.getInstance().setGetKeywordWrappers(getKeywordWrapperlist);
                        keywordItem =arrayList1.toArray(new CharSequence[arrayList1.size()]);
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
                          //  arrayListBoolean.add(false);
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


    private void addCompanyServiceKeywordRequest(JSONObject params) {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, Const.URL_AddCompanyServiceKeyword,
                params, newResponseRequesrtAddCompanyServiceKeywordRequest(), eErrorListenerRequesrtAddCompanyServiceKeywordRequest()) {
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

    private Response.ErrorListener eErrorListenerRequesrtAddCompanyServiceKeywordRequest() {
        Response.ErrorListener response_error = new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("this is error", error.toString());


                AppController.spinnerStop();
            }
        };
        return response_error;
    }

    public Response.Listener<JSONObject> newResponseRequesrtAddCompanyServiceKeywordRequest() {
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
                        textkeyword.setText("Keyword");
                        keywordtextshow.setText("Keywords");
                        textservice.setText("Service");


                       // textkeyword.setVisibility(View.GONE);

                        AppController.popErrorMsg("Alert!",response.getString("Desc"),getActivity());
                    }else {
                        AppController.popErrorMsg("Alert!",response.getString("Desc"),getActivity());

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


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
