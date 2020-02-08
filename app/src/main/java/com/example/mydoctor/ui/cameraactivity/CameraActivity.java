package com.example.mydoctor.ui.cameraactivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Surface;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.mydoctor.R;
import com.example.mydoctor.utils.CameraPreview;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CameraActivity extends AppCompatActivity {

    private static final String TAG = "CameraActivity";
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    @BindView(R.id.camera_preview)
    FrameLayout camera_preview;
    @BindView(R.id.camera_button_capture)
    ImageView camera_button_capture;
    @BindView(R.id.camera_flip_imageView)
    ImageView camera_flip_imageView;
    @BindView(R.id.camera_layout_take_image)
    LinearLayout camera_layout_take_image;
    @BindView(R.id.camera_layout_save_image)
    RelativeLayout camera_layout_save_image;
    @BindView(R.id.camera_button_save)
    ImageView camera_button_save;
    @BindView(R.id.camera_btn_cancel)
    ImageView camera_btn_cancel;

    int cameraId = 0;
    private Camera mCamera;
    private boolean safeToTakePicture = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        ButterKnife.bind(this);

        verifyPermissions();

        camera_flip_imageView.setOnClickListener(v -> switchCamera());

    }

    void startCamera(int cameraId) {

        Log.d(TAG, "startCamera: " + cameraId);

        // Create an instance of Camera
        mCamera = getCameraInstance(cameraId);

        // Create our Preview view and set it as the content of our activity.
        CameraPreview mPreview = new CameraPreview(this, mCamera);
        camera_preview.addView(mPreview);
        setCameraDisplayOrientation(this, cameraId, mCamera);

        camera_button_capture.setOnClickListener(v -> mCamera.takePicture(null, null, mPicture));


    }


    public static Camera getCameraInstance(int cameraId) {
        Camera c = null;
        try {
            c = Camera.open(cameraId); // attempt to get a Camera instance

        } catch (Exception e) {
            // Camera is not available (in use or does not exist)
            Log.d(TAG, "getCameraInstance: " + e.getLocalizedMessage());
        }
        Log.d(TAG, "getCameraInstance: " + c);
        return c; // returns null if camera is unavailable
    }

    private Camera.PictureCallback mPicture = (data, camera) -> {

        File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);

        String fileName = String.valueOf(pictureFile);

        Log.d(TAG, "file name: "+fileName);
        if (pictureFile == null) {
            Log.d(TAG, "Error creating media file, check storage permissions");
            return;
        }

        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            fos.write(data);
            fos.close();



            settingImage(fileName);
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(pictureFile)));

            Log.d(TAG, "File created: " + fos);
        } catch (FileNotFoundException e) {
            Log.d(TAG, "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d(TAG, "Error accessing file: " + e.getMessage());
        }
    };

    /**
     * Create a File for saving an image or video
     */
    private static File getOutputMediaFile(int type) {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "FittieApp");
        Log.d(TAG, "getOutputMediaFile: " + mediaStorageDir);
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_" + timeStamp + ".jpg");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }


    /**
     * Create a file Uri for saving an image or video
     */
    private static Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    private void verifyPermissions() {
        Log.d(TAG, "verifyPermissions: asking user for permissions");

        String[] permissions1 = {Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA};

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            int hasCameraPermission = checkSelfPermission(Manifest.permission.CAMERA);

            List<String> permissions = new ArrayList<>();

            if (hasCameraPermission != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.CAMERA);

            } else {

                startCamera(cameraId);

            }
            if (!permissions.isEmpty()) {
                ActivityCompat.requestPermissions(CameraActivity.this, permissions1, 111);
            }
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 111) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    System.out.println("Permissions --> " + "Permission Granted: " + permissions[i]);

                    startCamera(cameraId);


                } else if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    System.out.println("Permissions --> " + "Permission Denied: " + permissions[i]);
                    Toast.makeText(this, "Permission denied...", Toast.LENGTH_SHORT).show();

                }
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }

    public static void setCameraDisplayOrientation(Activity activity,
                                                   int cameraId, android.hardware.Camera camera) {
        android.hardware.Camera.CameraInfo info =
                new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay()
                .getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);
    }

    private boolean hasFrontCamera() {

        boolean hasFrontCamera = false;

        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.getCameraInfo(i, cameraInfo);
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                hasFrontCamera = true;
            }
        }
        return hasFrontCamera;
    }

    public void switchCamera() {

        if (hasFrontCamera()) {

            if (cameraId == 0) {

                cameraId = 1;
                mCamera.release();

                startCamera(cameraId);
            } else {

                cameraId = 0;

                mCamera.release();
                startCamera(cameraId);

            }
        } else {

            Toast.makeText(this, "Front camera is not available for this phone", Toast.LENGTH_SHORT).show();

        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mCamera.release();

    }

    public void settingImage(String pictureFile) {

        camera_layout_take_image.setVisibility(View.GONE);
        camera_layout_save_image.setVisibility(View.VISIBLE);

        camera_btn_cancel.setOnClickListener(v -> {
            startCamera(cameraId);

            camera_layout_take_image.setVisibility(View.VISIBLE);
            camera_layout_save_image.setVisibility(View.GONE);

        });

        camera_button_save.setOnClickListener(v -> {

            Intent returnIntent = new Intent();
            returnIntent.putExtra("result",pictureFile);
            setResult(Activity.RESULT_OK,returnIntent);
            finish();

        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();

    }
}
