package org.drulabs.bakencook.ui.home;


import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.drulabs.bakencook.BuildConfig;
import org.drulabs.bakencook.R;
import org.drulabs.bakencook.ui.common.RecipeListAdapter;
import org.drulabs.bakencook.ui.favorites.FavoritesActivity;
import org.drulabs.presentation.entities.PresentationRecipe;
import org.drulabs.presentation.factory.HomeVMFactory;
import org.drulabs.presentation.viewmodels.HomeVM;

import java.util.Objects;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import dagger.android.AndroidInjection;

public class HomeActivity extends AppCompatActivity implements SearchView.OnQueryTextListener,
        View.OnClickListener, RecipeListAdapter.ItemClickListener {

    @VisibleForTesting
    @Inject
    HomeVMFactory vmFactory;

    HomeVM homeVM;

    RecyclerView rvRecipeList;
    RecipeListAdapter recipeAdapter;
    ProgressBar progressBar;
    TextView tvSearchText, tvError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar_home);
        setSupportActionBar(toolbar);
        rvRecipeList = findViewById(R.id.rvRecipeList);
        progressBar = findViewById(R.id.progress_indicator_home);
        tvError = findViewById(R.id.tv_error);

        // Navigation handling
        tvSearchText = findViewById(R.id.tv_search_text);
        findViewById(R.id.tv_previous).setOnClickListener(this);
        findViewById(R.id.tv_next).setOnClickListener(this);

        recipeAdapter = new RecipeListAdapter(this);
        rvRecipeList.setLayoutManager(new LinearLayoutManager(this));
        rvRecipeList.setAdapter(recipeAdapter);

        homeVM = ViewModelProviders.of(this, vmFactory).get(HomeVM.class);

        homeVM.getRecipesLiveData().observe(this, listModel -> {
            if (listModel != null) {
                if (listModel.isLoading()) {
                    showLoader();
                } else if (listModel.getError() != null) {
                    showError(listModel.getError());
                } else {
                    hideLoader();
                    if (listModel.getData() == null || listModel.getData().size() == 0) {
                        showError(null);
                    } else {
                        recipeAdapter.populateRecipes(listModel.getData());
                    }
                }
            }
        });

        homeVM.getRecipeRequestLiveData().observe(this,
                request -> tvSearchText.setText(
                        String.format(getString(R.string.query_text_format),
                                request.getSearchQuery(),
                                request.getPageNum())));

        homeVM.reload();

        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(Objects.requireNonNull(searchManager)
                .getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_favorite_recipes:
                Intent favoritesIntent = new Intent(this, FavoritesActivity.class);
                favoritesIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(favoritesIntent);
                break;
            case R.id.action_about_recipe_puppy:
                Intent openUrlIntent = new Intent(Intent.ACTION_VIEW);
                openUrlIntent.setData(Uri.parse(BuildConfig.API_HOME));
                startActivity(openUrlIntent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            search(query);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_previous:
                homeVM.navigateToPreviousPage();
                break;
            case R.id.tv_next:
                homeVM.navigateToNextPage();
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        search(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public void onRecipeItemTapped(PresentationRecipe recipe) {
        final String recipePuppyURL = recipe.getDetailsUrl();
        Intent openUrlIntent = new Intent(Intent.ACTION_VIEW);
        openUrlIntent.setData(Uri.parse(recipePuppyURL));
        startActivity(openUrlIntent);
    }

    @Override
    public void onStarTapped(PresentationRecipe recipe) {
        LiveData<Boolean> status;
        if (recipe.isFavorite()) {
            status = homeVM.deleteRecipeFromFav(recipe);
        } else {
            status = homeVM.saveRecipeAsFav(recipe);
        }
        showLoader();
        status.observe(this, operationStatus -> {
            hideLoader();
            status.removeObservers(HomeActivity.this);
            homeVM.reload();
        });
    }

    private void showLoader() {
        rvRecipeList.setAlpha(0.3f);
        progressBar.setVisibility(View.VISIBLE);
        tvError.setVisibility(View.GONE);
    }

    private void hideLoader() {
        rvRecipeList.setAlpha(1.0f);
        progressBar.setVisibility(View.GONE);
        tvError.setVisibility(View.GONE);
    }

    private void showError(@Nullable Throwable throwable) {
        rvRecipeList.setAlpha(0.3f);
        progressBar.setVisibility(View.GONE);
        if (throwable != null) {
            tvError.setText(String.format("%s - %s", getString(R.string.something_went_wrong),
                    throwable.getMessage()));
        } else {
            tvError.setText(getString(R.string.txt_no_data));
        }
        tvError.setVisibility(View.VISIBLE);
    }

    private void search(String text) {
        homeVM.searchRecipes(text);
    }
}
