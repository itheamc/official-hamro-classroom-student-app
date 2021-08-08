package com.itheamc.hamroclassroom_student.handlers;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.core.os.HandlerCompat;

import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.itheamc.hamroclassroom_student.callbacks.FirestoreCallbacks;
import com.itheamc.hamroclassroom_student.models.Assignment;
import com.itheamc.hamroclassroom_student.models.Notice;
import com.itheamc.hamroclassroom_student.models.School;
import com.itheamc.hamroclassroom_student.models.Subject;
import com.itheamc.hamroclassroom_student.models.Submission;
import com.itheamc.hamroclassroom_student.models.Teacher;
import com.itheamc.hamroclassroom_student.models.User;

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
                        notifyOnSuccess(user, null, null, null, null, null, null, null);
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
                .update("_subjects", FieldValue.arrayUnion(data))
                .addOnSuccessListener(executorService, unused -> notifyOnSuccess(
                        null,
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
                .update("_subjects", FieldValue.arrayRemove(data))
                .addOnSuccessListener(executorService, unused -> notifyOnSuccess(
                        null,
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
                .update("_submissions", FieldValue.arrayUnion(submissionId))
                .addOnSuccessListener(executorService, unused -> notifyOnSuccess(
                        null,
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
                .whereArrayContains("_school", schoolId)
                .whereEqualTo("_class", _class)
                .get()
                .addOnSuccessListener(executorService, queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots != null) {
                        List<Subject> subjects = queryDocumentSnapshots.toObjects(Subject.class);
                        notifyOnSuccess(null, null, null, null, subjects, null, null, null);
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
        firestore.collection("subjects")
                .document(subjectId)
                .collection("assignments")
                .get()
                .addOnSuccessListener(executorService, queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots != null) {
                        List<Assignment> assignments = queryDocumentSnapshots.toObjects(Assignment.class);
                        notifyOnSuccess(null, null, null, null, null, assignments, null, null);
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
    public void addSubmission(String subjectId, String assignmentId, Submission submission) {
        firestore.collection("subjects")
                .document(subjectId)
                .collection("assignments")
                .document(assignmentId)
                .collection("submissions")
                .document(submission.get_id())
                .set(submission)
                .addOnSuccessListener(executorService, unused -> notifyOnSuccess(
                        null,
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
                    notifyOnSuccess(null, null, null, null, null, null, null, null);
                })
                .addOnFailureListener(executorService, this::notifyOnFailure);
    }


    /**
     * Function to get submission from the Firestore
     * --------------------------------------------------------------------------------------
     */
    public void getSubmission(String subjectId, String assignmentId, String userId) {
        firestore.collection("subjects")
                .document(subjectId)
                .collection("assignments")
                .document(assignmentId)
                .collection("submissions")
                .whereArrayContains("_submitted_by", userId)
                .get()
                .addOnSuccessListener(executorService, queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots != null) {
                        List<Submission> submissions = queryDocumentSnapshots.toObjects(Submission.class);
                        if (submissions.size() > 0) {
                            notifyOnSuccess(null, null, null, null, null, null, submissions.get(0), null);
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
                .whereEqualTo("_id", teacherId)
                .get()
                .addOnSuccessListener(executorService, queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots != null) {
                        List<Teacher> teachers = queryDocumentSnapshots.toObjects(Teacher.class);
                        if (teachers.size() > 0) {
                            notifyOnSuccess(null, teachers.get(0), null, null, null, null, null, null);
                        } else {
                            notifyOnFailure(new Exception("Teacher not found"));
                        }
                    } else {
                        notifyOnFailure(new Exception("Teacher not found"));
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
                        notifyOnSuccess(null, null, null, null, null, null, null, notices);
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
                        notifyOnSuccess(null, null, null, schools, null, null, null, null);
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
                .whereEqualTo("_id", _schoolId)
                .get()
                .addOnSuccessListener(executorService, queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots != null) {
                        List<School> schools = queryDocumentSnapshots.toObjects(School.class);
                        if (schools.isEmpty()) {
                            notifyOnFailure(new Exception("Schools not found"));
                        } else {
                            notifyOnSuccess(null, null, schools.get(0), null, null, null, null, null);
                        }
                    } else {
                        notifyOnFailure(new Exception("Schools not found"));
                    }
                })
                .addOnFailureListener(executorService, this::notifyOnFailure);
    }


    /**
     * Function to notify whether getUser() is success or failure
     * --------------------------------------------------------------------------------------
     */
    private void notifyOnSuccess(User user,
                                 Teacher teacher,
                                 School school,
                                 List<School> schools,
                                 List<Subject> subjects,
                                 List<Assignment> assignments,
                                 Submission submissions,
                                 List<Notice> notices) {
        handler.post(() -> {
            callbacks.onSuccess(user, teacher, school, schools, subjects, assignments, submissions, notices);
        });
    }

    private void notifyOnFailure(Exception e) {
        handler.post(() -> {
            callbacks.onFailure(e);
        });
    }

}
