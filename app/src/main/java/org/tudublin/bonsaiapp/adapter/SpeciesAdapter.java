package org.tudublin.bonsaiapp.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.tudublin.bonsaiapp.R;
import org.tudublin.bonsaiapp.model.Species;

import java.util.ArrayList;
import java.util.List;

public class SpeciesAdapter extends RecyclerView.Adapter<SpeciesAdapter.ViewHolder> {

    public interface OnItemClickListener { void onItemClick(Species species); }

    private List<Species> items = new ArrayList<>();
    private final OnItemClickListener listener;

    public SpeciesAdapter(OnItemClickListener listener) { this.listener = listener; }

    public void updateData(List<Species> newItems) {
        items = new ArrayList<>(newItems);
        notifyDataSetChanged();
    }

    @NonNull @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_species, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int position) {
        Species s = items.get(position);
        h.textName.setText(s.getName());
        h.textOrigin.setText(s.getOriginCountry());
        h.textDifficulty.setText(s.getDifficultyLevel());
        if (!TextUtils.isEmpty(s.getImageUrl())) {
            Glide.with(h.imageSpecies.getContext())
                    .load(s.getImageUrl())
                    .placeholder(R.drawable.ic_tree_placeholder)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(h.imageSpecies);
        } else {
            h.imageSpecies.setImageResource(R.drawable.ic_tree_placeholder);
        }
        h.itemView.setOnClickListener(v -> listener.onItemClick(s));
    }

    @Override public int getItemCount() { return items.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView textName, textOrigin, textDifficulty;
        final ImageView imageSpecies;
        ViewHolder(View v) {
            super(v);
            textName       = v.findViewById(R.id.textSpeciesName);
            textOrigin     = v.findViewById(R.id.textSpeciesOrigin);
            textDifficulty = v.findViewById(R.id.textSpeciesDifficulty);
            imageSpecies   = v.findViewById(R.id.imageSpecies);
        }
    }
}