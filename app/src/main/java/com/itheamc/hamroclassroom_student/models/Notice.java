package com.itheamc.hamroclassroom_student.models;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import java.util.Date;
import java.util.List;
import java.util.Objects;

public class Notice {
    private String _id;
    private String _title;
    private String _desc;
    private String _school;
    private List<String> _classes;      // Id and Name of the School
    private List<String> _notifier;     // Id and Name of the notifier
    private Date _notified_on;


    // Constructor
    public Notice() {
    }


    // Constructor with parameters
    public Notice(String _id, String _title, String _desc, String _school, List<String> _classes, List<String> _notifier, Date _notified_on) {
        this._id = _id;
        this._title = _title;
        this._desc = _desc;
        this._school = _school;
        this._classes = _classes;
        this._notifier = _notifier;
        this._notified_on = _notified_on;
    }

    // Getters and Setters
    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
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

    public String get_school() {
        return _school;
    }

    public void set_school(String _school) {
        this._school = _school;
    }

    public List<String> get_classes() {
        return _classes;
    }

    public void set_classes(List<String> _classes) {
        this._classes = _classes;
    }

    public List<String> get_notifier() {
        return _notifier;
    }

    public void set_notifier(List<String> _notifier) {
        this._notifier = _notifier;
    }

    public Date get_notified_on() {
        return _notified_on;
    }

    public void set_notified_on(Date _notified_on) {
        this._notified_on = _notified_on;
    }

    // Overriding toString() method


    @Override
    public String toString() {
        return "Notice{" +
                "_id='" + _id + '\'' +
                ", _title='" + _title + '\'' +
                ", _desc='" + _desc + '\'' +
                ", _school='" + _school + '\'' +
                ", _classes=" + _classes +
                ", _notifier=" + _notifier +
                ", _notified_on=" + _notified_on +
                '}';
    }

    // Overriding equals() method
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Notice notice = (Notice) o;
        return Objects.equals(get_id(), notice.get_id()) &&
                Objects.equals(get_title(), notice.get_title()) &&
                Objects.equals(get_desc(), notice.get_desc()) &&
                Objects.equals(get_school(), notice.get_school()) &&
                Objects.equals(get_classes(), notice.get_classes()) &&
                Objects.equals(get_notifier(), notice.get_notifier()) &&
                Objects.equals(get_notified_on(), notice.get_notified_on());
    }


    // Diffutils.ItemCallback
    public static DiffUtil.ItemCallback<Notice> noticeItemCallback = new DiffUtil.ItemCallback<Notice>() {
        @Override
        public boolean areItemsTheSame(@NonNull Notice oldItem, @NonNull Notice newItem) {
            return newItem.equals(oldItem);
        }

        @Override
        public boolean areContentsTheSame(@NonNull Notice oldItem, @NonNull Notice newItem) {
            return false;
        }
    };
}
