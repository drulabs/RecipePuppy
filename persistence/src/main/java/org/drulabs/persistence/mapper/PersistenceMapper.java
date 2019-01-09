package org.drulabs.persistence.mapper;

import org.drulabs.persistence.entities.DBRecipe;

public interface PersistenceMapper<T> {

    DBRecipe mapFrom(T t);

    T mapTo(DBRecipe dbRecipe);

}
