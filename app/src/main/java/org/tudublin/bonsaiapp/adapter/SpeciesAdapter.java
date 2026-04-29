package org.tudublin.bonsaiapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.tudublin.bonsaiapp.R;
import org.tudublin.bonsaiapp.model.Species;

import java.util.ArrayList;
import java.util.List;

public class SpeciesAdapter extends RecyclerView.Adapter<SpeciesAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Species species);
    }

    private List<Species> items = new ArrayList<>();
    private final OnItemClickListener listener;

    public SpeciesAdapter(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void updateData(List<Species> newItems) {
        items = new ArrayList<>(newItems);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_species, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Species species = items.get(position);
        holder.textName.setText(species.getName());
        holder.textOrigin.setText(species.getOriginCountry());
        holder.textDifficulty.setText(species.getDifficultyLevel());
        holder.itemView.setOnClickListener(v -> listener.onItemClick(species));
    }

    @Override
    public int getItemCount() { return items.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView textName;
        final TextView textOrigin;
        final TextView textDifficulty;
        final ImageView imageSpecies;

        ViewHolder(View view) {
            super(view);
            textName       = view.findViewById(R.id.textSpeciesName);
            textOrigin     = view.findViewById(R.id.textSpeciesOrigin);
            textDifficulty = view.findViewById(R.id.textSpeciesDifficulty);
            imageSpecies   = view.findViewById(R.id.imageSpecies);
        }
    }
}