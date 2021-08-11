package com.itheamc.hamroclassroom_student.handlers;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.core.os.HandlerCompat;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.itheamc.hamroclassroom_student.callbacks.FirestoreCallbacks;
import com.itheamc.hamroclassroom_student.models.Assignment;
import com.itheamc.hamroclassroom_student.models.Notice;
import com.itheamc.hamroclassroom_student.models.School;
import com.itheamc.hamroclassroom_student.models.Subject;
import com.itheamc.hamroclassroom_student.models.Submission;
import com.itheamc.hamroclassroom_student.models.Teacher;
import com.itheamc.hamroclassroom_student.models.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FirestoreHandler {
    private final FirebaseFirestore firestore;
    private final Handler handler;
    private final FirestoreCallbacks callbacks;
    private final ExecutorService executorService;

    // Constructor
    public FirestoreHandler(@NonNull FirestoreCallbacks firestoreCallbacks) {
        this.callbacks = firestoreCallbacks;
        this.firestore = FirebaseFirestore.getInstance();
        this.handler = HandlerCompat.createAsync(Looper.getMainLooper());
        this.executorService = Executors.newFixedThreadPool(4);
    }

    // Getter for instance
    public static FirestoreHandler getInstance(@NonNull FirestoreCallbacks firestoreCallbacks) {
        return new FirestoreHandler(firestoreCallbacks);
    }

    /**
     * Function to get user info from the cloud Firestore
     * --------------------------------------------------------------------------------------
     */
    public void getUser(String userId) {
        firestore.collection("students")
                .document(userId)
                .get()
                .addOnSuccessListener(executorService, documentSnapshot -> {
                    if (documentSnapshot != null) {
                        User user = new User();
                        user = documentSnapshot.toObject(User.class);
                        if (user != null) {
                            User finalUser = user;
                            firestore.collection("schools")
                                    .document(user.get_school_ref())
                                    .get()
                                    .addOnSuccessListener(executorService, schoolDocSnap -> {
                                        if (schoolDocSnap != null) {
                                            School school = schoolDocSnap.toObject(School.class);
                                            finalUser.set_school(school);
                                            notifyOnSuccess(finalUser, null, null, null, null, null, null);
                                        } else {
                                            notifyOnFailure(new Exception("School not found"));
                                        }
                                    })
                                    .addOnFailureListener(executorService, this::notifyOnFailure);
                        } else {
                            notifyOnFailure(new Exception("User not found"));
                        }
                    } else {
                        notifyOnFailure(new Exception("User not found"));
                    }

                })
                .addOnFailureListener(executorService, this::notifyOnFailure);
    }


    /**
     * Function to store user in the Firestore
     * --------------------------------------------------------------------------------------
     */
    public void storeUser(User user) {
        firestore.collection("students")
                .document(user.get_id())
                .set(user)
                .addOnSuccessListener(executorService, unused -> notifyOnSuccess(
                        user,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null))
                .addOnFailureListener(executorService, this::notifyOnFailure);
    }

    /**
     * Function to update user in the Firestore
     * --------------------------------------------------------------------------------------
     */
    public void updateUser(String _uid, Map<String, Object> data) {
        firestore.collection("students")
                .document(_uid)
                .update(data)
                .addOnSuccessListener(executorService, unused -> notifyOnSuccess(
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null))
                .addOnFailureListener(executorService, this::notifyOnFailure);
    }

    /**
     * Function to add subject to user in the Firestore
     * --------------------------------------------------------------------------------------
     */
    public void addSubjectToUser(String _uid, String data) {
        firestore.collection("students")
                .document(_uid)
                .update("_subjects_ref", FieldValue.arrayUnion(data))
                .addOnSuccessListener(executorService, unused -> notifyOnSuccess(
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null))
                .addOnFailureListener(executorService, this::notifyOnFailure);
    }

    /**
     * Function to remove subject from user in the Firestore
     * --------------------------------------------------------------------------------------
     */
    public void removeSubjectToUser(String _uid, String data) {
        firestore.collection("students")
                .document(_uid)
                .update("_subjects_ref", FieldValue.arrayRemove(data))
                .addOnSuccessListener(executorService, unused -> notifyOnSuccess(
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null))
                .addOnFailureListener(executorService, this::notifyOnFailure);
    }


    /**
     * Function to add submission Id to user in the Firestore
     * --------------------------------------------------------------------------------------
     */
    public void addSubmissionToUser(String _uid, String submissionId) {
        firestore.collection("students")
                .document(_uid)
                .update("_submissions_ref", FieldValue.arrayUnion(submissionId))
                .addOnSuccessListener(executorService, unused -> notifyOnSuccess(
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null))
                .addOnFailureListener(executorService, this::notifyOnFailure);
    }

    /**
     * Function to get subjects list from the cloud firestore
     * --------------------------------------------------------------------------------------
     */
    public void getSubjects(String schoolId, String _class) {
        firestore.collection("subjects")
                .whereEqualTo("_school_ref", schoolId)
                .whereEqualTo("_class", _class)
                .get()
                .addOnSuccessListener(executorService, queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots != null) {
                        List<Subject> subjects = queryDocumentSnapshots.toObjects(Subject.class);
                        notifyOnSuccess(null, null, null, subjects, null, null, null);
                    } else {
                        notifyOnFailure(new Exception("Subjects not found"));
                    }
                })
                .addOnFailureListener(executorService, this::notifyOnFailure);
    }


    /**
     * Function to get assignments list from the cloud firestore
     * --------------------------------------------------------------------------------------
     */
    public void getAssignments(String subjectId) {
        firestore.collection("assignments")
                .whereEqualTo("_subject_ref", subjectId)
                .get()
                .addOnSuccessListener(executorService, new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            List<Assignment> assignments = queryDocumentSnapshots.toObjects(Assignment.class);
                            notifyOnSuccess(null, null, null, null, assignments, null, null);
                        } else {
                            notifyOnFailure(new Exception("Assignments not found"));
                        }
                    }
                })
                .addOnFailureListener(executorService, this::notifyOnFailure);
    }


    /**
     * Function to get assignment from the cloud firestore
     * --------------------------------------------------------------------------------------
     */
    public void getAssignment(String assignment_ref) {
        firestore.collection("assignments")
                .document(assignment_ref)
                .get()
                .addOnSuccessListener(executorService, assignmentSnapshot -> {
                    if (assignmentSnapshot != null) {
                        List<Assignment> assignments = new ArrayList<>();
                        Assignment assignment = assignmentSnapshot.toObject(Assignment.class);

                        // Retrieving Subject
                        if (assignment != null) {
                            String sub_ref = assignment.get_subject_ref();
                            firestore.collection("subjects")
                                    .document(sub_ref)
                                    .get()
                                    .addOnSuccessListener(executorService, subjectSnapshot -> {
                                        Subject subject = null;
                                        if (subjectSnapshot != null) {
                                            subject = subjectSnapshot.toObject(Subject.class);
                                        } else {
                                            subject = new Subject();
                                            subject.set_name("Unknown");
                                        }
                                        assignment.set_subject(subject);
                                        assignments.add(assignment);
                                        notifyOnSuccess(null, null, null, null, assignments, null, null);
                                    })
                                    .addOnFailureListener(executorService, this::notifyOnFailure);
                        } else {
                            notifyOnFailure(new Exception("Something went wrong!!"));
                        }
                    } else {
                        notifyOnFailure(new Exception("Assignments not found"));
                    }
                })
                .addOnFailureListener(executorService, this::notifyOnFailure);
    }


    /**
     * Function to add submissions in the Firestore
     * --------------------------------------------------------------------------------------
     */
    public void addSubmission(Submission submission) {
        firestore.collection("submissions")
                .document(submission.get_id())
                .set(submission)
                .addOnSuccessListener(executorService, unused -> notifyOnSuccess(
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null))
                .addOnFailureListener(executorService, this::notifyOnFailure);
    }


    /**
     * Function to update submission in the Firestore
     * --------------------------------------------------------------------------------------
     */
    public void updateSubmission(String subjectId, String assignmentId, String submissionId, Map<String, Object> data) {
        firestore.collection("subjects")
                .document(subjectId)
                .collection("assignments")
                .document(assignmentId)
                .collection("submissions")
                .document(submissionId)
                .update(data)
                .addOnSuccessListener(executorService, unused -> {
                    notifyOnSuccess(null, null, null, null, null, null, null);
                })
                .addOnFailureListener(executorService, this::notifyOnFailure);
    }


    /**
     * Function to get submission from the Firestore
     * --------------------------------------------------------------------------------------
     */
    public void getSubmissions(String userId) {
        firestore.collection("submissions")
                .whereEqualTo("_student_ref", userId)
                .get()
                .addOnSuccessListener(executorService, queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots != null) {
                        List<Submission> submissions = queryDocumentSnapshots.toObjects(Submission.class);
                        if (submissions.size() > 0) {
                            notifyOnSuccess(null, null, null, null, null, submissions, null);
                        } else {
                            notifyOnFailure(new Exception("Submission not found"));
                        }
                    } else {
                        notifyOnFailure(new Exception("Submission not found"));
                    }
                })
                .addOnFailureListener(executorService, this::notifyOnFailure);
    }


    /**
     * Function to get teacher from the Firestore
     * --------------------------------------------------------------------------------------
     */
    public void getTeacher(String teacherId) {
        firestore.collection("teachers")
                .document(teacherId)
                .get()
                .addOnSuccessListener(executorService, documentSnapshot -> {
                    if (documentSnapshot != null) {
                        List<Teacher> teachers = new ArrayList<>();
                        Teacher teacher = documentSnapshot.toObject(Teacher.class);
                        teachers.add(teacher);
                        notifyOnSuccess(null, null, teachers, null, null, null, null);
                    } else {
                        notifyOnFailure(new Exception("Teacher not found"));
                    }
                })
                .addOnFailureListener(executorService, this::notifyOnFailure);
    }


    /**
     * Function to get teachers
     */
    public void getTeachers(String _school_ref) {
        firestore.collection("teachers")
                .whereArrayContains("_schools_ref", _school_ref)
                .get()
                .addOnSuccessListener(executorService, teachersQuerySnapshot -> {
                    if (teachersQuerySnapshot != null) {
                        List<Teacher> teachers = teachersQuerySnapshot.toObjects(Teacher.class);
                        notifyOnSuccess(null, null, teachers, null, null, null, null);
                    } else {
                        notifyOnFailure(new Exception("Teachers not found"));
                    }
                })
                .addOnFailureListener(executorService, this::notifyOnFailure);
    }

    /**
     * Function to get notices list from the cloud firestore
     * --------------------------------------------------------------------------------------
     */
    public void getNotices(String schoolId, String _class) {
        firestore.collection("notices")
                .whereEqualTo("_school", schoolId)
                .whereArrayContains("_classes", _class)
                .get()
                .addOnSuccessListener(executorService, queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots != null) {
                        List<Notice> notices = queryDocumentSnapshots.toObjects(Notice.class);
                        notifyOnSuccess(null, null, null, null, null, null, notices);
                    } else {
                        notifyOnFailure(new Exception("Subjects not found"));
                    }
                })
                .addOnFailureListener(executorService, this::notifyOnFailure);
    }


    /**
     * Function to get schools list from the cloud firestore
     * --------------------------------------------------------------------------------------
     */
    public void getSchools() {
        firestore.collection("schools")
                .get()
                .addOnSuccessListener(executorService, queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots != null) {
                        List<School> schools = queryDocumentSnapshots.toObjects(School.class);
                        notifyOnSuccess(null, schools, null, null, null, null, null);
                    } else {
                        notifyOnFailure(new Exception("Schools not found"));
                    }
                })
                .addOnFailureListener(executorService, this::notifyOnFailure);
    }

    /**
     * Function to get schools list from the cloud firestore
     * --------------------------------------------------------------------------------------
     */
    public void getSchool(String _schoolId) {
        firestore.collection("schools")
                .document(_schoolId)
                .get()
                .addOnSuccessListener(executorService, schoolSnapshot -> {
                    if (schoolSnapshot != null) {
                        List<School> schools = new ArrayList<>();
                        School school = schoolSnapshot.toObject(School.class);
                        schools.add(school);
                        notifyOnSuccess(
                                null,
                                schools,
                                null,
                                null,
                                null,
                                null,
                                null);
                    } else {
                        notifyOnFailure(new Exception("School not found"));
                    }
                })
                .addOnFailureListener(executorService, this::notifyOnFailure);
    }


    /**
     * Function to notify whether getUser() is success or failure
     * --------------------------------------------------------------------------------------
     */
    private void notifyOnSuccess(User user,
                                 List<School> schools,
                                 List<Teacher> teachers,
                                 List<Subject> subjects,
                                 List<Assignment> assignments,
                                 List<Submission> submissions,
                                 List<Notice> notices) {
        handler.post(() -> {
            callbacks.onSuccess(user, schools, teachers, subjects, assignments, submissions, notices);
        });
    }

    private void notifyOnFailure(Exception e) {
        handler.post(() -> {
            callbacks.onFailure(e);
        });
    }

}
