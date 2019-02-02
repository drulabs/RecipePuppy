package org.drulabs.bakencook.di;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@Module
class DomainModule {

    @Provides
    @Singleton
    @Named("execution")
    Scheduler providesExecutionScheduler() {
        return Schedulers.io();
    }

    @Provides
    @Singleton
    @Named("postExecution")
    Scheduler providesPostExecutionScheduler() {
        return AndroidSchedulers.mainThread();
    }

}
