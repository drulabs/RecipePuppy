package org.drulabs.presentation.model;

import android.arch.lifecycle.LiveData;
import android.util.Log;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

public class RecipeLiveData<T> extends LiveData<Model<List<T>>> {

    private static final String TAG = "RecipeLiveData";

    private Observable<T> observable;
    private Disposable disposable;

    public RecipeLiveData(Observable<T> observable) {
        this.observable = observable;
    }

    @Override
    protected void onActive() {
        postValue(Model.loading(true));
        disposable = observable//.map(domainRecipe -> mapper.mapFrom(domainRecipe))
                .doOnNext(r -> Log.d(TAG, "onActive: " + r.getClass().getSimpleName()))
                .doOnComplete(() -> Log.d(TAG, "onActive: COMPLETE"))
                .toList()
                .subscribe((ts, throwable) -> {
                    if (throwable != null) {
                        postValue(Model.error(throwable));
                    } else {
                        postValue(Model.success(ts));
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
