package com.itheamc.hamroclassroom_student.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.itheamc.hamroclassroom_student.callbacks.SchoolCallbacks;
import com.itheamc.hamroclassroom_student.databinding.SchoolViewBinding;
import com.itheamc.hamroclassroom_student.models.School;


import org.jetbrains.annotations.NotNull;

import static com.itheamc.hamroclassroom_student.models.School.schoolItemCallback;


public class SchoolAdapter extends ListAdapter<School, SchoolAdapter.SchoolViewHolder> {
    private final SchoolCallbacks callbacks;

    public SchoolAdapter(@NonNull SchoolCallbacks callbacks) {
        super(schoolItemCallback);
        this.callbacks = callbacks;
    }

    @NonNull
    @NotNull
    @Override
    public SchoolViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        SchoolViewBinding schoolViewBinding = SchoolViewBinding.inflate(inflater, parent, false);
        return new SchoolViewHolder(schoolViewBinding, callbacks);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull SchoolViewHolder holder, int position) {
        School school = getItem(position);
        holder.viewBinding.setSchool(school);
    }

    protected static class SchoolViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final SchoolViewBinding viewBinding;
        private final SchoolCallbacks callbacks;

        public SchoolViewHolder(@NonNull @NotNull SchoolViewBinding viewBinding, SchoolCallbacks callbacks) {
            super(viewBinding.getRoot());
            this.viewBinding = viewBinding;
            this.callbacks = callbacks;
            this.viewBinding.getRoot().setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            callbacks.onClick(getAdapterPosition());
        }
    }
}
