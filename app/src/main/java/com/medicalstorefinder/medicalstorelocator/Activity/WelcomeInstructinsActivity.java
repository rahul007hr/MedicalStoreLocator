package com.medicalstorefinder.medicalstorelocator.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.medicalstorefinder.medicalstorelocator.Adapter.SliderAdapter;
import com.medicalstorefinder.medicalstorelocator.Constants.SharedPreference;
import com.medicalstorefinder.medicalstorelocator.R;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class WelcomeInstructinsActivity extends AppCompatActivity {

    private ViewPager mSlideViewPager;
    private LinearLayout mDotLayout;
    private TextView[] mDots;
    private Button mNextBtn;
    private Button mBackBtn;
    private SliderAdapter sliderAdapter;
    private int mCurrentPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(!isFirstTimeStartApp()){
            Intent intent = new Intent(WelcomeInstructinsActivity.this,SplashScreenActivity.class);
            startActivity(intent);
            finish();
        }

        setContentView(R.layout.activity_welcome_instructins);

        mSlideViewPager = (ViewPager) findViewById(R.id.slideViewPager);
        mDotLayout = (LinearLayout) findViewById(R.id.dotsLayout);
        mNextBtn=(Button)findViewById(R.id.nextBtn);
        mBackBtn=(Button)findViewById(R.id.prevBtn);

        mSlideViewPager.setAdapter(sliderAdapter);
        addDotsIndicator(0);

        mSlideViewPager.addOnPageChangeListener(viewListener);

        mNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( mCurrentPage < mDots.length){
                    mSlideViewPager.setCurrentItem(mCurrentPage + 1);
                }else{
                    setFirstTimeStartStatus(true);
                    Intent intent = new Intent(WelcomeInstructinsActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSlideViewPager.setCurrentItem(mCurrentPage - 1);
            }
        });

    }

    public  void addDotsIndicator(int position){
        mDots=new TextView[3];
        for(int i=0;i<mDots.length;i++) {
            mDots[i] = new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226;"));
            mDots[i].setTextSize(35);
            mDots[i].setTextColor(getResources().getColor(R.color.color_transperant_white));
            mDotLayout.addView(mDots[i]);
        }

        if(mDots.length>0) {
            mDots[position].setTextColor(getResources().getColor(R.color.color_white));
        }

        }
ViewPager.OnPageChangeListener viewListener= new ViewPager.OnPageChangeListener() {
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        addDotsIndicator(position);
        mCurrentPage=position;

        if(position==0){
            mNextBtn.setEnabled(true);
            mBackBtn.setEnabled(false);
            mBackBtn.setVisibility(View.INVISIBLE);

            mNextBtn.setText("Next");
            mBackBtn.setText("");
        }else if(position==mDots.length - 1){
            mNextBtn.setEnabled(true);
            mBackBtn.setEnabled(true);
            mBackBtn.setVisibility(View.VISIBLE);

            mNextBtn.setText("Finish");
            mBackBtn.setText("Back");
        }else{
            mNextBtn.setEnabled(true);
            mBackBtn.setEnabled(true);
            mBackBtn.setVisibility(View.VISIBLE);

            mNextBtn.setText("Next");
            mBackBtn.setText("Back");
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
};

private boolean isFirstTimeStartApp(){
    SharedPreferences ref = getApplicationContext().getSharedPreferences("IntriSliderApp", Context.MODE_PRIVATE);
    return ref.getBoolean("FirstTimeStartFlag",true);
}

private void setFirstTimeStartStatus(boolean stt){
    SharedPreferences ref = getApplicationContext().getSharedPreferences("IntriSliderApp", Context.MODE_PRIVATE);
    SharedPreferences.Editor editor= ref.edit();
    editor.putBoolean("FirstTimeStartFlag",stt);
    editor.commit();
}

}
