package com.imark.nghia.idscore.helper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import com.windyroad.nghia.common.FileUtil;

import java.io.File;

/**
 * Created by Nghia-PC on 8/28/2015.
 */
public class Global {

    /**
     * Tạo file Path không cần gì hết
     * @param context
     * @param extension
     * @return
     */
    public static String makeFilePath(Context context, String extension) {
        String filePath = Environment.getExternalStorageDirectory().toString() + "/" + AppConfig.APP_IMAGE_DIRECTORY_PATH;
        return FileUtil.makeFilePathByTime(
                filePath,
                UserPref.getUserPrefId(context) + "",
                extension
        );
    }

    /**
     * Mở Camera trả kết quả về
     * Constants.REQUEST_CODE_CAPTURE_IMAGE
     */
    public static Uri startCameraForResult(Activity activity, int requestCode) {
        Uri resultUri;

        // tạo file
        String filePath = Global.makeFilePath(activity, AppFileExtensions.IMAGE);
        FileUtil.makeFileDirectory(filePath);
        File file = new File(filePath);
        resultUri = Uri.fromFile(file);  // create a file to save the image

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, resultUri);  // set the image file name
        activity.startActivityForResult(intent, requestCode);  // start the image capture Intent

        return resultUri;
    }
}
