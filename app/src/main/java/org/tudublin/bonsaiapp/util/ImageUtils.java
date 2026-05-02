package org.tudublin.bonsaiapp.util;

import android.text.TextUtils;
import android.util.Base64;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.tudublin.bonsaiapp.R;
import org.tudublin.bonsaiapp.model.Tree;

public final class ImageUtils {

    private ImageUtils() {}

    /**
     * Loads the best-available image for a tree. The fallback chain is:
     *   imageData (base64) -> imageUrl -> species.imageUrl -> placeholder.
     */
    public static void loadTreeImage(ImageView target, Tree tree) {
        if (tree == null) {
            target.setImageResource(R.drawable.ic_tree_placeholder);
            return;
        }

        if (!TextUtils.isEmpty(tree.getImageData())) {
            try {
                byte[] bytes = Base64.decode(tree.getImageData(), Base64.DEFAULT);
                Glide.with(target.getContext())
                        .asBitmap()
                        .load(bytes)
                        .placeholder(R.drawable.ic_tree_placeholder)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into(target);
                return;
            } catch (IllegalArgumentException ignored) { /* fallthrough */ }
        }

        if (!TextUtils.isEmpty(tree.getImageUrl())) {
            Glide.with(target.getContext())
                    .load(tree.getImageUrl())
                    .placeholder(R.drawable.ic_tree_placeholder)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(target);
            return;
        }

        if (tree.getSpecies() != null && !TextUtils.isEmpty(tree.getSpecies().getImageUrl())) {
            Glide.with(target.getContext())
                    .load(tree.getSpecies().getImageUrl())
                    .placeholder(R.drawable.ic_tree_placeholder)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(target);
            return;
        }

        target.setImageResource(R.drawable.ic_tree_placeholder);
    }
}