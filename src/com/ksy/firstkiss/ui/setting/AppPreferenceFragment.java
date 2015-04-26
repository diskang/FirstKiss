package com.ksy.firstkiss.ui.setting;

import com.ksy.firstkiss.R;

import android.os.Bundle;
import android.preference.PreferenceFragment;

public class AppPreferenceFragment extends PreferenceFragment{
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.fr_app_preference);
	}
	
	
}
