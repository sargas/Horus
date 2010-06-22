package net.neoturbine.horus;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;

public class OrbitDiagram extends Activity {
	private net.neoturbine.horus.view.OrbitDiagram orbit;
    private GraphData _data;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        orbit = (net.neoturbine.horus.view.OrbitDiagram)
        	findViewById(R.id.orbit);
        
        final Object data = getLastNonConfigurationInstance();

        if(data == null)
        	(new calcTask()).execute(0,1);
        else
        	setData((GraphData)data);
    }
    
    @Override
    public Object onRetainNonConfigurationInstance() {
    	return _data;
    }
    
    private void setData(GraphData result) {
    	_data = result;
    	orbit.setData(
			result.getData(),
			result.getMinX(),
			result.getMinY(),
			result.getMaxX(),
			result.getMaxY());
    }
    
    private class calcTask extends AsyncTask<Integer,Integer,GraphData> {
    	protected GraphData doInBackground(Integer... params) {
    		final int resA = 120;
            final int perit = 65;
            
            int j = 0;
            float[] data = new float[resA*perit*2];
            float minScatter = Float.MAX_VALUE;
            float maxScatter = Float.MIN_VALUE;
            
            for(int z=0;z<resA;z++) {
            	double A = (1.5 + (2.59807621135-1.5)/resA * z);
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
    	
    	protected void onPostExecute(GraphData result) {
    		OrbitDiagram.this.setData(result);
    	}
    }
}