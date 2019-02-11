package org.drulabs.bakencook.utils;

import com.google.gson.Gson;

import org.drulabs.network.entities.NetworkRecipeResponse;

public class Generator {

    static NetworkRecipeResponse getSampleResponse() {
        return (new Gson()).fromJson(SAMPLE_RESPONSE, NetworkRecipeResponse.class);
    }

    private static final String SAMPLE_RESPONSE = "{\n" +
            "  \"title\": \"Recipe Puppy\",\n" +
            "  \"version\": 0.1,\n" +
            "  \"href\": \"http:\\/\\/www.recipepuppy.com\\/\",\n" +
            "  \"results\": [\n" +
            "    {\n" +
            "      \"title\": \"Chicken Caesar Pasta Salad\",\n" +
            "      \"href\": \"http:\\/\\/www.recipezaar.com\\/Chicken-Caesar-Pasta-Salad-195066\",\n" +
            "      \"ingredients\": \"caesar salad dressing, chicken broth, pasta, croutons, green onion, italian cheese blend, parmesan cheese, red pepper, chicken\",\n" +
            "      \"thumbnail\": \"\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"title\": \"Chicken Pesto Pasta Salad\",\n" +
            "      \"href\": \"http:\\/\\/www.recipezaar.com\\/Chicken-Pesto-Pasta-Salad-300402\",\n" +
            "      \"ingredients\": \"pesto, cherry tomato, chicken, black pepper, olive oil, pasta, salt, white wine vinegar\",\n" +
            "      \"thumbnail\": \"\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"title\": \"Cold Thai Pasta Salad\",\n" +
            "      \"href\": \"http:\\/\\/www.recipezaar.com\\/Cold-Thai-Pasta-Salad-258948\",\n" +
            "      \"ingredients\": \"bean sprouts, shrimp, cucumber, coriander, mint, green onion, linguine, red pepper\",\n" +
            "      \"thumbnail\": \"http:\\/\\/img.recipepuppy.com\\/320315.jpg\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"title\": \"Creamy Pasta With Chicken, Broccoli and Basil - Low Fat Version\",\n" +
            "      \"href\": \"http:\\/\\/www.recipezaar.com\\/Creamy-Pasta-With-Chicken-Broccoli-and-Basil-Low-Fat-Version-321548\",\n" +
            "      \"ingredients\": \"evaporated milk, flour, broccoli, butter, chicken broth, chicken, tomato, dry pasta, basil, garlic, red pepper flakes, parmesan cheese, salt\",\n" +
            "      \"thumbnail\": \"http:\\/\\/img.recipepuppy.com\\/335449.jpg\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"title\": \"Creamy Spinach and Mushroom Penne Pasta\",\n" +
            "      \"href\": \"http:\\/\\/www.recipezaar.com\\/Creamy-Spinach-and-Mushroom-Penne-Pasta-227463\",\n" +
            "      \"ingredients\": \"portabella mushrooms, tomato, basil, oregano, fennel seed, spinach, garlic, black pepper, cream cheese, olive oil, parmesan cheese, penne, red onions, red pepper flakes, salt, seasoning, tomato sauce\",\n" +
            "      \"thumbnail\": \"http:\\/\\/img.recipepuppy.com\\/335994.jpg\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"title\": \"Creamy Vegetable Pasta\",\n" +
            "      \"href\": \"http:\\/\\/www.recipezaar.com\\/Creamy-Vegetable-Pasta-206389\",\n" +
            "      \"ingredients\": \"crabmeat, mushroom, garlic, cream cheese, olive oil, oregano, parmesan cheese, black pepper, red pepper, salt, skim milk, pasta (in general), zucchini\",\n" +
            "      \"thumbnail\": \"\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"title\": \"Carrabba's Cavatappi Amatriciana (Side Dish Pasta)\",\n" +
            "      \"href\": \"http:\\/\\/www.recipezaar.com\\/Carrabbas-Cavatappi-Amatriciana-Side-Dish-Pasta-351808\",\n" +
            "      \"ingredients\": \"cavatappi pasta, tomato, white wine, garlic, olive oil, onions, pancetta, romano cheese, red pepper flakes, salt\",\n" +
            "      \"thumbnail\": \"\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"title\": \"Simple And Easy Pasta Dough Recipe\",\n" +
            "      \"href\": \"http:\\/\\/www.grouprecipes.com\\/95155\\/simple-and-easy-pasta-dough.html\",\n" +
            "      \"ingredients\": \"semolina flour, eggs, olive oil, salt\",\n" +
            "      \"thumbnail\": \"\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"title\": \"Fagiole Pasta (Not Pasta Fagiole Soup)\",\n" +
            "      \"href\": \"http:\\/\\/www.recipezaar.com\\/fagiole-pasta-not-pasta-fagiole-soup-358673\",\n" +
            "      \"ingredients\": \"chicken broth, pasta, olive oil, garlic, tomato sauce, white beans, onions\",\n" +
            "      \"thumbnail\": \"\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"title\": \"Spinach and Ricotta Cheese Sauce for Pasta\",\n" +
            "      \"href\": \"http:\\/\\/www.recipezaar.com\\/Spinach-and-Ricotta-Cheese-Sauce-for-Pasta-96927\",\n" +
            "      \"ingredients\": \"black pepper, spinach, thyme, garlic, nutmeg, olive oil, onions, pine nuts, ricotta cheese, salt, vegetable broth\",\n" +
            "      \"thumbnail\": \"http:\\/\\/img.recipepuppy.com\\/617624.jpg\"\n" +
            "    }\n" +
            "  ]\n" +
            "}";
}
