package com.qun.test.slidermenu;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.qun.lib.slidermenu.SliderMenu;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private SliderMenu mSliderMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        mSliderMenu = findViewById(R.id.slider_menu);
        findViewById(R.id.btn1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSliderMenu.getState() == SliderMenu.SliderMenuState.CLOSE) {
                    mSliderMenu.changedState(SliderMenu.SliderMenuState.OPEN);
                } else {
                    mSliderMenu.changedState(SliderMenu.SliderMenuState.CLOSE);
                }
            }
        });
        ListView listView = findViewById(R.id.lv);
        listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, getData()));
        mSliderMenu.setCanSlider(true);

        ListView listView2 = findViewById(R.id.lv2);
        listView2.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, getData()));
    }

    public List<String> getData() {
        List<String> data = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            data.add("这是第" + i + "个条目");
        }
        return data;
    }
}
