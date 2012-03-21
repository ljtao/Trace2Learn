package edu.upenn.cis350;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class TtlView extends View {
    private static final int FADE_ALPHA = 0x06;
    private static final int MAX_FADE_STEPS = 256/FADE_ALPHA + 4;
    private static final int TRACKBALL_SCALE = 10;

    private Bitmap mBitmap;
    private Canvas mCanvas;
    private final Rect mRect = new Rect();
    private final Paint mPaint;
    private final Paint mFadePaint;
    private float mCurX;
    private float mCurY;
    private int mFadeSteps = MAX_FADE_STEPS;
    
    public TtlView(Context c) {
        super(c);
        setFocusable(true);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setARGB(255, 255, 255, 255);
        mFadePaint = new Paint();
        mFadePaint.setDither(true);
        mFadePaint.setARGB(FADE_ALPHA, 0, 0, 0);
    }
    
    public TtlView(Context c, AttributeSet as) {
        super(c, as);
        setFocusable(true);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setARGB(255, 255, 255, 255);
        mFadePaint = new Paint();
        mFadePaint.setDither(true);
        mFadePaint.setARGB(FADE_ALPHA, 0, 0, 0);
    }

    public void clear() {
        if (mCanvas != null) {
            mPaint.setARGB(0xff, 0, 0, 0);
            mCanvas.drawPaint(mPaint);
            invalidate();
            mFadeSteps = MAX_FADE_STEPS;
        }
    }
    
    public void fade() {
        if (mCanvas != null && mFadeSteps < MAX_FADE_STEPS) {
            mCanvas.drawPaint(mFadePaint);
            invalidate();
            mFadeSteps++;
        }
    }
    
    @Override protected void onSizeChanged(int w, int h, int oldw,
            int oldh) {
        int curW = mBitmap != null ? mBitmap.getWidth() : 0;
        int curH = mBitmap != null ? mBitmap.getHeight() : 0;
        if (curW >= w && curH >= h) {
            return;
        }
        
        if (curW < w) curW = w;
        if (curH < h) curH = h;
        
        Bitmap newBitmap = Bitmap.createBitmap(curW, curH,
                                               Bitmap.Config.RGB_565);
        Canvas newCanvas = new Canvas();
        newCanvas.setBitmap(newBitmap);
        if (mBitmap != null) {
            newCanvas.drawBitmap(mBitmap, 0, 0, null);
        }
        mBitmap = newBitmap;
        mCanvas = newCanvas;
        mFadeSteps = MAX_FADE_STEPS;
    }
    
    @Override protected void onDraw(Canvas canvas) {
        if (mBitmap != null) {
            canvas.drawBitmap(mBitmap, 0, 0, null);
        }
    }

    @Override public boolean onTrackballEvent(MotionEvent event) {
        int N = event.getHistorySize();
        final float scaleX = event.getXPrecision() * TRACKBALL_SCALE;
        final float scaleY = event.getYPrecision() * TRACKBALL_SCALE;
        for (int i=0; i<N; i++) {
            //Log.i("TouchPaint", "Intermediate trackball #" + i
            //        + ": x=" + event.getHistoricalX(i)
            //        + ", y=" + event.getHistoricalY(i));
            mCurX += event.getHistoricalX(i) * scaleX;
            mCurY += event.getHistoricalY(i) * scaleY;
            drawPoint(mCurX, mCurY, 1.0f, 16.0f);
        }
        //Log.i("TouchPaint", "Trackball: x=" + event.getX()
        //        + ", y=" + event.getY());
        mCurX += event.getX() * scaleX;
        mCurY += event.getY() * scaleY;
        drawPoint(mCurX, mCurY, 1.0f, 16.0f);
        return true;
    }
    
    @Override public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if (action != MotionEvent.ACTION_UP && action != MotionEvent.ACTION_CANCEL) {
            int N = event.getHistorySize();
            int P = event.getPointerCount();
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < P; j++) {
                    mCurX = event.getHistoricalX(j, i);
                    mCurY = event.getHistoricalY(j, i);
                    drawPoint(mCurX, mCurY,
                            event.getHistoricalPressure(j, i),
                            event.getHistoricalSize(j, i));
                }
            }
            for (int j = 0; j < P; j++) {
                mCurX = event.getX(j);
                mCurY = event.getY(j);
                drawPoint(mCurX, mCurY, 100, event.getHistoricalSize(j));
                //100 replaced event.getPressure(j)
            }
        }
        return true;
    }
    
    private void drawPoint(float x, float y, float pressure, float width) {
        //Log.i("TouchPaint", "Drawing: " + x + "x" + y + " p="
        //        + pressure + " width=" + width);
        if (width < 1) width = 1;
        if (mBitmap != null) {
            float radius = width / 2;
            int pressureLevel = (int)(pressure * 255);
            mPaint.setARGB(pressureLevel, 255, 255, 255);
            mCanvas.drawCircle(x, y, 5, mPaint);
            //5 replaced "radius"
            mRect.set((int) (x - radius - 2), (int) (y - radius - 2),
                    (int) (x + radius + 2), (int) (y + radius + 2));
            invalidate(mRect);
        }
        mFadeSteps = 0;
    }
}
