package com.carlog.gilberto.carlog.fragments;

/**
 * Created by Gilberto on 04/08/2015.
 */
import android.app.Activity;
import android.content.res.TypedArray;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;

import com.carlog.gilberto.carlog.R;
import com.carlog.gilberto.carlog.activity.baseActivity;
import com.carlog.gilberto.carlog.adapter.simpleHeaderRecyclerAdapter;
import com.carlog.gilberto.carlog.adapter.simpleRecyclerAdapter;

import java.util.ArrayList;

public abstract class baseFragment extends Fragment {
    public static ArrayList<String> getDummyData() {
        return baseActivity.getDummyData();
    }

    protected int getActionBarSize() {
        Activity activity = getActivity();
        if (activity == null) {
            return 0;
        }
        TypedValue typedValue = new TypedValue();
        int[] textSizeAttr = new int[]{R.attr.actionBarSize};
        int indexOfAttrTextSize = 0;
        TypedArray a = activity.obtainStyledAttributes(typedValue.data, textSizeAttr);
        int actionBarSize = a.getDimensionPixelSize(indexOfAttrTextSize, -1);
        a.recycle();
        return actionBarSize;
    }

    protected int getScreenHeight() {
        Activity activity = getActivity();
        if (activity == null) {
            return 0;
        }
        return activity.findViewById(android.R.id.content).getHeight();
    }

    protected void setDummyData(ListView listView) {
        listView.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, getDummyData()));
    }

    protected void setDummyDataWithHeader(ListView listView, View headerView) {
        listView.addHeaderView(headerView);
        setDummyData(listView);
    }

    protected void setDummyData(GridView gridView) {
        gridView.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, getDummyData()));
    }

   /* protected void setDummyDataWithHeader(ObservableGridView gridView, View headerView) {
        gridView.addHeaderView(headerView);
        setDummyData(gridView);
    }*/

    protected void setDummyData(RecyclerView recyclerView) {
        recyclerView.setAdapter(new simpleRecyclerAdapter(getActivity(), getDummyData()));
    }

    protected void setDummyDataWithHeader(RecyclerView recyclerView, View headerView) {
        recyclerView.setAdapter(new simpleHeaderRecyclerAdapter(getActivity(), getDummyData(), headerView));
    }
}