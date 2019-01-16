package org.drulabs.presentation.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;

import org.drulabs.domain.entities.DomainRecipe;
import org.drulabs.domain.entities.RecipeRequest;
import org.drulabs.domain.usecases.DeleteRecipeTask;
import org.drulabs.domain.usecases.GetRecipesTask;
import org.drulabs.domain.usecases.SaveRecipeTask;
import org.drulabs.presentation.entities.PresentationRecipe;
import org.drulabs.presentation.mapper.PresentationMapper;
import org.drulabs.presentation.model.CompletableLiveData;
import org.drulabs.presentation.model.Model;
import org.drulabs.presentation.model.RecipeLiveData;

import java.util.List;

import javax.inject.Inject;

public class HomeVM extends BaseVM {

    private LiveData<Model<List<PresentationRecipe>>> recipesLiveData;

    private PresentationMapper<DomainRecipe> mapper;

    private MutableLiveData<RecipeRequest> recipeRequest = new MutableLiveData<>();

    private SaveRecipeTask saveRecipeTask;
    private DeleteRecipeTask deleteRecipeTask;

    @Inject
    public HomeVM(PresentationMapper<DomainRecipe> mapper,
                  GetRecipesTask getRecipesTask,
                  SaveRecipeTask saveRecipeTask,
                  DeleteRecipeTask deleteRecipeTask) {
        this.mapper = mapper;
        this.saveRecipeTask = saveRecipeTask;
        this.deleteRecipeTask = deleteRecipeTask;

        this.recipesLiveData = Transformations.switchMap(
                recipeRequest,
                request -> new RecipeLiveData<>(
                        getRecipesTask.run(request).map(mapper::mapFrom)
                ));
    }

    public LiveData<Model<List<PresentationRecipe>>> getRecipesLiveData() {
        return recipesLiveData;
    }

    public void searchRecipes(String searchText, int pageNum) {
        RecipeRequest request = new RecipeRequest(searchText, pageNum);
        recipeRequest.postValue(request);
    }

    public LiveData<Boolean> saveRecipeAsFavorite(PresentationRecipe presentationRecipe) {
        return (new CompletableLiveData(saveRecipeTask.run(mapper.mapTo(presentationRecipe))));
    }

    public LiveData<Boolean> deleteRecipeFromFavorite(PresentationRecipe presentationRecipe) {
        return (new CompletableLiveData(deleteRecipeTask.run(mapper.mapTo(presentationRecipe))));
    }
}
