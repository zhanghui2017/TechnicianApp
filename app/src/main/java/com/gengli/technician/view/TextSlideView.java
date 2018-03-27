package com.gengli.technician.view;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gengli.technician.R;
import com.gengli.technician.bean.ImageTitleBean;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Jun on 2016/8/4.
 */
public class TextSlideView extends FrameLayout {

    private Context context;
    private View contentView;
    private ViewPager vpTextTitle;
//    private LinearLayout llDot;
    private int count;
    private List<View> viewList;
    private boolean isAutoPlay;
    private Handler handler;
    private int currentItem;
//    private Animator animatorToLarge;
//    private Animator animatorToSmall;
//    private SparseBooleanArray isLarge;
    private List<String> textTitleBeanList;
//    private int dotSize = 12;
//    private int dotSpace = 12;
    private int delay = 3000;
    public TextSlideView(Context context) {
        this(context, null);
    }

    public TextSlideView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TextSlideView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        // 初始化View
        initView();
        // 初始化Animator
//        initAnimator();
        // 初始化数据
        initData();
    }

    private void initData() {
        textTitleBeanList = new ArrayList<>();
    }

    private void initAnimator() {
//        animatorToLarge = AnimatorInflater.loadAnimator(context, R.animator.scale_to_large);
//        animatorToSmall = AnimatorInflater.loadAnimator(context, R.animator.scale_to_small);
    }

    /**
     * 初始化View
     */
    private void initView() {
        contentView = LayoutInflater.from(context).inflate(R.layout.text_slider_view, this, true);
        vpTextTitle = (ViewPager) findViewById(R.id.vp_text_title);
//        llDot = (LinearLayout) findViewById(R.id.ll_dot);
    }

//    // 设置小圆点的大小
//    public void setDotSize(int dotSize) {
//        this.dotSize = dotSize;
//    }
//
//    // 设置小圆点的间距
//    public void setDotSpace(int dotSpace) {
//        this.dotSpace = dotSpace;
//    }

    // 设置图片轮播间隔时间
    public void setDelay(int delay) {
        this.delay = delay;
    }

    // 添加图片
    public void addTextTitle(String text) {
//        ImageTitleBean imageTitleBean = new ImageTitleBean();
//        imageTitleBean.setImageUrl(imageUrl);
        textTitleBeanList.add(text);
    }

    // 添加图片和标题
//    public void addImageTitle(String imageUrl, String title) {
//        ImageTitleBean imageTitleBean = new ImageTitleBean();
//        imageTitleBean.setImageUrl(imageUrl);
//        imageTitleBean.setTitle(title);
//        imageTitleBeanList.add(imageTitleBean);
//
//    }

    // 添加图片和标题的JavaBean
//    public void addImageTitleBean(ImageTitleBean imageTitleBean) {
//        imageTitleBeanList.add(imageTitleBean);
//    }

    // 设置图片和标题的JavaBean数据列表
    public void setImageTitleBeanList(List<String> imageTitleBeanList) {
        this.textTitleBeanList = imageTitleBeanList;
    }

    // 设置完后最终提交
    public void commit() {
        if (textTitleBeanList != null) {
            count = textTitleBeanList.size();
            // 设置ViewPager
            setViewPager(textTitleBeanList);
            // 设置指示器
//            setIndicator();
            // 开始播放
            starPlay();
        } else {
//            Log.e(TAG, "数据为空");
        }
    }

    /**
     * 设置指示器
     */
//    private void setIndicator() {
////        isLarge = new SparseBooleanArray();
//        // 记得创建前先清空数据，否则会受遗留数据的影响。
////        llDot.removeAllViews();
//        for (int i = 0; i < count; i++) {
//            ImageView view = new ImageView(context);
//            view.setImageResource(R.drawable.dot_unselected);
//            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(dotSize, dotSize);
//            layoutParams.leftMargin = dotSpace / 2;
//            layoutParams.rightMargin = dotSpace / 2;
//            layoutParams.topMargin = dotSpace / 2;
//            layoutParams.bottomMargin = dotSpace / 2;
//            llDot.addView(view, layoutParams);
////            isLarge.put(i, false);
//        }
//        ((ImageView)llDot.getChildAt(0)).setImageResource(R.drawable.dot_selected);
////        animatorToLarge.setTarget(llDot.getChildAt(0));
////        animatorToLarge.start();
////        isLarge.put(0, true);
//    }

    /**
     * 开始自动播放图片
     */
    private void starPlay() {
        // 如果少于2张就不用自动播放了
        if (count < 2) {
            isAutoPlay = false;
        } else {
            isAutoPlay = true;
            handler = new Handler();
            handler.postDelayed(task, delay);
        }
    }

    private Runnable task = new Runnable() {
        @Override
        public void run() {
            if (isAutoPlay) {
                // 位置循环
                currentItem = currentItem % (count + 1) + 1;
                // 正常每隔3秒播放一张图片
                vpTextTitle.setCurrentItem(currentItem);
                handler.postDelayed(task, delay);
            } else {
                // 如果处于拖拽状态停止自动播放，会每隔5秒检查一次是否可以正常自动播放。
                handler.postDelayed(task, 2000);
            }
        }
    };

    // 创建监听器接口
    public interface OnItemClickListener {
        void onTextItemClick(View view, int position);
    }

    // 声明监听器
    private OnItemClickListener onItemClickListener;

    // 提供设置监听器的公共方法
    public void setOnItemClickListener(OnItemClickListener listener) {
        onItemClickListener = listener;
    }

    class TextTitlePagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return viewList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            View view = viewList.get(position);
            // 设置Item的点击监听器
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onTextItemClick(v, position - 1);
                }
            });
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeAllViews();
            }
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(viewList.get(position));
        }
    }

    /**
     * 设置ViewPager
     *
     * @param textTitleBeanList
     */
    private void setViewPager(List<String> textTitleBeanList) {
        // 设置View列表
        setViewList(textTitleBeanList);
        vpTextTitle.setAdapter(new TextTitlePagerAdapter());
        // 从第1张图片开始（位置刚好也是1，注意：0位置现在是最后一张图片）
        currentItem = 1;
        vpTextTitle.setCurrentItem(1);
        vpTextTitle.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                // 遍历一遍子View，设置相应的背景。
//                for (int i = 0; i < llDot.getChildCount(); i++) {
//                    if (i == position - 1) {// 被选中
//                        ((ImageView)llDot.getChildAt(i)).setImageResource(R.drawable.dot_selected);
////                        if (!isLarge.get(i)) {
////                            animatorToLarge.setTarget(llDot.getChildAt(i));
////                            animatorToLarge.start();
////                            isLarge.put(i, true);
////                        }
//                    } else {// 未被选中
//                        ((ImageView)llDot.getChildAt(i)).setImageResource(R.drawable.dot_unselected);
////                        if (isLarge.get(i)) {
////                            animatorToSmall.setTarget(llDot.getChildAt(i));
////                            animatorToSmall.start();
////                            isLarge.put(i, false);
////                        }
//                    }
//                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                switch (state) {
                    // 闲置中
                    case ViewPager.SCROLL_STATE_IDLE:
                        // “偷梁换柱”
                        if (vpTextTitle.getCurrentItem() == 0) {
                            vpTextTitle.setCurrentItem(count, false);
                        } else if (vpTextTitle.getCurrentItem() == count + 1) {
                            vpTextTitle.setCurrentItem(1, false);
                        }
                        currentItem = vpTextTitle.getCurrentItem();
                        isAutoPlay = true;
                        break;
                    // 拖动中
                    case ViewPager.SCROLL_STATE_DRAGGING:
                        isAutoPlay = false;
                        break;
                    // 设置中
                    case ViewPager.SCROLL_STATE_SETTLING:
                        isAutoPlay = true;
                        break;
                }
            }
        });
    }

    /**
     * 根据出入的数据设置View列表
     *
     * @param textTitleBeanList
     */
    private void setViewList(List<String> textTitleBeanList) {
        viewList = new ArrayList<>();
        for (int i = 0; i < count + 2; i++) {
            View view = LayoutInflater.from(context).inflate(R.layout.text_title_layout, null);
            TextView tvTitle = (TextView) view.findViewById(R.id.text_title);
            if (i == 0) {
                tvTitle.setText(textTitleBeanList.get(count - 1));

            } else if (i == count + 1) {
                tvTitle.setText(textTitleBeanList.get(0));
            } else {
                tvTitle.setText(textTitleBeanList.get(i - 1));
            }
            // 将设置好的View添加到View列表中

            viewList.add(view);
        }
    }

    /**
     * 释放资源
     */
    public void releaseResource() {
        handler.removeCallbacksAndMessages(null);
        context = null;
    }
}
