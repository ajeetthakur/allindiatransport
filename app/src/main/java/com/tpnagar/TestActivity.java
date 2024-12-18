package com.tpnagar;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class TestActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);

        ListView list1 = (ListView) findViewById(R.id.list1);
        ListView list2 = (ListView) findViewById(R.id.list2);
        ListView list3 = (ListView) findViewById(R.id.list3);


        String[] items1 = { "Alfa1", "Alfa2", "Alfa3", "Alfa4", "Alfa5", "Alfa6", "Alfa7", "Alfa8", "Alfa9", "Alfa10", "Alfa11", "Alfa12", "Alfa13", "Alfa14", "Alfa15", "Alfa16", "Alfa17"  };
        String[] items2 = { "Beta1", "Beta2", "Beta3", "Beta4", "Beta5", "Beta6", "Beta7", "Beta8", "Beta9", "Beta10", "Beta11", "Beta12", "Beta13", "Beta14", "Beta15", "Beta16", "Beta17" };
        String[] items3 = { "Teta1", "Teta2", "Teta3", "Teta4", "Teta5", "Teta6", "Teta7", "Teta8", "Teta9", "Teta10", "Teta11", "Teta12", "Teta13", "Teta14", "Teta15", "Teta16", "Teta17" };

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, items1);
        list1.setAdapter(adapter1);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, items2);
        list2.setAdapter(adapter2);
        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, items3);
        list3.setAdapter(adapter3);

        setListViewHeightBasedOnChildren(list1);
        setListViewHeightBasedOnChildren(list2);
        setListViewHeightBasedOnChildren(list3);

    }

    public void setListViewHeightBasedOnChildren(ListView listView) {
        ArrayAdapter listAdapter = (ArrayAdapter) listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }


}