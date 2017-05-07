package com.mudo.halfcandydemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.panxw.android.imageindicator.AutoPlayManager;
import com.panxw.android.imageindicator.ImageIndicatorView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private Fruit[] fruits = {new Fruit("Apple", R.mipmap.apple),
            new Fruit("banana", R.mipmap.banana),
            new Fruit("orange", R.mipmap.orange),
            new Fruit("watermelon", R.mipmap.watermelon),
            new Fruit("pear", R.mipmap.pear),
            new Fruit("grape", R.mipmap.grape),
            new Fruit("pineapple", R.mipmap.pineapple),
            new Fruit("strawberry", R.mipmap.strawberry),
            new Fruit("cherry", R.mipmap.cherry),
            new Fruit("mango", R.mipmap.mango)
    };
    private List<Fruit> fruitList = new ArrayList<>();
    private AppBarLayout appBarLayout;
    private Toolbar toolbar_default;
    private Toolbar toolbar_nono;

    boolean flag_defaulttono = true;
    private ImageView iv_defalut_left;
    private ImageView iv_default_right;
    private EditText et_nono_left;
    private ImageView iv_nono_right;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initFruits();
        initView();
        initData();
        updateView();
    }

    private void initFruits() {
        fruitList.clear();
        for (int i = 0; i < 50; i++) {
            Random random = new Random();
            int index = random.nextInt(fruits.length);
            fruitList.add(fruits[index]);
        }
    }

    private void initView() {
        // 第三方banner

        ImageCycleView banner = (ImageCycleView) findViewById(R.id.banner);
        ArrayList<Integer> resArray = new ArrayList<>();
        resArray.add(R.mipmap.ic_one);
        resArray.add(R.mipmap.ic_two);
        resArray.add(R.mipmap.ic_three);
        resArray.add(R.mipmap.ic_four);
        resArray.add(R.mipmap.ic_five);
        banner.setImageResources(resArray);
        banner.startImageCycle();

        // 其他控件
        mTabLayout = (TabLayout) findViewById(R.id.tl_main_tabtop);
        mViewPager = (ViewPager) findViewById(R.id.vp_main_page);

        appBarLayout = (AppBarLayout) findViewById(R.id.appbarlayout);
        toolbar_default = (Toolbar) findViewById(R.id.toolbar_default);
        toolbar_nono = (Toolbar) findViewById(R.id.toolbar_nono);

        iv_defalut_left = (ImageView) findViewById(R.id.iv_default_left);
        iv_default_right = (ImageView) findViewById(R.id.iv_default_right);

        et_nono_left = (EditText) findViewById(R.id.et_nono_left);
        iv_nono_right = (ImageView) findViewById(R.id.iv_nono_right);

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                //通过vertical的偏移值除以totalScrollRange来获取到百分比
                float progress = (float) (((int) (Math.abs(verticalOffset) / (appBarLayout.getTotalScrollRange() * 0.01))) * 0.01);

                if (verticalOffset == 0) {
                    // 展开状态 progress为0，展开状态的toolbar可见

                    toolbar_default.setVisibility(View.VISIBLE);
                    toolbar_nono.setVisibility(View.GONE);
                    toolbar_default.getBackground().setAlpha(255);
                    iv_defalut_left.getBackground().setAlpha(255);
                    iv_default_right.getBackground().setAlpha(255);

                    toolbar_nono.getBackground().setAlpha(255);

                } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
                    //折叠状态 progress为1，折叠状态的toolbar可见

                    toolbar_default.setVisibility(View.GONE);
                    toolbar_nono.setVisibility(View.VISIBLE);
                    toolbar_default.getBackground().setAlpha(255);

                    toolbar_nono.getBackground().setAlpha(255);
                    et_nono_left.getBackground().setAlpha(255);
                    iv_nono_right.getBackground().setAlpha(255);

                } else {

                    //中间状态，两个toolbar是重叠在一起的，根据是从折叠到展开，还是从展开到折叠，同时加上进度progress确定两个toolbar的透明比

                    if (toolbar_nono.getVisibility() == View.GONE) {// 折叠状态的toolbar不可见，则是从展开到折叠变化
                        flag_defaulttono = true;
                    } else if (toolbar_default.getVisibility() == View.GONE) {// 展开状态的toolbar不可见，则是从折叠到展开变化，为什么？因为只有到0的时候可见性才会变成gone，其他都是可见或改变透明度。
                        flag_defaulttono = false;
                    }// 以上两种情况都不是的话，则不需要改变flag_defaluttono的值

                    toolbar_default.setVisibility(View.VISIBLE);
                    toolbar_nono.setVisibility(View.VISIBLE);//

                    if (flag_defaulttono) { // 0到1变换
                        toolbar_default.getBackground().setAlpha((int) ((1 - progress) * 255));
                        iv_defalut_left.getBackground().setAlpha((int) ((1 - progress) * 255));
                        iv_default_right.getBackground().setAlpha((int) ((1 - progress) * 255));

                        toolbar_nono.getBackground().setAlpha((int) (progress * 255));
                        et_nono_left.getBackground().setAlpha((int) (progress * 255));
                        iv_nono_right.getBackground().setAlpha((int) (progress * 255));

                    } else {

                        toolbar_default.getBackground().setAlpha((int) (progress * 255));
                        iv_defalut_left.getBackground().setAlpha((int) (progress * 255));
                        iv_default_right.getBackground().setAlpha((int) (progress * 255));

                        toolbar_nono.getBackground().setAlpha((int) ((1 - progress) * 255));
                        et_nono_left.getBackground().setAlpha((int) ((1 - progress) * 255));
                        iv_nono_right.getBackground().setAlpha((int) ((1 - progress) * 255));
                    }

                }
            }
        });
    }

    private void initData() {
        SimpleFragmetnPagerAdapter pagerAdapter = new SimpleFragmetnPagerAdapter(getSupportFragmentManager(), this);
        mViewPager.setAdapter(pagerAdapter);

        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
    }

    private void updateView() {

    }

}