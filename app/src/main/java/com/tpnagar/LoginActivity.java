package com.tpnagar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.tpnagar.wrapper.CompanyTypeWrapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends Activity {
    SharedPreferences prefs = null;
    ConnectionDetector cd;
    EditText txtpassword, txtUsername;
    TextView text1, text2;
    ImageButton signin;
    String refreshedToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        cd = new ConnectionDetector(this);
        text1 = (TextView) findViewById(R.id.text1);
        text2 = (TextView) findViewById(R.id.text2);


        text2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(LoginActivity.this, ForgotPassword.class);
                startActivity(intent);

            }
        });
        prefs = getSharedPreferences("com.tpnagar", MODE_PRIVATE);
        txtpassword = (EditText) findViewById(R.id.txtpassword);
        txtUsername = (EditText) findViewById(R.id.txtUsername);

        signin = (ImageButton) findViewById(R.id.signin);

        if (AppController.isInternetPresent(this)) {
            new AsyncTaskRunner().execute();
        } else {
            AppController.popErrorMsg("Alert!", "Please check network connection.", LoginActivity.this);
        }

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Activity context = LoginActivity.this;
                if (txtUsername.getEditableText().toString() == null || txtUsername.getEditableText().toString().equals("")) {

                    AppController.showToast(context, "please enter Username");
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


        // LoggedInWithFb();
    }


    void LoggedInWithFb() {

        JSONObject obj = new JSONObject();

        try {


            obj.put("UserName", txtUsername.getText().toString());

            obj.put("Password", txtpassword.getText().toString());

            obj.put("FCM_Token", refreshedToken);
            AppController.spinnerStart(LoginActivity.this);

            Log.e("this is obj:::", obj.toString());


            FromCityJsonObjectRequest(obj);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void FromCityJsonObjectRequest(JSONObject params) {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, Const.URL_validatelogin,
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
                Log.e("this is error", error.toString());


                AppController.spinnerStop();
            }
        };
        return response_error;
    }

    private Response.Listener<JSONObject> newFromCityResponseRequesr() {
        Response.Listener<JSONObject> response = new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(final JSONObject response) {

                Log.e("response ", response.toString());

                AppController.spinnerStop();
                try {
                    String StatusValue = response.getString("StatusValue");

                    if (StatusValue.equalsIgnoreCase("Success")) {
                        JSONObject jSONObject = response.getJSONObject("Data");
                        String s = jSONObject.getString("Id");
                        prefs.edit().putString("Login_Id", s).commit();
                        prefs.edit().putString("IsActive", "yes").commit();

                        prefs.edit().putString("Password", txtpassword.getText().toString()).commit();
                        prefs.edit().putString("ProfileUpdate", "no").commit();


                        if (prefs.getString("Login_Id", "").length() > 0) {
                            JSONObject obj = new JSONObject();

                            try {


                                obj.put("LoginId", prefs.getString("Login_Id", ""));


                                AppController.spinnerStart(LoginActivity.this);
                                serviceGetCompanyDetailsRequestCompanyType(obj);
                                Log.e("this is obj:::", obj.toString());


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {

                        }


                    } else {

                        JSONObject jsonObjectnew = response.getJSONObject("Data");

                        if (jsonObjectnew.getString("ErrorCode").equalsIgnoreCase("T31019")) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                            builder.setTitle("Error").setMessage("Signup OTP Not Validated Please Generate OTP and Validate it")
                                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();


                                            JSONObject obj = new JSONObject();

                                            try {
                                                JSONObject jsonObjectnew = response.getJSONObject("Data");

                                                String s = jsonObjectnew.getString("Login_Id");
                                                prefs.edit().putString("Login_Id", s).commit();

                                                obj.put("LoginId", jsonObjectnew.getString("Login_Id"));

                                                obj.put("SMSType", 1);

                                                AppController.spinnerStart(LoginActivity.this);

                                                Log.e("this is obj:::", obj.toString());


                                                generateOTPJsonObjectRequest(obj);


                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }


                                        }
                                    });

                            AlertDialog alert = builder.create();
                            alert.show();


                        } else if (response.getString("Desc").equalsIgnoreCase("null")) {


                            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                            builder.setTitle("Error").setMessage("Signup OTP Not Validated Please Generate OTP and Validate it")
                                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();


                                            JSONObject obj = new JSONObject();

                                            try {
                                                JSONObject jsonObjectnew = response.getJSONObject("Data");

                                                String s = jsonObjectnew.getString("Login_Id");
                                                prefs.edit().putString("Login_Id", s).commit();

                                                obj.put("LoginId", jsonObjectnew.getString("Login_Id"));

                                                obj.put("SMSType", 1);
                                                AppController.spinnerStart(LoginActivity.this);

                                                Log.e("this is obj:::", obj.toString());


                                                generateOTPJsonObjectRequest(obj);


                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }


                                        }
                                    });

                            AlertDialog alert = builder.create();
                            alert.show();


                        } else {
                            AppController.popErrorMsg("Error", response.getString("Desc"), LoginActivity.this);


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
                Log.e("this is error", error.toString());


                AppController.spinnerStop();
            }
        };
        return response_error;
    }

    private Response.Listener<JSONObject> generateOTPResponseRequesr() {
        Response.Listener<JSONObject> response = new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                Log.e("response ", response.toString());

                AppController.spinnerStop();
                try {
                    String StatusValue = response.getString("StatusValue");

                    if (StatusValue.equalsIgnoreCase("Success")) {
                        Intent intent = new Intent(LoginActivity.this, OTPvalidateActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        AppController.popErrorMsg("Error", response.getString("Desc"), LoginActivity.this);


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        };
        return response;
    }

    private void loggedInUserInfoRequestCompanyType(JSONObject params) {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, Const.URL_LoggedInUserInfo + prefs.getString("Login_Id", "1"),
                params, newResponseRequesrtLoggedInUserInfo(), eErrorListenerRequesrtLoggedInUserInfo()) {
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

    private Response.ErrorListener eErrorListenerRequesrtLoggedInUserInfo() {
        Response.ErrorListener response_error = new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("this is error", error.toString());


                AppController.spinnerStop();
                Intent intent = new Intent(LoginActivity.this, com.tpnagar.designdemo.MainActivity.class);
                startActivity(intent);
                finish();
            }
        };
        return response_error;
    }

    public Response.Listener<JSONObject> newResponseRequesrtLoggedInUserInfo() {
        Response.Listener<JSONObject> response = new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                ArrayList<CompanyTypeWrapper> companyTypeWrapperslist = new ArrayList<CompanyTypeWrapper>();
                Log.e("CompanyType", response.toString());
                ArrayList<String> arrayList1 = new ArrayList<String>();
                try {
                    String StatusValue = response.getString("StatusValue");

                    if (StatusValue.equalsIgnoreCase("Success")) {

                       /* "Data": {
                        "Login_Id": 1,
                                "Company_Id": 1,
                                "Company_Name": "Patrotech Systems",
                                "Role_Id": 3,
                                "Role_Name": "Company Admin",
                                "Display_Name": "Patrotech Systems",
                                "Token": "",
                                "Status_Id": "2",
                                "Status_Name": "In-Active",
                                "Last_Login_Date": "12/29/2016 2:01:36 AM",
                                "Days_Of_Pwd_Expire": 0,
                                "Version": null,
                                "Tenant": null,
                                "ActivityId": null

*/


                        JSONObject jsonbObject = response.getJSONObject("Data");


                        prefs.edit().putString("Company_Id", jsonbObject.getString("Company_Id")).commit();
                        prefs.edit().putString("Role_Id", jsonbObject.getString("Role_Id")).commit();

                        prefs.edit().putString("Role_Name", jsonbObject.getString("Role_Name")).commit();
                        prefs.edit().putString("Display_Name", jsonbObject.getString("Display_Name")).commit();
                        prefs.edit().putString("Token", jsonbObject.getString("Token")).commit();
                        prefs.edit().putString("Status_Id", jsonbObject.getString("Status_Id")).commit();
                        prefs.edit().putString("Status_Name", jsonbObject.getString("Status_Name")).commit();
                        prefs.edit().putString("Broker_Search_Enabled", jsonbObject.getString("Broker_Search_Enabled")).commit();


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                AppController.spinnerStop();
                Intent intent = new Intent(LoginActivity.this, com.tpnagar.designdemo.MainActivity.class);
                startActivity(intent);
                finish();


            }
        };
        return response;
    }

    private void serviceGetCompanyDetailsRequestCompanyType(JSONObject params) {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, Const.URL_GetCompanyDetails + prefs.getString("Company_Id", "1"),
                params, newResponseRequesrtGetCompanyDetails(), eErrorListenerRequesrtGetCompanyDetails()) {
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

    private Response.ErrorListener eErrorListenerRequesrtGetCompanyDetails() {
        Response.ErrorListener response_error = new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("this is error", error.toString());


                AppController.spinnerStop();
            }
        };
        return response_error;
    }

    public Response.Listener<JSONObject> newResponseRequesrtGetCompanyDetails() {
        Response.Listener<JSONObject> response = new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                ArrayList<CompanyTypeWrapper> companyTypeWrapperslist = new ArrayList<CompanyTypeWrapper>();
                Log.e("CompanyType PREM", response.toString());
                ArrayList<String> arrayList1 = new ArrayList<String>();
                try {
                    String StatusValue = response.getString("StatusValue");

                    if (StatusValue.equalsIgnoreCase("Success")) {
                        JSONObject jsonbObject = response.getJSONObject("Data");

                        prefs.edit().putString("Id", jsonbObject.getString("Id")).commit();
                        prefs.edit().putString("Company_Name", jsonbObject.getString("Company_Name")).commit();
                        prefs.edit().putString("Company_Desc", jsonbObject.getString("Company_Desc")).commit();
                        prefs.edit().putString("Owner_Name", jsonbObject.getString("Owner_Name")).commit();
                        prefs.edit().putString("Contact_Person", jsonbObject.getString("Contact_Person")).commit();
                        prefs.edit().putString("Address", jsonbObject.getString("Address")).commit();
                        prefs.edit().putString("Fax_No", jsonbObject.getString("Fax_No")).commit();
                        prefs.edit().putString("Country_Id", jsonbObject.getString("Country_Id")).commit();
                        prefs.edit().putString("State_Id", jsonbObject.getString("State_Id")).commit();
                        prefs.edit().putString("City_Id", jsonbObject.getString("City_Id")).commit();

                        prefs.edit().putString("City_Name", jsonbObject.getString("City_Name")).commit();
                        prefs.edit().putString("State_Name", jsonbObject.getString("State_Name")).commit();

                        prefs.edit().putString("Area_Id", jsonbObject.getString("Area_Id")).commit();
                        prefs.edit().putString("Website", jsonbObject.getString("Website")).commit();
                        prefs.edit().putString("Pin_Code", jsonbObject.getString("Pin_Code")).commit();
                        prefs.edit().putString("Logo", jsonbObject.getString("Logo")).commit();
                        //  prefs.edit().putString("Company_Category_Id",jsonbObject.getString("Company_Category_Id")).commit();
                        prefs.edit().putString("Company_Type_Id", jsonbObject.getString("Company_Type_Id")).commit();
                        prefs.edit().putString("Current_Status_Id", jsonbObject.getString("Current_Status_Id")).commit();
                        prefs.edit().putString("Mobile_No", jsonbObject.getString("Mobile_No")).commit();
                        prefs.edit().putString("Storage_Id", jsonbObject.getString("Storage_Id")).commit();
                        prefs.edit().putString("Phone_No", jsonbObject.getString("Phone_No")).commit();
                        prefs.edit().putString("Email", jsonbObject.getString("Email")).commit();


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                JSONObject obj = new JSONObject();

                try {


                    obj.put("LoginId", prefs.getString("Login_Id", ""));


                    loggedInUserInfoRequestCompanyType(obj);


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        };
        return response;
    }

    private class AsyncTaskRunner extends AsyncTask<String, String, String> {
        private String resp;

        @Override
        protected String doInBackground(String... params) {
            // FirebaseDatabase.getInstance().setPersistenceEnabled(true);

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
            //  AppController.showToast(LoginActivity.this, result);
        }

        @Override
        protected void onPreExecute() {

        }

    }
}
