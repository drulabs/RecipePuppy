package org.drulabs.recipepuppy.di;

import android.app.Application;
import android.content.Context;

import org.drulabs.recipepuppy.MainActivity;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
abstract class AppModule {

    @Binds
    abstract Context bindContext(Application application);

    @ContributesAndroidInjector
    abstract MainActivity contributesMainActivity();

//    private Application application;
//
//    public AppModule(Application application) {
//        this.application = application;
//    }
//
//    @Provides
//    @Singleton
//    public Application getApplication() {
//        return application;
//    }

//    @Provides
//    @Singleton
//    public Context getApplicationContext() {
//        return application.getApplicationContext();
//    }
}
