package com.tpnagar.fcm;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.tpnagar.R;
import com.tpnagar.designdemo.MainActivity;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import me.leolin.shortcutbadger.ShortcutBadger;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    String DataPackage = "", message = "", NotificationType = "0";
    SharedPreferences prefs = null;

 /* @Override
    public void onNewToken(String token) {
        Log.e("Refreshed token:", "Refreshed token: " + token);
      prefs = getSharedPreferences("com.tpnagar", MODE_PRIVATE);
      prefs.edit().putString("FCM_Token",token).commit();
        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.

    }*/

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

    //Log.e("remoteMessage",remoteMessage.getData().toString());

        Log.e("remoteMessage title",remoteMessage.getData().toString());
       // Log.e("remoteMessage body",remoteMessage.getNotification().getBody());

        Map<String, String> params = remoteMessage.getData();
        JSONObject object = new JSONObject(params);

        //Log.e("remoteMessage3",remoteMessage.getNotification().toString());

        try{
            //   JSONObject jsonObj = new JSONObject(remoteMessage.getData());

            sendNotification( object.getString("title"),  object.getString("body"));
        }catch (Exception e){

        }




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


        try {
            prefs = getApplicationContext().getSharedPreferences("com.tpnagar", MODE_PRIVATE);
            int badgeCount = Integer.parseInt(prefs.getString("UnReadMsg","0"));

            int newCount= badgeCount +1;

            prefs.edit().putString("UnReadMsg",""+newCount).commit();
          //  notificationBuilder.setNumber(newCount);
            ShortcutBadger.applyCount(this, newCount); //for 1.1.4+
           // notificationBuilder.setNumber(newCount);


        }catch (Exception e){


        }
         //  notificationBuilder.setNumber(10);
          notificationBuilder.setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL);


        notificationBuilder.setContentIntent(pendingIntent);

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notificationManager.createNotificationChannel(getChannnel());
            }

            notificationManager.notify((int) System.currentTimeMillis(), notificationBuilder.build());



        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("com.therapy.mymind.USER_ACTION");
        broadcastIntent.putExtra("messageTitle", messageTitle);
        broadcastIntent.putExtra("messageBody", messageBody);
        broadcastIntent.putExtra("DataPackage","" );
        sendBroadcast(broadcastIntent);

        /*try {
            prefs = getApplicationContext().getSharedPreferences("com.tpnagar", MODE_PRIVATE);
            int badgeCount = Integer.parseInt(prefs.getString("UnReadMsg","0"));

            int newCount= badgeCount +1;
            ShortcutBadger.applyCount(getApplicationContext(), newCount); //for 1.1.4+

        }catch (Exception e){


        }*/
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
        return useWhiteIcon ? R.mipmap.icon : R.mipmap.icon;
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



 public class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            //  MainActivity mainActivity = ((AppController) context.getApplicationContext()).mainActivity;
            //  mainActivity.etReceivedBroadcast.append("broadcast: "+intent.getAction()+"\n");
        }

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

