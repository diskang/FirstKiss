package com.ksy.senfal.model;

public class ActionDiscover {
	private float mean;
	private float variance;
	private int frequency;
	private float vibrate_mean;
	private int[] level=new int[6];
	public void refresh(float m,float v, int freq,float v_mean,int[] l){
		mean=m;
		variance=v;
		frequency=freq;
		vibrate_mean=v_mean;
		for(int i=0;i<6;i++){
			level[i]=l[i];
		}
	}
	
	public String speculate(){
		if(frequency==0){
			//quiet
			if(mean<1 && variance<1){
				return "quiet";
			}
			else{
				return "play";
			}
		}
		else if(frequency<=3){
			//playwith call stand
			if(mean<2){
				return "play";
			}
			else if(mean<4){
				if(variance>=4 &&variance<=11){
					return "stand";
				}
				else if(variance>=36){
					return "call";
				}
				else{
					return "play";
				}
			}
			else if(mean<=7){
				return "call";
			}
			else{
				return "shake";
			}
		}

		else if(frequency<=5){//4-5
			// play with or walk or jump
			if(mean<2){
				return "play";
			}
			else if(mean<4){
				if(variance>=4 &&variance<=11){
					return "stand";
				}
				else{
					return "play";
				}
			}
			else if(mean<=8){
				//walk or jump
				if(variance<40){
					return "walk";}
				else{
					return "jump";
				}
			}
			else if (mean<=10){
				return "jump";
			}
			else{
				// shake
				return "shake";
			}
		}
		else if(frequency<=11){//6-11
			 //walk,jump,fall
			if(mean<3){
				return "unknown";
			}
			else if(mean<10){
				if(variance>70){
					return "jump";
				}
				else{
					if(level[5]!=0){
						if(level[1]<=90){
							return "fall";}
						else{
							return "unknown";
						}
					}
					else{
						return "walk";
					}
				}
			}
			else{
				return "unknown";
			}
		}
		else if(frequency<=15){
			// fall run walk
			if(variance>=100 && vibrate_mean>=4 && vibrate_mean<=6){
				return "run";
			}
			else if(variance>=20){
				if(level[5]!=0){
					return "fall";
				}
				else{
					return "walk";
				}
			}
			else{
				return "unknown";
			}
		}
		else{
			return "run";
		}
	}
}
