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

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.tudublin.bonsaiapp.adapter.TreeAdapter;
import org.tudublin.bonsaiapp.api.RetrofitClient;
import org.tudublin.bonsaiapp.model.Tree;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TreeListActivity extends AppCompatActivity {

    private static final String TAG = "BonsaiApp";

    private TreeAdapter adapter;
    private ProgressBar progressBar;
    private TextView emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tree_list);

        RecyclerView recycler = findViewById(R.id.recyclerTrees);
        progressBar = findViewById(R.id.progressBar);
        emptyView   = findViewById(R.id.emptyView);

        adapter = new TreeAdapter(t -> {
            Intent i = new Intent(TreeListActivity.this, TreeDetailActivity.class);
            i.putExtra(TreeDetailActivity.EXTRA_TREE_ID, t.getId());
            startActivity(i);
        });
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setHasFixedSize(true);
        recycler.setAdapter(adapter);

        FloatingActionButton fab = findViewById(R.id.fabAdd);
        fab.setOnClickListener(v ->
                startActivity(new Intent(TreeListActivity.this, AddEditTreeActivity.class)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadTrees();
    }

    private void loadTrees() {
        progressBar.setVisibility(View.VISIBLE);
        RetrofitClient.getService().getAllTrees().enqueue(new Callback<List<Tree>>() {
            @Override
            public void onResponse(Call<List<Tree>> call, Response<List<Tree>> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    List<Tree> list = response.body();
                    emptyView.setVisibility(list.isEmpty() ? View.VISIBLE : View.GONE);
                    adapter.updateData(list);
                    Log.d(TAG, "Loaded " + list.size() + " trees");
                }
            }

            @Override
            public void onFailure(Call<List<Tree>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(TreeListActivity.this, R.string.error_loading, Toast.LENGTH_LONG).show();
                Log.e(TAG, "Error loading trees: " + t.getMessage());
            }
        });
    }
}