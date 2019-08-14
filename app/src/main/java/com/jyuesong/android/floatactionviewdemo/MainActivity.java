package com.jyuesong.android.floatactionviewdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.jyuesong.android.floatactionview.FloatActionView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FloatActionView actionView = findViewById(R.id.actionview);
        actionView.setMainButtonIcon(R.drawable.ic_add_black_24dp);
        actionView.setOnClick(new FloatActionView.OnClick() {
            @Override
            public void positionClicked(int position) {

            }

            @Override
            public void mainClicked() {

            }

            @Override
            public void dismissed() {
            }
        });
        List<String> tips = new ArrayList<>();

        tips.add("从其他项目复制");
        tips.add("导入工作");
        tips.add("手动创建");

        List<Integer> images = new ArrayList<>();
        images.add(R.mipmap.sendtoclouds);
        images.add(R.mipmap.sendtocontact);
        images.add(R.mipmap.working_notice_bar);

        actionView.setData(tips, images);

    }
}
