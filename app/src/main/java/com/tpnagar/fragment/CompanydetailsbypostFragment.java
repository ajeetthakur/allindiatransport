package com.tpnagar.fragment;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.content.ContextCompat;
import android.text.Editable;
import android.text.Html;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import com.tpnagar.DataManager;
import com.tpnagar.GeocodingLocation;
import com.tpnagar.MapsActivity;
import com.tpnagar.R;
import com.tpnagar.adapter.PhoneDetailsAdapter;
import com.tpnagar.adapter.RatingAdapter;
import com.tpnagar.wrapper.RatingWrapper;
import com.tpnagar.wrapper.SearchCompanyWrapper;
import com.tpnagar.wrapper.VendorCompanyWrapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by admin on 03-03-2016.
 */
public class CompanydetailsbypostFragment extends BaseContainerFragment implements AdapterView.OnItemSelectedListener, ActivityCompat.OnRequestPermissionsResultCallback{
    SharedPreferences prefs = null;
    ConnectionDetector cd;
    String cityId="1",categoryId="1";
    private AutoCompleteTextView actv;
    String[] services;
    Spinner spinner;
    private boolean rootViewId;
    ImageButton search;
    ArrayList<String> city = new ArrayList<String>();
    ArrayList<String> servicesaStrings = new ArrayList<String>();
    ArrayList<String> cityid = new ArrayList<String>();
    ListView listview,listview_rate;
    FragmentTransaction transaction;
    TextView trusted,rating_text;
    String cityidForLoc;
    String  SearchText="";
    String  Location="";
    SearchCompanyWrapper searchCompanyWrapper;
    TextView contact_person,more,company_name,rate_number,text_call,text_address,text_mail,text_website,text_discrp,view_loc,enquiry,branches,show_vendor;
    RatingBar ratingbar;
   String id;
    LinearLayout top_bar;
    View rootView;
    Dialog dialog;
    ListView mCompleteListView;
    ArrayList<String> strings;
    LayoutInflater layoutinflater;
    //TextView more_rate;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.search_details, container, false);
        top_bar=(LinearLayout) rootView.findViewById(R.id.top_bar);
        top_bar.setVisibility(View.GONE);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        id=getArguments().getString("id");
        transaction = getActivity().getSupportFragmentManager().beginTransaction();
        initXmlViews(view);

        JSONObject obj1 = new JSONObject();

        try {

            obj1.put("Password", "prem$123");
            AppController.spinnerStart(getActivity());

            Log.e("this is obj:::",obj1.toString());


            companyDetailsRequest(obj1);


        } catch (JSONException e) {
            e.printStackTrace();
        }




        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new Dialog(getActivity(),R.style.FullHeightDialog);
                // Include dialog.xml file
                dialog.setContentView(R.layout.dialoglist_phone);
                mCompleteListView = (ListView) dialog.findViewById(R.id.completeList);

                // PhoneDetailsAdapter mListAdapter = new PhoneDetailsAdapter(mContext, strings);
                // mCompleteListView.setAdapter(mListAdapter);
                TextView declineButton = (TextView) dialog.findViewById(R.id.declineButton);
                // if decline button is clicked, close the custom dialog
                declineButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Close dialog
                        dialog.dismiss();


                    }
                });
                dialog.show();
                PhoneDetailsAdapter mListAdapter = new PhoneDetailsAdapter(getActivity(), strings);
                mCompleteListView.setAdapter(mListAdapter);
           /*     mCompleteListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                        //onCall(strings.get(i));

                    }
                });*/
            }
        });

    }

    private void initXmlViews(View base) {
        cd = new ConnectionDetector(getActivity());

        search=(ImageButton) base.findViewById(R.id.search);

        actv = (AutoCompleteTextView) base.findViewById(R.id.autoCompleteTextView1);
        actv.clearFocus();
        spinner = (Spinner) base.findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);

        prefs = getActivity().getSharedPreferences("com.tpnagar", MODE_PRIVATE);

        listview_rate=  base.findViewById(R.id.listview_rate);


        layoutinflater = getLayoutInflater();

        ViewGroup header = (ViewGroup)layoutinflater.inflate(R.layout.item_header,listview_rate,false);

        show_vendor=  header.findViewById(R.id.show_vendor);
        contact_person=  header.findViewById(R.id.contact_person);
        company_name = base.findViewById(R.id.company_name);
        rate_number =  header.findViewById(R.id.rate_number);
        text_call =  header.findViewById(R.id.text_call);
        text_address =  header.findViewById(R.id.text_address);
        ratingbar= header.findViewById(R.id.ratingbar);
        text_mail=  header.findViewById(R.id.text_mail);
        text_website=  header.findViewById(R.id.text_website);
        text_discrp=  header.findViewById(R.id.text_discrp);
        view_loc=  header.findViewById(R.id.view_loc);
        enquiry=  header.findViewById(R.id.enquiry);
        branches= header.findViewById(R.id.branches);

         more=(TextView) header.findViewById(R.id.more);
         //more_rate=(TextView) header.findViewById(R.id.more_rate);

        listview_rate.addHeaderView(header);

        JSONObject obj = new JSONObject();

        try {


            obj.put("Company_Id",id);


            AppController.spinnerStart(getActivity());

           // Log.e("this is Company_Id", id);
            vendorlistRequest(obj);


        } catch (JSONException e) {
            e.printStackTrace();
        }

       /* ScrollView nestedScrollView=  base.findViewById(R.id.nestedScrollView);
        nestedScrollView.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                listview_rate.getParent()
                        .requestDisallowInterceptTouchEvent(false);
                return false;
            }
        });

        listview_rate.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });*/
        show_vendor.setVisibility(View.GONE);

        text_website.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(searchCompanyWrapper.getWebsite().replace("NULL","").length()>0){
                    String url=searchCompanyWrapper.getWebsite();


                    if (!url.startsWith("https://") && !url.startsWith("http://")){
                        url = "http://" + url;
                    }



                    Intent openUrlIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(openUrlIntent);

                }

            }
        });

        show_vendor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                Bundle bundle=new Bundle();
                bundle.putString("Company_Id",searchCompanyWrapper.getCompany_Id());
                bundle.putString("ISDELETEHIDE","true");

                VendorListFromDetailsFragment vendorListFromDetailsFragment=new VendorListFromDetailsFragment();
                vendorListFromDetailsFragment.setArguments(bundle);
                transaction.replace(R.id.frame_container, vendorListFromDetailsFragment, "");
                transaction.addToBackStack(null);
                // transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                transaction.commit();


            }
        });


        text_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onCall();


            }
        });


        ratingbar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {


                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                Bundle bundle=new Bundle();
                bundle.putString("id",id);

                RatingFragment searchDetailsFragment=new RatingFragment();
                searchDetailsFragment.setArguments(bundle);
                transaction.replace(R.id.frame_container, searchDetailsFragment, "");
                transaction.addToBackStack(null);
                // transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                transaction.commit();



                return false;
            }
        });



      view_loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AppController.spinnerStart(getActivity());

                GeocodingLocation.getAddressFromLocation(searchCompanyWrapper.getPin_Code(),
                        getActivity(), new GeocoderHandler());


            }
        });
        enquiry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragmentTask(new EnquiryFragment());
            }
        });
        branches.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                replaceFragmentTask(new BranchListForDetailsFragment());

            }
        });


        prefs = getActivity().getSharedPreferences("com.tpnagar", MODE_PRIVATE);

        if (base != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(base.getWindowToken(), 0);
        }
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);


        if(prefs.getString("IsActive","").equalsIgnoreCase("yes"))
        {
            ((com.tpnagar.designdemo.MainActivity) getActivity())
                    .getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#FFFFFF\">" +"Company Details" + "</font>")));

        }else {
            ((com.tpnagar.designdemo.MainActivityWithoutLogin) getActivity())
                    .getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#FFFFFF\">" +"Company Details" + "</font>")));

        }



        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String  SearchText="";
                String  Location="";

                if(actv.getText()==null){
                    SearchText="";
                }else {
                    SearchText=actv.getText().toString();
                }
                Location=spinner.getSelectedItem().toString();



                Bundle bundle=new Bundle();
                bundle.putString("Location",Location);
                bundle.putString("SearchText",SearchText);

                SearchListFragment searchListFragment=new SearchListFragment();
                searchListFragment.setArguments(bundle);


                replaceFragmentTask(searchListFragment);
            }
        });

        actv.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(s.length()>2)
                {
                    if(cd.isConnectingToInternet())

                    {
                        cityidForLoc=cityid.get(spinner.getSelectedItemPosition());
                        ServiceListOnCityAndCategory(s.toString());
                        // ;

                    }else {
                        AppController.popErrorMsg("Alert!", "please check internet connection",getActivity());

                    }
                }

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        city=DataManager.getInstance().getCity();
        cityid=DataManager.getInstance().getCityIds();
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, city);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

        int pos=AppController.POS;
        spinner.setSelection(pos);

        //RandomCityList();
    }

    public void replaceFragmentTask(Fragment fragment) {

      /*  transaction.setCustomAnimations(
                R.anim.slide_in_from_right, R.anim.slide_out_to_left,
                R.anim.slide_in_from_left, R.anim.slide_out_to_right);*/

      Bundle  bundle=new Bundle();
        bundle.putString("id",id);
        fragment.setArguments(bundle);
        transaction.replace(R.id.frame_container, fragment, "");
        transaction.addToBackStack(null);
        // transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.commit();
    }

    void ServiceListOnCityAndCategory(String editText){

        JSONObject obj = new JSONObject();
        try {
            obj.put("SearchText", editText);
            obj.put("Location", cityidForLoc);

            Log.e("this is obj:::",obj.toString());


            ServiceListOnCityAndCategoryRequest(obj,editText);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void ServiceListOnCityAndCategoryRequest(JSONObject params,String editText) {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, Const.URL_SearchData,
                params, newFromCityResponseRequesrSearch(), eErrorListenerRequesrSearch()) {
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

    private Response.ErrorListener eErrorListenerRequesrSearch() {
        Response.ErrorListener response_error = new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("this is error",error.toString());


                AppController.spinnerStop();
            }
        };
        return response_error;
    }

    private Response.Listener<JSONObject> newFromCityResponseRequesrSearch() {
        Response.Listener<JSONObject> response = new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                Log.e("ServiceListOnCityAn", response.toString());

                try {
                    String StatusValue = response.getString("StatusValue");
                    servicesaStrings = new ArrayList<String>();
                    if (StatusValue.equalsIgnoreCase("Success")) {
                        JSONArray jsonArray = response.getJSONArray("Data");
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject obj = jsonArray.getJSONObject(i);

                            servicesaStrings.add(obj.getString("Data"));

                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (servicesaStrings != null ) {
                    services = servicesaStrings.toArray(new String[0]);
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>
                        (getActivity(),android.R.layout.simple_list_item_1,services);
                actv.setAdapter(adapter);

          /*     actv.clearFocus();
                getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                if (actv != null) {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(actv.getWindowToken(), 0);
                }*/
                //  AppController.spinnerStop();

            }
        };
        return response;
    }
    void RandomCityList(){

        JSONObject obj1 = new JSONObject();

        try {

            obj1.put("Password", "prem$123");
            AppController.spinnerStart(getActivity());

            Log.e("this is obj:::",obj1.toString());


            RandomCityListRequest(obj1);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void RandomCityListRequest(JSONObject params) {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, Const.URL_RandomCityList,
                params, newRandomCityListResponseRequesr(), eErrorListenerRequesrRandomCityList()) {
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

    private Response.ErrorListener eErrorListenerRequesrRandomCityList() {
        Response.ErrorListener response_error = new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("this is error",error.toString());


                AppController.spinnerStop();
            }
        };
        return response_error;
    }

    private Response.Listener<JSONObject> newRandomCityListResponseRequesr() {
        Response.Listener<JSONObject> response = new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                Log.e("response RandomCityList",response.toString());

                try{
                    String StatusValue=response.getString("StatusValue");

                    if(StatusValue.equalsIgnoreCase("Success"))
                    {
                        JSONArray jsonArray = response.getJSONArray("Data");
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject obj = jsonArray.getJSONObject(i);

                            city.add(obj.getString("City_Name"));

                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // Creating adapter for spinner
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, city);

                // Drop down layout style - list view with radio button
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                // attaching data adapter to spinner
                spinner.setAdapter(dataAdapter);
             //   spinner.setSelection(getArguments().getInt("spinnerId"));


                AppController.spinnerStop();

            }
        };
        return response;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
       // Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private class GeocoderHandler extends Handler {

        @Override
        public void handleMessage(Message message) {
            String locationAddress;
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    locationAddress = bundle.getString("address");
                    break;
                default:
                    locationAddress = null;
            }


                    if (locationAddress != null) {

                        if (locationAddress.length() > 0) {

                            String[] separated = locationAddress.split(",");
                            AppController.spinnerStop();

                            if(separated.length>0) {


                                try {
                                    Intent intent = new Intent(getActivity(), MapsActivity.class);
                                    intent.putExtra("LatStr", separated[0]);
                                    intent.putExtra("LongStr", separated[1]);
                                    intent.putExtra("Location", searchCompanyWrapper.getAddress());


                                    startActivity(intent);
                                }catch (Exception e){

                                }



                            }else {
                                Toast.makeText(getActivity(), "Could not detect your location when lat long not detected", Toast.LENGTH_SHORT).show();

                            }

                        } else {

                            Toast.makeText(getActivity(), "Could not detect your location when lat long not detected", Toast.LENGTH_SHORT).show();

                        }


                    } else {
                        Toast.makeText(getActivity(), "Could not detect your location when lat long not detected", Toast.LENGTH_SHORT).show();

                    }


                    Log.e("locationAddress123", locationAddress);

                    // latLongTV.setText(locationAddress);


                }
            }

    public void onCall() {
        int permissionCheck = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, 1);
        } else {
            startActivity(new Intent(Intent.ACTION_CALL).setData(Uri.parse("tel:" + searchCompanyWrapper.getPrimary_Phone())));
        }

    }

    private void companyDetailsRequest(JSONObject params) {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, Const.URL_GetCompanyDetails+id,
                params, companyDetailsResponseRequest(), eErrorListenercompanyDetails()) {
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

    private Response.ErrorListener eErrorListenercompanyDetails() {
        Response.ErrorListener response_error = new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("this is error",error.toString());


                AppController.spinnerStop();
            }
        };
        return response_error;
    }

    private Response.Listener<JSONObject> companyDetailsResponseRequest() {
        Response.Listener<JSONObject> response = new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                Log.e("response RandomCityList",response.toString());
AppController.spinnerStop();
                try {
                    String StatusValue = response.getString("StatusValue");

                    if (StatusValue.equalsIgnoreCase("Success"))


                    {

                        searchCompanyWrapper=new SearchCompanyWrapper();

                        JSONObject obj = response.getJSONObject("Data");
                        //searchCompanyWrapper.set_id(obj.getString("id"));
                       // searchCompanyWrapper.setCompany_Id(obj.getString("Company_Id"));
                       // searchCompanyWrapper.setService_Id(obj.getString("Service_Id"));
                        searchCompanyWrapper.setCompany_Name(obj.getString("Company_Name"));
                        searchCompanyWrapper.setCompany_Desc(obj.getString("Company_Desc"));
                        searchCompanyWrapper.setContact_Person(obj.getString("Contact_Person"));
                        searchCompanyWrapper.setAddress(obj.getString("Address"));
                        searchCompanyWrapper.setCountry_Id(obj.getString("Country_Id"));
                        searchCompanyWrapper.setState_Id(obj.getString("State_Id"));
                      //  searchCompanyWrapper.setState_Name(obj.getString("State_Name"));
                        searchCompanyWrapper.setCity_Id(obj.getString("City_Id"));
                        searchCompanyWrapper.setWebsite(obj.getString("Website"));
                        searchCompanyWrapper.setPin_Code(obj.getString("Pin_Code"));
                        searchCompanyWrapper.setPrimary_Phone(obj.getString("Mobile_No")+","+obj.getString("Phone_No"));

                      //  searchCompanyWrapper.setRating(obj.getString("Rating"));
                        // searchCompanyWrapper.setRating("2.5");
                      //  searchCompanyWrapper.setCompany_Type_Id(obj.getString("Company_Type_Id"));
                     //   searchCompanyWrapper.setCurrent_Status_Id(obj.getString("Current_Status_Id"));

if(obj.getString("Has_Branch").equalsIgnoreCase("true")){
    branches.setVisibility(View.VISIBLE);

                    }else {
    branches.setVisibility(View.GONE);
                    }


                        String myString=searchCompanyWrapper.getCompany_Name();
                        String upperString = myString.substring(0,1).toUpperCase() + myString.substring(1);
                         contact_person.setText(searchCompanyWrapper.getContact_Person());
                        company_name.setText(upperString.toUpperCase());
                        company_name.setTextColor(Color.RED);
                        rate_number.setText(searchCompanyWrapper.getRating()+" Votes");
                        text_address.setText(searchCompanyWrapper.getAddress().replace("NULL","")+", "+searchCompanyWrapper.getCity_Name().replace("NULL","")+", "+searchCompanyWrapper.getState_Name().replace("null",""));
                  //      ratingbar.setRating(Float.parseFloat(searchCompanyWrapper.getRating().replace("NULL","")));
                        // text_website.setText(searchCompanyWrapper.getWebsite().replace("NULL",""));
                        String url=searchCompanyWrapper.getWebsite().replace("NULL","");
                        SpannableString content = new SpannableString(url);
                        content.setSpan(new UnderlineSpan(), 0, url.length(), 0);

                        text_website.setText(url);
                        text_discrp.setText(searchCompanyWrapper.getCompany_Desc().replace("NULL",""));
strings =new ArrayList<String>();
                        String[] substrMobileArray = searchCompanyWrapper.getPrimary_Phone().split(Pattern.quote(","));
                        text_call.setText(substrMobileArray[0].replace("NULL",""));
                        for (int i = 0; i < substrMobileArray.length; i++) {
                            strings.add(substrMobileArray[i]);
                        }


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };
        return response;
    }

    private void vendorlistRequest(JSONObject params) {


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, Const.URL_GetCompanyRatingDetail+id,
                params, newResponseRequesrtpostedvehiclelist(), eErrorListenerRequesrtpostedvehiclelist()) {
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

    private Response.ErrorListener eErrorListenerRequesrtpostedvehiclelist() {
        Response.ErrorListener response_error = new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("this is error", error.toString());


                AppController.spinnerStop();
            }
        };
        return response_error;
    }

    public Response.Listener<JSONObject> newResponseRequesrtpostedvehiclelist() {
        Response.Listener<JSONObject> response = new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                ArrayList<VendorCompanyWrapper>vendorCompanyWrapperlist = new ArrayList<VendorCompanyWrapper>();
                Log.e("ServiceListOnCityAn", response.toString());
                try {
                    String StatusValue = response.getString("StatusValue");
                    ArrayList<RatingWrapper> list=new ArrayList<RatingWrapper>();
                    if (StatusValue.equalsIgnoreCase("Success"))


                    {
                        JSONArray jsonArray = response.getJSONArray("Data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            RatingWrapper ratingList = new RatingWrapper();
                            JSONObject obj = jsonArray.getJSONObject(i);
                            ratingList.setCustomerName(obj.getString("Customer_Name"));
                            ratingList.setEmail(obj.getString("Email"));
                            ratingList.setPhone(obj.getString("Phone"));
                            ratingList.setReview(obj.getString("Review"));
                            ratingList.setRating(obj.getString("Rating"));
                            ratingList.setDatetext(obj.getString("RatingDT"));

                            list.add(ratingList);


                        }
                        RatingAdapter mListAdapter = new RatingAdapter(getActivity(), list,transaction);
                        listview_rate.setAdapter(mListAdapter);



                    }else {
                        RatingAdapter mListAdapter = new RatingAdapter(getActivity(), list,transaction);
                        listview_rate.setAdapter(mListAdapter);


                       // AppController.popErrorMsg("Error",response.getString("Desc"),getActivity());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                AppController.spinnerStop();

            }
        };
        return response;
    }


}
