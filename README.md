#####半糖appUI效果

![半糖.gif](http://upload-images.jianshu.io/upload_images/1457293-0fe0cdc040d84236.gif?imageMogr2/auto-orient/strip)

#####需求
1 toolbar处随上下滑动事件的变化
2 向下滑动的时候，tablayout不会消失
3 左右滑动可切换fragment，点击也可切换fragment
4 banner的自动轮播及手动变换（这个点不讲了，网上有很多开源库甚至代码整理。）

#####思路及分析
0、 Material Design的UI真的很炫，对这个还不是太熟悉，后续要努力。
1 、标题栏处向上滑动，toolbar会从透明底渐变成白底，同时布局也会改变，细致观察，在渐变过程中两种布局都出现了，所以这里应该市同时存在两个toolbar，通过滑动事件的监听渐变透明到消失或显示
2、上滑到底的时候，banner会消失，toobar占据那个位置，同时tablayout也会置顶，不会随着banner的消失而消失。所以banner、Toolbar、以及tablayou都应该在AppBarLayout里面，同时有收缩banner的情况，所以用到CollapsingToolbarLayout
3、底下content部分的话，就是简单的tablayout+viewpager+fragment的处理，这个一般不难
4、viewpager是没有上下滑动的功能的，viewpager内部布局放在fragment里面处理了，所以我这里将viewpager放在一个NestedScrollView里面了
5、准备over

#####no bb，just show code

    <?xml version="1.0" encoding="utf-8"?>
    <!--外部用CoordinatorLayout，本来以为会用到Behavior的，后来发现想太多了。-->
    <android.support.design.widget.CoordinatorLayout     xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:fitsSystemWindows="true">   

     <!--AppBarLayout就是占据一个toolbar的位置，CollapsingToolbarLayout是官方封装好的收缩顶部的viewGroup，FrameLayout的子类-->
    <!--CollapsingToolbarLayout会在滑动收缩顶部的时候，自动给toolbar留一个位置，根据需求，直接在这个布局里面加两个toolbar的自定义布局-->
        <android.support.design.widget.AppBarLayout
            android:id="@+id/appbarlayout"
            android:background="@android:color/transparent"
            android:layout_width="match_parent"
            android:layout_height="wrap_content">

        <android.support.design.widget.CollapsingToolbarLayout
            android:id="@+id/ctb"
            android:layout_width="match_parent"
            android:layout_height="200dp"
            app:contentScrim="@android:color/white"
            app:layout_scrollFlags="scroll|exitUntilCollapsed">

                <!--↓ banner-->
            <com.mudo.halfcandydemo.ImageCycleView
                android:id="@+id/banner"
                android:visibility="visible"
                android:layout_width="match_parent"
                android:layout_height="200dp" />

                <!--toobar1，展开的时候显示的toolbar-->
                <android.support.v7.widget.Toolbar
                    android:id="@+id/toolbar_default"
                    app:layout_collapseMode="pin"
                    android:layout_width="match_parent"
                    android:layout_height="?attr/actionBarSize"
                    android:background="@android:color/transparent" >

                    <LinearLayout
                        android:id="@+id/ll_default_tool"
                        android:orientation="horizontal"
                        android:layout_width="match_parent"
                        android:layout_height="wrap_content">

                        <ImageView
                            android:id="@+id/iv_default_left"
                            android:background="@mipmap/ic_menu"
                            android:layout_width="wrap_content"
                            android:layout_height="wrap_content" />
                        <View
                            android:layout_weight="1"
                            android:layout_width="0dp"
                            android:layout_height="wrap_content"/>
                        <ImageView
                            android:id="@+id/iv_default_right"
                            android:background="@mipmap/ic_settings"
                            android:layout_width="wrap_content"
                            android:layout_height="wrap_content" />
                    </LinearLayout>


                </android.support.v7.widget.Toolbar>

            <!--toobar2 收缩的时候显示的toolbar-->
            <android.support.v7.widget.Toolbar
                android:visibility="gone"
                android:id="@+id/toolbar_nono"
                app:layout_collapseMode="pin"
                android:layout_width="match_parent"
                android:layout_height="?attr/actionBarSize"
                android:background="@android:color/white" >

                <LinearLayout
                    android:gravity="center_vertical"
                    android:orientation="horizontal"
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content">

                    <EditText
                        android:id="@+id/et_nono_left"
                        android:hint="搜索值得买的好物"
                        android:layout_weight="1"
                        android:background="@android:color/white"
                        android:layout_width="0dp"
                        android:layout_height="wrap_content" />

                    <ImageView
                        android:id="@+id/iv_nono_right"
                        android:background="@mipmap/ic_launcher"
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content" />

                </LinearLayout>

            </android.support.v7.widget.Toolbar>

            </android.support.design.widget.CollapsingToolbarLayout>

            <!--tablayout在收缩的时候不会消失，所以这里放在appBarLayout里面，appBarLayout是LinearLayout的子类，所以不影响上面的toolbar-->
            <android.support.design.widget.TabLayout
                android:id="@+id/tl_main_tabtop"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:background="@android:color/white"
                app:tabSelectedTextColor="@color/colorAccent" />
        </android.support.design.widget.AppBarLayout>

    <!--viewpager外面嵌套一个NestedScrollView，使得这部分布局可以上下滑动，当然viewpager里面是一个fragment，fragment里面就是一个recyclerView-->
        <android.support.v4.widget.NestedScrollView
            android:id="@+id/nestedScrollView"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:fillViewport="true"
            app:layout_behavior="@string/appbar_scrolling_view_behavior"
            >

            <LinearLayout
                android:layout_width="match_parent"
                android:layout_height="match_parent"
                android:orientation="vertical">

                <com.mudo.halfcandydemo.WrapContentHeightViewPager
                    android:id="@+id/vp_main_page"
                    android:layout_width="match_parent"
                    android:layout_height="0dp"
                    android:layout_weight="1"
                    android:background="@android:color/white" />
            </LinearLayout>


        </android.support.v4.widget.NestedScrollView>
    </android.support.design.widget.CoordinatorLayout>

上面是布局，剩下的就是java代码，都挺简单的，唯一需要注意的是，我们要根据上下滑动的状态对toolbar进行显示消失替换等一系列的动作，这个怎么办呢？用到**AppBarLayout的addOnOffsetChangedListener**，这个可以监听到偏移值（？是这么叫的吗？），可以获取到你滑动的程度。
    
    appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                //通过vertical的偏移值除以totalScrollRange来获取到百分比
                float progress = (float) (((int) (Math.abs(verticalOffset) / (appBarLayout.getTotalScrollRange()*0.01)))*0.01);

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

                    if(toolbar_nono.getVisibility() == View.GONE){// 折叠状态的toolbar不可见，则是从展开到折叠变化
                        flag_defaulttono = true;
                    }else if(toolbar_default.getVisibility() == View.GONE){// 展开状态的toolbar不可见，则是从折叠到展开变化，为什么？因为只有到0的时候可见性才会变成gone，其他都是可见或改变透明度。
                        flag_defaulttono = false;
                    }// 以上两种情况都不是的话，则不需要改变flag_defaluttono的值

                    toolbar_default.setVisibility(View.VISIBLE);
                    toolbar_nono.setVisibility(View.VISIBLE);//

                    if(flag_defaulttono){ // 0到1变换
                        toolbar_default.getBackground().setAlpha((int) ((1-progress) * 255));
                        iv_defalut_left.getBackground().setAlpha((int) ((1-progress) * 255));
                        iv_default_right.getBackground().setAlpha((int) ((1-progress) * 255));

                        toolbar_nono.getBackground().setAlpha((int) ( progress* 255));
                        et_nono_left.getBackground().setAlpha((int) ( progress* 255));
                        iv_nono_right.getBackground().setAlpha((int) ( progress* 255));

                    }else{

                        toolbar_default.getBackground().setAlpha((int) (progress * 255));
                        iv_defalut_left.getBackground().setAlpha((int) (progress * 255));
                        iv_default_right.getBackground().setAlpha((int) (progress * 255));

                        toolbar_nono.getBackground().setAlpha((int) ((1-progress) * 255));
                        et_nono_left.getBackground().setAlpha((int) ((1-progress) * 255));
                        iv_nono_right.getBackground().setAlpha((int) ((1-progress) * 255));
                    }

                }
            }
        });

剩下的就是banner啊，viewpager+tablayout等那些的，应该大部分都会用，我就不贴上来了。
代码已经放到了[gitHub](https://github.com/MarvelYan/HalfCandyDemo)上，有兴趣的可以去瞅瞅，多多提意见，谢谢！

######仿制的UI效果图

![仿半糖.gif](http://upload-images.jianshu.io/upload_images/1457293-6415d557e3fb1da7.gif?imageMogr2/auto-orient/strip)

######后续
后面还是会继续完善这个仿半糖的app吧，还有底部recyclerView的刷新后续可以添加进去，还有UI什么的。
and  源码有放在github（[https://github.com/MarvelYan/HalfCandyDemo](https://github.com/MarvelYan/HalfCandyDemo)）上，大家有兴趣的，可以瞅瞅源码，中间能优化的地方肯定还有很多，我对Material Design的掌握还不是很好，后续会继续多多学习
and  bug肯定有，各位多多提意见了
and  over，thx  ^_^
