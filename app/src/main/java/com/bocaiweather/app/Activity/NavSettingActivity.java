package com.bocaiweather.app.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.bocaiweather.app.R;

/**
 * Created by oyj
 * 此类的作用是为了创建设置界面
 */
public class NavSettingActivity extends AppCompatActivity
{
    @Override
    public void onCreate (Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.nav_setting);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_content,new SettingFragment()).commit();
    }
}


