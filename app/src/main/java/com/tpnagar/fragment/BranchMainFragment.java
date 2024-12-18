package com.tpnagar.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.text.Html;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tpnagar.BaseContainerFragment;
import com.tpnagar.ConnectionDetector;
import com.tpnagar.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by admin on 03-03-2016.
 */
public class BranchMainFragment extends BaseContainerFragment {
    private int splashTimeOut = 3000;
    private LinearLayout splashLayout;
    SharedPreferences prefs = null;
    ConnectionDetector cd;
    //ImageButton search;
   TextView addbranch,viewEidtBranch,addAdminBranch;
    View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.branchmain, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initXmlViews(view);


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
                        FragmentTransaction fragmentManager = getActivity().getSupportFragmentManager().beginTransaction();

                        fragmentManager.replace(R.id.frame_container, new BusinessDetailsMainFragment(),"item");
                        fragmentManager.addToBackStack("item");
                        fragmentManager.commit();

                  /*      AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setCancelable(false);
                        builder.setTitle("Alert!");
                        builder.setMessage("Are you want to exit from Tpnagar?")
                                .setCancelable(true)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        getActivity().finish();
                                    }
                                });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                dialog.cancel();
                            }
                        });

                        AlertDialog alert = builder.create();
                        alert.show();*/
                        return true;
                    }
                }
                return false;
            }
        });
    }
    private void initXmlViews(View view) {
        cd = new ConnectionDetector(getActivity());
        ((com.tpnagar.designdemo.MainActivity) getActivity())
                .getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#FFFFFF\">" +"Branch" + "</font>")));


        addbranch=(TextView) view.findViewById(R.id.addbranch);
        viewEidtBranch=(TextView) view.findViewById(R.id.viewEidtBranch);
        addAdminBranch=(TextView) view.findViewById(R.id.addAdminBranch);
        addbranch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                replaceFragmentTask(new AddBranchFragment());
            }
        });
        viewEidtBranch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               replaceFragmentTask(new BranchListFragment());

            }
        });
        addAdminBranch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                replaceFragmentTask(new BranchListForPermisstionFragment());

            }
        });


        prefs = getActivity().getSharedPreferences("com.tpnagar", MODE_PRIVATE);
        if (prefs.getString("CanEditBranch","").equalsIgnoreCase("true")){
            addbranch.setVisibility(View.VISIBLE);
        }else {
            addbranch.setVisibility(View.GONE);
        }


        if (prefs.getString("Role_Id","").equalsIgnoreCase("3")){
            if (prefs.getString("CanEditBranch","").equalsIgnoreCase("true")){
                addAdminBranch.setVisibility(View.VISIBLE);
            }else {
                addAdminBranch.setVisibility(View.GONE);
            }
        }else {
            addAdminBranch.setVisibility(View.GONE);
        }

        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);


    }
    private void replaceFragmentTask(Fragment fragment) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
      /*  transaction.setCustomAnimations(
                R.anim.slide_in_from_right, R.anim.slide_out_to_left,
                R.anim.slide_in_from_left, R.anim.slide_out_to_right);*/
        transaction.replace(R.id.frame_container, fragment, "");
        transaction.addToBackStack(null);
        // transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.commit();
    }

}
