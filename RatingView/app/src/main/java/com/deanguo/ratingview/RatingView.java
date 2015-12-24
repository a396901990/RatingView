package com.deanguo.ratingview;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.LinearInterpolator;

import java.util.ArrayList;

/**
 * Created by DeanGuo on 12/22/15.
 */
public class RatingView extends View {

    boolean isShow = false;

    private static final int STROKE_OFFSET = 10;

    private static final long ROTATING_ANIMATION_DURATION = 3000L;

    private static final long RATING_ANIMATION_DURATION = 3000L;

    private static final long TEXT_ANIMATION_DURATION = 1000L;

    private int mCenterX, mCenterY;

    private float rotateAngle;

    private int ratingGap = -1, textAlpha = 0;

    private ArrayList<RatingBar> mRatingBars;

    private ValueAnimator rotateAnimator, ratingAnimator,  textAnimator;

    public RatingView(Context context) {
        super(context);
    }

    public RatingView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mRatingBars = new ArrayList<>();
        initRotatingAnimation();
        initRatingAnimation();
        initTextAnimation();
    }

    private void initTextAnimation() {
        textAnimator = ValueAnimator.ofInt(0, 255);
        textAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                textAlpha = (int) animation.getAnimatedValue();
                invalidate();
            }
        });
        textAnimator.setDuration(TEXT_ANIMATION_DURATION);
        textAnimator.setInterpolator(new AccelerateInterpolator());
    }

    public void initRotatingAnimation() {
        rotateAnimator = ValueAnimator.ofFloat(0.0f, 360f * 3);
        rotateAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                rotateAngle = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        rotateAnimator.setDuration(ROTATING_ANIMATION_DURATION);
        rotateAnimator.setInterpolator(new AccelerateInterpolator());

        rotateAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                textAnimator.start();
                ratingAnimator.start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    public void initRatingAnimation() {
        ratingAnimator = ValueAnimator.ofInt(0, 9);
        ratingAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                ratingGap = (int) animation.getAnimatedValue();
                invalidate();
            }
        });
        ratingAnimator.setDuration(RATING_ANIMATION_DURATION);
        ratingAnimator.setInterpolator(new LinearInterpolator());
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mCenterX = w / 2;
        mCenterY = h / 2;
        initRatingBar();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (isShow) {
            canvas.save();
            canvas.rotate(rotateAngle, mCenterX, mCenterY);
            for (RatingBar ratingBar : mRatingBars) {
                ratingBar.drawOutLine(canvas);
            }
            canvas.restore();

            canvas.save();
            canvas.rotate(-rotateAngle, mCenterX, mCenterY);
            for (RatingBar ratingBar : mRatingBars) {
                ratingBar.drawUnRate(canvas);
                ratingBar.drawShadow(canvas);
            }
            canvas.restore();

            if (ratingGap != -1) {
                for (RatingBar ratingBar : mRatingBars) {
                    for (int rate = 0; rate < ratingBar.getRate(); rate++) {
                        if (rate <= ratingGap) {
                            ratingBar.drawRate(canvas, rate);
                        }
                    }
                }
            }

            for (RatingBar ratingBar : mRatingBars) {
                ratingBar.drawName(canvas, textAlpha);
            }
        }
    }

    public void addRatingBar(RatingBar ratingBar) {
        mRatingBars.add(ratingBar);
    }

    public void removeRatingBar(RatingBar ratingBar) {
        mRatingBars.remove(ratingBar);
    }

    public void removeAllRatingBar() {
        mRatingBars.retainAll(mRatingBars);
    }

    public void show() {
        initRatingBar();
        rotateAnimator.start();
        isShow = true;
    }

    private void initRatingBar() {

        int dividePart = mRatingBars.size();

        int sweepAngle = dividePart == 1 ? 360 : (360 - dividePart * STROKE_OFFSET) / dividePart;

        int rotateOffset = dividePart == 1 ? 90 : 90 + sweepAngle / 2;

        for (int i = 0; i < dividePart; i++) {
            float startAngle = i * (sweepAngle + STROKE_OFFSET) - rotateOffset;
            RatingBar ratingBar = mRatingBars.get(i);
            // only show one rating bar
            if (dividePart == 1) {
                ratingBar.setIsSingle(true);
            }
            ratingBar.setCenterX(mCenterX);
            ratingBar.setCenterY(mCenterY);
            ratingBar.setStartAngle(startAngle);
            ratingBar.setSweepAngle(sweepAngle);
            ratingBar.init();
        }

    }
}
