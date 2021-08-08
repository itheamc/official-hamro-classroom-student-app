package com.itheamc.hamroclassroom_student.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.itheamc.hamroclassroom_student.adapters.SubjectAdapter;
import com.itheamc.hamroclassroom_student.callbacks.FirestoreCallbacks;
import com.itheamc.hamroclassroom_student.callbacks.SubjectCallbacks;
import com.itheamc.hamroclassroom_student.databinding.FragmentSubjectsBinding;
import com.itheamc.hamroclassroom_student.handlers.FirestoreHandler;
import com.itheamc.hamroclassroom_student.models.Assignment;
import com.itheamc.hamroclassroom_student.models.Notice;
import com.itheamc.hamroclassroom_student.models.School;
import com.itheamc.hamroclassroom_student.models.Subject;
import com.itheamc.hamroclassroom_student.models.Submission;
import com.itheamc.hamroclassroom_student.models.Teacher;
import com.itheamc.hamroclassroom_student.models.User;
import com.itheamc.hamroclassroom_student.utils.LocalStorage;
import com.itheamc.hamroclassroom_student.utils.NotifyUtils;
import com.itheamc.hamroclassroom_student.utils.ViewUtils;
import com.itheamc.hamroclassroom_student.viewmodel.MainViewModel;

import java.util.ArrayList;
import java.util.List;


public class SubjectsFragment extends Fragment implements SubjectCallbacks, FirestoreCallbacks {
    private static final String TAG = "SubjectsFragment";
    private FragmentSubjectsBinding subjectsBinding;
    private NavController navController;
    private MainViewModel viewModel;
    private SubjectAdapter subjectAdapter;

    private String _message = "";


    public SubjectsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        subjectsBinding = FragmentSubjectsBinding.inflate(inflater, container, false);
        return subjectsBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initializing NavController and ViewModel
        navController = Navigation.findNavController(view);
        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        subjectAdapter = new SubjectAdapter(this);
        subjectsBinding.subjectsRecyclerView.setAdapter(subjectAdapter);

        // Handling back button icon
        subjectsBinding.backButton.setOnClickListener(v -> {
            navController.popBackStack();
        });

        // Implementing swipe refresh listener
        subjectsBinding.subjectsSwipeRefreshLayout.setOnRefreshListener(() -> {
            viewModel.setSubjects(null);
            passDataToRecyclerView();
        });

        // Calling function to processed subject lists
        passDataToRecyclerView();


    }

    /**
     * ----------------------------------------------------------------------
     * Function to process the subjects data
     */
    private void passDataToRecyclerView() {
        List<Subject> subjects = viewModel.getSubjects();

        if (subjects == null) {
            retrieveSubjects();
            return;
        }

        subjectAdapter.submitList(subjects);
    }

    /**
     * Function to get the subjects list according to the student's class and school
     */
    private void retrieveSubjects() {
        User user = viewModel.getUser();
        if (user == null) {
            retrieveUser();
            return;
        }
        FirestoreHandler.getInstance(this).getSubjects(user.get_school(), user.get_class());
        ViewUtils.showProgressBar(subjectsBinding.subjectsOverlayLayLayout);
    }

    /**
     * Function to retrieve user
     */
    private void retrieveUser() {
        String userId = null;
        if (getActivity() != null) userId = LocalStorage.getInstance(getActivity()).getUserId();
        if (userId != null) {
            FirestoreHandler.getInstance(this).getUser(userId);
            ViewUtils.showProgressBar(subjectsBinding.subjectsOverlayLayLayout);
        }
    }



    /**
     * -----------------------------------------------------------------------------
     * These are the methods overrided from the SubjectCallbacks
     * @param _position - It is the position of the clicked item
     */
    @Override
    public void onClick(int _position) {

    }

    @Override
    public void onJoinClassClick(int _position) {

    }

    @Override
    public void onCopyClick(int _position) {

    }

    @Override
    public void onLongClick(int _position) {

    }

    @Override
    public void onAddClick(int _position) {
        handleAddRemove(_position);
    }

    /**
     * Function to handle onAddClick() event
     */
    private void handleAddRemove(int _position) {
        List<Subject> subjects = viewModel.getSubjects();
        if (subjects == null) return;

        Subject subject = subjects.get(_position);
        User user = viewModel.getUser();
        List<String> subjectIds = user.get_subjects();

        if (subject.is_added()) {
            subject.set_added(false);
            subjectIds.remove(subject.get_id());
        } else {
            subject.set_added(true);
            subjectIds.add(subject.get_id());

        }

        // Updating to the view model
        user.set_subjects(subjectIds);
        viewModel.setUser(user);
        viewModel.modifySubjectItems(subject);

        if (subject.is_added()) {
            FirestoreHandler.getInstance(this).addSubjectToUser(user.get_id(), subject.get_id());
            _message = "Added";
            return;
        }
        FirestoreHandler.getInstance(this).removeSubjectToUser(user.get_id(), subject.get_id());
        _message = "Removed";
    }


    /**
     * -------------------------------------------------------------------------
     * These are the methods overrided from the FirestoreCallbacks
     * @param user - it is the instance of the user
     * @param teacher - it is the instance of the teacher
     * @param school - it is the instance of the school
     * @param schools - it is the instance of the List<School>
     * @param subjects - it is the instance of the List<Subject>
     * @param assignments - it is the instance of the List<Assignment>
     * @param submissions - it is the instance of the List<Submission>
     * @param notices - it is the instance of the List<Notice>
     */

    @Override
    public void onSuccess(User user, Teacher teacher, School school, List<School> schools, List<Subject> subjects, List<Assignment> assignments, Submission submissions, List<Notice> notices) {
        if (subjectsBinding == null) return;

        // If User retrieved from the Firestore
        if (user != null) {
            viewModel.setUser(user);
            retrieveSubjects();
            return;
        }

        // If Subjects retrieve from the firestore
        if (subjects != null) {
            ViewUtils.hideProgressBar(subjectsBinding.subjectsOverlayLayLayout);
            ViewUtils.handleRefreshing(subjectsBinding.subjectsSwipeRefreshLayout);
            if (!subjects.isEmpty()) {
                processedSubjects(subjects);
            }
            return;
        }

        ViewUtils.hideProgressBar(subjectsBinding.subjectsOverlayLayLayout);
        ViewUtils.handleRefreshing(subjectsBinding.subjectsSwipeRefreshLayout);
        NotifyUtils.showToast(getContext(), _message);
        passDataToRecyclerView();
    }


    /**
     * ----------------------------------------------------------------------
     * Function to process the subjects data
     */
    private void processedSubjects(List<Subject> subjects) {
        User user = viewModel.getUser();
        List<String> subject_ids = null;
        List<Subject> processedSubjects = new ArrayList<>();

        if (user != null) {
            subject_ids = user.get_subjects();
        }

        for (Subject subject: subjects) {
            subject.set_added(Subject.isAlreadyAdded(subject_ids, subject.get_id()));
            processedSubjects.add(subject);
        }

        viewModel.setSubjects(processedSubjects);
        passDataToRecyclerView();
    }

    @Override
    public void onFailure(Exception e) {
        if (subjectsBinding == null) return;
        if (getContext() != null) NotifyUtils.showToast(getContext(), e.getMessage());
        ViewUtils.hideProgressBar(subjectsBinding.subjectsOverlayLayLayout);
    }
}