package org.drulabs.domain.usecases;

import org.drulabs.domain.entities.Recipe;
import org.drulabs.domain.repository.RecipeRepository;
import org.drulabs.domain.usecases.base.ObservableUseCase;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.Scheduler;

public class GetSavedRecipes extends ObservableUseCase<Recipe, Void> {

    private RecipeRepository repository;

    @Inject
    public GetSavedRecipes(RecipeRepository repository, @Named("execution") Scheduler
            executionScheduler, @Named("postExecution") Scheduler postExecutionScheduler) {
        super(executionScheduler, postExecutionScheduler);
        this.repository = repository;
    }

    @Override
    protected Observable<Recipe> build(Void aVoid) {
        return repository.getSavedRecipes();
    }
}
