package org.drulabs.presentation.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import org.drulabs.domain.entities.DomainRecipe;
import org.drulabs.domain.usecases.DeleteAllRecipesTask;
import org.drulabs.domain.usecases.DeleteRecipeTask;
import org.drulabs.domain.usecases.GetLastSavedRecipeTask;
import org.drulabs.domain.usecases.GetSavedRecipesTask;
import org.drulabs.presentation.custom.SingleLiveEvent;
import org.drulabs.presentation.data.Model;
import org.drulabs.presentation.entities.PresentationRecipe;
import org.drulabs.presentation.mapper.PresentationMapper;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.observers.DisposableSingleObserver;

public class FavoritesVM extends ViewModel {

    private MutableLiveData<Model<List<PresentationRecipe>>> savedRecipes = new MutableLiveData<>();
    private MutableLiveData<Model<PresentationRecipe>> lastSavedRecipe = new MutableLiveData<>();
    private SingleLiveEvent<Boolean> deleteAllStatus = new SingleLiveEvent<>();
    private SingleLiveEvent<Boolean> deleteRecipeStatus = new SingleLiveEvent<>();


    private GetSavedRecipesTask getSavedRecipesTask;
    private GetLastSavedRecipeTask getLastSavedRecipeTask;
    private DeleteAllRecipesTask deleteAllRecipesTask;
    private DeleteRecipeTask deleteRecipeTask;

    private PresentationMapper<DomainRecipe> mapper;

    public FavoritesVM(PresentationMapper<DomainRecipe> mapper,
                       GetSavedRecipesTask getSavedRecipesTask,
                       GetLastSavedRecipeTask getLastSavedRecipeTask,
                       DeleteAllRecipesTask deleteAllRecipesTask,
                       DeleteRecipeTask deleteRecipeTask) {
        this.mapper = mapper;
        this.getSavedRecipesTask = getSavedRecipesTask;
        this.getLastSavedRecipeTask = getLastSavedRecipeTask;
        this.deleteAllRecipesTask = deleteAllRecipesTask;
        this.deleteRecipeTask = deleteRecipeTask;
    }

    public LiveData<Model<List<PresentationRecipe>>> getSavedRecipesData() {
        return savedRecipes;
    }

    public void fetchSavedRecipes() {
        savedRecipes.postValue(Model.loading(true));
        getSavedRecipesTask.run(new SavedRecipeObserver(), null);
    }

    public LiveData<Model<PresentationRecipe>> getLastSavedRecipe() {
        return lastSavedRecipe;
    }

    public void fetchLastSavedRecipe() {
        lastSavedRecipe.postValue(Model.loading(true));

        getLastSavedRecipeTask.run(lastSavedRecipeObserver, null);
    }

    public LiveData<Boolean> getDeleteAllRecipeStatus() {
        return deleteAllStatus;
    }

    public void deleteAllFavoriteRecipes() {

        deleteAllRecipesTask.run(new BooleanObserver(deleteAllStatus), null);
    }

    public LiveData<Boolean> getDeleteRecipeStatus() {
        return deleteRecipeStatus;
    }

    public void deleteRecipeFromFavorite(PresentationRecipe presentationRecipe) {

        deleteRecipeTask.run(new BooleanObserver(deleteRecipeStatus), mapper.mapTo(presentationRecipe));
    }

    private class BooleanObserver extends DisposableCompletableObserver {

        private MutableLiveData<Boolean> status;

        BooleanObserver(MutableLiveData<Boolean> status) {
            this.status = status;
        }

        @Override
        public void onComplete() {
            status.setValue(true);
        }

        @Override
        public void onError(Throwable e) {
            status.setValue(false);
        }
    }

    private class SavedRecipeObserver extends DisposableObserver<DomainRecipe> {

        private List<PresentationRecipe> pr;

        SavedRecipeObserver() {
            pr = new ArrayList<>();
        }

        @Override
        public void onNext(DomainRecipe domainRecipe) {
            PresentationRecipe presentationRecipe = mapper.mapFrom(domainRecipe);
            presentationRecipe.setFavorite(true);
            pr.add(presentationRecipe);
        }

        @Override
        public void onError(Throwable e) {
            savedRecipes.postValue(Model.error(e));
        }

        @Override
        public void onComplete() {
            savedRecipes.postValue(Model.success(pr));
        }
    }

    private DisposableSingleObserver<DomainRecipe> lastSavedRecipeObserver = new
            DisposableSingleObserver<DomainRecipe>() {
                @Override
                public void onSuccess(DomainRecipe domainRecipe) {
                    PresentationRecipe presentationRecipe = mapper.mapFrom(domainRecipe);
                    lastSavedRecipe.postValue(Model.success(presentationRecipe));
                }

                @Override
                public void onError(Throwable e) {
                    lastSavedRecipe.postValue(Model.error(e));
                }
            };
}
