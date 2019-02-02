package org.drulabs.bakencook.di;

import android.app.Application;
import android.content.Context;

import org.drulabs.bakencook.ui.home.HomeActivity;
import org.drulabs.bakencook.ui.favorites.FavoritesActivity;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
abstract class AppModule {

    @Binds
    abstract Context bindContext(Application application);

    @ContributesAndroidInjector
    abstract HomeActivity contributesMainActivity();

    @ContributesAndroidInjector
    abstract FavoritesActivity contributesFavoritesActivity();
}
