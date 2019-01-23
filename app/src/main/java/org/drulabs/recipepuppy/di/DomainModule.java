package org.drulabs.recipepuppy.di;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@Module
@Singleton
class DomainModule {

    @Provides
    @Named("execution")
    Scheduler providesExecutionScheduler() {
        return Schedulers.io();
    }

    @Provides
    @Named("postExecution")
    Scheduler providesPostExecutionScheduler() {
        return AndroidSchedulers.mainThread();
    }

}
