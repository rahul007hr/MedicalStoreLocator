package com.medicalstorefinder.medicalstoreslocatorss.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.medicalstorefinder.medicalstoreslocatorss.R;

import java.util.ArrayList;
import java.util.List;

public class MainFragment extends Fragment
{
    String imageUri,myValue;
    public MyAdapter.Item item;
    public List<MyAdapter.Item> items = new ArrayList<MyAdapter.Item>();
    FragmentManager fragmentManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_main, container, false);

        GridView gridView = (GridView)v.findViewById(R.id.gridview);
        gridView.setAdapter(new MyAdapter(getContext()));

        fragmentManager = getActivity().getSupportFragmentManager();

        return v;
    }

    private class MyAdapter extends BaseAdapter
    {

        private LayoutInflater inflater;

        public MyAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int i)
        {
            return items.get(i);
        }

        @Override
        public long getItemId(int i)
        {
            return items.get(i).drawableId;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup)
        {
            View v = view;
            ImageView picture;
            TextView name;

            item = (Item)getItem(i);

            return v;
        }

        private class Item
        {
            final String name;
            final int drawableId;

            Item(String name, int drawableId)
            {
                this.name = name;
                this.drawableId = drawableId;
            }
        }
    }
}