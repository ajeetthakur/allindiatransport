package com.tpnagar;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.util.Log;

import static android.content.Context.MODE_PRIVATE;


public class BaseContainerFragment extends Fragment {
	SharedPreferences prefs = null;
	public void replaceFragment(Fragment fragment, boolean addToBackStack) {
		FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
		if (addToBackStack) {
			transaction.addToBackStack(null);
		}
		transaction.replace(R.id.container_framelayout, fragment);
		transaction.commit();
		getChildFragmentManager().executePendingTransactions();
	}
	
	public boolean popFragment() {
		Log.e("test", "pop fragment: " + getChildFragmentManager().getBackStackEntryCount());
		boolean isPop = false;
		if (getChildFragmentManager().getBackStackEntryCount() > 0) {
			isPop = true;
			getChildFragmentManager().popBackStack();
		}
		return isPop;
	}
	
//	public void replaceFragment(Fragment fragment, boolean addToBackStack,String eventdetail) {
//
//		  FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
//		  if (addToBackStack) {
//		   transaction.addToBackStack(null);
//		  }
//		  transaction.replace(R.id.container_framelayout, fragment,eventdetail);
//		  transaction.commit();
//		  getChildFragmentManager().executePendingTransactions();
//
//	}
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
		prefs = getActivity().getSharedPreferences("com.tpnagar", MODE_PRIVATE);
        // getView().setFocusableInTouchMode(true);
        // getView().requestFocus();

    }
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
	
		Fragment frag = getChildFragmentManager().findFragmentByTag("eventdetail");
		if(frag!=null)  
		   frag.onActivityResult(requestCode, resultCode, data);



	}

    /*private void OpenDialog(){

        final Dialog dialog = new Dialog(getActivity(),android.R.style.Theme_Black_NoTitleBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.close_pop_up);


        TextView rateitapp=(TextView)dialog.findViewById(R.id.rate_it);
        TextView shareitapp=(TextView)dialog.findViewById(R.id.share_it);
        TextView skip=(TextView)dialog.findViewById(R.id.skip_app);

        rateitapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Uri uri = Uri.parse("market://details?id=" + getActivity().getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                // To count with Play market backstack, After pressing back button,
                // to taken back to our application, we need to add following flags to intent.
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + getActivity().getPackageName())));
                }
               // dialog.dismiss();

            }
        });

        shareitapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //String shareBody = item.getShare_link();
                String shareBody = "share";
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));
                sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share imotor"));

              //  dialog.dismiss();

            }
        });

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                getActivity().finish();
            }
        });

        dialog.show();


    }*/

}
