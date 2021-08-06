package com.itheamc.hamroclassroom_student.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;

import java.io.IOException;
import java.io.InputStream;

public class ImageUtils {
    private static final String TAG = "ImageUtility";
    private static ImageUtils instance;
    private final Activity activity;

    // Constructor
    public ImageUtils(Activity activity) {
        this.activity = activity;
    }

    // Instance
    public static ImageUtils getInstance(Activity activity) {
        if (instance == null) {
            instance = new ImageUtils(activity);
        }
        return instance;
    }


    /**
     * -----------------------------------------------------------------------------------
     * Function to convert uri to bitmap
     */
    public Bitmap getBitmap(Uri uri) {

        // If getActivity() is null
        if (activity == null) return null;

        Bitmap bitmap = null;
        ContentResolver contentResolver = activity.getContentResolver();

        // If Build.VERSION.SDK_INT is greater than or equal to P
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            ImageDecoder.Source source = ImageDecoder.createSource(contentResolver, uri);
            try {
                bitmap = ImageDecoder.decodeBitmap(source);
            } catch (IOException e) {
                NotifyUtils.logError(TAG, "getBitmap()", e);
                e.printStackTrace();
            }

            return bitmap;
        }

        // If Build.VERSION.SDK_INT is less than P
        try {
            InputStream inputStream = contentResolver.openInputStream(uri);
            bitmap = BitmapFactory.decodeStream(inputStream);
            inputStream.close();


//            ParcelFileDescriptor parcelFileDescriptor =
//                    contentResolver.openFileDescriptor(uri, "r");
//            FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
//            bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor);
//            parcelFileDescriptor.close();
        } catch (IOException e) {
            NotifyUtils.logError(TAG, "getBitmap()", e);
            e.printStackTrace();
        }

        return bitmap;
    }
}
