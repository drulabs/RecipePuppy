package org.drulabs.presentation.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import org.drulabs.domain.entities.DomainRecipe;
import org.drulabs.domain.entities.RecipeRequest;
import org.drulabs.domain.usecases.DeleteRecipeTask;
import org.drulabs.domain.usecases.GetRecipesTask;
import org.drulabs.domain.usecases.LookupRecipeTask;
import org.drulabs.domain.usecases.SaveRecipeTask;
import org.drulabs.presentation.entities.PresentationRecipe;
import org.drulabs.presentation.mapper.PresentationMapper;
import org.drulabs.presentation.custom.SingleLiveEvent;

import java.util.List;

import javax.inject.Inject;

public class HomeVM extends BaseVM {

    private MutableLiveData<Model<List<PresentationRecipe>>> recipesLiveData = new
            MutableLiveData<>();
    private SingleLiveEvent<Boolean> saveRecipeStatus = new SingleLiveEvent<>();
    private SingleLiveEvent<Boolean> deleteRecipeStatus = new SingleLiveEvent<>();

    private PresentationMapper<DomainRecipe> mapper;

    private SaveRecipeTask saveRecipeTask;
    private DeleteRecipeTask deleteRecipeTask;
    private GetRecipesTask getRecipesTask;
    private LookupRecipeTask lookupRecipeTask;

    @Inject
    public HomeVM(PresentationMapper<DomainRecipe> mapper,
                  GetRecipesTask getRecipesTask,
                  SaveRecipeTask saveRecipeTask,
                  DeleteRecipeTask deleteRecipeTask,
                  LookupRecipeTask lookupRecipeTask) {
        this.mapper = mapper;
        this.getRecipesTask = getRecipesTask;
        this.saveRecipeTask = saveRecipeTask;
        this.deleteRecipeTask = deleteRecipeTask;
        this.lookupRecipeTask = lookupRecipeTask;
    }

    public LiveData<Model<List<PresentationRecipe>>> getRecipesLiveData() {
        return recipesLiveData;
    }

    public LiveData<Boolean> getSaveRecipeStatus() {
        return saveRecipeStatus;
    }

    public LiveData<Boolean> getDeleteRecipeStatus() {
        return deleteRecipeStatus;
    }

    public void searchRecipes(String searchText, int pageNum) {
        recipesLiveData.postValue(Model.loading(true));
        RecipeRequest request = new RecipeRequest(searchText, pageNum);
        addDisposable(
                getRecipesTask.run(request)
                        .map(mapper::mapFrom)
                        .zipWith(lookupRecipeTask.run(request.getSearchQuery()).toObservable(),
                                (presentationRecipe, domainRecipe) -> {
                                    presentationRecipe.setFavorite(domainRecipe != null);
                                    return presentationRecipe;
                                })
                        .toList()
                        .subscribe(e -> recipesLiveData.postValue(Model.success(e)),
                                throwable -> recipesLiveData.postValue(Model.error(throwable)))
        );
    }

    public void saveRecipeAsFavorite(PresentationRecipe presentationRecipe) {
        addDisposable(
                saveRecipeTask.run(mapper.mapTo(presentationRecipe))
                        .subscribe(() -> saveRecipeStatus.postValue(true),
                                throwable -> saveRecipeStatus.postValue(false))
        );
    }

    public void deleteRecipeFromFavorite(PresentationRecipe presentationRecipe) {
        addDisposable(
                deleteRecipeTask.run(mapper.mapTo(presentationRecipe))
                        .subscribe(() -> deleteRecipeStatus.postValue(true),
                                throwable -> deleteRecipeStatus.postValue(false))
        );
    }
}
