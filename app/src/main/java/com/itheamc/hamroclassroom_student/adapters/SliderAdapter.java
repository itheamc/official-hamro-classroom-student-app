package com.itheamc.hamroclassroom_student.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.itheamc.hamroclassroom_student.databinding.SubmissionSliderViewBinding;

import org.jetbrains.annotations.NotNull;

public class SliderAdapter extends ListAdapter<String, SliderAdapter.SliderViewHolder> {
    public SliderAdapter() {
        super(sliderItemCallback);
    }

    // DiffUtils
    public static DiffUtil.ItemCallback<String> sliderItemCallback = new DiffUtil.ItemCallback<String>() {
        @Override
        public boolean areItemsTheSame(@NonNull @NotNull String oldItem, @NonNull @NotNull String newItem) {
            return false;
        }

        @Override
        public boolean areContentsTheSame(@NonNull @NotNull String oldItem, @NonNull @NotNull String newItem) {
            return false;
        }
    };

    @NonNull
    @NotNull
    @Override
    public SliderViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        SubmissionSliderViewBinding sliderViewBinding = SubmissionSliderViewBinding.inflate(inflater, parent, false);
        return new SliderViewHolder(sliderViewBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull SliderViewHolder holder, int position) {
        String imageUrl = getItem(position);
        holder.sliderViewBinding.setImageurl(imageUrl);
    }





    public static class SliderViewHolder extends RecyclerView.ViewHolder {
        private final SubmissionSliderViewBinding sliderViewBinding;

        public SliderViewHolder(@NonNull SubmissionSliderViewBinding viewBinding) {
            super(viewBinding.getRoot());
            this.sliderViewBinding = viewBinding;
        }

    }
}
