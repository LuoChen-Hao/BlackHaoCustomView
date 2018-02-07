package com.balckhao.testActivities;

import android.app.Activity;
import android.os.Bundle;

import com.balckhao.blackhaocustomview.R;
import com.balckhao.blackhaocustomview.circleProgressBarViews.CircleProgressBar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CircleProgressActivity extends Activity {

    @BindView(R.id.circle_progress_1)
    CircleProgressBar circleProgress1;
    @BindView(R.id.circle_progress_2)
    CircleProgressBar circleProgress2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle_progress);
        ButterKnife.bind(this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    for (int i = 0; i <= 20; i++) {
                        circleProgress1.setProgress(i * 5);
                        circleProgress2.setProgress(i * 10);
                        Thread.sleep(500);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
