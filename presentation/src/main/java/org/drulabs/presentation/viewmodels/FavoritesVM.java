package org.drulabs.presentation.viewmodels;

import android.arch.lifecycle.LiveData;

import org.drulabs.domain.entities.DomainRecipe;
import org.drulabs.domain.usecases.DeleteAllRecipesTask;
import org.drulabs.domain.usecases.GetLastSavedRecipeTask;
import org.drulabs.domain.usecases.GetSavedRecipesTask;
import org.drulabs.presentation.entities.PresentationRecipe;
import org.drulabs.presentation.mapper.PresentationMapper;
import org.drulabs.presentation.model.CompletableLiveData;
import org.drulabs.presentation.model.Model;
import org.drulabs.presentation.model.RecipeLiveData;
import org.drulabs.presentation.model.SingleLiveData;

import java.util.List;

import javax.inject.Inject;

public class FavoritesVM extends BaseVM {

    private PresentationMapper<DomainRecipe> mapper;
    private LiveData<Model<List<PresentationRecipe>>> savedRecipes;
    private LiveData<Model<PresentationRecipe>> lastSavedRecipe;
    private DeleteAllRecipesTask deleteAllRecipesTask;


    @Inject
    public FavoritesVM(PresentationMapper<DomainRecipe> mapper,
                       GetSavedRecipesTask getSavedRecipesTask,
                       GetLastSavedRecipeTask getLastSavedRecipeTask,
                       DeleteAllRecipesTask deleteAllRecipesTask) {
        this.mapper = mapper;
        this.savedRecipes = new RecipeLiveData<>(getSavedRecipesTask.run(null)
                .map(mapper::mapFrom));
        this.lastSavedRecipe = new SingleLiveData<>(getLastSavedRecipeTask.run(null).map
                (mapper::mapFrom));
        this.deleteAllRecipesTask = deleteAllRecipesTask;
    }

    public LiveData<Model<List<PresentationRecipe>>> getSavedRecipes() {
        return savedRecipes;
    }

    public LiveData<Model<PresentationRecipe>> getLastSavedRecipe() {
        return lastSavedRecipe;
    }

    public LiveData<Boolean> deleteAllFavoriteRecipes(PresentationRecipe presentationRecipe) {
        return (new CompletableLiveData(deleteAllRecipesTask.run(null)));
    }
}
