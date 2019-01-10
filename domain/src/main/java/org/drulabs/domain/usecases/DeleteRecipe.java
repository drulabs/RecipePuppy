package org.drulabs.domain.usecases;

import org.drulabs.domain.entities.DomainRecipe;
import org.drulabs.domain.repository.RecipeRepository;
import org.drulabs.domain.usecases.base.CompletableUseCase;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Completable;
import io.reactivex.Scheduler;

public class DeleteRecipe extends CompletableUseCase<DomainRecipe> {

    private RecipeRepository repository;

    @Inject
    public DeleteRecipe(RecipeRepository repository, @Named("execution") Scheduler
            executionScheduler, @Named("postExecution") Scheduler postExecutionScheduler) {
        super(executionScheduler, postExecutionScheduler);
        this.repository = repository;
    }

    @Override
    protected Completable build(DomainRecipe domainRecipe) {
        return repository.deleteRecipe(domainRecipe);
    }
}
