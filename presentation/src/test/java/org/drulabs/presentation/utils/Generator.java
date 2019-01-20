package org.drulabs.presentation.utils;

import org.drulabs.presentation.entities.PresentationRecipe;

import java.util.Arrays;
import java.util.List;

public class Generator {

    public static PresentationRecipe generateRecipe(String identifier) {
        String name = "Test" + identifier;
        String detailsUrl = "href" + identifier;
        String thumbNailUrl = "thumb" + identifier;
        List<String> ingredientList = Arrays.asList("raw chicken", "green curry", identifier);

        return (new PresentationRecipe(name, detailsUrl, ingredientList, thumbNailUrl, false));
    }

}
