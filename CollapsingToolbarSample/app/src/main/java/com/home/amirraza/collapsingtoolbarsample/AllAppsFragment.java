package com.home.amirraza.collapsingtoolbarsample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Created by AmirRaza on 7/5/2015.
 */
public class AllAppsFragment extends Fragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    String[] arr = {"Item","Item","Item","Item","Item","Item","Item","Item","Item","Item","Item","Item","Item","Item"};
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_all_apps,container,false);
        ListView listView = (ListView) v.findViewById(R.id.myList);
        listView.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,arr));
        return v;
    }
}
