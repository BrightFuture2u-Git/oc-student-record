package com.m7.imkfsdk.chat;

import android.os.Bundle;
import android.util.DisplayMetrics;

import androidx.appcompat.app.AppCompatActivity;

/**
 * 基础Activity
 */
public class MyBaseActivity extends AppCompatActivity {

    protected int width;
    protected int height;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        getDisplay();
        super.onCreate(savedInstanceState);
    }

    private void getDisplay() {
        DisplayMetrics metrics = new DisplayMetrics();

        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        width = metrics.widthPixels;
        height = metrics.heightPixels;
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}