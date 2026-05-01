package org.tudublin.bonsaiapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.tudublin.bonsaiapp.R;
import org.tudublin.bonsaiapp.model.Tree;
import org.tudublin.bonsaiapp.util.ImageUtils;

import java.util.ArrayList;
import java.util.List;

public class TreeAdapter extends RecyclerView.Adapter<TreeAdapter.ViewHolder> {

    public interface OnItemClickListener { void onItemClick(Tree tree); }

    private List<Tree> items = new ArrayList<>();
    private final OnItemClickListener listener;

    public TreeAdapter(OnItemClickListener listener) { this.listener = listener; }

    public void updateData(List<Tree> newItems) {
        items = new ArrayList<>(newItems);
        notifyDataSetChanged();
    }

    @NonNull @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tree, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int position) {
        Tree t = items.get(position);
        h.textNickname.setText(t.getNickname());
        if (t.getSpecies() != null) {
            h.textSpecies.setText(t.getSpecies().getName());
        } else {
            h.textSpecies.setText("");
        }
        h.textAge.setText(t.getAge() + " yrs");
        ImageUtils.loadTreeImage(h.imageTree, t);
        h.itemView.setOnClickListener(v -> listener.onItemClick(t));
    }

    @Override public int getItemCount() { return items.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView textNickname, textSpecies, textAge;
        final ImageView imageTree;
        ViewHolder(View v) {
            super(v);
            textNickname = v.findViewById(R.id.textTreeNickname);
            textSpecies  = v.findViewById(R.id.textTreeSpecies);
            textAge      = v.findViewById(R.id.textTreeAge);
            imageTree    = v.findViewById(R.id.imageTree);
        }
    }
}