package com.tpnagar.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListView;

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
import com.tpnagar.adapter.BranchDetailsAdapter;
import com.tpnagar.wrapper.BranchWrapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class BranchListForDetailsFragment extends BaseContainerFragment {
    SharedPreferences prefs = null;
    ConnectionDetector cd;
   // EditText txtContact;

    ListView listview;
    String id="";

    View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.searchlist, container, false);
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


        prefs = getActivity().getSharedPreferences("com.tpnagar", MODE_PRIVATE);
        if(prefs.getString("IsActive","").equalsIgnoreCase("yes"))
        {
            ((com.tpnagar.designdemo.MainActivity) getActivity())
                    .getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#FFFFFF\">" +"Details of Branch List" + "</font>")));

        }else {
            ((com.tpnagar.designdemo.MainActivityWithoutLogin) getActivity())
                    .getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#FFFFFF\">" +"Details of Branch List" + "</font>")));

        }


        listview=(ListView) view.findViewById(R.id.listview);

        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        JSONObject obj = new JSONObject();

        try {


            obj.put("LoginId", "");
  id=getArguments().getString("id");

            AppController.spinnerStart(getActivity());

            Log.e("this is obj:::", obj.toString());
            serviceBranchlistRequest(obj);


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }




    private void serviceBranchlistRequest(JSONObject params) {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, Const.URL_branchlist+id,
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
                ArrayList<BranchWrapper> branchWrapperlist = new ArrayList<BranchWrapper>();
                Log.e("ServiceListOnCityAn", response.toString());
                try {
                    String StatusValue = response.getString("StatusValue");

                    if (StatusValue.equalsIgnoreCase("Success"))


                    {
                        JSONArray jsonArray = response.getJSONArray("Data");


                        if(!(jsonArray.length()>0)){

                            AppController.popErrorMsg("ALert!","No Branch Find for this Company",getActivity());

                        }


                        for (int i = 0; i < jsonArray.length(); i++) {
                            BranchWrapper branchWrapper = new BranchWrapper();
                            JSONObject obj = jsonArray.getJSONObject(i);
                            branchWrapper.setCompany_Name(obj.getString("Company_Name"));
                            branchWrapper.setContact_Person(obj.getString("Contact_Person"));
                            branchWrapper.setCompany_Desc(obj.getString("Company_Desc"));
                            branchWrapper.setId(obj.getString("Id"));
                            branchWrapper.setCompany_Desc(obj.getString("Company_Desc"));
                            branchWrapper.setMobile_No(obj.getString("Mobile_No"));
                            branchWrapper.setCity_Id(obj.getString("City_Id"));
                            branchWrapper.setState_Id(obj.getString("State_Id"));

                            branchWrapper.setAddress(obj.getString("Address"));
                            //branchWrapper.setBranchAdmin(obj.getString("BranchAdmin"));
                            branchWrapper.setCompany_Type_Id(obj.getString("Company_Type_Id"));
                            branchWrapper.setEmail(obj.getString("Email"));
                            branchWrapper.setPin_Code(obj.getString("Pin_Code"));
                            branchWrapper.setWebsite(obj.getString("Website"));
                            branchWrapper.setState_Name(obj.getString("State_Name"));
                            branchWrapper.setPhone_No(obj.getString("Phone_No"));
                            branchWrapper.setCity_Name(obj.getString("City_Name"));

                            branchWrapper.setOwner_Name(obj.getString("Owner_Name"));
                            branchWrapper.setAddress(obj.getString("Address"));
                            branchWrapper.setLogin_Email(obj.getString("Login_Email"));
                            branchWrapper.setLogin_Mobile_No(obj.getString("Login_Mobile_No"));
                            branchWrapper.setCanEditBranch(obj.getString("CanEditBranch"));
                            branchWrapper.setMainCompany
                                    (obj.getBoolean("IsMainCompany"));

                            branchWrapperlist.add(branchWrapper);

                        }
                     //FragmentTransaction transaction=getActivity().getSupportFragmentManager().beginTransaction();
                      //  BranchViewOnlyAdapter mListAdapter = new BranchViewOnlyAdapter(getActivity(), branchWrapperlist,transaction);
                      //  listview.setAdapter(mListAdapter);
                        DataManager.getInstance().setBranchWrapperlist(branchWrapperlist);

                        BranchDetailsAdapter mListAdapter = new BranchDetailsAdapter(getActivity(), branchWrapperlist,true);
                        listview.setAdapter(mListAdapter);



                        //DataManager.getInstance().setSearchCompanyBrokerWrapper(branchWrapperlist)

                       //DataManager.getInstance().setCitiesOfStateWrapper(citiesOfStateWrapperlist);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                AppController.spinnerStop();

            }
        };
        return response;
    }

}
