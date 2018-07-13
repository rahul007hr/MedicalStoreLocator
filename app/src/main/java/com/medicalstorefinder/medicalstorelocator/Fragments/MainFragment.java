package com.medicalstorefinder.medicalstorelocator.Fragments;

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

import com.medicalstorefinder.medicalstorelocator.R;

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
        Bundle bundle = this.getArguments();
        myValue = bundle.getString("message");


        GridView gridView = (GridView)v.findViewById(R.id.gridview);
        gridView.setAdapter(new MyAdapter(getContext()));

        fragmentManager = getActivity().getSupportFragmentManager();

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                if(myValue.equals("User")){

                    ServiceProviderListFragment fragobj = new ServiceProviderListFragment();
                    Fragment fragment = null;
                    Class fragmentClass1 = null;
                    Intent intent = null;
                    Bundle bundle = new Bundle();
                    String myMessage;

                    FragmentTransaction xfragmentTransaction = fragmentManager.beginTransaction();

                    switch (position) {



                        case 14:
                            myMessage = "medical";
                            bundle.putString("message", myMessage );
                            fragobj.setArguments(bundle);

                            xfragmentTransaction.replace(R.id.containerView, fragobj).commit();
                            fragmentClass1 = ServiceProviderListFragment.class;
                            break;


                       /* case 17:
                            xfragmentTransaction.replace(R.id.containerView, new UserHistryFragment()).commit();
                            fragmentClass1 = UserHistryFragment.class;
                            break;*/
                    }
                    try {
                        fragment = (Fragment) fragmentClass1.newInstance();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        return v;
    }

    private class MyAdapter extends BaseAdapter
    {

        private LayoutInflater inflater;

        public MyAdapter(Context context) {
            inflater = LayoutInflater.from(context);


            if (myValue.equals("User")) {
//                items.add(new Item("Medical", R.drawable.medical));
//                items.add(new Item("Histry", R.drawable.history));
            }

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

            if(v == null)
            {
             /*   v = inflater.inflate(R.layout.gridview_item, viewGroup, false);
                v.setTag(R.id.picture, v.findViewById(R.id.picture));
                v.setTag(R.id.text, v.findViewById(R.id.text));*/
            }

            picture = (ImageView)v.getTag(R.id.profile);
            name = (TextView)v.getTag(R.id.text);

            item = (Item)getItem(i);

            picture.setImageResource(item.drawableId);
            imageUri=picture.toString();
            name.setText(item.name);

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