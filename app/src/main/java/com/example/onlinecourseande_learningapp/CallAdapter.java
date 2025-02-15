package com.example.onlinecourseande_learningapp;

import android.content.Context;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onlinecourseande_learningapp.room_database.entities.Call;

import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CallAdapter extends RecyclerView.Adapter<CallAdapter.CallViewHolder> {
    private List<Call> callList;

    public CallAdapter(List<Call> calls) {
        this.callList = calls;
    }

    public void updateCalls(List<Call> calls) {
        this.callList = calls;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CallViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_call_status, parent, false);
        return new CallViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CallViewHolder holder, int position) {
        Call call = callList.get(position);
        // Assume you have a helper to format dates
        holder.tvCallDate.setText(formatDate(call.getTimestamp()));
        // Set user name and photo (similar to ChatAdapter)
        holder.tvUserName.setText(getUserName(call));
        // Customize call status icon/text based on call status
        updateCallStatusUI(holder, call);

        // When the call icon is tapped, start a call (voice/video)
        holder.ivCallIcon.setOnClickListener(v -> {
            startCall(v.getContext(), call);
        });
    }

    @Override
    public int getItemCount() {
        return callList.size();
    }

    static class CallViewHolder extends RecyclerView.ViewHolder {
        ImageView ivUserPhoto, ivCallIcon;
        TextView tvUserName, tvCallDate, tvCallStatus;

        public CallViewHolder(@NonNull View itemView) {
            super(itemView);
            ivUserPhoto = itemView.findViewById(R.id.iv_item_chat_student_photo); // reuse the image view id from your layout
            tvUserName = itemView.findViewById(R.id.tv_item_chat_user_name);
            tvCallDate = itemView.findViewById(R.id.item_call_date); // add in your XML if needed
            tvCallStatus = itemView.findViewById(R.id.item_call_received_status); // for text like "Missed", "Incoming", etc.
            ivCallIcon = itemView.findViewById(R.id.item_call_icon); // your call icon view
        }
    }


    private String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
        return sdf.format(date);
    }

    // Helper to get user name from Call data
    private String getUserName(Call call) {
        // Determine if the current user is caller or receiver and return the other party's name.
        return "Other User Name"; // Replace with actual lookup logic.
    }

    // Update UI based on call status
    private void updateCallStatusUI(CallViewHolder holder, Call call) {
        // For example, if callStatus is "Missed", "Incoming", or "Outgoing"
        String status = call.getCallStatus(); // you might store "Incoming", "Outgoing", "Missed" here
        holder.tvCallStatus.setText(status);
        // Change the icon accordingly (you may have separate drawable resources)
        if ("Missed".equalsIgnoreCase(status)) {
            holder.ivCallIcon.setImageResource(R.drawable.baseline_call_missed_24);
        } else if ("Incoming".equalsIgnoreCase(status)) {
            holder.ivCallIcon.setImageResource(R.drawable.baseline_call_received_24);
        } else {
            holder.ivCallIcon.setImageResource(R.drawable.baseline_call_made_24);
        }
    }

    // Launch the appropriate call based on call type (Voice or Video)
    private void startCall(Context context, Call call) {
        Intent intent;
        if ("Video".equalsIgnoreCase(call.getCallType())) {
            intent = new Intent(context, VideoCallActivity.class);
        } else {
            intent = new Intent(context, VoiceCallActivity.class);
        }
        intent.putExtra("call_id", call.getCall_id());
        context.startActivity(intent);
    }
}
