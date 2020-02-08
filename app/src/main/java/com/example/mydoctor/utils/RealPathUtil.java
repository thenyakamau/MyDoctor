package com.example.mydoctor.utils;

import android.app.Application;
import android.database.Cursor;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import androidx.loader.content.CursorLoader;

public class RealPathUtil {
    public static String getRealPathFromURI_BelowAPI11(Application application, Uri fileUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = application.getContentResolver().query(fileUri, proj, null, null, null);
        int column_index
                = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public static String getRealPathFromURI_API11to18(Application application, Uri fileUri) {

        String[] proj = { MediaStore.Images.Media.DATA };
        String result = null;

        CursorLoader cursorLoader = new CursorLoader(
                application,
                fileUri, proj, null, null, null);
        Cursor cursor = cursorLoader.loadInBackground();

        if(cursor != null){
            int column_index =
                    cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            result = cursor.getString(column_index);
        }
        return result;

    }

    public static String getRealPathFromURI_API19(Application application, Uri fileUri) {

        String filePath = "";
        try {
            String wholeID = DocumentsContract.getDocumentId(fileUri);

            // Split at colon, use second item in the array
            String id = wholeID.split(":")[1];

            String[] column = {MediaStore.Images.Media.DATA};

            // where id is equal to
            String sel = MediaStore.Images.Media._ID + "=?";

            Cursor cursor = application.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    column, sel, new String[]{id}, null);

            int columnIndex = cursor.getColumnIndex(column[0]);

            if (cursor.moveToFirst()) {
                filePath = cursor.getString(columnIndex);
            }
            cursor.close();
            return filePath;
        }
        catch (Exception e){
            return getRealPathFromURI_API11to18(application, fileUri);
        }

    }
}
