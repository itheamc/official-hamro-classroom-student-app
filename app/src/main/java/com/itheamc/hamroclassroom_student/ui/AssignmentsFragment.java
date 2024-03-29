package com.itheamc.hamroclassroom_student.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.itheamc.hamroclassroom_student.R;
import com.itheamc.hamroclassroom_student.adapters.AssignmentAdapter;
import com.itheamc.hamroclassroom_student.callbacks.AssignmentCallbacks;
import com.itheamc.hamroclassroom_student.callbacks.FirestoreCallbacks;
import com.itheamc.hamroclassroom_student.databinding.FragmentAssignmentsBinding;
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


public class AssignmentsFragment extends Fragment implements FirestoreCallbacks, AssignmentCallbacks {
    private static final String TAG = "AssignmentsFragment";
    private FragmentAssignmentsBinding assignmentsBinding;
    private MainViewModel viewModel;
    private NavController navController;
    private AssignmentAdapter assignmentAdapter;

    /*
    Lists
     */
    private List<Subject> filteredSubjects;
    private List<Assignment> listOfAssignments;
    private int position = 0;

    public AssignmentsFragment() {
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
        assignmentsBinding = FragmentAssignmentsBinding.inflate(inflater, container, false);
        return assignmentsBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initializing NavController and ViewModel
        navController = Navigation.findNavController(view);
        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        // Initializing AssignmentAdapter and setting to recyclerview
        assignmentAdapter = new AssignmentAdapter(this);
        assignmentsBinding.assignmentsRecyclerView.setAdapter(assignmentAdapter);

        // Initializing filteredSubjects
        filteredSubjects = new ArrayList<>();
        listOfAssignments = new ArrayList<>();


        // Setting swipe and refresh layout
        assignmentsBinding.assignmentsSwipeRefreshLayout.setOnRefreshListener(() -> {
            filteredSubjects = new ArrayList<>();
            listOfAssignments = new ArrayList<>();
            viewModel.setAllAssignments(null);
            position = 0;
            ViewUtils.hideViews(assignmentsBinding.noAssignmentsLayout);
            checksUser();
        });

        // Handling back button
        assignmentsBinding.backButton.setOnClickListener(v -> {
            navController.popBackStack();
        });



        // Checks User for assignment extraction
        checksUser();
    }


    /**
     * Function to checks whether the user is already stored in viewmodel or not
     */
    private void checksUser() {
        User user = viewModel.getUser();
        if (user == null) {
            if (getActivity() != null) {
                FirestoreHandler.getInstance(this).getUser(LocalStorage.getInstance(getActivity()).getUserId());
                ViewUtils.showProgressBar(assignmentsBinding.assignmentsOverlayLayLayout);
            }
            return;
        }

        retrieveSubjects();
    }

    /**
     * Function to get the subjects list according to the student's class and school
     */
    private void retrieveSubjects() {
        User user = viewModel.getUser();
        List<Subject> subjects = viewModel.getSubjects();
        if (subjects != null && !subjects.isEmpty()) {
            // Getting filtered subjects list from the available subjects
            filteredSubjects = Subject.filterSubjects(subjects);
            if (!filteredSubjects.isEmpty()) {
                retrieveAssignments();
                ViewUtils.showProgressBar(assignmentsBinding.assignmentsOverlayLayLayout);
                return;
            }

           viewsOnCompletedOrFailure();     // ProgressBar, NoItemView and Stop Refreshing
            return;
        }

        FirestoreHandler.getInstance(this).getSubjects(user.get_school_ref(), user.get_class());
        ViewUtils.showProgressBar(assignmentsBinding.assignmentsOverlayLayLayout);
    }


    /*
    Handle views
     */
    private void viewsOnCompletedOrFailure() {
        ViewUtils.visibleViews(assignmentsBinding.noAssignmentsLayout);
        ViewUtils.hideProgressBar(assignmentsBinding.assignmentsOverlayLayLayout);
        ViewUtils.handleRefreshing(assignmentsBinding.assignmentsSwipeRefreshLayout);
    }


    /**
     * -----------------------------------------------------------------------------
     * These are the methods implemented from the AssignmentCallbacks
     *
     * @param _position - It is the position of the clicked item in the list
     */
    @Override
    public void onSubmissionsClick(int _position) {
        List<Assignment> lists = viewModel.getAllAssignments();
        Assignment ass = null;
        if (lists != null && !lists.isEmpty()) {
            ass = lists.get(_position);
        }

        if (ass != null) viewModel.setAssignment(ass);
        navController.navigate(R.id.action_assignmentsFragment_to_submitFragment);
    }

    @Override
    public void onClick(int _position) {
        List<Assignment> lists = viewModel.getAllAssignments();
        Assignment ass = null;
        if (lists != null && !lists.isEmpty()) {
            ass = lists.get(_position);
        }

        if (ass != null) viewModel.setAssignment(ass);
        navController.navigate(R.id.action_assignmentsFragment_to_assignmentFragment);
    }

    @Override
    public void onLongClick(int _position) {

    }

    /**
     * -----------------------------------------------------------------------------
     * These are the methods implemented from the FirestoreCallbacks
     *
     * @param user        - It is the user object
     * @param teacher     - It is the teacher object
     * @param school      - It is the school object
     * @param schools     - It is the  list of schools
     * @param subjects    - It is the  list of subjects
     * @param assignments - It is the  list of assignments
     * @param submissions - It is the  list of submissions
     * @param notices     - It is the  list of notices
     */
    @Override
    public void onSuccess(User user, Teacher teacher, School school, List<School> schools, List<Subject> subjects, List<Assignment> assignments, List<Submission> submissions, List<Notice> notices) {
        if (assignmentsBinding == null) return;

        // If User retrieved from the Firestore
        if (user != null) {
            viewModel.setUser(user);
            retrieveSubjects();
            return;
        }

        // If Subjects retrieve from the firestore
        if (subjects != null) {
            if (!subjects.isEmpty()) {
                handleSubjects(subjects);
                return;
            }
            viewsOnCompletedOrFailure();     // ProgressBar, NoItemView and Stop Refreshing
            return;
        }

        // If Assignment is retrieved
        if (assignments != null) {
            if (!assignments.isEmpty()) {
                for (Assignment a: assignments) {
                    a.set_subject(filteredSubjects.get(position));
                    listOfAssignments.add(a);
                }
            }

            position += 1;
            retrieveAssignments();
            return;
        }

        ViewUtils.hideProgressBar(assignmentsBinding.assignmentsOverlayLayLayout);
        ViewUtils.handleRefreshing(assignmentsBinding.assignmentsSwipeRefreshLayout);
    }

    @Override
    public void onFailure(Exception e) {
        if (assignmentsBinding == null) return;
        viewsOnCompletedOrFailure();     // ProgressBar, NoItemView and Stop Refreshing
        if (e.getMessage() == null) return;
        if (getContext() != null) NotifyUtils.showToast(getContext(), e.getMessage());
    }


    /**
     * ----------------------------------------------------------------------
     * Function to handle subjects data
     */
    private void handleSubjects(List<Subject> subjects) {
        User u = viewModel.getUser();
        List<Subject> processedSubjects = Subject.processedSubjects(subjects, u);

        if (!processedSubjects.isEmpty()) {
            viewModel.setSubjects(processedSubjects);
            // Getting filtered subjects list from the available subjects
            filteredSubjects = Subject.filterSubjects(processedSubjects);

            if (!filteredSubjects.isEmpty()) {
                retrieveAssignments();
            } else {
                viewsOnCompletedOrFailure();     // ProgressBar, NoItemView and Stop Refreshing
            }
            return;
        }

        viewsOnCompletedOrFailure();     // ProgressBar, NoItemView and Stop Refreshing
    }

    /**
     * Function to retrieve assignments
     */
    private void retrieveAssignments() {
        if (position < filteredSubjects.size()) {
            String id = filteredSubjects.get(position).get_id();
            FirestoreHandler.getInstance(this).getAssignments(id);
            return;
        }

        position = 0;
        ViewUtils.hideProgressBar(assignmentsBinding.assignmentsOverlayLayLayout);
        ViewUtils.handleRefreshing(assignmentsBinding.assignmentsSwipeRefreshLayout);

        if (listOfAssignments == null || listOfAssignments.isEmpty()) {
            ViewUtils.visibleViews(assignmentsBinding.noAssignmentsLayout);
            return;
        }

        assignmentAdapter.submitList(listOfAssignments);
        viewModel.setAllAssignments(listOfAssignments);
    }


    // Overriding function to handle the destroy of view

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        assignmentsBinding = null;
    }
}