package com.study.hancom.sharephoto.util;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;

import java.util.ArrayList;

public class ImageUtil {
    private static final String TAG = ImageUtil.class.getName();

    public static ArrayList<String> getMediaImage(Context context) {
        ArrayList<String> galleryPictures = new ArrayList<>();

        final String orderBy = MediaStore.Images.Media.DATE_TAKEN;
        final String[] columns = {MediaStore.Images.Media.DATA, //The data stream for the file
                MediaStore.Images.Media._ID, //The unique ID for a row.
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME};
        Cursor imageCursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null, null, orderBy);

        int count = imageCursor.getCount();
        for (int i = 0; i < count; i++) {
            imageCursor.moveToPosition(i);

            int dataColumnIndex = imageCursor.getColumnIndex(MediaStore.Images.Media.DATA);
            int directoryColumnIndex = imageCursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);

            String directoryName = imageCursor.getString(directoryColumnIndex);
            Log.v(TAG, "directoryName --->" + directoryName);
            String fileName = imageCursor.getString(dataColumnIndex);
            Log.d(TAG, "fileName--->" + fileName);

            galleryPictures.add(i, imageCursor.getString(dataColumnIndex));

        }
        imageCursor.close();

        return galleryPictures;
    }
}