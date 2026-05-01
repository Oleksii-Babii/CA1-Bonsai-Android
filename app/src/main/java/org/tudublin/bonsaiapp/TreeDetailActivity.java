package org.tudublin.bonsaiapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.tudublin.bonsaiapp.api.RetrofitClient;
import org.tudublin.bonsaiapp.model.Tree;
import org.tudublin.bonsaiapp.util.ImageUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TreeDetailActivity extends AppCompatActivity {

    public static final String EXTRA_TREE_ID = "org.tudublin.bonsaiapp.TREE_ID";
    private static final String TAG = "BonsaiApp";

    private TextView textNickname, textSpecies, textAge, textHeight, textLastWatered, textNotes;
    private ImageView imageTree;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tree_detail);

        textNickname    = findViewById(R.id.textNickname);
        textSpecies     = findViewById(R.id.textSpecies);
        textAge         = findViewById(R.id.textAge);
        textHeight      = findViewById(R.id.textHeight);
        textLastWatered = findViewById(R.id.textLastWatered);
        textNotes       = findViewById(R.id.textNotes);
        imageTree       = findViewById(R.id.imageTree);
        progressBar     = findViewById(R.id.progressBar);

        int id = getIntent().getIntExtra(EXTRA_TREE_ID, -1);
        if (id == -1) { finish(); return; }
        loadTree(id);
    }

    private static String formatDate(String iso) {
        if (iso == null || iso.length() < 10) return "";
        return iso.substring(0, 10);
    }

    private void loadTree(int id) {
        progressBar.setVisibility(View.VISIBLE);
        RetrofitClient.getService().getTree(id).enqueue(new Callback<Tree>() {
            @Override
            public void onResponse(Call<Tree> call, Response<Tree> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    Tree t = response.body();
                    textNickname.setText(t.getNickname());
                    if (t.getSpecies() != null) textSpecies.setText(t.getSpecies().getName());
                    textAge.setText(getString(R.string.label_age) + ": " + t.getAge());
                    textHeight.setText(getString(R.string.label_height) + ": " + t.getHeight());
                    textLastWatered.setText(getString(R.string.label_last_watered) + ": " + formatDate(t.getLastWateredDate()));
                    textNotes.setText(t.getNotes());
                    ImageUtils.loadTreeImage(imageTree, t);
                    Log.d(TAG, "Loaded tree: " + t.getNickname());
                }
            }

            @Override
            public void onFailure(Call<Tree> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(TreeDetailActivity.this, R.string.error_loading, Toast.LENGTH_LONG).show();
                Log.e(TAG, "Error loading tree: " + t.getMessage());
            }
        });
    }
}