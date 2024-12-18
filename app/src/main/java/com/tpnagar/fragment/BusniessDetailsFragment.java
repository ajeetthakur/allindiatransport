package com.tpnagar.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import androidx.fragment.app.FragmentTransaction;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
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
import com.tpnagar.wrapper.CompanyTypeWrapper;
import com.tpnagar.wrapper.ServiceShowWrapper;
import com.tpnagar.wrapper.StateWrapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class BusniessDetailsFragment extends BaseContainerFragment {
    ConnectionDetector cd;

    CharSequence[] listItem_State, listItem_City, listItem_Membertype;
    TextView save;
    TextView spinner_state, spinner_city, spinner_membertype;
    View rootView;
    String stringstateId = "1", stringcityId = "1", stringmembertyprId = "1";
    SharedPreferences prefs = null;
    boolean iSedit = false;
    //Dialog dialog;
    ArrayList<String> mItems, mItemsphone;
    ListView mCompleteListView, mCompleteListViewphone;
    String mobileStr = "", phoneStr = "";
    private CompleteListAdapter mListAdapter, mListAdapter2;
    private CompleteListAdapterphone mListAdapterphone;
    EditText txtContact, edit_website, edit_emailis, txtaddress, txtDesc, txtpincode;
    TextView edit;
    TextView txtcompanyname;
    Handler mHandler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_businessdetails, container, false);
        return rootView;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        RelativeLayout topbg = (RelativeLayout) getActivity().findViewById(R.id.topbg);

        topbg.setVisibility(View.GONE);
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {

                        FragmentTransaction fragmentManager = getActivity().getSupportFragmentManager().beginTransaction();

                        fragmentManager.replace(R.id.frame_container, new MainFragment(), "item");
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
        initViews();


    }

    private void initViews() {
        ((com.tpnagar.designdemo.MainActivity) getActivity())
                .getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#FFFFFF\">" + "Company  Info" + "</font>")));


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

    private void initXmlViews(View view) {
        cd = new ConnectionDetector(getActivity());

        prefs = getActivity().getSharedPreferences("com.tpnagar", MODE_PRIVATE);
        txtContact = (EditText) view.findViewById(R.id.txtContact);
        spinner_state = (TextView) view.findViewById(R.id.spinner_state);
        spinner_city = (TextView) view.findViewById(R.id.spinner_city);
        spinner_membertype = (TextView) view.findViewById(R.id.spinner_membertype);

        edit_website = (EditText) view.findViewById(R.id.edit_website);
        edit_emailis = (EditText) view.findViewById(R.id.edit_emailis);
        txtaddress = (EditText) view.findViewById(R.id.txtaddress);
        txtDesc = (EditText) view.findViewById(R.id.txtDesc);

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

      /*  final LinearLayout lin_dec = (LinearLayout) view.findViewById(R.id.lin_dec);
        mHandler = new Handler();
        lin_dec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //showKeyboard(txtDesc);

                InputMethodManager inputMethodManager =
                        (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.toggleSoftInputFromWindow(
                        lin_dec.getApplicationWindowToken(),
                        InputMethodManager.SHOW_FORCED, 0);
                inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

            }
        });*/

        //txtmobile2= (TextView) view.findViewById(R.id.txtmobile2);
        //       txtphone2= (TextView) view.findViewById(R.id.txtphone2);

        txtcompanyname = (TextView) view.findViewById(R.id.txtcompanyname);
        //txtfaxnumber = (EditText) view.findViewById(txtfaxnumber);
        txtpincode = (EditText) view.findViewById(R.id.txtpincode);
        // txtmobile = (EditText) view.findViewById(txtmobile);
        //  txtownername = (EditText) view.findViewById(txtownername);
        //  txtphone = (EditText) view.findViewById(R.id.txtphone);
        // mCompleteListView=(ListView) view.findViewById(R.id.completeList);
        // txtfaxnumber.setText(prefs.getString("Fax_No", ""));
        txtpincode.setText(prefs.getString("Pin_Code", ""));


        mItemsphone = new ArrayList<String>();
        String[] itemphone = prefs.getString("Phone_No", "").split(",");
        //  txtphone.setText(itemphone[itemphone.length-1]);
        //  String s=prefs.getString("Phone_No", "");
        //  txtmobile.setVisibility(View.GONE);
        //      txtphone.setVisibility(View.GONE);

        for (int i = 0; i < itemphone.length; i++) {

            if (i == 1) {
                String s = itemphone[1].substring(0, 5);
                // txtphone2.setText(s+"...");
            }

            mItemsphone.add(itemphone[i]);

        }

        if (!(mItemsphone.size() > 0)) {
            mItemsphone.add("");
        }

/*
if(itemphone.length>1) {
    mListAdapterphone = new CompleteListAdapterphone(getActivity(), mItemsphone);
    mCompleteListView.setAdapter(mListAdapterphone);
}
*/


        mItems = new ArrayList<String>();
        String[] item = prefs.getString("Mobile_No", "").split(",");
        //txtmobile.setText(item[item.length-1]);
        // String s=prefs.getString("Mobile_No", "");


        for (int i = 0; i < item.length; i++) {

            if (i == 1) {

                String s = item[1].substring(0, 5);
                // txtmobile2.setText(s+"...");
            }

            mItems.add(item[i]);

        }

        if (!(mItems.size() > 0)) {
            mItems.add("");
        }

/*if(item.length>1) {
    dialog = new Dialog(getActivity(),R.style.FullHeightDialog);
    mCompleteListView = (ListView) dialog.findViewById(R.id.completeList);

    mListAdapter = new CompleteListAdapter(getActivity(), mItems);
    mCompleteListView.setAdapter(mListAdapter);
}*/
        //  txtownername.setText(prefs.getString("Owner_Name", ""));
        //  txtphone.setText(prefs.getString("Phone_No", ""));

        prefs = getActivity().getSharedPreferences("com.tpnagar", MODE_PRIVATE);

        String myString = prefs.getString("Company_Name", "");
        String upperString = "";
        if (myString.length() > 0) {
            upperString = myString.substring(0, 1).toUpperCase() + myString.substring(1);

        } else {
            upperString = "";
        }

        txtcompanyname.setText(upperString);
        txtContact.setText(prefs.getString("Contact_Person", ""));
        edit_website.setText(prefs.getString("Website", ""));
        edit_emailis.setText(prefs.getString("Email", ""));
        txtaddress.setText(prefs.getString("Address", ""));
        txtDesc.setText(prefs.getString("Company_Desc", ""));


        spinner_state.setEnabled(false);
        spinner_city.setEnabled(false);
        spinner_membertype.setEnabled(false);

        txtContact.setEnabled(false);
        edit_website.setEnabled(false);
        edit_emailis.setEnabled(false);
        txtaddress.setEnabled(false);
        txtDesc.setEnabled(false);
        // txtfaxnumber.setEnabled(false);
        txtpincode.setEnabled(false);
        // txtmobile.setEnabled(false);
        // txtownername.setEnabled(false);
        // txtphone.setEnabled(false);
        txtcompanyname.setEnabled(false);

        edit = (TextView) view.findViewById(R.id.edit);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (iSedit) {


                    spinner_state.setEnabled(false);
                    spinner_city.setEnabled(false);
                    spinner_membertype.setEnabled(false);

                    txtContact.setEnabled(false);
                    edit_website.setEnabled(false);
                    edit_emailis.setEnabled(false);
                    txtaddress.setEnabled(false);
                    txtDesc.setEnabled(false);
                    // txtfaxnumber.setEnabled(false);
                    txtpincode.setEnabled(false);
                    //  txtmobile.setEnabled(false);
                    // txtownername.setEnabled(false);
                    // txtphone.setEnabled(false);
                    txtcompanyname.setEnabled(false);
                    AppController.showToast(getActivity(), "Edit Mode Disable");
                    iSedit = false;


                } else {


                    spinner_state.setEnabled(true);
                    spinner_city.setEnabled(true);
                    spinner_membertype.setEnabled(true);
                    txtContact.setEnabled(true);
                    edit_website.setEnabled(true);
                    edit_emailis.setEnabled(true);
                    txtaddress.setEnabled(true);
                    txtDesc.setEnabled(true);
                    //  txtfaxnumber.setEnabled(true);
                    txtpincode.setEnabled(true);
                    // txtmobile.setEnabled(true);
                    // txtownername.setEnabled(true);
                    //  txtphone.setEnabled(true);
                    txtcompanyname.setEnabled(true);
                    AppController.showToast(getActivity(), "Edit Mode Enable");
                    iSedit = true;

                }


            }
        });


        save = (TextView) view.findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (iSedit) {


                    if (txtcompanyname.getEditableText().toString() == null || txtcompanyname.getEditableText().toString().equals("")) {

                        AppController.showToast(getActivity(), "please enter Company Name");
                        return;

                    }

                    if (txtContact.getEditableText().toString() == null || txtContact.getEditableText().toString().equals("")) {

                        AppController.showToast(getActivity(), "please enter Contact Person");
                        return;

                    }
                    if (spinner_state.getText().toString() == null || spinner_state.getText().toString().equalsIgnoreCase("") || spinner_state.getEditableText().toString().equals("state")) {

                        AppController.showToast(getActivity(), "please enter State");
                        return;

                    }
                    if (spinner_city.getText().toString() == null || spinner_city.getText().toString().equalsIgnoreCase("") || spinner_city.getText().toString().equals("City")) {

                        AppController.showToast(getActivity(), "please enter City");
                        return;

                    }
                    if (spinner_membertype.getText().toString().equalsIgnoreCase("Member Type")) {

                        AppController.showToast(getActivity(), "please enter Member Type");
                        return;

                    }
                    if (edit_website.getEditableText().toString() == null) {
                        edit_website.setText("");
                        //   AppController.showToast(getActivity(), "please enter website");
                        //  return;

                    }
                    if (edit_emailis.getEditableText().toString() == null) {
                        edit_emailis.setText("");
                        //  AppController.showToast(getActivity(), "please enter Email");
                        //  return;

                    }
                    if (txtaddress.getEditableText().toString() == null || txtaddress.getEditableText().toString().equals("")) {

                        AppController.showToast(getActivity(), "please enter Address");
                        return;

                    }
                    if (txtDesc.getEditableText().toString() == null || txtDesc.getEditableText().toString().equals("")) {

                        AppController.showToast(getActivity(), "please enter Description");
                        return;

                    }





             /*       if (txtfaxnumber.getEditableText().toString() == null || txtfaxnumber.getEditableText().toString().equals("")) {

                        AppController.showToast(getActivity(), "please enter fax number");
                        return;

                    }*/
                    if (txtpincode.getEditableText().toString() == null || txtpincode.getEditableText().toString().equals("")) {

                        AppController.showToast(getActivity(), "please enter Pincode");
                        return;

                    }
                   /* if (txtmobile.getEditableText().toString() == null || txtmobile.getEditableText().toString().equals("")) {

                        AppController.showToast(getActivity(), "please enter Mobile");
                        return;

                    }*/
                   /* if (txtownername.getEditableText().toString() == null || txtownername.getEditableText().toString().equals("")) {

                        AppController.showToast(getActivity(), "please enter Owner name");
                        return;

                    }*/
                   /* if (txtphone.getEditableText().toString() == null) {
                        txtphone.setText("");
                        //AppController.showToast(getActivity(), "please enter Phone");
                        // return;

                    }*/


                    // mobileStr=txtmobile.getText().toString();

                    if (mItems.size() > 0) {

                        for (int i = 0; i < mItems.size(); i++) {
                            mobileStr = mobileStr + "," + mItems.get(i);
                        }
                    }
                    // phoneStr=txtphone.getText().toString();
                    if (mItemsphone.size() > 0) {

                        for (int i = 0; i < mItemsphone.size(); i++) {
                            phoneStr = phoneStr + "," + mItemsphone.get(i);
                        }
                    }
                    if (mobileStr.equalsIgnoreCase("") || mobileStr.equalsIgnoreCase(",")) {

                        AppController.showToast(getActivity(), "please enter Mobile");
                        return;

                    }

                    if (!(mobileStr.length() > 9)) {

                        AppController.showToast(getActivity(), "please enter Mobile (10 digit)");
                        return;

                    }
                    /*if ( phoneStr.equalsIgnoreCase("")) {

                        AppController.showToast(getActivity(), "please enter Phone");
                        return;

                    }*/

                    if (AppController.isInternetPresent(getActivity())) {
                        updateApi();

                    } else {

                        AppController.showToast(getActivity(), "please check internet connection");
                    }
                } else {
                    AppController.popErrorMsg("Alert!", "please Enable Edit Mode", getActivity());

                }

            }
        });
        spinner_state.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (AppController.isInternetPresent(getActivity())) {
                    showListState();
                } else {
                    AppController.popErrorMsg("Alert!", "please check internet connection", getActivity());

                }


            }
        });
        spinner_city.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String s = spinner_state.getText().toString();
                Log.e("state", "" + s);

                if (!s.equalsIgnoreCase("State")) {

                    if (AppController.isInternetPresent(getActivity())) {
                        showListCity();
                    } else {
                        AppController.popErrorMsg("Alert!", "please check internet connection", getActivity());

                    }

                } else {
                    AppController.popErrorMsg("Alert!", "Please Select State before", getActivity());
                }


            }
        });
        spinner_membertype.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                JSONObject obj = new JSONObject();

                try {


                    obj.put("LoginId", "");


                    AppController.spinnerStart(getActivity());

                    Log.e("this is obj:::", obj.toString());
                    serviceServicrlistRequest(obj);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // showListMemberType();

            }
        });
        prefs = getActivity().getSharedPreferences("com.tpnagar", MODE_PRIVATE);

        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        prefs = getActivity().getSharedPreferences("com.tpnagar", MODE_PRIVATE);

        cd = new ConnectionDetector(getActivity());

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        if (cd.isConnectingToInternet()) {
            stasteApi();
        } else {
            AppController.popErrorMsg("Alert!", "please check internet connection", getActivity());

        }

    }

    void updateApi() {

        JSONObject obj = new JSONObject();

        try {

            obj.put("Id", prefs.getString("Company_Id", ""));
            obj.put("Company_Name", txtcompanyname.getText().toString());
            //obj.put("Company_Desc", txtDesc.getText().toString());

            obj.put("Owner_Name", prefs.getString("Owner_Name", ""));
            obj.put("Contact_Person", txtContact.getText().toString());
            obj.put("Company_Desc", txtDesc.getText().toString());
            obj.put("Address", txtaddress.getText().toString());

            obj.put("UserId", prefs.getString("Login_Id", ""));
            obj.put("Fax_No", prefs.getString("Fax_No", ""));

            obj.put("Country_Id", "1");
            obj.put("State_Id", stringstateId);
            obj.put("City_Id", stringcityId);
            obj.put("Area_Id", prefs.getString("Area_Id", "1"));
            obj.put("Website", edit_website.getText().toString());
            obj.put("Pin_Code", txtpincode.getText().toString());
            obj.put("Company_Category_Id", prefs.getString("Company_Category_Id", "1"));
            obj.put("Company_Type_Id", stringmembertyprId);

            obj.put("Current_Status_Id", prefs.getString("Current_Status_Id", "1"));
            obj.put("Storage_Id", prefs.getString("Storage_Id", "1"));

            obj.put("Mobile_No", mobileStr);
            obj.put("Phone_No", phoneStr);
            obj.put("Email", edit_emailis.getText().toString());


            AppController.spinnerStart(getActivity());

            Log.e("this is obj:::", obj.toString());

            if (cd.isConnectingToInternet()){
                updateCompanyProfileRequest(obj);

            } else {
                AppController.popErrorMsg("Alert!", "please check internet connection", getActivity());

            }



        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    void stasteApi() {

        JSONObject obj = new JSONObject();

        try {


            obj.put("LoginId", "");


            AppController.spinnerStart(getActivity());

            Log.e("this is obj:::", obj.toString());

            serviceStatesOfCountryRequest(obj);
            serviceCitiesOfStateRequest(obj);
            serviceStatesOfCountryRequestCompanyType(obj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void showListState() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Select State");

        builder.setItems(listItem_State,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        spinner_state.setText(listItem_State[item]);
                        stringstateId = DataManager.getInstance()
                                .getStateWrapper().get(item)
                                .getId();

                        spinner_city.setText("");
                        spinner_city.setHint("City");
                        JSONObject obj = new JSONObject();

                        try {
                            obj.put("LoginId", "");
                            AppController.spinnerStart(getActivity());
                            Log.e("this is obj:::", obj.toString());


                            if (cd.isConnectingToInternet()){
                                serviceCitiesOfStateRequest(obj);

                            } else {
                                AppController.popErrorMsg("Alert!", "please check internet connection", getActivity());

                            }

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

        builder.setItems(listItem_City,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        spinner_city.setText(listItem_City[item]);
                        stringcityId = DataManager.getInstance()
                                .getCitiesOfStateWrapper().get(item)
                                .getId();
                        System.out.println("item County " + item);
                    }
                });
        builder.create();
        builder.show();

    }

    private void showListMemberType() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Select Member Type");

        builder.setItems(listItem_Membertype,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        spinner_membertype.setText(listItem_Membertype[item]);
                        stringmembertyprId = DataManager.getInstance()
                                .getCcompanyTypeWrapper().get(item)
                                .getId();
                        System.out.println("item County " + item);
                    }
                });
        builder.create();
        builder.show();

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

                            Log.e("City Id ", "" + prefs.getString("City_Id", ""));
                            Log.e("States Id ", "" + prefs.getString("State_Id", ""));

                            if (prefs.getString("Login_Id", "").length() > 0) {
                                if (prefs.getString("State_Id", "").equalsIgnoreCase(obj.getString("Id").toString())) {
                                    spinner_state.setText(obj.getString("State_Name"));
                                    stringstateId = obj.getString("Id");
                                    serviceCitiesOfStateRequest(obj);
                                }


                            }

                        }

                        DataManager.getInstance().setStateWrapper(stateWrappers);
                        listItem_State = arrayList1
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

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, Const.URL_CitiesOfState + stringstateId,
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

                            if (prefs.getString("Login_Id", "").length() > 0) {
                                if (prefs.getString("City_Id", "").equalsIgnoreCase(obj.getString("Id").toString())) {
                                    spinner_city.setText(obj.getString("City_Name"));
                                    stringcityId = obj.getString("Id");
                                }
                            }
                            citiesOfStateWrapperlist.add(citiesOfStateWrapper);

                        }
                        DataManager.getInstance().setCitiesOfStateWrapper(citiesOfStateWrapperlist);
                        listItem_City = arrayList1
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


    private void serviceStatesOfCountryRequestCompanyType(JSONObject params) {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, Const.URL_CompanyType,
                params, newResponseRequesrtCompanyType(), eErrorListenerRequesrtCompanyType()) {
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

    private Response.ErrorListener eErrorListenerRequesrtCompanyType() {
        Response.ErrorListener response_error = new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("this is error", error.toString());


                AppController.spinnerStop();
            }
        };
        return response_error;
    }

    public Response.Listener<JSONObject> newResponseRequesrtCompanyType() {
        Response.Listener<JSONObject> response = new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                ArrayList<CompanyTypeWrapper> companyTypeWrapperslist = new ArrayList<CompanyTypeWrapper>();
                Log.e("CompanyType resp", response.toString());
                ArrayList<String> arrayList1 = new ArrayList<String>();
                try {
                    String StatusValue = response.getString("StatusValue");

                    if (StatusValue.equalsIgnoreCase("Success"))


                    {
                        JSONArray jsonArray = response.getJSONArray("Data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            CompanyTypeWrapper companyTypeWrapper = new CompanyTypeWrapper();
                            JSONObject obj = jsonArray.getJSONObject(i);
                            companyTypeWrapper.setCompany_Type(obj.getString("Company_Type"));
                            companyTypeWrapper.setId(obj.getString("Id"));
                            //companyTypeWrapper.setIsActive(obj.getString("Active"));
                            arrayList1.add(obj.getString("Company_Type"));
                            companyTypeWrapperslist.add(companyTypeWrapper);

                            Log.e("Company_Type_Id", prefs.getString("Company_Type_Id", ""));
                            Log.e("ID", obj.getString("Id"));
                            if (prefs.getString("Login_Id", "").length() > 0) {
                                if (prefs.getString("Company_Type_Id", "").equalsIgnoreCase(obj.getString("Id").toString())) {
                                    spinner_membertype.setText(obj.getString("Company_Type"));
                                }
                            }

                        }
                        DataManager.getInstance().setCcompanyTypeWrapper(companyTypeWrapperslist);

                        listItem_Membertype = arrayList1
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


    private void updateCompanyProfileRequest(JSONObject params) {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, Const.URL_UpdateCompanyProfile,
                params, newFromCityResponseRequesrtupdate(), eErrorListenerRequesrtupdate()) {
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
                Log.e("this is error", error.toString());


                AppController.spinnerStop();
            }
        };
        return response_error;
    }

    private Response.Listener<JSONObject> newFromCityResponseRequesrtupdate() {
        Response.Listener<JSONObject> response = new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                Log.e("UpdateCompanyProfile", response.toString());
                prefs.edit().putString("ProfileUpdate", "intoprofile").commit();
                AppController.spinnerStop();
                try {
                    String StatusValue = response.getString("StatusValue");

                    if (StatusValue.equalsIgnoreCase("Success")) {

                        iSedit = false;


                        spinner_state.setEnabled(false);
                        spinner_city.setEnabled(false);
                        spinner_membertype.setEnabled(false);

                        txtContact.setEnabled(false);
                        edit_website.setEnabled(false);
                        edit_emailis.setEnabled(false);
                        txtaddress.setEnabled(false);
                        txtDesc.setEnabled(false);
                        //  txtfaxnumber.setEnabled(false);
                        txtpincode.setEnabled(false);
                        // txtmobile.setEnabled(false);
                        //  txtownername.setEnabled(false);
                        // txtphone.setEnabled(false);
                        txtcompanyname.setEnabled(false);
                        //  JSONObject jSONObject = response.getJSONObject("Data");

                        if (prefs.getString("Login_Id", "").length() > 0) {
                            JSONObject obj = new JSONObject();

                            try {


                                obj.put("LoginId", prefs.getString("Login_Id", ""));


                                AppController.spinnerStart(getActivity());

                                Log.e("this is obj:::", obj.toString());

                                if (cd.isConnectingToInternet()){
                                    serviceGetCompanyDetailsRequestCompanyType(obj);

                                } else {
                                    AppController.popErrorMsg("Alert!", "please check internet connection", getActivity());

                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {

                        }


                    } else {
                        AppController.popErrorMsg("Error", response.getString("Desc"), getActivity());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        };
        return response;
    }


    private void serviceGetCompanyDetailsRequestCompanyType(JSONObject params) {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, Const.URL_GetCompanyDetails + prefs.getString("Company_Id", "1"),
                params, newResponseRequesrtGetCompanyDetails(), eErrorListenerRequesrtGetCompanyDetails()) {
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

    private Response.ErrorListener eErrorListenerRequesrtGetCompanyDetails() {
        Response.ErrorListener response_error = new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("this is error", error.toString());


                AppController.spinnerStop();
            }
        };
        return response_error;
    }

    public Response.Listener<JSONObject> newResponseRequesrtGetCompanyDetails() {
        Response.Listener<JSONObject> response = new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                ArrayList<CompanyTypeWrapper> companyTypeWrapperslist = new ArrayList<CompanyTypeWrapper>();
                Log.e("CompanyType", response.toString());
                ArrayList<String> arrayList1 = new ArrayList<String>();
                try {
                    String StatusValue = response.getString("StatusValue");

                    if (StatusValue.equalsIgnoreCase("Success"))


                    {
                        JSONObject jsonbObject = response.getJSONObject("Data");

                        prefs.edit().putString("Id", jsonbObject.getString("Id")).commit();
                        prefs.edit().putString("Company_Name", jsonbObject.getString("Company_Name")).commit();
                        prefs.edit().putString("Company_Desc", jsonbObject.getString("Company_Desc")).commit();
                        prefs.edit().putString("Owner_Name", jsonbObject.getString("Owner_Name")).commit();
                        prefs.edit().putString("Contact_Person", jsonbObject.getString("Contact_Person")).commit();
                        prefs.edit().putString("Address", jsonbObject.getString("Address")).commit();
                        prefs.edit().putString("Fax_No", jsonbObject.getString("Fax_No")).commit();
                        prefs.edit().putString("Country_Id", jsonbObject.getString("Country_Id")).commit();
                        prefs.edit().putString("State_Id", jsonbObject.getString("State_Id")).commit();
                        prefs.edit().putString("City_Id", jsonbObject.getString("City_Id")).commit();
                        prefs.edit().putString("Area_Id", jsonbObject.getString("Area_Id")).commit();
                        prefs.edit().putString("Website", jsonbObject.getString("Website")).commit();
                        prefs.edit().putString("Pin_Code", jsonbObject.getString("Pin_Code")).commit();
                        prefs.edit().putString("Logo", jsonbObject.getString("Logo")).commit();
                        //  prefs.edit().putString("Company_Category_Id",jsonbObject.getString("Company_Category_Id")).commit();
                        prefs.edit().putString("Company_Type_Id", jsonbObject.getString("Company_Type_Id")).commit();
                        prefs.edit().putString("Current_Status_Id", jsonbObject.getString("Current_Status_Id")).commit();
                        prefs.edit().putString("Mobile_No", jsonbObject.getString("Mobile_No")).commit();
                        prefs.edit().putString("Storage_Id", jsonbObject.getString("Storage_Id")).commit();
                        prefs.edit().putString("Phone_No", jsonbObject.getString("Phone_No")).commit();
                        prefs.edit().putString("Email", jsonbObject.getString("Email")).commit();


                        AppController.popErrorMsg("Alert!", "Successfully update", getActivity());


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                AppController.spinnerStop();
                FragmentTransaction fragmentManager = getActivity().getSupportFragmentManager().beginTransaction();


                fragmentManager.replace(R.id.frame_container, new AddServiceFragment(), "item");
                fragmentManager.addToBackStack("item");
                fragmentManager.commit();

            }
        };
        return response;
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
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            final int positionNew = position;
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

            if (position == 0) {
                viewHolder.delete.setImageResource(R.drawable.plus);
            } else {
                viewHolder.delete.setImageResource(R.drawable.manace);
            }
            viewHolder.editnumber.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    mItems.set(positionNew, s.toString());

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            viewHolder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (positionNew == 0) {
                        addItemsToList();
                    } else {
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
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            final int positionNew = position;
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
            if (position == 0) {
                viewHolder.delete.setImageResource(R.drawable.plus);
            } else {
                viewHolder.delete.setImageResource(R.drawable.manace);
            }
            viewHolder.editnumber.setText(mList.get(position));


            viewHolder.editnumber.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    mItemsphone.set(positionNew, s.toString());

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            viewHolder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (positionNew == 0) {
                        addItemsToListphone();
                    } else {
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

    private void serviceServicrlistRequest(JSONObject params) {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, Const.URL_GetCompanyService + prefs.getString("Company_Id", ""),
                params, newResponseRequesrtpostedvehiclelist(), eErrorListenerRequesrtpostedvehiclelist()) {
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

    private Response.ErrorListener eErrorListenerRequesrtpostedvehiclelist() {
        Response.ErrorListener response_error = new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("this is error", error.toString());


                AppController.spinnerStop();
            }
        };
        return response_error;
    }

    public Response.Listener<JSONObject> newResponseRequesrtpostedvehiclelist() {
        Response.Listener<JSONObject> response = new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                ArrayList<ServiceShowWrapper> serviceShowWrapperlist = new ArrayList<ServiceShowWrapper>();
                Log.e("ServiceListOnCityAn", response.toString());
                try {
                    String StatusValue = response.getString("StatusValue");

                    if (StatusValue.equalsIgnoreCase("Success"))


                    {
                        JSONArray jsonArray = response.getJSONArray("Data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            ServiceShowWrapper serviceShowWrapper = new ServiceShowWrapper();
                            JSONObject obj = jsonArray.getJSONObject(i);
                            serviceShowWrapper.setService_Name(obj.getString("Service_Name"));
                            serviceShowWrapper.setId(obj.getString("Id"));
                            serviceShowWrapper.setService_Type(obj.getString("Service_Type"));
                            serviceShowWrapper.setHas_Destination("" + obj.getBoolean("Has_Destination"));
                            // serviceShowWrapper.setId(obj.getString("Id"));


                            serviceShowWrapperlist.add(serviceShowWrapper);

                        }
                        //DataManager.getInstance().setCitiesOfStateWrapper(citiesOfStateWrapperlist);
                        if (serviceShowWrapperlist.size() > 0) {
                            AppController.popErrorMsg("Alert!", "Please Remove all services first then You can update Member type", getActivity());
                        } else {
                            showListMemberType();
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                AppController.spinnerStop();

            }
        };
        return response;
    }

    public void showKeyboard(final EditText ettext) {
        ettext.requestFocus();
        ettext.postDelayed(new Runnable() {
                               @Override
                               public void run() {
                                   InputMethodManager keyboard = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                                   keyboard.showSoftInput(ettext, 0);
                               }
                           }
                , 200);
    }
}
