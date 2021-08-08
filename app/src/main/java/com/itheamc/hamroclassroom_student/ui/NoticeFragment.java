package com.itheamc.hamroclassroom_student.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.itheamc.hamroclassroom_student.R;
import com.itheamc.hamroclassroom_student.databinding.FragmentNoticeBinding;
import com.itheamc.hamroclassroom_student.models.Notice;
import com.itheamc.hamroclassroom_student.viewmodel.MainViewModel;


public class NoticeFragment extends Fragment {
    private static final String TAG = "NoticeFragment";
    private FragmentNoticeBinding noticeBinding;
    private MainViewModel viewModel;



    public NoticeFragment() {
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
        noticeBinding = FragmentNoticeBinding.inflate(inflater, container, false);
        return noticeBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initializing NavController and ViewModel
        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        Notice notice = viewModel.getNotice();
        if (notice != null) noticeBinding.setNotice(notice);

    }
}