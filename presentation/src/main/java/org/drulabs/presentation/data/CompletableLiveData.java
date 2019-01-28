package org.drulabs.presentation.data;

import org.drulabs.domain.usecases.base.CompletableUseCase;
import org.drulabs.presentation.custom.SingleLiveEvent;

import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableCompletableObserver;

public class CompletableLiveData<T> extends SingleLiveEvent<Boolean> {

    private CompletableUseCase<T> completableUseCase;
    private T input;
    private BooleanObserver booleanObserver;

    public CompletableLiveData(@NonNull CompletableUseCase<T> completableUseCase, T input) {
        this.completableUseCase = completableUseCase;
        this.input = input;
        this.booleanObserver = new BooleanObserver();
    }

    @Override
    protected void onActive() {
        if (booleanObserver.isDisposed()) {
            booleanObserver = new BooleanObserver();
        }
        completableUseCase.run(booleanObserver, input);
    }

    @Override
    protected void onInactive() {
        booleanObserver.dispose();
    }

    private class BooleanObserver extends DisposableCompletableObserver {
        @Override
        public void onComplete() {
            setValue(true);
        }

        @Override
        public void onError(Throwable e) {
            setValue(false);
        }
    }
}
