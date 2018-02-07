package com.balckhao.blackhaocustomview.segmentViews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.balckhao.blackhaocustomview.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by ZhangHao on 2017/5/24.
 * 自定义仿iOS SegmentControlView
 */

public class SegmentControlView extends View implements View.OnTouchListener {

    //字体大小
    private int textSize;
    //选中时的字体颜色
    private int textSelectedColor;
    //未选中时的字体颜色
    private int textUnSelectedColor;
    //选中时的背景色
    private int viewSelectedColor;
    //未选中时的背景色
    private int viewUnSelectedColor;
    //数据源
    private List<String> values;
    //选中的位置
    private int selectedItem = 0;
    //画笔
    private Paint paint;
    //文本画笔
    private Paint textPaint;
    //用于drawText
    private Rect rect;
    //用于绘制最外层圆角边框
    private RectF rectF;
    //用来绘制圆角
    private RectF roundRectF;
    //path用来绘制第一个和最后一个的半圆角矩形以及最外层圆角边框
    private Path roundPath;
    //边框圆角角度
    private int roundRadius;
    //边框宽度
    private int strokeWidth;
    //点击之后的回调
    private SegmentViewItemClickCallBack callBack;


    public SegmentControlView(Context context) {
        this(context, null, 0);
    }

    public SegmentControlView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SegmentControlView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initDefault();
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SegmentControlView, defStyleAttr, 0);
        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.SegmentControlView_textSelectedColor:
                    //选中时的字体颜色
                    textSelectedColor = a.getColor(attr, Color.BLACK);
                    break;
                case R.styleable.SegmentControlView_textUnSelectedColor:
                    //未选中时的字体颜色
                    textUnSelectedColor = a.getColor(attr, Color.GREEN);
                    break;
                case R.styleable.SegmentControlView_viewSelectedColor:
                    //选中时的背景色
                    viewSelectedColor = a.getColor(attr, Color.GREEN);
                    break;
                case R.styleable.SegmentControlView_viewUnSelectedColor:
                    //选中时的背景色
                    viewUnSelectedColor = a.getColor(attr, Color.GREEN);
                    break;
                case R.styleable.SegmentControlView_textSize:
                    //字体大小
                    // 默认设置为16sp，TypeValue也可以把sp转化为px
                    textSize = a.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics()));
                    break;
                case R.styleable.SegmentControlView_roundRadius:
                    // 变宽圆角角度
                    roundRadius = a.getInt(attr, 0);
                    break;
                case R.styleable.SegmentControlView_strokeWidth:
                    // 边框宽度，TypeValue也可以把dp转化为px
                    strokeWidth = a.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP, 16, getResources().getDisplayMetrics()));
                    break;
            }
        }
        //Recycles the TypedArray
        a.recycle();
        //初始化list
        values = new ArrayList<>();
        //初始化画笔
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(strokeWidth);

        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(textSize);

        //初始化Rect
        rect = new Rect();
        rectF = new RectF();
        roundRectF = new RectF();
        //初始化roundPath
        roundPath = new Path();
        //设置onTouch
        setOnTouchListener(this);
    }

    /**
     * 设置默认值
     */
    private void initDefault() {
        //选中时的字体颜色
        textSelectedColor = Color.WHITE;
        //未选中时的字体颜色
        textUnSelectedColor = Color.GRAY;
        //选中时的背景色
        viewSelectedColor = Color.GREEN;
        //未选中时的背景色
        viewUnSelectedColor = Color.WHITE;
        //字体大小16
        textSize = 10;
        //边框圆角角度
        roundRadius = 20;
        //边框宽度
        strokeWidth = 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //获取View的宽高
        int width = getWidth();
        int height = getHeight();
        //获取数据长度
        int dataLen = values.size();
        //判断roundRadius，必须小于width/2和height/2
        if (roundRadius > width / 2 || roundRadius > height / 2) {
            roundRadius = width > height ? height / 2 : width / 2;
        }
        //根据数据长度画背景、文本以及分割线(这里数据长度最少为2)
        if (dataLen > 1) {
            //获取每个item的长度
            int itemLen = width / dataLen;
            //设置画笔为填满
            paint.setStyle(Paint.Style.FILL);
            //绘制背景色以及绘制对应的文本
            for (int i = 0; i < dataLen; i++) {
                if (i == selectedItem) {
                    //如果是选中的item
                    paint.setColor(viewSelectedColor);
                    textPaint.setColor(textSelectedColor);
                } else {
                    paint.setColor(viewUnSelectedColor);
                    textPaint.setColor(textUnSelectedColor);
                }
                //绘制背景
                rectF.set(i * itemLen, 0, (i + 1) * itemLen, height);
                if (i == 0) {
                    //第一个
                    roundPath.reset();
                    roundPath.moveTo(rectF.right, rectF.top);
                    roundPath.lineTo(rectF.left + roundRadius, rectF.top);
                    roundRectF.set(rectF.left, rectF.top,
                            rectF.left + roundRadius * 2, rectF.top + roundRadius * 2);
                    roundPath.arcTo(roundRectF, 270, -90);
                    roundPath.lineTo(rectF.left, rectF.bottom - roundRadius);
                    roundRectF.set(rectF.left, rectF.bottom - roundRadius * 2,
                            rectF.left + roundRadius * 2, rectF.bottom);
                    roundPath.arcTo(roundRectF, 180, -90);
                    roundPath.lineTo(rectF.right, rectF.bottom);
                    roundPath.close();
                    canvas.drawPath(roundPath, paint);
                } else if (i == dataLen - 1) {
                    //最后一个
                    roundPath.reset();
                    roundPath.moveTo(rectF.left, rectF.top);
                    roundPath.lineTo(rectF.right - roundRadius, rectF.top);
                    roundRectF.set(rectF.right - roundRadius * 2, rectF.top,
                            rectF.right, rectF.top + roundRadius * 2);
                    roundPath.arcTo(roundRectF, 270, 90);
                    roundPath.lineTo(rectF.right, rectF.bottom - roundRadius);
                    roundRectF.set(rectF.right - roundRadius * 2, rectF.bottom - roundRadius * 2,
                            rectF.right, rectF.bottom);
                    roundPath.arcTo(roundRectF, 0, 90);
                    roundPath.lineTo(rectF.left, rectF.bottom);
                    roundPath.close();
                    canvas.drawPath(roundPath, paint);
                } else {
                    //除了第一个和最后一个其他的直接绘制矩形即可
                    canvas.drawRect(rectF, paint);
                }
                //绘制文本
                rect.set(i * itemLen, 0, (i + 1) * itemLen, height);
                drawTextOnRect(canvas, rect, values.get(i));
            }
            //分割线
            paint.setColor(viewSelectedColor);
            for (int j = 1; j < dataLen; j++) {
                float x = itemLen * j;
                canvas.drawLine(x, 0, x, height, paint);
            }
        }
        //绘制最外层边框(空心矩形)
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(viewSelectedColor);
        //这里stroke是为了让边框能完全显示，如果rectF.set(0,0,width,height)，那么边框会有一半因为超出画布范围而无法显示
        int stroke = strokeWidth / 2;
        rectF.set(stroke, stroke, width - stroke, height - stroke);
        roundPath.reset();
        roundPath.moveTo(rectF.left, rectF.top + roundRadius);
        roundRectF.set(rectF.left, rectF.top,
                rectF.left + roundRadius * 2, rectF.top + roundRadius * 2);
        roundPath.arcTo(roundRectF, 180, 90);
        roundPath.lineTo(rectF.right - roundRadius, rectF.top);
        roundRectF.set(rectF.right - roundRadius * 2, rectF.top,
                rectF.right, rectF.top + roundRadius * 2);
        roundPath.arcTo(roundRectF, 270, 90);
        roundPath.lineTo(rectF.right, rectF.bottom - roundRadius);
        roundRectF.set(rectF.right - roundRadius * 2, rectF.bottom - roundRadius * 2,
                rectF.right, rectF.bottom);
        roundPath.arcTo(roundRectF, 0, 90);
        roundPath.lineTo(rectF.left + roundRadius, rectF.bottom);
        roundRectF.set(rectF.left, rectF.bottom - roundRadius * 2,
                rectF.left + roundRadius * 2, rectF.bottom);
        roundPath.arcTo(roundRectF, 90, 90);
        roundPath.lineTo(rectF.left, rectF.top + roundRadius);
        roundPath.close();
        canvas.drawPath(roundPath, paint);
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

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //存在数据时才判断点击
                if (values.size() > 0) {
                    //记录点击x坐标
                    int x = (int) event.getX();
                    //获取每个item的长度
                    int itemLen = getWidth() / values.size();
                    //根据x判断点击的区域
                    selectedItem = x / itemLen;
                    if (callBack != null) {
                        callBack.onItemClick(v, selectedItem);
                    }
                }
                //重绘
                postInvalidate();
                break;
        }
        return false;
    }

    public int getTextSize() {
        return textSize;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
        postInvalidate();
    }

    public int getTextSelectedColor() {
        return textSelectedColor;
    }

    public void setTextSelectedColor(int textSelectedColor) {
        this.textSelectedColor = textSelectedColor;
        postInvalidate();
    }

    public int getTextUnSelectedColor() {
        return textUnSelectedColor;
    }

    public void setTextUnSelectedColor(int textUnSelectedColor) {
        this.textUnSelectedColor = textUnSelectedColor;
        postInvalidate();
    }

    public int getViewSelectedColor() {
        return viewSelectedColor;
    }

    public void setViewSelectedColor(int viewSelectedColor) {
        this.viewSelectedColor = viewSelectedColor;
        postInvalidate();
    }

    public int getViewUnSelectedColor() {
        return viewUnSelectedColor;
    }

    public void setViewUnSelectedColor(int viewUnSelectedColor) {
        this.viewUnSelectedColor = viewUnSelectedColor;
        postInvalidate();
    }

    public List<String> getValues() {
        return values;
    }

    public void setValues(List<String> values) {
        this.values.clear();
        this.values.addAll(values);
        postInvalidate();
    }

    public void setValues(String[] values) {
        this.values.clear();
        Collections.addAll(this.values, values);
        postInvalidate();
    }

    public void setOnItemClick(SegmentViewItemClickCallBack callBack) {
        this.callBack = callBack;
    }

    public void setCallBack(SegmentViewItemClickCallBack callBack) {
        this.callBack = callBack;
    }

}
