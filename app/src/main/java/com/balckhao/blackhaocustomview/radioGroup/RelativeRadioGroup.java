package com.balckhao.blackhaocustomview.radioGroup;

import android.content.Context;
import android.support.annotation.IdRes;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

/**
 * Author ： BlackHao
 * Time : 2018/10/26 10:46
 * Description : 自定义 RadioGroup
 */
public class RelativeRadioGroup extends RelativeLayout implements CompoundButton.OnCheckedChangeListener {

    private int checkId = -1;

    private CompoundButton.OnCheckedChangeListener mChildOnCheckedChangeListener;

    public RelativeRadioGroup(Context context) {
        super(context);
    }

    public RelativeRadioGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RelativeRadioGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        //添加监听
        for (int i = 0; i < getChildCount(); i++) {
            View v = getChildAt(i);
            if (v instanceof RadioButton && !(v instanceof CompoundButton.OnCheckedChangeListener)) {
                ((RadioButton) v).setOnCheckedChangeListener(this);
            }
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        setCheck(buttonView.getId(), isChecked);
    }

    public void check(@IdRes int checkId) {
        if (checkId == -1 || this.checkId == checkId) {
            return;
        }

        setCheck(checkId, true);

    }

    public void clearCheck() {
        setCheck(-1, false);
    }

    public int getCheckedRadioButtonId() {
        return this.checkId;
    }

    /**
     * 设置选中状态
     */
    private void setCheck(@IdRes int checkId, boolean isChecked) {
        if (checkId != -1 && this.checkId == checkId) {
            return;
        }

        if (checkId != -1) {
            CompoundButton view = (CompoundButton) findViewById(checkId);
            //未选中的RadioButton被选中
            if (checkId != this.checkId && isChecked) {
                this.checkId = checkId;
                if (mChildOnCheckedChangeListener != null) {
                    mChildOnCheckedChangeListener.onCheckedChanged(view, true);
                }
                //某个RadioButton被选中，将其他的改为未选中
                for (int i = 0; i < getChildCount(); i++) {
                    View v = getChildAt(i);
                    if (v instanceof RadioButton && v.getId() != checkId) {
                        ((RadioButton) v).setChecked(false);
                    } else if (v instanceof RadioButton && v.getId() == checkId) {
                        ((RadioButton) v).setChecked(true);
                    }
                }
            }
            //被选中的RadioButton被取消选中
            if (checkId == this.checkId && !isChecked) {
                this.checkId = checkId;
                if (mChildOnCheckedChangeListener != null) {
                    mChildOnCheckedChangeListener.onCheckedChanged(view, false);
                }
            }

        } else {
            //清空所有选择
            if (this.checkId != -1) {
                this.checkId = -1;
                CompoundButton view = (CompoundButton) findViewById(this.checkId);
                //将选中的置为未选中
                if (view instanceof RadioButton) {
                    view.setChecked(false);
                }
            }
        }
    }

    public void setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener mChildOnCheckedChangeListener) {
        this.mChildOnCheckedChangeListener = mChildOnCheckedChangeListener;
    }
}
