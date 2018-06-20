package com.balckhao.testActivities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.balckhao.blackhaocustomview.R;
import com.balckhao.blackhaocustomview.downloadProgressButton.DownloadProgressButton;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DownloadProgressButtonActivity extends AppCompatActivity implements
        DownloadProgressButton.ProgressChangeListener {

    @BindView(R.id.download_pb_1)
    DownloadProgressButton downloadPb1;
    @BindView(R.id.download_pb_2)
    DownloadProgressButton downloadPb2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_progress_botton);
        ButterKnife.bind(this);
        downloadPb1.setClickListener(this);
        downloadPb2.setClickListener(this);
    }

    @Override
    public void onPbClick(View view, int model) {
        switch (view.getId()) {
            case R.id.download_pb_1:
                if (model == DownloadProgressButton.READY_TO_DOWNLOAD) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            for (int i = 0; i <= 100; i = i + 5) {
                                downloadPb1.setProgress(i);
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }).start();
                }
                break;
            case R.id.download_pb_2:
                if (model == DownloadProgressButton.READY_TO_DOWNLOAD) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            for (int i = 0; i <= 103; i = i + 3) {
                                downloadPb2.setProgress(i);
                                try {
                                    Thread.sleep(500);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }

                        }
                    }).start();
                }
                break;
        }
    }

    @Override
    public void onSuccess(View view) {
        switch (view.getId()) {
            case R.id.download_pb_1:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(DownloadProgressButtonActivity.this, "First Success", Toast.LENGTH_SHORT).show();
                        }
                    });

                break;
            case R.id.download_pb_2:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(DownloadProgressButtonActivity.this, "Second Success", Toast.LENGTH_SHORT).show();
                        }
                    });
                break;
        }
    }

    @Override
    public void onFailed(View view) {
        switch (view.getId()) {
            case R.id.download_pb_1:
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(DownloadProgressButtonActivity.this, "First Failed", Toast.LENGTH_SHORT).show();
                    }
                });

                break;
            case R.id.download_pb_2:
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(DownloadProgressButtonActivity.this, "Second Failed", Toast.LENGTH_SHORT).show();
                    }
                });
                break;
        }
    }
}
