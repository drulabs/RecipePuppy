package org.drulabs.presentation.data;

import android.os.Handler;

import org.drulabs.domain.entities.DomainRecipe;
import org.drulabs.domain.usecases.GetSavedRecipesTask;
import org.drulabs.presentation.entities.PresentationRecipe;
import org.drulabs.presentation.mapper.PresentationMapper;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.LiveData;
import io.reactivex.observers.DisposableObserver;

public class SavedRecipesLiveData extends LiveData<Model<List<PresentationRecipe>>> {
    private static final long DELAY_FOR_DISPOSE = 2000;

    private PresentationMapper<DomainRecipe> mapper;
    private GetSavedRecipesTask getSavedRecipesTask;

    private boolean isDisposePending = false;
    private Handler disposerHandler = new Handler();
    private SavedRecipesObserver disposableObserver;
    private Runnable disposer = new Runnable() {
        @Override
        public void run() {
            disposableObserver.dispose();
            isDisposePending = false;
        }
    };

    public SavedRecipesLiveData(PresentationMapper<DomainRecipe> mapper,
                                GetSavedRecipesTask getSavedRecipesTask) {
        this.mapper = mapper;
        this.getSavedRecipesTask = getSavedRecipesTask;
        this.disposableObserver = new SavedRecipesObserver();
    }

    @Override
    protected void onActive() {
        if (isDisposePending) {
            disposerHandler.removeCallbacks(disposer);
        } else {
            postValue(Model.loading(true));
            if (disposableObserver.isDisposed()) {
                disposableObserver = new SavedRecipesObserver();
            }
            getSavedRecipesTask.run(disposableObserver, null);
        }
        isDisposePending = false;
    }

    @Override
    protected void onInactive() {
        disposerHandler.postDelayed(disposer, DELAY_FOR_DISPOSE);
        isDisposePending = true;
    }


    private class SavedRecipesObserver extends DisposableObserver<DomainRecipe> {

        private List<PresentationRecipe> presentationRecipes;

        SavedRecipesObserver() {
            presentationRecipes = new ArrayList<>();
        }

        @Override
        public void onNext(DomainRecipe domainRecipe) {
            PresentationRecipe presentationRecipe = mapper.mapFrom(domainRecipe);
            presentationRecipe.setFavorite(true);
            presentationRecipes.add(presentationRecipe);
        }

        @Override
        public void onError(Throwable e) {
            postValue(Model.error(e));
        }

        @Override
        public void onComplete() {
            postValue(Model.success(presentationRecipes));
        }
    }
}
