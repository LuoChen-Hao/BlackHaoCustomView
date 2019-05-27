package com.balckhao.blackhaocustomview.imgAvatarView;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.balckhao.blackhaocustomview.R;
import com.balckhao.blackhaocustomview.util.ImgLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Author ： BlackHao
 * Time : 2019/2/26 09:29
 * Description :
 */
public class ImagesAvatarView extends ViewGroup {

    //图片链接
    private List<String> imageUrls = new ArrayList<>();

    public ImagesAvatarView(Context context) {
        this(context, null, 0);
    }

    public ImagesAvatarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImagesAvatarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int size = getChildCount();
        //通过图片数量设置背景色
        if (size == 1) {
            setBackgroundColor(Color.TRANSPARENT);
        } else {
            setBackgroundColor(Color.parseColor("#e0e0e0"));
        }
        if (size == 1) {
            //只有一个ImageView
            ImageView imageView = (ImageView) getChildAt(0);
            imageView.layout(0, 0, getWidth(), getHeight());
        } else if (size == 2) {
            //存在两个ImageView，分左上/右下显示
            ImageView imageView1 = (ImageView) getChildAt(0);
            imageView1.layout(0, 0, getWidth() / 2 - 1, getHeight() / 2 - 1);

            ImageView imageView2 = (ImageView) getChildAt(1);
            imageView2.layout(getWidth() / 2 + 1, getHeight() / 2 + 1, getWidth(), getHeight());
        } else if (size == 3) {
            //存在三个ImageView，四分格显示
            ImageView imageView1 = (ImageView) getChildAt(0);
            imageView1.layout(getWidth() / 2 - getWidth() / 4, 0, getWidth() / 2 + getWidth() / 4, getHeight() / 2 - 1);

            ImageView imageView2 = (ImageView) getChildAt(1);
            imageView2.layout(0, getHeight() / 2 + 1, getWidth() / 2 - 1, getHeight());

            ImageView imageView3 = (ImageView) getChildAt(2);
            imageView3.layout(getWidth() / 2 + 1, getHeight() / 2 + 1, getWidth(), getHeight());
        } else {
            //最多显示四个
            ImageView imageView1 = (ImageView) getChildAt(0);
            imageView1.layout(0, 0, getWidth() / 2 - 1, getHeight() / 2 - 1);

            ImageView imageView2 = (ImageView) getChildAt(1);
            imageView2.layout(getWidth() / 2 + 1, 0, getWidth(), getHeight() / 2 - 1);

            ImageView imageView3 = (ImageView) getChildAt(2);
            imageView3.layout(0, getHeight() / 2 + 1, getWidth() / 2 - 1, getHeight());

            ImageView imageView4 = (ImageView) getChildAt(3);
            imageView4.layout(getWidth() / 2 + 1, getHeight() / 2 + 1, getWidth(), getHeight());
        }
        //显示图片
        showImg();
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
        //宽高必须一致
        if (width > height) {
            width = height;
        } else if (width < height) {
            height = width;
        }
        setMeasuredDimension(width, height);
    }

    public void setImageUrls(List<String> imageUrls) {
        if (imageUrls == null || imageUrls.size() == 0) {
            if (imageUrls == null) {
                imageUrls = new ArrayList<>();
            }
            imageUrls.add("drawable://" + R.drawable.wsq_default_avatar);
        }
        //child imageView 数量是否发生改变(如果改变则需要removeAll,重新添加)
        boolean isChanged = this.imageUrls.size() != imageUrls.size();
        this.imageUrls.clear();
        this.imageUrls.addAll(imageUrls);
        if (isChanged) {
            modifyChild();
            //重新布局
            requestLayout();
        } else {
            showImg();
        }
    }

    /**
     * 设置显示图片
     */
    private void showImg() {
        //显示图片
        int count = getChildCount();
        count = count > 4 ? 4 : count;
        for (int i = 0; i < count; i++) {
            ImgLoader.display((ImageView) getChildAt(i), this.imageUrls.get(i));
        }
    }

    private void modifyChild() {
        //移除所有的子控件
        removeAllViews();
        //根据图片数量增加对应的图片
        int size = this.imageUrls.size();
        for (int i = 0; i < size; i++) {
            ImageView imageView = new ImageView(getContext());
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            addView(imageView);
        }
    }
}
