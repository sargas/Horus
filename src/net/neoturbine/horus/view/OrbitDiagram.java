package net.neoturbine.horus.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

//TODO: javadoc
public class OrbitDiagram extends View {
	int widthMeasureSpec;
	int heightMeasureSpec;
	float[] _data;
	Paint _paint;
	
	public OrbitDiagram(Context context) {
		super(context);
	}
	public OrbitDiagram(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	public OrbitDiagram(Context context, AttributeSet attrs, int defStyle) {
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
		_paint = new Paint();
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
			canvas.drawPoints(_data, _paint);
		}
	}
	
}