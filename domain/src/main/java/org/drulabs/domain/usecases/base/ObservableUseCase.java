package org.drulabs.domain.usecases.base;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

public abstract class ObservableUseCase<T, Input> {

    private Scheduler executionScheduler;
    private Scheduler postExecutionScheduler;

    private CompositeDisposable disposables = new CompositeDisposable();

    public ObservableUseCase(Scheduler executionScheduler, Scheduler postExecutionScheduler) {
        this.executionScheduler = executionScheduler;
        this.postExecutionScheduler = postExecutionScheduler;
    }

    abstract protected Observable<T> build(Input input);

    public void run(DisposableObserver<T> disposableObserver, Input input) {
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
