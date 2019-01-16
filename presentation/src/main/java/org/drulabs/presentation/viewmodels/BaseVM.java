package org.drulabs.presentation.viewmodels;

import android.arch.lifecycle.ViewModel;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public abstract class BaseVM extends ViewModel {

    private CompositeDisposable disposables = new CompositeDisposable();

    protected void addDisposable(Disposable disposable) {
        disposables.add(disposable);
    }

    @Override
    protected void onCleared() {
        if (!disposables.isDisposed()) {
            disposables.dispose();
        }
        super.onCleared();
    }
}
