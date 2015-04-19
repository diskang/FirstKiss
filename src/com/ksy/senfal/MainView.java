package com.ksy.senfal;

import com.ksy.senfal.activity.CalAccTask;
import com.ksy.senfal.fragment.CanvasDrawFragment;
import com.ksy.senfal.fragment.TagAccDataFragment;
import com.ksy.firstkiss.R;


import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

public class MainView extends FragmentActivity implements OnClickListener{
	private SensorManager mSensorManager;
	private CalAccTask mCalAccTask;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.senfal_main_view);
		initWidget();
		 // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (findViewById(R.id.senfal_sensor_canvas_fragment) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            // Create a new Fragment to be placed in the activity layout
//            HeadlinesFragment firstFragment = 
            
            // In case this activity was started with special instructions from an
            // Intent, pass the Intent's extras to the fragment as arguments
//            firstFragment.setArguments(getIntent().getExtras());
            
            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.senfal_sensor_canvas_fragment, new CanvasDrawFragment()).commit();
        }

	}
	@Override
	protected void onResume() {
		super.onResume();
		mCalAccTask = new CalAccTask(getApplicationContext());
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
    	mSensorManager.registerListener(mCalAccTask,
                mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION),
                SensorManager.SENSOR_DELAY_FASTEST);
	}
	@Override
	protected void onDestroy() {
		
		mSensorManager.unregisterListener(mCalAccTask);
    	super.onDestroy();
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {  
			case R.id.senfal_main_button:   
	          try {  
	          	Intent intent = new Intent();
					intent.setClass(MainView.this, MainView.class);
					startActivity(intent); 
					MainView.this.finish();
	          } catch (Exception e) {  
	              // TODO Auto-generated catch block  
	              e.printStackTrace();  
	              //Toast.makeText(RecordAccDataActivity.this, R.string.ok_error, Toast.LENGTH_SHORT).show();  
	          }  
	          break;  
			case R.id.senfal_record_button: 
				
//				getSupportFragmentManager().popBackStack();
				
				FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
				// Replace whatever is in the fragment_container view with this fragment,
				// and add the transaction to the back stack
				transaction.replace(R.id.senfal_sensor_canvas_fragment,new TagAccDataFragment());
				Log.i("after1","nullpointer");
//				transaction.add(R.id.senfal_main,new TagAccDataFragment());
//				Log.i("after2","nullpointer");
//				transaction.add(newFragment, "new");
				transaction.addToBackStack(null);
				getSupportFragmentManager().executePendingTransactions();
				// Commit the transaction
				transaction.commit();
			break;   
		}
	}
	private void initWidget(){
		findViewById(R.id.senfal_main_button).setOnClickListener(this);
		findViewById(R.id.senfal_record_button).setOnClickListener(this);
		
		}
}
