package org.drulabs.presentation.viewmodels;

import org.drulabs.domain.entities.DomainRecipe;
import org.drulabs.domain.usecases.DeleteAllRecipesTask;
import org.drulabs.domain.usecases.DeleteRecipeTask;
import org.drulabs.domain.usecases.GetLastSavedRecipeTask;
import org.drulabs.domain.usecases.GetSavedRecipesTask;
import org.drulabs.presentation.data.CompletableLiveData;
import org.drulabs.presentation.data.LastSavedLiveData;
import org.drulabs.presentation.data.Model;
import org.drulabs.presentation.data.SavedRecipesLiveData;
import org.drulabs.presentation.entities.PresentationRecipe;
import org.drulabs.presentation.mapper.PresentationMapper;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class FavoritesVM extends ViewModel {

    private SavedRecipesLiveData savedRecipes;


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

        this.savedRecipes = new SavedRecipesLiveData(mapper, getSavedRecipesTask);
    }

    public LiveData<Model<List<PresentationRecipe>>> getSavedRecipes() {
        return savedRecipes;
    }

    public LiveData<Model<PresentationRecipe>> getLastSavedRecipe() {
        return (new LastSavedLiveData(mapper, getLastSavedRecipeTask));
    }

    public LiveData<Boolean> deleteAllFavoriteRecipes() {
        return (new CompletableLiveData<>(deleteAllRecipesTask, null));
    }

    public LiveData<Boolean> deleteRecipeFromFavorite(PresentationRecipe presentationRecipe) {
        return (new CompletableLiveData<>(deleteRecipeTask, mapper.mapTo(presentationRecipe)));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        getSavedRecipesTask.dispose();
        deleteAllRecipesTask.dispose();
        deleteRecipeTask.dispose();
        getLastSavedRecipeTask.dispose();
    }
}
