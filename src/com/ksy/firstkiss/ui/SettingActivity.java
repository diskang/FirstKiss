package com.ksy.firstkiss.ui;

import java.util.List;

import javax.inject.Inject;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.LayoutInflater;
import butterknife.ButterKnife;

import com.ksy.firstkiss.Injector;
import com.ksy.firstkiss.R;
import com.ksy.firstkiss.ui.setting.AppPreferenceFragment;
import com.squareup.otto.Bus;

public class SettingActivity extends PreferenceActivity {
	//TODO need  clear
	@Inject protected LayoutInflater layoutInflater;
	@Inject protected Bus eventBus;
	
	@Override 
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState); 
        Injector.inject(this);
        
//        if(hasHeaders()){
//        	@SuppressLint("InflateParams")
//        	LinearLayout view = (LinearLayout)layoutInflater.inflate(R.layout.preference_setup_bottom, null);
//            setListFooter(view);                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     
//        } 
        ButterKnife.inject(this);
	}
	@Override
    public void onBuildHeaders(List<Header> target){
        loadHeadersFromResource(R.xml.act_setting_preference, target);
    }

    @Override
    protected boolean isValidFragment(String fragmentName) {
        return AppPreferenceFragment.class.getName().equals(fragmentName);
    }
    
	
    @Override
    protected void onResume() {
        super.onResume();
        eventBus.register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        eventBus.unregister(this);
    }
    
    
    
}
