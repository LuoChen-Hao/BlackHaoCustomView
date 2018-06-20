package com.balckhao.blackhaocustomview.downloadProgressButton;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.balckhao.blackhaocustomview.R;

/**
 * Author ： BlackHao
 * Time : 2018/5/18 10:25
 * Description : 自定义带圆形进度条下载控件
 * Remarks :
 */

public class DownloadProgressButton extends View implements View.OnClickListener {

    /**
     * 未下载
     */
    public static final int READY_TO_DOWNLOAD = 0;
    /**
     * 正在下载
     */
    public static final int IS_DOWNLOADING = 1;
    /**
     * 下载完成
     */
    public static final int FINISH_DOWNLOAD = 2;
    /**
     * 下载失败
     */
    public static final int FAIL_DOWNLOAD = 3;
    //进度最大值
    private int max;
    //当前进度
    private int progress;
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
    //当前模式
    private int downloadModel = 0;
    //
    private ProgressChangeListener clickListener;
    private Path path;

    public DownloadProgressButton(Context context) {
        this(context, null, 0);
    }

    public DownloadProgressButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DownloadProgressButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initDefault();
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.DownloadProgressButton, defStyleAttr, 0);
        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.DownloadProgressButton_topColor) {//最上层圆的颜色,默认绿色
                topColor = a.getColor(attr, Color.TRANSPARENT);

            } else if (attr == R.styleable.DownloadProgressButton_textColor) {//进度文本的颜色
                textColor = a.getColor(attr, Color.BLACK);

            } else if (attr == R.styleable.DownloadProgressButton_progressColor) {//进度条颜色
                progressColor = a.getColor(attr, Color.GREEN);

            } else if (attr == R.styleable.DownloadProgressButton_bottomColor) {//背景色
                bottomColor = a.getColor(attr, Color.GREEN);

            } else if (attr == R.styleable.DownloadProgressButton_textSize) {//进度文本的大小
                // 默认设置为16sp，TypeValue也可以把sp转化为px
                textSize = a.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics()));

            } else if (attr == R.styleable.DownloadProgressButton_progressWidthPercent) {//进度条宽度占总宽度的百分比
                progressWidthPercent = a.getInteger(attr, 50);
            }
        }
        //Recycles the TypedArray
        a.recycle();
        /*
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
        path = new Path();
        //
        setOnClickListener(this);
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
        //中点
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        //获取宽高中较短的作为底层圆的半径
        int bottomRadius = width > height ? getHeight() / 2 : getWidth() / 2;
        //下载边框宽度
        int circleWidth = bottomRadius / 5;
        switch (downloadModel) {
            case READY_TO_DOWNLOAD:
                //绘制下载按钮
                progressPaint.setColor(Color.GRAY);
                progressPaint.setStrokeWidth(circleWidth);
                //绘制圆
                progressPaint.setStyle(Paint.Style.STROKE);
                canvas.drawCircle(centerX, centerY, bottomRadius - circleWidth / 2, progressPaint);
                //绘制三角箭头
                progressPaint.setStyle(Paint.Style.FILL);
                canvas.drawLine(centerX, centerY, centerX, centerY - bottomRadius / 2, progressPaint);
                //绘制三角形
                path.reset();
                path.moveTo(centerX - bottomRadius / 2, centerY);// 此点为多边形的起点
                path.lineTo(centerX + bottomRadius / 2, centerY);
                path.lineTo(centerX, centerY + bottomRadius / 2);
                path.close(); // 使这些点构成封闭的多边形
                canvas.drawPath(path, progressPaint);
                break;
            case IS_DOWNLOADING:
                //进度条宽度占的比例
                double scale = progressWidthPercent / 100.00;
                //这四个参数用来drawText以及drawArc
                int top = centerY - bottomRadius;
                int bottom = centerY + bottomRadius;
                int left = centerX - bottomRadius;
                int right = centerX + bottomRadius;
                //显示的进度
                int showProgress = progress * 100 / max;
                //绘制最底层的大圆(空心圆)
                progressPaint.setStyle(Paint.Style.STROKE);
                progressPaint.setColor(bottomColor);
                //大圆的宽度刚好等于进度条的宽度
                int strokeWidth = (int) (bottomRadius * scale);
                progressPaint.setStrokeWidth(strokeWidth);
                //这里要考虑到strokeWidth的长度
                canvas.drawCircle(centerX, centerY, bottomRadius - strokeWidth / 2, progressPaint);
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
                canvas.drawCircle(centerX, centerY, topRadius, progressPaint);
                //在圆的中央显示进度文本
                textRect.set(left, top, right, bottom);
                drawTextOnRect(canvas, textRect, showProgress + " %");
                break;
            case FINISH_DOWNLOAD:
                //绘制下载完成
                progressPaint.setColor(Color.GREEN);
                progressPaint.setStrokeWidth(circleWidth);
                //绘制圆
                progressPaint.setStyle(Paint.Style.STROKE);
                canvas.drawCircle(centerX, centerY, bottomRadius - circleWidth / 2, progressPaint);
                //绘制完成符号（√）
                progressPaint.setStyle(Paint.Style.FILL);
                canvas.drawLine(centerX, centerY + bottomRadius / 2, centerX - bottomRadius / 2, centerY, progressPaint);
                canvas.drawLine(centerX - circleWidth / 2, centerY + bottomRadius / 2, centerX + bottomRadius / 2, centerY - bottomRadius / 2, progressPaint);
                break;
            case FAIL_DOWNLOAD:
                //下载失败
                progressPaint.setColor(Color.RED);
                progressPaint.setStrokeWidth(circleWidth);
                //绘制圆
                progressPaint.setStyle(Paint.Style.STROKE);
                canvas.drawCircle(centerX, centerY, bottomRadius - circleWidth / 2, progressPaint);
                //绘制完成符号（X）
                progressPaint.setStyle(Paint.Style.FILL);
                canvas.drawLine(centerX - bottomRadius / 2, centerY - bottomRadius / 2, centerX + bottomRadius / 2, centerY + bottomRadius / 2, progressPaint);
                canvas.drawLine(centerX - bottomRadius / 2, centerY + bottomRadius / 2, centerX + bottomRadius / 2, centerY - bottomRadius / 2, progressPaint);
                break;
        }

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
     * @param text       需要绘制的 Text
     */
    private void drawTextOnRect(Canvas canvas, Rect targetRect, String text) {
        Paint.FontMetricsInt fontMetrics = textPaint.getFontMetricsInt();
        // 获取baseLine
        int baseline = targetRect.top + (targetRect.bottom - targetRect.top - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
        // 下面这行是实现水平居中，drawText对应改为传入targetRect.centerX()
        textPaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(text, targetRect.centerX(), baseline, textPaint);
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
        if (progress == max) {
            downloadModel = FINISH_DOWNLOAD;
            //完成回调结果
            if (clickListener != null) {
                clickListener.onSuccess(this);
            }
        } else if (progress == 0) {
            downloadModel = READY_TO_DOWNLOAD;
        } else if (progress > 0 && progress < max) {
            downloadModel = IS_DOWNLOADING;
        }else {
            downloadModel = FAIL_DOWNLOAD;
            //完成回调结果
            if (clickListener != null) {
                clickListener.onFailed(this);
            }
        }
        postInvalidate();
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

    public void setClickListener(ProgressChangeListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public void onClick(View v) {
        if (clickListener != null) {
            clickListener.onPbClick(v, downloadModel);
        }
    }

    /**
     * 点击/下载完成（失败） 回调
     */
    public interface ProgressChangeListener {
        //点击监听
        void onPbClick(View view, int model);
        //下载成功
        void onSuccess(View view);
        //下载失败
        void onFailed(View view);
    }
}
