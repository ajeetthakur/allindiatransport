package com.tpnagar.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.FragmentTransaction;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
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
import com.tpnagar.R;
import com.tpnagar.adapter.VendorBranchAdapter;
import com.tpnagar.wrapper.BranchWrapper;
import com.tpnagar.wrapper.GetallowedbranchWrapper;
import com.tpnagar.wrapper.VendorCompanyWrapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class VendorBranchListFragment extends BaseContainerFragment {
    SharedPreferences prefs = null;
    ConnectionDetector cd;
   // EditText txtContact;
   ArrayList<BranchWrapper> branchWrapperlist = new ArrayList<BranchWrapper>();

    TextView text_branch_name;
    ListView listview;
    RelativeLayout rel_main;
    FragmentTransaction transaction;
    View rootView;
    CharSequence[]  fromlistItem_Branch;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.vendor_branch_list, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        transaction = getActivity().getSupportFragmentManager().beginTransaction();
        initXmlViews(view);


    }

    private void initXmlViews(View view) {
        cd = new ConnectionDetector(getActivity());
        prefs = getActivity().getSharedPreferences("com.tpnagar", MODE_PRIVATE);
        ((com.tpnagar.designdemo.MainActivity) getActivity())
                .getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#FFFFFF\">" +"Vendor Branch List" + "</font>")));


        listview=(ListView) view.findViewById(R.id.listview);
        rel_main=(RelativeLayout) view.findViewById(R.id.rel_main);
        text_branch_name=(TextView) view.findViewById(R.id.text_branch_name);


        rel_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromshowListBranch();
            }
        });

        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        JSONObject obj = new JSONObject();

        try {


            obj.put("LoginId", "");

            obj.put("Branch_Id", prefs.getString("Company_Id",""));
            obj.put("Company_Id", prefs.getString("Company_Id",""));

            AppController.spinnerStart(getActivity());

            Log.e("this is Company_Id", prefs.getString("Company_Id",""));
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

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, Const.URL_getallowedbranch,
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
                branchWrapperlist = new ArrayList<BranchWrapper>();
                ArrayList<String> arrayList1 = new ArrayList<String>();
                Log.e("ServiceListOnCityAn", response.toString());
                try {
                    String StatusValue = response.getString("StatusValue");

                    if (StatusValue.equalsIgnoreCase("Success"))
                    {
                            JSONArray jsonArray = response.getJSONArray("Data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                GetallowedbranchWrapper getallowedbranchWrapper = new GetallowedbranchWrapper();
                                JSONObject obj = jsonArray.getJSONObject(i);
                                getallowedbranchWrapper.setBranchId(obj.getInt("Branch_Id"));
                                getallowedbranchWrapper.setBranchName(obj.getString("Branch_Name"));
                                getallowedbranchWrapper.setId(obj.getInt("Id"));
                                getallowedbranchWrapper.setStatus(obj.getBoolean("Status"));

                                BranchWrapper branchWrapper=new BranchWrapper();
                                branchWrapper.setId(""+obj.getInt("Branch_Id"));
                                branchWrapperlist.add(branchWrapper);
                                arrayList1.add(obj.getString("Branch_Name"));


                            }



                        fromlistItem_Branch = arrayList1
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
    private void fromshowListBranch() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Select Branch");

        builder.setItems(fromlistItem_Branch,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        text_branch_name.setText(fromlistItem_Branch[item]);


                        JSONObject obj = new JSONObject();

                        try {


                            obj.put("Company_Id", branchWrapperlist.get(item).getId());


                            AppController.spinnerStart(getActivity());

                            vendorlistRequest(obj);
dialog.dismiss();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        System.out.println("item County " + item);
                    }
                });
        builder.create();
        builder.show();

    }

    private void vendorlistRequest(JSONObject params) {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, Const.URL_SHOWCOMPANYVENDORS,
                params, newResponseRequest(), eErrorListenerRequest()) {
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

    private Response.ErrorListener eErrorListenerRequest() {
        Response.ErrorListener response_error = new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("this is error", error.toString());


                AppController.spinnerStop();
            }
        };
        return response_error;
    }

    public Response.Listener<JSONObject> newResponseRequest() {
        Response.Listener<JSONObject> response = new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                ArrayList<VendorCompanyWrapper>vendorCompanyWrapperlist = new ArrayList<VendorCompanyWrapper>();
                Log.e("ServiceListOnCityAn", response.toString());
                try {
                    String StatusValue = response.getString("StatusValue");

                    if (StatusValue.equalsIgnoreCase("Success"))


                    {
                        JSONArray jsonArray = response.getJSONArray("Data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            VendorCompanyWrapper vendorCompanyWrapper = new VendorCompanyWrapper();
                            JSONObject obj = jsonArray.getJSONObject(i);
                            vendorCompanyWrapper.setCompanyId(obj.getLong("Company_Id"));
                            vendorCompanyWrapper.setCompanyName(obj.getString("Company_Name"));
                            vendorCompanyWrapper.setContactPerson(obj.getString("Contact_Person"));
                            vendorCompanyWrapper.setHideByCompany(obj.getBoolean("Hide_By_Company"));
                            vendorCompanyWrapper.setHideByVendor(obj.getBoolean("Hide_By_Vendor"));
                            vendorCompanyWrapper.setId(obj.getLong("Id"));
                            vendorCompanyWrapper.setMobileNo(obj.getString("Mobile_No"));
                            vendorCompanyWrapper.setCity_Name(obj.getString("City_Name"));

                            vendorCompanyWrapperlist.add(vendorCompanyWrapper);

                        }
                        VendorBranchAdapter mListAdapter = new VendorBranchAdapter(getActivity(), vendorCompanyWrapperlist,transaction);
                        listview.setAdapter(mListAdapter);


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
