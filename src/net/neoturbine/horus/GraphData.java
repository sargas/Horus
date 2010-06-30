package net.neoturbine.horus;

import expr.Expr;

public final class GraphData {
	private final float[] _data;
	private final float _minX, _minY, _maxX, _maxY;
	private final Expr _expr;
	public GraphData(float[] data, 
			float minX, float minY,
			float maxX, float maxY,
			Expr expr) {
		_data = data;
		_minX = minX; _maxX = maxX;
		_minY = minY; _maxY = maxY;
		_expr = expr;
	}
	public float[] getData() {return _data;}
	public float getMinX() {return _minX;}
	public float getMinY() {return _minY;}
	public float getMaxX() {return _maxX;}
	public float getMaxY() {return _maxY;}
	public Expr getExpression() {return _expr; }
	public String toString() {
		StringBuilder out = new StringBuilder();
		out .append("Independent Axis (")
			.append(getMinX())
			.append(',')
			.append(getMaxX())
			.append("), Dependent Axis (")
			.append(getMinY())
			.append(',')
			.append(getMaxY())
			.append(')');
		return out.toString();
	}
}