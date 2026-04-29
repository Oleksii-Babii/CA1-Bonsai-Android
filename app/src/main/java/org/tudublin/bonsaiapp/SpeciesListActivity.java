package org.tudublin.bonsaiapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.tudublin.bonsaiapp.adapter.SpeciesAdapter;
import org.tudublin.bonsaiapp.api.BonsaiApiService;
import org.tudublin.bonsaiapp.api.RetrofitClient;
import org.tudublin.bonsaiapp.model.Species;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SpeciesListActivity extends AppCompatActivity {

    private static final String TAG = "BonsaiApp";

    private SpeciesAdapter adapter;
    private ProgressBar progressBar;
    private TextView emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_species_list);

        RecyclerView recycler = findViewById(R.id.recyclerSpecies);
        progressBar = findViewById(R.id.progressBar);
        emptyView   = findViewById(R.id.emptyView);

        adapter = new SpeciesAdapter(species -> {
            Intent intent = new Intent(SpeciesListActivity.this, SpeciesDetailActivity.class);
            intent.putExtra(SpeciesDetailActivity.EXTRA_SPECIES_ID, species.getId());
            startActivity(intent);
        });
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setHasFixedSize(true);
        recycler.setAdapter(adapter);

        loadSpecies();
    }

    private void loadSpecies() {
        progressBar.setVisibility(View.VISIBLE);
        BonsaiApiService service = RetrofitClient.getService();
        service.getAllSpecies().enqueue(new Callback<List<Species>>() {
            @Override
            public void onResponse(Call<List<Species>> call, Response<List<Species>> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    List<Species> list = response.body();
                    emptyView.setVisibility(list.isEmpty() ? View.VISIBLE : View.GONE);
                    adapter.updateData(list);
                    Log.d(TAG, "Loaded " + list.size() + " species");
                }
            }

            @Override
            public void onFailure(Call<List<Species>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(SpeciesListActivity.this, R.string.error_loading, Toast.LENGTH_LONG).show();
                Log.e(TAG, "Error loading species: " + t.getMessage());
            }
        });
    }
}