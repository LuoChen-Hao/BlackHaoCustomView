package com.balckhao.testActivities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.balckhao.blackhaocustomview.R;
import com.balckhao.blackhaocustomview.imgAvatarView.ImagesAvatarView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ImgGroupActivity extends Activity {

    @BindView(R.id.lv_avatar)
    ListView lvAvatar;

    private String[] urls = {"http://192.168.10.189:8700/g0/000/000/icon_1553656935880.png"
            , "http://192.168.10.189:8700/g0/000/000/icon_1553563285121.png"
            , "http://192.168.10.189:8700/g0/000/000/icon_1553508473399.png"
            , ""};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_img_group_view);
        ButterKnife.bind(this);
        ArrayList<List<String>> data1 = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            int random = new Random().nextInt(4) + 1;
            ArrayList<String> list = new ArrayList<>();
            for (int j = 0; j < random; j++) {
                int index = new Random().nextInt(3);
                list.add(urls[index]);
            }
            data1.add(list);
        }

        TestAdapter adapter = new TestAdapter(data1);
        lvAvatar.setAdapter(adapter);
    }


    private class TestAdapter extends BaseAdapter {
        private ArrayList<List<String>> list;

        TestAdapter(ArrayList<List<String>> list) {
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public List<String> getItem(int position) {
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
                convertView = View.inflate(getBaseContext(), R.layout.adapter_img, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.tv.setText("position : " + position + " urls.size : " + getItem(position).size());
            holder.iv.setImageUrls(getItem(position));
            return convertView;
        }

        private class ViewHolder {
            ImagesAvatarView iv;
            TextView tv;

            public ViewHolder(View v) {
                this.iv = (ImagesAvatarView) v.findViewById(R.id.img_group);
                this.tv = (TextView) v.findViewById(R.id.tv);
            }
        }
    }


}
