package org.drulabs.recipepuppy.di;

import org.drulabs.data.entities.DataRecipe;
import org.drulabs.data.repository.RemoteDataSource;
import org.drulabs.network.api.RecipeService;
import org.drulabs.network.mapper.NetworkDataMapper;
import org.drulabs.network.mapper.NetworkMapper;
import org.drulabs.network.source.NetworkDataSource;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module(includes = NetworkModule.Binders.class)
class NetworkModule {

    @Module
    interface Binders {
        @Binds
        RemoteDataSource bindNetworkDataSource(NetworkDataSource networkDataSource);

        @Binds
        NetworkMapper<DataRecipe> bindsNetworkDataMapper(NetworkDataMapper mapper);
    }

    @Provides
    RecipeService providesRecipeService(Retrofit retrofit) {
        return retrofit.create(RecipeService.class);
    }

    @Provides
    Retrofit providesRetrofit() {
        return new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(RecipeService.BASE_URL)
                .build();
    }

    // TODO add interceptors, logger, http client
}
