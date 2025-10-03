package com.tpnagar;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.tpnagar.adapter.BranchDetailsActivityAdapter;
import com.tpnagar.wrapper.BranchWrapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BranchListForDetailsActivity extends FragmentActivity {
    SharedPreferences prefs = null;
    ConnectionDetector cd;
   // EditText txtContact;

    ListView listview;
    String id="";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.branch_view_newlist);
        try {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                    WindowManager.LayoutParams.FLAG_SECURE);
        } catch (Exception e) {
        }
        initXmlViews();
    }



    private void initXmlViews() {
        cd = new ConnectionDetector(this);
        prefs = getSharedPreferences("com.tpnagar", MODE_PRIVATE);


        listview=(ListView) findViewById(R.id.listview);
       ImageView back=findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        JSONObject obj = new JSONObject();

        try {

            obj.put("LoginId", "");
  id=getIntent().getStringExtra("id");

            AppController.spinnerStart(this);

            Log.e("this is obj:::", obj.toString());
            serviceBranchlistRequest(obj);


        } catch (JSONException e) {
            e.printStackTrace();
        }

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

                            AppController.popErrorMsg("ALert!","No Branch Find for this Company",BranchListForDetailsActivity.this);

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


                        BranchDetailsActivityAdapter mListAdapter = new BranchDetailsActivityAdapter(BranchListForDetailsActivity.this, branchWrapperlist);
                        listview.setAdapter(mListAdapter);



                        //DataManager.getInstance().setSearchCompanyBrokerWrapper(branchWrapperlist)
                        DataManager.getInstance().setBranchWrapperlist(branchWrapperlist);
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
