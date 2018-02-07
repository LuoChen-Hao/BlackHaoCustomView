package com.balckhao.testActivities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.balckhao.blackhaocustomview.R;
import com.balckhao.blackhaocustomview.calendarView.CalendarView;
import com.balckhao.blackhaocustomview.calendarView.DateSelectedCallBack;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CalendarViewActivity extends AppCompatActivity implements DateSelectedCallBack {
    @BindView(R.id.my_calendar_view)
    CalendarView myCalendarView;
    @BindView(R.id.year_input)
    EditText yearInput;
    @BindView(R.id.month_input)
    EditText monthInput;
    @BindView(R.id.show_result)
    TextView showResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_view);
        ButterKnife.bind(this);
        initCalendarView();
    }

    private void initCalendarView() {
        myCalendarView.setDateSelectedCallBack(this);
    }

    @Override
    public void getDate(View v, int year, int month, int date) {
        showResult.setText(year + " 年 " + month + " 月 " + date + " 日 ");
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.update_calender:
                String yearStr = yearInput.getText().toString().trim();
                String monStr = monthInput.getText().toString().trim();
                if (!yearStr.equals("") && !monStr.equals("")) {
                    int year = Integer.parseInt(yearStr);
                    int month = Integer.parseInt(monStr);
                    myCalendarView.setDate(year, month, -1);
                } else {
                    Toast.makeText(this, "非法输入", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
