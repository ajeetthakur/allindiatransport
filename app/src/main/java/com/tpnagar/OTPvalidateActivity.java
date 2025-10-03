package com.tpnagar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.tpnagar.designdemo.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class OTPvalidateActivity extends Activity {
    SharedPreferences prefs = null;
    ConnectionDetector cd;
    EditText txtotp;
    String phone="";
    Button send,update_mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        try {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                    WindowManager.LayoutParams.FLAG_SECURE);
        } catch (Exception e) {
        }
        cd = new ConnectionDetector(this);

        prefs = getSharedPreferences("com.tpnagar", MODE_PRIVATE);
        txtotp = (EditText) findViewById(R.id.txtotp);
        update_mobile= (Button) findViewById(R.id.update_mobile);
        send = (Button) findViewById(R.id.send);
        update_mobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OTPvalidateActivity.this, UpdateMobileFromregistrationActivity.class);
                startActivity(intent);
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Activity context = OTPvalidateActivity.this;


                if (txtotp.getText().toString() == null || txtotp.getText().toString().equals("")) {

                    AppController.showToast(context, "please enter OTP");
                    return;

                }


                if (AppController.isInternetPresent(context)) {
                    OTPvalidateApi();

                } else {

                    AppController.showToast(context, "please check internet connection");
                }


            }
        });

        getLoginDetailApi();
    }


    void OTPvalidateApi() {

        JSONObject obj = new JSONObject();

        try {


            obj.put("LoginId", prefs.getString("Login_Id", ""));

            obj.put("OTP", txtotp.getText().toString());
            AppController.spinnerStart(OTPvalidateActivity.this);

            Log.e("this is obj:::", obj.toString());


            FromCityJsonObjectRequest(obj);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void FromCityJsonObjectRequest(JSONObject params) {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, Const.URL_ValidateOTP,
                params, newFromCityResponseRequesr1(), eErrorListenerRequesr1()) {
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

    private Response.ErrorListener eErrorListenerRequesr1() {
        Response.ErrorListener response_error = new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("this is error", error.toString());


                AppController.spinnerStop();
            }
        };
        return response_error;
    }

    private Response.Listener<JSONObject> newFromCityResponseRequesr1() {
        Response.Listener<JSONObject> response = new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                Log.e("response ", response.toString());
                try {
                    String StatusValue = response.getString("StatusValue");

                    if (StatusValue.equalsIgnoreCase("Success"))

                    {

                        AlertDialog.Builder builder = new AlertDialog.Builder(OTPvalidateActivity.this);
                        builder.setTitle("Alert!").setMessage("Your OTP Successfully Validated")
                                .setPositiveButton("ok", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();


                                        prefs.edit().putString("IsActive", "yes").commit();
                                        prefs.edit().putString("ProfileUpdate", "no").commit();
                                        Intent intent = new Intent(OTPvalidateActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();


                                    }
                                });

                        AlertDialog alert = builder.create();
                        alert.show();


                    } else {


                        AppController.popErrorMsg("Error", response.getString("Desc"), OTPvalidateActivity.this);


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                AppController.spinnerStop();

            }
        };
        return response;
    }

    void getLoginDetailApi(){

        JSONObject obj = new JSONObject();

        try {


            obj.put("LoginId", "");


            AppController.spinnerStart(OTPvalidateActivity.this);

            Log.e("this is obj:::",obj.toString());


            getLoginDetailJsonObjectRequest(obj);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getLoginDetailJsonObjectRequest(JSONObject params) {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, Const.URL_GetLoginDetail+prefs.getString("Login_Id",""),
                params, newFromCityResponseRequesr22(), eErrorListenerRequesr22()) {
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

    private Response.ErrorListener eErrorListenerRequesr22() {
        Response.ErrorListener response_error = new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("this is error",error.toString());


                AppController.spinnerStop();
            }
        };
        return response_error;
    }

    private Response.Listener<JSONObject> newFromCityResponseRequesr22() {
        Response.Listener<JSONObject> response = new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                Log.e("response ",response.toString());

                AppController.spinnerStop();
                try {
                    String StatusValue = response.getString("StatusValue");

                    if (StatusValue.equalsIgnoreCase("Success"))
                    {
                        JSONObject jsonObject=response.getJSONObject("Data");

                        String email=jsonObject.getString("Login_Email");
                         phone=jsonObject.getString("Login_Phone");
                        String pwd=jsonObject.getString("Pwd");

                        TextView text11= (TextView) findViewById(R.id.text11);

                        text11.setText("Please Update OTP with "+phone);
                       /* if(email!=null){
                            txtEmail.setText(email);
                        }
                        if(phone!=null){
                            txtPhone.setText(phone);
                        }
                        if(pwd!=null){
                            txtPwd.setText(pwd);
                        }
*/


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };
        return response;
    }

}
