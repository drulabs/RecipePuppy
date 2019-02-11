package org.drulabs.bakencook.utils;

import org.drulabs.network.api.RecipeService;
import org.drulabs.network.entities.NetworkRecipeResponse;

import io.reactivex.Observable;

public class FakeRecipeRetrofitService implements RecipeService {

    @Override
    public Observable<NetworkRecipeResponse> fetchRecipes(String ingredients, String searchQuery,
                                                          int pageNum) {
        return Observable.just(Generator.getSampleResponse());
    }
}
