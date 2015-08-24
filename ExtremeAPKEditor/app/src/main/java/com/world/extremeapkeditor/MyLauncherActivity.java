package com.world.extremeapkeditor;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.world.extremeapkeditor.Adapters.IntroAdapter;

/**
 * Created by AbdulAziz on 7/5/2015.
 */
public class MyLauncherActivity extends AppCompatActivity implements View.OnClickListener {
    //    LinearLayout open,help;
    ViewPager viewPager;
    IntroAdapter adapter;
    View mView[] = new View[4];
    private int last = 0;
    ImageButton nextBTN;
    private int COUNT = 1;
    String appPrefs = "ExtremeAPKExtractor";
    SharedPreferences preference;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preference = getSharedPreferences(appPrefs, Context.MODE_PRIVATE);
        if (preference.getString("FirstTime","").equals("done")){
            Intent intent = new Intent(MyLauncherActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        setContentView(R.layout.activity_launcher);
        mView[0] = findViewById(R.id.v1);
        mView[1] = findViewById(R.id.v2);
        mView[2] = findViewById(R.id.v3);
        mView[3] = findViewById(R.id.v4);
        nextBTN = (ImageButton) findViewById(R.id.next_btn);


        viewPager = (ViewPager) findViewById(R.id.view_pager);
        adapter = new IntroAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mView[position].setBackgroundResource(R.drawable.white_view);
                mView[last].setBackgroundResource(R.drawable.grey_bg);
                last = position;
                if (position == 3) {
                    nextBTN.setImageResource(R.drawable.ic_done);
                } else {
                    nextBTN.setImageResource(R.drawable.arrow_forward_w);
                }
                COUNT = position + 1;

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        nextBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (COUNT == 3) {
                    nextBTN.setImageResource(R.drawable.ic_done);
                    viewPager.setCurrentItem(COUNT);
                } else if (COUNT == 4) {
                    Intent intent = new Intent(MyLauncherActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    SharedPreferences.Editor editor = preference.edit();
                    editor.putString("FirstTime","done");
                    editor.commit();
                } else {
                    nextBTN.setImageResource(R.drawable.arrow_forward_w);
                    viewPager.setCurrentItem(COUNT);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
//        if(view.getId() == R.id.open){
//
//        }else{
//            Intent intent = new Intent(MyLauncherActivity.this,HelpActivity.class);
//            startActivity(intent);
//        }
    }
}
