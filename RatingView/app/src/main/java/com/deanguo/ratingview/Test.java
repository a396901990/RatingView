package com.deanguo.ratingview;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.LinearInterpolator;

import java.util.ArrayList;

/**
 * Created by DeanGuo on 12/22/15.
 */
public class Test extends View {

    private static final int OUTSIDE_STROKE_WIDTH = 10;

    private static final int INSIDE_STROKE_WIDTH = 60;

    private static final int SHADOW_STROKE_WIDTH = 20;

    private static final int STROKE_OFFSET = 10;

    private static final int OUTSIDE_INSIDE_OFFSET = 60;

    private int dividePart = 3;

    Paint outsidePaint, insidePaint, shadowPaint, textPaint;

    private RectF outsideOval, insideOval, shadowOval;
    private int mCenterX;
    private int mCenterY;
    private int mRadius;
    private int mSignRadius;
    private float rotateAngle;
    private int ratingGap = -1;

    ArrayList<ArcProgress> arcProgresses;

    public Test(Context context) {
        super(context);
    }

    ValueAnimator rotateAnimator, ratingAnimaor;

    public Test(Context context, AttributeSet attrs) {
        super(context, attrs);

        outsidePaint = new Paint();
        outsidePaint.setAntiAlias(true);
        outsidePaint.setStyle(Paint.Style.STROKE);
        outsidePaint.setColor(Color.WHITE);
        outsidePaint.setStrokeWidth(OUTSIDE_STROKE_WIDTH);
        outsidePaint.setAlpha(50);

        textPaint = new TextPaint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(Color.BLUE);
        textPaint.setAlpha(50);
        textPaint.setTextSize(30);

        insidePaint = new Paint();
        insidePaint.setAntiAlias(true);
        insidePaint.setStyle(Paint.Style.STROKE);
        insidePaint.setColor(Color.WHITE);
        insidePaint.setStrokeWidth(INSIDE_STROKE_WIDTH);
        insidePaint.setAlpha(100);

        shadowPaint = new Paint();
        shadowPaint.setAntiAlias(true);
        shadowPaint.setStyle(Paint.Style.STROKE);
        shadowPaint.setStrokeWidth(SHADOW_STROKE_WIDTH);
        shadowPaint.setColor(Color.WHITE);
        shadowPaint.setAlpha(50);

        outsideOval = new RectF();
        insideOval = new RectF();
        shadowOval = new RectF();

        rotateAnimator = ValueAnimator.ofFloat(0.0f, 1.0f);
        rotateAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                rotateAngle = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        rotateAnimator.setDuration(5000);
        rotateAnimator.setInterpolator(new AccelerateInterpolator());

        rotateAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                ratingAnimaor.start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        ratingAnimaor = ValueAnimator.ofInt(0, 9);
        ratingAnimaor.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                ratingGap = (int) animation.getAnimatedValue();
                invalidate();
            }
        });
        ratingAnimaor.setDuration(3000);
        ratingAnimaor.setInterpolator(new LinearInterpolator());


        arcProgresses = new ArrayList<>();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mCenterX = w / 2;
        mCenterY = h / 2;

        mRadius = mCenterX > mCenterY ? mCenterY : mCenterX;
        mSignRadius = (int) (mRadius * 0.55F);

        int outsideRadius = mRadius - (OUTSIDE_STROKE_WIDTH / 2);

        int insideRadius = mRadius - OUTSIDE_INSIDE_OFFSET - (INSIDE_STROKE_WIDTH / 2);

        int shadowRadius = insideRadius - (INSIDE_STROKE_WIDTH / 2) - (SHADOW_STROKE_WIDTH / 2);

        outsideOval.left = mCenterX - outsideRadius;
        outsideOval.top = mCenterY - outsideRadius;
        outsideOval.right = mCenterX + outsideRadius;
        outsideOval.bottom = mCenterY + outsideRadius;

        insideOval.left = mCenterX - insideRadius;
        insideOval.top = mCenterY - insideRadius;
        insideOval.right = mCenterX + insideRadius;
        insideOval.bottom = mCenterY + insideRadius;

        shadowOval.left = mCenterX - shadowRadius;
        shadowOval.top = mCenterY - shadowRadius;
        shadowOval.right = mCenterX + shadowRadius;
        shadowOval.bottom = mCenterY + shadowRadius;

        rotateAnimator.start();

        // 分成几份，画几段
        int sweepAngle = (360 - dividePart * STROKE_OFFSET) / dividePart;
        for (int i = 0; i < dividePart; i++) {
            float startAngle = i * (sweepAngle + STROKE_OFFSET);
            arcProgresses.add(new ArcProgress(startAngle, sweepAngle));
        }
            arcProgresses.get(0).setCurRate(5);
        arcProgresses.get(1).setCurRate(8);
        arcProgresses.get(2).setCurRate(10);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.save();
        canvas.rotate(rotateAngle * 360, mCenterX, mCenterY);
        for (ArcProgress arcProgress : arcProgresses) {
            arcProgress.drawOutSideLine(canvas, outsideOval, outsidePaint);
        }
        canvas.restore();

        canvas.drawText("haha", mCenterX, mCenterY, textPaint);

        canvas.save();
        canvas.rotate(-rotateAngle * 360, mCenterX, mCenterY);
        for (ArcProgress arcProgress : arcProgresses) {
            arcProgress.drawProgress(canvas, insideOval, insidePaint);
            arcProgress.drawShadow(canvas, shadowOval, shadowPaint);
        }
        canvas.restore();

        if (ratingGap != -1) {
            for (ArcProgress arcProgress : arcProgresses) {
                for (int rate = 0; rate < arcProgress.getCurRate(); rate++ ) {
                    if (rate <= ratingGap) {
                        arcProgress.drawRate(canvas, insideOval, rate);
                    }
                }
            }
        }

    }

    public static class ArcProgress {
        private float mStartAngle;
        private float mSweepAngle;

        private int maxRate = 10;

        private int curRate = 5;

        private static final int ITEM_OFFSET = 1;

        ArrayList<Arc> arcs;

        private Paint ratingPaint;

        public ArcProgress(float startAngle, float sweepAngle) {
            this.mStartAngle = startAngle;
            this.mSweepAngle = sweepAngle;

            initArcs();
            initPaint();
        }

        private void initPaint() {
            ratingPaint = new Paint();
            ratingPaint.setAntiAlias(true);
            ratingPaint.setStyle(Paint.Style.STROKE);
            ratingPaint.setColor(Color.WHITE);
            ratingPaint.setStrokeWidth(INSIDE_STROKE_WIDTH);
        }

        private void initArcs() {
            arcs = new ArrayList<>();

            float itemSweepAngle = (mSweepAngle - (ITEM_OFFSET * (maxRate - 1))) / maxRate;
            for (int i = 0; i < maxRate; i++) {
                float itemStartAngle = mStartAngle + i * (itemSweepAngle + ITEM_OFFSET);
                arcs.add(new Arc(itemStartAngle, itemSweepAngle));
            }
        }

        public void drawProgress(Canvas canvas, RectF oval, Paint paint) {
            for (Arc arc : arcs) {
                arc.drawArc(canvas, oval, paint);
            }
        }

        public void drawRate(Canvas canvas, RectF oval, int index) {
            Arc arc = arcs.get(index);
            arc.drawArc(canvas, oval, ratingPaint);
        }


        public void drawShadow(Canvas canvas, RectF oval, Paint paint) {
            for (Arc arc : arcs) {
                arc.drawArc(canvas, oval, paint);
            }
        }

        public void drawOutSideLine(Canvas canvas, RectF oval, Paint paint) {
            canvas.drawArc(oval, mStartAngle, mSweepAngle, false, paint);
        }

        public void setMaxRate(int maxRate) {
            this.maxRate = maxRate;
            initArcs();
        }

        public void setCurRate(int curRate) {
            this.curRate = curRate;
        }

        public int getCurRate() {
            return curRate;
        }

    }

    public static class Arc {

        private float startAngle, sweepAngle;

        public Arc(float startAngle, float sweepAngle) {
            this.startAngle = startAngle;
            this.sweepAngle = sweepAngle;
        }

        public void drawArc(Canvas canvas, RectF oval, Paint paint) {
            canvas.drawArc(oval, startAngle, sweepAngle, false, paint);
        }
    }
}
