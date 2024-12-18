package com.tpnagar.adapter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tpnagar.R;

import java.util.ArrayList;


public class PhoneDetailsAdapter extends BaseAdapter {
    private Activity mContext;
    private ArrayList<String> mList;
    private LayoutInflater mLayoutInflater = null;

    public PhoneDetailsAdapter(Activity context, ArrayList<String> list) {
        mContext = context;
        mList = list;
        mLayoutInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int pos) {
        return mList.get(pos);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        final int positionNew=position;
       CompleteListViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater li = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = li.inflate(R.layout.custom_phone, null);
            viewHolder = new CompleteListViewHolder(v);
            v.setTag(viewHolder);
        } else {
            viewHolder = (CompleteListViewHolder) v.getTag();
        }

        viewHolder.text.setText(mList.get(positionNew));

        viewHolder.rel_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCall(mList.get(positionNew));
            }
        });

        return v;
    }


class CompleteListViewHolder {

    TextView text;
LinearLayout rel_main;

    public CompleteListViewHolder(View base) {



        text = (TextView) base.findViewById(R.id.text);
        rel_main=  base.findViewById(R.id.rel_main);

    }
}
    public void onCall(String number) {
        int permissionCheck = ContextCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(mContext, new String[]{Manifest.permission.CALL_PHONE}, 1);
        } else {
            mContext.startActivity(new Intent(Intent.ACTION_CALL).setData(Uri.parse("tel:"+ number)));
        }
    }
}