package org.drulabs.data.utils;

import org.drulabs.data.entities.DataRecipe;

import java.util.Arrays;
import java.util.List;

public class Generator {

    public static DataRecipe generateDomainRecipe() {
        String name = "Test" + String.valueOf(System.currentTimeMillis());
        String detailsUrl = "href" + String.valueOf(System.currentTimeMillis());
        String recipeImageUrl = "thumb" + String.valueOf(System.currentTimeMillis());
        List<String> ingredients = Arrays.asList("raw chicken","green curry");

        return new DataRecipe(name, detailsUrl, ingredients, recipeImageUrl);
    }

}
