package com.tpnagar.viewgallery;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;

import com.squareup.picasso.Picasso;
import com.tpnagar.R;


/**
 * Created by Ashish on 01-08-2017.
 */

public class ViewZoomImageActivity extends AppCompatActivity {
    TouchImageView touchImageView;
    String Url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zoom_fragment);
        try {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                    WindowManager.LayoutParams.FLAG_SECURE);
        } catch (Exception e) {
        }
        touchImageView=(TouchImageView) findViewById(R.id.touch_imv);
        Url=getIntent().getStringExtra("Url");

        Picasso.with(this)
                .load(Url)
                .error(R.drawable.default_user)
                .placeholder(R.drawable.progress_animation )
                .into(touchImageView);
    }

    /**
     * method called on click cross button to go back
     *
     * @param view button view
     */
    public void closeMap(View view) {
        onBackPressed();
    }
}