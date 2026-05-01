package org.tudublin.bonsaiapp;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import org.tudublin.bonsaiapp.api.RetrofitClient;
import org.tudublin.bonsaiapp.model.Tree;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddEditTreeActivity extends AppCompatActivity {

    private static final String TAG = "BonsaiApp";

    private TextInputEditText editNickname, editAge, editHeight, editNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_tree);

        editNickname = findViewById(R.id.editNickname);
        editAge      = findViewById(R.id.editAge);
        editHeight   = findViewById(R.id.editHeight);
        editNotes    = findViewById(R.id.editNotes);

        MaterialButton btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(v -> saveTree());
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