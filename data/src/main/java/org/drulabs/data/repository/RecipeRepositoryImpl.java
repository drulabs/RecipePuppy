package org.drulabs.data.repository;

import org.drulabs.data.entities.DataRecipe;
import org.drulabs.data.mapper.DataMapper;
import org.drulabs.domain.entities.DomainRecipe;
import org.drulabs.domain.repository.RecipeRepository;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.functions.Function;

public class RecipeRepositoryImpl implements RecipeRepository {

    private LocalDataSource localDataSource;
    private RemoteDataSource remoteDataSource;
    private DataMapper<DomainRecipe> mapper;

    @Inject
    public RecipeRepositoryImpl(DataMapper<DomainRecipe> mapper, LocalDataSource
            localDataSource, RemoteDataSource remoteDataSource) {
        this.mapper = mapper;
        this.localDataSource = localDataSource;
        this.remoteDataSource = remoteDataSource;
    }

    @Override
    public Observable<DomainRecipe> getRecipes(String searchQuery, int pageNum) {
        return remoteDataSource.getRecipes(searchQuery, pageNum)
                .map(dataRecipe -> mapper.mapTo(dataRecipe))
                .flatMap((Function<DomainRecipe, ObservableSource<DomainRecipe>>)
                        domainRecipe -> localDataSource.lookupRecipe(domainRecipe.getTitle())
                                .map(dataRecipe -> {
                                    if (dataRecipe != null) {
                                        DomainRecipe recipe = mapper.mapTo(dataRecipe);
                                        recipe.setStarred(true);
                                        return recipe;
                                    } else {
                                        return domainRecipe;
                                    }
                                }).onErrorReturn(throwable -> domainRecipe)
                                .toObservable());

    }

    @Override
    public Observable<List<DomainRecipe>> getSavedRecipes() {
        return localDataSource.getSavedRecipes().map(dataRecipes -> {
            List<DomainRecipe> domainRecipes = new ArrayList<>();
            for (DataRecipe dataRecipe : dataRecipes) {
                domainRecipes.add(mapper.mapTo(dataRecipe));
            }
            return domainRecipes;
        });
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

    @Override
    public Single<DomainRecipe> lookupSavedRecipe(String recipeName) {
        return localDataSource.lookupRecipe(recipeName)
                .map(dataRecipe -> mapper.mapTo(dataRecipe));
    }
}
