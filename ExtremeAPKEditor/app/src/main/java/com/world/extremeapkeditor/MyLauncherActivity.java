package com.world.extremeapkeditor;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by AmirRaza on 7/5/2015.
 */
public class MyLauncherActivity extends AppCompatActivity implements View.OnClickListener {
    LinearLayout open,help;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        open = (LinearLayout) findViewById(R.id.open);
        help = (LinearLayout) findViewById(R.id.help_btn);
        open.setOnClickListener(this);
        help.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.open){
            Intent intent = new Intent(MyLauncherActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }else{
            Intent intent = new Intent(MyLauncherActivity.this,HelpActivity.class);
            startActivity(intent);
        }
    }
}
