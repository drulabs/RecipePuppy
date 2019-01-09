package org.drulabs.domain.usecases.base;

import io.reactivex.Scheduler;
import io.reactivex.Single;

public abstract class SingleUseCase<T, Input> {

    private Scheduler executionScheduler;
    private Scheduler postExecutionScheduler;

    public SingleUseCase(Scheduler executionScheduler, Scheduler postExecutionScheduler) {
        this.executionScheduler = executionScheduler;
        this.postExecutionScheduler = postExecutionScheduler;
    }

    abstract protected Single<T> build(Input input);

    public Single<T> run(Input input) {
        return build(input)
                .subscribeOn(executionScheduler)
                .observeOn(postExecutionScheduler);
    }
}
