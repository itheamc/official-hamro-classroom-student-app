package com.itheamc.hamroclassroom_student.callbacks;

import com.itheamc.hamroclassroom_student.models.Assignment;
import com.itheamc.hamroclassroom_student.models.Notice;
import com.itheamc.hamroclassroom_student.models.School;
import com.itheamc.hamroclassroom_student.models.Subject;
import com.itheamc.hamroclassroom_student.models.Submission;
import com.itheamc.hamroclassroom_student.models.Teacher;
import com.itheamc.hamroclassroom_student.models.User;

import java.util.List;

public interface FirestoreCallbacks {
    void onSuccess(
            User user,
            Teacher teacher,
            School school,
            List<School> schools,
            List<Subject> subjects,
            List<Assignment> assignments,
            List<Submission> submissions,
            List<Notice> notices);
    void onFailure(Exception e);
}
