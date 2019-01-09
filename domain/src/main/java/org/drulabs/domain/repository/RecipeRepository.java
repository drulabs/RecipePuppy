package org.drulabs.domain.repository;

import org.drulabs.domain.entities.Recipe;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

public interface RecipeRepository {

    Observable<Recipe> getRecipes(String searchQuery, int pageNum);

    Observable<Recipe> getSavedRecipes();

    Completable saveRecipe(Recipe recipe);

    Completable deleteRecipe(Recipe recipe);

    Completable deleteAllRecipes();

    Single<Recipe> getLastSavedRecipe();

}
