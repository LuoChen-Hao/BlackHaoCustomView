package com.balckhao.testActivities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.balckhao.blackhaocustomview.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SeekBarActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seek_bar);
        ButterKnife.bind(this);
    }

}
