package com.balckhao.blackhaocustomview.pressView;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.balckhao.blackhaocustomview.R;
import com.balckhao.blackhaocustomview.util.DensityUtil;

/**
 * Author ： BlackHao
 * Time : 2019/4/18 14:03
 * Description : 自定义录音按钮布局界面
 */
public class PressedView extends View implements View.OnTouchListener {

    private int normalRes;
    private String normalText = "";
    private int pressedRes;
    private String pressedText = "";
    //
    private Paint paint;
    private Rect rect;
    //当前是否是按下状态
    private boolean isPressed = false;
    //
    private PressCallback callback;
    //按下的位置y坐标
    private int pressedY = 0;
    //当前是否是outSize
    private boolean isOutSize = false;
    //字体dp大小
    private static int TEXT_SIZE = 20;
    //对话框相关
    private Dialog soundVolumeDialog = null;
    //音量图片
    private ImageView soundVolumeImg = null;
    //对话框背景
    private RelativeLayout soundVolumeLayout = null;

    public PressedView(Context context) {
        super(context);
        init();
    }

    public PressedView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PressedView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        //
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTextSize(DensityUtil.dip2px(getContext(), TEXT_SIZE));
        paint.setColor(Color.WHITE);
        rect = new Rect();
        //
        normalRes = R.drawable.blue_btn_bk;
        normalText = "按住 说话";
        pressedRes = R.drawable.red_btn_bk;
        pressedText = "松开 结束";
        //
        setOnTouchListener(this);
        //
        initSoundVolumeDlg();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        rect.set(0, 0, getWidth(), getHeight());
        if (!isPressed) {
            setBackgroundResource(normalRes);
            drawTextOnRect(canvas, rect, normalText);
        } else {
            setBackgroundResource(pressedRes);
            drawTextOnRect(canvas, rect, pressedText);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                pressedY = (int) event.getRawY();
                isOutSize = false;
                if (!isPressed) {
                    isPressed = true;
                    postInvalidate();
                    if (callback != null) {
                        //回调
                        callback.onStartRecord();
                        //按下，弹出对话框
                        soundVolumeImg.setImageResource(R.mipmap.sound_volume_01);
                        soundVolumeImg.setVisibility(View.VISIBLE);
                        soundVolumeLayout.setBackgroundResource(R.mipmap.sound_volume_default_bk);
                        soundVolumeDialog.show();
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if (isPressed) {
                    isPressed = false;
                    postInvalidate();
                    if (callback != null) {
                        int upY = (int) event.getRawY();
                        if (pressedY - upY < getHeight()) {
                            //录音结束
                            if (soundVolumeDialog.isShowing()) {
                                soundVolumeDialog.dismiss();
                            }
                            callback.onStopRecord();
                        } else {
                            //录音取消
                            if (soundVolumeDialog.isShowing()) {
                                soundVolumeDialog.dismiss();
                            }
                            callback.onCancelRecord();
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (isPressed && callback != null) {
                    int upY = (int) event.getRawY();
                    if (pressedY - upY < getHeight()) {
                        if (isOutSize) {
                            isOutSize = false;
                            soundVolumeLayout.setBackgroundResource(R.mipmap.sound_volume_default_bk);
                        }
                    } else {
                        if (!isOutSize) {
                            isOutSize = true;
                            soundVolumeLayout.setBackgroundResource(R.mipmap.sound_volume_cancel_bk);
                        }
                    }
                }
                break;
        }
        return true;
    }

    public void setCallback(PressCallback callback) {
        this.callback = callback;
    }

    public interface PressCallback {

        //开始录音
        void onStartRecord();

        //停止录音
        void onStopRecord();

        //取消录音
        void onCancelRecord();
    }

    /**
     * 在指定矩形中间 drawText
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

    /**
     * 初始化音量信息对话框
     */
    private void initSoundVolumeDlg() {
        soundVolumeDialog = new Dialog(getContext(), R.style.SoundVolumeStyle);
        soundVolumeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        soundVolumeDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        soundVolumeDialog.setContentView(R.layout.tt_sound_volume_dialog);
        soundVolumeDialog.setCanceledOnTouchOutside(true);
        soundVolumeImg = (ImageView) soundVolumeDialog.findViewById(R.id.sound_volume_img);
        soundVolumeLayout = (RelativeLayout) soundVolumeDialog.findViewById(R.id.sound_volume_bk);
    }

    /**
     * 根据分贝值设置录音时的音量动画
     */
    public void setVolume(int voiceValue) {
        if (voiceValue < 200.0) {
            soundVolumeImg.setImageResource(R.mipmap.sound_volume_01);
        } else if (voiceValue > 200.0 && voiceValue < 600) {
            soundVolumeImg.setImageResource(R.mipmap.sound_volume_02);
        } else if (voiceValue > 600.0 && voiceValue < 1200) {
            soundVolumeImg.setImageResource(R.mipmap.sound_volume_03);
        } else if (voiceValue > 1200.0 && voiceValue < 2400) {
            soundVolumeImg.setImageResource(R.mipmap.sound_volume_04);
        } else if (voiceValue > 2400.0 && voiceValue < 10000) {
            soundVolumeImg.setImageResource(R.mipmap.sound_volume_05);
        } else if (voiceValue > 10000.0 && voiceValue < 28000.0) {
            soundVolumeImg.setImageResource(R.mipmap.sound_volume_06);
        } else if (voiceValue > 28000.0) {
            soundVolumeImg.setImageResource(R.mipmap.sound_volume_07);
        }
    }

}
