package com.imark.nghia.idsdemo.helper;

import android.app.ProgressDialog;
import android.content.Context;

import com.imark.nghia.idscore.R;
import com.windyroad.nghia.common.DialogUtil;

/**
 * Created by Nghia-PC on 9/3/2015.
 */
public class AppAlertDialog {

    /** Hiện waiting dialog */
    public static void showWaitingDialog (Context context, ProgressDialog progressDlg){
        //progressDlg = new ProgressDialog(context);
        DialogUtil.showProgressDialog(progressDlg, true, null, context.getString(R.string.message_dialog_waiting));
    }

    /** Hiện get data dialog */
    public static void showGetDataDialog (Context context, ProgressDialog progressDlg){
        //progressDlg = new ProgressDialog(context);
        DialogUtil.showProgressDialog(progressDlg, true,
                context.getString(R.string.title_dialog_waiting),
                context.getString(R.string.message_dialog_waiting_get_data));
    }

    /** Hiện postAttendanceImg data dialog */
    public static void showPostDataDialog (Context context, ProgressDialog progressDlg){
        //progressDlg = new ProgressDialog(context);
        DialogUtil.showProgressDialog(progressDlg, true,
                context.getString(R.string.title_dialog_waiting),
                context.getString(R.string.message_dialog_waiting_post_data));
    }

    /** Hiện Mở mạng dialog */
    public static void showNetworkSettingAlert(Context context){
        DialogUtil.showNetworkSettingAlert(context,
                context.getString(R.string.title_dialog_question, ""),
                context.getString(R.string.message_dialog_open_network, ""),
                context.getString(R.string.action_settings, ""),
                context.getString(R.string.action_cancel, ""));
    }

    public static void showLocationSettingAlert(Context context) {
        DialogUtil.showLocationSettingAlert(
                context,
                context.getString(com.imark.nghia.idscore.R.string.title_dialog_gps),
                context.getString(com.imark.nghia.idscore.R.string.message_dialog_open_gps),
                context.getString(com.imark.nghia.idscore.R.string.action_settings),
                context.getString(com.imark.nghia.idscore.R.string.action_cancel)
        );
    }
}
