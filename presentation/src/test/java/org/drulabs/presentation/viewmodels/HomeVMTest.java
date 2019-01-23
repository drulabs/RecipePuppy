package org.drulabs.presentation.viewmodels;

import android.arch.core.executor.testing.InstantTaskExecutorRule;

import org.drulabs.domain.entities.DomainRecipe;
import org.drulabs.domain.entities.RecipeRequest;
import org.drulabs.domain.usecases.DeleteRecipeTask;
import org.drulabs.domain.usecases.GetRecipesTask;
import org.drulabs.domain.usecases.SaveRecipeTask;
import org.drulabs.presentation.entities.PresentationRecipe;
import org.drulabs.presentation.mapper.PresentationMapper;
import org.drulabs.presentation.mapper.PresentationDomainMapper;
import org.drulabs.presentation.utils.Generator;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.observers.DisposableSingleObserver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@RunWith(JUnit4.class)
public class HomeVMTest {
    @Rule
    public InstantTaskExecutorRule rule = new InstantTaskExecutorRule();

    private PresentationMapper<DomainRecipe> mapper = new PresentationDomainMapper();

    @Mock
    private GetRecipesTask getRecipesTask;
    @Mock
    private SaveRecipeTask saveRecipeTask;
    @Mock
    private DeleteRecipeTask deleteRecipeTask;

    private HomeVM homeVM;

    @Captor
    private ArgumentCaptor<DisposableSingleObserver<List<DomainRecipe>>> singleCaptor;

    @Captor
    private ArgumentCaptor<DisposableCompletableObserver> completableCaptor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        homeVM = new HomeVM(mapper, getRecipesTask, saveRecipeTask, deleteRecipeTask);
    }

    @Test
    public void testSearchRecipes() {
        PresentationRecipe presentationRecipe = Generator.generateRecipe("01");
        List<PresentationRecipe> pr = new ArrayList<>();
        pr.add(presentationRecipe);
        DomainRecipe domainRecipe = mapper.mapTo(presentationRecipe);
        List<DomainRecipe> dr = new ArrayList<>();
        dr.add(domainRecipe);

        RecipeRequest request = new RecipeRequest("test", 1);

        homeVM.searchRecipes(request.getSearchQuery(), request.getPageNum());

        verify(getRecipesTask).run(singleCaptor.capture(), eq(request));
        singleCaptor.getValue().onSuccess(dr);

        assertEquals(homeVM.getRecipesLiveData().getValue().getData(), pr);
    }

    @Test
    public void testSaveRecipeAsFavorite() {
        PresentationRecipe presentationRecipe = Generator.generateRecipe("01");
        DomainRecipe domainRecipe = mapper.mapTo(presentationRecipe);

        homeVM.saveRecipeAsFavorite(presentationRecipe);

        verify(saveRecipeTask).run(completableCaptor.capture(), eq(domainRecipe));
        completableCaptor.getValue().onComplete();

        assertEquals(homeVM.getSaveRecipeStatus().getValue(), true);
    }

    @Test
    public void testSaveRecipeAsFavoriteFAILURE() {
        PresentationRecipe presentationRecipe = Generator.generateRecipe("01");
        DomainRecipe domainRecipe = mapper.mapTo(presentationRecipe);

        homeVM.saveRecipeAsFavorite(presentationRecipe);

        verify(saveRecipeTask).run(completableCaptor.capture(), eq(domainRecipe));
        completableCaptor.getValue().onError(new Throwable());

        assertEquals(false, homeVM.getSaveRecipeStatus().getValue());
    }

    @Test
    public void testDeleteRecipeFromFavorite() {
        PresentationRecipe presentationRecipe = Generator.generateRecipe("01");
        DomainRecipe domainRecipe = mapper.mapTo(presentationRecipe);

        homeVM.deleteRecipeFromFavorite(presentationRecipe);

        verify(deleteRecipeTask).run(completableCaptor.capture(), eq(domainRecipe));
        completableCaptor.getValue().onComplete();

        assertEquals(true, homeVM.getDeleteRecipeStatus().getValue());
    }

    @Test
    public void testDeleteRecipeFromFavoriteFAILURE() {
        PresentationRecipe presentationRecipe = Generator.generateRecipe("01");
        DomainRecipe domainRecipe = mapper.mapTo(presentationRecipe);

        homeVM.deleteRecipeFromFavorite(presentationRecipe);

        verify(deleteRecipeTask).run(completableCaptor.capture(), eq(domainRecipe));
        completableCaptor.getValue().onError(new Throwable());

        assertEquals(false, homeVM.getDeleteRecipeStatus().getValue());
    }
}
