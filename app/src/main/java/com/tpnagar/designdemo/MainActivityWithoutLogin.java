package com.tpnagar.designdemo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import com.tpnagar.LoginActivity;
import com.tpnagar.R;
import com.tpnagar.SignupActivity;
import com.tpnagar.fragment.LoginDetailsFragment;
import com.tpnagar.fragment.MainFragment;
import com.tpnagar.fragment.SearchListFragment;
import com.tpnagar.fragment.SearchParcelServiceFragment;
import com.tpnagar.wrapper.CompanyTypeWrapper;
import com.tpnagar.wrapper.DBTabsWrapper;
import com.tpnagar.wrapper.GetComapnyServiceWrapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.tpnagar.R.id.topbg;


public class MainActivityWithoutLogin extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private DrawerLayout mDrawerLayout;
   // TextView txtUserName,txtemail,Phone;
    SharedPreferences prefs = null;
    ConnectionDetector cd;

    private boolean rootViewId;
    ImageButton search;
    String[] services,cityStringArray;
    CharSequence[]  serviceItem,cityItem;
    ArrayList<String> city = new ArrayList<String>();
    ArrayList<String> cityid= new ArrayList<String>();
    ArrayList<String> servicesaStrings = new ArrayList<String>();
   String cityidForLoc="127";
    ImageView dropdown,dropdown_city;
    AutoCompleteTextView actv,spinner_autocompleCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_witoutlogin);
       
        cd=new ConnectionDetector(this);
        RelativeLayout   topbg11=(RelativeLayout) findViewById(topbg) ;

        topbg11.setVisibility(View.VISIBLE);

        dropdown=(ImageView) findViewById(R.id.dropdown);
        dropdown_city=(ImageView) findViewById(R.id.dropdown_city);
        search=(ImageButton) findViewById(R.id.search);

        actv = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView1);
        actv.clearFocus();
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        spinner_autocompleCity = (AutoCompleteTextView) findViewById(R.id.spinner_autocompleCity);
        spinner_autocompleCity.clearFocus();


        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.frame_container);
                Log.e("Prem PP", "find the current fragment" +currentFragment.getClass().getSimpleName());
if(currentFragment.getClass().getSimpleName().equalsIgnoreCase("CompanydetailsbypostFragment") ){
    onBackPressed();
    onBackPressed();
}

                if(actv.getText().toString()!=null){


                    if(actv.getText().toString().length()>3){

                        String  SearchText="";
                        String  Location="";



                        if(actv.getText()==null){
                            SearchText="";
                        }else {
                            SearchText=actv.getText().toString();

                        }
                        Location=spinner_autocompleCity.getText().toString();
                        for (int i = 0; i <cityStringArray.length ; i++) {

                            if(cityStringArray[i].equalsIgnoreCase(Location))
                            cityidForLoc=cityid.get(i);

                        }



                        Bundle bundle=new Bundle();
                        bundle.putString("Location",Location);
                        bundle.putString("SearchText",SearchText);
                        bundle.putInt("spinnerId",0);



                        SearchListFragment searchListFragment=new SearchListFragment();
                        searchListFragment.setArguments(bundle);


                        replaceFragmentTask(searchListFragment);

                    }else{
                        AppController.showToast(MainActivityWithoutLogin.this, "please enter service completely");

                    }
                }


            }
        });

        RandomCityList();

        JSONObject obja = new JSONObject();

        try {


            obja.put("serviceRequest", "");


            AppController.spinnerStart(MainActivityWithoutLogin.this);

            Log.e("this is obj:::", obja.toString());


            serviceRequest(obja);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        dropdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showListService();
            }
        });

        dropdown_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showListCity();
            }
        });

        spinner_autocompleCity.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(spinner_autocompleCity.getText().length()>0){
                    spinner_autocompleCity.setText("");
                }
                return false;
            }
        });

       /* spinner_autocompleCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(spinner_autocompleCity.getText().length()>0){
                    spinner_autocompleCity.setText("");
                }

            }
        });*/

        actv.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

         /*       if(s.length()>2)
                {
                    if(cd.isConnectingToInternet())

                    {


                        if(cityid!=null&&spinner_autocompleCity!=null)
                        {

                            if(cityid.size()>0) {

                            String Location = spinner_autocompleCity.getText().toString();
                            for (int i = 0; i < cityStringArray.length; i++) {

                                if (cityStringArray[i].equalsIgnoreCase(Location))
                                    cityidForLoc = cityid.get(i);

                            }
                        }
                        }


                        ServiceListOnCityAndCategory(s.toString());
                        // ;

                    }else {
                        AppController.popErrorMsg("Alert!", "please check internet connection",MainActivityWithoutLogin.this);

                    }
                }*/

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {


                if(s.length()>2)
                {
                    if(cd.isConnectingToInternet())

                    {


                        if(cityid!=null&&spinner_autocompleCity!=null)
                        {

                            if(cityid.size()>0) {

                                String Location = spinner_autocompleCity.getText().toString();
                                for (int i = 0; i < cityStringArray.length; i++) {

                                    if (cityStringArray[i].equalsIgnoreCase(Location))
                                        cityidForLoc = cityid.get(i);

                                }
                            }
                        }
                        boolean isHas=false;

                        for (int i = 0; i <servicesaStrings.size() ; i++) {

                            if(servicesaStrings.get(i).equalsIgnoreCase(actv.getText().toString())){
                                isHas=true;
                            }

                        }
                        if(!isHas){
                            ServiceListOnCityAndCategory(s.toString());
                        }else {

                        }


                        // ;

                    }else {
                        AppController.popErrorMsg("Alert!", "please check internet connection",MainActivityWithoutLogin.this);

                    }
                }



               if(s.toString().equalsIgnoreCase("parcel service")){
                    RelativeLayout  topbg=(RelativeLayout) findViewById(R.id.topbg) ;

                    topbg.setVisibility(View.GONE);

                    String  SearchText="Parcel Service";
                    String  Location="";




                    Bundle bundle=new Bundle();
                    bundle.putString("Location",Location);
                    bundle.putString("SearchText",SearchText);
                    bundle.putInt("spinnerId",0);

                    SearchParcelServiceFragment searchListFragment=new SearchParcelServiceFragment();
                    searchListFragment.setArguments(bundle);

                   actv.setThreshold(2);
                   actv.setText("");
                   actv.dismissDropDown();
                    replaceFragmentTask(searchListFragment);
                }

            }
        });



        // txtUserName=(TextView) findViewById(R.id.txtUserName) ;
       //   txtemail=(TextView) findViewById(R.id.txtemail) ;
      //  Phone=(TextView) findViewById(R.id.Phone) ;
        prefs = getSharedPreferences("com.tpnagar", MODE_PRIVATE);

      //  txtUserName.setText("Guest User");
      //  txtemail.setText("Email");
      //  Phone.setText("");


/*        if(prefs.getString("Login_Id","").length()>0){
            JSONObject obj = new JSONObject();

            try {


                obj.put("LoginId", prefs.getString("Login_Id",""));

                obj.put("OTP", "");
                AppController.spinnerStart(this);

                Log.e("this is obj:::", obj.toString());
               serviceGetCompanyDetailsRequestCompanyType(obj);
                serviceGetDBTabsRequestCompanyType(obj);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else {

        }*/
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.menu_icon);
      actionBar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#FFFFFF\">" +"Tpnagar" + "</font>")));

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                mDrawerLayout.closeDrawers();


                BaseContainerFragment fragment = null;
                switch (menuItem.getTitle().toString()) {
                    case "Dashboard":
                        fragment = new MainFragment();

                        break;

                    case "Login":
                        Intent intent = new Intent(MainActivityWithoutLogin.this, LoginActivity.class);
                        startActivity(intent);
                        finish();

                        break;

                    case "Register":
                        Intent intent1 = new Intent(MainActivityWithoutLogin.this, SignupActivity.class);
                        startActivity(intent1);
                        finish();

                        break;
                    case "Services":
                        fragment = new LoginDetailsFragment();

                        break;

                    case "Keyword":
                        fragment = new LoginDetailsFragment();

                        break;

                    case "Branch":
                        fragment = new LoginDetailsFragment();

                        break;
                    case "Sms Details":
                        fragment = new LoginDetailsFragment();

                        break;
                    case "Own Vehicle":
                        fragment = new LoginDetailsFragment();

                        break;
                    case "Logout":
                        fragment = new LoginDetailsFragment();

                        break;



                    default:
                        break;
                }
                if (fragment != null) {
                    FragmentTransaction fragmentManager = getSupportFragmentManager().beginTransaction();

                    fragmentManager.replace(R.id.frame_container, fragment,"item");
                    fragmentManager.addToBackStack("item");
                    fragmentManager.commit();
                    // update selected item and title, then close the drawer

                    setTitle(menuItem.getTitle());

                    getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#FFFFFF\">" +menuItem.getTitle() + "</font>")));

                    mDrawerLayout.closeDrawers();

                } else {
                    // error in creating fragment
                    Log.e("MainActivity", "Error in creating fragment");
                }



              //  Toast.makeText(MainActivity.this, menuItem.getTitle(), Toast.LENGTH_LONG).show();
                return true;
            }
        });

        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(findViewById(R.id.coordinator), "I'm a Snackbar", Snackbar.LENGTH_LONG).setAction("Action", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(MainActivityWithoutLogin.this, "Snackbar Action", Toast.LENGTH_LONG).show();
                    }
                }).show();
            }
        });

     //   DesignDemoPagerAdapter adapter = new DesignDemoPagerAdapter(getSupportFragmentManager());
      //  ViewPager viewPager = (ViewPager)findViewById(R.id.viewpager);
      //  viewPager.setAdapter(adapter);
      //  TabLayout tabLayout = (TabLayout)findViewById(R.id.tablayout);
     //   tabLayout.setupWithViewPager(viewPager);

        FragmentTransaction fragmentManager = getSupportFragmentManager().beginTransaction();

        fragmentManager.replace(R.id.frame_container, new MainFragment(),"item");
        fragmentManager.addToBackStack("item");
        fragmentManager.commit();

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;

        }

        return super.onOptionsItemSelected(item);
    }
    private void serviceGetCompanyDetailsRequestCompanyType(JSONObject params) {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, Const.URL_GetCompanyDetails+prefs.getString("Login_Id","1"),
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
                Log.e("CompanyType", response.toString());
                ArrayList<String> arrayList1 = new ArrayList<String>();
                try {
                    String StatusValue = response.getString("StatusValue");

                    if (StatusValue.equalsIgnoreCase("Success"))


                    {
                        JSONObject jsonbObject = response.getJSONObject("Data");

                        prefs.edit().putString("Id",jsonbObject.getString("Id")).commit();
                        prefs.edit().putString("Company_Name",jsonbObject.getString("Company_Name")).commit();
                        prefs.edit().putString("Company_Desc",jsonbObject.getString("Company_Desc")).commit();
                        prefs.edit().putString("Owner_Name",jsonbObject.getString("Owner_Name")).commit();
                        prefs.edit().putString("Contact_Person",jsonbObject.getString("Contact_Person")).commit();
                        prefs.edit().putString("Address",jsonbObject.getString("Address")).commit();
                        prefs.edit().putString("Fax_No",jsonbObject.getString("Fax_No")).commit();
                        prefs.edit().putString("Country_Id",jsonbObject.getString("Country_Id")).commit();
                        prefs.edit().putString("State_Id",jsonbObject.getString("State_Id")).commit();
                        prefs.edit().putString("City_Id",jsonbObject.getString("City_Id")).commit();
                        prefs.edit().putString("Area_Id",jsonbObject.getString("Area_Id")).commit();
                        prefs.edit().putString("Website",jsonbObject.getString("Website")).commit();
                        prefs.edit().putString("Pin_Code",jsonbObject.getString("Pin_Code")).commit();
                        prefs.edit().putString("Logo",jsonbObject.getString("Logo")).commit();
                        prefs.edit().putString("Company_Category_Id",jsonbObject.getString("Company_Category_Id")).commit();
                        prefs.edit().putString("Company_Type_Id",jsonbObject.getString("Company_Type_Id")).commit();
                        prefs.edit().putString("Current_Status_Id",jsonbObject.getString("Current_Status_Id")).commit();
                        prefs.edit().putString("Mobile_No",jsonbObject.getString("Mobile_No")).commit();
                        prefs.edit().putString("Storage_Id",jsonbObject.getString("Storage_Id")).commit();
                        prefs.edit().putString("Phone_No",jsonbObject.getString("Phone_No")).commit();
                        prefs.edit().putString("Email",jsonbObject.getString("Email")).commit();

                     //   txtUserName.setText(prefs.getString("Company_Name",""));
                   //     txtemail.setText(prefs.getString("Email",""));
                    //    Phone.setText(prefs.getString("Mobile_No",""));


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                AppController.spinnerStop();

            }
        };
        return response;
    }

    private void serviceGetDBTabsRequestCompanyType(JSONObject params) {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, Const.URL_GetDBTabs+prefs.getString("Login_Id","1"),
                params, newResponseRequesrtGetDBTabs(), eErrorListenerRequesrtGetDBTabs()) {
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

    private Response.ErrorListener eErrorListenerRequesrtGetDBTabs() {
        Response.ErrorListener response_error = new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("this is error", error.toString());


                AppController.spinnerStop();
            }
        };
        return response_error;
    }

    public Response.Listener<JSONObject> newResponseRequesrtGetDBTabs() {
        Response.Listener<JSONObject> response = new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                ArrayList<DBTabsWrapper> dBTabsWrapperlist = new ArrayList<DBTabsWrapper>();
                Log.e("CompanyType PREM", response.toString());
                ArrayList<String> arrayList1 = new ArrayList<String>();
                try {
                    String StatusValue = response.getString("StatusValue");

                    if (StatusValue.equalsIgnoreCase("Success"))
                    {
                        JSONArray jsonbArray = response.getJSONArray("Data");

                        for (int i = 0; i < jsonbArray.length(); i++) {
                            DBTabsWrapper dBTabsWrapper = new DBTabsWrapper();
                            JSONObject obj = jsonbArray.getJSONObject(i);
                            dBTabsWrapper.setID(obj.getString("ID"));
                             dBTabsWrapper.setActivityId(obj.getString("ActivityId"));
                            dBTabsWrapper.setTab_Desc(obj.getString("Tab_Desc"));
                            dBTabsWrapper.setTarget_URL(obj.getString("Target_URL"));

                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                AppController.spinnerStop();

            }
        };
        return response;
    }


    private void serviceRequest(JSONObject params) {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, Const.URL_MainFORAll+"service/ContractorServices/0",
                params, newResponseRequesrtservice(), eErrorListenerRequesrtservice()) {
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

    private Response.ErrorListener eErrorListenerRequesrtservice() {
        Response.ErrorListener response_error = new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("this is error", error.toString());


                AppController.spinnerStop();
            }
        };
        return response_error;
    }

    public Response.Listener<JSONObject> newResponseRequesrtservice() {
        Response.Listener<JSONObject> response = new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                ArrayList<GetComapnyServiceWrapper> getComapnyServiceWrapperlist = new ArrayList<GetComapnyServiceWrapper>();
                Log.e("ServiceListOnCityAn", response.toString());
                ArrayList<String> arrayList1 = new ArrayList<String>();
                try {
                    String StatusValue = response.getString("StatusValue");

                    if (StatusValue.equalsIgnoreCase("Success"))

                    // arrayListBoolean=new ArrayList<Boolean>();


                    {
                        JSONArray jsonArray = response.getJSONArray("Data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            GetComapnyServiceWrapper getComapnyServiceWrapper = new GetComapnyServiceWrapper();
                            JSONObject obj = jsonArray.getJSONObject(i);
                            getComapnyServiceWrapper.setService_Name(obj.getString("Service_Name"));
                            getComapnyServiceWrapper.setId(obj.getString("Id"));
                            getComapnyServiceWrapper.setHas_Destination(""+obj.getBoolean("Has_Destination"));
                            getComapnyServiceWrapper.setService_Type(obj.getString("Service_Type"));

                            // arrayList1.add(obj.getString("City_Name"));
                            // arrayListBoolean.add(false);
                            arrayList1.add(obj.getString("Service_Name"));
                            // arrayList1.add(obj.getString("Service_Name"));
                            servicesaStrings.add(obj.getString("Service_Name"));
                            // arrayList1.add(obj.getString("Service_Name"));
                            getComapnyServiceWrapperlist.add(getComapnyServiceWrapper);

                        }
                        if (servicesaStrings != null ) {
                            services = servicesaStrings.toArray(new String[0]);
                        }

                        serviceItem =arrayList1.toArray(new CharSequence[arrayList1.size()]);

                      DataManager.getInstance().setServiceItem(serviceItem);

                       /* ArrayAdapter<String> adapter = new ArrayAdapter<String>
                                (getActivity(),android.R.layout.simple_list_item_1,services);
                        actv.setAdapter(adapter);*/
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                AppController.spinnerStop();

            }
        };
        return response;
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void showListService() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivityWithoutLogin.this);

        builder.setTitle("Select Service");

        builder.setItems(serviceItem,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {



                        if(serviceItem[item].toString().equalsIgnoreCase("parcel service")){
                            RelativeLayout  topbg=(RelativeLayout) findViewById(R.id.topbg) ;

                            topbg.setVisibility(View.GONE);

                            String  SearchText="Parcel Service";
                            String  Location="";
                            Bundle bundle=new Bundle();
                            bundle.putString("Location",Location);
                            bundle.putString("SearchText",SearchText);
                            bundle.putInt("spinnerId",0);

                            SearchParcelServiceFragment searchListFragment=new SearchParcelServiceFragment();
                            searchListFragment.setArguments(bundle);

                            actv.setThreshold(2);
                            actv.setText("");
                            actv.dismissDropDown();
                            replaceFragmentTask(searchListFragment);
                        }else {
                            actv.setText(serviceItem[item]);
                        }


                      /*  stringServiceId = DataManager.getInstance()
                                .getGetComapnyServiceWrapperlist().get(item)
                                .getId();*/




                    }
                });
        builder.create();
        builder.show();

    }

    private void showListCity() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivityWithoutLogin.this);

        builder.setTitle("Select City");

        builder.setItems(cityItem,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        spinner_autocompleCity.setText(cityItem[item]);
                        cityidForLoc = cityid.get(item);




                    }
                });
        builder.create();
        builder.show();

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

    void ServiceListOnCityAndCategory(String editText){

        JSONObject obj = new JSONObject();
        try {
            obj.put("SearchText", editText);
            obj.put("Location", cityidForLoc);
             AppController.spinnerStart(MainActivityWithoutLogin.this);

            Log.e("this is obj:::",obj.toString());


            ServiceListOnCityAndCategoryRequest(obj,editText);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void ServiceListOnCityAndCategoryRequest(JSONObject params,String editText) {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, Const.URL_SearchData,
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

                Log.e("ServiceListOnCity Prem", response.toString());

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
                    services = servicesaStrings.toArray(new String[servicesaStrings.size()]);


                }

                AppController.spinnerStop();

                AppController.spinnerStop();
                ArrayAdapter<String> adapter = new ArrayAdapter<String>
                        (MainActivityWithoutLogin.this,R.layout.custom_text,services);
                actv.setAdapter(adapter);

                actv.showDropDown();
             /*   actv.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        actv.showDropDown();
                    }
                },100);
*/
              //  actv.setText(actv.getText().toString());
               // actv.setSelection(actv.getText().length());

           /* // actv.clearFocus();
               getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                if (actv != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(actv.getWindowToken(), 0);
                }
*/

            }
        };
        return response;
    }
    void RandomCityList(){

        JSONObject obj1 = new JSONObject();

        try {

            obj1.put("Password", "prem$123");
            AppController.spinnerStart(this);

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
                            cityid.add(obj.getString("Id"));

                            if(obj.getString("City_Name").equalsIgnoreCase("Delhi")){
                                cityidForLoc=obj.getString("Id");
                                spinner_autocompleCity.setText(obj.getString("City_Name"));
                            }
                            DataManager.getInstance().setCity(city);

                           DataManager.getInstance().setCityIds(cityid);
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // Creating adapter for spinner
       /*         ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(MainActivityWithoutLogin.this, android.R.layout.simple_spinner_item, city);

                // Drop down layout style - list view with radio button
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                // attaching data adapter to spinner
                spinner.setAdapter(dataAdapter);*/

                cityItem=city.toArray(new CharSequence[city.size()]);
                cityStringArray =city.toArray(new String[city.size()]);

                ArrayAdapter<String> adapter = new ArrayAdapter<String>
                        (MainActivityWithoutLogin.this,R.layout.custom_text,cityStringArray);
                spinner_autocompleCity.setAdapter(adapter);
                AppController.spinnerStop();

            }
        };
        return response;
    }
    private void replaceFragmentTask(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
      /*  transaction.setCustomAnimations(
                R.anim.slide_in_from_right, R.anim.slide_out_to_left,
                R.anim.slide_in_from_left, R.anim.slide_out_to_right);*/
        transaction.replace(R.id.frame_container, fragment, "");
        transaction.addToBackStack(null);
        // transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.commit();
    }
}
