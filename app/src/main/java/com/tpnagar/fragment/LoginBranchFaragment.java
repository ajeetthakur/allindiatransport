package com.tpnagar.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class LoginBranchFaragment extends BaseContainerFragment {
    SharedPreferences prefs = null;
    ConnectionDetector cd;
    EditText txtpassword,txtEmail,txtMobile;
   // TextView text1,text2;
   TextView signin,back;
    View rootView;
    String idbranch;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_login_branch, container, false);


        idbranch=getArguments().getString("id");

        cd = new ConnectionDetector(getActivity());
        // text1=(TextView) rootView.findViewById(R.id.text1);
        // text2=(TextView) rootView.findViewById(R.id.text2);
        prefs = getActivity().getSharedPreferences("com.tpnagar", MODE_PRIVATE);
        txtpassword=(EditText) rootView.findViewById(R.id.txtpassword);
        txtEmail=(EditText) rootView.findViewById(R.id.txtEmail);
        txtMobile=(EditText) rootView.findViewById(R.id.txtMobile);
        back=(TextView) rootView.findViewById(R.id.back);
        signin=(TextView) rootView.findViewById(R.id.signin);

        ((com.tpnagar.designdemo.MainActivity) getActivity())
                .getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#FFFFFF\">" +"Create Login" + "</font>")));



        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragmentTask(new AddKeyWordForBranchFragment());
            }
        });
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Activity context=getActivity();
                /*if (txtEmail.getEditableText().toString() == null || txtEmail.getEditableText().toString().equals("")) {

                    AppController.showToast(context, "please enter Email");
                    return;

                }*/
                if (txtMobile.getEditableText().toString() == null || txtMobile.getEditableText().toString().equals("")) {

                    AppController.showToast(context, "please enter Mobile");
                    return;

                }

                if (txtpassword.getEditableText().toString() == null || txtpassword.getEditableText().toString().equals("")) {

                    AppController.showToast(context, "please enter password");
                    return;

                }


                if (AppController.isInternetPresent(context)) {
                    LoggedInWithFb();

                } else {

                    AppController.showToast(context, "please check internet connection");
                }






            }
        });

        return rootView;
    }




    void LoggedInWithFb(){

        JSONObject obj = new JSONObject();

        try {
/*
            {
                "Company_Id": 242,
                    "Login_Mobile_No": "9953021421",
                    "Login_Email": "info1patrotrech.com",
                    "Password": "bijay$123"
            }*/

            obj.put("Company_Id", idbranch);
            obj.put("Login_Mobile_No", txtMobile.getText().toString());
            obj.put("Login_Email", txtEmail.getText().toString());
            obj.put("Password", txtpassword.getText().toString());
            AppController.spinnerStart(getActivity());

            Log.e("this is obj:::",obj.toString());


            FromCityJsonObjectRequest(obj);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void FromCityJsonObjectRequest(JSONObject params) {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, Const.URL_createbranchlogin,
                params, newFromCityResponseRequesr(), eErrorListenerRequesr()) {
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

    private Response.ErrorListener eErrorListenerRequesr() {
        Response.ErrorListener response_error = new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("this is error",error.toString());


                AppController.spinnerStop();
            }
        };
        return response_error;
    }

    private Response.Listener<JSONObject> newFromCityResponseRequesr() {
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
                        builder.setCancelable(false);
                        builder.setTitle("Alert!");
                        builder.setMessage(response.getString("Desc"))
                                .setCancelable(true)
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                       getActivity().finish();

                                        Intent intent=new Intent(getActivity(),com.tpnagar.designdemo.MainActivity.class);
                                        startActivity(intent);



                                        dialog.dismiss();

                                    }
                                });


                        AlertDialog alert = builder.create();
                        alert.show();



                    }else {



                        if(response.getString("Desc").equalsIgnoreCase("Signup OTP Not Validated")){


                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setTitle("Error").setMessage("Signup OTP Not Validated Please Generate OTP and Validate it")
                                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();



                                            JSONObject obj = new JSONObject();

                                            try {


                                                obj.put("LoginId", prefs.getString("Login_Id",""));

                                                obj.put("SMSType", 1);
                                                AppController.spinnerStart(getActivity());

                                                Log.e("this is obj:::",obj.toString());


                                                generateOTPJsonObjectRequest(obj);


                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }



                                        }
                                    });

                            AlertDialog alert = builder.create();
                            alert.show();



                        }else {
                            AppController.popErrorMsg("Error",response.getString("Desc"),getActivity());


                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        };
        return response;
    }


    private void generateOTPJsonObjectRequest(JSONObject params) {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, Const.URL_GenerateOTP,
                params, generateOTPResponseRequesr(), generateOTPeErrorListenerRequesr()) {
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

    private Response.ErrorListener generateOTPeErrorListenerRequesr() {
        Response.ErrorListener response_error = new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("this is error",error.toString());


                AppController.spinnerStop();
            }
        };
        return response_error;
    }

    private Response.Listener<JSONObject>  generateOTPResponseRequesr() {
        Response.Listener<JSONObject> response = new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                Log.e("response ",response.toString());

                AppController.spinnerStop();
                try{
                    String StatusValue=response.getString("StatusValue");

                    if(StatusValue.equalsIgnoreCase("Success"))
                    {
                      ///  Intent intent=new Intent(getActivity(),OTPvalidateActivity.class);
                       // startActivity(intent);

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

}
