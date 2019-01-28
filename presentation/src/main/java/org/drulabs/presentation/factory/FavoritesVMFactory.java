package org.drulabs.presentation.factory;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.NonNull;

import org.drulabs.domain.entities.DomainRecipe;
import org.drulabs.domain.usecases.DeleteAllRecipesTask;
import org.drulabs.domain.usecases.DeleteRecipeTask;
import org.drulabs.domain.usecases.GetLastSavedRecipeTask;
import org.drulabs.domain.usecases.GetSavedRecipesTask;
import org.drulabs.presentation.mapper.PresentationMapper;
import org.drulabs.presentation.viewmodels.FavoritesVM;

import javax.inject.Inject;

public class FavoritesVMFactory implements ViewModelProvider.Factory {

    private PresentationMapper<DomainRecipe> mapper;
    private GetSavedRecipesTask getSavedRecipesTask;
    private GetLastSavedRecipeTask getLastSavedRecipeTask;
    private DeleteAllRecipesTask deleteAllRecipesTask;
    private DeleteRecipeTask deleteRecipeTask;

    @Inject
    public FavoritesVMFactory(PresentationMapper<DomainRecipe> mapper,
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

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new FavoritesVM(mapper, getSavedRecipesTask, getLastSavedRecipeTask,
                deleteAllRecipesTask, deleteRecipeTask);
    }
}
