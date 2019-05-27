package com.balckhao.blackhaocustomview;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.SeekBar;

import com.balckhao.blackhaocustomview.radioGroup.RelativeRadioGroupActivity;
import com.balckhao.testActivities.DownloadProgressButtonActivity;
import com.balckhao.testActivities.ImgGroupActivity;
import com.balckhao.testActivities.PressedViewActivity;
import com.balckhao.testActivities.SeekBarActivity;
import com.balckhao.testActivities.CalendarViewActivity;
import com.balckhao.testActivities.CircleProgressActivity;
import com.balckhao.testActivities.SegmentActivity;
import com.balckhao.testActivities.SpinnerActivity;
import com.balckhao.testActivities.TableTimeActivity;
import com.balckhao.testActivities.spinnerNew.TestSpinnerNewActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.segment_activity:
                startActivity(new Intent(this, SegmentActivity.class));
                break;
            case R.id.calendar_activity:
                startActivity(new Intent(this, CalendarViewActivity.class));
                break;
            case R.id.circle_progress_activity:
                startActivity(new Intent(this, CircleProgressActivity.class));
                break;
            case R.id.spinner_view_activity:
                startActivity(new Intent(this, SpinnerActivity.class));
                break;
            case R.id.table_time_activity:
                startActivity(new Intent(this, TableTimeActivity.class));
                break;
            case R.id.seek_bar_activity:
                startActivity(new Intent(this, SeekBarActivity.class));
                break;
            case R.id.download_progress_button_activity:
                startActivity(new Intent(this, DownloadProgressButtonActivity.class));
                break;
            case R.id.relative_radio_group_activity:
                startActivity(new Intent(this, RelativeRadioGroupActivity.class));
                break;
            case R.id.spinner_new_activity:
                startActivity(new Intent(this, TestSpinnerNewActivity.class));
                break;
            case R.id.img_group_activity:
                startActivity(new Intent(this, ImgGroupActivity.class));
                break;
            case R.id.pressed_activity:
                startActivity(new Intent(this, PressedViewActivity.class));
                break;
        }
    }
}
