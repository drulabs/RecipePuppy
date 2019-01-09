package org.drulabs.data.mapper;

import org.drulabs.data.entity.DataRecipe;
import org.drulabs.domain.entities.Recipe;

import java.util.Arrays;
import java.util.List;

public class DomainDataMapper implements DomainMapper<Recipe> {

    @Override
    public Recipe mapTo(DataRecipe dataRecipe) {
        String ingredients = dataRecipe.getIngredients().toString()
                .replace(", ", ",")
                .replaceAll("[\\[.\\]]", "");

        return (new Recipe(dataRecipe.getName(), dataRecipe.getDetailsUrl(), ingredients,
                dataRecipe.getRecipeImageUrl()));
    }

    @Override
    public DataRecipe mapFrom(Recipe recipe) {
        List<String> ingredientList = Arrays.asList(recipe.getIngredients().split(","));
        return (new DataRecipe(recipe.getTitle(), recipe.getHref(), ingredientList, recipe
                .getThumbnail()));
    }
}
