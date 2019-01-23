package org.drulabs.recipepuppy.di;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
@Singleton
public class AppModule {

    private Application application;

    public AppModule(Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    public Application getApplication() {
        return application;
    }

    @Provides
    @Singleton
    public Context getApplicationContext() {
        return application.getApplicationContext();
    }
}
