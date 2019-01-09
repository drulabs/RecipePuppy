package org.drulabs.domain.usecases.base;

import io.reactivex.Observable;
import io.reactivex.Scheduler;

public abstract class ObservableUseCase<T, Input> {

    private Scheduler executionScheduler;
    private Scheduler postExecutionScheduler;

    public ObservableUseCase(Scheduler executionScheduler, Scheduler postExecutionScheduler) {
        this.executionScheduler = executionScheduler;
        this.postExecutionScheduler = postExecutionScheduler;
    }

    abstract protected Observable<T> build(Input input);

    public Observable<T> run(Input input) {
        return build(input)
                .subscribeOn(executionScheduler)
                .observeOn(postExecutionScheduler);
    }
}
