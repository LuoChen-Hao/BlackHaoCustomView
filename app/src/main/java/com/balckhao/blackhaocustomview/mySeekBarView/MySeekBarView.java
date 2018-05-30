package com.balckhao.blackhaocustomview.mySeekBarView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.balckhao.blackhaocustomview.R;

/**
 * Author ： BlackHao
 * Time : 2018/3/27 13:29
 * Description : 自定义 SeekBar
 */

public class MySeekBarView extends View implements View.OnTouchListener {

    //下拉按钮资源ID
    private int pullDownSrc;
    //下拉按钮按下资源ID
    private int pullDownPressedSrc;
    //下拉按钮Bitmap
    private Bitmap pullDownBitmap;
    //下拉按钮按下Bitmap
    private Bitmap pullDownPressedBitmap;
    //边框圆角
    private int radius;
    //偏移量
    private int offset;
    //垂直/水平样式
    private boolean isVertical;
    //当前进度
    private int progress;
    //总进度
    private int max;
    //进度条颜色
    private int progressColor;
    //未选中进度颜色
    private int unProgressColor;
    //画笔
    private Paint paint;
    //是否按下标识
    private boolean isPressed = false;
    //
    private Rect rect;
    //按钮位置
    private int x, y;
    //进度条长度
    private int progressLength;
    //按钮宽度
    private int srcLength;
    //是否可拖动
    private boolean isDrag = true;
    //进度回调
    private OnProgressChangeListener listener;

    public MySeekBarView(Context context) {
        this(context, null, 0);
    }

    public MySeekBarView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MySeekBarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //默认参数
        initDefaultValue();
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.MySeekBarView, defStyleAttr, 0);
        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.MySeekBarView_pullDownPressedSrc:
                    //下拉按钮按下资源ID
                    pullDownPressedSrc = a.getResourceId(attr, R.drawable.seek_bar_pressed);
                    break;
                case R.styleable.MySeekBarView_pullDownSrc:
                    //下拉按钮资源ID
                    pullDownSrc = a.getResourceId(attr, R.drawable.seek_bar_normal);
                    break;
                case R.styleable.MySeekBarView_radius:
                    //圆角度
                    radius = a.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics()));
                    break;
                case R.styleable.MySeekBarView_offset:
                    //偏移量
                    offset = a.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics()));
                    break;
                case R.styleable.MySeekBarView_orientation:
                    //垂直/水平样式
                    int style = a.getInt(attr, -1);
                    if (style > 0) {
                        isVertical = style == 1;
                    }
                    break;
                case R.styleable.MySeekBarView_progressColor:
                    //进度颜色
                    progressColor = a.getColor(attr, Color.GRAY);
                    break;
                case R.styleable.MySeekBarView_unProgressColor:
                    //未选中进度颜色
                    unProgressColor = a.getColor(attr, Color.GRAY);
                    break;
                case R.styleable.MySeekBarView_progress:
                    //获取进度
                    progress = a.getInt(attr, 0);
                    break;
                case R.styleable.MySeekBarView_max:
                    //获取进度
                    max = a.getInt(attr, 0);
                    break;
                case R.styleable.MySeekBarView_draggable:
                    isDrag = a.getBoolean(attr, true);
                    break;
                default:
                    break;
            }
        }
        a.recycle();
        //初始化画笔
        paint = new Paint();
        paint.setAntiAlias(true);
        //初始化rect
        rect = new Rect();
        //初始化Bitmap
        pullDownBitmap = BitmapFactory.decodeResource(getResources(), pullDownSrc);
        pullDownPressedBitmap = BitmapFactory.decodeResource(getResources(), pullDownPressedSrc);
        //转换失败
        if (pullDownBitmap == null) {
            pullDownBitmap = getBitmapFromVectorDrawable(pullDownSrc);
        }
        if (pullDownPressedBitmap == null) {
            pullDownPressedBitmap = getBitmapFromVectorDrawable(pullDownPressedSrc);
        }
        //设置点击监听
        setOnTouchListener(this);
    }

    //初始化默认参数
    private void initDefaultValue() {
        pullDownPressedSrc = R.drawable.seek_bar_pressed;
        pullDownSrc = R.drawable.seek_bar_normal;
        radius = 0;
        offset = 2;
        isVertical = false;
        progressColor = Color.GRAY;
        unProgressColor = Color.WHITE;
        max = 100;
        progress = 0;
        isDrag = true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制已选中进度
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(progressColor);
        if (isVertical) {
            canvas.drawRoundRect(offset, y,
                    getWidth() - offset, getHeight() - srcLength / 2,
                    radius / 2, radius / 2, paint);
        } else {
            canvas.drawRoundRect(offset + srcLength / 2, offset,
                    x, getHeight() - offset, radius / 2, radius / 2, paint);
        }
        //绘制未选中进度
        paint.setColor(unProgressColor);
        if (isVertical) {
            canvas.drawRoundRect(offset, srcLength / 2,
                    getWidth() - offset, y, radius / 2, radius / 2, paint);
        } else {
            canvas.drawRoundRect(x, offset, getWidth() - offset - srcLength / 2,
                    getHeight() - offset, radius / 2, radius / 2, paint);
        }
        //绘制按钮
        rect.set(x - srcLength / 2, y - srcLength / 2, x + srcLength / 2, y + srcLength / 2);
        if (isPressed) {
            canvas.drawBitmap(pullDownPressedBitmap, null, rect, paint);
        } else {
            canvas.drawBitmap(pullDownBitmap, null, rect, paint);
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
            width = 300;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            //指定大小或者match_parent
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            //wrap_content
            height = 50;
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //利用垂直/水平样式获取默认x,y
        srcLength = w < h ? w : h;
        if (isVertical) {
            progressLength = h - srcLength;
            x = w / 2;
            y = progressLength - progressLength * progress / max + srcLength;
        } else {
            progressLength = w - srcLength;
            x = progressLength * progress / max + srcLength;
            y = h / 2;
        }
    }

    /**
     * 从矢量图获取 Bitmap
     */
    public Bitmap getBitmapFromVectorDrawable(int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(getContext(), drawableId);
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (isDrag) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    isPressed = true;
                    postInvalidate();
                    if (listener != null) {
                        listener.onPressDown(v, progress);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    isPressed = false;
                    postInvalidate();
                    if (listener != null) {
                        listener.onPutUp(v, progress);
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (isVertical) {
                        if (event.getY() > srcLength / 2 && event.getY() < getHeight() - srcLength / 2) {
                            y = (int) event.getY();
                            x = getWidth() / 2;
                            postInvalidate();
                            progress = (int) ((getHeight() - srcLength / 2 - y) * 1.0 / progressLength * max);
                        }
                    } else {
                        if (event.getX() > srcLength / 2 && event.getX() < getWidth() - srcLength / 2) {
                            x = (int) event.getX();
                            y = getHeight() / 2;
                            postInvalidate();
                            progress = (int) ((x - srcLength / 2) * 1.0 / progressLength * max);
                        }
                    }
                    if (listener != null) {
                        listener.onMove(v, progress);
                    }
                    break;
            }
        }
        return true;
    }

    public void setOnProgressChangeListener(OnProgressChangeListener listener) {
        this.listener = listener;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
        postInvalidate();
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public boolean isDrag() {
        return isDrag;
    }

    public void setDrag(boolean drag) {
        isDrag = drag;
    }

    /**
     * 进度条回调
     */
    public interface OnProgressChangeListener {
        //按下
        void onPressDown(View view, int progress);

        //拖动
        void onMove(View view, int progress);

        //抬起
        void onPutUp(View view, int progress);
    }
}
