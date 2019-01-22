package org.drulabs.data.mapper;

import org.drulabs.data.entities.DataRecipe;

public interface DataMapper<R> {
    R mapTo(DataRecipe dataRecipe);

    DataRecipe mapFrom(R r);
}
