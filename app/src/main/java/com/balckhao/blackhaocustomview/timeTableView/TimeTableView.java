package com.balckhao.blackhaocustomview.timeTableView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.balckhao.blackhaocustomview.R;

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

    private int textSize;

    private int textColor;
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
    //星期数
    private String[] weeks = new String[]{"周一", "周二", "周三", "周四", "周五"};

    private Rect rect;

    public TimeTableView(Context context) {
        this(context, null, 0);
    }

    public TimeTableView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TimeTableView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initDefault();
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.TableTimeView, defStyleAttr, 0);
        for (int i = 0; i < a.getIndexCount(); i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.TableTimeView_borderColor:
                    //边框颜色
                    borderColor = a.getColor(attr, Color.BLUE);
                    break;
                case R.styleable.TableTimeView_borderWidth:
                    //
                    borderWidth = a.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics()));
                    break;
                case R.styleable.TableTimeView_dayClassNum:
                    //
                    dayClassNum = a.getInteger(attr, 6);
                    break;
                case R.styleable.SpinnerView_textSize:
                    // 默认设置为16sp，TypeValue也可以把sp转化为px
                    textSize = a.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics()));
                    break;
                case R.styleable.SpinnerView_textColor:
                    //字体颜色
                    textColor = a.getColor(attr, Color.BLUE);
                    break;
            }
        }
        a.recycle();

        //初始化画笔
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTextSize(textSize);
        paint.setStrokeWidth(borderWidth);

        rect = new Rect();
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
        textColor = Color.parseColor("#104d4f");
        textSize = 14;
        borderWidth = 2;
        dayClassNum = 6;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制边框,内部线条
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(borderColor);
        rect.set(borderWidth / 2, borderWidth / 2, getWidth() - borderWidth / 2, getHeight() - borderWidth / 2);
        canvas.drawRect(rect, paint);
        //标题框和课程框高度
        int titleHeight = getHeight() / (dayClassNum + 2);
        int classHeight = (getHeight() - titleHeight) / dayClassNum;
        canvas.drawLine(0, titleHeight, getWidth(), titleHeight, paint);
        for (int i = 0; i < dayClassNum - 1; i++) {
            canvas.drawLine(0, titleHeight + classHeight * (i + 1),
                    getWidth(), titleHeight + classHeight * (i + 1), paint);
        }
        //标题框和课程框宽度
        int timeWidth = getWidth() / 9;
        int classWidth = (getWidth() - timeWidth * 2) / 5;
        canvas.drawLine(timeWidth, 0, timeWidth, getHeight(), paint);
        canvas.drawLine(timeWidth * 2, 0, timeWidth * 2, getHeight(), paint);
        for (int i = 0; i < 4; i++) {
            canvas.drawLine(timeWidth * 2 + classWidth * (i + 1), 0,
                    timeWidth * 2 + classWidth * (i + 1), getHeight(), paint);
        }
        paint.setStyle(Paint.Style.FILL);
        //绘制开始/结束 时间
        paint.setColor(textColor);
        //开始时间
        for (int i = 0; i < dayClassNum + 1; i++) {
            if (i == 0) {
                //绘制标题
                rect.set(0, 0, timeWidth, titleHeight);
                drawTextOnRect(canvas, rect, "开始时间");
            } else {
                rect.set(0, titleHeight + classHeight * (i - 1), timeWidth, titleHeight + classHeight * i);
                drawTextOnRect(canvas, rect, classTimes.get(i - 1).getStartHour() + ":" + classTimes.get(i - 1).getStartMin());
            }
        }
        //结束时间
        for (int i = 0; i < dayClassNum + 1; i++) {
            if (i == 0) {
                //绘制标题
                rect.set(timeWidth, 0, timeWidth * 2, titleHeight);
                drawTextOnRect(canvas, rect, "结束时间");
            } else {
                rect.set(timeWidth, titleHeight + classHeight * (i - 1), timeWidth * 2,
                        titleHeight + classHeight * i);
                drawTextOnRect(canvas, rect, classTimes.get(i - 1).getEndHour() + ":" + classTimes.get(i - 1).getEndMin());
            }
        }
        //星期数
        for (int i = 0; i < 5; i++) {
            rect.set(timeWidth * 2 + classWidth * i, 0, timeWidth * 2 + classWidth * (i + 1), titleHeight);
            drawTextOnRect(canvas, rect, weeks[i]);
        }
        //周一至周五课程
        for (int i = 0; i < 5; i++) {
            int left = timeWidth * 2 + classWidth * i;
            int right = timeWidth * 2 + classWidth * (i + 1);
            for (int j = 0; j < dayClassNum; j++) {
                rect.set(left, titleHeight + classHeight * j, right, titleHeight + classHeight * (j + 1));
                drawTextOnRect(canvas, rect, getClassName(i, j));
            }
        }

    }

    public void setDayClassNum(int dayClassNum) {
        this.dayClassNum = dayClassNum;
    }

    public void setClassTimes(ArrayList<ClassTime> classTimes) {
        this.classTimes.clear();
        this.classTimes.addAll(classTimes);
    }

    public void setMonClasses(ArrayList<String> monClasses) {
        this.monClasses.clear();
        this.monClasses.addAll(monClasses);
    }

    public void setTueClasses(ArrayList<String> tueClasses) {
        this.tueClasses.clear();
        this.tueClasses.addAll(tueClasses);
    }

    public void setThrClasses(ArrayList<String> thrClasses) {
        this.thrClasses.clear();
        this.thrClasses.addAll(thrClasses);
    }

    public void setWenClasses(ArrayList<String> wenClasses) {
        this.wenClasses.clear();
        this.wenClasses.addAll(wenClasses);
    }

    public void setFriClasses(ArrayList<String> friClasses) {
        this.friClasses.clear();
        this.friClasses.addAll(friClasses);
    }

    public void setAllClasses(ArrayList<ClassTime> classTimes, ArrayList<String> monClasses, ArrayList<String> tueClasses,
                              ArrayList<String> thrClasses, ArrayList<String> wenClasses, ArrayList<String> friClasses) {
        this.classTimes.clear();
        this.classTimes.addAll(classTimes);
        this.monClasses.clear();
        this.monClasses.addAll(monClasses);
        this.tueClasses.clear();
        this.tueClasses.addAll(tueClasses);
        this.thrClasses.clear();
        this.thrClasses.addAll(thrClasses);
        this.wenClasses.clear();
        this.wenClasses.addAll(wenClasses);
        this.friClasses.clear();
        this.friClasses.addAll(friClasses);
        postInvalidate();
    }

    /**
     * 在指定矩形中间drawText
     *
     * @param canvas     画布
     * @param targetRect 指定矩形
     * @param text       需要绘制的Text
     */
    private void drawTextOnRect(Canvas canvas, Rect targetRect, String text) {
        Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
        // 获取baseLine
        int baseline = targetRect.top + (targetRect.bottom - targetRect.top - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
        // 下面这行是实现水平居中，drawText对应改为传入targetRect.centerX()
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(text, targetRect.centerX(), baseline, paint);
    }

    //通过周几和第几节来获取课程名
    private String getClassName(int weekday, int classNum) {
        String drawText = "";
        try {
            switch (weekday) {
                case 0:
                    drawText = monClasses.get(classNum);
                    break;
                case 1:
                    drawText = tueClasses.get(classNum);
                    break;
                case 2:
                    drawText = thrClasses.get(classNum);
                    break;
                case 3:
                    drawText = wenClasses.get(classNum);
                    break;
                case 4:
                    drawText = friClasses.get(classNum);
                    break;
            }
        } catch (Exception e) {
            drawText = "";
        }
        return drawText;
    }
}
