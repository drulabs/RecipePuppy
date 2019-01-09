package org.drulabs.network.utils;

import org.drulabs.network.entities.NetworkRecipe;

import java.util.Arrays;
import java.util.List;

public class Generator {

    public static NetworkRecipe generateRecipe(String identifier) {
        String recipeName = "Test" + identifier;
        String detailsUrl = "href" + identifier;
        String recipeImageUrl = "thumb" + identifier;
        String ingredients = "raw chicken,green curry,meruem";

        return (new NetworkRecipe(recipeName, detailsUrl, ingredients, recipeImageUrl));
    }

}
