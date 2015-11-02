package com.windyroad.nghia.common;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Nghia-PC on 8/1/2015.
 */
public class FileUtil {

    /**
     * Copy file từ Asset to File
     *
     * @param assetSourcePath Nguồn ở Asset
     * @param targetPath      Đích copy đến
     * @param override        Ghi đè
     * @return Kết quả, True = Thành công
     */
    public static boolean copyAssetFile(Context context, String assetSourcePath, String targetPath,
                                        boolean override) {
        try {
            // Kiểm tra ghi đè, tồn tại
            if (!override) {
                File file = new File(targetPath);
                if (file.exists())
                    return false;
            }
            makeFileDirectory(targetPath);  // tạo thư mục

            // Mở file = InputStream
            InputStream input = context.getAssets().open(assetSourcePath);
            OutputStream output = new FileOutputStream(targetPath);

            // Chuyển byte từ Input => Output
            byte[] buffer = new byte[1024];
            int length;
            while ((length = input.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }

            // Close the streams
            output.flush();
            output.close();
            input.close();
            return true;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Tạo 1 Đường Dẫn File
     *
     * @param folderPath   đường dẫn phụ
     * @param prefix    key word đầu tiên (userId) của file
     * @param extension phần mở rộng của file (không chấm)
     * @return
     */
    public static String makeFilePathByTime(String folderPath, String prefix, String extension) {
        String resultPath = "";

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS", Locale.getDefault());
        long date = Long.valueOf(dateFormat.format(new Date()));

        resultPath = folderPath + "/" + prefix + "_" + date + "." + extension;

        return resultPath;
    }

    /**
     * Tạo thư mục chứa file
     * @param filePath
     */
    public static boolean makeFileDirectory(String filePath){
        String folderTargetPath = filePath.substring(0, filePath.lastIndexOf("/"));
        File folder = new File(folderTargetPath);

        if (!folder.exists() || !folder.isDirectory())
            return folder.mkdirs();

        return true;
    }

    /**
     * Lấy file Name từ filePath
     * @param filePath
     * @return
     */
    public static String getFileName(String filePath) {
        return filePath.substring(filePath.lastIndexOf("/")+1, filePath.length());
    }
}
