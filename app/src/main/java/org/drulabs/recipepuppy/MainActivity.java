package org.drulabs.recipepuppy;

import android.arch.lifecycle.ViewModelProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.drulabs.presentation.viewmodels.HomeVM;

public class MainActivity extends AppCompatActivity {

    TextView tvHola;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvHola = findViewById(R.id.text1);
    }
}
