package com.itheamc.hamroclassroom_student.adapters;

import static com.itheamc.hamroclassroom_student.models.Subject.subjectItemCallback;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.itheamc.hamroclassroom_student.callbacks.SubjectCallbacks;
import com.itheamc.hamroclassroom_student.utils.NotifyUtils;
import com.itheamc.hamroclassroom_student.databinding.SubjectViewBinding;
import com.itheamc.hamroclassroom_student.models.Subject;

import org.jetbrains.annotations.NotNull;

public class SubjectAdapter extends ListAdapter<Subject, SubjectAdapter.SubjectViewHolder> {
    private static final String TAG = "SubjectAdapter";
    private final SubjectCallbacks subjectViewCallbacks;

    public SubjectAdapter(@NonNull @NotNull SubjectCallbacks callbacks) {
        super(subjectItemCallback);
        this.subjectViewCallbacks = callbacks;
    }

    @NonNull
    @NotNull
    @Override
    public SubjectViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        SubjectViewBinding viewBinding = SubjectViewBinding.inflate(inflater, parent, false);
        return new SubjectViewHolder(viewBinding, subjectViewCallbacks);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull SubjectViewHolder holder, int position) {
        Subject subject = getItem(position);
        holder.viewBinding.setSubject(subject);
    }

    protected static class SubjectViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private final SubjectViewBinding viewBinding;
        private final SubjectCallbacks callbacks;

        public SubjectViewHolder(@NonNull @NotNull SubjectViewBinding viewBinding, @NonNull SubjectCallbacks callbacks) {
            super(viewBinding.getRoot());
            this.viewBinding = viewBinding;
            this.callbacks = callbacks;
            this.viewBinding.indicatorIcon.setOnClickListener(this);
            this.viewBinding.subjectCardView.setOnClickListener(this);
            this.viewBinding.subjectCardView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int _id = v.getId();
            if (_id == viewBinding.indicatorIcon.getId()) callbacks.onAddClick(getAdapterPosition());
            else if (_id == viewBinding.subjectCardView.getId()) callbacks.onClick(getAdapterPosition());
            else NotifyUtils.logDebug(TAG, "Unspecified view is clicked!!");
        }

        @Override
        public boolean onLongClick(View v) {
            if (v.getId() == viewBinding.subjectCardView.getId()) callbacks.onLongClick(getAdapterPosition());
            return true;
        }
    }
}
