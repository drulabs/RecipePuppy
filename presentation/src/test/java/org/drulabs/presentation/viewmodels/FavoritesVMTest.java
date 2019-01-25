package org.drulabs.presentation.viewmodels;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.lifecycle.LiveData;

import org.drulabs.domain.entities.DomainRecipe;
import org.drulabs.domain.usecases.DeleteAllRecipesTask;
import org.drulabs.domain.usecases.DeleteRecipeTask;
import org.drulabs.domain.usecases.GetLastSavedRecipeTask;
import org.drulabs.domain.usecases.GetSavedRecipesTask;
import org.drulabs.presentation.data.Model;
import org.drulabs.presentation.entities.PresentationRecipe;
import org.drulabs.presentation.mapper.PresentationDomainMapper;
import org.drulabs.presentation.mapper.PresentationMapper;
import org.drulabs.presentation.utils.Generator;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.observers.DisposableSingleObserver;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@RunWith(JUnit4.class)
public class FavoritesVMTest {

    @Rule
    public TestRule rule = new InstantTaskExecutorRule();

    private PresentationMapper<DomainRecipe> mapper = new PresentationDomainMapper();

    @Mock
    private GetSavedRecipesTask getSavedRecipesTask;
    @Mock
    private GetLastSavedRecipeTask getLastSavedRecipeTask;
    @Mock
    private DeleteAllRecipesTask deleteAllRecipesTask;
    @Mock
    private DeleteRecipeTask deleteRecipeTask;

    private FavoritesVM favoritesVM;

    @Captor
    private ArgumentCaptor<DisposableSingleObserver<DomainRecipe>> singleCaptor;

    @Captor
    private ArgumentCaptor<DisposableObserver<DomainRecipe>> observableCaptor;

    @Captor
    private ArgumentCaptor<DisposableCompletableObserver> completableCaptor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        favoritesVM = new FavoritesVM(mapper, getSavedRecipesTask, getLastSavedRecipeTask,
                deleteAllRecipesTask, deleteRecipeTask);
    }

    @Test
    public void testGetSavedRecipes() {
        PresentationRecipe presentationRecipe = Generator.generateRecipe("dsds");
        DomainRecipe domainRecipe = mapper.mapTo(presentationRecipe);

        LiveData<Model<List<PresentationRecipe>>> savedRecipes = favoritesVM.getSavedRecipes();
        savedRecipes.observeForever(Generator.generateMockObserver());

        verify(getSavedRecipesTask).run(observableCaptor.capture(), eq(null));
        observableCaptor.getValue().onNext(domainRecipe);
        observableCaptor.getValue().onComplete();

        assertEquals(savedRecipes.getValue().getData().get(0), presentationRecipe);
    }

    @Test
    public void testGetLastSavedRecipe() {
        PresentationRecipe presentationRecipe = Generator.generateRecipe("dsds");
        DomainRecipe domainRecipe = mapper.mapTo(presentationRecipe);

        LiveData<Model<PresentationRecipe>> lastRecipe = favoritesVM.getLastSavedRecipe();
        lastRecipe.observeForever(Generator.generateMockObserver());

        verify(getLastSavedRecipeTask).run(singleCaptor.capture(), eq(null));
        singleCaptor.getValue().onSuccess(domainRecipe);

        assertEquals(lastRecipe.getValue().getData(), presentationRecipe);
    }

    @Test
    public void testDeleteAllRecipes() {

        LiveData<Boolean> status = favoritesVM.deleteAllFavoriteRecipes();
        status.observeForever(Generator.generateMockObserver());

        verify(deleteAllRecipesTask).run(completableCaptor.capture(), eq(null));
        completableCaptor.getValue().onComplete();

        assertEquals(true, status.getValue());
    }

    @Test
    public void testDeleteAllRecipesFAILURE() {

        LiveData<Boolean> status = favoritesVM.deleteAllFavoriteRecipes();
        status.observeForever(Generator.generateMockObserver());

        verify(deleteAllRecipesTask).run(completableCaptor.capture(), eq(null));
        completableCaptor.getValue().onError(new Exception());

        assertEquals(false, status.getValue());
    }

    @Test
    public void testDeleteRecipeFromFavorite() {
        PresentationRecipe presentationRecipe = Generator.generateRecipe("01");
        DomainRecipe domainRecipe = mapper.mapTo(presentationRecipe);

        LiveData<Boolean> status = favoritesVM.deleteRecipeFromFavorite(presentationRecipe);
        status.observeForever(Generator.generateMockObserver());

        verify(deleteRecipeTask).run(completableCaptor.capture(), eq(domainRecipe));
        completableCaptor.getValue().onComplete();

        assertEquals(true, status.getValue());
    }

    @Test
    public void testDeleteRecipeFromFavoriteFAILURE() {
        PresentationRecipe presentationRecipe = Generator.generateRecipe("01");
        DomainRecipe domainRecipe = mapper.mapTo(presentationRecipe);

        LiveData<Boolean> status = favoritesVM.deleteRecipeFromFavorite(presentationRecipe);
        status.observeForever(Generator.generateMockObserver());

        verify(deleteRecipeTask).run(completableCaptor.capture(), eq(domainRecipe));
        completableCaptor.getValue().onError(new Exception());

        assertEquals(false, status.getValue());
    }

}
