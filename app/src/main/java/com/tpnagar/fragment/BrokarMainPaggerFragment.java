package com.tpnagar.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tpnagar.AppController;
import com.tpnagar.BaseContainerFragment;
import com.tpnagar.ConnectionDetector;
import com.tpnagar.R;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by admin on 03-03-2016.
 */
public class BrokarMainPaggerFragment extends BaseContainerFragment {
    private int splashTimeOut = 3000;
    private LinearLayout splashLayout;
    SharedPreferences prefs = null;
    ConnectionDetector cd;
    static String  SearchText="",FromCityId="",ToCityId="",Service_Id="";
    static String  Location="";
    static String FromCity,ToCity;
    static String ToSateName,ToSateId,FromSateName,FromSateId;
    static  int goodsCount=0;
    static int brokersCount=0;
    static  int vehiclesCount=0;
    DesignDemoPagerAdapter adapter;
    ViewPager viewPager;
    TabLayout tabLayout;
    TextView tvtab1, tvtab2,  tvtab3;
    View headerView;
    View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.pagerfragment, container, false);
        ((com.tpnagar.designdemo.MainActivity) getActivity())
                .getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#FFFFFF\">" +"Broker Search" + "</font>")));

        initXmlViews(rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();

        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {

                        replaceFragmentTask(new SearchBrokerFragment());
                        return true;
                    }
                }
                return false;
            }
        });
    }
    public void replaceFragmentTask(Fragment fragment) {
        FragmentTransaction transaction;
        transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment, "");
        transaction.addToBackStack(null);
        // transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.commit();
    }
    private void initXmlViews(View view) {
        cd = new ConnectionDetector(getActivity());
        prefs = getActivity().getSharedPreferences("com.tpnagar", MODE_PRIVATE);

        if (getArguments().getString("SearchText")==null){

            SearchText=AppController.SearchText;
            Location="";
            FromCityId=AppController.FromCityId;
            ToCityId=AppController.ToCityId;
            Service_Id=AppController.Service_Id;
            ToSateName=AppController.ToSateName;
            ToSateId=AppController.ToSateId;
            FromSateName=AppController.FromSateName;
            FromSateId=AppController.FromSateId;


        }else {
            SearchText=getArguments().getString("SearchText");
            Location=getArguments().getString("Location");
            FromCityId=getArguments().getString("FromCityId");
            ToCityId=getArguments().getString("ToCityId");
            Service_Id=getArguments().getString("Service_Id");
            ToSateName=getArguments().getString("ToSateName");
            ToSateId=getArguments().getString("ToSateId");
            FromSateName=getArguments().getString("FromSateName");
            FromSateId=getArguments().getString("FromSateId");

        }






        if(getArguments().getString("FromCity")!=null){
            FromCity=getArguments().getString("FromCity");
        }

        if(getArguments().getString("ToCity")!=null){
            ToCity=getArguments().getString("ToCity");
        }


         adapter = new DesignDemoPagerAdapter(getActivity().getSupportFragmentManager());
         viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        viewPager.setAdapter(adapter);
         tabLayout = (TabLayout) view.findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);

        if(AppController.isPageone){
            viewPager.setCurrentItem(0);
        }
else {
            viewPager.setCurrentItem(1);
        }

       // txtfrom_state=(EditText) view.findViewById(txtfrom_state);


       // search=(ImageButton) view.findViewById(R.id.search);
        /*search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Activity context=getActivity();

            }
        });*/

        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
         headerView = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.custom_tab, null, false);

        LinearLayout linearLayoutOne = (LinearLayout) headerView.findViewById(R.id.ll);
        LinearLayout linearLayout2 = (LinearLayout) headerView.findViewById(R.id.ll2);
        LinearLayout linearLayout3 = (LinearLayout) headerView.findViewById(R.id.ll3);

         tvtab1=headerView.findViewById(R.id.tvtab1);
         tvtab2=headerView.findViewById(R.id.tvtab2);
         tvtab3=headerView.findViewById(R.id.tvtab3);

        tvtab1.setText(""+goodsCount);
        tvtab2.setText(""+brokersCount);
        tvtab3.setText(""+vehiclesCount);

        tabLayout.getTabAt(0).setCustomView(linearLayoutOne);
        tabLayout.getTabAt(1).setCustomView(linearLayout2);
        tabLayout.getTabAt(2).setCustomView(linearLayout3);

    }


     class DesignDemoPagerAdapter extends FragmentStatePagerAdapter {

        public DesignDemoPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return DesignDemoFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            return 3;
        }



        @Override
        public CharSequence getPageTitle(int position) {



            String title;
            if(position==0){
                title ="GOODS AVAILABLE "+"("+goodsCount+")" ;
            }else if(position==1){
                title ="BROKER "+"("+brokersCount+")" ;
            }

            else {
                title ="VEHICLES AVAILABLE "+"("+vehiclesCount+")" ;
            }

            /*Drawable myDrawable   = getActivity().getResources().getDrawable(R.drawable.dashboard);
            SpannableStringBuilder sb = new SpannableStringBuilder("   " + title); // space added before text for convenience
            try {
                myDrawable.setBounds(5, 5, myDrawable.getIntrinsicWidth(), myDrawable.getIntrinsicHeight());
                ImageSpan span = new ImageSpan(myDrawable, DynamicDrawableSpan.ALIGN_BASELINE);
                sb.setSpan(span, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            } catch (Exception e) {
                // TODO: handle exception
            }*/

            return title;

        }
    }

    public void updateTitleData(int items,int pos,String name) {



       if (pos==0){
            tvtab1.setText(""+items);
        }else if (pos==1){
            tvtab2.setText(""+items);
        }else {
            tvtab3.setText(""+items);
        }

        TabLayout.Tab tab = tabLayout.getTabAt(pos);
        if (tab != null) {
            tab.setText(name+" "+"("+items+")");
        }
    }
    public static class DesignDemoFragment extends Fragment {
        private static final String TAB_POSITION = "tab_position";

        public DesignDemoFragment() {

        }

        public static Fragment newInstance(int tabPosition) {


            if(tabPosition==0) {
                GoodsAvailbleListFragment fragmentGood = new GoodsAvailbleListFragment();
                Bundle args = new Bundle();
                args.putInt(TAB_POSITION, tabPosition);
                args.putString("Location",Location);
                args.putString("SearchText",SearchText);

                args.putString("FromCity",FromCity);

                args.putString("ToCity",ToCity);
                args.putString("FromCityId",FromCityId);
                args.putString("Service_Id",Service_Id);

                args.putString("ToCityId",ToCityId);
                args.putInt("spinnerId",0);

                args.putString("ToSateName",ToSateName);
                args.putString("ToSateId",ToSateId);
                args.putString("FromSateName",FromSateName);
                args.putString("FromSateId",FromSateId);





                fragmentGood.setArguments(args);
                return fragmentGood;
            }else if(tabPosition==1){


                BrokerListFragment fragmentBroker = new BrokerListFragment();

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


                args.putInt(TAB_POSITION, tabPosition);
                fragmentBroker.setArguments(args);
                return fragmentBroker;

            }
            else {
                VehicleAvailbleListFragment fragmentVehicle = new VehicleAvailbleListFragment();

                Bundle args = new Bundle();
                args.putInt(TAB_POSITION, tabPosition);
                args.putString("Location",Location);
                args.putString("SearchText",SearchText);
                args.putString("FromCity",FromCity);
                args.putString("ToCity",ToCity);
                args.putString("FromCityId",FromCityId);
                args.putString("Service_Id",Service_Id);
                args.putString("ToCityId",ToCityId);
                args.putInt("spinnerId",0);

                args.putString("ToSateName",ToSateName);
                args.putString("ToSateId",ToSateId);
                args.putString("FromSateName",FromSateName);
                args.putString("FromSateId",FromSateId);

                fragmentVehicle.setArguments(args);
                return fragmentVehicle;
            }
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            Bundle args = getArguments();
            int tabPosition = args.getInt(TAB_POSITION);

            ArrayList<String> items = new ArrayList<String>();
            for (int i = 0; i < 50; i++) {
                items.add("Tab #" + tabPosition + " item #" + i);
            }

            View v =  inflater.inflate(R.layout.fragment_list_view, container, false);
            RecyclerView recyclerView = (RecyclerView)v.findViewById(R.id.recyclerview);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setAdapter(new DesignDemoRecyclerAdapter(items));

            return v;
        }
    }

    @Override
    public void onResume() {
        super.onResume();



    }

}
