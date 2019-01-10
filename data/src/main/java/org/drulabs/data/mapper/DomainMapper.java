package org.drulabs.data.mapper;

import org.drulabs.data.entities.DataRecipe;

public interface DomainMapper<R> {
    R mapTo(DataRecipe dataRecipe);

    DataRecipe mapFrom(R r);
}
