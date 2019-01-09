package org.drulabs.domain.usecases.base;

import io.reactivex.Completable;
import io.reactivex.Scheduler;

public abstract class CompletableUseCase<Input> {

    private Scheduler executionScheduler;
    private Scheduler postExecutionScheduler;

    public CompletableUseCase(Scheduler executionScheduler, Scheduler postExecutionScheduler) {
        this.executionScheduler = executionScheduler;
        this.postExecutionScheduler = postExecutionScheduler;
    }

    abstract protected Completable build(Input input);

    public Completable run(Input input) {
        return build(input)
                .subscribeOn(executionScheduler)
                .observeOn(postExecutionScheduler);
    }
}
