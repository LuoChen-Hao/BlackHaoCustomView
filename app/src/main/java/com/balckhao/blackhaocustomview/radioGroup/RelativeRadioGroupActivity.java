package com.balckhao.blackhaocustomview.radioGroup;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.balckhao.blackhaocustomview.R;

public class RelativeRadioGroupActivity extends AppCompatActivity {
    RelativeRadioGroup radioGroup;
    TextView tvShowSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relative_radio_group);

        radioGroup = (RelativeRadioGroup) findViewById(R.id.rg_test);
        tvShowSelected = (TextView) findViewById(R.id.tv_show_select);
        radioGroup.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                switch (buttonView.getId()) {
                    case R.id.cb_1:
                        tvShowSelected.setText("Test 1 is Selected");
                        break;
                    case R.id.cb_2:
                        tvShowSelected.setText("Test 2 is Selected");
                        break;
                    case R.id.cb_3:
                        tvShowSelected.setText("Test 3 is Selected");
                        break;
                    case R.id.cb_4:
                        tvShowSelected.setText("Test 4 is Selected");
                        break;
                    case R.id.cb_5:
                        tvShowSelected.setText("Test 5 is Selected");
                        break;
                }
            }
        });

    }

    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bt_get_check_id:
                tvShowSelected.setText("Selected ID IS "+radioGroup.getCheckedRadioButtonId());
                break;
            case R.id.bt_clear_check:
                radioGroup.clearCheck();
                break;
        }
    }
}
