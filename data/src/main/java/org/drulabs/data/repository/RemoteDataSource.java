package org.drulabs.data.repository;

import org.drulabs.data.entities.DataRecipe;

import io.reactivex.Observable;

public interface RemoteDataSource {

    Observable<DataRecipe> getRecipes(String searchQuery, int pageNum);

}
