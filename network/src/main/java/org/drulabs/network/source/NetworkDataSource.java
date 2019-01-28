package org.drulabs.network.source;

import org.drulabs.data.entities.DataRecipe;
import org.drulabs.data.repository.RemoteDataSource;
import org.drulabs.network.api.RecipeService;
import org.drulabs.network.mapper.NetworkMapper;

import javax.inject.Inject;

import io.reactivex.Observable;

public class NetworkDataSource implements RemoteDataSource {

    private NetworkMapper<DataRecipe> mapper;
    private RecipeService service;

    @Inject
    public NetworkDataSource(NetworkMapper<DataRecipe> mapper, RecipeService service) {
        this.mapper = mapper;
        this.service = service;
    }

    @Override
    public Observable<DataRecipe> getRecipes(String searchQuery, int pageNum) {
        System.out.println("Remote source invoked");
        return service.fetchRecipes("", searchQuery, pageNum)
                .flatMap(networkRes -> Observable.fromIterable(networkRes.getRecipes()))
                .map(networkRecipe -> mapper.mapTo(networkRecipe));
    }
}
