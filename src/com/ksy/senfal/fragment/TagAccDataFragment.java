package com.ksy.senfal.fragment;



import com.ksy.firstkiss.R;
import com.ksy.senfal.activity.RecordAccData;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

public class TagAccDataFragment extends Fragment{
	private Context mcontext;
	/*0:	1:	2:	3:
	 * 4:	5:	6:	7
	 * 8:	9:	10:	11:*/
	private int recordType=0;
	private String[] recordTypeTable ={"slip", "walk","quiet", "upstair",
	                                   "standup", "downstair","sitdown", "jump",
	                                   "running", "playwith", "call", "work"};
	private SensorManager mSensorManager;
	private RecordAccData mRecordAcc;
    @Override 
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	mcontext=getActivity().getApplicationContext();
    
    }
    @Override
 	public void onResume() {
         super.onResume();
         mSensorManager = (SensorManager) mcontext.getSystemService(Context.SENSOR_SERVICE);
    }
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.senfal_acc_record, container, false);
        
    }
    @Override
    public void onPause(){
//    	Log.w("onpause1","should disappear");
    	super.onPause();
    }
    @Override 
    public void onStart() {
    	super.onStart();
    	GridView gridview = (GridView) getActivity().findViewById(R.id.senfal_gridview);
        gridview.setAdapter(new ImageAdapter(mcontext));

        gridview.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Toast.makeText(mcontext, "" + position, Toast.LENGTH_SHORT).show();
            	recordType=position;
            }
        });
    	initButton();
    }
    private void initButton() {
    	final Button startbutton = (Button) getActivity().findViewById(R.id.senfal_record_start);
    	startbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
            	mRecordAcc = new RecordAccData(mcontext,recordTypeTable[recordType]);
            	mSensorManager.registerListener(mRecordAcc,
                        mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION),
                        SensorManager.SENSOR_DELAY_FASTEST);
            }
        });
        final Button endbutton = (Button) getActivity().findViewById(R.id.senfal_record_end);
        endbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	try{
            		//,,,
            		if(mRecordAcc!=null){
            			mRecordAcc.finish();
            		}
                	mSensorManager.unregisterListener(mRecordAcc);
                }
                catch (Exception e)
                {
                    Log.e("unregisterListner Error", e.getMessage());
                }
            }
        });
		
	}

    public class ImageAdapter extends BaseAdapter {
        private Context mContext;

        public ImageAdapter(Context c) {
            mContext = c;
        }

        public int getCount() {
            return mThumbIds.length;
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        // create a new ImageView for each item referenced by the Adapter
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if (convertView == null) {  // if it's not recycled, initialize some attributes
                imageView = new ImageView(mContext);
                imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setPadding(0, 0, 0, 0);
            } else {
                imageView = (ImageView) convertView;
            }

            imageView.setImageResource(mThumbIds[position]);
            return imageView;
        }

        // references to our images
        private Integer[] mThumbIds = {
                R.drawable.senfal_record_slip, R.drawable.senfal_record_walk,
                R.drawable.senfal_record_quiet, R.drawable.senfal_record_upstair,
                R.drawable.senfal_record_standup, R.drawable.senfal_record_downstair,
                R.drawable.senfal_record_sitdown, R.drawable.senfal_record_jump,
                R.drawable.senfal_record_running, R.drawable.senfal_record_playwith2,
                R.drawable.senfal_record_call,R.drawable.senfal_record_work,
        };
    }
}
