package org.drulabs.data.mapper;

import org.drulabs.data.entities.DataRecipe;
import org.drulabs.domain.entities.DomainRecipe;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

public class DataDomainMapper implements DataMapper<DomainRecipe> {

    @Inject
    public DataDomainMapper() {
    }

    @Override
    public DomainRecipe mapTo(DataRecipe dataRecipe) {
        String ingredients = dataRecipe.getIngredients().toString()
                .replace(", ", ",")
                .replaceAll("[\\[.\\]]", "");

        return (new DomainRecipe(dataRecipe.getName(), dataRecipe.getDetailsUrl(), ingredients,
                dataRecipe.getRecipeImageUrl()));
    }

    @Override
    public DataRecipe mapFrom(DomainRecipe domainRecipe) {
        List<String> ingredientList = Arrays.asList(domainRecipe.getIngredients().split(","));
        return (new DataRecipe(domainRecipe.getTitle(), domainRecipe.getHref(), ingredientList, domainRecipe
                .getThumbnail()));
    }
}
