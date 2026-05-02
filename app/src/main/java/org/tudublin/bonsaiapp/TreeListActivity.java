package org.tudublin.bonsaiapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import org.tudublin.bonsaiapp.adapter.TreeAdapter;
import org.tudublin.bonsaiapp.api.RetrofitClient;
import org.tudublin.bonsaiapp.model.Tree;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TreeListActivity extends AppCompatActivity {

    private static final String TAG = "BonsaiApp";
    private static final long SEARCH_DEBOUNCE_MS = 300L;

    private TreeAdapter adapter;
    private ProgressBar progressBar;
    private TextView emptyView;
    private TextInputEditText editSearch;
    private final Handler debounce = new Handler(Looper.getMainLooper());
    private Runnable pending;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tree_list);

        RecyclerView recycler = findViewById(R.id.recyclerTrees);
        progressBar = findViewById(R.id.progressBar);
        emptyView   = findViewById(R.id.emptyView);
        editSearch  = findViewById(R.id.editSearch);

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

        editSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (pending != null) debounce.removeCallbacks(pending);
                final String q = s == null ? "" : s.toString().trim();
                pending = () -> runQuery(q);
                debounce.postDelayed(pending, SEARCH_DEBOUNCE_MS);
            }
            @Override public void afterTextChanged(Editable s) {}
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        runQuery(editSearch.getText() == null ? "" : editSearch.getText().toString().trim());
    }

    private void runQuery(String query) {
        progressBar.setVisibility(View.VISIBLE);
        Callback<List<Tree>> cb = new Callback<List<Tree>>() {
            @Override
            public void onResponse(Call<List<Tree>> call, Response<List<Tree>> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    List<Tree> list = response.body();
                    emptyView.setVisibility(list.isEmpty() ? View.VISIBLE : View.GONE);
                    adapter.updateData(list);
                    Log.d(TAG, "Trees loaded: " + list.size());
                }
            }

            @Override
            public void onFailure(Call<List<Tree>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(TreeListActivity.this, R.string.error_loading, Toast.LENGTH_LONG).show();
                Log.e(TAG, "Trees load failed: " + t.getMessage());
            }
        };

        if (TextUtils.isEmpty(query)) {
            RetrofitClient.getService().getAllTrees().enqueue(cb);
        } else {
            RetrofitClient.getService().searchTrees(query).enqueue(cb);
        }
    }
}