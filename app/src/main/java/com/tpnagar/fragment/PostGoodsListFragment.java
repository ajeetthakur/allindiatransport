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
import com.tpnagar.adapter.PostGoodsAdapter;
import com.tpnagar.wrapper.PostGoodsWrapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class PostGoodsListFragment extends BaseContainerFragment {
    SharedPreferences prefs = null;
    ConnectionDetector cd;
   // EditText txtContact;

    ListView listview;

    View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.postgoodslist, container, false);
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
                .getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#FFFFFF\">" +"Postgoods List" + "</font>")));


        listview=(ListView) view.findViewById(R.id.listview);

        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        JSONObject obj = new JSONObject();

        try {


            obj.put("LoginId", prefs.getString("Company_Id",""));


            AppController.spinnerStart(getActivity());

            Log.e("this is obj:::", obj.toString());
            servicePostgoodsistRequest(obj);


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }



    private void servicePostgoodsistRequest(JSONObject params) {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, Const.URL_postedgoodslist+prefs.getString("Company_Id",""),
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
                ArrayList<PostGoodsWrapper> postGoodsWrapperlist = new ArrayList<PostGoodsWrapper>();
                Log.e("ServiceListOnCityAn", response.toString());

                try {
                    String StatusValue = response.getString("StatusValue");

                    if (StatusValue.equalsIgnoreCase("Success"))


                    {
                        JSONArray jsonArray = response.getJSONArray("Data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            PostGoodsWrapper postGoodsWrapper = new PostGoodsWrapper();
                            JSONObject obj = jsonArray.getJSONObject(i);
                            postGoodsWrapper.setGoods_Detail(obj.getString("Service_Name"));
                            postGoodsWrapper.setContact_Person(obj.getString("Contact_Person"));
                            postGoodsWrapper.setTo_City_Name(obj.getString("To_City_Name"));
                            postGoodsWrapper.setTo_State_Name(obj.getString("To_State_Name"));
                            postGoodsWrapper.setFrom_City_Name(obj.getString("From_City_Name"));
                            postGoodsWrapper.setFrom_State_Name(obj.getString("From_State_Name"));
                            postGoodsWrapper.setTo_City(obj.getString("To_City"));
                            postGoodsWrapper.setTo_State(obj.getString("To_State"));
                            postGoodsWrapper.setFrom_State(obj.getString("From_State"));
                            postGoodsWrapper.setFrom_City(obj.getString("From_City"));
                            postGoodsWrapper.setDescription(obj.getString("Description"));
                          //  postGoodsWrapper.setGoods_Detail(obj.getString("Goods_Detail"));
                           postGoodsWrapper.setFreight(""+obj.getInt("Freight"));

                            postGoodsWrapper.setCompName(obj.getString("CompName"));
                            postGoodsWrapper.setEmail(obj.getString("Email"));
                            postGoodsWrapper.setId(obj.getString("Id"));
                            postGoodsWrapper.setPhone_No(obj.getString("Phone_No"));
                            postGoodsWrapper.setMobile_Nod(obj.getString("Mobile_No"));

                            postGoodsWrapperlist.add(postGoodsWrapper);

                        }



                        PostGoodsAdapter mListAdapter = new PostGoodsAdapter(getActivity(), postGoodsWrapperlist);
                        listview.setAdapter(mListAdapter);
                      //  DataManager.getInstance().setCitiesOfStateWrapper(citiesOfStateWrapperlist);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                AppController.spinnerStop();

                if(!(postGoodsWrapperlist.size()>0)){

                    AppController.popErrorMsg("Alert!","No Goods posted ",getActivity());

                }


            }
        };
        return response;
    }

}
