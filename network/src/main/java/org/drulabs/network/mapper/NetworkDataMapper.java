package org.drulabs.network.mapper;

import org.drulabs.data.entity.DataRecipe;
import org.drulabs.network.entities.NetworkRecipe;

import java.util.Arrays;
import java.util.List;

public class NetworkDataMapper implements NetworkMapper<DataRecipe> {
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
