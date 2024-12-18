package com.tpnagar.fragment;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
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


public class ChangePasswordFragment extends BaseContainerFragment {
	public static String TAG="ChangePasswordFragment";
	EditText et_oldpw,et_newpw,et_conpw;
	TextView tv_update;
	View root;
	SharedPreferences prefs = null;

View view;
	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

		view = inflater.inflate(R.layout.activity_change_password,
				container, false);
		getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		((com.tpnagar.designdemo.MainActivity) getActivity())
				.getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#FFFFFF\">" +"Change Password" + "</font>")));

		prefs = getActivity().getSharedPreferences("com.tpnagar", MODE_PRIVATE);
		et_oldpw= (EditText) view.findViewById(R.id.ch_et_oldPass);
		et_newpw= (EditText) view.findViewById(R.id.ch_et_newPass);
		et_conpw= (EditText) view.findViewById(R.id.ch_et_confPass);

		tv_update= (TextView) view.findViewById(R.id.ch_tv_update);

		root =view.findViewById(R.id.activity_change_password).getRootView();
		root.setBackgroundColor(Color.parseColor("#FFFFFF"));
		//tbview=view.findViewById(R.id.tab_view1);
		//tbview.setBackgroundColor(Color.parseColor("#FFEBFA"));


		tv_update.setOnClickListener(new View.OnClickListener() {
			Activity context=getActivity();
			@Override
			public void onClick(View v) {
				if (et_oldpw.getEditableText().toString() == null || et_oldpw.getEditableText().toString().equals(""))
				{   AppController.showToast(context, "please enter old Password");
					return;}
				else if (!et_oldpw.getText().toString().trim().equals(prefs.getString("Password","")))
				{  // et_newpw.setError("please enter new Password");
					AppController.showToast(context, "Old password is wrong.");
					return;

				}
				else if (et_newpw.getEditableText().toString() == null || et_newpw.getEditableText().toString().equals(""))
				{  // et_newpw.setError("please enter new Password");
					AppController.showToast(context, "please enter new Password");
					return;

				}
                /*else if (et_newpw.getText().length()<=6)
                {  AppController.showToast(context, "please enter password must be six digites more");
                    return;
                }*/
				else if (et_conpw.getEditableText().toString() == null || et_conpw.getEditableText().toString().equals(""))
				{     // et_conpw.setError("Please ! Enter Confirm Password.");
					AppController.showToast(context, "please enter confirm Password");
					return;

				}

				else if (!et_conpw.getText().toString().trim().equals(et_newpw.getText().toString()))
				{
					//et_conpw.setError("Please ! Enter Valid Confirm Password.");
					AppController.showToast(context, "Please ! Enter Your Confirm Password is Not Valid.");
					return;

				}
				else {
					// prefs=getSharedPreferences(LoginScreen.MyPREFERENCES, Context.MODE_PRIVATE);
					if (prefs.contains("Email")) {
						JSONObject parmas = new JSONObject();

						/*{
							"Login_Id": 24,
								"PWD": "bijay$1234",
								"NewPwd": "bijay$123"
						}*/


						try {
							parmas.put("Login_Id",prefs.getString("Login_Id",""));
							parmas.put("PWD",et_oldpw.getText().toString());

							parmas.put("NewPwd", et_newpw.getText().toString());
							AppController.spinnerStart(getActivity());
							Log.e(TAG, "Send json obj:::" + parmas.toString());

						} catch (JSONException e) {
							e.printStackTrace();
						}

						if (AppController.isInternetPresent(context)) {
							reqChangePass(parmas);

						} else {

							AppController.showToast(context, "please check internet connection");
						}


					}
					else {    AppController.showToast(context,"prefs value is null");
						Log.e(TAG,"Please check ! prefs value is null");
					}

				}





			}
		});

		//back btn code
	//	ImageView imgBack= (ImageView) view.findViewById(R.id.tab_imgbtn_back);
	//	final TextView tabtext= (TextView) view.findViewById(R.id.tab_txt);
	//	tabtext.setText("Change Password");

	/*	imgBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {


			}
		});*/


		return view;
	}



	private void reqChangePass(JSONObject parmas)
	{
		JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, Const.URL_changepwd,
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
						prefs.edit().putString("Password",et_newpw.getText().toString()).commit();


						AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
						builder.setCancelable(false);
						builder.setTitle("Alert!");
						builder.setMessage(response.getString("Desc"))
								.setCancelable(true)
								.setPositiveButton("OK", new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int id) {
										dialog.dismiss();

										et_oldpw.setText("");
												et_newpw.setText("");
										et_conpw.setText("");

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
