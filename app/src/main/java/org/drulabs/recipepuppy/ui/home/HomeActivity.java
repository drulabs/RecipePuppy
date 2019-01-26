package org.drulabs.recipepuppy.ui.home;


import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.drulabs.presentation.factory.HomeVMFactory;
import org.drulabs.presentation.viewmodels.HomeVM;
import org.drulabs.recipepuppy.R;
import org.drulabs.recipepuppy.ui.common.RecipeListAdapter;

import javax.inject.Inject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import dagger.android.AndroidInjection;

public class HomeActivity extends AppCompatActivity implements SearchView.OnQueryTextListener,
        View.OnClickListener {

    private static final String TAG = "HomeActivity";
    private static final String DEFAULT_SEARCH_TERM = "pasta";
    private static final int DEFAULT_PAGE_NUM = 1;

    @Inject
    HomeVMFactory vmFactory;

    HomeVM homeVM;

    RecyclerView rvRecipeList;
    RecipeListAdapter recipeAdapter;
    ProgressBar progressBar;
    TextView tvSearchText;

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

        // Navigation handling
        tvSearchText = findViewById(R.id.tv_search_text);
        findViewById(R.id.tv_previous).setOnClickListener(this);
        findViewById(R.id.tv_next).setOnClickListener(this);

        recipeAdapter = new RecipeListAdapter();
        rvRecipeList.setLayoutManager(new LinearLayoutManager(this));
        rvRecipeList.setAdapter(recipeAdapter);

        homeVM = ViewModelProviders.of(this, vmFactory).get(HomeVM.class);

        homeVM.getRecipesLiveData().observe(this, listModel -> {
            if (listModel != null) {
                if (!listModel.isLoading() && listModel.getError() == null) {
                    hideLoader();
                    recipeAdapter.populateRecipes(listModel.getData());
                } else if (!listModel.isLoading() && listModel.getError() != null) {
                    showError(listModel.getError());
                } else if (listModel.isLoading()) {
                    showLoader();
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

    private void showLoader() {
        rvRecipeList.setAlpha(0.3f);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideLoader() {
        rvRecipeList.setAlpha(1.0f);
        progressBar.setVisibility(View.GONE);
    }

    private void showError(Throwable throwable) {
        hideLoader();
        throwable.printStackTrace();
    }

    private void search(String text, int page) {
        homeVM.searchRecipes(text, page);
        tvSearchText.setText(String.format(getString(R.string.query_text_format), text, page));
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        searchQuery = query;
        currentPageNumber = DEFAULT_PAGE_NUM;
        search(query, currentPageNumber);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        //System.out.println("ignore search query change");
        return false;
    }
}
