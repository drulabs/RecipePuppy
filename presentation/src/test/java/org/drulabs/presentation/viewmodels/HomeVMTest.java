package org.drulabs.presentation.viewmodels;

import org.drulabs.domain.entities.DomainRecipe;
import org.drulabs.domain.entities.RecipeRequest;
import org.drulabs.domain.repository.RecipeRepository;
import org.drulabs.domain.usecases.DeleteRecipeTask;
import org.drulabs.domain.usecases.GetRecipesTask;
import org.drulabs.domain.usecases.SaveRecipeTask;
import org.drulabs.presentation.data.Model;
import org.drulabs.presentation.entities.PresentationRecipe;
import org.drulabs.presentation.mapper.PresentationDomainMapper;
import org.drulabs.presentation.mapper.PresentationMapper;
import org.drulabs.presentation.utils.Generator;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class HomeVMTest {
    @Rule
    public InstantTaskExecutorRule rule = new InstantTaskExecutorRule();

    private PresentationMapper<DomainRecipe> mapper = new PresentationDomainMapper();

    @Mock
    private RecipeRepository recipeRepository;

    private HomeVM homeVM;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        GetRecipesTask getRecipesTask = new GetRecipesTask(recipeRepository,
                Schedulers.trampoline(), Schedulers.trampoline());
        SaveRecipeTask saveRecipeTask = new SaveRecipeTask(recipeRepository,
                Schedulers.trampoline(), Schedulers.trampoline());
        DeleteRecipeTask deleteRecipeTask = new DeleteRecipeTask(recipeRepository,
                Schedulers.trampoline(), Schedulers.trampoline());

        homeVM = new HomeVM(mapper, getRecipesTask, saveRecipeTask, deleteRecipeTask);
    }

    @Test
    public void testSearchRecipes() {
        PresentationRecipe presentationRecipe1 = Generator.generateRecipe("01");
        PresentationRecipe presentationRecipe2 = Generator.generateRecipe("02");
        DomainRecipe domainRecipe1 = mapper.mapTo(presentationRecipe1);
        DomainRecipe domainRecipe2 = mapper.mapTo(presentationRecipe2);

        RecipeRequest request = new RecipeRequest("test", 1);

        when(recipeRepository.getRecipes(request.getSearchQuery(), request.getPageNum()))
                .thenReturn(Observable.just(domainRecipe1, domainRecipe2));

        LiveData<Model<List<PresentationRecipe>>> recipes = homeVM.getRecipesLiveData();
        recipes.observeForever(Generator.generateMockObserver());
        homeVM.searchRecipes(request.getSearchQuery());

        assertTrue(
                recipes.getValue().getData().contains(presentationRecipe1) &&
                        recipes.getValue().getData().contains(presentationRecipe2)
        );
    }

    @Test
    public void success_testSaveRecipeAsFavorite() {
        PresentationRecipe presentationRecipe = Generator.generateRecipe("01");
        DomainRecipe domainRecipe = mapper.mapTo(presentationRecipe);

        when(recipeRepository.saveRecipe(domainRecipe)).thenReturn(Completable.complete());

        LiveData<Boolean> status = homeVM.saveRecipeAsFav(presentationRecipe);
        status.observeForever(Generator.generateMockObserver());

        assertEquals(true, status.getValue());
    }

    @Test
    public void failure_testSaveRecipeAsFavorite() {
        PresentationRecipe presentationRecipe = Generator.generateRecipe("01");
        DomainRecipe domainRecipe = mapper.mapTo(presentationRecipe);

        when(recipeRepository.saveRecipe(domainRecipe)).thenReturn(Completable.error(new Throwable()));

        LiveData<Boolean> status = homeVM.saveRecipeAsFav(presentationRecipe);
        status.observeForever(Generator.generateMockObserver());

        assertEquals(false, status.getValue());
    }

    @Test
    public void success_testDeleteRecipeFromFavorite() {
        PresentationRecipe presentationRecipe = Generator.generateRecipe("01");
        DomainRecipe domainRecipe = mapper.mapTo(presentationRecipe);

        when(recipeRepository.deleteRecipe(domainRecipe)).thenReturn(Completable.complete());

        LiveData<Boolean> status = homeVM.deleteRecipeFromFav(presentationRecipe);
        status.observeForever(Generator.generateMockObserver());

        assertEquals(true, status.getValue());
    }

    @Test
    public void failure_testDeleteRecipeFromFavoriteFAILURE() {
        PresentationRecipe presentationRecipe = Generator.generateRecipe("01");
        DomainRecipe domainRecipe = mapper.mapTo(presentationRecipe);

        when(recipeRepository.deleteRecipe(domainRecipe)).thenReturn(Completable.error(new Throwable()));

        LiveData<Boolean> status = homeVM.deleteRecipeFromFav(presentationRecipe);
        status.observeForever(Generator.generateMockObserver());

        assertEquals(false, status.getValue());
    }
}
