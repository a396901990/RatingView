package com.deanguo.ratingview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by DeanGuo on 12/20/15.
 */
public class RatingView extends View {

    /**
     * The rated area bar color.
     */
    private int mRatedBarColor;

    /**
     * The bar unrated area color.
     */
    private int mUnratedBarColor;

    /**
     * The rating text color.
     */
    private int mTextColor;

    /**
     * The rating text size.
     */
    private float mTextSize;

    /**
     * The rating title color.
     */
    private int mTitleColor;

    /**
     * The rating title size.
     */
    private float mTitleSize;

    /**
     * The height of the rated area.
     */
    private float mRatedBarHeight;

    /**
     * The height of the unrated area.
     */
    private float mUnratedBarHeight;

    /**
     * The Paint of the reached area.
     */
    private Paint mRatedBarPaint;
    /**
     * The Paint of the unreached area.
     */
    private Paint mUnratedBarPaint;
    /**
     * The Paint of the progress text.
     */
    private Paint mTextPaint;

    /**
     * The Paint of the progress text title.
     */
    private Paint mTitlePaint;

    /**
     * Unrated bar area to draw rect.
     */
    private RectF mUnratedRectF = new RectF(0, 0, 0, 0);
    /**
     * Rated bar area rect.
     */
    private RectF mRatedRectF = new RectF(0, 0, 0, 0);

    /**
     * The rating text offset.
     */
    private float mOffset;

    /**
     * The text that to be drawn in onDraw().
     */
    private String mText;

    /**
     * The title that to be drawn in onDraw().
     */
    private String mTitle;

    private boolean mIfDrawText = true;

    private boolean mIfDrawTitle = true;

    private final int default_text_color = Color.rgb(66, 145, 241);
    private final int default_rated_color = Color.rgb(66, 145, 241);
    private final int default_unrated_color = Color.rgb(204, 204, 204);
    private final float default_rating_text_offset = dp2px(1.5f);
    private final float default_text_size = dp2px(1.0f);
    private final float default_rated_bar_height = sp2px(10);
    private final float default_unrated_bar_height = dp2px(3.0f);

    public enum ProgressTextVisibility {
        Visible, Invisible
    }

    private static final int RATING_TEXT_VISIBLE = 0;
    private static final int RATING_TITLE_VISIBLE = 0;

    public RatingView(Context context) {
        super(context);
    }

    public RatingView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RatingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //load styled attributes.
        final TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.RatingView,
                defStyleAttr, 0);

        mRatedBarColor = attributes.getColor(R.styleable.RatingView_rating_rated_color, default_rated_color);
        mUnratedBarColor = attributes.getColor(R.styleable.RatingView_rating_unrated_color, default_unrated_color);
        mTextColor = attributes.getColor(R.styleable.RatingView_rating_text_color, default_text_color);
        mTextSize = attributes.getDimension(R.styleable.RatingView_rating_text_size, default_text_size);

        mTitle = attributes.getString(R.styleable.RatingView_rating_title);
        if (TextUtils.isEmpty(mTitle)) {
            mTitle = "";
        }
        mTitleColor = attributes.getColor(R.styleable.RatingView_rating_title_color, Color.WHITE);
        mTitleSize = attributes.getDimension(R.styleable.RatingView_rating_title_size, default_text_size);

        mRatedBarHeight = attributes.getDimension(R.styleable.RatingView_rating_rated_bar_height, default_rated_bar_height);
        mUnratedBarHeight = attributes.getDimension(R.styleable.RatingView_rating_unrated_bar_height, default_unrated_bar_height);
        mOffset = attributes.getDimension(R.styleable.RatingView_rating_text_offset, default_rating_text_offset);

        int textVisible = attributes.getInt(R.styleable.RatingView_rating_text_visibility, RATING_TEXT_VISIBLE);
        if (textVisible != RATING_TEXT_VISIBLE) {
            mIfDrawText = false;
        }

        int titleVisible = attributes.getInt(R.styleable.RatingView_rating_title_visibility, RATING_TITLE_VISIBLE);
        if (titleVisible != RATING_TITLE_VISIBLE) {
            mIfDrawTitle = false;
        }

        attributes.recycle();
        initPaint();
    }

    private void initPaint() {
        mRatedBarPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRatedBarPaint.setColor(mRatedBarColor);

        mUnratedBarPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mUnratedBarPaint.setColor(mUnratedBarColor);

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextSize(mTextSize);

        mTitlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTitlePaint.setColor(mTitleColor);
        mTitlePaint.setTextSize(mTitleSize);
    }

    public float dp2px(float dp) {
        final float scale = getResources().getDisplayMetrics().density;
        return dp * scale + 0.5f;
    }

    public float sp2px(float sp) {
        final float scale = getResources().getDisplayMetrics().scaledDensity;
        return sp * scale;
    }
}
