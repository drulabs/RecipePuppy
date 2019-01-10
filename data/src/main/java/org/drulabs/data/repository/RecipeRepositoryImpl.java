package org.drulabs.data.repository;

import org.drulabs.data.entities.DataRecipe;
import org.drulabs.data.mapper.DomainMapper;
import org.drulabs.domain.entities.DomainRecipe;
import org.drulabs.domain.repository.RecipeRepository;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

public class RecipeRepositoryImpl implements RecipeRepository {

    private LocalDataSource localDataSource;
    private RemoteDataSource remoteDataSource;
    private DomainMapper<DomainRecipe> mapper;

    @Inject
    public RecipeRepositoryImpl(DomainMapper<DomainRecipe> mapper, LocalDataSource
            localDataSource, RemoteDataSource remoteDataSource) {
        this.mapper = mapper;
        this.localDataSource = localDataSource;
        this.remoteDataSource = remoteDataSource;
    }

    @Override
    public Observable<DomainRecipe> getRecipes(String searchQuery, int pageNum) {
        return remoteDataSource.getRecipes(searchQuery, pageNum)
                .map(domainRecipe -> mapper.mapTo(domainRecipe));

    }

    @Override
    public Observable<DomainRecipe> getSavedRecipes() {
        return localDataSource.getSavedRecipes()
                .map(domainRecipe -> mapper.mapTo(domainRecipe));
    }

    @Override
    public Completable saveRecipe(DomainRecipe domainRecipe) {
        DataRecipe dataRecipe = mapper.mapFrom(domainRecipe);
        return localDataSource.saveRecipe(dataRecipe);
    }

    @Override
    public Completable deleteRecipe(DomainRecipe domainRecipe) {
        DataRecipe dataRecipe = mapper.mapFrom(domainRecipe);
        return localDataSource.deleteRecipe(dataRecipe);
    }

    @Override
    public Completable deleteAllRecipes() {
        return localDataSource.deleteAllRecipes();
    }

    @Override
    public Single<DomainRecipe> getLastSavedRecipe() {
        return localDataSource.getLastSavedRecipe()
                .map(domainRecipe -> mapper.mapTo(domainRecipe));
    }
}
