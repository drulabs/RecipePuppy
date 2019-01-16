package org.drulabs.domain.usecases;

import org.drulabs.domain.entities.DomainRecipe;
import org.drulabs.domain.repository.RecipeRepository;
import org.drulabs.domain.usecases.base.SingleUseCase;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Scheduler;
import io.reactivex.Single;

public class GetLastSavedRecipeTask extends SingleUseCase<DomainRecipe, Void> {

    private RecipeRepository repository;

    @Inject
    public GetLastSavedRecipeTask(RecipeRepository repository, @Named("execution") Scheduler
            executionScheduler, @Named("postExecution") Scheduler postExecutionScheduler) {
        super(executionScheduler, postExecutionScheduler);
        this.repository = repository;
    }

    @Override
    protected Single<DomainRecipe> build(Void aVoid) {
        return repository.getLastSavedRecipe();
    }
}
