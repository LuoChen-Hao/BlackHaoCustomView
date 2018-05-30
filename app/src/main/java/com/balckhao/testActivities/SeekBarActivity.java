package com.balckhao.testActivities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.balckhao.blackhaocustomview.R;
import com.balckhao.blackhaocustomview.mySeekBarView.MySeekBarView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SeekBarActivity extends Activity {

    @BindView(R.id.horizontal_seekbar)
    MySeekBarView horizontalSeekbar;
    @BindView(R.id.vertical_seekbar)
    MySeekBarView verticalSeekbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seek_bar);
        ButterKnife.bind(this);
        horizontalSeekbar.setOnProgressChangeListener(new MySeekBarView.OnProgressChangeListener() {
            @Override
            public void onPressDown(View view, int progress) {

            }

            @Override
            public void onMove(View view, int progress) {

            }

            @Override
            public void onPutUp(View view, int progress) {
                Log.e("SeekBarActivity", "progress:" + progress);
            }
        });

        verticalSeekbar.setOnProgressChangeListener(new MySeekBarView.OnProgressChangeListener() {
            @Override
            public void onPressDown(View view, int progress) {

            }

            @Override
            public void onMove(View view, int progress) {

            }

            @Override
            public void onPutUp(View view, int progress) {
                Log.e("SeekBarActivity", "progress:" + progress);
            }
        });
    }

}
