package com.wyf;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.wyf.view.ViewUtils;
import com.wyf.view.annotation.BindContent;
import com.wyf.view.annotation.BindView;
import com.wyf.view.annotation.OnClick;
import com.wyf.view.annotation.OnLongClick;

/**
 * Created by wyf on 2017/8/19.
 */
@BindContent(R.layout.activity_sample)
public class SampleActivity extends AppCompatActivity {

    @BindView(R.id.tv)
    private TextView tv;
    @BindView(R.id.vp)
    private ViewPager vp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtils.inject(this);
        vp.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return new SampleFragment();
            }

            @Override
            public int getCount() {
                return 1;
            }
        });
    }

    @OnClick(R.id.btn)
    void onClick(View v) {
        tv.append("onClick->");
    }

    @OnLongClick(R.id.btn)
    boolean onLongClick(View v) {
        tv.append("onLongClick->");
        return true;
    }

}
