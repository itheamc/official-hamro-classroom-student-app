package com.itheamc.hamroclassroom_student.adapters;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.itheamc.hamroclassroom_student.callbacks.ImageCallbacks;
import com.itheamc.hamroclassroom_student.databinding.ImageViewBinding;

import org.jetbrains.annotations.NotNull;

public class ImageAdapter extends ListAdapter<Uri, ImageAdapter.ImageViewHolder> {
    private static final String TAG = "ImageAdapter";
    private final ImageCallbacks callback;

    public ImageAdapter(@NonNull ImageCallbacks callback) {
        super(imageItemCallback);
        this.callback = callback;
    }

    public static DiffUtil.ItemCallback<Uri> imageItemCallback = new DiffUtil.ItemCallback<Uri>() {
        @Override
        public boolean areItemsTheSame(@NonNull @NotNull Uri oldItem, @NonNull @NotNull Uri newItem) {
            return false;
        }

        @Override
        public boolean areContentsTheSame(@NonNull @NotNull Uri oldItem, @NonNull @NotNull Uri newItem) {
            return false;
        }
    };

    @BindingAdapter("android:imageUri")
    public static void setImage(ImageView imageView, Uri uri) {
        imageView.setImageURI(uri);
    }

    @NonNull
    @NotNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ImageViewBinding viewBinding = ImageViewBinding.inflate(inflater, parent, false);
        return new ImageViewHolder(viewBinding, callback);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ImageViewHolder holder, int position) {
        Uri uri = getItem(position);
        holder.viewBinding.setUri(uri);
    }

    protected static class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ImageViewBinding viewBinding;
        private final ImageCallbacks callback;

        public ImageViewHolder(@NonNull @NotNull ImageViewBinding imageViewBinding, ImageCallbacks imageViewCallback) {
            super(imageViewBinding.getRoot());
            this.viewBinding = imageViewBinding;
            this.callback = imageViewCallback;
            this.viewBinding.removeImageBtn.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int _id = v.getId();
            if (_id == viewBinding.removeImageBtn.getId()) {
                callback.onRemove(getAdapterPosition());
            }
        }
    }
}
