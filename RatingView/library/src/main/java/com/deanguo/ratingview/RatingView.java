package com.deanguo.ratingview;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;

import com.deanguo.library.R;

import java.util.ArrayList;

/**
 * Created by DeanGuo on 12/22/15.
 */
public class RatingView extends FrameLayout {

    public static interface AnimatorListener {
        void onRotateStart();
        void onRotateEnd();

        void onRatingStart();
        void onRatingEnd();
    }

    /**
     * animator callback listener
     */
    private AnimatorListener mAnimatorListener;

    // style attr
    private int mRatedColor, mUnratedColor, mTitleColor, mOutlineColor, mDefaultColor;

    private boolean isShowTitle = true;

    private int mRatingMax;


    private boolean isShow = false;

    private static final int STROKE_OFFSET = 10;

    private static final long ROTATING_ANIMATION_DURATION = 3000L;

    private static final long RATING_ANIMATION_DURATION = 3000L;

    private static final long TEXT_ANIMATION_DURATION = 1000L;

    private int mCenterX, mCenterY;

    private float rotateAngle;

    private int ratingGap = -1, textAlpha = 0;

    private ArrayList<RatingBar> mRatingBars;

    private ValueAnimator rotateAnimator, ratingAnimator, titleAnimator;

    public RatingView(Context context) {
        super(context);
    }

    public RatingView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // call onDraw in ViewGroup
        setWillNotDraw(false);

        //load styled attributes.
        final TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.RatingView, 0, 0);

        mRatedColor = attributes.getColor(R.styleable.RatingView_rating_rated_color, 0);
        mUnratedColor = attributes.getColor(R.styleable.RatingView_rating_unrated_color, 0);
        mTitleColor = attributes.getColor(R.styleable.RatingView_rating_title_color, 0);
        mOutlineColor = attributes.getColor(R.styleable.RatingView_rating_outline_color, 0);
        mDefaultColor = attributes.getColor(R.styleable.RatingView_rating_default_color, 0);
        isShowTitle = attributes.getBoolean(R.styleable.RatingView_rating_title_visible, true);
        mRatingMax = attributes.getInt(R.styleable.RatingView_rating_max, 0);
        attributes.recycle();

        mRatingBars = new ArrayList<>();
        initRotatingAnimation();
        initRatingAnimation();
        initTextAnimation();
    }

    private void initTextAnimation() {
        titleAnimator = ValueAnimator.ofInt(0, 255);
        titleAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                textAlpha = (int) animation.getAnimatedValue();
                invalidate();
            }
        });
        titleAnimator.setDuration(TEXT_ANIMATION_DURATION);
        titleAnimator.setInterpolator(new AccelerateInterpolator());
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
        rotateAnimator.setInterpolator(new AccelerateDecelerateInterpolator());

        rotateAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                mAnimatorListener.onRotateStart();
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mAnimatorListener.onRotateEnd();
                titleAnimator.start();
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
        rotateAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                mAnimatorListener.onRatingStart();
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mAnimatorListener.onRatingEnd();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
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
                ratingBar.drawTitle(canvas, textAlpha);
            }
        }
    }

    public void addRatingBar(RatingBar ratingBar) {
        mRatingBars.add(ratingBar);
    }

    public void removeRatingBar(RatingBar ratingBar) {
        mRatingBars.remove(ratingBar);
    }

    public void removeAll() {
        mRatingBars.removeAll(mRatingBars);
        clear();
    }

    public void clear() {
        isShow = false;
        ratingGap = -1;
        textAlpha = 0;
    }

    public void show() {
        if (mRatingBars.size() == 0) {
            return;
        }
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

            if (dividePart == 1) {
                // only show one rating bar
                ratingBar.setIsSingle(true);
            }
            ratingBar.setCenterX(mCenterX);
            ratingBar.setCenterY(mCenterY);
            ratingBar.setStartAngle(startAngle);
            ratingBar.setSweepAngle(sweepAngle);

            // style attr
            ratingBar.isShowTitle(isShowTitle);
            if (mDefaultColor != 0) {
                ratingBar.setRatingBarColor(mDefaultColor);
            }
            if (mTitleColor != 0) {
                ratingBar.setTitleColor(mTitleColor);
            }
            if (mRatedColor != 0) {
                ratingBar.setRatedColor(mRatedColor);
            }
            if (mUnratedColor != 0) {
                ratingBar.setUnRatedColor(mUnratedColor);
            }
            if (mOutlineColor != 0) {
                ratingBar.setOutlineColor(mOutlineColor);
            }
            if (mRatingMax != 0) {
                ratingBar.setMaxRate(mRatingMax);
            }

            ratingBar.init();
        }

    }

    public AnimatorListener getAnimatorListener() {
        return mAnimatorListener;
    }

    public void setAnimatorListener(AnimatorListener mAnimatorListener) {
        this.mAnimatorListener = mAnimatorListener;
    }

    public void isShowTitle(boolean isShowTitle) {
        this.isShowTitle = isShowTitle;
    }

    public void setDefaultColor(int color) {
        this.mDefaultColor = color;
    }
}
