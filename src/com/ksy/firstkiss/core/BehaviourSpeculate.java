package com.ksy.firstkiss.core;

public class BehaviourSpeculate {
	public static int WAITING=0;
	public static int SUSPENDING=1;
	public static int NOTIFYING=2;
	public int SYS_STATE=WAITING;
	//Behavior : quiet walk run fall jump shake stand
	public String behaviour="quiet";
	//actionListener :quiet walk run fall jump shake stand
	public String actionListener="quiet";
	public int actionConfirmWeight=0;
//	private int actionDiscardWeight=0;
	private int actionNumber=0;
	public void SetActionListener(String state,int initWeight){
		actionListener=state;
		actionNumber=0;
		actionConfirmWeight=initWeight;
	}
	public void SetActionListener(String state){
		//clear all variables
		actionListener=state;
		actionNumber=0;
		actionConfirmWeight=0;
	}
	public void addAction(String state){
		String stateBehavior=state;
		if(state=="call"||state=="play"||state=="unknown"){
			stateBehavior="quiet";//initial 	
		}
		if(SYS_STATE==NOTIFYING){
			SYS_STATE=WAITING;
			SetActionListener(stateBehavior);
		}
		
		actionNumber+=1;// add number by 1
		switch(state){
		case "fall":
			_addFall();break;
		case  "run":
			_addRun();break;
		case "walk":
			_addWalk();break;
		case "jump": 
			_addJump();break;
		case "quiet":
			_addQuiet();break;
		case "stand":
			_addStand();break;
		case "shake":
			_addShake();break;
		case "call":case "play":
			_addPlay();break;
		default: 
			_addUnknown();
		break;
		}
		if(actionNumber>=6){
			if(checkBehaviourSatisfy()){
				SYS_STATE=NOTIFYING;
				behaviour=actionListener;
			}
			else{
				SetActionListener(stateBehavior);
			}
		}
		//actionNumber<6, waiting for next state to be added in
	}
	private boolean checkBehaviourSatisfy(){
		switch(actionListener){
		case "quiet":
			if(actionConfirmWeight>=20){
				return true;
			}
			break;
		case "walk":
			if(actionConfirmWeight>=20){
				return true;
			}
			break;
		case "run":
			if(actionConfirmWeight>=15){
				return true;
			}
			break;
		case "fall":
			if(actionConfirmWeight>=8){
				return true;
			}
			break;
		case "stand":
			if(actionConfirmWeight>=10){
				return true;
			}
			break;
		case "jump":
			if(actionConfirmWeight>=10){
				return true;
			}
			break;
		case "shake":
			if(actionConfirmWeight>=10){
				return true;
			}
			break;
		
		}
		return false;
	}
	private void _addUnknown() {
		actionConfirmWeight+=1;
	}
	private void _addShake() {
		switch(actionListener){
		case "fall":
			if(actionNumber<=3){// if detect wrong,take shake for fall
				actionConfirmWeight+=1;}
			else{// impossible
				SetActionListener("shake");
			}
			break;
		case "shake":
			actionConfirmWeight+=3*actionNumber;//shake is hard to fake, so times 3
			break;
		case "run": case "walk"://less possible
			actionConfirmWeight-=actionNumber;
			break;
		case "quiet":
			SetActionListener("shake",5);
			break;
		case "jump": case "stand":
			actionConfirmWeight-=actionNumber;
			if(actionConfirmWeight<0){
				SetActionListener("shake");
			}
		}
	}
	private void _addStand() {
		switch(actionListener){
		case "fall":
			if(actionNumber<=3){// if detect wrong,take stand for fall
				actionConfirmWeight+=1;}
			else{// less possible
//				SetActionListener("stand");
				actionConfirmWeight-=actionNumber;
			}
			break;
		case "stand":
			if(actionNumber<=2){
				actionConfirmWeight+=3*actionNumber;//shake is hard to fake, so times 3
			}
			else{
				actionConfirmWeight-=2;
			}
			break;
		case "run": case "walk": case "jump":
			actionConfirmWeight-=1;
			break;
		case "quiet":
			SetActionListener("stand",5);
		case "shake":
			actionConfirmWeight-=2;
			if(actionConfirmWeight<0){
				SetActionListener("shake");
			}
		}
	}
	private void _addWalk() {
		switch(actionListener){
		case "fall":
			if(actionNumber<=4){//discard walk
				actionConfirmWeight-=1;}
			else{
				actionConfirmWeight-=actionNumber;
				if(actionConfirmWeight<0){
					SetActionListener("walk");
				}
			}
			break;
		case "walk": 
			actionConfirmWeight+=2*actionNumber;
			break;
		case "run":
			actionConfirmWeight+=actionNumber;
			break;
		case "stand"://stand is not very accurate
			if(actionNumber<=3){
				actionConfirmWeight+=2*actionNumber;
			}
			else{
				SetActionListener("walk");
			}
		case "jump": case "shake":
			actionConfirmWeight+=1;//not very accurate cannot exclude,so plus only one
			break;
		
		case "quiet":// quite possible
			SetActionListener("walk",5);
			break;
		}
	}
	private void _addRun() {
		switch(actionListener){
		case "fall":
			if(actionNumber<=3){//discard run
				actionConfirmWeight-=1;}
			else{
				actionConfirmWeight-=actionNumber;
				if(actionConfirmWeight<0){
					SetActionListener("run");
				}
			}
			break;
		case "run": 
			actionConfirmWeight+=2*actionNumber;
			break;
		case "walk": case "stand":
			actionConfirmWeight+=actionNumber;
			break;
		 case "jump": case "shake":
			actionConfirmWeight+=1;//not very accurate cannot exclude,so plus only one
			break;
		
		case "quiet"://
			SetActionListener("run",5);
			break;
		}
	}
	private void _addFall(){
		//quiet walk run fall jump shake stand
		//fall���и����ȼ���once fall state announced, listener is set to be fall 
		switch(actionListener){
		case "fall":
			if(actionNumber<=3){
				actionConfirmWeight+=2*actionNumber;}
			else{
				actionConfirmWeight++;//
			}
			break;
		case "run": case "walk": case "jump":
			SetActionListener("fall",3);
			break;
		case "quiet": case "stand": case "shake":
			// less possible to fall
			SetActionListener("fall");
			break;
		}
	}
	private void _addJump(){
		switch(actionListener){
		case "fall":
			if(actionNumber<=3){// if detect wrong,take jump for fall
				actionConfirmWeight+=2;}
			else{// impossible
				SetActionListener("jump");
			}
			break;
		case "jump":
			actionConfirmWeight+=3*actionNumber;//jump is hard to fake, so times 3
			break;
		case "run": case "walk":
//			ideally! but jump is not accurate
//			SetActionListener("jump",5);
//			reality
			actionConfirmWeight+=1;
			break;
		case "shake":
			if(actionNumber<=3){
				actionConfirmWeight+=actionNumber;}
			else{// impossible
				SetActionListener("jump");
			}
			break;
		case "quiet": case "stand": 
			// jump has high priority to preempt
			SetActionListener("jump");
			break;
		}
	}
	private void _addPlay(){//Listener : quiet  is responsible for play
		// no place to set quiet listener is that ok?
		switch(actionListener){
		case "quiet":
			actionConfirmWeight+=2*actionNumber;
			break;
		case "jump":
			actionConfirmWeight+=2;
			break;
		case "stand": case "shake":case "fall":
			actionConfirmWeight+=actionNumber;
			break;
			
		case "walk": // less possible
			actionConfirmWeight-=1;
			if(actionConfirmWeight<0){
				SetActionListener("quiet");
			}
			break;
		case "run"://far less possible
			actionConfirmWeight-=actionNumber;
			if(actionConfirmWeight<0){
				SetActionListener("quiet");
			}
			break;	
			
		}
	}
	private void _addQuiet() {
		switch(actionListener){
		case "quiet":
			actionConfirmWeight+=2*actionNumber;
			break;
		case "jump":
			actionConfirmWeight+=2;
			break;
		case "stand": case "shake":case "fall":
			actionConfirmWeight+=actionNumber;
			break;
			
		case "walk": // less possible
			actionConfirmWeight-=actionNumber;
			if(actionConfirmWeight<0){
				SetActionListener("quiet");
			}
			break;
		case "run"://far less possible
			actionConfirmWeight-=2*actionNumber;
			if(actionConfirmWeight<0){
				SetActionListener("quiet");
			}
			break;	
			
		}
		
	}

}
