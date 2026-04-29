package org.tudublin.bonsaiapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.tudublin.bonsaiapp.api.BonsaiApiService;
import org.tudublin.bonsaiapp.api.RetrofitClient;
import org.tudublin.bonsaiapp.model.Species;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SpeciesDetailActivity extends AppCompatActivity {

    public static final String EXTRA_SPECIES_ID = "org.tudublin.bonsaiapp.SPECIES_ID";
    private static final String TAG = "BonsaiApp";

    private TextView textName, textOrigin, textDifficulty, textDescription;
    private ImageView imageSpecies;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_species_detail);

        textName        = findViewById(R.id.textName);
        textOrigin      = findViewById(R.id.textOrigin);
        textDifficulty  = findViewById(R.id.textDifficulty);
        textDescription = findViewById(R.id.textDescription);
        imageSpecies    = findViewById(R.id.imageSpecies);
        progressBar     = findViewById(R.id.progressBar);

        int id = getIntent().getIntExtra(EXTRA_SPECIES_ID, -1);
        if (id == -1) { finish(); return; }
        loadSpeciesDetail(id);
    }

    private void loadSpeciesDetail(int id) {
        progressBar.setVisibility(View.VISIBLE);
        BonsaiApiService service = RetrofitClient.getService();
        service.getSpecies(id).enqueue(new Callback<Species>() {
            @Override
            public void onResponse(Call<Species> call, Response<Species> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    Species s = response.body();
                    textName.setText(s.getName());
                    textOrigin.setText(getString(R.string.label_origin) + " " + s.getOriginCountry());
                    textDifficulty.setText(s.getDifficultyLevel());
                    textDescription.setText(s.getDescription());
                    Log.d(TAG, "Loaded species detail: " + s.getName());
                }
            }

            @Override
            public void onFailure(Call<Species> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(SpeciesDetailActivity.this, R.string.error_loading, Toast.LENGTH_LONG).show();
                Log.e(TAG, "Error loading species detail: " + t.getMessage());
            }
        });
    }
}