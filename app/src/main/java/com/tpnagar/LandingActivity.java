package com.tpnagar;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import me.leolin.shortcutbadger.ShortcutBadger;

/**
 * Created by admin on 03-03-2016.
 */
public class LandingActivity extends Activity {
    private int splashTimeOut = 3000;
    private LinearLayout splashLayout;
    TextView text1,text2,text3,text4;
    ImageButton signup_btn,skip_btn,login_btn;
    SharedPreferences prefs = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.landing);
        prefs = getSharedPreferences("com.tpnagar", MODE_PRIVATE);
        text1=(TextView) findViewById(R.id.text1);
        text2=(TextView) findViewById(R.id.text2);
        text3=(TextView) findViewById(R.id.text3);
        text4=(TextView) findViewById(R.id.text4);


        text1.setTypeface(AppController.Externalregular(this));
        text2.setTypeface(AppController.Externalregular(this));
        text3.setTypeface(AppController.Externalregular(this));
        text4.setTypeface(AppController.Externalregular(this));

        signup_btn=(ImageButton) findViewById(R.id.signup_btn);
        skip_btn=(ImageButton) findViewById(R.id.skip_btn);
        login_btn=(ImageButton) findViewById(R.id.login_btn);
        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LandingActivity.this, SignupActivity.class);
                startActivity(intent);
                finish();

            }
        });

        skip_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LandingActivity.this, com.tpnagar.designdemo.MainActivityWithoutLogin.class);
                startActivity(intent);
                finish();

            }
        });
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LandingActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();

            }
        });



    }



}
