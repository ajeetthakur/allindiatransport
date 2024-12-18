package com.tpnagar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ForgotPassword extends Activity {

    public static String TAG=ForgotPassword.class.getCanonicalName();

    EditText et_email;
    TextView tv_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        et_email= (EditText) findViewById(R.id.fp_te_email);
        tv_login= (TextView) findViewById(R.id.fp_tv_login);

        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Activity context=ForgotPassword.this;

                if (et_email.getEditableText().toString() == null || et_email.getEditableText().toString().equals(""))
                {   //et_email.setError("Please ! Enter  Email Address.");
                    AppController.showToast(context, "please enter Email");
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

        //back btn code
/*        ImageView imgBack= (ImageView) findViewById(R.id.tab_imgbtn_back);
        final TextView tabtext= (TextView) findViewById(R.id.tab_txt);
        tabtext.setText("Forgot Password");

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ForgotPassword.this,LoginActivity.class);
                startActivity(intent);
                finish();


            }
        });*/


    }


    public boolean emailValidator(String email)
    {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private void sendForPassData() {
        JSONObject parmas = new JSONObject();

        try {
/*{

    "Login_Email": "bijaymandal@gmail.com",

    "Login_Phone": "9015505211",

    "SelectedForgetOption": "3", //1 for email, 2 for sms and 3 for both

    "SendPWDOn": "3", //1 for email, 2 for sms and 3 for both

    "User_Name": "9015505211"// logged in user name

}*/

            parmas.put("Login_Email", et_email.getText().toString());
            parmas.put("Login_Phone", et_email.getText().toString());
            parmas.put("SelectedForgetOption", "3");
            parmas.put("SendPWDOn","3");

            parmas.put("User_Name", et_email.getText().toString());
            AppController.spinnerStart(ForgotPassword.this);
            Log.e(TAG,"Send json obj:::"+ parmas.toString());
            requestForgotPassword(parmas);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    private void requestForgotPassword(JSONObject parmas)
    {


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, Const.URL_forgetpwd,
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
        AppController.getInstance().addToRequestQueue(jsonObjReq, "forgetpass");


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

                        AlertDialog.Builder builder = new AlertDialog.Builder(ForgotPassword.this);
                        builder.setCancelable(false);
                        builder.setTitle("Alert!");
                        builder.setMessage(response.getString("Desc")+" on Your registered Email Id")
                                .setCancelable(true)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.dismiss();
                                        //Intent intent=new Intent(ForgotPassword.this,LoginActivity.class);
                                       // startActivity(intent);
                                        finish();
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
