package com.balckhao.blackhaocustomview.spinnerView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.balckhao.blackhaocustomview.R;

import java.util.ArrayList;

/**
 * Author ： BlackHao
 * Time : 2018/2/23 13:54
 * Description : 自定义 Spinner
 */

public class SpinnerView extends View implements View.OnTouchListener {

    //数据源
    private ArrayList<String> data;
    //需要显示的数据的下标
    private int showStringIndex = 0;
    //边框颜色
    private int borderColor;
    //边框宽度
    private int borderWidth;
    //下拉按钮资源ID
    private int pullDownSrc;
    //下拉按钮按下资源ID
    private int pullDownPressedSrc;
    //下拉按钮Bitmap
    private Bitmap pullDownBitmap;
    //下拉按钮按下Bitmap
    private Bitmap pullDownPressedBitmap;
    //padding
    private int srcPadding;
    //按钮显示的边长
    private int sideLength;
    //字体颜色
    private int textColor;
    //字体大小
    private int textSize;
    //边框圆角
    private int radius;
    //输入框长度
    private int inputLen;
    //标题
    private String title;
    //标题颜色
    private int titleColor;
    //标题大小
    private int titleSize;
    //画笔
    private Paint paint;
    //是否按下标识
    private boolean isPressed = false;
    //
    private Rect rect;
    //选择框
    private PopupWindow pop;


    public SpinnerView(Context context) {
        this(context, null, 0);
    }

    public SpinnerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SpinnerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //默认参数
        initDefaultValue();
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SpinnerView, defStyleAttr, 0);
        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.SpinnerView_pullDownPressedSrc:
                    //下拉按钮按下资源ID
                    pullDownPressedSrc = a.getResourceId(attr, R.drawable.spinner_pressed);
                    break;
                case R.styleable.SpinnerView_pullDownSrc:
                    //下拉按钮资源ID
                    pullDownSrc = a.getResourceId(attr, R.drawable.spinner_normal);
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
                case R.styleable.SpinnerView_borderWidth:
                    // 默认设置为2px，TypeValue也可以把sp转化为px
                    borderWidth = a.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics()));
                    break;
                case R.styleable.SpinnerView_borderColor:
                    //边框颜色
                    borderColor = a.getColor(attr, Color.GRAY);
                    break;
                case R.styleable.SpinnerView_srcPadding:
                    // 默认设置为2px，TypeValue也可以把sp转化为px
                    srcPadding = a.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics()));
                    break;
                case R.styleable.SpinnerView_titleSize:
                    // 默认设置为16sp，TypeValue也可以把sp转化为px
                    titleSize = a.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics()));
                    break;
                case R.styleable.SpinnerView_titleColor:
                    //字体颜色
                    titleColor = a.getColor(attr, Color.BLUE);
                    break;
                case R.styleable.SpinnerView_title:
                    //字体颜色
                    title = a.getString(attr);
                    break;
                case R.styleable.SpinnerView_inputLen:
                    inputLen = a.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics()));
                    break;
                case R.styleable.SpinnerView_radius:
                    radius = a.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics()));
                    break;
                default:
                    break;
            }
        }
        a.recycle();
        //初始化画笔
        paint = new Paint();
        paint.setAntiAlias(true);

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
        textSize = 20;
        textColor = Color.GRAY;
        title = "";
        titleSize = 20;
        titleColor = Color.BLACK;
        srcPadding = 2;
        sideLength = 20;
        borderWidth = 3;
        borderColor = Color.GRAY;
        pullDownPressedSrc = R.drawable.spinner_pressed;
        pullDownSrc = R.drawable.spinner_normal;
        inputLen = 50;
        radius = 0;
        data = new ArrayList<>();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //输入框以外的长度
        int otherLen;
        //绘制标题
        if (!title.equals("") && title.length() > 0) {
            paint.setColor(titleColor);
            paint.setTextSize(titleSize);
            paint.setStyle(Paint.Style.FILL);
            rect.set(10, 0, getTitleLength(title), getHeight());
            drawTextOnRect(canvas, rect, title);
            otherLen = getWidth() - inputLen;
        } else {
            otherLen = 0;
        }
        //绘制边框
        paint.setStrokeWidth(borderWidth);
        paint.setColor(borderColor);
        paint.setStyle(Paint.Style.STROKE);

        canvas.drawRoundRect(otherLen + borderWidth / 2, borderWidth / 2,
                getWidth() - borderWidth / 2, getHeight() - borderWidth / 2, radius, radius, paint);
        //绘制按钮
        sideLength = getHeight() - srcPadding * 2;
        rect.set(getWidth() - sideLength - srcPadding, srcPadding,
                getWidth() - srcPadding, getHeight() - srcPadding);
        if (isPressed) {
            canvas.drawBitmap(pullDownPressedBitmap, null, rect, paint);
        } else {
            canvas.drawBitmap(pullDownBitmap, null, rect, paint);
        }
        //绘制选中的文字
        paint.setStyle(Paint.Style.FILL);
        rect.set(otherLen, 0, getWidth() - sideLength, getHeight());
        if (data != null && data.size() > 0) {
            paint.setColor(textColor);
            paint.setTextSize(textSize);
            drawTextOnRect(canvas, rect, data.get(showStringIndex));
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
        Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
        // 获取baseLine
        int baseline = targetRect.top + (targetRect.bottom - targetRect.top - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
        // 下面这行是实现水平居中，drawText对应改为传入targetRect.centerX()
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(text, targetRect.centerX(), baseline, paint);
    }

    public int getTitleLength(String title) {
        Rect rect = new Rect();
        paint.getTextBounds(title, 0, title.length(), rect);
        return rect.width();//文本的宽度
    }

    //设置数据源
    public void setData(ArrayList<String> data) {
        this.data.clear();
        this.data.addAll(data);
        postInvalidate();
    }

    //设置当前显示的数据在数据源的下标
    public void setShowStringIndex(int showStringIndex) {
        this.showStringIndex = showStringIndex;
    }

    //初始化popUpWindow
    private void initPop() {
        //显示ratioPop
        View view = LayoutInflater.from(getContext()).inflate(R.layout.pop_layout, null);
        //通过是否存在title判断pop的宽度
        int width = (!title.equals("") && title.length() > 0) ? inputLen : getWidth();
        //设置pop的宽度,数据长度大于5时，显示5个item的高度，否则显示所有item的高度
        int height = data.size() > 5 ? dp2px(getContext(), 30) * 5 : dp2px(getContext(), 30) * data.size();
        pop = new PopupWindow(view, width - getHeight(), height);
        ListView listView = (ListView) view.findViewById(R.id.pop_list_view);
        ListAdapter arrayAdapter = new ListAdapter();
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showStringIndex = position;
                postInvalidate();
                pop.dismiss();
            }
        });
        pop.setOutsideTouchable(false);
    }

    //显示popUpWindow
    private void showPop() {
        if (pop == null) {
            initPop();
        }
        if (pop.isShowing()) {
            pop.dismiss();
        } else {
            if (!title.equals("") && title.length() > 0) {
                pop.showAsDropDown(this, getWidth() - inputLen, 0);
            } else {
                int xPos = -pop.getWidth() / 2 + pop.getWidth() / 2;
                pop.showAsDropDown(this, xPos, 0);
            }
        }
    }

    //返回选中的字符
    public String getText() {
        return data.get(showStringIndex);
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
        //设置按钮图片的边长
        setMeasuredDimension(width, height);
    }

    //从矢量图获取Bitmap
    public Bitmap getBitmapFromVectorDrawable(int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(getContext(), drawableId);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawable = (DrawableCompat.wrap(drawable)).mutate();
        }
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * 将dp转换为与之相等的px
     */
    public static int dp2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isPressed = true;
                postInvalidate();
                break;
            case MotionEvent.ACTION_UP:
                isPressed = false;
                postInvalidate();
                showPop();
                break;
        }
        return true;
    }

    private class ListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_spinner_list, parent, false);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.tv.setText(data.get(position));
            return convertView;
        }

        class ViewHolder {
            TextView tv;
            ViewHolder(View v) {
                this.tv = (TextView) v.findViewById(R.id.spinner_list_tv);
            }
        }
    }
}
