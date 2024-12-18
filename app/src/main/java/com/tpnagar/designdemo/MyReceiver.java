package com.tpnagar.designdemo;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import me.leolin.shortcutbadger.ShortcutBadger;


/**
 * Created by admin on 12/29/2016.
 */
public class MyReceiver extends BroadcastReceiver {
   // Intent intent=null;
    AlertDialog.Builder alert;
    @Override
    public void onReceive(final Context context, Intent intent) {


        Intent broadcastIntent = new Intent(context, Noticount.class);
        broadcastIntent.putExtra("messageTitle", intent.getStringExtra("messageTitle"));
        broadcastIntent.putExtra("messageBody", intent.getStringExtra("messageBody"));
        broadcastIntent.putExtra("DataPackage", intent.getStringExtra("DataPackage"));
        // broadcastIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        broadcastIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        broadcastIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        context.startActivity(broadcastIntent);


      /* */

    }

}