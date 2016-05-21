package com.lzp.looppager;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by liuzipeng on 16/5/17.
 */
public class Navigation extends View {
    private Paint mPaint;
    private int mSelectColor;
    private int mSelectPosition;
    private int mRadius;
    private int mPagerCount;
    private int mShape;
    private int mNaviRectangleWidth;
    private int mNaviRectangleHeight;

    public Navigation(Context context) {
        this(context, null);
    }

    public Navigation(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Navigation(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(dp2px(2));
    }

    /**
     * 设置矩形宽度
     *
     * @param width
     * @return
     */
    Navigation setNaviRectangleWidth(int width) {
        mNaviRectangleWidth = width;
        return this;
    }

    /**
     * 设置矩形高度
     *
     * @param height
     * @return
     */
    Navigation setNaviRectangleHeight(int height) {
        mNaviRectangleHeight = height;
        return this;
    }

    /**
     * 设置导航形状
     *
     * @param shape
     * @return
     */
    Navigation setShape(int shape) {
        mShape = shape;
        return this;
    }

    /**
     * 设置导航被选状态的颜色
     *
     * @param color
     * @return
     */
    Navigation setSelectColor(int color) {
        mSelectColor = color;
        return this;
    }

    /**
     * 设置被选中的位置
     *
     * @param index
     * @return
     */
    Navigation setSelectPosition(int index) {
        mSelectPosition = index;
        return this;
    }

    /**
     * 设置半径
     *
     * @param radius
     * @return
     */
    Navigation setRadius(int radius) {
        mRadius = radius;
        return this;
    }

    /**
     * 设置导航的个数
     *
     * @param count
     * @return
     */
    Navigation setPagerCount(int count) {
        mPagerCount = count;
        return this;
    }

    /**
     * 刷新view
     */
    void change() {
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
    }

    private int measureWidth(int widthMeasureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(widthMeasureSpec);
        int specSize = MeasureSpec.getSize(widthMeasureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            switch (mShape) {
                case LoopPager.CIRCLE:
                    result = mRadius * 3 * mPagerCount - mRadius;
                    break;
                case LoopPager.RECTANGLE:
                case LoopPager.ROUND_RECTANGLE:
                    result = (mNaviRectangleWidth + dp2px(4)) * mPagerCount - dp2px(4);
                    break;
            }

            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }

        return result;
    }

    private int measureHeight(int heightMeasureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(heightMeasureSpec);
        int specSize = MeasureSpec.getSize(heightMeasureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            switch (mShape) {
                case LoopPager.CIRCLE:
                    result = mRadius * 2;
                    break;
                case LoopPager.RECTANGLE:
                case LoopPager.ROUND_RECTANGLE:
                    result = mNaviRectangleHeight;
                    break;
            }

            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }

        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (int i = 0; i < mPagerCount; i++) {
            if (i == mSelectPosition) {
                mPaint.setColor(mSelectColor);
            } else {
                mPaint.setColor(Color.WHITE);
            }

            switch (mShape) {
                case LoopPager.CIRCLE:
                    canvas.drawCircle(((mRadius * 3) * i) + mRadius, mRadius, mRadius, mPaint);
                    break;
                case LoopPager.RECTANGLE:
                    canvas.drawRect((mNaviRectangleWidth + dp2px(4)) * i, 0, (mNaviRectangleWidth +
                            dp2px(4)) * i + mNaviRectangleWidth, mNaviRectangleHeight, mPaint);
                    break;
                case LoopPager.ROUND_RECTANGLE:
                    RectF rectF = new RectF((mNaviRectangleWidth + dp2px(4)) * i, 0, (mNaviRectangleWidth +
                            dp2px(4)) * i + mNaviRectangleWidth, mNaviRectangleHeight);
                    canvas.drawRoundRect(rectF, dp2px(2), dp2px(2), mPaint);
                    break;
            }
        }
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }
}
