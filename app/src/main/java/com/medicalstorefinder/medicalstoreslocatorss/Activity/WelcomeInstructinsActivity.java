package com.medicalstorefinder.medicalstoreslocatorss.Activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.medicalstorefinder.medicalstoreslocatorss.Adapter.MyPagerAdapter;
import com.medicalstorefinder.medicalstoreslocatorss.Constants.Constants;
import com.medicalstorefinder.medicalstoreslocatorss.Constants.SharedPreference;
import com.medicalstorefinder.medicalstoreslocatorss.R;

public class WelcomeInstructinsActivity extends AppCompatActivity {
    SharedPreference sharedPreference;
    Activity context = this;
    private ViewPager viewPager;
    private LinearLayout layoutDot;
    private TextView[]dotstv;
    private int[]layouts;
    private Button btnSkip;
    private Button btnNext;
    private MyPagerAdapter pagerAdapter;
    String datas = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();
       /* String string = "Bundle{";
        for (String key : bundle.keySet()) {
            string += " " + key + " => " + bundle.get(key) + ";";
        }
        string += " }Bundle";

        Log.d(string, "onCreate:string ");*/
        if (bundle != null && bundle.get("keys")!=null) {


            //here can get notification message
           datas = bundle.get("keys").toString();

        }

        sharedPreference = new SharedPreference();

        if (sharedPreference.isSPKeyExits(context, Constants.PREF_IS_FIRST_TIME, Constants.PREF_IS_FIRST_TIME)){

            Intent intent = new Intent(WelcomeInstructinsActivity.this,SplashScreenActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("keys", datas);
            startActivity(intent);
            finish();
        }

        setStatusBarTransparent();

        setContentView(R.layout.activity_welcome_instructins);

        viewPager = findViewById(R.id.view_pager);
        layoutDot = findViewById(R.id.dotLayout);
        btnNext = findViewById(R.id.btn_next);
        btnSkip = findViewById(R.id.btn_skip);

        //When user press skip, start Main Activity
        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFirstTimeStartStatus();
                Intent intent = new Intent(WelcomeInstructinsActivity.this,RoleSelectorActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentPage = viewPager.getCurrentItem()+1;
                if(currentPage < layouts.length) {
                    //move to next page
                    viewPager.setCurrentItem(currentPage);
                } else {
                    setFirstTimeStartStatus();
                    Intent intent = new Intent(WelcomeInstructinsActivity.this,RoleSelectorActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
        layouts = new int[]{R.layout.slider_1,R.layout.slider_2, R.layout.slider_3, R.layout.slider_4};
        pagerAdapter = new MyPagerAdapter(layouts,getApplicationContext());
        viewPager.setAdapter(pagerAdapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
                if(position == layouts.length-1){
                    //LAST PAGE
                    btnNext.setText("START");
                    btnSkip.setVisibility(View.GONE);
                }else {
                    btnNext.setText("NEXT");
                    btnSkip.setVisibility(View.VISIBLE);
                }
                setDotStatus(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        setDotStatus(0);
    }

    private void setDotStatus(int page){
        layoutDot.removeAllViews();
        dotstv =new TextView[layouts.length];
        for (int i = 0; i < dotstv.length; i++) {
            dotstv[i] = new TextView(this);
            dotstv[i].setText(Html.fromHtml("&#8226;"));
            dotstv[i].setTextSize(30);
            dotstv[i].setTextColor(Color.parseColor("#a9b4bb"));
            layoutDot.addView(dotstv[i]);
        }
        //Set current dot active
        if(dotstv.length>0){
            dotstv[page].setTextColor(Color.parseColor("#ffffff"));
        }
    }

    private void setStatusBarTransparent(){
        if (Build.VERSION.SDK_INT >= 21){
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE|View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

private void setFirstTimeStartStatus(){

    sharedPreference = new SharedPreference();

    sharedPreference.clearSharedPreference(context, Constants.PREF_IS_FIRST_TIME);
    sharedPreference.createSharedPreference(context, Constants.PREF_IS_FIRST_TIME);
    sharedPreference.putValue(context, Constants.PREF_IS_FIRST_TIME, Constants.PREF_IS_FIRST_TIME,Constants.PREF_IS_FIRST_TIME);
}

}
