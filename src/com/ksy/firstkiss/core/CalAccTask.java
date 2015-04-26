package com.ksy.firstkiss.core;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.Log;
import android.widget.Toast;

public class CalAccTask implements SensorEventListener{
	private Context mcontext;
	private ActionDiscover fallModel=new ActionDiscover();
	private BehaviourSpeculate behaviourModel= new BehaviourSpeculate();
	
	private final int WINDOWSIZE=75;
	/*marking variables*/
	private int currWindow=0;
	private int oldWindow=0;
	private int pos=0;//denote current position in current window
	private int trend = 0;
	private double previous=0f;
	
	/*record variables*/
	private float []squareSum= {0f,0f};
	private float []Sum = {0f,0f};
	
	//private int tipping[][]=new int[2][75]; temporarily not used
	private int []tippingPos={0,0};//for two window , tipping value is different
	private float []vibrate_sum={0f,0f};
	private int [][]level ={{0,0,0,0,0,0},{0,0,0,0,0,0}};//level0 to 5
	private int []freq={0,0};//amount of positive number in tipping
	
	/*statistic variables*/
	private float mean;	
	private float variance;
	private int frequency;//=freq[0]+freq[1]
	private float vibrate_mean;
	private int LEVEL[] = new int[6];
	
	//private float balance; temporarily not used
	
	//private float vibrate_variance;temporarily not used
	public CalAccTask(Context context){
		mcontext=context;
	}
	private void LevelCount(double a){
		if(a<=2){
			level[currWindow][0]+=1;
		}
		else if(a<=6){
			level[currWindow][1]+=1;
		}
		else if(a<=11){
			level[currWindow][2]+=1;
		}
		else if(a<=17){
			level[currWindow][3]+=1;
		}
		else if(a<=25){
			level[currWindow][4]+=1;
		}
		else{
			level[currWindow][5]+=1;
		}
	}
	private void TrendCalculate(double a){
		if (trend>=0){
			if (a-previous<-3.25){
				//tipping[currWindow][tippingPos[currWindow]]= trend;
				freq[currWindow]++;//
				vibrate_sum[currWindow]+=trend;
				tippingPos[currWindow]+=1;
				trend=-1;
			}
			else{
				trend+=1;
			}
			if (a-previous>0 || a-previous<-3.25){
				previous=a;
			}
		}
		else{//trend <=0
			if (a-previous>3.25){
				//tipping[currWindow][tippingPos[currWindow]]=trend;
				vibrate_sum[currWindow]+=-trend;//add the absolute value
				tippingPos[currWindow]+=1;
				trend=1;
			}
			else{
				trend-=1;
			}
			if (a-previous<=0 || a-previous>3.25){
				previous=a;
			}
		}
	}
	private void CalVibrate(){
		frequency=freq[currWindow]+freq[oldWindow];
		int total =(tippingPos[currWindow]+tippingPos[oldWindow]);
		if(total>0){
			vibrate_mean=(vibrate_sum[currWindow]+vibrate_sum[oldWindow])/total;
		}
		else{//total=0
			vibrate_mean=0;
		}
		for(int i=0;i<6;i++){
			LEVEL[i]=level[currWindow][i]+level[oldWindow][i];
		}
	}

	private void changeCurrWindowType(){
		if(currWindow==0){
			currWindow=1;
		}
		else{
			currWindow=0;
		}
	}
	private void swapWindowType(){
		if(currWindow!=oldWindow){
			int temp=oldWindow;
			oldWindow=currWindow;
			currWindow=temp;
		}
		else{
			changeCurrWindowType();
		}
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		if (event.sensor.getType()==Sensor.TYPE_LINEAR_ACCELERATION){
			double square = event.values[0]*event.values[0]+event.values[1]*event.values[1]+event.values[2]*event.values[2];
			float acc=(float) Math.sqrt(square);
			if(pos>=WINDOWSIZE){
				if(oldWindow==currWindow){//at first time,there is no old window
					changeCurrWindowType();
				}
				else{
					mean=(Sum[currWindow]+Sum[oldWindow])/(2*WINDOWSIZE);
					variance=(squareSum[currWindow]+squareSum[oldWindow])/(2*WINDOWSIZE)-mean*mean;
					CalVibrate();
					fallModel.refresh(mean,variance,frequency,vibrate_mean,LEVEL);
					String result=fallModel.speculate();
					behaviourModel.addAction(result);
					if(behaviourModel.SYS_STATE==behaviourModel.NOTIFYING){
//						Log.i("behavior",behaviourModel.behaviour);
						Toast.makeText(mcontext, behaviourModel.behaviour, Toast.LENGTH_SHORT).show();
					}
					else{
						//do nothing ,wait
					}
//					Log.i(result,behaviourModel.actionListener+":"+behaviourModel.actionConfirmWeight);
//					Log.i(result,"mean:"+mean+"variance"+variance+"freq"+frequency+"vmean"+vibrate_mean);
					Log.i(result,LEVEL[0]+","+LEVEL[1]+","+LEVEL[2]+","+LEVEL[3]+","+LEVEL[4]+","+LEVEL[5]+","+mean+","+variance+","+frequency+","+vibrate_mean+","+result);
					clear();
					swapWindowType();
				}
			}
			else{
				squareSum[currWindow]+=square;
				Sum[currWindow]+=acc;
				LevelCount(acc);
				TrendCalculate(acc);
				pos++;
			}
		}
	}

	private void clear() {
		pos=0;
		trend=0;
		//'previous` don't need to be cleared
		
		//clear old before swap window type ,after swapping,current will be old
		for(int i=0;i<6;i++){
			level[oldWindow][i]=0;
		}
		Sum[oldWindow]=0f;
		squareSum[oldWindow]=0f;
		tippingPos[oldWindow]=0;
		freq[oldWindow]=0;
		vibrate_sum[oldWindow]=0f;
	}
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

}
