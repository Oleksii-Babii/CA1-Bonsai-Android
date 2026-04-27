package org.tudublin.bonsaiapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button browseSpecies = findViewById(R.id.btnViewSpecies);
        Button myCollection  = findViewById(R.id.btnViewTrees);

        browseSpecies.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, SpeciesListActivity.class)));

        myCollection.setOnClickListener(v -> {
            // TODO: open trees screen once it lands
        });
    }
}