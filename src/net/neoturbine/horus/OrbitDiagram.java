package net.neoturbine.horus;

import expr.Expr;
import expr.Parser;
import expr.SyntaxException;
import expr.Variable;
import expr.VariableFactory;
import net.neoturbine.horus.view.GraphView;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

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
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.main);
		
		orbit = (GraphView) findViewById(R.id.orbit);

		final Object data = getLastNonConfigurationInstance();

		if(data == null) {
			setTitle(R.string.working);
			setProgressBarIndeterminateVisibility(true);
			(new calcTask()).execute(0,1);
		} else
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
		//setTitle(result.getExpression().toString());
		setTitle("Calculated"); //TODO: replace by map string
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
			double minScatter = Float.MAX_VALUE;
			double maxScatter = Float.MIN_VALUE;
			
			Expr expr;
			Variable var_x = VariableFactory.make("x");
			Variable var_A = VariableFactory.make("A");
			Parser parser = new Parser();
			parser.allow(var_x);parser.allow(var_A);
			try {
				expr = parser.parseString("A*(x-x^3)");
			} catch (SyntaxException e) {
				Log.v("Horus",e.explain());
				return null;
			}

			float A = 1.5f;
			final float diffA = (2.59807621135f-1.5f)/resA;
			for(int z=0; z<resA; z++,A += diffA) {
				var_A.setValue(A);
				var_x.setValue(Math.random());
				for(int k=0;k<200;++k) {
					var_x.setValue(expr.value());
				}
				for(int i=2*j; i<2*(j+perit-1);i+=2) {
					var_x.setValue(expr.value());
					final double xval = var_x.value();
					if(xval < minScatter)
						minScatter = xval;
					else if(xval > maxScatter)
						maxScatter = xval;
					data[i] = A;
					data[i+1] = (float)xval;
				}
				j=j+perit;
			}
			return new GraphData(data,1.5f,(float)minScatter,
					2.59807621135f,(float)maxScatter,expr);
		}

		/**
		 * Calls within the UI thread with the data calculated.
		 * @param result the data calculated in a separate thread
		 */
		protected void onPostExecute(GraphData result) {
			OrbitDiagram.this.setData(result);
			OrbitDiagram.this.setProgressBarIndeterminateVisibility(false);
		}
	}
}