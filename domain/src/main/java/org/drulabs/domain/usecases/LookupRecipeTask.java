package org.drulabs.domain.usecases;

import org.drulabs.domain.entities.DomainRecipe;
import org.drulabs.domain.repository.RecipeRepository;
import org.drulabs.domain.usecases.base.SingleUseCase;

import javax.inject.Named;

import io.reactivex.Scheduler;
import io.reactivex.Single;

public class LookupRecipeTask extends SingleUseCase<DomainRecipe, String> {

    private RecipeRepository repository;

    public LookupRecipeTask(RecipeRepository repository, @Named("execution") Scheduler
            executionScheduler, @Named("postExecution") Scheduler postExecutionScheduler) {
        super(executionScheduler, postExecutionScheduler);
        this.repository = repository;
    }

    @Override
    protected Single<DomainRecipe> build(String name) {
        return repository.lookupSavedRecipe(name);
    }
}
