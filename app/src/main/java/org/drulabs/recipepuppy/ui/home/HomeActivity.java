package org.drulabs.recipepuppy.ui.home;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import org.drulabs.presentation.factory.HomeVMFactory;
import org.drulabs.presentation.viewmodels.HomeVM;
import org.drulabs.recipepuppy.R;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "HomeActivity";

    @Inject
    HomeVMFactory vmFactory;

    HomeVM homeVM;

    TextView tvHola;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvHola = findViewById(R.id.text1);

        homeVM = ViewModelProviders.of(this, vmFactory).get(HomeVM.class);

        homeVM.getRecipesLiveData().observe(this, listModel -> {
            Log.d(TAG, "onCreate: Data received");
            if (listModel != null) {
                if (!listModel.isLoading() && listModel.getError() == null) {
                    Log.d(TAG, "onCreate: DATA = " + listModel.getData());
                } else if (!listModel.isLoading() && listModel.getError() != null) {
                    Log.d(TAG, "onCreate: DATA = " + listModel.getError());
                } else if (listModel.isLoading()) {
                    Log.d(TAG, "onCreate: LOADING");
                }
            }
        });

        homeVM.searchRecipes("pasta", 2);
    }
}
