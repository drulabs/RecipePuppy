package org.drulabs.domain.repository;

import org.drulabs.domain.entities.DomainRecipe;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

public interface RecipeRepository {

    Observable<DomainRecipe> getRecipes(String searchQuery, int pageNum);

    Observable<DomainRecipe> getSavedRecipes();

    Completable saveRecipe(DomainRecipe domainRecipe);

    Completable deleteRecipe(DomainRecipe domainRecipe);

    Completable deleteAllRecipes();

    Single<DomainRecipe> getLastSavedRecipe();

    Single<DomainRecipe> lookupSavedRecipe(String recipeName);

}
