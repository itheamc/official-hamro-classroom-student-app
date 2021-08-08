package com.itheamc.hamroclassroom_student.adapters;

import static com.itheamc.hamroclassroom_student.models.Subject.subjectItemCallback;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.itheamc.hamroclassroom_student.callbacks.SubjectCallbacks;
import com.itheamc.hamroclassroom_student.databinding.ClassesViewBinding;
import com.itheamc.hamroclassroom_student.models.Subject;
import com.itheamc.hamroclassroom_student.utils.NotifyUtils;

public class ClassesAdapter extends ListAdapter<Subject, ClassesAdapter.ClassesViewHolder> {
    private static final String TAG = "ClassesAdapter";
    private final SubjectCallbacks subjectCallbacks;

    public ClassesAdapter(@NonNull SubjectCallbacks subjectCallbacks) {
        super(subjectItemCallback);
        this.subjectCallbacks = subjectCallbacks;
    }

    @NonNull
    @Override
    public ClassesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ClassesViewBinding classesViewBinding = ClassesViewBinding.inflate(inflater, parent, false);
        return new ClassesViewHolder(classesViewBinding, subjectCallbacks);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassesViewHolder holder, int position) {
        Subject subject = getItem(position);
        holder.viewBinding.setSubject(subject);
    }

    protected static class ClassesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private final ClassesViewBinding viewBinding;
        private final SubjectCallbacks callbacks;

        public ClassesViewHolder(@NonNull ClassesViewBinding classesViewBinding, SubjectCallbacks subjectCallbacks) {
            super(classesViewBinding.getRoot());
            this.viewBinding = classesViewBinding;
            this.callbacks = subjectCallbacks;

            // Implementing OnClickListener on Views
            this.viewBinding.joinButton.setOnClickListener(this);
            this.viewBinding.subjectCardView.setOnClickListener(this);
            this.viewBinding.copyLink.setOnClickListener(this);
            this.viewBinding.subjectCardView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int _id = v.getId();
            if (_id == viewBinding.joinButton.getId()) callbacks.onJoinClassClick(getAdapterPosition());
            else if (_id == viewBinding.subjectCardView.getId()) callbacks.onClick(getAdapterPosition());
            else if (_id == viewBinding.copyLink.getId()) callbacks.onCopyClick(getAdapterPosition());
            else NotifyUtils.logDebug(TAG, "Unspecified view is clicked!!");
        }

        @Override
        public boolean onLongClick(View v) {
            if (v.getId() == viewBinding.subjectCardView.getId()) callbacks.onLongClick(getAdapterPosition());
            return true;
        }
    }
}
