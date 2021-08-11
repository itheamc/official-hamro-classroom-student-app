package com.itheamc.hamroclassroom_student.models;

import java.util.Date;
import java.util.List;

public class Teacher {
    private String _id;
    private String _name;
    private String _gender;
    private String _image;
    private String _phone;
    private String _email;
    private String _address;
    private List<String> _schools_ref;
    private List<School> _schools;
    private List<String> _subjects_ref;
    private List<Subject> _subjects;
    private List<String> _assignments_ref;
    private List<Assignment> _assignments;
    private Date _joined_on;

    // Constructor
    public Teacher() {
    }

    // Constructor with parameters
    public Teacher(String _id, String _name, String _gender, String _image, String _phone, String _email, String _address, List<String> _schools_ref, List<School> _schools, List<String> _subjects_ref, List<Subject> _subjects, List<String> _assignments_ref, List<Assignment> _assignments, Date _joined_on) {
        this._id = _id;
        this._name = _name;
        this._gender = _gender;
        this._image = _image;
        this._phone = _phone;
        this._email = _email;
        this._address = _address;
        this._schools_ref = _schools_ref;
        this._schools = _schools;
        this._subjects_ref = _subjects_ref;
        this._subjects = _subjects;
        this._assignments_ref = _assignments_ref;
        this._assignments = _assignments;
        this._joined_on = _joined_on;
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

    public String get_gender() {
        return _gender;
    }

    public void set_gender(String _gender) {
        this._gender = _gender;
    }

    public String get_image() {
        return _image;
    }

    public void set_image(String _image) {
        this._image = _image;
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

    public List<String> get_schools_ref() {
        return _schools_ref;
    }

    public void set_schools_ref(List<String> _schools_ref) {
        this._schools_ref = _schools_ref;
    }

    public List<School> get_schools() {
        return _schools;
    }

    public void set_schools(List<School> _schools) {
        this._schools = _schools;
    }

    public List<String> get_subjects_ref() {
        return _subjects_ref;
    }

    public void set_subjects_ref(List<String> _subjects_ref) {
        this._subjects_ref = _subjects_ref;
    }

    public List<Subject> get_subjects() {
        return _subjects;
    }

    public void set_subjects(List<Subject> _subjects) {
        this._subjects = _subjects;
    }

    public List<String> get_assignments_ref() {
        return _assignments_ref;
    }

    public void set_assignments_ref(List<String> _assignments_ref) {
        this._assignments_ref = _assignments_ref;
    }

    public List<Assignment> get_assignments() {
        return _assignments;
    }

    public void set_assignments(List<Assignment> _assignments) {
        this._assignments = _assignments;
    }

    public Date get_joined_on() {
        return _joined_on;
    }

    public void set_joined_on(Date _joined_on) {
        this._joined_on = _joined_on;
    }

    // Overriding toString() method

    @Override
    public String toString() {
        return "Teacher{" +
                "_id='" + _id + '\'' +
                ", _name='" + _name + '\'' +
                ", _gender='" + _gender + '\'' +
                ", _image='" + _image + '\'' +
                ", _phone='" + _phone + '\'' +
                ", _email='" + _email + '\'' +
                ", _address='" + _address + '\'' +
                ", _schools_ref=" + _schools_ref +
                ", _schools=" + _schools +
                ", _subjects_ref=" + _subjects_ref +
                ", _subjects=" + _subjects +
                ", _assignments_ref=" + _assignments_ref +
                ", _assignments=" + _assignments +
                ", _joined_on=" + _joined_on +
                '}';
    }
}
