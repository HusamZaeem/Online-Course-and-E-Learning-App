package com.example.onlinecourseande_learningapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onlinecourseande_learningapp.databinding.ItemReviewBinding;
import com.example.onlinecourseande_learningapp.room_database.AppViewModel;
import com.example.onlinecourseande_learningapp.room_database.entities.Review;

import java.util.List;

public class ReviewsAdapter extends ListAdapter<Review, ReviewsAdapter.ReviewViewHolder> {

    private final AppViewModel appViewModel;
    private final LifecycleOwner lifecycleOwner;

    private final Handler handler = new Handler();


    private final Runnable updateTimestamps = new Runnable() {
        @Override
        public void run() {
            notifyDataSetChanged();
            handler.postDelayed(this, 60000);
        }
    };

    public ReviewsAdapter(AppViewModel appViewModel, LifecycleOwner lifecycleOwner) {
        super(new DiffUtil.ItemCallback<Review>() {
            @Override
            public boolean areItemsTheSame(@NonNull Review oldItem, @NonNull Review newItem) {
                return oldItem.getReview_id().equals(newItem.getReview_id());
            }

            @SuppressLint("DiffUtilEquals")
            @Override
            public boolean areContentsTheSame(@NonNull Review oldItem, @NonNull Review newItem) {
                return oldItem.equals(newItem);
            }
        });
        this.appViewModel = appViewModel;
        this.lifecycleOwner = lifecycleOwner;  // Initialize lifecycleOwner
    }

    @Override
    public void submitList(@Nullable List<Review> list) {
        super.submitList(list);
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemReviewBinding binding = ItemReviewBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ReviewViewHolder(binding, appViewModel, lifecycleOwner);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        Review review = getItem(position);
        if (review != null) {
            Log.d("ReviewsAdapter", "Review details: " + review.getComment());
        }
        holder.bind(review);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        handler.post(updateTimestamps);
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        handler.removeCallbacks(updateTimestamps); // Stop updates when detached
    }

    static class ReviewViewHolder extends RecyclerView.ViewHolder {
        private final ItemReviewBinding binding;
        private final AppViewModel appViewModel;
        private final LifecycleOwner lifecycleOwner;

        public ReviewViewHolder(ItemReviewBinding binding, AppViewModel appViewModel, LifecycleOwner lifecycleOwner) {
            super(binding.getRoot());
            this.binding = binding;
            this.appViewModel = appViewModel;
            this.lifecycleOwner = lifecycleOwner;
        }

        public void bind(Review review) {
            binding.reviewComment.setText(review.getComment());
            binding.tvReviewRating.setText(String.valueOf(review.getRate()));


            TimeUtils.getCurrentTimeLive().observe(lifecycleOwner, currentTime -> {
                String relativeTime = TimeUtils.getRelativeTime(review.getTimestamp().getTime(), System.currentTimeMillis());
                binding.reviewTimestamp.setText(relativeTime);
            });


            appViewModel.getStudentByIdLive(review.getStudent_id()).observe(lifecycleOwner, student -> {
                if (student != null) {
                    binding.tvStudentFullName.setText(student.getFirst_name() + " " + student.getLast_name());
                    ImageLoaderUtil.loadImageFromFirebaseStorage(binding.getRoot().getContext(),
                            student.getProfile_photo(), binding.ivReviewStudentPhoto);
                }
            });


            String loggedInStudentId = getLoggedInStudentId(binding.getRoot().getContext());

            // Show delete icon if the review belongs to the logged-in student
            if (review.getStudent_id().equals(loggedInStudentId)) {
                binding.ivDeleteReview.setVisibility(View.VISIBLE);

                binding.ivDeleteReview.setOnClickListener(v -> {
                    new AlertDialog.Builder(binding.getRoot().getContext())
                            .setTitle("Delete Review")
                            .setMessage("Are you sure you want to delete this review?")
                            .setPositiveButton("Delete", (dialog, which) -> {
                                appViewModel.deleteReview(review);
                            })
                            .setNegativeButton("Cancel", null)
                            .show();
                });
            } else {
                binding.ivDeleteReview.setVisibility(View.GONE);
            }
        }

        private String getLoggedInStudentId(Context context) {
            SharedPreferences sharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
            return sharedPreferences.getString("student_id", null);
        }
    }
}
