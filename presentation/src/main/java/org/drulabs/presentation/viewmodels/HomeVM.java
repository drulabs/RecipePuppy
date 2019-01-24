package org.drulabs.presentation.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import org.drulabs.domain.entities.DomainRecipe;
import org.drulabs.domain.entities.RecipeRequest;
import org.drulabs.domain.usecases.DeleteRecipeTask;
import org.drulabs.domain.usecases.GetRecipesTask;
import org.drulabs.domain.usecases.SaveRecipeTask;
import org.drulabs.presentation.data.CompletableLiveData;
import org.drulabs.presentation.data.Model;
import org.drulabs.presentation.data.RecipesLiveData;
import org.drulabs.presentation.entities.PresentationRecipe;
import org.drulabs.presentation.mapper.PresentationMapper;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.observers.DisposableSingleObserver;

public class HomeVM extends ViewModel {

    private RecipesLiveData recipesLiveData;

    private PresentationMapper<DomainRecipe> mapper;

    private SaveRecipeTask saveRecipeTask;
    private DeleteRecipeTask deleteRecipeTask;

    public HomeVM(PresentationMapper<DomainRecipe> mapper,
                  GetRecipesTask getRecipesTask,
                  SaveRecipeTask saveRecipeTask,
                  DeleteRecipeTask deleteRecipeTask) {
        this.mapper = mapper;
        this.saveRecipeTask = saveRecipeTask;
        this.deleteRecipeTask = deleteRecipeTask;

        this.recipesLiveData = new RecipesLiveData(mapper, getRecipesTask);
    }

    public LiveData<Model<List<PresentationRecipe>>> searchRecipes(
            String searchText, int pageNum) {
        RecipeRequest request = new RecipeRequest(searchText, pageNum);
        recipesLiveData.setRecipeRequest(request);
        return recipesLiveData;
    }

    public LiveData<Boolean> saveRecipeAsFav(PresentationRecipe recipe) {
        return (new CompletableLiveData<>(saveRecipeTask, mapper.mapTo(recipe)));
    }

    public LiveData<Boolean> deleteRecipeFromFav(PresentationRecipe recipe) {
        return (new CompletableLiveData<>(deleteRecipeTask, mapper.mapTo(recipe)));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        saveRecipeTask.dispose();
        deleteRecipeTask.dispose();
    }
}
