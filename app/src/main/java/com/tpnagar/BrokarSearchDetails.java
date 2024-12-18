package com.tpnagar;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.content.ContextCompat;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.tpnagar.adapter.PhoneDetailsAdapter;
import com.tpnagar.adapter.SearchMainAdapter;
import com.tpnagar.fragment.EnquiryFragment;
import com.tpnagar.fragment.RatingFragment;
import com.tpnagar.fragment.SearchListFragment;
import com.tpnagar.fragment.VendorListFromDetailsFragment;
import com.tpnagar.wrapper.SearchCompanyWrapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class BrokarSearchDetails extends FragmentActivity implements AdapterView.OnItemSelectedListener, ActivityCompat.OnRequestPermissionsResultCallback{

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

    ListView listview;
    FragmentTransaction transaction;
    TextView trusted,rating_text;

    String  SearchText="";
    String  Location="";
    SearchCompanyWrapper searchCompanyWrapper;
    TextView vendor,contact_person,company_name,rate_number,text_call,text_address,text_mail,text_website,text_discrp,view_loc,enquiry,branches;
    RatingBar ratingbar;
    int id;
    
    Dialog dialog;
    ListView mCompleteListView;
    ArrayList<String> strings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.brokers_search_details);

        ImageView back=findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Click action

                onBackPressed();

               /* String ToSateName,ToSateId,FromSateName,FromSateId;
                String  SearchText="";
                String  Location="",ToCityId;
                String FromCity,ToCity,FromCityId,Service_Id;

                SearchText=getArguments().getString("SearchText");
                Location=getArguments().getString("Location");
                ToSateName=getArguments().getString("ToSateName");
                ToSateId=getArguments().getString("ToSateId");
                FromSateName=getArguments().getString("FromSateName");
                FromSateId=getArguments().getString("FromSateId");
                FromCity=getArguments().getString("FromCity");
                Service_Id=getArguments().getString("Service_Id");
                FromCityId=getArguments().getString("FromCityId");
                ToCity=getArguments().getString("ToCity");
                ToCityId=getArguments().getString("ToCityId");
                BrokarMainPaggerFragment fragmentBroker = new BrokarMainPaggerFragment();

                Bundle args = new Bundle();

                args.putString("Location",Location);
                args.putString("SearchText",SearchText);

                args.putString("FromCity",FromCity);
                args.putString("Service_Id",Service_Id);
                args.putString("ToCity",ToCity);
                args.putString("FromCityId",FromCityId);

                args.putString("ToCityId",ToCityId);
                args.putInt("spinnerId",0);

                args.putString("ToSateName",ToSateName);
                args.putString("ToSateId",ToSateId);
                args.putString("FromSateName",FromSateName);
                args.putString("FromSateId",FromSateId);

                fragmentBroker.setArguments(args);
               
                replaceFragmentTask(fragmentBroker);*/

            }
        });

        id=getIntent().getIntExtra("id",0);
        searchCompanyWrapper= DataManager.getInstance().getSearchCompanyBrokerWrapper().get(id);
        transaction = getSupportFragmentManager().beginTransaction();
        initXmlViews();

    }
    private void initXmlViews() {
        cd = new ConnectionDetector(BrokarSearchDetails.this);

        search=(ImageButton) findViewById(R.id.search);

        actv = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView1);
        actv.clearFocus();
        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);

        prefs = BrokarSearchDetails.this.getSharedPreferences("com.tpnagar", MODE_PRIVATE);

        contact_person= (TextView) findViewById(R.id.contact_person);

        company_name = (TextView) findViewById(R.id.company_name);
        rate_number = (TextView) findViewById(R.id.rate_number);
        text_call = (TextView) findViewById(R.id.text_call);
        text_address = (TextView) findViewById(R.id.text_address);
        ratingbar= (RatingBar) findViewById(R.id.ratingbar);
        text_mail= (TextView) findViewById(R.id.text_mail);
        text_website= (TextView) findViewById(R.id.text_website);
        text_discrp= (TextView) findViewById(R.id.text_discrp);
        view_loc= (TextView) findViewById(R.id.view_loc);
        enquiry= (TextView) findViewById(R.id.enquiry);
        branches= (TextView) findViewById(R.id.branches);
        vendor= (TextView) findViewById(R.id.vendor);


        vendor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                Bundle bundle=new Bundle();
                bundle.putString("Company_Id",searchCompanyWrapper.getCompany_Id());

                VendorListFromDetailsFragment vendorListFromDetailsFragment=new VendorListFromDetailsFragment();
                vendorListFromDetailsFragment.setArguments(bundle);
                transaction.replace(R.id.frame_container, vendorListFromDetailsFragment, "");
                transaction.addToBackStack(null);
                // transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                transaction.commit();

 
            }
        });



        contact_person.setText(searchCompanyWrapper.getContact_Person());

        company_name.setText(searchCompanyWrapper.getCompany_Name());
        rate_number.setText(searchCompanyWrapper.getRating()+" Votes");
        text_call.setText(searchCompanyWrapper.getPrimary_Phone().replace("NULL",""));
        text_address.setText(searchCompanyWrapper.getAddress().replace("NULL","")+", "+searchCompanyWrapper.getCity_Name()+", "+searchCompanyWrapper.getState_Name());
        ratingbar.setRating(Float.parseFloat(searchCompanyWrapper.getRating().replace("NULL","")));
        // text_website.setText(searchCompanyWrapper.getWebsite().replace("NULL",""));
        String url=searchCompanyWrapper.getWebsite().replace("NULL","");
        SpannableString content = new SpannableString(url);
        content.setSpan(new UnderlineSpan(), 0, url.length(), 0);

        text_website.setText(url);


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

        text_discrp.setText(searchCompanyWrapper.getCompany_Desc().replace("NULL",""));
        //text_mail.setText(searchCompanyWrapper.getEmail());
       /* text_website.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto",searchCompanyWrapper.getEmail(), null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "");
                startActivity(Intent.createChooser(emailIntent, "Send email..."));
            }
        });*/

        String s=searchCompanyWrapper.getPrimary_Phone();

        String[] value_split = s.split(Pattern.quote("|"));



        strings=new ArrayList<String>();

        String substrMobile=value_split[0];
        String substrPhone=value_split[1];
        String substrPR=value_split[2];
        String[] substrMobileArray = substrMobile.split(Pattern.quote("-"));
        String[] substrPhoneArray = substrPhone.split(Pattern.quote("-"));
        String[] substrPRArray = substrPR.split(Pattern.quote("-"));


        if(substrMobileArray.length>0){
            text_call.setText(substrMobileArray[0].replace("M:","").replace("NA",""));
        }else if(substrPhoneArray.length>0){
            text_call.setText(substrPhoneArray[0].replace("P:","").replace("NA",""));

        }else {
            text_call.setText(substrPRArray[0].replace("PR:","").replace("NA",""));

        }

        if(substrMobileArray.length>0){

            for (int i = 0; i < substrMobileArray.length; i++) {

                String s1= substrMobileArray[i].replace("M:","").replace("NA","");
                if(s1.length()>0){
                    strings.add(s1);
                }
            }
        }
        if(substrPhoneArray.length>0){

            for (int i = 0; i < substrPhoneArray.length; i++) {

                String s1=substrPhoneArray[i].replace("P:","").replace("NA","");
                if(s1.length()>0){
                    strings.add(s1);
                }

            }
        }
        if(substrPRArray.length>0){

            for (int i = 0; i < substrPRArray.length; i++) {

                String s1=substrPRArray[i].replace("PR:","").replace("NA","");
                if(s1.length()>0){
                    strings.add(s1);
                }


            }
        }
        TextView more=(TextView) findViewById(R.id.more);

        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new Dialog(BrokarSearchDetails.this,R.style.FullHeightDialog);
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
                PhoneDetailsAdapter mListAdapter = new PhoneDetailsAdapter(BrokarSearchDetails.this, strings);
                mCompleteListView.setAdapter(mListAdapter);
                mCompleteListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                        onCall(strings.get(i));

                    }
                });
            }
        });

        text_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onCall(text_call.getText().toString());


            }
        });


        ratingbar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {


                FragmentTransaction transaction = BrokarSearchDetails.this.getSupportFragmentManager().beginTransaction();
                Bundle bundle=new Bundle();
                bundle.putString("id",searchCompanyWrapper.getCompany_Id());

                RatingFragment searchDetailsFragment=new RatingFragment();
                searchDetailsFragment.setArguments(bundle);
                transaction.replace(R.id.frame_container, searchDetailsFragment, "");
                transaction.addToBackStack(null);
                // transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                transaction.commit();



                return false;
            }
        });

       /* ratingbar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {


                FragmentTransaction transaction = BrokarSearchDetails.this.getSupportFragmentManager().beginTransaction();
                Bundle bundle=new Bundle();
                bundle.putInt("id",id);

                RatingFragment searchDetailsFragment=new RatingFragment();
                searchDetailsFragment.setArguments(bundle);
                transaction.replace(R.id.frame_container, searchDetailsFragment, "");
                transaction.addToBackStack(null);
                // transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                transaction.commit();


            }
        });*/

        view_loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AppController.spinnerStart(BrokarSearchDetails.this);

                GeocodingLocation.getAddressFromLocation(searchCompanyWrapper.getPin_Code(),
                        BrokarSearchDetails.this, new GeocoderHandler());


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

                Intent intent=new Intent(BrokarSearchDetails.this, BranchListForDetailsActivity.class);
                intent.putExtra("id",searchCompanyWrapper.getCompany_Id());
                //intent.putExtra("idFORBranch",mList.get(position).get());
                startActivity(intent);

              /*  BranchListForDetailsActivity branchListForDetailsActivity
                BranchListForDetailsFragment branchListForDetailsFragment=new BranchListForDetailsFragment();
                Bundle bundle=new Bundle();
                bundle.putString("id", searchCompanyWrapper.getCompany_Id());
                branchListForDetailsFragment.setArguments(bundle);
                replaceFragmentTask(branchListForDetailsFragment);*/

            }
        });


        prefs = BrokarSearchDetails.this.getSharedPreferences("com.tpnagar", MODE_PRIVATE);


        BrokarSearchDetails.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);


       /* if(prefs.getString("IsActive","").equalsIgnoreCase("yes"))
        {
            ((com.tpnagar.designdemo.MainActivity) BrokarSearchDetails.this)
                    .getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#FFFFFF\">" +"Broker Search Details" + "</font>")));

        }else {
            ((com.tpnagar.designdemo.MainActivityWithoutLogin) BrokarSearchDetails.this)
                    .getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#FFFFFF\">" +"Broker Search Details" + "</font>")));

        }*/

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
                        ServiceListOnCityAndCategory(s.toString());
                        // ;

                    }else {
                        AppController.popErrorMsg("Alert!", "please check internet connection",BrokarSearchDetails.this);

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

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(BrokarSearchDetails.this, android.R.layout.simple_spinner_item, city);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

        int pos=AppController.POS;
        spinner.setSelection(pos);

        //RandomCityList();
    }



    void searchApi(){
        JSONObject obj = new JSONObject();

        try {


            //  bundle.putString("Location","");
            // bundle.putString("SearchText","");


            obj.put("SearchText",SearchText );
            obj.put("Location", Location);
            obj.put("FromState", "");
            obj.put("FromCity", "");
            obj.put("ToState", "");
            obj.put("ToCity", "");


            AppController.spinnerStart(BrokarSearchDetails.this);

            Log.e("this is obj:::", obj.toString());


            searchRequest(obj);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

   /* public void searchRequestPost(String url, final Map<String, String> json) {
       AppController.spinnerStart(BrokarSearchDetails.this);

        StringRequest post = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ArrayList<SearchCompanyWrapper > searchCompanyWrapperlist=new ArrayList<SearchCompanyWrapper>();
                Log.d("searchRequestPost", response.toString());
                try {
                    AppController.spinnerStop();


                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {

                        SearchCompanyWrapper searchCompanyWrapper=new SearchCompanyWrapper();

                        JSONObject obj = jsonArray.getJSONObject(i);
                        searchCompanyWrapper.set_id(obj.getString("_id"));
                        searchCompanyWrapper.setCompany_Id(obj.getString("Company_Id"));
                        searchCompanyWrapper.setService_Id(obj.getString("Service_Id"));
                        searchCompanyWrapper.setCompany_Name(obj.getString("Company_Name"));
                        searchCompanyWrapper.setCompany_Desc(obj.getString("Company_Desc"));
                        searchCompanyWrapper.setContact_Person(obj.getString("Contact_Person"));
                        searchCompanyWrapper.setAddress(obj.getString("Address"));
                        searchCompanyWrapper.setCountry_Id(obj.getString("Country_Id"));
                        searchCompanyWrapper.setState_Id(obj.getString("State_Id"));
                        searchCompanyWrapper.setState_Name(obj.getString("State_Name"));
                        searchCompanyWrapper.setCity_Id(obj.getString("City_Id"));
                        searchCompanyWrapper.setWebsite(obj.getString("Website"));
                        searchCompanyWrapper.setPin_Code(obj.getString("Pin_Code"));
                        searchCompanyWrapper.setPrimary_Phone(obj.getString("Primary_Phone"));
                       // searchCompanyWrapper.setRating(obj.getString("Rating"));
                        searchCompanyWrapper.setRating("2.5");
                        searchCompanyWrapper.setCompany_Type_Id(obj.getString("Company_Type_Id"));
                        searchCompanyWrapper.setCurrent_Status_Id(obj.getString("Current_Status_Id"));
                        searchCompanyWrapperlist.add(searchCompanyWrapper);

                    }
                    SearchMainAdapter mListAdapter = new SearchMainAdapter(BrokarSearchDetails.this, searchCompanyWrapperlist);
                    listview.setAdapter(mListAdapter);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("onErrorResponse ", error.toString());
              AppController.popErrorMsg("Error!",error.toString(),BrokarSearchDetails.this);

               AppController.spinnerStop();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = json;

                return map;
            }
        };
        post.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppController.getInstance().getRequestQueue().add(post);

    }
*/

    private void searchRequest(JSONObject params) {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, Const.URL_GetSearchResult,
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

        AppController.getInstance().addToRequestQueue(jsonObjReq, "URL_GetSearchResult");
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
                AppController.spinnerStop();

                ArrayList<SearchCompanyWrapper > searchCompanyWrapperlist=new ArrayList<SearchCompanyWrapper>();
                Log.e("Search data", response.toString());
                try {
                    String StatusValue = response.getString("StatusValue");

                    if (StatusValue.equalsIgnoreCase("Success"))


                    {
                        JSONArray jsonArray = response.getJSONArray("Data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            SearchCompanyWrapper searchCompanyWrapper=new SearchCompanyWrapper();

                            JSONObject obj = jsonArray.getJSONObject(i);
                            searchCompanyWrapper.set_id(obj.getString("_id"));
                            searchCompanyWrapper.setCompany_Id(obj.getString("Company_Id"));
                            searchCompanyWrapper.setService_Id(obj.getString("Service_Id"));
                            searchCompanyWrapper.setCompany_Name(obj.getString("Company_Name"));
                            searchCompanyWrapper.setCompany_Desc(obj.getString("Company_Desc"));
                            searchCompanyWrapper.setContact_Person(obj.getString("Contact_Person"));
                            searchCompanyWrapper.setAddress(obj.getString("Address"));
                            searchCompanyWrapper.setCountry_Id(obj.getString("Country_Id"));
                            searchCompanyWrapper.setState_Id(obj.getString("State_Id"));
                            searchCompanyWrapper.setState_Name(obj.getString("State_Name"));
                            searchCompanyWrapper.setCity_Id(obj.getString("City_Id"));
                            searchCompanyWrapper.setWebsite(obj.getString("Website"));
                            searchCompanyWrapper.setPin_Code(obj.getString("Pin_Code"));
                            searchCompanyWrapper.setPrimary_Phone(obj.getString("Primary_Phone"));
                            searchCompanyWrapper.setRating(obj.getString("Rating"));
                            // searchCompanyWrapper.setRating("2.5");
                            searchCompanyWrapper.setCompany_Type_Id(obj.getString("Company_Type_Id"));
                            searchCompanyWrapper.setCurrent_Status_Id(obj.getString("Current_Status_Id"));
                            searchCompanyWrapperlist.add(searchCompanyWrapper);

                        }

                        DataManager.getInstance().setSearchCompanyBrokerWrapper(searchCompanyWrapperlist);
                        SearchMainAdapter mListAdapter = new SearchMainAdapter(BrokarSearchDetails.this, searchCompanyWrapperlist ,transaction);
                        listview.setAdapter(mListAdapter);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        };
        return response;
    }
    public void replaceFragmentTask(Fragment fragment) {

      /*  transaction.setCustomAnimations(
                R.anim.slide_in_from_right, R.anim.slide_out_to_left,
                R.anim.slide_in_from_left, R.anim.slide_out_to_right);*/

        Bundle  bundle=new Bundle();
        bundle.putString("id",searchCompanyWrapper.getCompany_Id());
        fragment.setArguments(bundle);
        transaction.replace(R.id.frame_container, fragment, "");
        transaction.addToBackStack(null);
        // transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.commit();
    }

    void ServiceListOnCityAndCategory(String editText){

        JSONObject obj = new JSONObject();
        try {
            obj.put("Password", "prem$123");
            //  AppController.spinnerStart(BrokarSearchDetails.this);

            Log.e("this is obj:::",obj.toString());


            ServiceListOnCityAndCategoryRequest(obj,editText);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void ServiceListOnCityAndCategoryRequest(JSONObject params,String editText) {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, Const.URL_SearchData+editText,
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
                        for (int i = 0; i < 20; i++) {

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
                        (BrokarSearchDetails.this,android.R.layout.simple_list_item_1,services);
                actv.setAdapter(adapter);

          /*     actv.clearFocus();
                BrokarSearchDetails.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                if (actv != null) {
                    InputMethodManager imm = (InputMethodManager) BrokarSearchDetails.this.getSystemService(Context.INPUT_METHOD_SERVICE);
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
            AppController.spinnerStart(BrokarSearchDetails.this);

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
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(BrokarSearchDetails.this, android.R.layout.simple_spinner_item, city);

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

                        Intent intent = new Intent(BrokarSearchDetails.this, MapsActivity.class);
                        intent.putExtra("LatStr", separated[0]);
                        intent.putExtra("LongStr", separated[1]);
                        intent.putExtra("Location", searchCompanyWrapper.getAddress());


                        startActivity(intent);

                    }else {
                        Toast.makeText(BrokarSearchDetails.this, "Could not detect your location when lat long not detected", Toast.LENGTH_SHORT).show();

                    }

                } else {

                    Toast.makeText(BrokarSearchDetails.this, "Could not detect your location when lat long not detected", Toast.LENGTH_SHORT).show();

                }


            } else {
                Toast.makeText(BrokarSearchDetails.this, "Could not detect your location when lat long not detected", Toast.LENGTH_SHORT).show();

            }


            Log.e("locationAddress123", locationAddress);

            // latLongTV.setText(locationAddress);


        }
    }

    public void onCall(String number) {
        int permissionCheck = ContextCompat.checkSelfPermission(BrokarSearchDetails.this, android.Manifest.permission.CALL_PHONE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(BrokarSearchDetails.this, new String[]{Manifest.permission.CALL_PHONE}, 1);
        } else {
            startActivity(new Intent(Intent.ACTION_CALL).setData(Uri.parse("tel:" +number)));
        }
    }


}

