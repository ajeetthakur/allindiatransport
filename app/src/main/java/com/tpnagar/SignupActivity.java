package com.tpnagar;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends Activity {
    SharedPreferences prefs = null;
    ConnectionDetector cd;
    TextView text1;
    EditText txtcompanyname,txtemail,txtmobile,txtpassword;
    ImageButton signup;
    String refreshedToken="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        if (AppController.isInternetPresent(this)) {
            new SignupActivity.AsyncTaskRunner().execute();
        } else {
            AppController.popErrorMsg("Alert!", "Please check network connection.", SignupActivity.this);
        }
        cd = new ConnectionDetector(this);
       getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        text1=(TextView) findViewById(R.id.text1);

        txtcompanyname=(EditText) findViewById(R.id.txtcompanyname);
        txtemail=(EditText) findViewById(R.id.txtemail);
        txtmobile=(EditText) findViewById(R.id.txtmobile);
        txtpassword=(EditText) findViewById(R.id.txtpassword);
        text1.setTypeface(AppController.Externalregular(this));

        prefs = getSharedPreferences("com.tpnagar", MODE_PRIVATE);
        signup=(ImageButton) findViewById(R.id.signup);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                Activity context=SignupActivity.this;
                if (txtcompanyname.getEditableText().toString() == null || txtcompanyname.getEditableText().toString().equals("")) {

                    AppController.showToast(context, "please enter Username");
                    return;

                }

                if (txtpassword.getEditableText().toString() == null || txtpassword.getEditableText().toString().equals("")) {

                    AppController.showToast(context, "please enter password");
                    return;

                }
                if (txtpassword.getEditableText().toString().length()<5) {

                    AppController.showToast(context, "please enter password minimum 6 length");
                    return;

                }

              /*  if (txtemail.getEditableText().toString() == null || txtemail.getEditableText().toString().equals("")) {

                    AppController.showToast(context, "please enter email");
                    return;

                }*/
                if (txtmobile.getEditableText().toString() == null || txtmobile.getEditableText().toString().equals("")) {

                    AppController.showToast(context, "please enter mobile");
                    return;

                }

if(!txtemail.getEditableText().toString().equals("")) {
    if (!isValidEmail(txtemail.getEditableText().toString())) {

        AppController.showToast(context, "please enter valid email");
        return;
    }
}
                if (AppController.isInternetPresent(context)) {
                    LoggedInWithFb();

                } else {

                    AppController.showToast(context, "please check internet connection");
                }






            }






        });

    }


    void LoggedInWithFb(){

        JSONObject obj = new JSONObject();

        try {
            obj.put("Company_Name", txtcompanyname.getText().toString());
            obj.put("Mobile_No", txtmobile.getText().toString());
            obj.put("Email", txtemail.getText().toString());
            obj.put("Password", txtpassword.getText().toString());
            obj.put("FCM_Token", refreshedToken);
            obj.put("AppSource", "2");
            AppController.spinnerStart(SignupActivity.this);

            Log.e("this is obj:::",obj.toString());


            FromCityJsonObjectRequest(obj);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void FromCityJsonObjectRequest(JSONObject params) {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, Const.URL_signup,
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

        AppController.getInstance().addToRequestQueue(jsonObjReq, "register");
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
                try{
                    String StatusValue=response.getString("StatusValue");

                    if(StatusValue.equalsIgnoreCase("Success"))
                    {
                        JSONObject jSONObject = response.getJSONObject("Data");
                        String s=jSONObject.getString("Id");
                        prefs.edit().putString("Login_Id",s).commit();
                        prefs.edit().putString("IsActive","no").commit();
                        prefs.edit().putString("Password",txtpassword.getText().toString()).commit();

                        prefs.edit().putString("ProfileUpdate", "no").commit();
                        Intent intent=new Intent(SignupActivity.this,OTPvalidateActivity.class);
                        startActivity(intent);
                        finish();

                    }else {

                        AppController.popErrorMsg("Error",response.getString("Desc"),SignupActivity.this);


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                AppController.spinnerStop();

            }
        };
        return response;
    }

    public final static boolean isValidEmail(CharSequence target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    private class AsyncTaskRunner extends AsyncTask<String, String, String> {
        private String resp;
        @Override
        protected String doInBackground(String... params) {
          //  FirebaseDatabase.getInstance().setPersistenceEnabled(true);

            FirebaseMessaging.getInstance().getToken()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful() && task.getResult() != null) {
                            //   sendFCMTokenToDatabase(task.getResult());
                            refreshedToken = task.getResult();
                            // Log.d(TAG, "New Token: " + refreshedToken);
                        }
                    });


            return refreshedToken;
        }

        @Override
        protected void onPostExecute(String result) {
           // AppController.showToast(SignupActivity.this, result);
        }
        @Override
        protected void onPreExecute() {

        }

    }

}
