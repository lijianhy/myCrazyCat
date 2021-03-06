package com.levi.cat;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;

public class PlayGroud extends SurfaceView implements OnTouchListener{
	private static final String TAG = "lijian";
	private static final int ROW = 10;
	private static final int CLOUMN = 10;
	private static final int OFFECT = 2;
	private static final int WIDTH = 20;
	private Paint paint;
	private Point[][] points;
	
	public PlayGroud(Context context) {
		super(context);
		paint = new Paint();
		points = new Point[ROW][CLOUMN];
		for (int i = 0; i < points.length; i++) {
			for (int j = 0; j < points[i].length; j++) {
				Point point = new Point(Math.random() >= 0.1 ? Point.STATE_OFF : Point.STATE_ON,
						i,j);
				points[i][j] = point;
			}
		}
		points[ROW/2-1][CLOUMN/2-1].setState(Point.STATE_IN);
		
		getHolder().addCallback(new Callback() {
			
			@Override
			public void surfaceDestroyed(SurfaceHolder holder) {
				
			}
			
			@Override
			public void surfaceCreated(SurfaceHolder holder) {
				reDraw();
			}
			
			@Override
			public void surfaceChanged(SurfaceHolder holder, int format, int width,
					int height) {
				
			}
		});
		setOnTouchListener(this);
	}
	
	public void reDraw(){
		Canvas canvas = getHolder().lockCanvas();
		canvas.drawColor(Color.GRAY);
		RectF rect = new RectF();
		for (int i = 0; i < points.length; i++) {
			for (int j = 0; j < points[i].length; j++) {
				Point point = points[i][j];
				switch(point.getState()){
				case Point.STATE_IN:
					paint.setColor(Color.YELLOW);
					break;
				case Point.STATE_OFF:
					paint.setColor(Color.WHITE);
					break;
				case Point.STATE_ON:
					paint.setColor(Color.RED);
					break;
				}
				rect.set(OFFECT*j+j*WIDTH + (i%2)*(WIDTH/2), OFFECT*i+i*WIDTH, 
						OFFECT*j+(j+1)*WIDTH + (i%2)*(WIDTH/2), OFFECT*i+(i+1)*WIDTH);
				canvas.drawOval(rect, paint);
			}
		}
		getHolder().unlockCanvasAndPost(canvas);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()) {
	    case MotionEvent.ACTION_DOWN:
	        Point p = getPointByXY((int)event.getX(), (int)event.getY());
	        pointClick(p);
	        break;
	    case MotionEvent.ACTION_UP:
	        v.performClick();
	        break;
	    default:
	        break;
	    }
		Log.v(TAG, "event x:"+event.getX()+" y:"+event.getY());
		return false;
	}
	
	private void pointClick(Point p) {
		if (p == null) {
			return;
		}
		switch (p.getState()) {
		case Point.STATE_OFF:
		case Point.STATE_IN:
			p.setState(Point.STATE_ON);
			reDraw();
			break;
		}
	}

	@Override
	public boolean performClick() {
		return super.performClick();
	}
	
	private Point getPointByXY(int x,int y){
		int arrayX,arrayY;
		arrayX = y/(OFFECT+WIDTH);
		if (x % 2 == 0) {
			arrayY = (x-WIDTH/2)/(OFFECT + WIDTH);
		} else {
			arrayY = x/(OFFECT + WIDTH);
		}
		if (arrayY >= CLOUMN || arrayX >= ROW || (x < OFFECT + (arrayX%2)*WIDTH/2)) {
			return null;
		}
		return points[arrayX][arrayY];
	}

}
