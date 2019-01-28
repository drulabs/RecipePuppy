package org.drulabs.domain.usecases;

import org.drulabs.domain.entities.DomainRecipe;
import org.drulabs.domain.repository.RecipeRepository;
import org.drulabs.domain.usecases.base.ObservableUseCase;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.Scheduler;

public class GetSavedRecipesTask extends ObservableUseCase<List<DomainRecipe>, Void> {

    private RecipeRepository repository;

    @Inject
    public GetSavedRecipesTask(RecipeRepository repository, @Named("execution") Scheduler
            executionScheduler, @Named("postExecution") Scheduler postExecutionScheduler) {
        super(executionScheduler, postExecutionScheduler);
        this.repository = repository;
    }

    @Override
    protected Observable<List<DomainRecipe>> build(Void aVoid) {
        return repository.getSavedRecipes();
    }
}
