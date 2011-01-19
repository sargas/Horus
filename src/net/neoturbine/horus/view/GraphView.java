package net.neoturbine.horus.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

//TODO: javadoc
public class GraphView extends View {
	int widthMeasureSpec;
	int heightMeasureSpec;
	float[] _data;
	Paint _pointPaint;
	Paint _tickPaint;
	
	public GraphView(Context context) {
		super(context);
	}
	public GraphView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	public GraphView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	public void setData(float[] data, 
			float minA, float minS,
			float maxA, float maxS) {
		_data = data;
		
		final int width = View.MeasureSpec.getSize(this.widthMeasureSpec);
		final int height = View.MeasureSpec.getSize(this.heightMeasureSpec);
		final float factorA = width/(maxA-minA);
		final float factorS = height/(maxS-minS);
		final float constFactorA = factorA * minA;
		final float constFactorS = factorS * minS;
		final int dataLength = data.length;
		
		for(int i = 0; i < dataLength;++i) {
			if(i%2==0)
				_data[i] = _data[i]*factorA - constFactorA;
			else
				_data[i] = _data[i]*factorS - constFactorS;
		}
		_pointPaint = new Paint();
		_tickPaint = new Paint();
		_tickPaint.setColor(Color.DKGRAY);
		_tickPaint.setStrokeWidth(10);
		invalidate();
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		setMeasuredDimension(widthMeasureSpec,heightMeasureSpec);
		this.widthMeasureSpec = widthMeasureSpec;
		this.heightMeasureSpec = heightMeasureSpec;
	}
	
	protected void onDraw(Canvas canvas) {
		if(_data != null) {
			canvas.drawColor(Color.WHITE);
			canvas.drawPoints(_data, _pointPaint);
			
			//draw axes
			canvas.drawLine(0, 0, 10, heightMeasureSpec, _tickPaint);
			canvas.drawLine(0, 0, widthMeasureSpec, 10, _tickPaint);
		}
	}
	
}