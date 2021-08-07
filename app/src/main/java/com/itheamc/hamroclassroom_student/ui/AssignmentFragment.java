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

import com.itheamc.hamroclassroom_student.adapters.SliderAdapter;
import com.itheamc.hamroclassroom_student.databinding.FragmentAssignmentBinding;
import com.itheamc.hamroclassroom_student.models.Assignment;
import com.itheamc.hamroclassroom_student.viewmodel.MainViewModel;


public class AssignmentFragment extends Fragment {
    private static final String TAG = "AssignmentFragment";
    private FragmentAssignmentBinding assignmentBinding;
    private MainViewModel viewModel;
    private NavController navController;
    private SliderAdapter sliderAdapter;

    public AssignmentFragment() {
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
        assignmentBinding = FragmentAssignmentBinding.inflate(inflater, container, false);
        return assignmentBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initializing NavController and ViewModel
        navController = Navigation.findNavController(view);
        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);


        // Initializing slider adapter...
        sliderAdapter = new SliderAdapter();
        assignmentBinding.assignmentViewPager.setAdapter(sliderAdapter);

        Assignment assignment = viewModel.getAssignment();
        if (assignment != null) {
            assignmentBinding.setAssignment(assignment);
            if (assignment.get_images().size() > 0) sliderAdapter.submitList(assignment.get_images());
        }

    }

    // Overrided to manage the view destroy
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        assignmentBinding = null;
    }



}