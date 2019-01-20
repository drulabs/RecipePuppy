package org.drulabs.presentation.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import org.drulabs.domain.entities.DomainRecipe;
import org.drulabs.domain.usecases.DeleteAllRecipesTask;
import org.drulabs.domain.usecases.DeleteRecipeTask;
import org.drulabs.domain.usecases.GetLastSavedRecipeTask;
import org.drulabs.domain.usecases.GetSavedRecipesTask;
import org.drulabs.presentation.custom.SingleLiveEvent;
import org.drulabs.presentation.entities.PresentationRecipe;
import org.drulabs.presentation.mapper.PresentationMapper;

import java.util.List;

import javax.inject.Inject;

public class FavoritesVM extends BaseVM {

    private MutableLiveData<Model<List<PresentationRecipe>>> savedRecipes = new MutableLiveData<>();
    private MutableLiveData<Model<PresentationRecipe>> lastSavedRecipe = new MutableLiveData<>();
    private SingleLiveEvent<Boolean> deleteAllStatus = new SingleLiveEvent<>();
    private SingleLiveEvent<Boolean> deleteRecipeStatus = new SingleLiveEvent<>();


    private GetSavedRecipesTask getSavedRecipesTask;
    private GetLastSavedRecipeTask getLastSavedRecipeTask;
    private DeleteAllRecipesTask deleteAllRecipesTask;
    private DeleteRecipeTask deleteRecipeTask;

    private PresentationMapper<DomainRecipe> mapper;

    @Inject
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
        addDisposable(
                getSavedRecipesTask.run(null)
                        .map(domainRecipe -> {
                            PresentationRecipe presentationRecipe = mapper.mapFrom(domainRecipe);
                            presentationRecipe.setFavorite(true);
                            return presentationRecipe;
                        })
                        .toList()
                        .subscribe(e -> savedRecipes.postValue(Model.success(e)),
                                throwable -> savedRecipes.postValue(Model.error(throwable)))
        );
    }

    public LiveData<Model<PresentationRecipe>> getLastSavedRecipe() {
        return lastSavedRecipe;
    }

    public void fetchLastSavedRecipe() {
        lastSavedRecipe.postValue(Model.loading(true));
        addDisposable(
                getLastSavedRecipeTask.run(null)
                        .map(domainRecipe -> mapper.mapFrom(domainRecipe))
                        .subscribe(presentationRecipe -> lastSavedRecipe.postValue(Model.success
                                        (presentationRecipe)),
                                throwable -> lastSavedRecipe.postValue(Model.error(throwable)))
        );
    }

    public LiveData<Boolean> getDeleteAllRecipeStatus() {
        return deleteAllStatus;
    }

    public void deleteAllFavoriteRecipes() {
        addDisposable(
                deleteAllRecipesTask.run(null)
                        .subscribe(() -> deleteAllStatus.postValue(true),
                                throwable -> deleteAllStatus.postValue(false))
        );
    }

    public LiveData<Boolean> getDeleteRecipeStatus() {
        return deleteRecipeStatus;
    }

    public void deleteRecipeFromFavorite(PresentationRecipe presentationRecipe) {
        addDisposable(
                deleteRecipeTask.run(mapper.mapTo(presentationRecipe))
                        .subscribe(() -> deleteRecipeStatus.postValue(true),
                                throwable -> deleteRecipeStatus.postValue(false))
        );
    }
}
