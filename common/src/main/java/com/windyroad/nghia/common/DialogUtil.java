package com.windyroad.nghia.common;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.support.annotation.Nullable;

/**
 * Created by Nghia-PC on 8/27/2015.
 * Các Dialog hỗ trợ nhanh
 */
public class DialogUtil {
    /**
     * Hiện Dialog Mở GPS
     * */
    public static void showLocationSettingAlert(
            final Context context, String title, String message, String positiveButtonText, String negativeButtonText){

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle(title).setMessage(message);

        alertDialog.setPositiveButton(positiveButtonText, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(intent);
            }
        });
        alertDialog.setNegativeButton(negativeButtonText, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }


    /**
     * Mở Dialog hỏi mở Mobile Data
     */
    public static void showNetworkSettingAlert(
            final Context context, String title, String message, String positiveButtonText, String negativeButtonText){

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle(title).setMessage(message);

        alertDialog.setPositiveButton(positiveButtonText, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent();
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setAction(android.provider.Settings.ACTION_DATA_ROAMING_SETTINGS);
                context.startActivity(intent);
            }
        });
        alertDialog.setNegativeButton(negativeButtonText, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

    /**
     * Ẩn/Hiện Progress Đang hoạt động
     * Ko tắt khi ở ngoài, không cho hủy
     * @param progressDialog
     * @param isWorking
     */
    public static void showProgressDialog(
            ProgressDialog progressDialog, boolean isWorking, @Nullable String title,@Nullable String message) {

        if (isWorking) {

            if (title != null)
                progressDialog.setTitle(title);
            if (message != null)
                progressDialog.setMessage(message);

            progressDialog.setCancelable(false);  // không cho hủy
            progressDialog.setCanceledOnTouchOutside(false);  // không ẩn khi click ngoài

            progressDialog.show();

        } else {
            progressDialog.dismiss();
            progressDialog.cancel();  // giống dismiss, gọi hàm dialog.cancellistner
        }
    }



}
