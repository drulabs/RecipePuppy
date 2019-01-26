package org.drulabs.presentation.data;

import android.os.Handler;

import org.drulabs.domain.entities.DomainRecipe;
import org.drulabs.domain.entities.RecipeRequest;
import org.drulabs.domain.usecases.GetRecipesTask;
import org.drulabs.presentation.entities.PresentationRecipe;
import org.drulabs.presentation.mapper.PresentationMapper;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.LiveData;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableSingleObserver;

public class RecipesLiveData extends LiveData<Model<List<PresentationRecipe>>> {

    private static final long DELAY_FOR_DISPOSE = 2000;
    private static final String DEFAULT_SEARCH_TERM = "pasta";
    private static final int DEFAULT_PAGE_NUM = 4;

    private GetRecipesTask getRecipesTask;
    private PresentationMapper<DomainRecipe> mapper;
    private RecipeRequest recipeRequest = new RecipeRequest(DEFAULT_SEARCH_TERM, DEFAULT_PAGE_NUM);

    private boolean isDisposePending = false;
    private Handler disposerHandler = new Handler();
    private SingleDisposable disposableSingleObserver;

    private Runnable disposer = new Runnable() {
        @Override
        public void run() {
            disposableSingleObserver.dispose();
            isDisposePending = false;
        }
    };

    public RecipesLiveData(@NonNull PresentationMapper<DomainRecipe> mapper,
                           @NonNull GetRecipesTask getRecipesTask) {
        this.mapper = mapper;
        this.getRecipesTask = getRecipesTask;
        this.disposableSingleObserver = new SingleDisposable();
    }

    public void setRecipeRequest(RecipeRequest recipeRequest) {
        this.recipeRequest = recipeRequest;
    }

    @Override
    protected void onActive() {
        if (isDisposePending) {
            disposerHandler.removeCallbacks(disposer);
        } else {
            postValue(Model.loading(true));
            if (disposableSingleObserver.isDisposed()) {
                disposableSingleObserver = new SingleDisposable();
            }
            getRecipesTask.run(disposableSingleObserver, recipeRequest);
        }
        isDisposePending = false;
    }

    @Override
    protected void onInactive() {
        disposerHandler.postDelayed(disposer, DELAY_FOR_DISPOSE);
        isDisposePending = true;
    }

    private class SingleDisposable extends DisposableSingleObserver<List<DomainRecipe>> {

        @Override
        public void onSuccess(List<DomainRecipe> domainRecipes) {
            List<PresentationRecipe> presentationRecipes = new ArrayList<>();
            if (domainRecipes != null) {
                for (DomainRecipe d : domainRecipes) {
                    PresentationRecipe presentationRecipe = mapper.mapFrom(d);
                    presentationRecipes.add(presentationRecipe);
                }
            }
            postValue(Model.success(presentationRecipes));
        }

        @Override
        public void onError(Throwable e) {
            postValue(Model.error(e));
        }
    }
}
