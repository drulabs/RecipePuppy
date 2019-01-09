package org.drulabs.domain.usecases;

import org.drulabs.domain.entities.Recipe;
import org.drulabs.domain.repository.RecipeRepository;
import org.drulabs.domain.usecases.base.SingleUseCase;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Scheduler;
import io.reactivex.Single;

public class GetLastSavedRecipe extends SingleUseCase<Recipe, Void> {

    private RecipeRepository repository;

    @Inject
    public GetLastSavedRecipe(RecipeRepository repository, @Named("execution") Scheduler
            executionScheduler, @Named("postExecution") Scheduler postExecutionScheduler) {
        super(executionScheduler, postExecutionScheduler);
        this.repository = repository;
    }

    @Override
    protected Single<Recipe> build(Void aVoid) {
        return repository.getLastSavedRecipe();
    }
}
