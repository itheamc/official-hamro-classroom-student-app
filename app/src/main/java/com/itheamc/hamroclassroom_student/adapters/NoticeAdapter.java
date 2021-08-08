package com.itheamc.hamroclassroom_student.adapters;

import static com.itheamc.hamroclassroom_student.models.Notice.noticeItemCallback;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.itheamc.hamroclassroom_student.callbacks.NoticeCallbacks;
import com.itheamc.hamroclassroom_student.databinding.NoticeViewBinding;
import com.itheamc.hamroclassroom_student.models.Notice;
import com.itheamc.hamroclassroom_student.utils.NotifyUtils;

import java.text.DateFormat;

public class NoticeAdapter extends ListAdapter<Notice, NoticeAdapter.NoticeViewHolder> {
    private static final String TAG = "NoticeAdapter";
    private final NoticeCallbacks callbacks;

    public NoticeAdapter(@NonNull NoticeCallbacks noticeCallbacks) {
        super(noticeItemCallback);
        this.callbacks = noticeCallbacks;
    }

    @NonNull
    @Override
    public NoticeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        NoticeViewBinding viewBinding = NoticeViewBinding.inflate(inflater, parent, false);
        return new NoticeViewHolder(viewBinding, callbacks);
    }

    @Override
    public void onBindViewHolder(@NonNull NoticeViewHolder holder, int position) {
        Notice notice = getItem(position);
        String formattedDate = DateFormat.getDateInstance().format(notice.get_notified_on());
        holder.viewBinding.setDate(formattedDate);

        // Submitting to the data binding
        holder.viewBinding.setNotice(notice);
        holder.viewBinding.setDate(formattedDate);
    }

    protected static class NoticeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final NoticeViewBinding viewBinding;
        private final NoticeCallbacks callbacks;

        public NoticeViewHolder(@NonNull NoticeViewBinding noticeViewBinding, NoticeCallbacks noticeCallbacks) {
            super(noticeViewBinding.getRoot());
            this.viewBinding = noticeViewBinding;
            this.callbacks = noticeCallbacks;
            this.viewBinding.noticeCardView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == viewBinding.noticeCardView.getId()) {
                callbacks.onClick(getAdapterPosition());
            } else NotifyUtils.logDebug(TAG, "onClick() -> Unspecified");
        }
    }
}
