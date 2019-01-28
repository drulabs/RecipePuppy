package org.drulabs.domain.usecases.base;

import io.reactivex.Completable;
import io.reactivex.Scheduler;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableCompletableObserver;

public abstract class CompletableUseCase<Input> {

    private Scheduler executionScheduler;
    private Scheduler postExecutionScheduler;

    private CompositeDisposable disposables = new CompositeDisposable();

    public CompletableUseCase(Scheduler executionScheduler, Scheduler postExecutionScheduler) {
        this.executionScheduler = executionScheduler;
        this.postExecutionScheduler = postExecutionScheduler;
    }

    abstract protected Completable build(Input input);

    public void run(DisposableCompletableObserver disposableObserver, Input input) {
        addDisposable(build(input)
                .subscribeOn(executionScheduler)
                .observeOn(postExecutionScheduler)
                .subscribeWith(disposableObserver)
        );
    }

    private void addDisposable(Disposable disposable) {
        disposables.add(disposable);
    }

    public void dispose() {
        if (!disposables.isDisposed()) {
            disposables.dispose();
        }
    }
}
