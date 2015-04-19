package com.ksy.senfal.activity;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.Context;  
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.Log;
import android.widget.SimpleAdapter;

import com.ksy.senfal.DataService;

public class RecordAccData  implements SensorEventListener{
	private DataService fdata;
	SimpleAdapter mSchedule;
	public RecordAccData(Context context,String recordTag){
		fdata = new DataService(context,generateFilename(recordTag));
		try {
			fdata.startFileStream();
		} catch (FileNotFoundException e) {
			Log.e("data service start",e.getMessage());
		}
	}
	public void finish(){
		if(fdata!=null){
			try {
				fdata.closeFileStream();
			} catch (IOException e) {
				Log.e("data service finish",e.getMessage());
			}
		}
	}
	@SuppressLint("SimpleDateFormat")
	private String generateFilename(String recordTag){
		DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        Date date = new Date();
        return recordTag+"_"+dateFormat.format(date);
	}

	@Override
	public final void onSensorChanged(SensorEvent event) {
	// The light sensor returns a single value.
	// Many sensors return 3 values, one for each axis.event.values[0];1,2
		if (event.sensor.getType()==Sensor.TYPE_LINEAR_ACCELERATION){
			String samplePoint=event.values[0]+" "+event.values[1]+" "+event.values[2]+"\n";
			try {
				fdata.saveFileStream(samplePoint);
			} catch (Exception e) {
				e.printStackTrace();
			}

//			else if(this.CURRENT_STATE==RecordAccDataActivity.ACC_DETECT_STATE){
//				mylist.clear();
//				for(int i=0;i<3;i++) 
//			    {  
//			        HashMap<String, String> map = new HashMap<String, String>();  
//			        sum[i]+=event.values[i];
//			        squareSum[i]+=event.values[i]*event.values[i];
//			        map.put("ItemTitle", "item"+i);  
//			        map.put("ItemText", "="+event.values[i]);
//			        mylist.add(map);
//			    } 
//			    accSquareSum+=event.values[0]*event.values[0]+event.values[1]*event.values[1]+event.values[2]*event.values[2];
//			    accSum+=Math.sqrt(accSquareSum);
//			    bufferZ[counter]=event.values[2];
//			    bufferX[counter]=event.values[0];
//			    bufferY[counter]=event.values[1];
//			    //Ìí¼Ó²¢ÇÒÏÔÊ¾  
////			    list.setAdapter(mSchedule);
//			    counter++;
//			    if(counter==20){
//			    	end_time = System.nanoTime();
//			    	Log.i("time",end_time-start_time+"");
//			    	start_time=end_time;
//			    	detect_fall();
//			    	counter=0;
//			    	sum[0]=sum[1]=sum[2]=0;
//					squareSum[0]=squareSum[1]=squareSum[2]=0;
//					accSum=0;
//					accSquareSum=0;
//			    }
//			}
		}
	
//	Log.i("123",a1+"");
//	Log.i("123",a2+"");
//	Log.i("123",a3+"");
	// Do something with this sensor value.
	}

	@Override
	public final void onAccuracyChanged(Sensor sensor, int accuracy) {
	// Do something here if sensor accuracy changes.
	}
//	private void detect_fall(){
//		float ex=sum[0]/20;
//		float ey=sum[1]/20;
//		float ez=sum[2]/20;
//		float dx=squareSum[0]/20-ex*ex;
//		float dy=squareSum[1]/20-ey*ey;
//		float dz=squareSum[2]/20-ez*ez;
//		float a=accSum/20;
//		float da=accSquareSum/20-a*a;
//		if(a>1.172836){
//			if(ez<=0.718164){
//				if(dz>0.718164){
//					if(a<=1.309688){
//						if (ez<=-0.297461 || ez>0.033008) {
//							Log.i("fall","1fall!ez"+ez+" a:"+a);
//						}
//						else{
//							//no
//						}
//					}
//					else{
//						if(da<1.334591){
//							Log.i("fall","2fall!ez"+ez+" a:"+a);
//						}
//						else{
//							if(dz>1.229073){
//								Log.i("fall","3fall!ez"+ez+" a:"+a);
//							}
//						}
//					}
//				}
//			}
//		}
//	}

}
