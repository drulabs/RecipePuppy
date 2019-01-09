package org.drulabs.domain.utils;

import org.drulabs.domain.entities.Recipe;
import org.drulabs.domain.entities.RecipeRequest;

public class TestFactory {

    public static Recipe getRecipe() {
        String title = "Test" + String.valueOf(System.currentTimeMillis());
        String href = "href" + String.valueOf(System.currentTimeMillis());
        String thumbnail = "thumb" + String.valueOf(System.currentTimeMillis());
        String ingredients = "ingredients" + String.valueOf(System.currentTimeMillis());

        return new Recipe(title, href, ingredients, thumbnail);
    }

    public static RecipeRequest getRecipeRequest() {
        String query = "kotlin";
        int page = 1;

        return new RecipeRequest(query, page);
    }

}
