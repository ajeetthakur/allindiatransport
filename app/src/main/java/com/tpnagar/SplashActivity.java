package com.tpnagar;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.fragment.app.FragmentActivity;
import androidx.core.app.NotificationCompat;
import android.util.Base64;
import android.util.Log;
import android.widget.LinearLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.tpnagar.designdemo.MainActivity;
import com.tpnagar.wrapper.CompanyTypeWrapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.leolin.shortcutbadger.ShortcutBadger;

/**
 * Created by admin on 03-03-2016.
 */
public class SplashActivity extends FragmentActivity {
    private int splashTimeOut = 500;
    private LinearLayout splashLayout;
    SharedPreferences prefs = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);


        prefs = getSharedPreferences("com.tpnagar", MODE_PRIVATE);

        try {
            int badgeCount = Integer.parseInt(prefs.getString("UnReadMsg","0"));


            //  notificationBuilder.setNumber(newCount);
            ShortcutBadger.applyCount(this, badgeCount); //for 1.1.4+
            // notificationBuilder.setNumber(newCount);


        }catch (Exception e){


        }
      //sendNotification("test","sdsd");
     // Intent intent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
    //    intent.putExtra("badge_count", 34);
   //     intent.putExtra("badge_count_package_name", getPackageName());
    //    intent.putExtra("badge_count_class_name", SplashActivity.class);
   //     sendBroadcast(intent);
       /* BadgeUtils.setBadge(this,70);
      */


     // int badgeCount = Integer.parseInt(prefs.getString("UnReadMsg","0"));
    // ShortcutBadger.applyCount(this, 90);

    //    BadgeUtils.setBadge(this,5);
    //    AppIcon.setBadge(this,7);

        if(prefs.getString("Login_Id","").length()>0){
            JSONObject obj = new JSONObject();

            try {

                obj.put("LoginId", prefs.getString("Login_Id",""));
                AppController.spinnerStart(this);
                serviceGetCompanyDetailsRequestCompanyType(obj);
                Log.e("this is obj:::", obj.toString());

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else {
            if (savedInstanceState == null) {
                splashTaskConfig();
            }
        }



    }



    private void getHashKey() {

        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:",
                        Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
        } catch (NoSuchAlgorithmException e) {
        }
    }

    private void splashTaskConfig() {
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

              //  prefs.edit().putString("IsActive","yes").commit();

                if(prefs.getString("IsActive","").equalsIgnoreCase("yes"))
                {
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    Intent intent = new Intent(SplashActivity.this, LandingActivity.class);
                    startActivity(intent);
                    finish();
                }


            }
        }, splashTimeOut);
    }

    private void serviceGetCompanyDetailsRequestCompanyType(JSONObject params) {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, Const.URL_GetCompanyDetails+prefs.getString("Company_Id","1"),
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
                        //  prefs.edit().putString("Company_Category_Id",jsonbObject.getString("Company_Category_Id")).commit();
                        prefs.edit().putString("Company_Type_Id",jsonbObject.getString("Company_Type_Id")).commit();
                        prefs.edit().putString("Current_Status_Id",jsonbObject.getString("Current_Status_Id")).commit();
                        prefs.edit().putString("Mobile_No",jsonbObject.getString("Mobile_No")).commit();
                        prefs.edit().putString("Storage_Id",jsonbObject.getString("Storage_Id")).commit();
                        prefs.edit().putString("Phone_No",jsonbObject.getString("Phone_No")).commit();
                        prefs.edit().putString("Email",jsonbObject.getString("Email")).commit();




                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
splashTaskConfig();

                AppController.spinnerStop();

            }
        };
        return response;
    }

    //This method is only generating push notification
    //It is same as we did in earlier posts
    public void sendNotification(String messageTitle, String messageBody) {

        Boolean background = isAppIsInBackground(this);

        // if(background){
        Intent intent = null;
        intent = new Intent(this, MainActivity.class);
        intent.putExtra("ISNotification", "yes");
        intent.putExtra("iSPending", false);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_IMMUTABLE);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this,"com.therapy.mymind");
        notificationBuilder.setSmallIcon(getNotificationIcon());
        notificationBuilder.setContentTitle(messageTitle);
        notificationBuilder.setContentText(messageBody);
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setSound(defaultSoundUri);

        notificationBuilder.setNumber(10);
        notificationBuilder.setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL);

        notificationBuilder.setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(getChannnel());
        }

        notificationManager.notify((int) System.currentTimeMillis(), notificationBuilder.build());
           /* }else {

            Intent broadcastIntent = new Intent();
            broadcastIntent.setAction("com.therapy.mymind.USER_ACTION");
            broadcastIntent.putExtra("messageTitle", messageTitle);
            broadcastIntent.putExtra("messageBody", messageBody);

            sendBroadcast(broadcastIntent);
        }*/

    }




    private int getNotificationIcon() {
        boolean useWhiteIcon = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP);
        return useWhiteIcon ? R.drawable.ic_stat_tp_nagar : R.drawable.ic_stat_tp_nagar;
    }

    public  boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }




    private NotificationChannel getChannnel() {

        String CHANNEL_ID = "com.therapy.mymind";// The id of the channel.
        CharSequence name = getString(R.string.app_name);// The user-visible name of the channel.
        int importance = NotificationManager.IMPORTANCE_HIGH;

        NotificationChannel mChannel = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            mChannel.setShowBadge(true);
            mChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
        }

        return mChannel;
    }

}
