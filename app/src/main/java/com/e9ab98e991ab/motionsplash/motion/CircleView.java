package com.e9ab98e991ab.motionsplash.motion;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;

import androidx.annotation.Nullable;

import com.e9ab98e991ab.motionsplash.R;

public class CircleView extends View {
    public CircleView(Context context) {
        this(context,null);
    }

    public CircleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private Paint mPaint = new Paint();
    private int[] colors;

    //旋转圆的中心坐标
    private float mCenterX;
    private float mCenterY;


    //小球的半径
    private float mCircleRadius = 20;

    //小球围成的圆的半径
    private float mRadius  = 100;

    private float mCurrentRotateDegrees;
    private float mCurrentRadius = mRadius;


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mCenterX = w * 1f /2;
        mCenterY = h * 1f/2;
    }

    private void init(){
        mPaint.setStyle(Paint.Style.FILL);
        colors = getResources().getIntArray(R.array.color_array);

    }

    private BallState mBallState;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(mBallState == null){
            mBallState = new RotateState();
        }

        mBallState.drawState(canvas);

    }

    class RotateState implements BallState{

        private RotateState(){
            ValueAnimator mAnimator = ValueAnimator.ofFloat(0,(float) Math.PI);
            mAnimator.setDuration(1000);
            //LinearInterpolator以常量速率改变
            mAnimator.setInterpolator(new LinearInterpolator());
            mAnimator.setRepeatCount(2);
            mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mCurrentRotateDegrees = (float)animation.getAnimatedValue();

                    invalidate();
                }
            });

            mAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    //旋转动画结束后，开始扩散聚合动画
                    mBallState = new MergeState();
                }
            });

            mAnimator.start();
        }

        @Override
        public void drawState(Canvas canvas) {
            drawBallState(canvas);
        }
    }

    class MergeState implements BallState{

        private MergeState(){
            ValueAnimator mAnimator = ValueAnimator.ofFloat(mCircleRadius,mRadius);
            mAnimator.setDuration(1000);
            //OvershootInterpolator 向前甩一定值后再回到原来位置 (tension影响扩大比例的值)
            mAnimator.setInterpolator(new OvershootInterpolator(20f));
            //mAnimator.setRepeatCount(2);//默认0，执行一次
            mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mCurrentRadius = (float)animation.getAnimatedValue();
                    Log.d("CircleView","onAnimationUpdate | mCurrentRadius-->"+mCurrentRadius);
                    invalidate();
                }
            });

            //mAnimator.start();
            //动画反向执行
            mAnimator.reverse();
        }


        @Override
        public void drawState(Canvas canvas) {
            drawBallState(canvas);
        }
    }

    interface BallState{
        void drawState(Canvas canvas);
    }

    private void drawBallState(Canvas canvas){
        double degrees = Math.PI * 2 / colors.length;

        for (int i = 0; i < colors.length; i++) {

            // x = r * cos(a) + centX;
            // y= r * sin(a) + centY;

            mPaint.setColor(colors[i]);

            float angle = (float) ((i * degrees) + mCurrentRotateDegrees);

            float cx = (float) (mCurrentRadius * Math.cos(angle) + mCenterX);
            float cy = (float) (mCurrentRadius * Math.sin(angle) + mCenterY);

            canvas.drawCircle(cx,cy,mCircleRadius,mPaint);
        }
    }
}

