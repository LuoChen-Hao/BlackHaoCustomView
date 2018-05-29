package com.balckhao.blackhaocustomview;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.balckhao.testActivities.SeekBarActivity;
import com.balckhao.testActivities.CalendarViewActivity;
import com.balckhao.testActivities.CircleProgressActivity;
import com.balckhao.testActivities.SegmentActivity;
import com.balckhao.testActivities.SpinnerActivity;
import com.balckhao.testActivities.TableTimeActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.segment_activity:
                Intent i1 = new Intent(this, SegmentActivity.class);
                startActivity(i1);
                break;
            case R.id.calendar_activity:
                Intent i2 = new Intent(this, CalendarViewActivity.class);
                startActivity(i2);
                break;
            case R.id.circle_progress_activity:
                Intent i3 = new Intent(this, CircleProgressActivity.class);
                startActivity(i3);
                break;
            case R.id.spinner_view_activity:
                Intent i4 = new Intent(this, SpinnerActivity.class);
                startActivity(i4);
                break;
            case R.id.table_time_activity:
                Intent i5 = new Intent(this, TableTimeActivity.class);
                startActivity(i5);
                break;
            case R.id.seek_bar_activity:
                Intent i6 = new Intent(this, SeekBarActivity.class);
                startActivity(i6);
                break;
        }
    }
}
