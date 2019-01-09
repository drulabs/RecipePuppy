package org.drulabs.network;

import org.drulabs.data.entity.DataRecipe;
import org.drulabs.data.repository.RemoteDataSource;
import org.drulabs.network.api.RecipeService;
import org.drulabs.network.entities.NetworkRecipe;
import org.drulabs.network.mapper.NetworkDataMapper;
import org.drulabs.network.mapper.NetworkMapper;
import org.drulabs.network.source.NetworkDataSource;
import org.drulabs.network.utils.Generator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;

import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class NetworkDataSourceTest {

    @Mock
    RecipeService service;

    NetworkMapper<DataRecipe> mapper = new NetworkDataMapper();

    private RemoteDataSource dataSource;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        dataSource = new NetworkDataSource(mapper, service);
    }

    @Test
    public void testGetRecipes() {
        NetworkRecipe networkRecipe = Generator.generateRecipe("001");
        DataRecipe dataRecipe = mapper.mapTo(networkRecipe);

        String ingredients = "";
        String searchQuery = "pasta";
        int pageNum = 2;

        when(service.fetchRecipes(ingredients, searchQuery, pageNum))
                .thenReturn(Observable.just(networkRecipe));

        TestObserver<DataRecipe> testObserver = dataSource.getRecipes(searchQuery, pageNum)
                .test();
        testObserver.assertValue(recipe -> recipe.equals(dataRecipe));
    }

}
