package com.balckhao.testActivities;

import android.app.Activity;
import android.os.Bundle;

import com.balckhao.blackhaocustomview.R;
import com.balckhao.blackhaocustomview.timeTableView.ClassTime;
import com.balckhao.blackhaocustomview.timeTableView.TimeTableView;

import java.util.ArrayList;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TableTimeActivity extends Activity {

    @BindView(R.id.table_time_view)
    TimeTableView tableTimeView;
    ArrayList<String> classes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_time_view);
        ButterKnife.bind(this);
        classes.add("语文");
        classes.add("数学");
        classes.add("英语");
        classes.add("物理");
        classes.add("化学");
        classes.add("生物");
        classes.add("体育");
        ArrayList<ClassTime> classTimes = new ArrayList<>();
        classTimes.add(new ClassTime(8, 30, 9, 20));
        classTimes.add(new ClassTime(9, 30, 10, 20));
        classTimes.add(new ClassTime(10, 30, 11, 20));
        classTimes.add(new ClassTime(1, 30, 2, 20));
        classTimes.add(new ClassTime(2, 30, 3, 20));
        classTimes.add(new ClassTime(3, 30, 4, 20));
        tableTimeView.setAllClasses(classTimes, getRandomList(6), getRandomList(6),
                getRandomList(6), getRandomList(6), getRandomList(6));
    }

    private ArrayList<String> getRandomList(int len) {
        ArrayList<String> data = new ArrayList<>();
        int randomMax = classes.size();
        Random random = new Random();
        for (int i = 0; i < len; i++) {
            int num = random.nextInt(randomMax);
            data.add(classes.get(num));
        }
        return data;
    }
}
