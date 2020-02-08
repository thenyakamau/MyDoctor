package com.example.mydoctor.di.createpartfromstring;

import android.app.Application;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import com.example.mydoctor.utils.RealPathUtil;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Singleton;

import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

@Singleton
public class CreatePartFromString {

    private static final String TAG = "CreatePartFromStringMod";
    private static final MediaType MEDIA_TYPE_JPEG = MediaType.get("image/jpeg");
    private static final MediaType MEDIA_TYPE_PDF = MediaType.get("application/pdf");


    private Application application;

    @Inject
    CreatePartFromString(Application application) {
        this.application = application;

    }

    public RequestBody createPartFromString(String item) {

        Log.d(TAG, "createPartFromString: "+item);

        if (item != null && !item.isEmpty()) {

            return RequestBody.create(MultipartBody.FORM, item);
        } else {
            return null;
        }

    }


    public MultipartBody.Part prepareFilePart(String filetype, Uri fileUri) {

        if (fileUri != null) {

            String realPath;
                // SDK < API 11
            if (Build.VERSION.SDK_INT < 11) {
                realPath = RealPathUtil.getRealPathFromURI_BelowAPI11(application, fileUri);
            }
                // SDK >= 11 && SDK < 19
            else if (Build.VERSION.SDK_INT < 19) {
                realPath = RealPathUtil.getRealPathFromURI_API11to18(application, fileUri);
            }
                // SDK => 19 (Android 4.4)
            else {
                realPath = RealPathUtil.getRealPathFromURI_API19(application, fileUri);
            }

            Log.d(TAG, "prepareFilePart: " + realPath + fileUri);

            File file = new File(realPath);


            Log.d(TAG, "prepareFilePart: " + file);

            // create RequestBody instance from file
            RequestBody requestFile = RequestBody.create(MediaType.parse(Objects.requireNonNull(application.getContentResolver().getType(fileUri))), file);

            Log.d(TAG, "prepareFilePart: " + application.getContentResolver().getType(Uri.parse(realPath)));

            // MultipartBody.Part is used to send also the actual file name
            return MultipartBody.Part.createFormData(filetype, file.getName(), requestFile);

        } else {

            return null;
        }
    }

    public MultipartBody.Part preparePdfFilePart(String filetype, Uri fileUri) {

        if (fileUri != null) {

            String realPath;
            // SDK < API 11
            if (Build.VERSION.SDK_INT < 11) {
                realPath = RealPathUtil.getRealPathFromURI_BelowAPI11(application, fileUri);
            }
            // SDK >= 11 && SDK < 19
            else if (Build.VERSION.SDK_INT < 19) {
                realPath = RealPathUtil.getRealPathFromURI_API11to18(application, fileUri);
            }
            // SDK => 19 (Android 4.4)
            else {
                realPath = RealPathUtil.getRealPathFromURI_API19(application, fileUri);
            }

            Log.d(TAG, "prepareFilePart: " + realPath + fileUri);

            File file;
            if (realPath!=null){
                file = new File(realPath);
            }else {

                file = new File(Objects.requireNonNull(fileUri.getPath()));
            }



            Log.d(TAG, "prepareFilePart: " + file);

            // create RequestBody instance from file
            RequestBody requestFile = RequestBody.create(MEDIA_TYPE_PDF, file);

            //Log.d(TAG, "prepareFilePart: " + application.getContentResolver().getType(Uri.parse(realPath)));

            // MultipartBody.Part is used to send also the actual file name
            return MultipartBody.Part.createFormData(filetype, file.getName(), requestFile);

        } else {

            return null;
        }
    }

    public MultipartBody.Part createImage(String fietype, Uri fileUri) {


        if (fileUri != null) {

            String realPath;
            // SDK < API 11
            if (Build.VERSION.SDK_INT < 11) {
                realPath = RealPathUtil.getRealPathFromURI_BelowAPI11(application, fileUri);
            }
            // SDK >= 11 && SDK < 19
            else if (Build.VERSION.SDK_INT < 19) {
                realPath = RealPathUtil.getRealPathFromURI_API11to18(application, fileUri);
            }
            // SDK => 19 (Android 4.4)
            else {
                realPath = RealPathUtil.getRealPathFromURI_API19(application, fileUri);
            }

            Log.d(TAG, "prepareFilePart: " + realPath + fileUri);

            File file;

            if (realPath!=null){
                file = new File(realPath);
            }else {

                file = new File(Objects.requireNonNull(fileUri.getPath()));
            }

            File comprossedFile = null;
            try {
                comprossedFile = new Compressor(application)
                        .setQuality(75)
                        .setMaxHeight(300)
                        .setMaxWidth(290)
                        .setCompressFormat(Bitmap.CompressFormat.JPEG)
                        .compressToFile(file);
            } catch (IOException e) {
                e.printStackTrace();
            }


            // create RequestBody instance from file
            RequestBody requestFile = null;
            if (comprossedFile != null) {
                requestFile = RequestBody.create(MEDIA_TYPE_JPEG, comprossedFile);
            }

            // MultipartBody.Part is used to send also the actual file name
            if (requestFile != null) {
                return MultipartBody.Part.createFormData(fietype, file.getName(), requestFile);
            }else {
                return null;
            }

        } else {

            return null;
        }

    }

    public RequestBody createFile(Uri fileUri) {

        if (fileUri != null) {

            String realPath;
            // SDK < API 11
            if (Build.VERSION.SDK_INT < 11) {
                realPath = RealPathUtil.getRealPathFromURI_BelowAPI11(application, fileUri);
            }
            // SDK >= 11 && SDK < 19
            else if (Build.VERSION.SDK_INT < 19) {
                realPath = RealPathUtil.getRealPathFromURI_API11to18(application, fileUri);
            }
            // SDK => 19 (Android 4.4)
            else {
                realPath = RealPathUtil.getRealPathFromURI_API19(application, fileUri);
            }

            Log.d(TAG, "prepareFilePart: " + realPath + fileUri);

            File file = new File(realPath);


            Log.d(TAG, "prepareFilePart: " + file);

            // create RequestBody instance from file

            return RequestBody.create(MediaType.parse(Objects.requireNonNull(application.getContentResolver().getType(fileUri))), file);

        } else {

            return null;
        }

    }
}
