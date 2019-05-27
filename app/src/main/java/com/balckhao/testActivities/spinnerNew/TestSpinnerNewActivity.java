package com.balckhao.testActivities.spinnerNew;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.balckhao.blackhaocustomview.R;

import java.util.ArrayList;

/**
 * Author ： BlackHao
 * Time : 2019/3/1 16:51
 * Description :
 */
public class TestSpinnerNewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spinner_view_new);
        TestSpinnerView testSpinnerView1 = (TestSpinnerView) findViewById(R.id.test_spinner_1);
        TestSpinnerView testSpinnerView2 = (TestSpinnerView) findViewById(R.id.test_spinner_2);
        //
        testSpinnerView1.isNeedAdd = true;
        testSpinnerView2.isNeedSetButton = true;
        ArrayList<TestBean> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(new TestBean(i, "Name : " + i, System.currentTimeMillis() / 1000));
        }
        testSpinnerView1.setData(list);
        testSpinnerView2.setData(list);
    }
}
