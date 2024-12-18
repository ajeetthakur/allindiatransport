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
public class AddBranchFragment extends BaseContainerFragment {
    SharedPreferences prefs = null;
    ConnectionDetector cd;
    TextView save;
    String sBrenchid="";
    String stringstateId="1",stringcityId="1",stringareaId="";
    CharSequence[]  fromlistItem_State, fromlistItem_City,fromlistItem_Area;
   EditText edit_website,edit_pincode,edit_branch,edit_contactperson,edit_emailid,txtDesc,text_address,txtmobile,txtphone;

ArrayList<AreaWrapper> areaWrappers=new ArrayList<AreaWrapper>();
    ArrayList<String> mItems,mItemsphone;
    ListView mCompleteListView,mCompleteListViewphone;
    String mobileStr="",phoneStr="";
    private CompleteListAdapter mListAdapter,mListAdapter2;
    private CompleteListAdapterphone mListAdapterphone;
  TextView txtstate,txtcity;
    View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.addbranch, container, false);
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
        txtmobile = (EditText) view.findViewById(R.id.txtmobile);

        txtphone = (EditText) view.findViewById(R.id.txtphone);
        txtmobile.setVisibility(View.GONE);
                txtphone.setVisibility(View.GONE);
        mItemsphone = new ArrayList<String>();

        mItemsphone.add("");

       /* String[] itemphone = prefs.getString("Phone_No", "").split(",");
        txtphone.setText(itemphone[itemphone.length-1]);
        //  String s=prefs.getString("Phone_No", "");


        for (int i =0; i <itemphone.length-1 ; i++) {

            if(i==1){
                String s= itemphone[1].substring(0,5);
                // txtphone2.setText(s+"...");
            }

            mItemsphone.add(itemphone[i]);

        }

        if(!(mItemsphone.size()>0)){
            mItemsphone.add("");
        }
*/



        mItems = new ArrayList<String>();
        mItems.add("");
       /* String[] item = prefs.getString("Mobile_No", "").split(",");
        txtmobile.setText(item[item.length-1]);
        // String s=prefs.getString("Mobile_No", "");


        for (int i =0; i <item.length-1 ; i++) {

            if(i==1){

                String s= item[1].substring(0,5);
                // txtmobile2.setText(s+"...");
            }

            mItems.add(item[i]);

        }

        if(!(mItems.size()>0)){
            mItems.add("");
        }
*/

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
      //  edit_contactdetails=(EditText) view.findViewById(R.id.edit_contactdetails);
      //  edit_phoneno=(EditText) view.findViewById(R.id.edit_phoneno);
        edit_emailid=(EditText) view.findViewById(R.id.edit_emailid);
        text_address=(EditText) view.findViewById(R.id.text_address);
        txtDesc=(EditText) view.findViewById(R.id.txtDesc);

        txtstate=(TextView) view.findViewById(R.id.txtstate);
        txtcity=(TextView) view.findViewById(R.id.txtcity);

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

    // getActivity().getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#FFFFFF\">" +"Post Googs" + "</font>")));
        ((com.tpnagar.designdemo.MainActivity) getActivity())
                .getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#FFFFFF\">" +"Add Branch" + "</font>")));

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
/*text_address.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

        String s=txtcity.getText().toString();
        if ( !s.equalsIgnoreCase("City")) {
            showListArea();
        }else {
            AppController.popErrorMsg("Alert!","Please Select City before",getActivity());
        }

    }
});*/

        stasteApi();

        save = (TextView) view.findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (edit_branch.getEditableText().toString() == null || edit_branch.getEditableText().toString().equals("")) {

                    AppController.showToast(getActivity(), "please enter Branch Name");
                    return;

                }

                if (edit_contactperson.getEditableText().toString() == null || edit_contactperson.getEditableText().toString().equals("")) {

                    AppController.showToast(getActivity(), "please enter Contact Person");
                    return;

                }
       /*         if (txtmobile.getEditableText().toString() == null || txtmobile.getEditableText().toString().equals("")) {

                    AppController.showToast(getActivity(), "please enter Mobile number");
                    return;

                }
                if (txtphone.getEditableText().toString() == null || txtphone.getEditableText().toString().equals("")) {

                    AppController.showToast(getActivity(), "please enter phone No");
                    return;

                }*/
                if (text_address.getEditableText().toString() == null || text_address.getEditableText().toString().equals("")) {

                    AppController.showToast(getActivity(), "please enter Address");
                    return;

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

               // mobileStr=txtmobile.getText().toString();

                if(mItems.size()>0) {

                    for (int i = 0; i < mItems.size(); i++) {
                        mobileStr =mobileStr+ "," + mItems.get(i);
                    }
                }
              //  phoneStr=txtphone.getText().toString();
                if(mItemsphone.size()>0) {

                    for (int i = 0; i < mItemsphone.size(); i++) {
                        phoneStr =phoneStr+ "," + mItemsphone.get(i);
                    }
                }


                if (AppController.isInternetPresent(getActivity())) {
                    addBranchApi();

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
    void addBranchApi(){

        JSONObject obj = new JSONObject();

        try {
     /*      {
  "Company_Id": 205,
  "": "Raju Roadlines Delhi",
  "": "Raju Roadlines Delhi branch",
  "": "Bijay Mandal",
  "": "Bijay Mabdal",
  "": "Jagatpuri Delhi",
  "": null,
  "": "98999579331,98999579332,98999579333",
  "": "22448971,22448972,22448973,22448974",
  "": "bijaym@RR.com",
  "": 1,
  "": 35,
  "": 132,
  "": 98,
  "": "www.rrdelhi.com",
  "": "110051",
  "": 2,
  "UserId": 1,
  "CreateLogin": false
}*/

            obj.put("Company_Id", prefs.getString("Company_Id",""));
            obj.put("Company_Name", edit_branch.getText().toString());
            obj.put("Company_Desc", txtDesc.getText().toString());
            obj.put("Owner_Name", "");
            obj.put("Contact_Person", edit_contactperson.getText().toString());


            obj.put("Mobile_No", mobileStr);
            obj.put("Phone_No", phoneStr);
            obj.put("Email", "");
            obj.put("Country_Id", "1");
            obj.put("State_Id", stringstateId);
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

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, Const.URL_addbranch,
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
            public void onResponse(final JSONObject response) {

                Log.e("response BRanch ",response.toString());

                AppController.spinnerStop();
                try{
                    String StatusValue=response.getString("StatusValue");

                    if(StatusValue.equalsIgnoreCase("Success"))
                    {

                     JSONObject jsonObject=new JSONObject(response.getString("Data"));

                         sBrenchid=jsonObject.getString("Id");

                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle("Alert!").setMessage(response.getString("Desc"))
                                .setPositiveButton("ok", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();


                                    Bundle bundle=new Bundle();
                                        bundle.putString("id",sBrenchid);
                                        AppController.ToComefromEdit="no";
                                        AddServiceForBranchFragment searchListFragment=new AddServiceForBranchFragment();
                                        searchListFragment.setArguments(bundle);


                                        replaceFragmentTask(searchListFragment);                                    }
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
