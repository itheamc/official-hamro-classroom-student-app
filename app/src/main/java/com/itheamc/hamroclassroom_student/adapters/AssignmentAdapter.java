package com.itheamc.hamroclassroom_student.adapters;

import static com.itheamc.hamroclassroom_student.models.Assignment.assignmentItemCallback;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.itheamc.hamroclassroom_student.callbacks.AssignmentCallbacks;
import com.itheamc.hamroclassroom_student.databinding.AssignmentViewBinding;
import com.itheamc.hamroclassroom_student.models.Assignment;
import com.itheamc.hamroclassroom_student.utils.NotifyUtils;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;


public class AssignmentAdapter extends ListAdapter<Assignment, AssignmentAdapter.AssignmentViewHolder> {
    private static final String TAG = "AssignmentAdapter";
    private final AssignmentCallbacks callbacks;

    public AssignmentAdapter(@NonNull AssignmentCallbacks callbacks) {
        super(assignmentItemCallback);
        this.callbacks = callbacks;
    }

    @NonNull
    @NotNull
    @Override
    public AssignmentViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        AssignmentViewBinding viewBinding = AssignmentViewBinding.inflate(inflater, parent, false);
        return new AssignmentViewHolder(viewBinding, callbacks);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull AssignmentViewHolder holder, int position) {
        Assignment assignment = getItem(position);
        holder.viewBinding.setAssignment(assignment);
        holder.viewBinding.setNumber(String.valueOf(position + 1));
        String formattedDate = DateFormat.getDateInstance().format(assignment.get_assigned_date());
        holder.viewBinding.setDate(formattedDate);
    }

    protected static class AssignmentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private final AssignmentViewBinding viewBinding;
        private final AssignmentCallbacks callbacks;

        public AssignmentViewHolder(@NonNull @NotNull AssignmentViewBinding assignmentViewBinding, AssignmentCallbacks callbacks) {
            super(assignmentViewBinding.getRoot());
            this.callbacks = callbacks;
            this.viewBinding = assignmentViewBinding;
            this.viewBinding.submissionsButton.setOnClickListener(this);
            this.viewBinding.assignmentCardView.setOnClickListener(this);
            this.viewBinding.assignmentCardView.setOnLongClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int _id = v.getId();
            if (_id == viewBinding.assignmentCardView.getId()) callbacks.onClick(getAdapterPosition());
            else if (_id == viewBinding.submissionsButton.getId()) callbacks.onSubmissionsClick(getAdapterPosition());
            else NotifyUtils.logDebug(TAG, "Unspecified view is clicked!!");
        }

        @Override
        public boolean onLongClick(View v) {
            if (v.getId() == viewBinding.assignmentCardView.getId()) callbacks.onLongClick(getAdapterPosition());
            return true;
        }
    }
}
