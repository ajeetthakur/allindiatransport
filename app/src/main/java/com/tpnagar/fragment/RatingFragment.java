package com.tpnagar.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;

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
import com.tpnagar.wrapper.AreaWrapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by admin on 03-03-2016.
 */
public class RatingFragment extends BaseContainerFragment {
    SharedPreferences prefs = null;
    ConnectionDetector cd;
    ImageButton save;
    EditText text_company_name,txtDesc,text_email,text_mobile;

    RatingBar ratingbar;

    ArrayList<AreaWrapper> areaWrappers=new ArrayList<AreaWrapper>();

    View rootView;
    String id;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.rating, container, false);

        prefs = getActivity().getSharedPreferences("com.tpnagar", MODE_PRIVATE);
        if(prefs.getString("IsActive","").equalsIgnoreCase("yes"))
        {
            ((com.tpnagar.designdemo.MainActivity) getActivity())
                    .getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#FFFFFF\">" +"Search Rating" + "</font>")));

        }else {
            ((com.tpnagar.designdemo.MainActivityWithoutLogin) getActivity())
                    .getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#FFFFFF\">" +"Rating" + "</font>")));

        }

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initXmlViews(view);
        id=getArguments().getString("id");


    }
/*
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

                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                        Bundle bundle=new Bundle();
                        bundle.putInt("id",getArguments().getInt("id"));

                        SearchDetailsFragment searchDetailsFragment=new SearchDetailsFragment();
                        searchDetailsFragment.setArguments(bundle);

                        transaction.replace(R.id.frame_container, searchDetailsFragment, "");
                        // transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        transaction.commit();


                        return true;
                    }
                }
                return false;
            }
        });
    }*/

    private void initXmlViews(View view) {
        cd = new ConnectionDetector(getActivity());
        prefs = getActivity().getSharedPreferences("com.tpnagar", MODE_PRIVATE);


         ratingbar = (RatingBar) view.findViewById(R.id.ratingbar);

        text_company_name=(EditText) view.findViewById(R.id.text_company_name);
        txtDesc=(EditText) view.findViewById(R.id.txtDesc);
        text_email=(EditText) view.findViewById(R.id.text_email);
        text_mobile=(EditText) view.findViewById(R.id.text_mobile);

        text_company_name.setText(prefs.getString("Company_Name",""));
        text_email.setText(prefs.getString("Email",""));
        text_mobile.setText(prefs.getString("Mobile_No",""));

        text_company_name.setText(prefs.getString("Company_Name",""));
        text_email.setText(prefs.getString("Email",""));
        String[] strings=prefs.getString("Mobile_No","").split(",");

        text_mobile.setText(strings[0]);

        save = (ImageButton) view.findViewById(R.id.submit);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (text_company_name.getEditableText().toString() == null || text_company_name.getEditableText().toString().equals("")) {

                    AppController.showToast(getActivity(), "please enter Company Name");
                    return;

                }

                if (text_email.getEditableText().toString() == null || text_email.getEditableText().toString().equals("")) {

                    AppController.showToast(getActivity(), "please enter Email");
                    return;

                }
                if (text_mobile.getEditableText().toString() == null || text_mobile.getEditableText().toString().equals("")) {

                    AppController.showToast(getActivity(), "please enter Mobile number");
                    return;

                }


                if (txtDesc.getEditableText().toString() == null || txtDesc.getEditableText().toString().equals("")) {

                    AppController.showToast(getActivity(), "please enter Description");
                    return;

                }
                float rating=ratingbar.getRating();
                if (rating==0.0) {

                    AppController.showToast(getActivity(), "please add rating");
                    return;

                }


                if (AppController.isInternetPresent(getActivity())) {
                    addRatingApi();

                } else {

                    AppController.showToast(getActivity(), "please check internet connection");
                }


            }
        });



    }


    void addRatingApi(){

        JSONObject obj = new JSONObject();

        try {
           /* {
                "Id": 1,
                    "Company_Id": 2,
                    "Customer_Name": "sample string 3",
                    "Email": "sample string 4",
                    "Phone": "sample string 5",
                    "Review": "sample string 6",
                    "Rating_Id": 7,
                    "IsApproved": true,
                    "ReflectOnline": true,
                    "IsActive": true
            }*/

            obj.put("Company_Id", id);
           // obj.put("Id", "0");
            obj.put("Customer_Name", text_company_name.getText().toString());
            obj.put("Email", ""+text_email.getText().toString());
            obj.put("Phone", text_mobile.getText().toString());
            obj.put("Review", txtDesc.getText().toString());
          //  obj.put("IsApproved", true);
          //  obj.put("ReflectOnline", true);
            obj.put("Rating_Id", ""+ratingbar.getRating());

           // obj.put("IsActive", true);

            AppController.spinnerStart(getActivity());

            Log.e("this is obj:::",obj.toString());


            addRatingRequest(obj);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void addRatingRequest(JSONObject params) {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, Const.URL_addrating,
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



                                        text_company_name.setText("");
                                        txtDesc.setText("");
                                        text_email.setText("");
                                        text_mobile.setText("");

                                        txtDesc.setText("");

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




}
