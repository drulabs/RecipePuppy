package org.drulabs.presentation.model;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import io.reactivex.Single;
import io.reactivex.disposables.Disposable;

public class SingleLiveData<T> extends LiveData<Model<T>> {

    private Disposable disposable;
    private Single<T> singleObservable;

    public SingleLiveData(@NonNull Single<T> single) {
        this.singleObservable = single;
    }

    @Override
    protected void onActive() {
        postValue(Model.loading(true));
        disposable = singleObservable.subscribe((t, throwable) -> {
            if (throwable != null) {
                postValue(Model.error(throwable));
            } else {
                postValue(Model.success(t));
            }
        });
    }

    @Override
    protected void onInactive() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

}
