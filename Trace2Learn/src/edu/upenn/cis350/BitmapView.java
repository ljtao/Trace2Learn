package edu.upenn.cis350;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

public class BitmapView extends View {
	Bitmap mBitmap = null;
	
	public BitmapView(Context context, AttributeSet as) {
		super(context,as);
	}

	@Override
	protected void onDraw(Canvas canvas) {
	// called when view is drawn
	Paint paint = new Paint();
	paint.setFilterBitmap(true);
	// The image will be scaled so it will fill the width, and the
	// height will preserve the image�s aspect ration
	double aspectRatio = ((double) mBitmap.getWidth()) / mBitmap.getHeight();
	Rect dest = new Rect(0, 0, this.getWidth(),(int) (this.getHeight() / aspectRatio));
	canvas.drawBitmap(mBitmap, null, dest, paint);
	}
	
	protected void setBMP(Bitmap bm){
		mBitmap = bm;
	}
}
