package org.tudublin.bonsaiapp.util;

import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import org.tudublin.bonsaiapp.R;
import org.tudublin.bonsaiapp.model.Tree;

public final class ImageUtils {

    private ImageUtils() {}

    public static void loadTreeImage(ImageView target, Tree tree) {
        if (tree != null && !TextUtils.isEmpty(tree.getImageUrl())) {
            Glide.with(target.getContext())
                    .load(tree.getImageUrl())
                    .placeholder(R.drawable.ic_tree_placeholder)
                    .into(target);
        } else {
            target.setImageResource(R.drawable.ic_tree_placeholder);
        }
    }
}