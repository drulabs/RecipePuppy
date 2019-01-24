package org.drulabs.presentation.data;

import org.drulabs.domain.usecases.base.CompletableUseCase;
import org.drulabs.presentation.custom.SingleLiveEvent;

import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableCompletableObserver;

public class CompletableLiveData<T> extends SingleLiveEvent<Boolean> {

    private CompletableUseCase<T> completableUseCase;
    private T input;

    public CompletableLiveData(@NonNull CompletableUseCase<T> completableUseCase, T input) {
        this.completableUseCase = completableUseCase;
        this.input = input;
    }

    @Override
    protected void onActive() {
        completableUseCase.run(booleanObserver, input);
    }

    @Override
    protected void onInactive() {
        completableUseCase.dispose();
    }

    private DisposableCompletableObserver booleanObserver = new DisposableCompletableObserver() {
        @Override
        public void onComplete() {
            setValue(true);
        }

        @Override
        public void onError(Throwable e) {
            setValue(false);
        }
    };
}
