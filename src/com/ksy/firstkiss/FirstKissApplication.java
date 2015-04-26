package com.ksy.firstkiss;

import android.app.Application;
import android.app.Instrumentation;
import android.content.Context;
import butterknife.ButterKnife;

import com.ksy.firstkiss.BuildConfig;
import com.ksy.firstkiss.core.utils.CrashHandler;

public class FirstKissApplication extends Application{
	private static FirstKissApplication instance;
	/**
     * Create main application
     */
    public FirstKissApplication() {
    }
    
    /**
     * Create main application
     *
     * @param context
     */
    public FirstKissApplication(final Context context) {
        this();
        attachBaseContext(context);
    }
    
    /**
     * Create main application
     *
     * @param instrumentation
     */
    public FirstKissApplication(final Instrumentation instrumentation) {
        this();
        attachBaseContext(instrumentation.getTargetContext());
    }
    
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        Injector.init(getRootModule(), this);
        CrashHandler catchHandler = CrashHandler.getInstance();  
        catchHandler.init(this);
		
		
        ButterKnife.setDebug(BuildConfig.DEBUG);
    }
	
	private Object getRootModule() {
        return new RootModule();
    }
	
	
	public static FirstKissApplication getInstance() {
        return instance;
    }
	
}
