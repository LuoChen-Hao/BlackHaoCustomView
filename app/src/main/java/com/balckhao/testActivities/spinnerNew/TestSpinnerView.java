package com.balckhao.testActivities.spinnerNew;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.balckhao.blackhaocustomview.spinnerViewNew.SpinnerView;

/**
 * Author ： BlackHao
 * Time : 2019/3/1 16:49
 * Description :
 */
public class TestSpinnerView extends SpinnerView<TestBean> {

    public TestSpinnerView(Context context) {
        super(context);
    }

    public TestSpinnerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TestSpinnerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public String getDataString(int pos) {
        return getItem(pos).getName() + "---" + getItem(pos).getId();
    }
}
