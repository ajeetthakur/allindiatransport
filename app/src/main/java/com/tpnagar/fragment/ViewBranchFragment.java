package com.tpnagar.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.tpnagar.wrapper.AreaWrapper;
import com.tpnagar.wrapper.BranchWrapper;
import com.tpnagar.wrapper.CitiesOfStateWrapper;
import com.tpnagar.wrapper.StateWrapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by admin on 03-03-2016.
 */
public class ViewBranchFragment extends BaseContainerFragment {
    SharedPreferences prefs = null;
    ConnectionDetector cd;
    TextView save;
    String stringstateId="1",stringcityId="1",stringareaId="";
    CharSequence[]  fromlistItem_State, fromlistItem_City,fromlistItem_Area;
   EditText txtmobile,txtphone,text_address,edit_website,edit_pincode,edit_branch,edit_contactperson,edit_emailid,txtDesc;

//ArrayList<AreaWrapper> areaWrappers=new ArrayList<AreaWrapper>();

  TextView txtstate,txtcity;
    TextView edit_keyword,edit_service,edit,show_service;
    View rootView;
    boolean iSedit = false;
    private CompleteListAdapter mListAdapter,mListAdapter2;
    private CompleteListAdapterphone mListAdapterphone;
    BranchWrapper branchWrapper;
    ArrayList<AreaWrapper> areaWrappers=new ArrayList<AreaWrapper>();
    ArrayList<String> mItems,mItemsphone;
    ListView mCompleteListView,mCompleteListViewphone;
    String mobileStr="",phoneStr="";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.addbranch, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        int i=getArguments().getInt("id");

        branchWrapper =DataManager.getInstance().getBranchWrapperlist().get(i);
        stringstateId=DataManager.getInstance().getBranchWrapperlist().get(i).getState_Id();
        stringcityId=DataManager.getInstance().getBranchWrapperlist().get(i).getCity_Id();

        ((com.tpnagar.designdemo.MainActivity) getActivity())
                .getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#FFFFFF\">" +"View Branch" + "</font>")));

        initXmlViews(view);


    }
    private void initXmlViews(View view) {
        cd = new ConnectionDetector(getActivity());
        prefs = getActivity().getSharedPreferences("com.tpnagar", MODE_PRIVATE);
        txtmobile = (EditText) view.findViewById(R.id.txtmobile);

        txtphone = (EditText) view.findViewById(R.id.txtphone);
        mItemsphone = new ArrayList<String>();
        String[] itemphone = branchWrapper.getPhone_No().split(",");
        //txtphone.setText(itemphone[itemphone.length-1]);
        //  String s=prefs.getString("Phone_No", "");
        txtphone.setVisibility(View.GONE);


        for (int i =0; i <itemphone.length ; i++) {

            if(i==1){
                String s= itemphone[1].substring(0,5);
                // txtphone2.setText(s+"...");
            }

            mItemsphone.add(itemphone[i]);

        }

        if(!(mItemsphone.size()>0)){
            mItemsphone.add("");
        }

/*
if(itemphone.length>1) {
    mListAdapterphone = new CompleteListAdapterphone(getActivity(), mItemsphone);
    mCompleteListView.setAdapter(mListAdapterphone);
}
*/


        mItems = new ArrayList<String>();
        String[] item = branchWrapper.getMobile_No().split(",");
//        txtmobile.setText(item[item.length]);
        txtmobile.setVisibility(View.GONE);
        // String s=prefs.getString("Mobile_No", "");


        for (int i =0; i <item.length ; i++) {

            if(i==1){

                String s= item[1].substring(0,5);
                // txtmobile2.setText(s+"...");
            }

            mItems.add(item[i]);

        }

        if(!(mItems.size()>0)){
            mItems.add("");
        }

        mCompleteListView = (ListView) rootView.findViewById(R.id.list1);

        mCompleteListViewphone = (ListView) rootView.findViewById(R.id.listphone);
        // TextView mAddItemToListphone = (TextView) rootView.findViewById(R.id.addphone);
        //  TextView mAddItemToList = (TextView) rootView.findViewById(R.id.add);


        mListAdapter = new CompleteListAdapter(getActivity(), mItems);
        mCompleteListView.setAdapter(mListAdapter);
        setListViewHeightBasedOnChildren(mCompleteListView);


        //mListAdapter2 = new CompleteListAdapter(getActivity(), mItemsphone);
        mListAdapterphone = new CompleteListAdapterphone(getActivity(), mItemsphone);
        mCompleteListViewphone.setAdapter(mListAdapterphone);
        setListViewHeightBasedOnChildrenphone(mCompleteListViewphone);
        edit_website=(EditText) view.findViewById(R.id.edit_website);
        edit_pincode=(EditText) view.findViewById(R.id.edit_pincode);
        edit_branch=(EditText) view.findViewById(R.id.edit_branch);
        edit_contactperson=(EditText) view.findViewById(R.id.edit_contactperson);

        edit_emailid=(EditText) view.findViewById(R.id.edit_emailid);
        text_address=(EditText) view.findViewById(R.id.text_address);
        txtDesc=(EditText) view.findViewById(R.id.txtDesc);

        txtDesc.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                if (txtDesc.hasFocus()) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    switch (event.getAction() & MotionEvent.ACTION_MASK){
                        case MotionEvent.ACTION_SCROLL:
                            v.getParent().requestDisallowInterceptTouchEvent(false);
                            return true;
                    }
                }
                return false;
            }
        });

        txtstate=(TextView) view.findViewById(R.id.txtstate);
        txtcity=(TextView) view.findViewById(R.id.txtcity);
        edit=(TextView) view.findViewById(R.id.edit);
        edit_keyword=(TextView) view.findViewById(R.id.edit_keyword);
        edit_service=(TextView) view.findViewById(R.id.edit_service);
        show_service=(TextView) view.findViewById(R.id.show_service);
        edit.setVisibility(View.GONE);

      //new work
        edit_keyword.setVisibility(View.GONE);
        edit_service.setVisibility(View.GONE);
        show_service.setVisibility(View.GONE);


        show_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // branchWrapper.getId()

                Bundle bundle=new Bundle();
                bundle.putString("id",branchWrapper.getId());

                ServicesListForBranchFragment searchListFragment=new ServicesListForBranchFragment();
                searchListFragment.setArguments(bundle);

                replaceFragmentTask(searchListFragment);
            }
        });

        edit_keyword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // branchWrapper.getId()

                Bundle bundle=new Bundle();
                bundle.putString("id",branchWrapper.getId());
               AppController.ToComefromEdit="yes";

                AddKeyWordForBranchFragment searchListFragment=new AddKeyWordForBranchFragment();
                searchListFragment.setArguments(bundle);

                replaceFragmentTask(searchListFragment);
            }
        });
        edit_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // branchWrapper.getId()
                Bundle bundle=new Bundle();
                bundle.putString("id",branchWrapper.getId());
                AppController.ToComefromEdit="yes";
                AddServiceForBranchFragment searchListFragment=new AddServiceForBranchFragment();
                searchListFragment.setArguments(bundle);
                replaceFragmentTask(searchListFragment);
            }
        });

        edit_website.setText(branchWrapper.getWebsite());
                edit_pincode.setText(branchWrapper.getPin_Code());
        edit_branch.setText(branchWrapper.getCompany_Name());
                edit_contactperson.setText(branchWrapper.getContact_Person());

       // String[] item = branchWrapper.getPhone_No().split(",");
         //       edit_phoneno.setText(item[item.length-1]);

        edit_emailid.setText(branchWrapper.getEmail());
                txtDesc.setText(branchWrapper.getCompany_Desc().replace("null",""));
        txtstate.setText(branchWrapper.getState_Name());
        txtcity.setText(branchWrapper.getCity_Name());
        text_address.setText(branchWrapper.getAddress());
        edit_website.setEnabled(false);
        edit_pincode.setEnabled(false);
        edit_branch.setEnabled(false);
        edit_contactperson.setEnabled(false);

        txtmobile.setEnabled(false);
        txtphone.setEnabled(false);
        edit_emailid.setEnabled(false);
        txtDesc.setEnabled(false);
        txtstate.setEnabled(false);
        txtcity.setEnabled(false);
        text_address.setEnabled(false);


        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (iSedit) {

                    edit_website.setEnabled(false);
                    edit_pincode.setEnabled(false);
                    edit_branch.setEnabled(false);
                    edit_contactperson.setEnabled(false);

                    txtmobile.setEnabled(false);
                    txtphone.setEnabled(false);
                    edit_emailid.setEnabled(false);
                    txtDesc.setEnabled(false);
                    txtstate.setEnabled(false);
                    txtcity.setEnabled(false);
                    text_address.setEnabled(false);
                    AppController.showToast(getActivity(), "Edit Mode Disable");
                    iSedit = false;


                } else {

                    edit_website.setEnabled(true);
                    edit_pincode.setEnabled(true);
                    edit_branch.setEnabled(true);
                    edit_contactperson.setEnabled(true);

                    txtmobile.setEnabled(true);
                    txtphone.setEnabled(true);
                    edit_emailid.setEnabled(true);
                    txtDesc.setEnabled(true);
                    txtstate.setEnabled(true);
                    txtcity.setEnabled(true);
                    text_address.setEnabled(true);
                    AppController.showToast(getActivity(), "Edit Mode Enable");
                    iSedit = true;

                }
            }
        });

    // getActivity().getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#FFFFFF\">" +"Post Googs" + "</font>")));
        ((com.tpnagar.designdemo.MainActivity) getActivity())
                .getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#FFFFFF\">" +"Edit Branch" + "</font>")));

        txtstate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showListState();
            }
        });
        txtcity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s=txtstate.getText().toString();
                if ( !s.equalsIgnoreCase("State")) {
                    showListCity();
                }else {
                    AppController.popErrorMsg("Alert!","Please Select State before",getActivity());
                }

            }
        });
         text_address.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

        String s=txtcity.getText().toString();
        if ( !s.equalsIgnoreCase("City")) {
            showListArea();
        }else {
            AppController.popErrorMsg("Alert!","Please Select City before",getActivity());
        }

    }
});

        stasteApi();

        save = (TextView) view.findViewById(R.id.save);
        save.setVisibility(View.INVISIBLE);
        save.setText("Update");

        save.setText("SAVE");
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (iSedit) {

                if (edit_branch.getEditableText().toString() == null || edit_branch.getEditableText().toString().equals("")) {

                    AppController.showToast(getActivity(), "please enter Branch Name");
                    return;

                }

                if (edit_contactperson.getEditableText().toString() == null || edit_contactperson.getEditableText().toString().equals("")) {

                    AppController.showToast(getActivity(), "please enter Contact Person");
                    return;

                }
           /*     if (txtmobile.getEditableText().toString() == null || txtmobile.getEditableText().toString().equals("")) {

                    AppController.showToast(getActivity(), "please enter Mobile number");
                    return;

                }
                if (txtphone.getEditableText().toString() == null || txtphone.getEditableText().toString().equals("")) {

                    AppController.showToast(getActivity(), "please enter phone No");
                    return;

                }*/
               if (text_address.getEditableText().toString() == null ) {
                   text_address.setText("");


                    //AppController.showToast(getActivity(), "please enter Address");


                }
                if (edit_pincode.getEditableText().toString() == null || edit_pincode.getEditableText().toString().equals("")) {

                    AppController.showToast(getActivity(), "please enter Pincode");
                    return;

                }


                if (txtstate.getText().toString() == null || txtstate.getText().toString().equalsIgnoreCase("")|| txtstate.getText().toString().equals("state")) {

                    AppController.showToast(getActivity(), "please select State");
                    return;

                }
                if (txtcity.getText().toString() == null || txtcity.getText().toString().equalsIgnoreCase("")|| txtcity.getText().toString().equals("City")) {

                    AppController.showToast(getActivity(), "please select City");
                    return;

                }

                if (txtDesc.getEditableText().toString() == null || txtDesc.getEditableText().toString().equals("")) {

                    AppController.showToast(getActivity(), "please enter Description");
                    return;

                }

                    mobileStr=txtmobile.getText().toString();

                    if(mItems.size()>0) {

                        for (int i = 0; i < mItems.size(); i++) {
                            mobileStr =mobileStr+ "," + mItems.get(i);
                        }
                    }else{
                      //  mobileStr="";
                    }
                    phoneStr=txtphone.getText().toString();
                    if(mItemsphone.size()>0) {

                        for (int i = 0; i < mItemsphone.size(); i++) {
                            phoneStr =phoneStr+ "," + mItemsphone.get(i);
                        }
                    }else{

                    }
                if (AppController.isInternetPresent(getActivity())) {
                    addBranchApi();

                } else {

                    AppController.showToast(getActivity(), "please check internet connection");
                }



                } else {
                    AppController.popErrorMsg("Alert!", "please Enable Edit Mode", getActivity());

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
    void addBranchApi(){

        JSONObject obj = new JSONObject();

        try {
           /* {
  "Id": 249,
  "Company_Name": "Raju Roadlines Delhi",
  "Company_Desc": "Raju Roadlines Delhi branch1",
  "Owner_Name": "Bijay Mandal",
  "Contact_Person": "Bijay Mabdal",
  "Address": "Jagatpuri Delhi",
  "Fax_No": null,
  "Mobile_No": "98999579331,98999579332,98999579333",
  "Phone_No": "22448971,22448972,22448973,22448974",
  "Email": "bijaym@RR.com",
  "Country_Id": 1,
  "State_Id": 35,
  "City_Id": 132,
  "Area_Id": 98,
  "Website": "www.rrdelhi.com",
  "Pin_Code": "110051",
  "Company_Type_Id": 2,
  "UserId": 1,
  "CreateLogin": false
}*/

            obj.put("Id", branchWrapper.getId());
           // obj.put("Company_Name", edit_branch.getText().toString());
            obj.put("Company_Desc", txtDesc.getText().toString());
            obj.put("Owner_Name", mobileStr);
            obj.put("Contact_Person", edit_contactperson.getText().toString());
            obj.put("Mobile_No", mobileStr);
            obj.put("Phone_No", phoneStr);
            obj.put("Email","");
            obj.put("Country_Id", "1");
            obj.put("State_Id", stringstateId);
            obj.put("Logo", "");
           obj.put("Branch_Name", edit_branch.getText().toString());
            obj.put("Fax_No", "");


            obj.put("City_Id", stringcityId);
            obj.put("Area_Id", stringareaId);
            obj.put("Address", text_address.getText().toString());
            obj.put("Website", edit_website.getText().toString());
            obj.put("Pin_Code", edit_pincode.getText().toString());
            obj.put("Company_Type_Id", 2);

            obj.put("UserId", prefs.getString("Login_Id",""));
            obj.put("CreateLogin", false);

            AppController.spinnerStart(getActivity());

            Log.e("this is obj:::",obj.toString());


            addbranchRequest(obj);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void addbranchRequest(JSONObject params) {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, Const.URL_updatebranch,
                params, addbranchRequesrtupdate(), eErrorListenerRequesrtaddbranch()) {
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

    private Response.ErrorListener eErrorListenerRequesrtaddbranch() {
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

    private Response.Listener<JSONObject> addbranchRequesrtupdate() {
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

                                        /* edit_mobile.setText("");
                                                 edit_companyname.setText("");
                                        edit_contactperson.setText("");
                                                edit_contactno.setText("");
                                        edit_needvehicle.setText("");
                                                edit_goodsdetails.setText("");
                                        txtDesc.setText("");


                                         txtfrom_state.setText("From State");
                                        txtfrom_city.setText("From City");
                                                txtto_state.setText("To State");
                                        txtto_city.setText("To City");*/
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


    private void showListState() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Select State");

        builder.setItems(fromlistItem_State,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        txtstate.setText(fromlistItem_State[item]);
                        stringstateId = DataManager.getInstance()
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

    private void showListCity() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Select City");

        builder.setItems(fromlistItem_City,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        txtcity.setText(fromlistItem_City[item]);
                        stringcityId = DataManager.getInstance()
                                .getCitiesOfStateWrapper().get(item)
                                .getId();

                        JSONObject obj = new JSONObject();

                        try {
                            obj.put("LoginId", "");
                            AppController.spinnerStart(getActivity());
                            Log.e("this is obj:::", obj.toString());
                            serviceAreaRequest(obj);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        System.out.println("item txtfrom_city " + item);
                    }
                });
        builder.create();
        builder.show();

    }

    private void showListArea() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Select Area");

        builder.setItems(fromlistItem_Area,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        text_address.setText(fromlistItem_Area[item]);
                        stringareaId = areaWrappers.get(item)
                                .getId();
                        System.out.println("item txtfrom_city " + item);
                    }
                });
        builder.create();
        builder.show();

    }




    private void serviceCitiesOfStateRequest(JSONObject params) {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, Const.URL_CitiesOfState+stringstateId,
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


    private void serviceAreaRequest(JSONObject params) {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, Const.URL_AreasOfCity+stringcityId,
                params, newResponseRequesrtCitiesOfArea(), eErrorListenerRequesrtCitiesOfArea()) {
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

    private Response.ErrorListener eErrorListenerRequesrtCitiesOfArea() {
        Response.ErrorListener response_error = new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("this is error", error.toString());


                AppController.spinnerStop();
            }
        };
        return response_error;
    }

    public Response.Listener<JSONObject> newResponseRequesrtCitiesOfArea() {
        Response.Listener<JSONObject> response = new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.e("ServiceListOnCityAn", response.toString());
                ArrayList<String> arrayList1 = new ArrayList<String>();
                try {
                    String StatusValue = response.getString("StatusValue");

                    if (StatusValue.equalsIgnoreCase("Success"))


                    {
                        JSONArray jsonArray = response.getJSONArray("Data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            AreaWrapper areaWrapper = new AreaWrapper();
                            JSONObject obj = jsonArray.getJSONObject(i);
                            areaWrapper.setArea_Name(obj.getString("Area_Name"));
                            areaWrapper.setId(obj.getString("Id"));

                            arrayList1.add(obj.getString("Area_Name"));


                            areaWrappers.add(areaWrapper);

                        }
                        fromlistItem_Area = arrayList1
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


    public class CompleteListAdapter extends BaseAdapter {
        private Activity mContext;
        private List<String> mList;
        private LayoutInflater mLayoutInflater = null;

        public CompleteListAdapter(Activity context, List<String> list) {
            mContext = context;
            mList = list;
            mLayoutInflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Object getItem(int pos) {
            return mList.get(pos);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView( int position, View convertView, ViewGroup parent) {
            View v = convertView;
            final int positionNew=position;
            CompleteListViewHolder viewHolder;
            if (convertView == null) {
                LayoutInflater li = (LayoutInflater) mContext
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = li.inflate(R.layout.list_layout, null);
                viewHolder = new CompleteListViewHolder(v);
                v.setTag(viewHolder);
            } else {
                viewHolder = (CompleteListViewHolder) v.getTag();
            }
            viewHolder.editnumber.setText(mList.get(position));

            if(position==0){
                viewHolder.delete.setImageResource(R.drawable.plus);
            }else {
                viewHolder.delete.setImageResource(R.drawable.manace);
            }
            viewHolder.editnumber.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    mItems.set(positionNew,s.toString());

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            viewHolder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if(positionNew==0){
                        addItemsToList();
                    }else {
                        mItems.remove(positionNew);
                    }

                    mListAdapter.notifyDataSetChanged();
                    setListViewHeightBasedOnChildren(mCompleteListView);
                }
            });
            return v;
        }
    }

    class CompleteListViewHolder {
        public EditText editnumber;
        public ImageView delete;

        public CompleteListViewHolder(View base) {
            editnumber = (EditText) base.findViewById(R.id.editnumber);
            delete = (ImageView) base.findViewById(R.id.delete);
        }
    }

    public class CompleteListAdapterphone extends BaseAdapter {
        private Activity mContext;
        private List<String> mList;
        private LayoutInflater mLayoutInflater = null;

        public CompleteListAdapterphone(Activity context, List<String> list) {
            mContext = context;
            mList = list;
            mLayoutInflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Object getItem(int pos) {
            return mList.get(pos);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView( int position, View convertView, ViewGroup parent) {
            View v = convertView;
            final int positionNew=position;
            CompleteListViewHolder viewHolder;
            if (convertView == null) {
                LayoutInflater li = (LayoutInflater) mContext
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = li.inflate(R.layout.list_layout, null);
                viewHolder = new CompleteListViewHolder(v);
                v.setTag(viewHolder);
            } else {
                viewHolder = (CompleteListViewHolder) v.getTag();
            }
            if(position==0){
                viewHolder.delete.setImageResource(R.drawable.plus);
            }else {
                viewHolder.delete.setImageResource(R.drawable.manace);
            }
            viewHolder.editnumber.setText(mList.get(position));


            viewHolder.editnumber.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    mItemsphone.set(positionNew,s.toString());

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            viewHolder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(positionNew==0){
                        addItemsToListphone();
                    }else {
                        mItemsphone.remove(positionNew);
                    }

                    mListAdapterphone.notifyDataSetChanged();
                    setListViewHeightBasedOnChildrenphone(mCompleteListViewphone);
                }
            });
            return v;
        }
    }

   /* public class CompleteListAdapterphone extends BaseAdapter {
        private Activity mContext;
        private List<String> mList;
        private LayoutInflater mLayoutInflater = null;

        public CompleteListAdapterphone(Activity context, List<String> list) {
            mContext = context;
            mList = list;
            mLayoutInflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Object getItem(int pos) {
            return mList.get(pos);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView( int position, View convertView, ViewGroup parent) {
            View v = convertView;
            final int positionNew=position;
            CompleteListViewHolder viewHolder;
            if (convertView == null) {
                LayoutInflater li = (LayoutInflater) mContext
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = li.inflate(R.layout.list_layout, null);
                viewHolder = new CompleteListViewHolder(v);
                v.setTag(viewHolder);
            } else {
                viewHolder = (CompleteListViewHolder) v.getTag();
            }
            viewHolder.editnumber.setText(mList.get(position));


            viewHolder.editnumber.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    mItemsphone.set(positionNew,s.toString());

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            viewHolder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemsphone.remove(positionNew);
                    mListAdapterphone.notifyDataSetChanged();
                }
            });
            return v;
        }
    }*/

    class CompleteListViewHolderphone {
        public EditText editnumber;
        public ImageView delete;

        public CompleteListViewHolderphone(View base) {
            editnumber = (EditText) base.findViewById(R.id.editnumber);
            delete = (ImageView) base.findViewById(R.id.delete);
        }
    }

    public void setListViewHeightBasedOnChildren(ListView listView) {
        CompleteListAdapter listAdapter = (CompleteListAdapter) listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }


    public void setListViewHeightBasedOnChildrenphone(ListView listView) {
        CompleteListAdapterphone listAdapter = (CompleteListAdapterphone) listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    private void addItemsToListphone() {


        mListAdapterphone = new CompleteListAdapterphone(getActivity(), mItemsphone);
        mCompleteListViewphone.setAdapter(mListAdapterphone);

        //  int randomVal = MIN + (int) (Math.random() * ((MAX - MIN) + 1));
        mItemsphone.add("");
        //  mCompleteListView.smoothScrollToPosition(mListAdapterphone.getCount());
        // mItems.add("prem");
        mListAdapterphone.notifyDataSetChanged();
    }


    private void addItemsToList() {


        mListAdapter = new CompleteListAdapter(getActivity(), mItems);
        mCompleteListView.setAdapter(mListAdapter);
        //  int randomVal = MIN + (int) (Math.random() * ((MAX - MIN) + 1));
        mItems.add("");
        //   mCompleteListView.smoothScrollToPosition(mListAdapter.getCount());
        // mItems.add("prem");
        mListAdapter.notifyDataSetChanged();
    }
}

