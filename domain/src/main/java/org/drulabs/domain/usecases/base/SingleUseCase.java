package org.drulabs.domain.usecases.base;

import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableSingleObserver;

public abstract class SingleUseCase<T, Input> {

    private Scheduler executionScheduler;
    private Scheduler postExecutionScheduler;

    private CompositeDisposable disposables = new CompositeDisposable();

    public SingleUseCase(Scheduler executionScheduler, Scheduler postExecutionScheduler) {
        this.executionScheduler = executionScheduler;
        this.postExecutionScheduler = postExecutionScheduler;
    }

    abstract protected Single<T> build(Input input);

    public void run(DisposableSingleObserver<T> disposableObserver, Input input) {
        addDisposable(
                build(input)
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
