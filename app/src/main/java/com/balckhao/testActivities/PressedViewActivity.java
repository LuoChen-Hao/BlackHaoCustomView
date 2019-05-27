package com.balckhao.testActivities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.balckhao.blackhaocustomview.R;
import com.balckhao.blackhaocustomview.pressView.PressedView;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PressedViewActivity extends AppCompatActivity {
    @BindView(R.id.pressed_view)
    PressedView pressedView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pressed_view);
        ButterKnife.bind(this);
        //录音按钮监听
        pressedView.setCallback(new PressedView.PressCallback() {

            @Override
            public void onStartRecord() {
                isFinish = false;
                new TestThread().start();
            }

            @Override
            public void onStopRecord() {
                isFinish = true;
            }

            @Override
            public void onCancelRecord() {
                isFinish = true;
            }
        });
    }

    boolean isFinish = false;

    /**
     * 模拟音量线程
     */
    private class TestThread extends Thread {
        @Override
        public void run() {
            super.run();
            while (!isFinish) {
                try {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //模拟音量
                            int volume = (int) (Math.random() * 30000);
                            pressedView.setVolume(volume);
                        }
                    });
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
