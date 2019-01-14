package org.drulabs.presentation.model;

import android.arch.lifecycle.LiveData;
import android.util.Log;

import org.drulabs.domain.entities.DomainRecipe;
import org.drulabs.presentation.entities.PresentationRecipe;
import org.drulabs.presentation.mapper.PresentationMapper;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

public class RecipeLiveData extends LiveData<Model<List<PresentationRecipe>>> {

    private static final String TAG = "RecipeLiveData";

    private PresentationMapper<DomainRecipe> mapper;
    private Observable<DomainRecipe> observable;
    private Disposable disposable;

    public RecipeLiveData(PresentationMapper<DomainRecipe> mapper, Observable<DomainRecipe>
            observable) {
        this.mapper = mapper;
        this.observable = observable;
    }

    @Override
    protected void onActive() {
        postValue(Model.loading(true));
        disposable = observable.map(domainRecipe -> mapper.mapFrom(domainRecipe))
                .doOnNext(r -> Log.d(TAG, "onActive: " + r.getName()))
                .doOnComplete(() -> Log.d(TAG, "onActive: COMPLETE"))
                .toList()
                .subscribe((presentationRecipes, throwable) -> {
                    if (throwable != null) {
                        postValue(Model.error(throwable));
                    } else {
                        postValue(Model.success(presentationRecipes));
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
