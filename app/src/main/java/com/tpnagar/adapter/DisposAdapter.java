package com.tpnagar.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tpnagar.R;
import com.tpnagar.wrapper.DisposWrapper;

import java.util.ArrayList;


public class DisposAdapter extends BaseAdapter {
    private Activity mContext;
    private ArrayList<DisposWrapper> mList;
    private LayoutInflater mLayoutInflater = null;

    public DisposAdapter(Activity context, ArrayList<DisposWrapper> list) {
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
            v = li.inflate(R.layout.custom_dispos, null);
            viewHolder = new CompleteListViewHolder(v);
            v.setTag(viewHolder);
        } else {
            viewHolder = (CompleteListViewHolder) v.getTag();
        }

        viewHolder.text.setText(mList.get(position).getDisp_Name());

        return v;
    }


class CompleteListViewHolder {

    TextView text;


    public CompleteListViewHolder(View base) {



        text = (TextView) base.findViewById(R.id.text);


    }
}
}