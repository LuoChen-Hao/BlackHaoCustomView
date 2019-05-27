package com.balckhao.blackhaocustomview.spinnerViewNew;

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
import android.support.annotation.RequiresApi;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.balckhao.blackhaocustomview.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Author ： BlackHao
 * Time : 2018/2/23 13:54
 * Description : 自定义 Spinner
 */

public abstract class SpinnerView<T> extends View implements View.OnTouchListener {

    //数据源
    private ArrayList<T> data;
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
    //设置按钮按下Bitmap
    private Bitmap setBitmap;
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
    //    //输入框长度
//    private int inputLen;
    //标题
    private String title;
    //    //标题颜色
//    private int titleColor;
//    //标题大小
//    private int titleSize;
    //画笔
    private Paint paint;
    //是否按下标识
    private boolean isPressed = false;
    //
    private Rect rect;
    //选择框
    private PopupWindow pop;
    //是否需要显示设置按钮
    public boolean isNeedSetButton = false;
    //是否可点击
    public boolean isClickable = true;
    //是否需要点击新增
    public boolean isNeedAdd = false;
    //回调
    private OnItemSelectImpl impl;
    //设置按钮位置
    private Rect setRect;
    //List adapter
    private ListAdapter arrayAdapter;
    //不可点击item下标列表
    private List<Integer> unClickList = new ArrayList<>();

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
            if (attr == R.styleable.SpinnerView_pullDownPressedSrc) {//下拉按钮按下资源ID
                pullDownPressedSrc = a.getResourceId(attr, R.drawable.spinner_pressed);

            } else if (attr == R.styleable.SpinnerView_pullDownSrc) {//下拉按钮资源ID
                pullDownSrc = a.getResourceId(attr, R.drawable.spinner_normal);

            } else if (attr == R.styleable.SpinnerView_textSize) {// 默认设置为16sp，TypeValue也可以把sp转化为px
                textSize = a.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics()));

            } else if (attr == R.styleable.SpinnerView_textColor) {//字体颜色
                textColor = a.getColor(attr, Color.BLUE);

            } else if (attr == R.styleable.SpinnerView_borderWidth) {// 默认设置为2px，TypeValue也可以把sp转化为px
                borderWidth = a.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_PX, 1, getResources().getDisplayMetrics()));

            } else if (attr == R.styleable.SpinnerView_borderColor) {//边框颜色
                borderColor = a.getColor(attr, Color.GRAY);

            } else if (attr == R.styleable.SpinnerView_srcPadding) {// 默认设置为2px，TypeValue也可以把sp转化为px
                srcPadding = a.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_PX, 2, getResources().getDisplayMetrics()));

            } else if (attr == R.styleable.SpinnerView_titleSize) {// 默认设置为16sp，TypeValue也可以把sp转化为px
//                titleSize = a.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(
//                        TypedValue.COMPLEX_UNIT_PX, 16, getResources().getDisplayMetrics()));

            } else if (attr == R.styleable.SpinnerView_titleColor) {//字体颜色
//                titleColor = a.getColor(attr, Color.BLUE);

            } else if (attr == R.styleable.SpinnerView_title) {//字体颜色
                title = a.getString(attr);

            } else if (attr == R.styleable.SpinnerView_inputLen) {
//                inputLen = a.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(
//                        TypedValue.COMPLEX_UNIT_PX, 50, getResources().getDisplayMetrics()));

            } else if (attr == R.styleable.SpinnerView_radius) {
                radius = a.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_PX, 5, getResources().getDisplayMetrics()));
            }
        }
        a.recycle();
        //初始化画笔
        paint = new Paint();
        paint.setAntiAlias(true);

        rect = new Rect();
        setRect = new Rect();
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
//        titleSize = 20;
//        titleColor = Color.BLACK;
        srcPadding = 2;
        sideLength = 20;
        borderWidth = 3;
        borderColor = Color.GRAY;
        pullDownPressedSrc = R.drawable.spinner_pressed;
        pullDownSrc = R.drawable.spinner_normal;
//        inputLen = 50;
        radius = 0;
        data = new ArrayList<>();
        //
        setBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ui_set_model);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //输入框以外的长度
        int otherLen = 0;
        //绘制标题
//        if (!title.equals("") && title.length() > 0) {
//            paint.setColor(titleColor);
//            paint.setTextSize(titleSize);
//            paint.setStyle(Paint.Style.FILL);
//            rect.set(10, 0, getTitleLength(title), getHeight());
//            drawTextOnRect(canvas, rect, title);
//            otherLen = getWidth() - inputLen;
//        } else {
//            otherLen = 0;
//        }
        //绘制边框
        paint.setStrokeWidth(borderWidth);
        paint.setColor(borderColor);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawRoundRect((float) (otherLen + borderWidth / 2.0), (float) (borderWidth / 2.0),
                (float) (getWidth() - borderWidth / 2.0), (float) (getHeight() - borderWidth / 2.0), radius, radius, paint);
        //绘制按钮
        sideLength = getHeight() - srcPadding * 2;
        rect.set(getWidth() - sideLength - srcPadding, srcPadding,
                getWidth() - srcPadding, getHeight() - srcPadding);
        if (isPressed) {
            canvas.drawBitmap(pullDownPressedBitmap, null, rect, paint);
        } else {
            canvas.drawBitmap(pullDownBitmap, null, rect, paint);
        }
        //绘制设置按钮
        if (isNeedSetButton) {
            sideLength = getHeight() - srcPadding * 2;
            //结束位置在按下按钮的左侧
            int right = rect.left;
            setRect.set(right - sideLength - srcPadding, srcPadding,
                    right - srcPadding, getHeight() - srcPadding);
            canvas.drawBitmap(setBitmap, null, setRect, paint);
        }
        //绘制选中的文字
        paint.setStyle(Paint.Style.FILL);
        if (data != null && data.size() > 0 && showStringIndex >= 0
                && data.size() > showStringIndex) {
            paint.setColor(textColor);
            paint.setTextSize(textSize);
            int length = computeMaxStringWidth(getDataString(showStringIndex), paint);
            rect.set(10, 0, length + 10, getHeight());
            drawTextOnRect(canvas, rect, getDataString(showStringIndex));
        }
    }

    /**
     * 根据数据位置返回String
     */
    public abstract String getDataString(int pos);

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

    //设置数据源
    public void setData(List<T> data) {
        this.data.clear();
        this.data.addAll(data);
        postInvalidate();
        if (arrayAdapter != null) {
            arrayAdapter.setList(data);
        }
    }

    //设置不可点击的item

    public void setUnClickList(List<Integer> unClickList) {
        this.unClickList = unClickList;
    }

    //设置当前显示的数据在数据源的下标
    public void setSelectPos(int position) {
        this.showStringIndex = position;
        if (this.showStringIndex < getItemCount()) {
            //回调
            if (impl != null) {
                impl.onItemSelect(this, position);
            }
        } else {
            this.showStringIndex = -1;
        }
        postInvalidate();

    }

    public void setOnItemSelectImpl(OnItemSelectImpl impl) {
        this.impl = impl;
    }

    public T getItem(int pos) {
        return data.get(pos);
    }

    public int getItemCount() {
        return data.size();
    }

    /**
     * 获取当前显示的数据位置
     */
    public int getSelectPos() {
        return showStringIndex;
    }

    //初始化popUpWindow
    private void initPop() {
        //显示ratioPop
        View view = LayoutInflater.from(getContext()).inflate(R.layout.ui_spinner_pop_layout, null);
        //通过是否存在title判断pop的宽度
        int width = getWidth();
        //设置pop的宽度,数据长度大于5时，显示5个item的高度，否则显示所有item的高度
        int preHeight = dp2px(getContext(), 50);
        int size = isNeedAdd ? data.size() - 1 : data.size();
        int height = size > 5 ? 5 * preHeight : preHeight * size;
        pop = new PopupWindow(view, width, height);
        ListView listView = (ListView) view.findViewById(R.id.ui_spinner_list_view);
        arrayAdapter = new ListAdapter(data);
        //是否需要新增按钮
        if (isNeedAdd) {
            View header = View.inflate(getContext(), R.layout.header_list, null);
            listView.addHeaderView(header);
            header.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (impl != null) {
                        impl.onAddNew(SpinnerView.this);
                    }
                    pop.dismiss();
                }
            });
        }
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (isNeedAdd) {
                    //如果存在新增header view,那么对应的position需要减1
                    position = position - 1;
                }
                if (isUnClickedItem(position)) {
                    Toast.makeText(getContext(), "不可选择该项", Toast.LENGTH_SHORT).show();
                    return;
                }
                showStringIndex = position;
                postInvalidate();
                pop.dismiss();
                if (impl != null) {
                    impl.onItemSelect(SpinnerView.this, position);
                }
            }
        });
        pop.setOutsideTouchable(true);
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
                pop.showAsDropDown(this, getWidth(), 0);
            } else {
                int xPos = -pop.getWidth() / 2 + pop.getWidth() / 2;
                pop.showAsDropDown(this, xPos, 0);
            }
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
                if (!isClickable) {
                    break;
                }
                //判断是否按下设置按钮
                int x = (int) event.getX();
                int y = (int) event.getY();
                if (isNeedSetButton
                        && x > setRect.left && x < setRect.right
                        && y > setRect.top && y < setRect.bottom) {
                    //点击设置按钮
                    if (impl != null) {
                        impl.onSetClick(this);
                    }
                } else {
                    isPressed = true;
                    postInvalidate();
                }

                break;
            case MotionEvent.ACTION_UP:
                if (!isClickable) {
                    break;
                }
                int upX = (int) event.getX();
                int upY = (int) event.getY();
                if (isNeedSetButton
                        && upX > setRect.left && upX < setRect.right
                        && upY > setRect.top && upY < setRect.bottom) {
                    //点击设置按钮
                    break;
                }
                isPressed = false;
                postInvalidate();
                showPop();
                break;
        }
        return true;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        //销毁bitmap
        if (pullDownBitmap != null && !pullDownBitmap.isRecycled()) {
            pullDownBitmap.recycle();
            pullDownBitmap = null;
        }
        if (pullDownPressedBitmap != null && !pullDownPressedBitmap.isRecycled()) {
            pullDownPressedBitmap.recycle();
            pullDownPressedBitmap = null;
        }
        if (setBitmap != null && !setBitmap.isRecycled()) {
            setBitmap.recycle();
            setBitmap = null;
        }
    }

    private class ListAdapter extends BaseAdapter {

        private List<T> list = new ArrayList<>();

        public ListAdapter(List<T> list) {
            this.list.addAll(list);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.ui_item_spinner_list,
                        parent, false);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.tv.setText(getDataString(position));
//            int pos = position;
//            //存在新增的header view,对应的
//            if (isNeedAdd) {
//                pos = pos + 1;
//            }
            convertView.setEnabled(isUnClickedItem(position));
            return convertView;
        }

        class ViewHolder {
            TextView tv;

            ViewHolder(View v) {
                this.tv = (TextView) v.findViewById(R.id.ui_tv_spinner_mode_title);
            }
        }

        public void setList(List<T> list) {
            if (pop != null) {
                //通过是否存在title判断pop的宽度
                int width = getWidth();
                //设置pop的宽度,数据长度大于5时，显示5个item的高度，否则显示所有item的高度
                int preHeight = dp2px(getContext(), 50);
                int size = isNeedAdd ? data.size() - 1 : data.size();
                int height = size > 5 ? 5 * preHeight : preHeight * size;
                pop.setWidth(width);
                pop.setHeight(height);
                pop.dismiss();
            }

            this.list.clear();
            this.list.addAll(list);
            notifyDataSetChanged();
        }
    }

    /**
     * 判断当前选项是否是不可点击选项
     */
    private boolean isUnClickedItem(int pos) {
        for (int i = 0; i < unClickList.size(); i++) {
            if (pos == unClickList.get(i)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 计算字符串长度
     */
    private int computeMaxStringWidth(String strings, Paint p) {
        float maxWidthF = 0.0f;
        float width = p.measureText(strings);
        maxWidthF = Math.max(width, maxWidthF);
        return (int) (maxWidthF + 0.5);
    }

    /**
     * 选中回调
     */
    public interface OnItemSelectImpl {
        /**
         * 选中 item
         */
        void onItemSelect(View view, int position);

        /**
         * 点击设置按钮
         */
        void onSetClick(View view);

        /**
         * 点击新增按钮
         */
        void onAddNew(View view);
    }
}
