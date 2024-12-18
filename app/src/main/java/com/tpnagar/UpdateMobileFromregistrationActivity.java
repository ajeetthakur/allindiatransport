package com.tpnagar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UpdateMobileFromregistrationActivity extends Activity {
    SharedPreferences prefs = null;
    ConnectionDetector cd;
    EditText txtotp;

    Button send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_mobilefromreg);
        cd = new ConnectionDetector(this);

        prefs = getSharedPreferences("com.tpnagar", MODE_PRIVATE);
        txtotp = (EditText) findViewById(R.id.txtotp);


        send = (Button) findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Activity context = UpdateMobileFromregistrationActivity.this;


                if (txtotp.getText().toString() == null || txtotp.getText().toString().equalsIgnoreCase("")) {

                    AppController.showToast(context, "please enter mobile number");
                    return;

                }

                if (! (txtotp.getText().toString().length()>9)) {

                    AppController.showToast(context, "please enter mobile number correctly");
                    return;

                }


                if (AppController.isInternetPresent(context)) {
                    UpdatemobileApi();

                } else {

                    AppController.showToast(context, "please check internet connection");
                }


            }
        });


    }


    void UpdatemobileApi() {

        JSONObject obj = new JSONObject();

        try {


            obj.put("Login_Id", prefs.getString("Login_Id", ""));

            obj.put("Mobile_No", txtotp.getText().toString());
            AppController.spinnerStart(UpdateMobileFromregistrationActivity.this);

            Log.e("this is obj:::", obj.toString());


            FromCityJsonObjectRequest(obj);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void FromCityJsonObjectRequest(JSONObject params) {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, Const.URL_updateloginmobile,
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
        AppController.getInstance().addToRequestQueue(jsonObjReq, "validatelogin");
    }

    private Response.ErrorListener eErrorListenerRequesr() {
        Response.ErrorListener response_error = new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("this is error", error.toString());


                AppController.spinnerStop();
            }
        };
        return response_error;
    }

    private Response.Listener<JSONObject> newFromCityResponseRequesr() {
        Response.Listener<JSONObject> response = new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                Log.e("response ", response.toString());
                try {
                    String StatusValue = response.getString("StatusValue");

                    if (StatusValue.equalsIgnoreCase("Success"))

                    {
                        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateMobileFromregistrationActivity.this);
                        builder.setTitle("Alert").setMessage("Please verify new  mobile Number OTP. Generate OTP and Validate "+txtotp.getText().toString())
                                .setPositiveButton("ok", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();



                                        JSONObject obj = new JSONObject();
                                        try {


                                            obj.put("LoginId", prefs.getString("Login_Id", ""));

                                            obj.put("SMSType", 1);
                                            AppController.spinnerStart(UpdateMobileFromregistrationActivity.this);

                                            Log.e("this is obj:::",obj.toString());


                                            generateOTPJsonObjectRequest(obj);


                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }



                                    }
                                });

                        AlertDialog alert = builder.create();
                        alert.show();

                        JSONObject obj = new JSONObject();




                    } else {


                        AppController.popErrorMsg("Error", response.getString("Desc"), UpdateMobileFromregistrationActivity.this);


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                AppController.spinnerStop();

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
                        Intent intent=new Intent(UpdateMobileFromregistrationActivity.this,OTPvalidateActivity.class);
                        startActivity(intent);
                        finish();
                    }else {
                        AppController.popErrorMsg("Error",response.getString("Desc"),UpdateMobileFromregistrationActivity.this);



                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        };
        return response;
    }
}
