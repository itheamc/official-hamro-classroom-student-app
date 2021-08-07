package com.itheamc.hamroclassroom_student.ui;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.os.HandlerCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.storage.UploadTask;
import com.itheamc.hamroclassroom_student.R;
import com.itheamc.hamroclassroom_student.adapters.ImageAdapter;
import com.itheamc.hamroclassroom_student.callbacks.FirestoreCallbacks;
import com.itheamc.hamroclassroom_student.callbacks.ImageCallbacks;
import com.itheamc.hamroclassroom_student.callbacks.StorageCallbacks;
import com.itheamc.hamroclassroom_student.databinding.FragmentSubmissionBinding;
import com.itheamc.hamroclassroom_student.handlers.FirestoreHandler;
import com.itheamc.hamroclassroom_student.handlers.StorageHandler;
import com.itheamc.hamroclassroom_student.models.Assignment;
import com.itheamc.hamroclassroom_student.models.Notice;
import com.itheamc.hamroclassroom_student.models.School;
import com.itheamc.hamroclassroom_student.models.Subject;
import com.itheamc.hamroclassroom_student.models.Submission;
import com.itheamc.hamroclassroom_student.models.Teacher;
import com.itheamc.hamroclassroom_student.models.User;
import com.itheamc.hamroclassroom_student.utils.IdGenerator;
import com.itheamc.hamroclassroom_student.utils.ImageUtils;
import com.itheamc.hamroclassroom_student.utils.NotifyUtils;
import com.itheamc.hamroclassroom_student.utils.ViewUtils;
import com.itheamc.hamroclassroom_student.viewmodel.MainViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class SubmissionFragment extends Fragment implements StorageCallbacks, FirestoreCallbacks, ImageCallbacks {
    private static final String TAG = "SubmissionFragment";
    private FragmentSubmissionBinding submissionBinding;
    private MainViewModel viewModel;
    private NavController navController;

    /*
    Image Picker Related
     */
    private ActivityResultLauncher<Intent> imagePickerResultLauncher;
    private List<Uri> imagesUri;
    private ImageAdapter imageAdapter;

    /*
   Integer to store the uploaded image qty
    */
    int uploadCount = 0;
    /*
   List to store the uploaded image url
    */
    private List<String> imagesList;

    /*
    TextInputLayout
     */
    private TextInputLayout textInputLayout;

    /*
    EditTexts
     */
    private EditText textEdittext;

    /*
    Strings
     */
    private String _submissionId;
    private String _text = "";

    /*
    Boolean
     */
    private boolean is_uploading = false;   // To handle the image remove
    private boolean is_submitted = false;   // To handle the submission id update in user


    // Constructor
    public SubmissionFragment() {
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
        submissionBinding = FragmentSubmissionBinding.inflate(inflater, container, false);
        return submissionBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initializing NavController and ViewModel
        navController = Navigation.findNavController(view);
        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        // Initializing Image adapter and setting to the recycler view
        imageAdapter = new ImageAdapter(this);
        submissionBinding.submissionRecyclerView.setAdapter(imageAdapter);

        // Initializing InputLayout and Edittext
        textInputLayout = submissionBinding.textInputLayout;

        textEdittext = textInputLayout.getEditText();

        // Initializing imageList
        imagesList = new ArrayList<>();

        // Activity Result launcher to listen the result of the multi image picker
        imagePickerResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), result -> {
                    if (submissionBinding == null) return;
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        if (data == null) return;

                        ViewUtils.hideViews(submissionBinding.imagePickerButton);     // To hide the image picker
                        imagesUri = new ArrayList<>();

                        // Get the Images from data
                        ClipData mClipData = data.getClipData();
                        if (mClipData != null) {
                            int count = mClipData.getItemCount();
                            for (int i = 0; i < count; i++) {
                                // adding imageUri in array
                                Uri imageUri = mClipData.getItemAt(i).getUri();
                                imagesUri.add(imageUri);
                            }

                            submitImagesToImageAdapter();   // Submitting image to adapter
                            return;
                        }

                        Uri imageUri = data.getData();
                        imagesUri.add(imageUri);
                        submitImagesToImageAdapter();   // Submitting image to adapter

                    } else {
                        NotifyUtils.logDebug(TAG, "Image Picker Closed WithOut Picking Any Images");
                    }
                });

        /*
        Setting OnClickListener
         */
        submissionBinding.imagePickerButton.setOnClickListener(v -> showImagePicker());
        submissionBinding.submitButton.setOnClickListener(v -> {
            if (!isInputsValid() && (imagesUri == null || imagesUri.size() < 1)) {
                if (getContext() != null) NotifyUtils.showToast(getContext(), "Please write your views in the text box or pick images");
                return;
            }

            _submissionId = IdGenerator.generateId();
            ViewUtils.showProgressBar(submissionBinding.progressBarContainer);
            ViewUtils.disableViews(submissionBinding.submitButton, textInputLayout);

            // If Images are attached then
            if (imagesUri != null && imagesUri.size() > 0) {
                handleImageUpload();
                return;
            }

            // If images are not attached and something written on the text box then
            storeOnFirestore();
        });


    }


    /*
    Function to submit data to image adapter
     */
    private void submitImagesToImageAdapter() {
        if (imagesUri != null) imageAdapter.submitList(imagesUri);
    }


    /**
     * --------------------------------------------------------------------------
     * Function to handle image upload to cloud storage
     * It will be triggered continuously until all the images will be uploaded
     */
    private void handleImageUpload() {
        Assignment assignment = viewModel.getAssignment();
        Bitmap bitmap = ImageUtils.getInstance(getActivity()).getBitmap(imagesUri.get(uploadCount));
        if (bitmap != null) {
            if (!is_uploading) is_uploading = true;
            StorageHandler.getInstance(this)
                    .uploadImage(bitmap,
                            "image" + "-" + (uploadCount + 1) + ".jpg",
                            assignment.get_subject(),
                            assignment.get_id(),
                            _submissionId);
        }
    }

    /**
     * --------------------------------------------------------------------------
     * Function to handle Firestore upload
     * It will bi triggered only after all the images uploaded
     */
    private void storeOnFirestore() {
        submissionBinding.uploadedProgress.setText(R.string.finalizing_uploads);
        User user = viewModel.getUser();
        // Creating new assignment object
        Submission submission = new Submission(
                _submissionId,
                imagesList,
                new ArrayList<>(),
                _text,
                Arrays.asList(user.get_id(), user.get_name()),
                new Date(),
                new Date(),
                false,
                "No Comment"
        );


        Assignment ass = viewModel.getAssignment();
        if (ass != null) {
            String assId = ass.get_id();
            String subId = ass.get_subject();
            FirestoreHandler.getInstance(this)
                    .addSubmission(subId, assId, submission);
        }

    }


    /*
    Function to update submission id on the User._submissions list
     */
    private void updateSubmissionIdToUser() {
        Assignment ass = viewModel.getAssignment();
        if (ass == null) {
            ViewUtils.hideProgressBar(submissionBinding.progressBarContainer);
            ViewUtils.enableViews(submissionBinding.submitButton, textInputLayout);
            return;
        }
        User user = viewModel.getUser();
        FirestoreHandler.getInstance(this).addSubmissionToUser(user.get_id(), ass.get_subject() + "___" + ass.get_id());
    }


    /**
     * ----------------------------------------------------------------------------
     * Function to display the progress update in textview while loading
     */
    private void updateUploadProgress(double progress) {
        if (submissionBinding == null) return;

        String message = String.format(Locale.ROOT, "Uploading (%d/%d) Images", uploadCount + 1, imagesUri.size());
        HandlerCompat.createAsync(Looper.getMainLooper())
                .post(() -> {
                    submissionBinding.uploadedProgress
                            .setText(message);
                });
    }

    /**
     * -----------------------------------------------------------------------------
     * Function to start the image picker intent
     */
    private void showImagePicker() {
        // initialising intent
        Intent intent = new Intent();

        // setting type to select to be image
        intent.setType("image/*");

        // allowing multiple image to be selected
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        imagePickerResultLauncher.launch(Intent.createChooser(intent, "Pick Images"));
    }

    /**
     * -----------------------------------------------------------------------------
     * Function to verify inputs
     */
    private boolean isInputsValid() {
        if (textEdittext != null) {
            _text = textEdittext.getText().toString().trim();
        } else {
            _text = "";
        }
        return !TextUtils.isEmpty(_text);
    }

    /**
     * ---------------------------------------------------------------------------
     * Function to make edittext clear
     */
    private void clearAllInputs() {
        if (submissionBinding == null) return;
        ViewUtils.clearEditTexts(textEdittext);
        if (imagesUri != null) {
            imagesUri.clear();
            imageAdapter.submitList(new ArrayList<>());
        }
        ViewUtils.visibleViews(submissionBinding.imagePickerButton);    // To Show the image picker button
    }


    /**
     * -------------------------------------------------------------------------
     * These are the methods implemented from the StorageCallbacks
     * -------------------------------------------------------------------------
     */

    @Override
    public void onSuccess(String imageUrl) {
        if (imagesList == null) imagesList = new ArrayList<>();
        imagesList.add(imageUrl);
        uploadCount += 1;
        if (uploadCount < imagesUri.size()) {
            handleImageUpload();
            return;
        }

        // Storing to cloud Firestore
        storeOnFirestore();
    }

    @Override
    public void onFailure(Exception e) {
        if (submissionBinding == null) return;
        if (getContext() != null) NotifyUtils.showToast(getContext(), "Upload Failed");
        is_uploading = false;
        is_submitted = false;
        uploadCount = 0;
        ViewUtils.hideProgressBar(submissionBinding.progressBarContainer);
        ViewUtils.enableViews(submissionBinding.submitButton, textInputLayout);

    }

    @Override
    public void onCanceled() {
        if (submissionBinding == null) return;
        if (getContext() != null) NotifyUtils.showToast(getContext(), "You canceled the upload!");
        is_uploading = false;
        ViewUtils.hideProgressBar(submissionBinding.progressBarContainer);
    }

    @Override
    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
        if (submissionBinding == null) return;
        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
        NotifyUtils.logDebug(TAG, "Upload is " + progress + "% done");
        updateUploadProgress(progress);
    }

    /**
     * -------------------------------------------------------------------
     * This method is implemented from the ImageViewCallback
     * -------------------------------------------------------------------
     */

    @Override
    public void onRemove(int _position) {
        if (is_uploading) return;
        imagesUri.remove(_position);
        imageAdapter.notifyItemRemoved(_position);
        NotifyUtils.logDebug(TAG, imagesUri.toString());
        if (imagesUri.size() == 0) ViewUtils.visibleViews(submissionBinding.imagePickerButton);
    }

    /**
     * ------------------------------------------------------------------------------------------
     * This method is implemented from the FirestoreCallbacks
     * -  Due to the same name and same arguments onFailure(Exception e) there is a only one
     * on failure method for StorageCallbacks and FirestoreCallbacks
     * - If something went wrong above OnFailure(Exception e) will be triggered
     * ------------------------------------------------------------------------------------------
     */
    @Override
    public void onSuccess(User user, Teacher teacher, School school, List<School> schools, List<Subject> subjects, List<Assignment> assignments, Submission submissions, List<Notice> notices) {
        if (submissionBinding == null) return;

        if (!is_submitted) {
            is_submitted = true;
            updateSubmissionIdToUser();
            return;
        }

        ViewUtils.hideProgressBar(submissionBinding.progressBarContainer);
        ViewUtils.enableViews(submissionBinding.submitButton, textInputLayout);
        if (getContext() != null) NotifyUtils.showToast(getContext(), "Added Successfully");
        uploadCount = 0;
        is_uploading = false;
        is_submitted = false;
        clearAllInputs();

    }

    /*
    Overrided function to view destroy
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        submissionBinding = null;
    }
}