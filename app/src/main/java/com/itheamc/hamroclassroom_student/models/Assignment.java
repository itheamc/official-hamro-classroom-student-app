package com.itheamc.hamroclassroom_student.models;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import java.util.Date;
import java.util.List;
import java.util.Objects;

public class Assignment {
    private String _id;
    private List<String> _images;
    private List<String> _docs;
    private String _title;
    private String _desc;
    private String _subject;
    private Date _assigned_date;
    private Date _due_date;
    private int _total_submits;

    // Constructor
    public Assignment() {
    }

    // Constructor with parameters
    public Assignment(String _id, List<String> _images, List<String> _docs, String _title, String _desc, String _subject, Date _assigned_date, Date _due_date, int _total_submits) {
        this._id = _id;
        this._images = _images;
        this._docs = _docs;
        this._title = _title;
        this._desc = _desc;
        this._subject = _subject;
        this._assigned_date = _assigned_date;
        this._due_date = _due_date;
        this._total_submits = _total_submits;
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

    public String get_title() {
        return _title;
    }

    public void set_title(String _title) {
        this._title = _title;
    }

    public String get_desc() {
        return _desc;
    }

    public void set_desc(String _desc) {
        this._desc = _desc;
    }

    public String get_subject() {
        return _subject;
    }

    public void set_subject(String _subject) {
        this._subject = _subject;
    }

    public Date get_assigned_date() {
        return _assigned_date;
    }

    public void set_assigned_date(Date _assigned_date) {
        this._assigned_date = _assigned_date;
    }

    public Date get_due_date() {
        return _due_date;
    }

    public void set_due_date(Date _due_date) {
        this._due_date = _due_date;
    }

    public int get_total_submits() {
        return _total_submits;
    }

    public void set_total_submits(int _total_submits) {
        this._total_submits = _total_submits;
    }

    // Overriding toString() method
    @Override
    public String toString() {
        return "Assignment{" +
                "_id='" + _id + '\'' +
                ", _images=" + _images +
                ", _docs=" + _docs +
                ", _title='" + _title + '\'' +
                ", _desc='" + _desc + '\'' +
                ", _subject='" + _subject + '\'' +
                ", _assigned_date=" + _assigned_date +
                ", _due_date=" + _due_date +
                ", _total_submits=" + _total_submits +
                '}';
    }

    // Overriding equals() method
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Assignment that = (Assignment) o;
        return get_total_submits() == that.get_total_submits() &&
                Objects.equals(get_id(), that.get_id()) &&
                Objects.equals(get_images(), that.get_images()) &&
                Objects.equals(get_docs(), that.get_docs()) &&
                Objects.equals(get_title(), that.get_title()) &&
                Objects.equals(get_desc(), that.get_desc()) &&
                Objects.equals(get_subject(), that.get_subject()) &&
                Objects.equals(get_assigned_date(), that.get_assigned_date()) &&
                Objects.equals(get_due_date(), that.get_due_date());
    }


    // DiffUtil.ItemCallback
    public static DiffUtil.ItemCallback<Assignment> assignmentItemCallback = new DiffUtil.ItemCallback<Assignment>() {
        @Override
        public boolean areItemsTheSame(@NonNull Assignment oldItem, @NonNull Assignment newItem) {
            return newItem.equals(oldItem);
        }

        @Override
        public boolean areContentsTheSame(@NonNull Assignment oldItem, @NonNull Assignment newItem) {
            return false;
        }
    };
}
