package org.drulabs.data.repository;

import org.drulabs.data.entity.DataRecipe;
import org.drulabs.data.mapper.DomainMapper;
import org.drulabs.domain.entities.Recipe;
import org.drulabs.domain.repository.RecipeRepository;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

public class RecipeRepositoryImpl implements RecipeRepository {

    private LocalDataSource localDataSource;
    private RemoteDataSource remoteDataSource;
    private DomainMapper<Recipe> mapper;

    @Inject
    public RecipeRepositoryImpl(DomainMapper<Recipe> mapper, LocalDataSource
            localDataSource, RemoteDataSource remoteDataSource) {
        this.mapper = mapper;
        this.localDataSource = localDataSource;
        this.remoteDataSource = remoteDataSource;
    }


    @Override
    public Observable<Recipe> getRecipes(String searchQuery, int pageNum) {
        return remoteDataSource.getRecipes(searchQuery, pageNum)
                .map(domainRecipe -> mapper.mapTo(domainRecipe));

    }

    @Override
    public Observable<Recipe> getSavedRecipes() {
        return localDataSource.getSavedRecipes()
                .map(domainRecipe -> mapper.mapTo(domainRecipe));
    }

    @Override
    public Completable saveRecipe(Recipe recipe) {
        DataRecipe dataRecipe = mapper.mapFrom(recipe);
        return localDataSource.saveRecipe(dataRecipe);
    }

    @Override
    public Completable deleteRecipe(Recipe recipe) {
        DataRecipe dataRecipe = mapper.mapFrom(recipe);
        return localDataSource.deleteRecipe(dataRecipe);
    }

    @Override
    public Completable deleteAllRecipes() {
        return localDataSource.deleteAllRecipes();
    }

    @Override
    public Single<Recipe> getLastSavedRecipe() {
        return localDataSource.getLastSavedRecipe()
                .map(domainRecipe -> mapper.mapTo(domainRecipe));
    }
}
