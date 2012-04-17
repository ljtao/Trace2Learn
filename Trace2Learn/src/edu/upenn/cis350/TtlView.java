package edu.upenn.cis350;

import java.util.ArrayList;

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
    //private final Rect mRect = new Rect();
    private final Paint mPaint;
    
    private final float width = 8;

    private float mCurX;
    private float mCurY;
    
    private String paths = null;
    
    private ArrayList<Line> lineList;
    
    private Line curLine;
    
    public ArrayList<Line> getLineList() {
    	return lineList;
    }
    
    public String getLineListAsString() {
    	String ret = "";
    	boolean b = false;
    	for (Line l : lineList) {
    		if (b == true) {
    			ret+="~";
    		}
    		b=true;
    		ret += l.toString();
    	}
    	return ret;
    }
    
    public ArrayList<Line> getLineListFromString(String input) {
    	ArrayList<Line> list = new ArrayList<Line>();
    	for (String s : input.split("\\~")) {
    		Line l = new Line();
    		for (String s2 : s.split("\\|")) {
    			String sx = s2.split(",")[0];
    			String sy = s2.split(",")[1];
    			
    			float x = Float.parseFloat(sx);
    			float y = Float.parseFloat(sy);
    			Point p = new Point(x,y);
    			l.addPoint(p);
    		}
    		list.add(l);
    	}
    	
    	
    	return list;
    	
    }
    
    public void DrawFromString(String s) {
    	
    	ArrayList<Line> list = getLineListFromString(s);
    	
    	for (Line line : list) {
    		
    		Point prev = null;
    		for (Point p : line.getPointList()) {
    			drawPoint(p);
    			if (prev != null) {
    				drawLine(prev,p);
    			}
    			
    			prev = p;
    		}
    		
    	}
    	
    }
    
    public void tryDrawChar(String paths) {
    	if (paths != null && !paths.equals("")) {
    		this.DrawFromString(paths);
    	}
    }

    
    public TtlView(Context c) {
        super(c);
        setFocusable(true);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setARGB(255, 255, 255, 255);

        lineList = new ArrayList<Line>();
        
        
    }
    
    public TtlView(Context c, AttributeSet as) {
        super(c, as);
        setFocusable(true);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setARGB(255, 255, 255, 255);
        
        lineList = new ArrayList<Line>();

    }

    public void clear() {
        if (mCanvas != null) {
            mPaint.setARGB(0xff, 0, 0, 0);
            mCanvas.drawPaint(mPaint);
            invalidate();
            
            lineList.clear();
 
        }
    }
    
    public void setChar(String paths) {
    	this.paths = paths;
    	
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
    }
    
    @Override protected void onDraw(Canvas canvas) {
        if (mBitmap != null) {
            canvas.drawBitmap(mBitmap, 0, 0, null);
        }
        
        tryDrawChar(paths);
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
            drawPoint(mCurX, mCurY);
        }
        //Log.i("TouchPaint", "Trackball: x=" + event.getX()
        //        + ", y=" + event.getY());
        mCurX += event.getX() * scaleX;
        mCurY += event.getY() * scaleY;
        drawPoint(mCurX, mCurY);
        return true;
    }
    
    @Override public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
        	curLine = new Line();

            
        	
        }
        
        
        if (action == MotionEvent.ACTION_UP) {
        	lineList.add(curLine);
        	
        	
        }
        
        if (action != MotionEvent.ACTION_UP && action != MotionEvent.ACTION_CANCEL) {
            int N = event.getHistorySize();
//            int P = event.getPointerCount();
//            P = 1;


            
            for (int i = 0; i < N; i++) {

                    mCurX = event.getHistoricalX(i);

                    mCurY = event.getHistoricalY(i);

                    Point p = new Point(mCurX, mCurY);
                    
                    if (curLine.getLastPoint() != null) {
                    	drawLine(curLine.getLastPoint(), p);
                    }
                    
                    drawPoint(p);
                    
                    curLine.addPoint(p);

            }
            
            
            
            Point p = new Point(event.getX(), event.getY());
            drawPoint(p);
            if (curLine.getLastPoint() != null) {
            	drawLine(curLine.getLastPoint(), p);
            }
            
            curLine.addPoint(p);

            
        }
        return true;
    }
    
    private void drawPoint(Point p) {
    	drawPoint(p.getX(), p.getY());
    }
    
    private void drawPoint(float x, float y) {
        
        if (mBitmap != null) {

            mPaint.setARGB(255, 255, 255, 255);
            
            mCanvas.drawCircle(x, y, (float)(width/2), mPaint);

            invalidate();
        }

    }
    
    private void drawLine(Point p1, Point p2) {
    	
    	drawLine(p1.getX(), p1.getY(), p2.getX(), p2.getY());
    }
    
    public void testDraw() {
    	drawLine(300,300,500,500);
    }
    
    private void drawLine(float x1, float y1, float x2, float y2) {


        if (mBitmap != null) {

            mPaint.setARGB(255, 255, 255, 255);
            mPaint.setStrokeWidth(width);
            //mPaint.setStrokeJoin(Paint.Join.ROUND);
            //mPaint.setStrokeMiter(999);
            mPaint.setStrokeCap(Paint.Cap.ROUND);
            mCanvas.drawLine(x1, y1, x2, y2, mPaint);
            


            invalidate();
        }

    }
}
