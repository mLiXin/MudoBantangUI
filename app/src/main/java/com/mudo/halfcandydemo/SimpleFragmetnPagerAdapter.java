package com.mudo.halfcandydemo;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by MarvelY on 2017/1/7 15:39
 */

public class SimpleFragmetnPagerAdapter extends FragmentPagerAdapter {

    private String tabTitles[] = new String[]{"推荐", "最新", "热门", "礼物", "美食", "生活", "设计感", "家居", "数码", "阅读", "学生党", "上班族", "美妆", "护理", "运动户外", "健康"};
    private Context context;

    public SimpleFragmetnPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        return PageFragment.newInstance(position, tabTitles[position]);
    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
