package com.example.onlinecourseande_learningapp;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ImageLoaderUtil {

    public static void loadImageFromFirebaseStorage(Context context, String firebaseStorageUrl, ImageView imageView) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl(firebaseStorageUrl);

        storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
            if (context instanceof Activity && !((Activity) context).isDestroyed()) {
                Glide.with(context)
                        .load(uri)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.baseline_image_24)
                        .into(imageView);
            }
        }).addOnFailureListener(exception -> {
            Log.e("ImageLoaderUtil", "Error loading image: ", exception);
            imageView.setImageResource(R.drawable.head_icon);
        });
    }
}
