package org.drulabs.persistence.source;

import org.drulabs.data.entities.DataRecipe;
import org.drulabs.data.repository.LocalDataSource;
import org.drulabs.persistence.db.RecipeDao;
import org.drulabs.persistence.entities.DBRecipe;
import org.drulabs.persistence.mapper.PersistenceMapper;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

public class LocalDataSourceImpl implements LocalDataSource {

    private RecipeDao dao;
    private PersistenceMapper<DataRecipe> mapper;

    @Inject
    public LocalDataSourceImpl(PersistenceMapper<DataRecipe> mapper, RecipeDao dao) {
        this.mapper = mapper;
        this.dao = dao;
    }

    @Override
    public Observable<List<DataRecipe>> getSavedRecipes() {
        return dao.getSavedRecipes().map(dbRecipes -> {
            List<DataRecipe> dataRecipes = new ArrayList<>();
            for (DBRecipe dbRecipe : dbRecipes) {
                dataRecipes.add(mapper.mapTo(dbRecipe));
            }
            return dataRecipes;
        });
    }

    @Override
    public Completable saveRecipe(DataRecipe recipe) {
        DBRecipe dbRecipe = mapper.mapFrom(recipe);
        return Completable.fromAction(() -> dao.saveRecipe(dbRecipe));
    }

    @Override
    public Completable deleteRecipe(DataRecipe recipe) {
        DBRecipe dbRecipe = mapper.mapFrom(recipe);
        return Completable.fromAction(() -> dao.deleteRecipe(dbRecipe));
    }

    @Override
    public Completable deleteAllRecipes() {
        return Completable.fromAction(() -> dao.deleteAllRecipes());
    }

    @Override
    public Single<DataRecipe> getLastSavedRecipe() {
        return dao.getLastSavedRecipe().map(recipe -> mapper.mapTo(recipe));
    }

    @Override
    public Single<DataRecipe> lookupRecipe(String name) {
        return dao.lookupRecipe(name).map(recipe -> mapper.mapTo(recipe));
    }
}
