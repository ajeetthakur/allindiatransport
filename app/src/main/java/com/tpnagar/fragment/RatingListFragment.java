package com.tpnagar.fragment;

import android.content.Context;
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
import com.tpnagar.adapter.RatingAdapter;
import com.tpnagar.wrapper.RatingWrapper;
import com.tpnagar.wrapper.VendorCompanyWrapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class RatingListFragment extends BaseContainerFragment {
    SharedPreferences prefs = null;
    ConnectionDetector cd;
   // EditText txtContact;

    ListView listview;
    FragmentTransaction transaction;
    View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.vendor_list, container, false);
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



        if(prefs.getString("IsActive","").equalsIgnoreCase("yes"))
        {
            ((com.tpnagar.designdemo.MainActivity) getActivity())
                    .getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#FFFFFF\">" +"Company Rating List" + "</font>")));

        }else {
            ((com.tpnagar.designdemo.MainActivityWithoutLogin) getActivity())
                    .getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#FFFFFF\">" +"Company Rating List" + "</font>")));

        }



        listview=(ListView) view.findViewById(R.id.listview);

        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        JSONObject obj = new JSONObject();

        try {


            obj.put("Company_Id",getArguments().getString("Company_Id"));


            AppController.spinnerStart(getActivity());

            Log.e("this is Company_Id", prefs.getString("Company_Id",""));
            vendorlistRequest(obj);


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }




    private void vendorlistRequest(JSONObject params) {


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, Const.URL_GetCompanyRatingDetail+getArguments().getString("Company_Id"),
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
                ArrayList<VendorCompanyWrapper>vendorCompanyWrapperlist = new ArrayList<VendorCompanyWrapper>();
                Log.e("ServiceListOnCityAn", response.toString());
                try {
                    String StatusValue = response.getString("StatusValue");
                    ArrayList<RatingWrapper> list=new ArrayList<RatingWrapper>();
                    if (StatusValue.equalsIgnoreCase("Success"))


                    {
                        JSONArray jsonArray = response.getJSONArray("Data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            RatingWrapper ratingList = new RatingWrapper();
                            JSONObject obj = jsonArray.getJSONObject(i);
                            ratingList.setCustomerName(obj.getString("Customer_Name"));
                            ratingList.setEmail(obj.getString("Email"));
                            ratingList.setPhone(obj.getString("Phone"));
                            ratingList.setReview(obj.getString("Review"));
                            ratingList.setRating(obj.getString("Rating"));
                            ratingList.setDatetext(obj.getString("RatingDT"));

                            list.add(ratingList);


                        }
                        RatingAdapter mListAdapter = new RatingAdapter(getActivity(), list,transaction);
                        listview.setAdapter(mListAdapter);


                    }else {

                        AppController.popErrorMsg("Error",response.getString("Desc"),getActivity());
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
