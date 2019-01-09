package org.drulabs.network.api;

import org.drulabs.network.entities.NetworkRecipeResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RecipeService {

    String BASE_URL = "http://www.recipepuppy.com/";

    @GET("api")
    Observable<NetworkRecipeResponse> fetchRecipes(@Query("i") String ingredients, @Query("q")
            String searchQuery, @Query("p") int pageNum);
}
