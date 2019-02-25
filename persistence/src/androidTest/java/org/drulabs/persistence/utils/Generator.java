package org.drulabs.persistence.utils;

import org.drulabs.persistence.entities.DBRecipe;

import java.util.Arrays;
import java.util.List;

public class Generator {

    public static DBRecipe generateRecipe(String identifier) {
        String recipeName = "Test" + identifier;
        String detailsUrl = "href" + identifier;
        String recipeImageUrl = "thumb" + identifier;
        List<String> ingredients = Arrays.asList("raw chicken","green curry", identifier);

        return (new DBRecipe(recipeName, detailsUrl, ingredients, recipeImageUrl));
    }

    public static DBRecipe generateRecipe(String identifier, long createDate) {
        String recipeName = "Test" + identifier;
        String detailsUrl = "href" + identifier;
        String recipeImageUrl = "thumb" + identifier;
        List<String> ingredients = Arrays.asList("raw chicken","green curry", identifier);

        DBRecipe dbRecipe =new DBRecipe(recipeName, detailsUrl, ingredients, recipeImageUrl);
        dbRecipe.setCreateTime(createDate);

        return dbRecipe;
    }

}
