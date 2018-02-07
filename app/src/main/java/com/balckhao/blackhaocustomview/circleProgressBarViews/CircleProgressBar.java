package com.balckhao.blackhaocustomview.circleProgressBarViews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.balckhao.blackhaocustomview.R;

/**
 * Created by ZhangHao on 2017/5/22.
 * 自定义圆形Progress
 */

public class CircleProgressBar extends View {

    //进度最大值
    private int max;
    //当前进度
    private int progress;
    //显示的进度(progress/max*100 %)
    private int showProgress;
    //进度条的颜色
    private int progressColor;
    //进度条宽度百分比
    private int progressWidthPercent;
    //底层背景色
    private int bottomColor;
    //最上层圆的颜色
    private int topColor;
    //进度文本的颜色
    private int textColor;
    //进度文本的字体大小
    private int textSize;
    //进度条画笔
    private Paint progressPaint;
    //文本画笔
    private Paint textPaint;
    //用来显示文本的矩形
    private Rect textRect;

    public CircleProgressBar(Context context) {
        this(context, null);
    }

    public CircleProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initDefault();
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CircleProgressBar, defStyleAttr, 0);
        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.CircleProgressBar_topColor:
                    //最上层圆（进度文本背景）的颜色,默认绿色
                    topColor = a.getColor(attr, Color.TRANSPARENT);
                    break;
                case R.styleable.CircleProgressBar_textColor:
                    //进度文本的颜色
                    textColor = a.getColor(attr, Color.BLACK);
                    break;
                case R.styleable.CircleProgressBar_progressColor:
                    //进度条颜色
                    progressColor = a.getColor(attr, Color.GREEN);
                    break;
                case R.styleable.CircleProgressBar_bottomColor:
                    //背景色
                    bottomColor = a.getColor(attr, Color.GREEN);
                    break;
                case R.styleable.CircleProgressBar_textSize:
                    //进度文本的大小
                    // 默认设置为16sp，TypeValue也可以把sp转化为px
                    textSize = a.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics()));
                    break;
                case R.styleable.CircleProgressBar_progressWidthPercent:
                    //进度条宽度占总宽度的百分比
                    progressWidthPercent = a.getInteger(attr, 50);
                    break;
                case R.styleable.CircleProgressBar_max:
                    //进度最大值
                    max = a.getInteger(attr, 100);
                    break;
                case R.styleable.CircleProgressBar_progress:
                    //进度值
                    progress = a.getInteger(attr, 0);
                    break;
            }

        }
        //Recycles the TypedArray
        a.recycle();
        /**
         * 初始化画笔
         */
        //文本画笔
        textPaint = new Paint();
        textPaint.setTextSize(textSize);
        textPaint.setColor(textColor);
        //设置抗锯齿
        textPaint.setAntiAlias(true);
        //进度条画笔
        progressPaint = new Paint();
        //设置抗锯齿
        progressPaint.setAntiAlias(true);
        //初始化textRect
        textRect = new Rect();
    }

    /**
     * 设置默认值
     */
    private void initDefault() {
        //最上层默认为透明
        topColor = Color.TRANSPARENT;
        //字体颜色默认为黑色
        textColor = Color.BLACK;
        //进度条默认为绿色
        progressColor = Color.GREEN;
        //背景为灰色
        bottomColor = Color.GRAY;
        //字体大小16
        textSize = 16;
        //进度0
        progress = 0;
        //最大进度100
        max = 100;
        //进度条宽度比例50
        progressWidthPercent = 50;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //canvas的宽高
        int width = getWidth();
        int height = getHeight();
        //进度条宽度占的比例
        double scale = progressWidthPercent / 100.00;
        //获取宽高中较短的作为底层圆的半径
        int bottomRadius = width > height ? height / 2 : width / 2;
        //这四个参数用来drawText以及drawArc
        int top = width / 2 - bottomRadius;
        int bottom = width / 2 + bottomRadius;
        int left = height / 2 - bottomRadius;
        int right = height / 2 + bottomRadius;
        //显示的进度
        showProgress = progress * 100 / max;
        //绘制最底层的大圆(空心圆)
        progressPaint.setStyle(Paint.Style.STROKE);
        progressPaint.setColor(bottomColor);
        //大圆的宽度刚好等于进度条的宽度
        int strokeWidth = (int) (bottomRadius * scale);
        progressPaint.setStrokeWidth(strokeWidth);
        //这里要考虑到strokeWidth的长度
        canvas.drawCircle(width / 2, height / 2, bottomRadius - strokeWidth / 2, progressPaint);
        //根据进度绘制进度条
        progressPaint.setStyle(Paint.Style.STROKE);
        progressPaint.setStrokeWidth(strokeWidth);
        progressPaint.setColor(progressColor);
        //起始角度为-90度才是从正上方开始画
        canvas.drawArc(left + strokeWidth / 2, top + strokeWidth / 2, right - strokeWidth / 2, bottom - strokeWidth / 2,
                -90, 360 * showProgress / 100, false, progressPaint);
        //绘制最上层的圆(实心圆)
        progressPaint.setStyle(Paint.Style.FILL);
        int topRadius = (int) (bottomRadius * (1 - scale));
        progressPaint.setColor(topColor);
        //设置边框宽度
        progressPaint.setStrokeWidth(0);
        canvas.drawCircle(width / 2, height / 2, topRadius, progressPaint);
        //在圆的中央显示进度文本
        textRect.set(left, top, right, bottom);
        drawTextOnRect(canvas, textRect, showProgress + " %");
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

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
        postInvalidate();
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        if (progress <= 100) {
            this.progress = progress;
            postInvalidate();
        }
    }

    public int getProgressColor() {
        return progressColor;
    }

    public void setProgressColor(int progressColor) {
        this.progressColor = progressColor;
        postInvalidate();
    }

    public int getBottomColor() {
        return bottomColor;
    }

    public int getProgressWidthPercent() {
        return progressWidthPercent;
    }

    public void setProgressWidthPercent(int progressWidthPercent) {
        this.progressWidthPercent = progressWidthPercent;
        postInvalidate();
    }

    public void setBottomColor(int bottomColor) {
        this.bottomColor = bottomColor;
        postInvalidate();
    }

    public int getTopColor() {
        return topColor;
    }

    public void setTopColor(int topColor) {
        this.topColor = topColor;
        postInvalidate();
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
        postInvalidate();
    }

    public int getTextSize() {
        return textSize;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
        postInvalidate();
    }
}
