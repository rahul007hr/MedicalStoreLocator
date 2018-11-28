package com.medicalstorefinder.mychemists;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class SingleTouchImageViewFragment extends Fragment {

    private TouchImageView image;
    private ProgressBar pb;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.zoom2, container, false);
        String value = getArguments().getString("position1");
        image = (TouchImageView) v.findViewById(R.id.img);
        pb = (ProgressBar) v.findViewById(R.id.homeprogress);
        pb.setVisibility(View.GONE);
        setProgressBar(pb);
        if (!value.equalsIgnoreCase("")) {
            Picasso.with(getContext())
                    .load(value)
                    .into(image);
        } else {
            Toast.makeText(getContext(), "Image Not Available", Toast.LENGTH_LONG).show();
        }
        return v;

    }

    public ProgressBar getProgressBar() {
        return pb;
    }

    public void setProgressBar(ProgressBar progressBar) {
        pb = progressBar;
    }

}
