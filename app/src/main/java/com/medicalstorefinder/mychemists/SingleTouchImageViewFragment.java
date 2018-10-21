package com.medicalstorefinder.mychemists;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
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

//		spinner = (ProgressBar)itemView.findViewById(R.id.progressBar1);
		setProgressBar(pb);

		Picasso.with(getContext())
				.load(value) //extract as User instance method
				.into(image);
		/*RequestOptions options = new RequestOptions()
				.centerCrop()
				.placeholder(R.drawable.profile_pic)
//                            .error(R.drawable.ic_pic_error)
				.priority(Priority.HIGH);

		new PicasoImageLoader(image,
				getProgressBar()).load(value,options);*/

		return v;

	}

	public ProgressBar getProgressBar() {
		return pb;
	}

	public void setProgressBar(ProgressBar progressBar) {
		pb = progressBar;
	}

}
