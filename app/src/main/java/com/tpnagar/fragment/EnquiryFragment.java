package com.tpnagar.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;

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
public class EnquiryFragment extends BaseContainerFragment {
    SharedPreferences prefs = null;
    ConnectionDetector cd;
    ImageButton save;
    String id;
   EditText text_company_name,txtDesc,text_email,text_mobile;

ArrayList<AreaWrapper> areaWrappers=new ArrayList<AreaWrapper>();

    View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.enquray, container, false);
        prefs = getActivity().getSharedPreferences("com.tpnagar", MODE_PRIVATE);
        if(prefs.getString("IsActive","").equalsIgnoreCase("yes"))
        {
            ((com.tpnagar.designdemo.MainActivity) getActivity())
                    .getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#FFFFFF\">" +"Enquiry" + "</font>")));

        }else {
            ((com.tpnagar.designdemo.MainActivityWithoutLogin) getActivity())
                    .getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#FFFFFF\">" +"Enquiry" + "</font>")));

        }


        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initXmlViews(view);


    }

    private void initXmlViews(View view) {

        id=getArguments().getString("id");
        cd = new ConnectionDetector(getActivity());
        prefs = getActivity().getSharedPreferences("com.tpnagar", MODE_PRIVATE);


        text_company_name=(EditText) view.findViewById(R.id.text_company_name);
                txtDesc=(EditText) view.findViewById(R.id.txtDesc);
                        text_email=(EditText) view.findViewById(R.id.text_email);
                text_mobile=(EditText) view.findViewById(R.id.text_mobile);

        text_company_name.setText(prefs.getString("Company_Name",""));
        text_email.setText(prefs.getString("Email",""));
        String[] strings=prefs.getString("Mobile_No","").split(",");

        text_mobile.setText(strings[0]);

        //text_mobile.setText(prefs.getString("Mobile_No",""));

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


                if (AppController.isInternetPresent(getActivity())) {
                    addEnquiry();

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


    void addEnquiry(){

        JSONObject obj = new JSONObject();

        try {
            obj.put("Company_Id", id);

            obj.put("First_Name", text_company_name.getText().toString());
            obj.put("Last_Name", "");
            obj.put("Contact_No", text_mobile.getText().toString());
            obj.put("Email", text_email.getText().toString());
            obj.put("EnquiryDesc", txtDesc.getText().toString());
            obj.put("AppSource", "2");
            obj.put("Contact_Source_Id", "2");
            obj.put("Heard_About_Us_Id", "2");

            AppController.spinnerStart(getActivity());

            Log.e("this is obj:::",obj.toString());


            addbranchRequest(obj);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void addbranchRequest(JSONObject params) {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, Const.URL_addenquiry,
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
