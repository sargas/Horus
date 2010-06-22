package net.neoturbine.horus;

import net.neoturbine.horus.view.GraphView;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;

/**
 * Activity for viewing orbit diagrams as described in TODO
 * @author Joseph Booker
 *
 */
public class OrbitDiagram extends Activity {
	/**
	 * The GraphView object showing the orbit diagram.
	 */
	private GraphView orbit;
	/**
	 * GraphData containing the data
	 */
	private GraphData _data;

	/**
	 * Called when Activity is created. Reloads or recreates the orbit diagram.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		orbit = (GraphView) findViewById(R.id.orbit);

		final Object data = getLastNonConfigurationInstance();

		if(data == null)
			(new calcTask()).execute(0,1);
		else
			setData((GraphData)data);
	}

	/**
	 * Called when changing orientation for saving state.
	 * @return the GraphData object for later use.
	 */
	@Override
	public Object onRetainNonConfigurationInstance() {
		return _data;
	}

	/**
	 * Saves computed data and loads it into the GraphView.
	 * @param result GraphData containing the scatter plot
	 * @see GraphData
	 */
	private void setData(GraphData result) {
		_data = result;
		orbit.setData(
				result.getData(),
				result.getMinX(),
				result.getMinY(),
				result.getMaxX(),
				result.getMaxY());
	}

	/**
	 * Helper class for performing orbit diagrams in separate thread.
	 * @author Joseph Booker
	 *
	 */
	private class calcTask extends AsyncTask<Integer,Integer,GraphData> {
		/**
		 * Calculations to perform in another thread.
		 * @param params ignored for now, but eventually going to let resolutions through it
		 * @return the calculated data.
		 */
		protected GraphData doInBackground(Integer... params) {
			final int resA = 420;
			final int perit = 350;

			int j = 0;
			float[] data = new float[resA*perit*2];
			float minScatter = Float.MAX_VALUE;
			float maxScatter = Float.MIN_VALUE;

			for(int z=0;z<resA;z++) {
				double A = 1.5 + (2.59807621135-1.5)/resA * z;
				double x = Math.random();
				for(int k=1;k<200;++k) {
					x = (A*x*(1-Math.pow(x,2)));
				}
				for(int i=j; i<j+perit-1;++i) {
					x = A*x*(1-Math.pow(x,2));
					if(x < minScatter) minScatter = (float)x;
					else if(x > maxScatter) maxScatter = (float)x;
					data[2*i] = (float)A;
					data[2*i+1] = (float)x;
				}
				j=j+perit;
			}
			return new GraphData(data,1.5f,minScatter,2.59807621135f,maxScatter);
		}

		/**
		 * Calls within the UI thread with the data calculated.
		 * @param result the data calculated in a separate thread
		 */
		protected void onPostExecute(GraphData result) {
			OrbitDiagram.this.setData(result);
		}
	}
}