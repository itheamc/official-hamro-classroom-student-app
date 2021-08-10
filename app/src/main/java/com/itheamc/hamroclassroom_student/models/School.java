package com.itheamc.hamroclassroom_student.models;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import java.util.Date;
import java.util.List;
import java.util.Objects;

public class School {
    private String _id;
    private String _name;
    private String _phone;
    private String _email;
    private String _address;
    private String _website;
    private String _icon;
    private String _principal_ref;
    private User _principal;
    private Date _established_on;
    private Date _joined_on;
    private int _students;
    private int _teachers;

    // Constructor
    public School() {
    }

    // Constructor with parameters
    public School(String _id, String _name, String _phone, String _email, String _address, String _website, String _icon, String _principal_ref, User _principal, Date _established_on, Date _joined_on, int _students, int _teachers) {
        this._id = _id;
        this._name = _name;
        this._phone = _phone;
        this._email = _email;
        this._address = _address;
        this._website = _website;
        this._icon = _icon;
        this._principal_ref = _principal_ref;
        this._principal = _principal;
        this._established_on = _established_on;
        this._joined_on = _joined_on;
        this._students = _students;
        this._teachers = _teachers;
    }

    // Getters and Setters
    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public String get_phone() {
        return _phone;
    }

    public void set_phone(String _phone) {
        this._phone = _phone;
    }

    public String get_email() {
        return _email;
    }

    public void set_email(String _email) {
        this._email = _email;
    }

    public String get_address() {
        return _address;
    }

    public void set_address(String _address) {
        this._address = _address;
    }

    public String get_website() {
        return _website;
    }

    public void set_website(String _website) {
        this._website = _website;
    }

    public String get_icon() {
        return _icon;
    }

    public void set_icon(String _icon) {
        this._icon = _icon;
    }

    public String get_principal_ref() {
        return _principal_ref;
    }

    public void set_principal_ref(String _principal_ref) {
        this._principal_ref = _principal_ref;
    }

    public User get_principal() {
        return _principal;
    }

    public void set_principal(User _principal) {
        this._principal = _principal;
    }

    public Date get_established_on() {
        return _established_on;
    }

    public void set_established_on(Date _established_on) {
        this._established_on = _established_on;
    }

    public Date get_joined_on() {
        return _joined_on;
    }

    public void set_joined_on(Date _joined_on) {
        this._joined_on = _joined_on;
    }

    public int get_students() {
        return _students;
    }

    public void set_students(int _students) {
        this._students = _students;
    }

    public int get_teachers() {
        return _teachers;
    }

    public void set_teachers(int _teachers) {
        this._teachers = _teachers;
    }

    // Overriding toString() method
    @Override
    public String toString() {
        return "School{" +
                "_id='" + _id + '\'' +
                ", _name='" + _name + '\'' +
                ", _phone='" + _phone + '\'' +
                ", _email='" + _email + '\'' +
                ", _address='" + _address + '\'' +
                ", _website='" + _website + '\'' +
                ", _icon='" + _icon + '\'' +
                ", _principal_ref='" + _principal_ref + '\'' +
                ", _principal=" + _principal +
                ", _established_on=" + _established_on +
                ", _joined_on=" + _joined_on +
                ", _students=" + _students +
                ", _teachers=" + _teachers +
                '}';
    }

    // Overriding equals() method
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        School school = (School) o;
        return get_students() == school.get_students() &&
                get_teachers() == school.get_teachers() &&
                Objects.equals(get_id(), school.get_id()) &&
                Objects.equals(get_name(), school.get_name()) &&
                Objects.equals(get_phone(), school.get_phone()) &&
                Objects.equals(get_email(), school.get_email()) &&
                Objects.equals(get_address(), school.get_address()) &&
                Objects.equals(get_website(), school.get_website()) &&
                Objects.equals(get_icon(), school.get_icon()) &&
                Objects.equals(get_principal_ref(), school.get_principal_ref()) &&
                Objects.equals(get_principal(), school.get_principal()) &&
                Objects.equals(get_established_on(), school.get_established_on()) &&
                Objects.equals(get_joined_on(), school.get_joined_on());
    }



    // DiffUtil.ItemCallback object
    public static DiffUtil.ItemCallback<School> schoolItemCallback = new DiffUtil.ItemCallback<School>() {
        @Override
        public boolean areItemsTheSame(@NonNull School oldItem, @NonNull School newItem) {
            return newItem.equals(oldItem);
        }

        @Override
        public boolean areContentsTheSame(@NonNull School oldItem, @NonNull School newItem) {
            return false;
        }
    };
}
