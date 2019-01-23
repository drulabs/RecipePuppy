package org.drulabs.recipepuppy.di;

import android.app.Application;

import org.drulabs.data.entities.DataRecipe;
import org.drulabs.data.repository.LocalDataSource;
import org.drulabs.persistence.db.RecipeDao;
import org.drulabs.persistence.db.RecipeDatabase;
import org.drulabs.persistence.mapper.PersistenceDataMapper;
import org.drulabs.persistence.mapper.PersistenceMapper;
import org.drulabs.persistence.source.LocalDataSourceImpl;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module(includes = PersistenceModule.Binders.class)
class PersistenceModule {

    interface Binders {
        @Binds
        LocalDataSource bindsLocalDataSource(LocalDataSourceImpl localDataSourceImpl);

        @Binds
        PersistenceMapper<DataRecipe> bindsPersistenceDataMapper(PersistenceDataMapper mapper);
    }

    @Provides
    RecipeDatabase providesRecipeDatabase(Application application) {
        return RecipeDatabase.getInstance(application.getApplicationContext());
    }

    @Provides
    RecipeDao providesRecipeDAO(RecipeDatabase database) {
        return database.getRecipeDao();
    }

}
