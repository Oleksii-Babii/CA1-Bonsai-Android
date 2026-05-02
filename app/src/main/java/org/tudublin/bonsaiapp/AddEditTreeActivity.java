package org.tudublin.bonsaiapp;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import org.tudublin.bonsaiapp.api.RetrofitClient;
import org.tudublin.bonsaiapp.model.Species;
import org.tudublin.bonsaiapp.model.Tree;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddEditTreeActivity extends AppCompatActivity {

    private static final String TAG = "BonsaiApp";

    private TextInputEditText editNickname, editAge, editHeight, editNotes;
    private Spinner spinnerSpecies;
    private final List<Species> speciesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_tree);

        editNickname   = findViewById(R.id.editNickname);
        editAge        = findViewById(R.id.editAge);
        editHeight     = findViewById(R.id.editHeight);
        editNotes      = findViewById(R.id.editNotes);
        spinnerSpecies = findViewById(R.id.spinnerSpecies);

        ((MaterialButton) findViewById(R.id.btnSave)).setOnClickListener(v -> saveTree());

        loadSpecies();
    }

    private void loadSpecies() {
        RetrofitClient.getService().getAllSpecies().enqueue(new Callback<List<Species>>() {
            @Override
            public void onResponse(Call<List<Species>> call, Response<List<Species>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    speciesList.clear();
                    speciesList.addAll(response.body());
                    List<String> names = new ArrayList<>();
                    for (Species s : speciesList) names.add(s.getName());
                    spinnerSpecies.setAdapter(new ArrayAdapter<>(AddEditTreeActivity.this,
                            android.R.layout.simple_spinner_dropdown_item, names));
                }
            }

            @Override
            public void onFailure(Call<List<Species>> call, Throwable t) {
                Log.e(TAG, "Species load failed: " + t.getMessage());
            }
        });
    }

    private void saveTree() {
        String nickname = editNickname.getText() == null ? "" : editNickname.getText().toString().trim();
        String ageStr   = editAge.getText() == null ? "" : editAge.getText().toString().trim();
        String hStr     = editHeight.getText() == null ? "" : editHeight.getText().toString().trim();

        if (TextUtils.isEmpty(nickname) || TextUtils.isEmpty(ageStr) || TextUtils.isEmpty(hStr)) {
            Toast.makeText(this, R.string.error_required_fields, Toast.LENGTH_SHORT).show();
            return;
        }

        Tree t = new Tree();
        t.setNickname(nickname);
        t.setAge(Integer.parseInt(ageStr));
        t.setHeight(Double.parseDouble(hStr));
        if (editNotes.getText() != null) t.setNotes(editNotes.getText().toString().trim());

        int pos = spinnerSpecies.getSelectedItemPosition();
        if (pos >= 0 && pos < speciesList.size()) t.setSpeciesId(speciesList.get(pos).getId());

        RetrofitClient.getService().createTree(t).enqueue(new Callback<Tree>() {
            @Override
            public void onResponse(Call<Tree> call, Response<Tree> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(AddEditTreeActivity.this, R.string.msg_tree_saved, Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(AddEditTreeActivity.this, R.string.error_loading, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Tree> call, Throwable t) {
                Toast.makeText(AddEditTreeActivity.this, R.string.error_loading, Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Save failed: " + t.getMessage());
            }
        });
    }
}