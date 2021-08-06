package com.itheamc.hamroclassroom_student.handlers;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.core.os.HandlerCompat;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.itheamc.hamroclassroom_student.callbacks.StorageCallbacks;

import java.io.ByteArrayOutputStream;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class StorageHandler {
    private static final String TAG = "StorageHandler";
    private static StorageHandler instance;
    private FirebaseStorage storage;
    private UploadTask imageUploadTask;
    private StorageCallbacks storageCallback;
    private final ExecutorService executorService;
    private Handler handler;

    /**
     * ----------------------------------------------------------------------
     * ---------------------- Constructor for Cloud Storage------------------
     *
     * @param storageCallback -> instance of the StorageCallbacks
     */

    // Constructor
    public StorageHandler(@NonNull StorageCallbacks storageCallback) {
        this.storage = FirebaseStorage.getInstance();
        this.storageCallback = storageCallback;
        this.executorService = Executors.newFixedThreadPool(4);
        this.handler = HandlerCompat.createAsync(Looper.getMainLooper());
    }

    // Instance for Cloud Storage
    public static StorageHandler getInstance(@NonNull StorageCallbacks storageCallback) {
        if (instance == null) {
            instance = new StorageHandler(storageCallback);
        }
        return instance;
    }


    /**
     * -------------------------------------------------------------------------------------------
     * -------------------------------------------------------------------------------------------
     * -------------------------------------------------------------------------------------------
     * ----------------------- THESE ARE THE FUNCTIONS FOR CLOUD STORAGE--------------------------
     * -------------------------------------------------------------------------------------------
     */

    /**
     * ---------------------------------------------
     * Function to upload image on the cloud storage
     */
    public void uploadImage(Bitmap bitmap, String imageName, String subjectId, String assignmentId, String submissionId) {
        StorageReference storageReference = storage.getReference();
        StorageReference imageRef = storageReference.child(subjectId).child(assignmentId).child(submissionId).child(imageName);
        byte[] imageBytes = getByteArray(bitmap);

        imageUploadTask = imageRef.putBytes(imageBytes);
        imageUploadTask.addOnCompleteListener(executorService, task -> task.continueWithTask(task1 -> {
            if (!task.isSuccessful()) {
                throw Objects.requireNonNull(task.getException());
            }

            // Continue with the task to get the download URL
            return imageRef.getDownloadUrl();
        }).addOnCompleteListener(task2 -> {
            if (task2.isSuccessful()) {
                Uri downloadUri = task2.getResult();
                String url = downloadUri.toString();
                notifySuccess(url);
            } else {
                notifyFailure(task2.getException());
            }
        }))
                .addOnFailureListener(executorService, this::notifyFailure)
                .addOnCanceledListener(executorService, this::notifyCancel)
                .addOnProgressListener(executorService, this::notifyProgress);
    }

    /**
     * -------------------------------------------
     * Function to pause, resume and cancel the upload
     */
    public boolean pauseUpload() {
        return imageUploadTask.pause();
    }

    public boolean resumeUpload() {
        return imageUploadTask.resume();
    }

    public boolean cancelUpload() {
        return imageUploadTask.cancel();
    }


    /**
     * ---------------------------------
     * Function to compress bitmap image
     */
    private byte[] getByteArray(Bitmap bitmap) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, out);
        return out.toByteArray();
    }


    /**
     * ------------------------------------------------------------------------------------
     * Function to notify the image upload status
     * - success
     * - failure
     * - canceled
     * - progress changed
     */
    private void notifySuccess(String imageUrl) {
        handler.post(() -> {
            storageCallback.onSuccess(imageUrl);
        });
    }

    private void notifyFailure(Exception e) {
        handler.post(() -> {
            storageCallback.onFailure(e);
        });
    }

    private void notifyCancel() {
        handler.post(() -> {
            storageCallback.onCanceled();
        });
    }

    private void notifyProgress(UploadTask.TaskSnapshot taskSnapshot) {
        handler.post(() -> {
            storageCallback.onProgress(taskSnapshot);
        });
    }
}
