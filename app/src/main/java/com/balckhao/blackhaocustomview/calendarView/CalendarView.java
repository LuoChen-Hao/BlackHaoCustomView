package com.balckhao.blackhaocustomview.calendarView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.balckhao.blackhaocustomview.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by ZhangHao on 2017/6/1.
 * 自定义日历
 */

public class CalendarView extends View implements View.OnTouchListener {
    //年月
    private int year;
    private int month;
    //选中的日期
    private int selectedDate;
    //字体大小
    private int textSize;
    //星期背景色
    private int weekViewBgColor;
    //星期文本颜色
    private int weekTextColor;
    //日期背景色
    private int dateViewBgColor;
    //日期文本色
    private int dateTextColor;
    //选中日期背景色
    private int dateSelectedBgColor;
    //选择日期文本颜色
    private int dateTextSelectedColor;
    //画笔
    private Paint textPaint;
    private Paint viewPaint;
    //需要用的矩形
    private Rect rect;
    //星期数
    private String[] weeks;
    //计算每行的宽高
    private int preWidth;
    private int preHeight;
    //用于记录对应区域的日期
    private int[][] dateMap;
    //回调
    private DateSelectedCallBack callback;
    //记录按下的坐标
    private float downX, downY;

    public CalendarView(Context context) {
        this(context, null);
    }

    public CalendarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CalendarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initDefault();
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CalendarView, defStyleAttr, 0);
        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.CalendarView_weekViewBgColor:
                    //星期栏背景色
                    weekViewBgColor = a.getColor(attr, Color.parseColor("#104d4f"));
                    break;
                case R.styleable.CalendarView_weekTextColor:
                    //星期栏文本颜色
                    weekTextColor = a.getColor(attr, Color.parseColor("#ffffff"));
                    break;
                case R.styleable.CalendarView_dateViewBgColor:
                    //日期栏背景色
                    dateViewBgColor = a.getColor(attr, Color.TRANSPARENT);
                    break;
                case R.styleable.CalendarView_dateTextColor:
                    //日期栏文本色
                    dateTextColor = a.getColor(attr, Color.parseColor("#104d4f"));
                    break;
                case R.styleable.CalendarView_textSize:
                    //字体大小
                    // 默认设置为16，TypeValue也可以把sp转化为px
                    textSize = a.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics()));
                    break;
                case R.styleable.CalendarView_dateSelectedBgColor:
                    // 选中日期背景色
                    dateSelectedBgColor = a.getColor(attr, Color.parseColor("#ffffff"));
                    break;
                case R.styleable.CalendarView_dateTextSelectedColor:
                    // 选择日期文本颜色
                    dateTextSelectedColor = a.getColor(attr, Color.parseColor("#fc9e19"));
                    break;
                case R.styleable.CalendarView_year:
                    //年
                    year = a.getInt(attr, 1970);
                    break;
                case R.styleable.CalendarView_month:
                    //月
                    month = a.getInt(attr, 1);
                    break;
                case R.styleable.CalendarView_selected:
                    //选中的日期
                    selectedDate = a.getInt(attr, 1);
                    break;
            }
        }
        //Recycles the TypedArray
        a.recycle();
        //初始化画笔
        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(textSize);

        viewPaint = new Paint();
        viewPaint.setAntiAlias(true);
        viewPaint.setStyle(Paint.Style.FILL);
        //初始化矩形
        rect = new Rect();
        //初始化星期数
        weeks = new String[]{"日", "一", "二", "三", "四", "五", "六"};
        //初始化dateMap
        dateMap = new int[7][7];
        //点击事件监听
        setOnTouchListener(this);
    }

    /**
     * 设置参数默认值
     */
    private void initDefault() {
        //默认年月日
        year = 1970;
        month = 1;
        selectedDate = 1;
        //默认字体16
        textSize = 16;
        //星期背景色
        weekViewBgColor = Color.parseColor("#104d4f");
        //星期文本颜色
        weekTextColor = Color.parseColor("#ffffff");
        //日期背景色
        dateViewBgColor = Color.TRANSPARENT;
        //日期文本色
        dateTextColor = Color.parseColor("#104d4f");
        //选中日期背景色
        dateSelectedBgColor = Color.parseColor("#fc9e19");
        //选择日期文本颜色
        dateTextSelectedColor = Color.parseColor("#ffffff");
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        //计算View的宽高
        int width = widthSize, height = heightSize;
        if (widthMode == MeasureSpec.EXACTLY) {
            //指定大小或者match_parent
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            //wrap_content
            width = 100;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            //指定大小或者match_parent
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            //wrap_content
            height = 100;
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        /**
         * 获取基本信息
         */
        //当前月份的天数/星期数
        int[] monthInfos = getInfosByYearMonth(year, month);
        //当前月1号是星期几
        int weekOfFirstDay = getDayOfWeekByDate(year + "-" + month + "-1");
        //计算每行的宽高
        preWidth = getWidth() / 7;
        //这里第一行是星期，所以计算高时要在星期数的基础上加1
        preHeight = getHeight() / (monthInfos[1] + 1);
        /**
         * 先绘制星期栏相关
         */
        //背景
        rect.set(0, 0, getWidth(), preHeight);
        viewPaint.setColor(weekViewBgColor);
        canvas.drawRect(rect, viewPaint);
        //星期数
        textPaint.setColor(weekTextColor);
        for (int i = 0; i < 7; i++) {
            rect.set(i * preWidth, 0, (i + 1) * preWidth, preHeight);
            drawTextOnRect(canvas, rect, weeks[i]);
        }
        /**
         * 绘制日期
         */
        //背景
        rect.set(0, preHeight, getWidth(), getHeight());
        viewPaint.setColor(dateViewBgColor);
        canvas.drawRect(rect, viewPaint);
        //绘制日期
        //行,从第二行开始计算
        int row = 2;
        //列
        int rank = weekOfFirstDay;
        for (int i = 1; i <= monthInfos[0]; i++) {
            rect.set(rank * preWidth, (row - 1) * preHeight, (rank + 1) * preWidth, row * preHeight);
            dateMap[row - 1][rank] = i;
            if (i == selectedDate) {
                //如果当前是选中的日期
                textPaint.setColor(dateTextSelectedColor);
                //绘制背景圆
                viewPaint.setColor(dateSelectedBgColor);
                //圆半径
                int radius = preWidth > preHeight ? preHeight / 2 : preWidth / 2;
                if (radius > 30) {
                    //设置圆半径最大值
                    radius = 30;
                }
                canvas.drawCircle(rect.left + preWidth / 2, rect.top + preHeight / 2, radius, viewPaint);
            } else {
                textPaint.setColor(dateTextColor);
            }
            //绘制日期
            drawTextOnRect(canvas, rect, i + "");
            //行列重新计算
            if (rank + 1 >= weeks.length) {
                //如果当前已经是周六
                rank = 0;
                row++;
            } else {
                rank++;
            }
        }
    }

    /**
     * 在指定矩形中间drawText
     *
     * @param canvas     画布
     * @param targetRect 指定矩形
     * @param text       需要绘制的Text
     */
    private void drawTextOnRect(Canvas canvas, Rect targetRect, String text) {
        Paint.FontMetricsInt fontMetrics = textPaint.getFontMetricsInt();
        // 获取baseLine
        int baseline = targetRect.top + (targetRect.bottom - targetRect.top - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
        // 下面这行是实现水平居中，drawText对应改为传入targetRect.centerX()
        textPaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(text, targetRect.centerX(), baseline, textPaint);
    }

    /**
     * 根据年 月 获取对应的月份 天数以及星期数
     */
    private int[] getInfosByYearMonth(int year, int month) {
        int[] infos = new int[2];
        Calendar a = Calendar.getInstance();
        a.set(Calendar.YEAR, year);
        a.set(Calendar.MONTH, month - 1);
        a.set(Calendar.DATE, 1);
        a.roll(Calendar.DATE, -1);
        infos[0] = a.get(Calendar.DATE);
        infos[1] = a.getActualMaximum(Calendar.WEEK_OF_MONTH);
        return infos;
    }

    /**
     * 根据日期 找到对应日期是星期几
     */
    private int getDayOfWeekByDate(String date) {
        int week = -1;
        try {
            String dayOfWeek = "-1";
            SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date myDate = myFormatter.parse(date);
            SimpleDateFormat formatter = new SimpleDateFormat("E", Locale.getDefault());
            dayOfWeek = formatter.format(myDate);
            for (int i = 0; i < weeks.length; i++) {
                if (weeks[i].equals(dayOfWeek.substring(dayOfWeek.length() - 1))) {
                    week = i;
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return week;
    }


    public void setTextSize(int textSize) {
        this.textSize = textSize;
        postInvalidate();
    }

    public void setWeekViewBgColor(int weekViewBgColor) {
        this.weekViewBgColor = weekViewBgColor;
        postInvalidate();
    }

    public void setWeekTextColor(int weekTextColor) {
        this.weekTextColor = weekTextColor;
        postInvalidate();
    }

    public void setDateViewBgColor(int dateViewBgColor) {
        this.dateViewBgColor = dateViewBgColor;
        postInvalidate();
    }

    public void setDateTextColor(int dateTextColor) {
        this.dateTextColor = dateTextColor;
        postInvalidate();
    }

    public void setDateSelectedBgColor(int dateSelectedBgColor) {
        this.dateSelectedBgColor = dateSelectedBgColor;
        postInvalidate();
    }

    public void setDateTextSelectedColor(int dateTextSelectedColor) {
        this.dateTextSelectedColor = dateTextSelectedColor;
        postInvalidate();
    }

    public void setDateSelectedCallBack(DateSelectedCallBack callback) {
        this.callback = callback;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getSelectedDate() {
        return selectedDate;
    }

    /**
     * 设置日期，不需要修改可以传0或者负数
     *
     * @param year  年
     * @param month 月
     * @param date  日
     */
    public void setDate(int year, int month, int date) {
        if (year >= 1970 && year <= 3000) {
            this.year = year;
        }
        if (month > 0 && month < 13) {
            this.month = month;
        }
        if (date > 0 && date < 31) {
            this.selectedDate = date;
        }
        postInvalidate();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //仅监听按下的事件
                //记录坐标点
                downX = event.getX();
                downY = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                //监听抬起事件
                float moveX = event.getX() - downX;
                float moveY = event.getY() - downY;
                if (Math.abs(moveX) <= 10 && Math.abs(moveY) <= 10) {
                    //按下抬起x,y距离都不超过10时，认为是按下
                    if (preHeight > 0 && preWidth > 0) {
                        int rank = (int) (downX / preWidth);
                        int row = (int) (downY / preHeight);
                        if (dateMap[row][rank] > 0) {
                            //如果选中的是日期
                            selectedDate = dateMap[row][rank];
                            postInvalidate();
                            //回调
                            if (callback != null) {
                                callback.getDate(v, year, month, selectedDate);
                            }
                        }
                    }
                } else if (moveX < -10) {
                    //向左滑动
                    if (month == 12) {
                        setDate(year + 1, 1, -1);
                    } else {
                        setDate(-1, month + 1, -1);
                    }
                    //回调
                    if (callback != null) {
                        callback.getDate(v, year, month, selectedDate);
                    }
                } else if (moveX > -10) {
                    //向右滑动
                    if (month == 1) {
                        setDate(year - 1, 12, -1);
                    } else {
                        setDate(-1, month - 1, -1);
                    }
                    //回调
                    if (callback != null) {
                        callback.getDate(v, year, month, selectedDate);
                    }
                }
                break;
        }
        return true;
    }
}
