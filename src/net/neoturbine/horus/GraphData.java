package net.neoturbine.horus;

public final class GraphData {
	private final float[] _data;
	private final float _minX, _minY, _maxX, _maxY;
	public GraphData(float[] data, 
			float minX, float minY,
			float maxX, float maxY) {
		_data = data;
		_minX = minX; _maxX = maxX;
		_minY = minY; _maxY = maxY;
	}
	public float[] getData() {return _data;}
	public float getMinX() {return _minX;}
	public float getMinY() {return _minY;}
	public float getMaxX() {return _maxX;}
	public float getMaxY() {return _maxY;}
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