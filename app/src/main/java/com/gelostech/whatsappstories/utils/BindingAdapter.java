package com.gelostech.whatsappstories.utils;

import android.media.Image;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;

import de.hdodenhof.circleimageview.CircleImageView;

public class BindingAdapter {

    @android.databinding.BindingAdapter({"imagePath"})
    public static void loadImage(ImageView view, String path) {
        Glide.with(view.getContext())
                .load(path)
                .thumbnail(0.05f)
                .into(view);
    }


}
