package org.drulabs.persistence.mapper;

import org.drulabs.data.entities.DataRecipe;
import org.drulabs.persistence.entities.DBRecipe;

import javax.inject.Inject;

public class PersistenceDataMapper implements PersistenceMapper<DataRecipe> {

    @Inject
    public PersistenceDataMapper() {
    }

    @Override
    public DBRecipe mapFrom(DataRecipe dataRecipe) {
        return (new DBRecipe(dataRecipe.getName(), dataRecipe.getDetailsUrl(), dataRecipe
                .getIngredients(), dataRecipe.getRecipeImageUrl()));
    }

    @Override
    public DataRecipe mapTo(DBRecipe dbRecipe) {
        return (new DataRecipe(dbRecipe.getRecipeName(), dbRecipe.getDetailsUrl(), dbRecipe
                .getIngredients(), dbRecipe.getRecipeImageUrl()));
    }
}
