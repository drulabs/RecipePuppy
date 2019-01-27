package org.drulabs.data.cache;

import org.drulabs.data.entities.DataRecipe;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryCache {
    private static final long CACHE_EXPIRE_MILLIS = 30000;

    private static Map<String, List<DataRecipe>> memCache = new HashMap<>();
    private static long cacheLastUpdateTime;

    public static void addDataRecipe(String query, int page, DataRecipe dataRecipe) {

    }

    public static List<DataRecipe> getData(String query, int page) {
        return null;
    }
}
