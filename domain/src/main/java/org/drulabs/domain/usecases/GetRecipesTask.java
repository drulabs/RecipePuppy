package org.drulabs.domain.usecases;

import org.drulabs.domain.entities.DomainRecipe;
import org.drulabs.domain.entities.RecipeRequest;
import org.drulabs.domain.repository.RecipeRepository;
import org.drulabs.domain.usecases.base.ObservableUseCase;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.Scheduler;

public class GetRecipesTask extends ObservableUseCase<DomainRecipe, RecipeRequest> {

    private RecipeRepository repository;

    @Inject
    public GetRecipesTask(RecipeRepository repository, @Named("execution") Scheduler
            executionScheduler, @Named("postExecution") Scheduler postExecutionScheduler) {
        super(executionScheduler, postExecutionScheduler);
        this.repository = repository;
    }

    @Override
    protected Observable<DomainRecipe> build(RecipeRequest request) {
        return repository.getRecipes(request.getSearchQuery(), request.getPageNum());
    }
}
