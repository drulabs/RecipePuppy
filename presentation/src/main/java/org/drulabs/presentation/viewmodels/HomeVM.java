package org.drulabs.presentation.viewmodels;

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

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

public class HomeVM extends ViewModel {

    private static final int DEFAULT_PAGE_NUM = 1;
    private static final String DEFAULT_SEARCH_TERM = "parmesan";

    private RecipeRequest currentRequest = new RecipeRequest(DEFAULT_SEARCH_TERM, DEFAULT_PAGE_NUM);

    private MutableLiveData<RecipeRequest> requestLiveData = new MutableLiveData<>();
    private LiveData<Model<List<PresentationRecipe>>> recipesLiveData;

    private PresentationMapper<DomainRecipe> mapper;

    private GetRecipesTask getRecipesTask;
    private SaveRecipeTask saveRecipeTask;
    private DeleteRecipeTask deleteRecipeTask;

    public HomeVM(PresentationMapper<DomainRecipe> mapper,
                  GetRecipesTask getRecipesTask,
                  SaveRecipeTask saveRecipeTask,
                  DeleteRecipeTask deleteRecipeTask) {
        this.mapper = mapper;
        this.getRecipesTask = getRecipesTask;
        this.saveRecipeTask = saveRecipeTask;
        this.deleteRecipeTask = deleteRecipeTask;

        this.recipesLiveData = Transformations.switchMap(requestLiveData, input -> {
            RecipesLiveData liveData = new RecipesLiveData(mapper, getRecipesTask);
            liveData.setRecipeRequest(input);
            return liveData;
        });
    }

    public void searchRecipes(String searchText) {
        currentRequest = new RecipeRequest(searchText, DEFAULT_PAGE_NUM);
        requestLiveData.setValue(currentRequest);
    }

    private void searchRecipes(String searchText, int pageNum) {
        currentRequest = new RecipeRequest(searchText, pageNum);
        requestLiveData.setValue(currentRequest);
    }

    public void navigateToNextPage() {
        searchRecipes(currentRequest.getSearchQuery(), currentRequest.getPageNum() + 1);
    }

    public void reload() {
        requestLiveData.setValue(currentRequest);
    }

    public void navigateToPreviousPage() {
        if (currentRequest.getPageNum() > 1) {
            searchRecipes(currentRequest.getSearchQuery(), currentRequest.getPageNum() - 1);
        }
    }

    public LiveData<RecipeRequest> getRecipeRequestLiveData() {
        return requestLiveData;
    }

    public LiveData<Model<List<PresentationRecipe>>> getRecipesLiveData() {
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
        getRecipesTask.dispose();
        saveRecipeTask.dispose();
        deleteRecipeTask.dispose();
    }
}
