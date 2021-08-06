package com.itheamc.hamroclassroom_student.models;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.DiffUtil;

import java.util.Date;
import java.util.List;
import java.util.Objects;

public class Submission {
    private String _id;
    private List<String> _images;
    private List<String> _docs;
    private String _texts;
    private List<String> _submitted_by;     // Name and id of the student
    private Date _submitted_date;
    private Date _checked_date;
    private boolean _checked;
    private String _comment;

    // Constructor
    public Submission() {
    }

    // Constructor with parameters
    public Submission(String _id, List<String> _images, List<String> _docs, String _texts, List<String> _submitted_by, Date _submitted_date, Date _checked_date, boolean _checked, String _comment) {
        this._id = _id;
        this._images = _images;
        this._docs = _docs;
        this._texts = _texts;
        this._submitted_by = _submitted_by;
        this._submitted_date = _submitted_date;
        this._checked_date = _checked_date;
        this._checked = _checked;
        this._comment = _comment;
    }

    // Getters and Setters
    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public List<String> get_images() {
        return _images;
    }

    public void set_images(List<String> _images) {
        this._images = _images;
    }

    public List<String> get_docs() {
        return _docs;
    }

    public void set_docs(List<String> _docs) {
        this._docs = _docs;
    }

    public String get_texts() {
        return _texts;
    }

    public void set_texts(String _texts) {
        this._texts = _texts;
    }

    public List<String> get_submitted_by() {
        return _submitted_by;
    }

    public void set_submitted_by(List<String> _submitted_by) {
        this._submitted_by = _submitted_by;
    }

    public Date get_submitted_date() {
        return _submitted_date;
    }

    public void set_submitted_date(Date _submitted_date) {
        this._submitted_date = _submitted_date;
    }

    public Date get_checked_date() {
        return _checked_date;
    }

    public void set_checked_date(Date _checked_date) {
        this._checked_date = _checked_date;
    }

    public boolean is_checked() {
        return _checked;
    }

    public void set_checked(boolean _checked) {
        this._checked = _checked;
    }

    public String get_comment() {
        return _comment;
    }

    public void set_comment(String _comment) {
        this._comment = _comment;
    }

    // Overriding toString() method
    @Override
    public String toString() {
        return "Submission{" +
                "_id='" + _id + '\'' +
                ", _images=" + _images +
                ", _docs=" + _docs +
                ", _texts='" + _texts + '\'' +
                ", _submitted_by=" + _submitted_by +
                ", _submitted_date=" + _submitted_date +
                ", _checked_date=" + _checked_date +
                ", _checked=" + _checked +
                ", _comment='" + _comment + '\'' +
                '}';
    }

    // Overriding equals() method
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Submission that = (Submission) o;
        return is_checked() == that.is_checked() &&
                Objects.equals(get_id(), that.get_id()) &&
                Objects.equals(get_images(), that.get_images()) &&
                Objects.equals(get_docs(), that.get_docs()) &&
                Objects.equals(get_texts(), that.get_texts()) &&
                Objects.equals(get_submitted_by(), that.get_submitted_by()) &&
                Objects.equals(get_submitted_date(), that.get_submitted_date()) &&
                Objects.equals(get_checked_date(), that.get_checked_date()) &&
                Objects.equals(get_comment(), that.get_comment());
    }

    // DiffUtil.ItemCallback
    public static DiffUtil.ItemCallback<Submission> submissionItemCallback = new DiffUtil.ItemCallback<Submission>() {
        @Override
        public boolean areItemsTheSame(@NonNull Submission oldItem, @NonNull Submission newItem) {
            return newItem.equals(oldItem);
        }

        @Override
        public boolean areContentsTheSame(@NonNull Submission oldItem, @NonNull Submission newItem) {
            return false;
        }
    };

    // Binding adapter to handle the view visibility
    @BindingAdapter("android:is_visible")
    public static void setVisibility(View view, boolean bool) {
        view.setVisibility(bool ? View.VISIBLE : View.GONE);
    }
}
