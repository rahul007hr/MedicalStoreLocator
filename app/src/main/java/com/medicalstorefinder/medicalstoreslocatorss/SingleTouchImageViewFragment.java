package com.medicalstorefinder.medicalstoreslocatorss;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class SingleTouchImageViewFragment extends Fragment {
	
	private TouchImageView image;
	private ProgressBar pb;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.zoom2, container, false);

//Retrieve the value
		String value = getArguments().getString("position1");

		image = (TouchImageView) v.findViewById(R.id.img);
		pb = (ProgressBar)v.findViewById(R.id.homeprogress);
		pb.setVisibility(View.GONE);

		Picasso.with(getContext())
				.load(value) //extract as User instance method
				.into(image);

		return v;

	}
}
