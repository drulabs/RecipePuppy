package org.drulabs.bakencook.di;

import android.app.Application;

import org.drulabs.bakencook.app.App;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;
import dagger.android.AndroidInjector;

@Singleton
@Component(modules = {
        AndroidInjectionModule.class,
        DomainModule.class,
        DataModule.class,
        PersistenceModule.class,
        NetworkModule.class,
        PresentationModule.class,
        AppModule.class
})
public interface AppComponent extends AndroidInjector<App> {

    @Component.Builder
    interface Builder {

        @BindsInstance
        AppComponent.Builder application(Application app);


        AppComponent build();
    }

    void inject(App app);

}
