package org.drulabs.persistence.converters;

import android.arch.persistence.room.TypeConverter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class IngredientListConverter {

    @TypeConverter
    public static List<String> createIngredientListFrom(String ingredients) {
        if (ingredients == null) {
            return Collections.emptyList();
        }

        return Arrays.asList(ingredients.split(","));
    }

    @TypeConverter
    public static String createIngredientStringRepFrom(List<String> ingredients) {
        if (ingredients == null) {
            return "";
        }
        return ingredients.toString().replace(", ", ",").replaceAll("[\\[.\\]]",
                "");
    }
}
