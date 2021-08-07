package com.itheamc.hamroclassroom_student.adapters;

import static com.itheamc.hamroclassroom_student.models.Submission.submissionItemCallback;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.itheamc.hamroclassroom_student.callbacks.SubmissionCallbacks;
import com.itheamc.hamroclassroom_student.databinding.SubmissionViewBinding;
import com.itheamc.hamroclassroom_student.models.Submission;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;

public class SubmissionAdapter extends ListAdapter<Submission, SubmissionAdapter.SubmissionViewHolder> {
    private static final String TAG = "SubmissionAdapter";
    private final SubmissionCallbacks callbacks;

    public SubmissionAdapter(@NonNull @NotNull SubmissionCallbacks submissionCallbacks) {
        super(submissionItemCallback);
        this.callbacks = submissionCallbacks;
    }

    @NonNull
    @NotNull
    @Override
    public SubmissionViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        SubmissionViewBinding viewBinding = SubmissionViewBinding.inflate(inflater, parent, false);
        return new SubmissionViewHolder(viewBinding, callbacks);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull SubmissionViewHolder holder, int position) {
        Submission submission = getItem(position);
        holder.viewBinding.setSubmission(submission);
        String formattedDate = DateFormat.getDateInstance().format(submission.get_submitted_date());
        holder.viewBinding.setDate(formattedDate);
    }

    protected static class SubmissionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final SubmissionViewBinding viewBinding;
        private final SubmissionCallbacks callbacks;

        public SubmissionViewHolder(@NonNull @NotNull SubmissionViewBinding submissionViewBinding, SubmissionCallbacks submissionCallbacks) {
            super(submissionViewBinding.getRoot());
            this.viewBinding = submissionViewBinding;
            this.callbacks = submissionCallbacks;

            this.viewBinding.submissionCardView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            callbacks.onClick(getAdapterPosition());
        }
    }

}
