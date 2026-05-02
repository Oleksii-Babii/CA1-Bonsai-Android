package org.tudublin.bonsaiapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
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
    private int treeId = -1;

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

        treeId = getIntent().getIntExtra(EXTRA_TREE_ID, -1);
        if (treeId == -1) { finish(); return; }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadTree(treeId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_tree_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_edit) {
            Intent i = new Intent(this, AddEditTreeActivity.class);
            i.putExtra(AddEditTreeActivity.EXTRA_TREE_ID, treeId);
            startActivity(i);
            return true;
        } else if (id == R.id.action_delete) {
            confirmDelete();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void confirmDelete() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.dialog_delete_title)
                .setMessage(R.string.dialog_delete_message)
                .setPositiveButton(R.string.btn_delete, (d, w) -> deleteTree())
                .setNegativeButton(R.string.btn_cancel, null)
                .show();
    }

    private void deleteTree() {
        RetrofitClient.getService().deleteTree(treeId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(TreeDetailActivity.this, R.string.msg_tree_deleted, Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(TreeDetailActivity.this, R.string.error_loading, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(TreeDetailActivity.this, R.string.error_loading, Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Delete failed: " + t.getMessage());
            }
        });
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