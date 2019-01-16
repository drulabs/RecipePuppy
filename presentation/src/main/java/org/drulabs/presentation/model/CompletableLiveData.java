package org.drulabs.presentation.model;

import io.reactivex.Completable;
import io.reactivex.disposables.Disposable;

public class CompletableLiveData extends SingleLiveEvent<Boolean> {

    private Disposable disposable;
    private Completable completable;

    public CompletableLiveData(Completable completable) {
        this.completable = completable;
    }

    @Override
    protected void onActive() {
        disposable = completable.subscribe(() -> postValue(true),
                throwable -> postValue(false));
    }

    @Override
    protected void onInactive() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }
}
