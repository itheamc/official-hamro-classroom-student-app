package com.itheamc.hamroclassroom_student.viewmodel;

import androidx.lifecycle.ViewModel;

import com.itheamc.hamroclassroom_student.models.Assignment;
import com.itheamc.hamroclassroom_student.models.Notice;
import com.itheamc.hamroclassroom_student.models.School;
import com.itheamc.hamroclassroom_student.models.Subject;
import com.itheamc.hamroclassroom_student.models.Submission;
import com.itheamc.hamroclassroom_student.models.Teacher;
import com.itheamc.hamroclassroom_student.models.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainViewModel extends ViewModel {
    /*
    Objects
     */
    private User user;
    private Teacher teacher;
    private Subject subject;
    private Assignment assignment;
    private Submission submission;
    private School school;
    private Notice notice;

    /*
    Lists
     */
    private List<School> schools;
    private List<Subject> subjects;
    private List<Teacher> teachers;
    private List<Assignment> assignments;
    private List<Assignment> allAssignments;
    private List<Submission> allSubmissions;
    private List<Submission> submissions;
    private List<Notice> notices;

    /*
    Past Date
     */
    private long past_date;


    /*
    Getters and Setters
     */
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public Assignment getAssignment() {
        return assignment;
    }

    public void setAssignment(Assignment assignment) {
        this.assignment = assignment;
    }

    public Submission getSubmission() {
        return submission;
    }

    public void setSubmission(Submission submission) {
        this.submission = submission;
    }

    public School getSchool() {
        return school;
    }

    public void setSchool(School school) {
        this.school = school;
    }

    public Notice getNotice() {
        return notice;
    }

    public void setNotice(Notice notice) {
        this.notice = notice;
    }

    public List<School> getSchools() {
        return schools;
    }

    public void setSchools(List<School> schools) {
        this.schools = schools;
    }

    public List<Subject> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<Subject> subjects) {
        this.subjects = subjects;
    }

    public List<Teacher> getTeachers() {
        return teachers;
    }

    public void setTeachers(List<Teacher> teachers) {
        this.teachers = teachers;
    }

    public List<Assignment> getAssignments() {
        return assignments;
    }

    public void setAssignments(List<Assignment> assignments) {
        this.assignments = assignments;
    }

    public List<Assignment> getAllAssignments() {
        return allAssignments;
    }

    public void setAllAssignments(List<Assignment> allAssignments) {
        this.allAssignments = allAssignments;
    }

    public List<Submission> getAllSubmissions() {
        return allSubmissions;
    }

    public void setAllSubmissions(List<Submission> allSubmissions) {
        this.allSubmissions = allSubmissions;
    }

    public List<Submission> getSubmissions() {
        return submissions;
    }

    public void setSubmissions(List<Submission> submissions) {
        this.submissions = submissions;
    }

    public List<Notice> getNotices() {
        return notices;
    }

    public void setNotices(List<Notice> notices) {
        this.notices = notices;
    }

    public long get_past_date() {
        return past_date;
    }

    public void set_past_date(long past_date) {
        this.past_date = past_date;
    }

    /*
         Function to replace item in List<Subject> subjects
          */
    public void replaceSubject(Subject _subject) {
        List<Subject> subjectList = new ArrayList<>();
        for (Subject sub: subjects) {
            if (sub.get_id().equals(_subject.get_id())) {
                subjectList.add(_subject);
                continue;
            }
            subjectList.add(sub);
        }

        this.subjects = new ArrayList<>();
        this.subjects = subjectList;
    }

    /*
    Function to update subject Item in the subjects
     */
    public List<Subject> addTeacherToSubject(List<Teacher> __teachers) {
        List<Subject> updatedSubjects = new ArrayList<>();
        this.teachers = __teachers;
        for (Subject sub: subjects) {
            for (Teacher teach: __teachers) {
                if (!sub.get_teacher_ref().equals(teach.get_id())) continue;
                sub.set_teacher(teach);
                break;
            }

            updatedSubjects.add(sub);
        }

        this.subjects = new ArrayList<>();
        this.subjects = updatedSubjects;
        return updatedSubjects;
    }
}
