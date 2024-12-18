package com.tpnagar.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
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
import com.tpnagar.viewgallery.ImageAttachmentProfileActivity;
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
public class GalleryFragment extends BaseContainerFragment {
    SharedPreferences prefs = null;
    ConnectionDetector cd;
    ImageView imageView1,imageView2,imageView3,imageView4,imageView5,imageView6;
    ImageView delete1,delete2,delete3,delete4,delete5,delete6;

    RatingBar ratingbar;

    ArrayList<AreaWrapper> areaWrappers=new ArrayList<AreaWrapper>();

    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.gallery_fragment, container, false);

        prefs = getActivity().getSharedPreferences("com.tpnagar", MODE_PRIVATE);
        if(prefs.getString("IsActive","").equalsIgnoreCase("yes"))
        {
            ((com.tpnagar.designdemo.MainActivity) getActivity())
                    .getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#FFFFFF\">" +"Gallery" + "</font>")));

        }else {
            ((com.tpnagar.designdemo.MainActivityWithoutLogin) getActivity())
                    .getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#FFFFFF\">" +"Gallery" + "</font>")));

        }

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initXmlViews(view);



    }


    private void initXmlViews(View view) {
        cd = new ConnectionDetector(getActivity());
        prefs = getActivity().getSharedPreferences("com.tpnagar", MODE_PRIVATE);

        imageView1=(ImageView) view.findViewById(R.id.imageView1);
        imageView2=(ImageView) view.findViewById(R.id.imageView2);
        imageView3=(ImageView) view.findViewById(R.id.imageView3);
        imageView4=(ImageView) view.findViewById(R.id.imageView4);
        imageView5=(ImageView) view.findViewById(R.id.imageView5);
        imageView6=(ImageView) view.findViewById(R.id.imageView6);

        delete1=(ImageView) view.findViewById(R.id.delete1);
        delete2=(ImageView) view.findViewById(R.id.delete2);
        delete3=(ImageView) view.findViewById(R.id.delete3);
        delete4=(ImageView) view.findViewById(R.id.delete4);
        delete5=(ImageView) view.findViewById(R.id.delete5);
        delete6=(ImageView) view.findViewById(R.id.delete6);


      /*  if (AppController.isInternetPresent(getActivity())) {
            getCompanyProfilePicsRequestApi();

        } else {

            AppController.showToast(getActivity(), "please check internet connection");
        }*/

        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              Intent intent = new Intent(getActivity(), ImageAttachmentProfileActivity.class);
              startActivityForResult(intent, AppController.IMAGEPICKER);

            }
        });


       /* delete1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (AppController.isInternetPresent(getActivity())) {
                    addRatingApi();

                } else {

                    AppController.showToast(getActivity(), "please check internet connection");
                }


            }
        });*/



    }


    void getCompanyProfilePicsRequestApi(){

        JSONObject obj = new JSONObject();

        try {
          String  id=prefs.getString("Company_Id","");
            obj.put("Company_Id", id);


            AppController.spinnerStart(getActivity());

            Log.e("this is obj:::",obj.toString());


            getCompanyProfilePicsRequest(obj);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void getCompanyProfilePicsRequest(JSONObject params) {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, Const.URL_SHOWVENDOROFCOMPANY+prefs.getString("Company_Id",""),
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

        AppController.getInstance().addToRequestQueue(jsonObjReq, "GetCompanyProfilePics");
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
/*
                        Response:
                        Data: {
                            "Id": 2,
                                    "Company_Id": 0,
                                    "Logo_File_Name": "akshay.PNG",
                                    "Logo": null,
                                    "ProfilePic1_File_Name": "LMS.jpg",
                                    "ProfilePic1": null,
                                    "ProfilePic2_File_Name": "LMS_Testing.PNG",
                                    "ProfilePic2": null,
                                    "ProfilePic3_File_Name": "",
                                    "ProfilePic3": null,
                                    "ProfilePic4_File_Name": "",
                                    "ProfilePic4": null,
                                    "ProfilePic5_File_Name": "",
                                    "ProfilePic5": null,
                                    "ProfilePic6": null,
                                    "RootPathApi": "http://localhost:4875",
                                    "RootFolder": "UploadedFiles"
                        }
                        Desc: ""
                        MethodName: "GetCompanyProfilePics"
                        StatusCode: 0
                        StatusValue: "Success"*/

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

    void updateCompanyProfilePicsApi(){

        JSONObject obj = new JSONObject();

        try {
            String  id=prefs.getString("Company_Id","");
            obj.put("Company_Id", id);
            obj.put("Logo", id);
            obj.put("Logo_File_Name", "");
            obj.put("ProfilePic1", "");
            obj.put("ProfilePic1_File_Name", id);
            obj.put("ProfilePic2", id);
            obj.put("ProfilePic2_File_Name", id);
            obj.put("ProfilePic3", id);
            obj.put("ProfilePic3_File_Name", id);
            obj.put("ProfilePic4", id);
            obj.put("ProfilePic4_File_Name", id);
            obj.put("ProfilePic5", id);
            obj.put("ProfilePic5_File_Name", id);
            obj.put("ProfilePic6", id);
            obj.put("ProfilePic6_File_Name", id);


            AppController.spinnerStart(getActivity());

            Log.e("this is obj:::",obj.toString());


            updateProfilePicsRequest(obj);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void updateProfilePicsRequest(JSONObject params) {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, Const.URL_UpdateCompanyProfilePics,
                params, requesrtupdate(), eErrorListenerRequesrt()) {
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

        AppController.getInstance().addToRequestQueue(jsonObjReq, "GetCompanyProfilePics");
    }

    private Response.ErrorListener eErrorListenerRequesrt() {
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

    private Response.Listener<JSONObject> requesrtupdate() {
        Response.Listener<JSONObject> response = new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                Log.e("response ",response.toString());

                AppController.spinnerStop();
                try{
                    String StatusValue=response.getString("StatusValue");

                    if(StatusValue.equalsIgnoreCase("Success"))
                    {
/*
                       {
"Data": { "Id": "CompanyId" },
    StatusCode: 0,
    StatusValue: "Success",
    Desc: "",
    MethodName: "GetCompanyProfilePics"

}*/



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
