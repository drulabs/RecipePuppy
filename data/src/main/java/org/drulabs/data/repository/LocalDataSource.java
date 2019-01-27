package org.drulabs.data.repository;

import org.drulabs.data.entities.DataRecipe;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

public interface LocalDataSource {

    Observable<List<DataRecipe>> getSavedRecipes();

    Completable saveRecipe(DataRecipe recipe);

    Completable deleteRecipe(DataRecipe recipe);

    Completable deleteAllRecipes();

    Single<DataRecipe> getLastSavedRecipe();

    Single<DataRecipe> lookupRecipe(String name);

}
