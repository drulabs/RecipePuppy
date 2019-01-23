package org.drulabs.presentation.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import org.drulabs.domain.entities.DomainRecipe;
import org.drulabs.domain.entities.RecipeRequest;
import org.drulabs.domain.usecases.DeleteRecipeTask;
import org.drulabs.domain.usecases.GetRecipesTask;
import org.drulabs.domain.usecases.SaveRecipeTask;
import org.drulabs.presentation.custom.SingleLiveEvent;
import org.drulabs.presentation.entities.PresentationRecipe;
import org.drulabs.presentation.mapper.PresentationMapper;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.observers.DisposableSingleObserver;

public class HomeVM extends ViewModel {

    private MutableLiveData<Model<List<PresentationRecipe>>> recipesLiveData = new
            MutableLiveData<>();
    private SingleLiveEvent<Boolean> saveRecipeStatus = new SingleLiveEvent<>();
    private SingleLiveEvent<Boolean> deleteRecipeStatus = new SingleLiveEvent<>();

    private PresentationMapper<DomainRecipe> mapper;

    private SaveRecipeTask saveRecipeTask;
    private DeleteRecipeTask deleteRecipeTask;
    private GetRecipesTask getRecipesTask;

    public HomeVM(PresentationMapper<DomainRecipe> mapper,
                  GetRecipesTask getRecipesTask,
                  SaveRecipeTask saveRecipeTask,
                  DeleteRecipeTask deleteRecipeTask) {
        this.mapper = mapper;
        this.getRecipesTask = getRecipesTask;
        this.saveRecipeTask = saveRecipeTask;
        this.deleteRecipeTask = deleteRecipeTask;
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

        getRecipesTask.run(disposableSingleObserver, request);
    }

    public void saveRecipeAsFavorite(PresentationRecipe presentationRecipe) {
        saveRecipeTask.run(new BooleanObserver(saveRecipeStatus),
                mapper.mapTo(presentationRecipe));
    }

    public void deleteRecipeFromFavorite(PresentationRecipe presentationRecipe) {
        deleteRecipeTask.run(new BooleanObserver(deleteRecipeStatus),
                mapper.mapTo(presentationRecipe));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        saveRecipeTask.dispose();
        getRecipesTask.dispose();
        deleteRecipeTask.dispose();
    }

    private class BooleanObserver extends DisposableCompletableObserver {

        private MutableLiveData<Boolean> status;

        BooleanObserver(MutableLiveData<Boolean> status) {
            this.status = status;
        }

        @Override
        public void onComplete() {
            status.setValue(true);
        }

        @Override
        public void onError(Throwable e) {
            status.setValue(false);
        }
    }

    private DisposableSingleObserver<List<DomainRecipe>> disposableSingleObserver = new
            DisposableSingleObserver<List<DomainRecipe>>() {
                @Override
                public void onSuccess(List<DomainRecipe> domainRecipes) {
                    List<PresentationRecipe> presentationRecipes = new ArrayList<>();
                    if (domainRecipes != null) {
                        for (DomainRecipe d : domainRecipes) {
                            PresentationRecipe presentationRecipe = mapper.mapFrom(d);
                            presentationRecipes.add(presentationRecipe);
                        }

                    }
                    recipesLiveData.postValue(Model.success(presentationRecipes));
                }

                @Override
                public void onError(Throwable e) {
                    recipesLiveData.postValue(Model.error(e));
                }
            };
}
