package org.drulabs.data.cache;

import org.drulabs.data.entities.DataRecipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryCache {
    private static final long CACHE_EXPIRE_MILLIS = 30000;

    private static Map<String, List<DataRecipe>> memCache = new HashMap<>();
    private static long cacheLastUpdateTime;

    public static void addDataRecipe(String query, int page, DataRecipe dataRecipe) {
        if (memCache.get(query + "_" + page) == null) {
            memCache.put(query + "_" + page, new ArrayList<>());
        }
        memCache.get(query + "_" + page).add(dataRecipe);
        cacheLastUpdateTime = System.currentTimeMillis();
    }

    public static List<DataRecipe> getData(String query, int page) {
        boolean isCacheExpired =
                (System.currentTimeMillis() - cacheLastUpdateTime) >= CACHE_EXPIRE_MILLIS;

        if (isCacheExpired) {
            memCache.remove(query + "_" + page, null);
            return null;
        } else {
            return memCache.get(query + "_" + page);
        }
    }
}
