package org.tudublin.bonsaiapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import org.tudublin.bonsaiapp.api.RetrofitClient;
import org.tudublin.bonsaiapp.model.Species;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "BonsaiApp";

    private ImageView imageFeatured;
    private TextView textFeaturedName, textFeaturedOrigin, textFeaturedDifficulty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageFeatured          = findViewById(R.id.imageFeatured);
        textFeaturedName       = findViewById(R.id.textFeaturedName);
        textFeaturedOrigin     = findViewById(R.id.textFeaturedOrigin);
        textFeaturedDifficulty = findViewById(R.id.textFeaturedDifficulty);

        Button browseSpecies = findViewById(R.id.btnViewSpecies);
        Button myCollection  = findViewById(R.id.btnViewTrees);
        browseSpecies.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, SpeciesListActivity.class)));
        myCollection.setOnClickListener(v -> { /* trees screen lands later */ });

        loadFeaturedSpecies();
    }

    private void loadFeaturedSpecies() {
        RetrofitClient.getService().getAllSpecies().enqueue(new Callback<List<Species>>() {
            @Override
            public void onResponse(Call<List<Species>> call, Response<List<Species>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    Species s = response.body().get(0);
                    textFeaturedName.setText(s.getName());
                    textFeaturedOrigin.setText(s.getOriginCountry());
                    textFeaturedDifficulty.setText(s.getDifficultyLevel());
                    if (!TextUtils.isEmpty(s.getImageUrl())) {
                        Glide.with(MainActivity.this).load(s.getImageUrl()).into(imageFeatured);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Species>> call, Throwable t) {
                Log.e(TAG, "Featured species failed: " + t.getMessage());
            }
        });
    }
}