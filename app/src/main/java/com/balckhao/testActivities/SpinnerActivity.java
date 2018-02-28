package com.balckhao.testActivities;

import android.app.Activity;
import android.os.Bundle;

import com.balckhao.blackhaocustomview.R;
import com.balckhao.blackhaocustomview.spinnerView.SpinnerView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SpinnerActivity extends Activity {

    @BindView(R.id.spinner_view_1)
    SpinnerView spinnerView1;
    @BindView(R.id.spinner_view_2)
    SpinnerView spinnerView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spinner_view);
        ButterKnife.bind(this);
        ArrayList<String> data1 = new ArrayList<>();
        data1.add("Test1");
        data1.add("Test2");
        data1.add("Test3");
        data1.add("Test4");
        data1.add("Test5");
        data1.add("Test6");
        spinnerView1.setData(data1);

        ArrayList<String> data2 = new ArrayList<>();
        data2.add("Data1");
        data2.add("Data2");
        data2.add("Data3");
        data2.add("Data4");
        spinnerView2.setData(data2);
        spinnerView2.setShowStringIndex(2);
    }

}
