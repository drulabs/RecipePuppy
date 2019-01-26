package org.drulabs.presentation.factory;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.NonNull;

import org.drulabs.domain.entities.DomainRecipe;
import org.drulabs.domain.usecases.DeleteRecipeTask;
import org.drulabs.domain.usecases.GetRecipesTask;
import org.drulabs.domain.usecases.SaveRecipeTask;
import org.drulabs.presentation.mapper.PresentationMapper;
import org.drulabs.presentation.viewmodels.HomeVM;

import javax.inject.Inject;

public class HomeVMFactory implements ViewModelProvider.Factory {

    private PresentationMapper<DomainRecipe> mapper;
    private SaveRecipeTask saveRecipeTask;
    private DeleteRecipeTask deleteRecipeTask;
    private GetRecipesTask getRecipesTask;

    @Inject
    public HomeVMFactory(PresentationMapper<DomainRecipe> mapper, SaveRecipeTask saveRecipeTask,
                         DeleteRecipeTask deleteRecipeTask, GetRecipesTask getRecipesTask) {
        this.mapper = mapper;
        this.saveRecipeTask = saveRecipeTask;
        this.deleteRecipeTask = deleteRecipeTask;
        this.getRecipesTask = getRecipesTask;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new HomeVM(mapper, getRecipesTask, saveRecipeTask, deleteRecipeTask);
    }
}
