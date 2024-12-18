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
import com.tpnagar.R;
import com.tpnagar.adapter.PostVehicleAdapter;
import com.tpnagar.wrapper.PostVehicleWrapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class PostVehiclesListFragment extends BaseContainerFragment {
    SharedPreferences prefs = null;
    ConnectionDetector cd;
   // EditText txtContact;

    ListView listview;

    View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.postvehiclelist, container, false);
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
        ((com.tpnagar.designdemo.MainActivity) getActivity())
                .getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#FFFFFF\">" +"Post Vehicles List" + "</font>")));

        listview=(ListView) view.findViewById(R.id.listview);

        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        JSONObject obj = new JSONObject();

        try {


            obj.put("LoginId", "");


            AppController.spinnerStart(getActivity());

            Log.e("this is obj:::", obj.toString());
            servicePostvhiclelistRequest(obj);


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    private void servicePostvhiclelistRequest(JSONObject params) {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, Const.URL_postedvehiclelist+prefs.getString("Company_Id",""),
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
                ArrayList<PostVehicleWrapper> postVehicleWrapperlist = new ArrayList<PostVehicleWrapper>();
                Log.e("ServiceListOnCityAn", response.toString());

                try {
                    String StatusValue = response.getString("StatusValue");

                    if (StatusValue.equalsIgnoreCase("Success"))


                    {
                        JSONArray jsonArray = response.getJSONArray("Data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            PostVehicleWrapper postVehicleWrapper = new PostVehicleWrapper();
                            JSONObject obj = jsonArray.getJSONObject(i);
                            postVehicleWrapper.setVehicle_Detail(obj.getString("Service_Name"));
                            postVehicleWrapper.setVehicle_No(obj.getString("Vehicle_No"));

                            postVehicleWrapper.setContact_Person(obj.getString("Contact_Person"));
                            postVehicleWrapper.setTo_City_Name(obj.getString("To_City_Name"));
                            postVehicleWrapper.setTo_State_Name(obj.getString("To_State_Name"));
                            postVehicleWrapper.setFrom_City_Name(obj.getString("From_City_Name"));
                            postVehicleWrapper.setFrom_State_Name(obj.getString("From_State_Name"));
                            postVehicleWrapper.setTo_City(obj.getString("To_City"));
                            postVehicleWrapper.setTo_State(obj.getString("To_State"));
                            postVehicleWrapper.setFrom_State(obj.getString("From_State"));
                            postVehicleWrapper.setFrom_City(obj.getString("From_City"));

                            postVehicleWrapper.setPhone_No(obj.getString("Phone_No"));
                            postVehicleWrapper.setMobile_No(obj.getString("Mobile_No"));
                            postVehicleWrapper.setId(obj.getString("PVD_Id"));

                            postVehicleWrapper.setEmail(obj.getString("Email"));
                            postVehicleWrapper.setCompName(obj.getString("CompName"));

                            postVehicleWrapper.setDescription(obj.getString("Description"));


                            postVehicleWrapperlist.add(postVehicleWrapper);

                        }


                        PostVehicleAdapter mListAdapter = new PostVehicleAdapter(getActivity(), postVehicleWrapperlist);
                        listview.setAdapter(mListAdapter);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                AppController.spinnerStop();
                if(!(postVehicleWrapperlist.size()>0)){
                    AppController.popErrorMsg("Alert!","No Vehicles posted ",getActivity());
                }
            }
        };
        return response;
    }

}
