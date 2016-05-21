package com.lzp.looppager;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuzipeng on 16/5/15.
 */
public class LoopPager extends FrameLayout {
    public static final int BOTTOM_CENTER = 0;
    public static final int BOTTOM_LEFT = 1;
    public static final int BOTTOM_RIGHT = 2;
    public static final int TOP_CENTER = 3;
    public static final int TOP_LEFT = 4;
    public static final int TOP_RIGHT = 5;

    public static final int CIRCLE = 0;
    public static final int RECTANGLE = 1;
    public static final int ROUND_RECTANGLE = 2;

    private boolean isLoop;
    private List<Integer> mLocalImages = null;
    private int mLoopDuration;
    private List<String> mRemoteImageUrls = null;
    private boolean mLocalMode;
    private Context mContext;
    private int mPagerCount;

    private boolean mEnableNavi;
    private int mNaviCoolor;
    private int mNaviRadius;
    private int mNaviRectangleWidth;
    private int mNaviRectangleHeight;
    private int mNaviPosition;
    private int mNaviShape;

    private Runnable mRunnable;
    private Handler mHandler;
    private ViewPager mViewPager;
    private Navigation mNavigation;
    private boolean isPause;

    private PagerChangeListener mPagerChangeListener;
    private OnClickListener mOnClickListener;

    public interface PagerChangeListener {
        void pagerChange(int position);
    }

    public interface OnClickListener {
        void onClick(int position, ImageView imageView);

        void longClick(int position, ImageView imageView);
    }

    public void setPagerChangeListener(PagerChangeListener listener) {
        mPagerChangeListener = listener;
    }

    public void setOnClickListener(OnClickListener listener) {
        mOnClickListener = listener;
    }

    public LoopPager(Context context) {
        this(context, null);
    }

    public LoopPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        getAttributes(context, attrs);
        init();
        if (mLocalMode) {
            loadPager();
        }
    }

    private void getAttributes(Context context, AttributeSet attrs) {
        mContext = context;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LoopPager);
        isLoop = typedArray.getBoolean(R.styleable.LoopPager_loop, true);
        mLoopDuration = typedArray.getInteger(R.styleable.LoopPager_loopDuration, 2000);
        mEnableNavi = typedArray.getBoolean(R.styleable.LoopPager_enableNavi, true);
        mNaviCoolor = typedArray.getColor(R.styleable.LoopPager_naviColor, Color.parseColor("#ff9800"));
        mNaviRadius = (int) typedArray.getDimension(R.styleable.LoopPager_naviRadius, dp2px(4));
        mNaviPosition = typedArray.getInt(R.styleable.LoopPager_mPosition, BOTTOM_CENTER);
        mNaviShape = typedArray.getInt(R.styleable.LoopPager_naviShape, CIRCLE);
        mNaviRectangleWidth = (int) typedArray.getDimension(R.styleable.LoopPager_rectangleWidth, dp2px(12));
        mNaviRectangleHeight = (int) typedArray.getDimension(R.styleable.LoopPager_rectangleHeight, dp2px(6));

        int resId = typedArray.getResourceId(R.styleable.LoopPager_imagesForLocal, 0x00);
        if (resId != 0x00) {
            mLocalImages = new ArrayList<>();
            TypedArray array = getResources().obtainTypedArray(resId);
            for (int i = 0; i < array.length(); i++) {
                mLocalImages.add(array.getResourceId(i, 0x00));
            }
            mPagerCount = mLocalImages.size();
            mLocalMode = true;

            array.recycle();
        }

        typedArray.recycle();
    }

    private void init() {
        mViewPager = new ViewPager(mContext);
        mViewPager.setLayoutParams(new MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        addView(mViewPager);

        if (mEnableNavi) {
            mNavigation = new Navigation(mContext);
            mNavigation.setLayoutParams(new MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            addView(mNavigation);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            measureChild(getChildAt(i), widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (mEnableNavi) {
            switch (mNaviShape) {
                case CIRCLE:
                    mNavigation.setShape(mNaviShape).setRadius(mNaviRadius).setPagerCount(mPagerCount).
                            setSelectColor(mNaviCoolor).change();
                    break;
                case RECTANGLE:
                case ROUND_RECTANGLE:
                    mNavigation.setShape(mNaviShape).setNaviRectangleWidth(mNaviRectangleWidth).
                            setNaviRectangleHeight(mNaviRectangleHeight).setPagerCount(mPagerCount).
                            setSelectColor(mNaviCoolor).change();
                    break;
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        if (mEnableNavi) {
            int width = getMeasuredWidth();
            int height = getMeasuredHeight();

            int naviWidth = mNavigation.getMeasuredWidth();
            int naviHeight = mNavigation.getMeasuredHeight();

            int l = 0, t = 0;

            switch (mNaviPosition) {
                case BOTTOM_CENTER:
                    l = width / 2 - naviWidth / 2;
                    t = height - naviHeight - dp2px(8);
                    break;
                case BOTTOM_LEFT:
                    l = dp2px(16);
                    t = height - naviHeight - dp2px(8);
                    break;
                case BOTTOM_RIGHT:
                    l = width - naviWidth - dp2px(16);
                    t = height - naviHeight - dp2px(8);
                    break;
                case TOP_CENTER:
                    l = width / 2 - naviWidth / 2;
                    t = dp2px(8);
                    break;
                case TOP_LEFT:
                    l = dp2px(16);
                    t = dp2px(8);
                    break;
                case TOP_RIGHT:
                    l = width - naviWidth - dp2px(16);
                    t = dp2px(8);
                    break;
            }

            mNavigation.layout(l, t, l + naviWidth, t + naviHeight);
        }
    }

    private void autoChange() {
        mHandler = new Handler();
        mRunnable = new Runnable() {
            @Override
            public void run() {
                if (mViewPager.getCurrentItem() != mPagerCount - 1) {
                    mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
                } else {
                    mViewPager.setCurrentItem(0);
                }

                mHandler.postDelayed(this, mLoopDuration);
            }
        };

        mRunnable.run();
    }

    private void loadPager() {
        final List<ImageView> views = constructViews();
        mViewPager.setAdapter(new adapter(views));
        if (isLoop) {
            autoChange();
        }
        bindEvents();
    }

    private void bindEvents() {
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (mEnableNavi) {
                    mNavigation.setSelectPosition(position).change();
                }

                if (mPagerChangeListener != null) {
                    mPagerChangeListener.pagerChange(position);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mViewPager.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        if (isPause) {
                            mHandler.postDelayed(mRunnable, mLoopDuration);
                            isPause = false;
                        }
                        break;
                }

                return false;
            }
        });
    }

    /**
     * 设置本地图片的资源ID
     *
     * @param imagesId
     */
    public void setLocalImages(List<Integer> imagesId) {
        if (mLocalMode) {
            mLocalImages = imagesId;
            mPagerCount = mLocalImages.size();
            loadPager();
        }
    }

    /**
     * 设置从网络获取图片的url
     *
     * @param RemoteImageUrls
     */
    public void setRemoteImageUrls(List<String> RemoteImageUrls) {
        if (!mLocalMode) {
            mRemoteImageUrls = RemoteImageUrls;
            mPagerCount = mRemoteImageUrls.size();
            loadPager();
        }
    }

    /**
     * 设置是否自动切换
     *
     * @param on_off
     */
    public void setLoop(boolean on_off) {
        isLoop = on_off;
        invalidate();
    }

    /**
     * 设置切换时间间隔
     *
     * @param duration
     */
    public void setLoopDuration(int duration) {
        mLoopDuration = duration;
        invalidate();
    }

    /**
     * 设置是否有导航标示
     *
     * @param on_off
     */
    public void setEnableNavi(boolean on_off) {
        mEnableNavi = on_off;
        invalidate();
    }

    /**
     * 设置导航图形
     * 三种可选
     * LoopPager.CIRCLE
     * LoopPager.RECTANGLE
     * LoopPager.ROUND_RECTANGLE
     *
     * @param shape
     */
    public void setNaviShape(int shape) {
        mNaviShape = shape;
        invalidate();
    }

    /**
     * 设置导航位置
     * 6种可选
     * LoopPager.BOTTOM_CENTER
     * LoopPager.BOTTOM_LEFT
     * LoopPager.BOTTOM_RIGHT
     * LoopPager.TOP_CENTER
     * LoopPager.TOP_LEFT
     * LoopPager.TOP_RIGHT
     *
     * @param position
     */
    public void setNaviPosition(int position) {
        mNaviPosition = position;
        invalidate();
    }

    /**
     * 设置原型导航的半径
     *
     * @param dp 规格为dp
     */
    public void setNaviRadius(int dp) {
        mNaviRadius = dp2px(dp);
        invalidate();
    }

    /**
     * 设置矩形或圆角矩形导航的宽和高
     * 同样的规格为dp
     *
     * @param width
     * @param height
     */
    public void setNaviWidthAndHeight(int width, int height) {
        mNaviRectangleWidth = dp2px(width);
        mNaviRectangleHeight = dp2px(height);
        invalidate();
    }

    /**
     * 构造要放入ViewPager的imageview
     *
     * @return
     */
    private List<ImageView> constructViews() {
        List<ImageView> views = new ArrayList<>();
        if (mLocalMode) {
            if (mLocalImages.size() != 0) {
                for (int i = 0; i < mLocalImages.size(); i++) {
                    ImageView imageView = new ImageView(mContext);
                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                    imageView.setLayoutParams(new MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                    Glide.with(mContext).load(mLocalImages.get(i)).into(imageView);
                    views.add(imageView);
                }
            } else {
                try {
                    throw new Throwable("LoopPager not found localImage");
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        } else {
            if (mRemoteImageUrls.size() != 0) {
                for (int i = 0; i < mRemoteImageUrls.size(); i++) {
                    ImageView imageView = new ImageView(mContext);
                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                    imageView.setLayoutParams(new MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                    Glide.with(mContext).load(mRemoteImageUrls.get(i)).into(imageView);
                    views.add(imageView);
                }
            } else {
                try {
                    throw new Throwable("LoopPager not found RemoteImageUrls");
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        }

        return views;
    }

    class adapter extends PagerAdapter {
        List<ImageView> views;

        public adapter(List<ImageView> views) {
            this.views = views;
        }

        @Override
        public int getCount() {
            if (mLocalMode) {
                return mLocalImages.size();
            } else {
                return mRemoteImageUrls.size();
            }
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
            view = null;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            final ImageView view = views.get(position);
            bindChildEvents(view, position);
            container.addView(view);
            return view;
        }

        private void bindChildEvents(final ImageView view, final int position) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnClickListener != null) {
                        mOnClickListener.onClick(position, view);
                    }
                }
            });

            view.setOnLongClickListener(new OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (mOnClickListener != null) {
                        mOnClickListener.longClick(position, view);
                    }
                    return true;
                }
            });

            view.setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                        case MotionEvent.ACTION_MOVE:
                            mHandler.removeCallbacks(mRunnable);
                            isPause = true;
                            break;
                        case MotionEvent.ACTION_UP:
                            if (isPause) {
                                mHandler.postDelayed(mRunnable, mLoopDuration);
                                isPause = false;
                            }
                            break;
                    }

                    return false;
                }
            });
        }
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }
}
