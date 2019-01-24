package org.drulabs.presentation.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import org.drulabs.domain.entities.DomainRecipe;
import org.drulabs.domain.usecases.DeleteAllRecipesTask;
import org.drulabs.domain.usecases.DeleteRecipeTask;
import org.drulabs.domain.usecases.GetLastSavedRecipeTask;
import org.drulabs.domain.usecases.GetSavedRecipesTask;
import org.drulabs.presentation.data.CompletableLiveData;
import org.drulabs.presentation.data.Model;
import org.drulabs.presentation.data.LastSavedLiveData;
import org.drulabs.presentation.entities.PresentationRecipe;
import org.drulabs.presentation.mapper.PresentationMapper;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.observers.DisposableObserver;

public class FavoritesVM extends ViewModel {

    private MutableLiveData<Model<List<PresentationRecipe>>> savedRecipes = new MutableLiveData<>();


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

    public LiveData<Model<PresentationRecipe>> fetchLastSavedRecipe() {
        return (LastSavedLiveData<PresentationRecipe, Void>)new LastSavedLiveData(getLastSavedRecipeTask, null);
    }

    public LiveData<Boolean> deleteAllFavoriteRecipes() {
        return (new CompletableLiveData<>(deleteAllRecipesTask, null));
    }

    public LiveData<Boolean> deleteRecipeFromFavorite(PresentationRecipe presentationRecipe) {
        return (new CompletableLiveData<>(deleteRecipeTask, mapper.mapTo(presentationRecipe)));
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

//    private DisposableSingleObserver<DomainRecipe> lastSavedRecipeObserver = new
//            DisposableSingleObserver<DomainRecipe>() {
//                @Override
//                public void onSuccess(DomainRecipe domainRecipe) {
//                    PresentationRecipe presentationRecipe = mapper.mapFrom(domainRecipe);
//                    lastSavedRecipe.postValue(Model.success(presentationRecipe));
//                }
//
//                @Override
//                public void onError(Throwable e) {
//                    lastSavedRecipe.postValue(Model.error(e));
//                }
//            };
}
