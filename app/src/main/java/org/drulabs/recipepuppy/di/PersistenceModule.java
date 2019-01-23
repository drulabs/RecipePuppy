package org.drulabs.recipepuppy.di;

import org.drulabs.data.entities.DataRecipe;
import org.drulabs.data.repository.LocalDataSource;
import org.drulabs.persistence.mapper.PersistenceDataMapper;
import org.drulabs.persistence.mapper.PersistenceMapper;
import org.drulabs.persistence.source.LocalDataSourceImpl;

import dagger.Binds;
import dagger.Module;

@Module
abstract class PersistenceModule {

    @Binds
    abstract LocalDataSource bindsLocalDataSource(LocalDataSourceImpl localDataSourceImpl);

    @Binds
    abstract PersistenceMapper<DataRecipe> bindsPersistenceDataMapper(PersistenceDataMapper mapper);

}
