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
		for(int i = 0; i < data.length;++i) {
			if(i%2==0)
				data[i] = 
					(_data[i]-minA)/(maxA-minA) *
					View.MeasureSpec.getSize(this.widthMeasureSpec);
			else
				_data[i] =
					(_data[i]-minS)/(maxS-minS) *
					View.MeasureSpec.getSize(this.heightMeasureSpec);
		}
		_pointPaint = new Paint();
		_tickPaint = new Paint();
		_tickPaint.setColor(Color.DKGRAY);
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
			canvas.drawLine(0, 0, 0, heightMeasureSpec, _tickPaint);
			canvas.drawLine(0, 0, widthMeasureSpec, 0, _tickPaint);
		}
	}
	
}