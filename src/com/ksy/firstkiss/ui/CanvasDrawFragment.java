package com.ksy.firstkiss.ui;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CanvasDrawFragment extends Fragment{
	private Context mcontext;
    private GraphView mGraphView;
    private SensorManager mSensorManager;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        // Inflate the layout for this fragment
    	super.onCreateView(inflater, container, savedInstanceState);
    	
    	mGraphView= new GraphView(mcontext);
//    	mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        return mGraphView;
//        return inflater.inflate(R.layout.senfal_sensor_canvas_fragment, container, false);
    }
    @Override 
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	mcontext=getActivity().getApplicationContext();

    }
    @Override
	public void onResume() {
        super.onResume();
        mSensorManager = (SensorManager) mcontext.getSystemService(Context.SENSOR_SERVICE);
    	mSensorManager.registerListener(mGraphView,
                mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION),
                SensorManager.SENSOR_DELAY_FASTEST);
//        mSensorManager.registerListener(mGraphView,
//                mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
//                SensorManager.SENSOR_DELAY_FASTEST);
        mSensorManager.registerListener(mGraphView,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_FASTEST);
    }
    @Override
    public void onPause(){
//    	((ViewGroup)mGraphView.getParent()).removeView(mGraphView);
    	try{
        	mSensorManager.unregisterListener(mGraphView);
        	
        }
        catch (Exception e)
        {
            Log.w("stop_warn", e.getMessage());
        }
    	super.onPause();
//    	Log.w("onpause1","should disappear");
    }
    @Override
	public void onStop() {
        super.onStop();
    }
    
}
