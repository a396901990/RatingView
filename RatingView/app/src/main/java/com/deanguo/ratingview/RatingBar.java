package com.deanguo.ratingview;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.text.TextPaint;

import java.util.ArrayList;

/**
 * Created by DeanGuo on 12/24/15.
 */
public class RatingBar {

    public static final int ITEM_OFFSET = 1;

    private static final int SHADOW_ALPHA = (int) (255 * 0.2); // 20%

    private static final int UN_RATING_ALPHA = (int) (255 * 0.4); // 40%

    private static final int RATING_ALPHA = (int) (255 * 1.0); // 100%

    private boolean isSingle = false;

    private int outlineWidth, ratingBarWidth, shadowWidth, textWidth;

    private float mStartAngle;

    private float mSweepAngle;

    private int maxRate = 10;

    private int mCurRate;

    private ArrayList<Rate> rates;

    private Paint ratedPaint, unRatedPaint, shadowPaint, outlinePaint;
    private TextPaint textPaint;

    private int mRadius;

    private String mName;

    private int mCenterX, mCenterY;

    private RectF outlineOval, ratingOval, shadowOval;

    public RatingBar(int curRate, String name) {
        this.mCurRate = curRate;
        this.mName = name;
    }

    public void init() {
        // in this order to init
        initRatingBar();
        initOval();
        initPaint();
    }

    private void initOval() {
        mRadius = mCenterX > mCenterY ? mCenterY : mCenterX;

        // text bar : 1/10 of radius
        textWidth = mRadius / 10;
        // rating bar : 1/10 of radius
        ratingBarWidth = mRadius / 10;
        // shadow : 1/3 of rating bar
        shadowWidth = ratingBarWidth / 3;
        // outline : 1/3 of shadow
        outlineWidth = shadowWidth / 3;

        int outlineRadius = mRadius - (textWidth / 2); // outline include text and outline, radius is base on textWidth

        int paddingRadius = outlineRadius - (textWidth / 2); // padding space draw nothing, radius is base on textWidth

        int ratingBarRadius = paddingRadius - (textWidth / 2) - (ratingBarWidth / 2);

        int shadowRadius = ratingBarRadius - (ratingBarWidth / 2) - (shadowWidth / 2);

        outlineOval = new RectF();
        ratingOval = new RectF();
        shadowOval = new RectF();

        outlineOval.left = mCenterX - outlineRadius;
        outlineOval.top = mCenterY - outlineRadius;
        outlineOval.right = mCenterX + outlineRadius;
        outlineOval.bottom = mCenterY + outlineRadius;

        ratingOval.left = mCenterX - ratingBarRadius;
        ratingOval.top = mCenterY - ratingBarRadius;
        ratingOval.right = mCenterX + ratingBarRadius;
        ratingOval.bottom = mCenterY + ratingBarRadius;

        shadowOval.left = mCenterX - shadowRadius;
        shadowOval.top = mCenterY - shadowRadius;
        shadowOval.right = mCenterX + shadowRadius;
        shadowOval.bottom = mCenterY + shadowRadius;
    }

    private void initPaint() {

        outlinePaint = new Paint();
        outlinePaint.setAntiAlias(true);
        outlinePaint.setStyle(Paint.Style.STROKE);
        outlinePaint.setColor(Color.WHITE);
        outlinePaint.setStrokeWidth(outlineWidth);
        outlinePaint.setAlpha(UN_RATING_ALPHA);

        ratedPaint = new Paint();
        ratedPaint.setAntiAlias(true);
        ratedPaint.setStyle(Paint.Style.STROKE);
        ratedPaint.setColor(Color.WHITE);
        ratedPaint.setStrokeWidth(ratingBarWidth);
        ratedPaint.setAlpha(RATING_ALPHA);

        unRatedPaint = new Paint();
        unRatedPaint.setAntiAlias(true);
        unRatedPaint.setStyle(Paint.Style.STROKE);
        unRatedPaint.setColor(Color.WHITE);
        unRatedPaint.setStrokeWidth(ratingBarWidth);
        unRatedPaint.setAlpha(UN_RATING_ALPHA);

        shadowPaint = new Paint();
        shadowPaint.setAntiAlias(true);
        shadowPaint.setStyle(Paint.Style.STROKE);
        shadowPaint.setStrokeWidth(shadowWidth);
        shadowPaint.setColor(Color.WHITE);
        shadowPaint.setAlpha(SHADOW_ALPHA);

        textPaint = new TextPaint();
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(textWidth);
        textPaint.setColor(Color.WHITE);
        textPaint.setAlpha(RATING_ALPHA);
    }

    private void initRatingBar() {
        rates = new ArrayList<>();

        float itemSweepAngle;
        if (isSingle) {
            itemSweepAngle = (mSweepAngle - (ITEM_OFFSET * (maxRate))) / maxRate;
        } else {
            itemSweepAngle = (mSweepAngle - (ITEM_OFFSET * (maxRate - 1))) / maxRate;
        }

        for (int i = 0; i < maxRate; i++) {
            float itemStartAngle = mStartAngle + i * (itemSweepAngle + ITEM_OFFSET);
            rates.add(new Rate(itemStartAngle, itemSweepAngle));
        }
    }

    public void drawUnRate(Canvas canvas) {
        for (Rate arc : rates) {
            arc.drawArc(canvas, ratingOval, unRatedPaint);
        }
    }

    public void drawRate(Canvas canvas, int index) {
        Rate arc = rates.get(index);
        arc.drawArc(canvas, ratingOval, ratedPaint);
    }

    public void drawShadow(Canvas canvas) {
        for (Rate arc : rates) {
            arc.drawArc(canvas, shadowOval, shadowPaint);
        }
    }

    public void drawName(Canvas canvas, int alpha) {
        Path path = new Path();
        float circumference = (float) (Math.PI * (outlineOval.right - outlineOval.left));
        float textAngle = (360 / circumference) * textPaint.measureText(getName());

        float startAngle = mStartAngle + mSweepAngle / 2 - textAngle / 2;

        if (isSingle) {
            // when single, draw 360 the path will be a circle
            path.addArc(outlineOval, startAngle - mSweepAngle / 2, mSweepAngle / 2);
        } else {
            path.addArc(outlineOval, startAngle, mSweepAngle);
        }

        textPaint.setAlpha(alpha);
        canvas.drawTextOnPath(mName, path, 0 , textWidth / 3 , textPaint);
    }

    public void drawOutLine(Canvas canvas) {

        float circumference = (float) (Math.PI * (outlineOval.right - outlineOval.left));
        float textAngle = (360 / circumference) * textPaint.measureText(getName());

        float sweepAngle = (mSweepAngle - textAngle - 1 - 1) / 2;
        // text left
        float leftStartAngle = mStartAngle;
        canvas.drawArc(outlineOval, leftStartAngle, sweepAngle, false, outlinePaint);
        // text right
        float rightStartAngle = mStartAngle + mSweepAngle - sweepAngle;
        canvas.drawArc(outlineOval, rightStartAngle, sweepAngle, false, outlinePaint);

        // canvas.drawArc(outlineOval, mStartAngle, mSweepAngle, false, outlinePaint);
    }

    public void setMaxRate(int maxRate) {
        this.maxRate = maxRate;
    }

    public void setRate(int curRate) {
        this.mCurRate = curRate;
    }

    public int getRate() {
        return mCurRate;
    }

    public void setStartAngle(float mStartAngle) {
        this.mStartAngle = mStartAngle;
    }

    public void setSweepAngle(float mSweepAngle) {
        this.mSweepAngle = mSweepAngle;
    }

    public void setCenterX(int mCenterX) {
        this.mCenterX = mCenterX;
    }

    public void setCenterY(int mCenterY) {
        this.mCenterY = mCenterY;
    }

    public void setIsSingle(boolean isSingle) {
        this.isSingle = isSingle;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public String getName() {
        return mName;
    }

    public void setProgressColor() {
        
    }

    /**
     * Rate class
     */
    public class Rate {
        private float startAngle, sweepAngle;

        public Rate(float startAngle, float sweepAngle) {
            this.startAngle = startAngle;
            this.sweepAngle = sweepAngle;
        }

        public void drawArc(Canvas canvas, RectF oval, Paint paint) {
            canvas.drawArc(oval, startAngle, sweepAngle, false, paint);
        }
    }

}
