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
    private String _teacher_ref;    // Hold the reference of the subject i.e. id
    private String _teacher;
    private String _subject_ref;    // Hold the reference of the subject i.e. id
    private Subject _subject;
    private List<String> _students_ref;     // Hold the reference of the student i.e. id
    private List<User> _students;        // Hold the list of students submitted
    private Date _assigned_date;
    private Date _due_date;

    // Constructor
    public Assignment() {
    }

    // Constructor with parameters
    public Assignment(String _id, List<String> _images, List<String> _docs, String _title, String _desc, String _teacher_ref, String _teacher, String _subject_ref, Subject _subject, List<String> _students_ref, List<User> _students, Date _assigned_date, Date _due_date) {
        this._id = _id;
        this._images = _images;
        this._docs = _docs;
        this._title = _title;
        this._desc = _desc;
        this._teacher_ref = _teacher_ref;
        this._teacher = _teacher;
        this._subject_ref = _subject_ref;
        this._subject = _subject;
        this._students_ref = _students_ref;
        this._students = _students;
        this._assigned_date = _assigned_date;
        this._due_date = _due_date;
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

    public String get_teacher_ref() {
        return _teacher_ref;
    }

    public void set_teacher_ref(String _teacher_ref) {
        this._teacher_ref = _teacher_ref;
    }

    public String get_teacher() {
        return _teacher;
    }

    public void set_teacher(String _teacher) {
        this._teacher = _teacher;
    }

    public String get_subject_ref() {
        return _subject_ref;
    }

    public void set_subject_ref(String _subject_ref) {
        this._subject_ref = _subject_ref;
    }

    public Subject get_subject() {
        return _subject;
    }

    public void set_subject(Subject _subject) {
        this._subject = _subject;
    }

    public List<String> get_students_ref() {
        return _students_ref;
    }

    public void set_students_ref(List<String> _students_ref) {
        this._students_ref = _students_ref;
    }

    public List<User> get_students() {
        return _students;
    }

    public void set_students(List<User> _students) {
        this._students = _students;
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

    // Overriding toString() method
    @Override
    public String toString() {
        return "Assignment{" +
                "_id='" + _id + '\'' +
                ", _images=" + _images +
                ", _docs=" + _docs +
                ", _title='" + _title + '\'' +
                ", _desc='" + _desc + '\'' +
                ", _teacher_ref='" + _teacher_ref + '\'' +
                ", _teacher='" + _teacher + '\'' +
                ", _subject_ref='" + _subject_ref + '\'' +
                ", _subject=" + _subject +
                ", _students_ref=" + _students_ref +
                ", _students=" + _students +
                ", _assigned_date=" + _assigned_date +
                ", _due_date=" + _due_date +
                '}';
    }

    // Overriding equals() method
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Assignment that = (Assignment) o;
        return Objects.equals(get_id(), that.get_id()) &&
                Objects.equals(get_images(), that.get_images()) &&
                Objects.equals(get_docs(), that.get_docs()) &&
                Objects.equals(get_title(), that.get_title()) &&
                Objects.equals(get_desc(), that.get_desc()) &&
                Objects.equals(get_teacher_ref(), that.get_teacher_ref()) &&
                Objects.equals(get_teacher(), that.get_teacher()) &&
                Objects.equals(get_subject_ref(), that.get_subject_ref()) &&
                Objects.equals(get_subject(), that.get_subject()) &&
                Objects.equals(get_students_ref(), that.get_students_ref()) &&
                Objects.equals(get_students(), that.get_students()) &&
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
