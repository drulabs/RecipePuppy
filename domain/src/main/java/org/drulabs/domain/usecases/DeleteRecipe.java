package org.drulabs.domain.usecases;

import org.drulabs.domain.entities.Recipe;
import org.drulabs.domain.repository.RecipeRepository;
import org.drulabs.domain.usecases.base.CompletableUseCase;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Completable;
import io.reactivex.Scheduler;

public class DeleteRecipe extends CompletableUseCase<Recipe> {

    private RecipeRepository repository;

    @Inject
    public DeleteRecipe(RecipeRepository repository, @Named("execution") Scheduler
            executionScheduler, @Named("postExecution") Scheduler postExecutionScheduler) {
        super(executionScheduler, postExecutionScheduler);
        this.repository = repository;
    }

    @Override
    protected Completable build(Recipe recipe) {
        return repository.deleteRecipe(recipe);
    }
}
