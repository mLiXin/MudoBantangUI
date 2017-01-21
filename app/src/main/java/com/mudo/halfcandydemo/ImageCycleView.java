package com.mudo.halfcandydemo;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IntegerRes;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 广告图片自动轮播控件</br>
 * <p/>
 * <pre>
 *   集合ViewPager和指示器的一个轮播控件，主要用于一般常见的广告图片轮播，具有自动轮播和手动轮播功能
 *   使用：只需在xml文件中使用{@code <com.minking.imagecycleview.ImageCycleView/>} ，
 *   然后在页面中调用  {@link #setImageResources(ArrayList) }即可!
 *
 *   另外提供{@link #startImageCycle() } \ {@link #stopImageCycle() }两种方法，用于在Activity不可见之时节省资源；
 *   因为自动轮播需要进行控制，有利于内存管理
 * </pre>
 *
 */
public class ImageCycleView extends LinearLayout {

    /**
     * 上下文
     */
    private Context mContext;
    /**
     * 传入的图片数量
     */
    private int imageCount;
    /**
     * 图片轮播视图
     */
    private ViewPager mAdvPager = null;

    /**
     * 图片轮播指示器控件
     */
    private ViewGroup mGroup;

    /**
     * 图片轮播指示器-个图
     */
    private ImageView mImageView = null;

    /**
     * 滚动图片指示器-视图列表
     */
    private ImageView[] mImageViews = null;

    /**
     * 图片滚动当前图片下标
     */
    private int mImageIndex = 10000;

    /**
     * 手机密度
     */
    private float mScale;
    /**
     * 小图标大小
     */
    private int imageParams;
    /**
     * 小图标间距
     */
    private int imagePadding;
    /**
     * 适配器
     */
    private ImageCycleAdapter iCycleAdapter;
    /**
     * 定时器
     */
    private Timer timer ;
    /**
     * 定时任务
     */
    private ImageWallRunnable imageWallRunnable ;
    /**
     * @param context
     */
    public ImageCycleView(Context context) {
        super(context);
    }

    /**
     * @param context
     * @param attrs
     */
    public ImageCycleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mScale = context.getResources().getDisplayMetrics().density;
        LayoutInflater.from(context).inflate(R.layout.ad_cycle_view, this);
        mAdvPager = (ViewPager) findViewById(R.id.adv_pager);
        mAdvPager.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        Log.d("lxx","down");
                        break;
                    case MotionEvent.ACTION_UP:
                        Log.d("lxx","start");
                        break;
                }
                return false;
            }
        });
        mAdvPager.setOnPageChangeListener(new GuidePageChangeListener());
        mAdvPager.setOnTouchListener(new OnTouchListener() {
            float x1;
            float x2;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        x2 = event.getX();
                        if (x1 - x2 > 0) {
                            mImageIndex++;
                        }
                        if (x1 - x2 < 0) {
                            mImageIndex--;
                        }
                        // 开始图片滚动
                        startImageCycle();
                        break;
                    case MotionEvent.ACTION_DOWN:
                        x1 = event.getX();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        stopImageCycle();
                        break;
                }
                return false;
            }
        });
        // 滚动图片右下指示器视图
        mGroup = (ViewGroup) findViewById(R.id.viewGroup);


    }

    public void setImageResources(ArrayList<Integer> imageList) {
        if(imageList ==null ||imageList.size() ==0){
            return;
        }
        // 清除所有子视图
        mGroup.removeAllViews();
        imageCount = imageList.size();
        imageParams = (int) (mScale * 8 + 0.5f);// XP与DP转换，适应不同分辨率
        imagePadding = (int) (mScale * 2 + 0.5f);
        if(imageCount ==1){//如果只有一张图，不显示圆点
            // 清除所有子视图
            mGroup.removeAllViews();
        }
        iCycleAdapter = new ImageCycleAdapter(mContext, imageList);
       if(imageCount>1) {
           configDot(imageCount, mImageIndex);
       }
        mAdvPager.setAdapter(iCycleAdapter);
        mAdvPager.setCurrentItem(mImageIndex);
        startImageCycle();
    }

    /**
     * 设置对应的点
     * @param imageCount
     * @param mImageIndex
     */
    private void configDot(int imageCount,int mImageIndex) {
        mImageViews = new ImageView[imageCount];
        for (int i = 0; i < imageCount; i++) {
            LayoutParams lp = new LayoutParams(imageParams, imageParams, 0);
            lp.setMargins(imagePadding, imagePadding, imagePadding, imagePadding);
            mImageView = new ImageView(mContext);
            mImageView.setLayoutParams(lp);
            mImageViews[i] = mImageView;
            if (i == mImageIndex%imageCount) {
                mImageViews[i].setBackgroundResource(R.mipmap.banner_dian_focus);
            } else {
                mImageViews[i].setBackgroundResource(R.mipmap.banner_dian_normal);
            }
            mImageViews[i].setPadding(imagePadding, imagePadding, imagePadding, imagePadding);
            mGroup.addView(mImageViews[i]);
        }
    }
    /**
     *任务
     */
     class  ImageWallRunnable  extends TimerTask {
        private boolean isShop;
        public void isStop(boolean isStop){
            this.isShop =isStop;
        }
        @Override
        public void run() {
            if(!isShop) {
                mHandler.sendEmptyMessage(0);
            }
        }
    }
    /**
     *处理消息
     */
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            mImageIndex++;
            mAdvPager.setCurrentItem(mImageIndex);
        }
    };

    private class  GuidePageChangeListener implements OnPageChangeListener {

        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int index) {
            if(imageCount>1) {
                // 设置当前显示的图片下标
                mImageIndex = index;
                // 设置图片滚动指示器背景
                mImageViews[index % imageCount].setBackgroundResource(R.mipmap.banner_dian_focus);
                for (int i = 0; i < mImageViews.length; i++) {
                    if (index % imageCount != i) {
                        mImageViews[i].setBackgroundResource(R.mipmap.banner_dian_normal);
                    }
                }
                mImageViews[index % imageCount].setPadding(imagePadding, imagePadding, imagePadding, imagePadding);
            }
        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }

    }
    private class  ImageCycleAdapter extends PagerAdapter {
        private Context context;
        private ArrayList<Integer> madList = new ArrayList<>();
        //广告图片的点击事件
        private ImageView imageView ;
        public ImageCycleAdapter(Context context, ArrayList<Integer> adList){
            this.context  = context;
            this.madList.clear();
            this.madList.addAll(adList);
        }

        @Override
        public int getCount() {
            int  ImageCycle = (imageCount>1)? Integer.MAX_VALUE:1;
            return ImageCycle;
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return view == o;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {

            imageView = new ImageView(context);
            imageView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setBackgroundResource(madList.get(position%madList.size()));
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ImageView view = (ImageView) object;
            container.removeView(view);
        }
    }

    /**
     * 是否开始轮播任务
     */
    public void startImageCycle(){
        stopImageCycle();
        if(imageCount>1) {
            timer = new Timer();
            imageWallRunnable = new ImageWallRunnable();
            timer.schedule(imageWallRunnable, 5000, 5000);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    /**
     * 取消定时任务
     */
    public void stopImageCycle(){
        if(timer!=null) {
            timer.cancel();
            imageWallRunnable=null;
            timer=null;
        }
    }
    /**
     * 轮播控件的监听事件
     *
     * @author minking
     */
    public interface ImageCycleViewListener {

        /**
         * 加载图片资源
         *
         * @param imageURL
         * @param imageView
         */
        void displayImage(String imageURL, ImageView imageView);

        /**
         * 单击图片事件
         *
         * @param position
         * @param imageView
         */
        void onImageClick(int position, View imageView);
    }

}
