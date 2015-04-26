package com.ksy.firstkiss;


import javax.inject.Singleton;

import com.ksy.firstkiss.core.utils.PostFromAnyThreadBus;
import com.ksy.firstkiss.ui.MainActivity;
import com.ksy.firstkiss.ui.SettingActivity;
import com.squareup.otto.Bus;

import dagger.Module;
import dagger.Provides;


/**
 * Dagger module for setting up provides statements.
 * Register all of your entry points below.
 */
@Module(
    complete = false,

    injects = {
        FirstKissApplication.class,
        MainActivity.class,
        SettingActivity.class
    },
    library = true
)
public class FirstKissModule {
	@Singleton
    @Provides
    Bus provideOttoBus() {
        return new PostFromAnyThreadBus();
    }
	
	
	

}
