package org.drulabs.recipepuppy.ui.home;


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
import android.widget.Toast;

import org.drulabs.presentation.entities.PresentationRecipe;
import org.drulabs.presentation.factory.HomeVMFactory;
import org.drulabs.presentation.viewmodels.HomeVM;
import org.drulabs.recipepuppy.R;
import org.drulabs.recipepuppy.ui.common.RecipeListAdapter;

import javax.inject.Inject;

import androidx.annotation.Nullable;
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

    private static final String TAG = "HomeActivity";
    private static final String DEFAULT_SEARCH_TERM = "parmesan";
    private static final int DEFAULT_PAGE_NUM = 1;

    @Inject
    HomeVMFactory vmFactory;

    HomeVM homeVM;

    RecyclerView rvRecipeList;
    RecipeListAdapter recipeAdapter;
    ProgressBar progressBar;
    TextView tvSearchText, tvError;

    String searchQuery = DEFAULT_SEARCH_TERM;
    int currentPageNumber = DEFAULT_PAGE_NUM;

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

        search(DEFAULT_SEARCH_TERM, DEFAULT_PAGE_NUM);

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
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_favorite_recipes:
                // TODO open the favorites page
                break;
            case R.id.action_about_recipe_puppy:
                final String recipePuppyURL = "http://www.recipepuppy.com/";
                Intent openUrlIntent = new Intent(Intent.ACTION_VIEW);
                openUrlIntent.setData(Uri.parse(recipePuppyURL));
                startActivity(openUrlIntent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            search(query, DEFAULT_PAGE_NUM);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_previous:
                if (currentPageNumber > DEFAULT_PAGE_NUM) {
                    currentPageNumber--;
                    search(searchQuery, currentPageNumber);
                }
                break;
            case R.id.tv_next:
                currentPageNumber++;
                search(searchQuery, currentPageNumber);
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        searchQuery = query;
        currentPageNumber = DEFAULT_PAGE_NUM;
        search(query, currentPageNumber);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public void onRecipeItemTapped(int index, PresentationRecipe recipe) {
        final String recipePuppyURL = recipe.getDetailsUrl();
        Intent openUrlIntent = new Intent(Intent.ACTION_VIEW);
        openUrlIntent.setData(Uri.parse(recipePuppyURL));
        startActivity(openUrlIntent);
    }

    @Override
    public void onStarTapped(int index, PresentationRecipe recipe) {
        LiveData<Boolean> status;
        if (recipe.isFavorite()) {
            status = homeVM.deleteRecipeFromFav(recipe);
        } else {
            status = homeVM.saveRecipeAsFav(recipe);
        }
        showLoader();
        status.observe(this, operationStatus -> {
            hideLoader();
            if (operationStatus) {
                recipe.setFavorite(!recipe.isFavorite());
                recipeAdapter.updateRecipe(index, recipe);
            } else {
                Toast.makeText(this, getString(R.string.something_went_wrong),
                        Toast.LENGTH_SHORT).show();
            }
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
            tvError.setText(getString(R.string.something_went_wrong));
        } else {
            tvError.setText(getString(R.string.txt_no_data));
        }
        tvError.setVisibility(View.VISIBLE);
    }

    private void search(String text, int page) {
        homeVM.searchRecipes(text, page);
        tvSearchText.setText(String.format(getString(R.string.query_text_format), text, page));
    }
}
