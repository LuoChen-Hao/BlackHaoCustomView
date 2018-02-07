package com.balckhao.testActivities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.balckhao.blackhaocustomview.R;
import com.balckhao.blackhaocustomview.segmentViews.SegmentControlView;
import com.balckhao.blackhaocustomview.segmentViews.SegmentViewItemClickCallBack;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SegmentActivity extends Activity implements SegmentViewItemClickCallBack {

    @BindView(R.id.segment_view_1)
    SegmentControlView segmentView1;
    @BindView(R.id.segment_view_2)
    SegmentControlView segmentView2;
    @BindView(R.id.result_tv)
    TextView resultTv;
    @BindView(R.id.segment_view_3)
    SegmentControlView segmentView3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_segement);
        ButterKnife.bind(this);
        initSegmentViews();
    }

    private void initSegmentViews() {
        segmentView1.setValues(new String[]{"首页", "推荐", "好友", "我的"});
        segmentView1.setOnItemClick(this);
        segmentView2.setValues(new String[]{"GAME", "BALL"});
        segmentView2.setOnItemClick(this);
        segmentView3.setValues(new String[]{"首页", "推荐", "好友", "我的"});
        segmentView3.setOnItemClick(this);
    }

    @Override
    public void onItemClick(View view, int selected) {
        switch (view.getId()) {
            case R.id.segment_view_1:
                resultTv.setText("segmentView1 ：" + segmentView1.getValues().get(selected));
                break;
            case R.id.segment_view_2:
                resultTv.setText("segmentView2 ：" + segmentView2.getValues().get(selected));
                break;
            case R.id.segment_view_3:
                resultTv.setText("segmentView3 ：" + segmentView3.getValues().get(selected));
                break;
        }
    }

}
