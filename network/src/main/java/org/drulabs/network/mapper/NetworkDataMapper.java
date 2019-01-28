package org.drulabs.network.mapper;

import org.drulabs.data.entities.DataRecipe;
import org.drulabs.network.entities.NetworkRecipe;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

public class NetworkDataMapper implements NetworkMapper<DataRecipe> {

    @Inject
    public NetworkDataMapper() {
    }

    @Override
    public NetworkRecipe mapFrom(DataRecipe recipe) {
        String strIngredients = recipe.getIngredients().toString().replace(", ", ",").replaceAll
                ("[\\[.\\]]", "");
        return (new NetworkRecipe(recipe.getName(), recipe.getDetailsUrl(), strIngredients,
                recipe.getRecipeImageUrl()));
    }

    @Override
    public DataRecipe mapTo(NetworkRecipe networkRecipe) {
        List<String> ingredients = Arrays.asList(networkRecipe.getCommaSeparatedIngredients()
                .split(","));
        return (new DataRecipe(networkRecipe.getName(), networkRecipe.getDetailsPage(),
                ingredients, networkRecipe.getImageUrl()));
    }
}
