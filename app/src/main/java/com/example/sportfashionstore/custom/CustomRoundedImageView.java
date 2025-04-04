package com.example.sportfashionstore.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

import com.example.sportfashionstore.R;

/**
 * Created April 04 2025 by Claude AI version 3.7 Sonnet
 */

public class CustomRoundedImageView extends AppCompatImageView {

    private static final ScaleType SCALE_TYPE = ScaleType.CENTER_CROP;
    private static final Bitmap.Config BITMAP_CONFIG = Bitmap.Config.ARGB_8888;
    private static final int DEFAULT_RADIUS = 0;

    private final RectF mDrawableRect = new RectF();
    private final RectF mBorderRect = new RectF();
    private final Path mPath = new Path();

    private final Matrix mShaderMatrix = new Matrix();
    private final Paint mBitmapPaint = new Paint();
    private final Paint mBorderPaint = new Paint();

    private int mBorderColor = Color.BLACK;
    private int mBorderWidth = DEFAULT_RADIUS;

    private Bitmap mBitmap;
    private BitmapShader mBitmapShader;
    private int mBitmapWidth;
    private int mBitmapHeight;

    // Corners radius
    private int mRadius = DEFAULT_RADIUS;
    private int mRadiusTopLeft = DEFAULT_RADIUS;
    private int mRadiusTopRight = DEFAULT_RADIUS;
    private int mRadiusBottomLeft = DEFAULT_RADIUS;
    private int mRadiusBottomRight = DEFAULT_RADIUS;

    private boolean mIsCircle = false;
    private float mDrawableRadius;
    private float mBorderRadius;

    private boolean mReady;
    private boolean mSetupPending;

    public CustomRoundedImageView(Context context) {
        super(context);
        init(context, null);
    }

    public CustomRoundedImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomRoundedImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        super.setScaleType(SCALE_TYPE);

        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomRoundedImageView);

            mBorderWidth = a.getDimensionPixelSize(R.styleable.CustomRoundedImageView_border_width, DEFAULT_RADIUS);
            mBorderColor = a.getColor(R.styleable.CustomRoundedImageView_border_color, Color.BLACK);

            mRadius = a.getDimensionPixelSize(R.styleable.CustomRoundedImageView_radius, DEFAULT_RADIUS);
            mRadiusTopLeft = a.getDimensionPixelSize(R.styleable.CustomRoundedImageView_radius_top_left, DEFAULT_RADIUS);
            mRadiusTopRight = a.getDimensionPixelSize(R.styleable.CustomRoundedImageView_radius_top_right, DEFAULT_RADIUS);
            mRadiusBottomLeft = a.getDimensionPixelSize(R.styleable.CustomRoundedImageView_radius_bottom_left, DEFAULT_RADIUS);
            mRadiusBottomRight = a.getDimensionPixelSize(R.styleable.CustomRoundedImageView_radius_bottom_right, DEFAULT_RADIUS);

            mIsCircle = a.getBoolean(R.styleable.CustomRoundedImageView_is_circle, false);

            a.recycle();
        }

        mReady = true;

        if (mSetupPending) {
            setup();
            mSetupPending = false;
        }
    }

    public void setRadius(int radius) {
        if (mRadius != radius) {
            mRadius = radius;

            // If radius is set, apply to all corners
            if (mRadius > 0) {
                mRadiusTopLeft = mRadius;
                mRadiusTopRight = mRadius;
                mRadiusBottomLeft = mRadius;
                mRadiusBottomRight = mRadius;
            }

            invalidate();
        }
    }

    public void setRadiusTopLeft(int radius) {
        if (mRadiusTopLeft != radius) {
            mRadiusTopLeft = radius;
            invalidate();
        }
    }

    public void setRadiusTopRight(int radius) {
        if (mRadiusTopRight != radius) {
            mRadiusTopRight = radius;
            invalidate();
        }
    }

    public void setRadiusBottomLeft(int radius) {
        if (mRadiusBottomLeft != radius) {
            mRadiusBottomLeft = radius;
            invalidate();
        }
    }

    public void setRadiusBottomRight(int radius) {
        if (mRadiusBottomRight != radius) {
            mRadiusBottomRight = radius;
            invalidate();
        }
    }

    public void setIsCircle(boolean isCircle) {
        if (mIsCircle != isCircle) {
            mIsCircle = isCircle;
            invalidate();
        }
    }

    public void setBorderWidth(int borderWidth) {
        if (mBorderWidth != borderWidth) {
            mBorderWidth = borderWidth;
            invalidate();
        }
    }

    public void setBorderColor(int borderColor) {
        if (mBorderColor != borderColor) {
            mBorderColor = borderColor;
            invalidate();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (getDrawable() == null) {
            return;
        }

        if (mIsCircle) {
            // Draw as circle
            canvas.drawCircle(getWidth() / 2.0f, getHeight() / 2.0f, mDrawableRadius, mBitmapPaint);
            if (mBorderWidth != DEFAULT_RADIUS) {
                canvas.drawCircle(getWidth() / 2.0f, getHeight() / 2.0f, mBorderRadius, mBorderPaint);
            }
        } else {
            // Draw with rounded corners
            canvas.drawPath(mPath, mBitmapPaint);
            if (mBorderWidth != DEFAULT_RADIUS) {
                canvas.drawPath(mPath, mBorderPaint);
            }
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        setup();
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        mBitmap = bm;
        setup();
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        mBitmap = getBitmapFromDrawable(drawable);
        setup();
    }

    @Override
    public void setImageResource(int resId) {
        super.setImageResource(resId);
        mBitmap = getBitmapFromDrawable(getDrawable());
        setup();
    }

    private Bitmap getBitmapFromDrawable(Drawable drawable) {
        if (drawable == null) {
            return null;
        }

        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        try {
            Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight(), BITMAP_CONFIG);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void setup() {
        if (!mReady) {
            mSetupPending = true;
            return;
        }

        if (mBitmap == null) {
            return;
        }

        mBitmapShader = new BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

        mBitmapPaint.setAntiAlias(true);
        mBitmapPaint.setShader(mBitmapShader);

        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setAntiAlias(true);
        mBorderPaint.setColor(mBorderColor);
        mBorderPaint.setStrokeWidth(mBorderWidth);

        mBitmapHeight = mBitmap.getHeight();
        mBitmapWidth = mBitmap.getWidth();

        mBorderRect.set(mBorderWidth / 2f, mBorderWidth / 2f,
                getWidth() - mBorderWidth / 2f, getHeight() - mBorderWidth / 2f);

        if (mIsCircle) {
            mBorderRadius = Math.min((mBorderRect.height()) / 2f,
                    (mBorderRect.width()) / 2f);

            mDrawableRect.set(mBorderWidth, mBorderWidth,
                    getWidth() - mBorderWidth, getHeight() - mBorderWidth);
            mDrawableRadius = Math.min(mDrawableRect.height() / 2f, mDrawableRect.width() / 2f);
        } else {
            mDrawableRect.set(mBorderWidth, mBorderWidth,
                    getWidth() - mBorderWidth, getHeight() - mBorderWidth);

            // Create path with rounded corners
            mPath.reset();

            float[] radii = new float[]{
                    mRadiusTopLeft, mRadiusTopLeft,
                    mRadiusTopRight, mRadiusTopRight,
                    mRadiusBottomRight, mRadiusBottomRight,
                    mRadiusBottomLeft, mRadiusBottomLeft
            };

            mPath.addRoundRect(mDrawableRect, radii, Path.Direction.CW);
        }

        updateShaderMatrix();
        invalidate();
    }

    private void updateShaderMatrix() {
        float scale;
        float dx = 0;
        float dy = 0;

        mShaderMatrix.set(null);

        if (mBitmapWidth * mDrawableRect.height() > mDrawableRect.width() * mBitmapHeight) {
            scale = mDrawableRect.height() / (float) mBitmapHeight;
            dx = (mDrawableRect.width() - mBitmapWidth * scale) * 0.5f;
        } else {
            scale = mDrawableRect.width() / (float) mBitmapWidth;
            dy = (mDrawableRect.height() - mBitmapHeight * scale) * 0.5f;
        }

        mShaderMatrix.setScale(scale, scale);
        mShaderMatrix.postTranslate(dx + mBorderWidth, dy + mBorderWidth);

        mBitmapShader.setLocalMatrix(mShaderMatrix);
    }
}
