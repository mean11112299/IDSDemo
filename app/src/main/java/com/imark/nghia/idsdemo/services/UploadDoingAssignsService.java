package com.imark.nghia.idsdemo.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.widget.Toast;

import com.windyroad.nghia.common.models.ActionResult;
import com.windyroad.nghia.common.models.BatchActionResult;
import com.imark.nghia.idsdemo.R;
import com.imark.nghia.idscore.data.AssignData;
import com.imark.nghia.idscore.data.models.Assign;
import com.imark.nghia.idscore.helper.UserPref;
import com.imark.nghia.idscore.tasks.PostListAssignWSTask;

import java.util.List;

/**
 * Created by Nghia-PC on 9/21/2015.
 * Upload Tất cả dữ liệu của Assign chưa update
 */
public class UploadDoingAssignsService extends Service {

    /** ID cho notification Uploading dữ liệu */
    public static final int ID_NOTIFY_UPLOADING = 110;
    public static final String ACTION_UPLOADING =  "com.imark.nghia.idsdemo.services.action.RECEIVER_UPLOADING";
    public static final String ACTION_UPLOAD_FINISH =  "com.imark.nghia.idsdemo.services.action.RECEIVER_UPLOAD_FINISH";

    private NotificationCompat.Builder mNotifyBuilderProgress;
    private PostListAssignWSTask mPostListAssignTask;
    BatchActionResult mUploadResult;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        mNotifyBuilderProgress_init();

        List<Assign> listDoingAssign = AssignData.getByWorkStatus(
                getApplicationContext(), UserPref.getUserPrefId(getApplicationContext()),
                Assign.WorkStatus.DOING
        );

        mPostListAssignTask = new PostListAssignWSTask(
                getApplicationContext(), listDoingAssign, postListAssignWSTask_listener
        );
        mPostListAssignTask.execute();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }


    /** Lấy kết quả trả về */
    private PostListAssignWSTask.TaskListener postListAssignWSTask_listener = new PostListAssignWSTask.TaskListener() {
        @Override
        public void onProgressUpdate(BatchActionResult batchResult) {

            mUploadResult = batchResult;

            String infoMsg = getString(
                    R.string.message_uploading_assign, batchResult.getTotalSuccess(), batchResult.getTotalFail()
            );
            mNotifyBuilderProgress_notify(batchResult.getTotal(), batchResult.getCurrentIndex(), infoMsg);

            publishUploadingResult();
        }

        @Override
        public void onPostExecute(ActionResult result) {
            finishService();
            showFinishNotification();
            publishUploadFinishResult();
        }
    };

    /** Gởi Receiver Đang Upload **/
    private void publishUploadingResult() {
        Intent intent = new Intent(ACTION_UPLOADING);
        sendBroadcast(intent);
    }

    /** Gởi Receiver Upload finish **/
    private void publishUploadFinishResult() {
        Intent intent = new Intent(ACTION_UPLOAD_FINISH);
        sendBroadcast(intent);
    }

    /** Dừng service */
    private void finishService() {
        Toast.makeText(getApplicationContext(), "Dừng lại rồi", Toast.LENGTH_LONG).show();
        stopForeground(true);  // dừng nền
        stopSelf();  // dừng service, notification
    }

    /**
     * Hiện thông tin kết thúc
     */
    private void showFinishNotification() {

        String infoMsg = getString(
                R.string.message_uploading_assign,
                mUploadResult.getTotalSuccess(),
                mUploadResult.getTotalFail()
        );

        // Intent chạy hết ứng dụng
        Intent backIntent = new Intent();
        final PendingIntent pendingIntent = PendingIntent
                .getActivity(getApplicationContext(), 0, backIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder infoBuilder = new NotificationCompat.Builder(getApplicationContext()) {{
            setContentIntent(pendingIntent);
            setContentTitle(getString(R.string.title_notify_upload_finish));
            setSmallIcon(R.mipmap.ic_launcher);
            setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
            setAutoCancel(true);  // ẩn khi click vào
        }};

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        infoBuilder.setContentText(infoMsg);
        manager.notify(ID_NOTIFY_UPLOADING, infoBuilder.build());
    }


    /**
     * Hiện Notification
     * @param max
     * @param progress
     * @param contentText
     */
    private void mNotifyBuilderProgress_notify(int max, int progress, String contentText) {

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotifyBuilderProgress.setProgress(max, progress, false);
        mNotifyBuilderProgress.setContentText(contentText);
        manager.notify(ID_NOTIFY_UPLOADING, mNotifyBuilderProgress.build());
    }

    private void mNotifyBuilderProgress_init() {
        // Intent chạy hết ứng dụng
        Intent backIntent = new Intent();
        final PendingIntent pendingIntent = PendingIntent
                .getActivity(getApplicationContext(), 0, backIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        mNotifyBuilderProgress = new NotificationCompat.Builder(getApplicationContext()) {{
            setContentIntent(pendingIntent);
            setContentTitle(getString(R.string.title_notify_uploading));
            setSmallIcon(R.mipmap.ic_launcher);
            setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        }};

        Notification notification = mNotifyBuilderProgress.build();
        startForeground(ID_NOTIFY_UPLOADING, notification);  // đính kèm nền
    }
}
