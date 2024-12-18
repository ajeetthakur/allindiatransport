package com.tpnagar.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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
import com.tpnagar.Const;
import com.tpnagar.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class ChangeMobileFragment extends BaseContainerFragment {

    public static String TAG=ChangeMobileFragment.class.getCanonicalName();
    SharedPreferences prefs = null;
    EditText et_email;
    TextView tv_login;
    View rootView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.change_mobile, container, false);
        et_email= (EditText) rootView.findViewById(R.id.number);
        tv_login= (TextView) rootView.findViewById(R.id.submit);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        et_email.setHint("Enter Your New Mobile number");

        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Activity context=getActivity();

                if (et_email.getEditableText().toString() == null || et_email.getEditableText().toString().equals(""))
                {   //et_email.setError("Please ! Enter  Email Address.");
                    AppController.showToast(context, "please enter New Mobile Number");
                    return;}

                else {

                    if (AppController.isInternetPresent(context)) {
                        sendForPassData();
                    } else {
                        AppController.showToast(context, "please check internet connection");
                    }



                }


            }
        });

        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = getActivity().getSharedPreferences("com.tpnagar", MODE_PRIVATE);

        ((com.tpnagar.designdemo.MainActivity) getActivity())
                .getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#FFFFFF\">" +"Change Mobile Number" + "</font>")));



    }




    private void sendForPassData() {
        JSONObject parmas = new JSONObject();

        try {
/*
          {
  " LoginId": 24,
  "OldMobile": "9015505211",
  " NewMobile": "9953021422"
}*/
            prefs = getActivity().getSharedPreferences("com.tpnagar", MODE_PRIVATE);
            parmas.put("LoginId", prefs.getString("Login_Id",""));
            parmas.put("OldMobile",AppController.PrimaryMobile);

            parmas.put("NewMobile", et_email.getText().toString());
            AppController.spinnerStart(getActivity());
            Log.e(TAG,"Send json obj:::"+ parmas.toString());
            requestForgotPassword(parmas);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    private void requestForgotPassword(JSONObject parmas)
    {


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, Const.URL_changeloginmobile,
                parmas, newFromCityResponseRequesr(), eErrorListenerRequesr()) {
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
                Log.e(TAG,"this is error"+error.toString());


                 AppController.spinnerStop();
            }
        };
        return response_error;
    }

    private Response.Listener<JSONObject> newFromCityResponseRequesr() {
        Response.Listener<JSONObject> response = new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                Log.e(TAG,"response "+response.toString());

                  AppController.spinnerStop();
                try{
                    String StatusValue=response.getString("StatusValue");

                    if(StatusValue.equalsIgnoreCase("Success"))
                    {
                        prefs.edit().putString("Mobile_No",et_email.getText().toString()).commit();
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setCancelable(false);
                        builder.setTitle("Alert!");
                        builder.setMessage(response.getString("Desc"))
                                .setCancelable(true)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.dismiss();
                                        et_email.setText("");
                                        //Intent intent=new Intent(ForgotPassword.this,LoginActivity.class);
                                        // startActivity(intent);
                                        //finish();
                                    }
                                });

                        AlertDialog alert = builder.create();
                        alert.show();

                    }else {
                        // AppController.popErrorMsg("Alert!",StatusValue,LoginActivity.this);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        };
        return response;
    }






}
