package com.balckhao.blackhaocustomview.timeTableView;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

/**
 * Author ： BlackHao
 * Time : 2018/2/28 13:53
 * Description : 自定义课程表
 */

public class TimeTableView extends View {

    //边框颜色
    private int borderColor;
    //边框宽度
    private int borderWidth;
    //画笔
    private Paint paint;
    //每日课程数
    private int dayClassNum;
    //课程时间
    private ArrayList<ClassTime> classTimes;
    //周一至周五课程
    private ArrayList<String> monClasses;
    private ArrayList<String> tueClasses;
    private ArrayList<String> thrClasses;
    private ArrayList<String> wenClasses;
    private ArrayList<String> friClasses;

    public TimeTableView(Context context) {
        this(context, null, 0);
    }

    public TimeTableView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TimeTableView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initDefault();
    }

    private void initDefault() {
        //课程时间
        classTimes = new ArrayList<>();
        //周一至周五课程
        monClasses = new ArrayList<>();
        tueClasses = new ArrayList<>();
        thrClasses = new ArrayList<>();
        wenClasses = new ArrayList<>();
        friClasses = new ArrayList<>();
        borderColor = Color.parseColor("#104d4f");
        borderWidth = 2;
        dayClassNum = 6;
    }

}
