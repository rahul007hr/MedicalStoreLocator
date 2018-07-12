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

                        case 0:
                            myMessage = "paanwala";
                            bundle.putString("message", myMessage );
                            fragobj.setArguments(bundle);

                            xfragmentTransaction.replace(R.id.containerView, fragobj).commit();
                            fragmentClass1 = ServiceProviderListFragment.class;
                            break;

                        case 1:
                            myMessage = "electrician";
                            bundle.putString("message", myMessage );
                            fragobj.setArguments(bundle);

                            xfragmentTransaction.replace(R.id.containerView, fragobj).commit();
                            fragmentClass1 = ServiceProviderListFragment.class;
                            break;

                        case 2:
                            myMessage = "painter";
                            bundle.putString("message", myMessage );
                            fragobj.setArguments(bundle);

                            xfragmentTransaction.replace(R.id.containerView, fragobj).commit();
                            fragmentClass1 = ServiceProviderListFragment.class;
                            break;

                        case 3:
                            myMessage = "kamwalibai";
                            bundle.putString("message", myMessage );
                            fragobj.setArguments(bundle);

                            xfragmentTransaction.replace(R.id.containerView, fragobj).commit();
                            fragmentClass1 = ServiceProviderListFragment.class;
                            break;

                        case 4:
                            myMessage = "garage";
                            bundle.putString("message", myMessage );
                            fragobj.setArguments(bundle);

                            xfragmentTransaction.replace(R.id.containerView, fragobj).commit();
                            fragmentClass1 = ServiceProviderListFragment.class;
                            break;

                        case 5:
                            myMessage = "furniture";
                            bundle.putString("message", myMessage );
                            fragobj.setArguments(bundle);

                            xfragmentTransaction.replace(R.id.containerView, fragobj).commit();
                            fragmentClass1 = ServiceProviderListFragment.class;
                            break;

                        case 6:
                            myMessage = "auto";
                            bundle.putString("message", myMessage );
                            fragobj.setArguments(bundle);

                            xfragmentTransaction.replace(R.id.containerView, fragobj).commit();
                            fragmentClass1 = ServiceProviderListFragment.class;
                            break;

                        case 7:
                            myMessage = "mess";
                            bundle.putString("message", myMessage );
                            fragobj.setArguments(bundle);

                            xfragmentTransaction.replace(R.id.containerView, fragobj).commit();
                            fragmentClass1 = ServiceProviderListFragment.class;
                            break;

                        case 8:
                            myMessage = "pastecontrol";
                            bundle.putString("message", myMessage );
                            fragobj.setArguments(bundle);

                            xfragmentTransaction.replace(R.id.containerView, fragobj).commit();
                            fragmentClass1 = ServiceProviderListFragment.class;
                            break;

                        case 9:
                            myMessage = "tempo";
                            bundle.putString("message", myMessage );
                            fragobj.setArguments(bundle);

                            xfragmentTransaction.replace(R.id.containerView, fragobj).commit();
                            fragmentClass1 = ServiceProviderListFragment.class;
                            break;

                        case 10:
                            myMessage = "plumber";
                            bundle.putString("message", myMessage );
                            fragobj.setArguments(bundle);

                            xfragmentTransaction.replace(R.id.containerView, fragobj).commit();
                            fragmentClass1 = ServiceProviderListFragment.class;
                            break;

                        case 11:
                            myMessage = "travel";
                            bundle.putString("message", myMessage );
                            fragobj.setArguments(bundle);

                            xfragmentTransaction.replace(R.id.containerView, fragobj).commit();
                            fragmentClass1 = ServiceProviderListFragment.class;
                            break;

                        case 12:
                            myMessage = "interior";
                            bundle.putString("message", myMessage );
                            fragobj.setArguments(bundle);

                            xfragmentTransaction.replace(R.id.containerView, fragobj).commit();
                            fragmentClass1 = ServiceProviderListFragment.class;
                            break;

                        case 13:
                            myMessage = "construction";
                            bundle.putString("message", myMessage );
                            fragobj.setArguments(bundle);

                            xfragmentTransaction.replace(R.id.containerView, fragobj).commit();
                            fragmentClass1 = ServiceProviderListFragment.class;
                            break;

                        case 14:
                            myMessage = "medical";
                            bundle.putString("message", myMessage );
                            fragobj.setArguments(bundle);

                            xfragmentTransaction.replace(R.id.containerView, fragobj).commit();
                            fragmentClass1 = ServiceProviderListFragment.class;
                            break;

                        case 15:
                            myMessage = "grocery";
                            bundle.putString("message", myMessage );
                            fragobj.setArguments(bundle);

                            xfragmentTransaction.replace(R.id.containerView, fragobj).commit();
                            fragmentClass1 = ServiceProviderListFragment.class;
                            break;

                        case 16:
                            myMessage = "salon";
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
                }else if(myValue.equals("Service Provider")){

//                    ServiceProviderHistryFragment fragobj = new ServiceProviderHistryFragment();
                    Fragment fragment = null;
                    Class fragmentClass1 = null;
                    Intent intent = null;
                    Bundle bundle = new Bundle();
                    String myMessage;

                    FragmentTransaction xfragmentTransaction = fragmentManager.beginTransaction();

                    switch (position) {

                        case 1:
                           /* xfragmentTransaction.replace(R.id.containerView, new ServiceProviderListItemFragment()).commit();
                            fragmentClass1 = ServiceProviderListItemFragment.class;*/
                            break;

                        case 0:
                            myMessage = "Pending";
                            bundle.putString("message", myMessage );
//                            fragobj.setArguments(bundle);


                           /* xfragmentTransaction.replace(R.id.containerView, fragobj).commit();
                            fragmentClass1 = ServiceProviderHistryFragment.class;*/

                            break;

                        case 2:

                            myMessage = "Histry";
                            bundle.putString("message", myMessage );
//                            fragobj.setArguments(bundle);


                           /* xfragmentTransaction.replace(R.id.containerView, fragobj).commit();
                            fragmentClass1 = ServiceProviderHistryFragment.class;*/

                            break;

                    }
                    try {
                        fragment = (Fragment) fragmentClass1.newInstance();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else{

//                    AdminHistryFragment fragobj = new AdminHistryFragment();
                    Fragment fragment = null;
                    Class fragmentClass1 = null;
                    Intent intent = null;
                    Bundle bundle = new Bundle();
                    String myMessage;

                    FragmentTransaction xfragmentTransaction = fragmentManager.beginTransaction();

                    switch (position) {

                        case 0:
                            myMessage = "Not Activated";
                            bundle.putString("message", myMessage );
//                            fragobj.setArguments(bundle);

                           /* xfragmentTransaction.replace(R.id.containerView, fragobj).commit();
                            fragmentClass1 = AdminHistryFragment.class;*/
                            break;

                        case 1:
                            myMessage = "Activated";
                            bundle.putString("message", myMessage );
//                            fragobj.setArguments(bundle);

                           /* xfragmentTransaction.replace(R.id.containerView, fragobj).commit();
                            fragmentClass1 = AdminHistryFragment.class;*/
                            break;

                        case 2:
                            myMessage = "Deactivated";
                            bundle.putString("message", myMessage );
                           /* fragobj.setArguments(bundle);

                            xfragmentTransaction.replace(R.id.containerView, fragobj).commit();
                            fragmentClass1 = AdminHistryFragment.class;*/
                            break;

                        case 3:

                            myMessage = "Online Service Provider";
                            bundle.putString("message", myMessage );
                           /* fragobj.setArguments(bundle);

                            xfragmentTransaction.replace(R.id.containerView, fragobj).commit();
                            fragmentClass1 = AdminHistryFragment.class;*/

                            break;

                        case 4:

                            myMessage = "Offline Service Provider";
                            bundle.putString("message", myMessage );
                           /* fragobj.setArguments(bundle);

                            xfragmentTransaction.replace(R.id.containerView, fragobj).commit();
                            fragmentClass1 = AdminHistryFragment.class;*/

                            break;

                        case 5:

                            myMessage = "Online User";
                            bundle.putString("message", myMessage );
                            /*fragobj.setArguments(bundle);

                            xfragmentTransaction.replace(R.id.containerView, fragobj).commit();
                            fragmentClass1 = AdminHistryFragment.class;*/

                            break;

                        case 6:

                            myMessage = "Offline User";
                            bundle.putString("message", myMessage );
                           /* fragobj.setArguments(bundle);

                            xfragmentTransaction.replace(R.id.containerView, fragobj).commit();
                            fragmentClass1 = AdminHistryFragment.class;*/

                            break;

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
            if (myValue.equals("Admin")) {
               /* items.add(new Item("Pending Activations", R.drawable.pending_activations));
                items.add(new Item("Activated", R.drawable.activated));
                items.add(new Item("Deactivated", R.drawable.activated));
                items.add(new Item("Online Service Provider", R.drawable.online_service_provider));
                items.add(new Item("Offline Service Providers", R.drawable.online_service_provider));
                items.add(new Item("Online Users", R.drawable.online_users));
                items.add(new Item("Offline Users", R.drawable.online_users));*/

            }

            if (myValue.equals("User")) {
               /* items.add(new Item("Paanwala", R.drawable.paan));
                items.add(new Item("Electrician", R.drawable.electrician));
                items.add(new Item("Painter", R.drawable.painter));
                items.add(new Item("Kaamwalibai", R.drawable.kaamwalibai));
                items.add(new Item("Garage", R.drawable.garage));
                items.add(new Item("Furniture", R.drawable.furniture));
                items.add(new Item("Auto", R.drawable.auto));
                items.add(new Item("Mess", R.drawable.mess));
                items.add(new Item("Pastecontrol", R.drawable.pastecontrol));
                items.add(new Item("Tempo", R.drawable.tempo));
                items.add(new Item("Plumber", R.drawable.plumber));
                items.add(new Item("Travel", R.drawable.travel));
                items.add(new Item("Interior", R.drawable.interior));
                items.add(new Item("Construction", R.drawable.construction));
                items.add(new Item("Medical", R.drawable.medical));
                items.add(new Item("Grocery", R.drawable.groceries));
                items.add(new Item("Salon", R.drawable.salon));
                items.add(new Item("Histry", R.drawable.history));*/
            }

            if (myValue.equals("Service Provider")) {
               /* items.add(new Item("Order Received", R.drawable.activated));
                items.add(new Item("Product List", R.drawable.pending_activations));
                items.add(new Item("Histry", R.drawable.history));*/
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