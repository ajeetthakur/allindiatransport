package com.tpnagar.designdemo;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import me.leolin.shortcutbadger.ShortcutBadger;

public class Noticount extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            SharedPreferences prefs = getSharedPreferences("com.tpnagar", MODE_PRIVATE);
            int badgeCount = Integer.parseInt(prefs.getString("UnReadMsg","0"));

            int newCount= badgeCount +1;
            ShortcutBadger.applyCount(this, newCount); //for 1.1.4+

        }catch (Exception e){


        }
        finish();
    }
}
