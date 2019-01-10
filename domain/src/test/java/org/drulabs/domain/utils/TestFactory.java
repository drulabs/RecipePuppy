package org.drulabs.domain.utils;

import org.drulabs.domain.entities.DomainRecipe;
import org.drulabs.domain.entities.RecipeRequest;

public class TestFactory {

    public static DomainRecipe getRecipe() {
        String title = "Test" + String.valueOf(System.currentTimeMillis());
        String href = "href" + String.valueOf(System.currentTimeMillis());
        String thumbnail = "thumb" + String.valueOf(System.currentTimeMillis());
        String ingredients = "ingredients" + String.valueOf(System.currentTimeMillis());

        return new DomainRecipe(title, href, ingredients, thumbnail);
    }

    public static RecipeRequest getRecipeRequest() {
        String query = "kotlin";
        int page = 1;

        return new RecipeRequest(query, page);
    }

}
