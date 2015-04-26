package com.ksy.firstkiss;

import dagger.Module;

/**
 * Add all the other modules to this one.
 */
@Module(
        includes = {
                AndroidModule.class,
                FirstKissModule.class
        }
)
public class RootModule {
}
