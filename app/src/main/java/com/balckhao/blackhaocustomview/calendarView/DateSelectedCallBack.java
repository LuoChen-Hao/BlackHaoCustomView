package com.balckhao.blackhaocustomview.calendarView;

import android.view.View;

/**
 * Created by ZhangHao on 2017/6/1.
 * CalendarView日期选中回调
 */

public interface DateSelectedCallBack {
    void getDate(View v, int year, int month, int date);
}
