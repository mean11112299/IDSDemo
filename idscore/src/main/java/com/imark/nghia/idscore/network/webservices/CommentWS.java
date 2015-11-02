package com.imark.nghia.idscore.network.webservices;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.imark.nghia.idscore.data.CommentData;
import com.imark.nghia.idscore.data.models.Assign;
import com.imark.nghia.idscore.network.webservices.models.BaseWSResult;
import com.windyroad.nghia.common.ConvertUtil;
import com.windyroad.nghia.common.models.BatchActionResult;
import com.windyroad.nghia.common.network.UploadStatus;
import com.windyroad.nghia.common.network.UrlParam;
import com.windyroad.nghia.common.network.FormMIMEType;
import com.windyroad.nghia.common.network.WebserviceUtil;
import com.imark.nghia.idscore.data.models.Comment;
import com.imark.nghia.idscore.network.webservices.models.PostCommentWSResult;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Nghia-PC on 9/18/2015.
 */
public class CommentWS {
    private static final String TAG = CommentWS.class.getName();

    public static PostCommentWSResult post(final Comment comment, final long assignServerId) {

        ArrayList<UrlParam> listParams = new ArrayList<UrlParam>() {{
            add(new UrlParam(UrlParam.ParamType.TEXT, "pAppCode", WSConfig.APP_CODE));
            add(new UrlParam(UrlParam.ParamType.TEXT, "pSessionCode", comment.getSessionCode()));
            add(new UrlParam(UrlParam.ParamType.TEXT, "pUserTeamID", comment.getCreateBy()+""));
            add(new UrlParam(UrlParam.ParamType.TEXT, "pDateTime", comment.getCreateAt()+""));

            add(new UrlParam(UrlParam.ParamType.TEXT, "pAssignID", assignServerId+""));
            add(new UrlParam(UrlParam.ParamType.TEXT, "pSuggestion", comment.getSuggestion()+""));
            add(new UrlParam(UrlParam.ParamType.TEXT, "pAdvantage", comment.getAdvantage()+""));
            add(new UrlParam(UrlParam.ParamType.TEXT, "pHard", comment.getDisadvantage()+""));
        }};

        InputStream inputStream = WebserviceUtil.sendPostForm(
                FormMIMEType.X_WWW_FORM_URLENDCODED,
                WSConfig.PATH_POST_ADD_COMMENT,
                listParams);

        String strValue = ConvertUtil.Stream2String(inputStream);
        Log.e(TAG, strValue);

        return new Gson().fromJson(strValue, PostCommentWSResult.class);
    }


    /**
     * Post và Update lại List Comment
     * @param listComment
     * @return
     */
    public static BatchActionResult postAndUpdate(Context context, ArrayList<Comment> listComment) {
        BatchActionResult batchResult = new BatchActionResult(0, 0);  // tính thành công, thất bại

        for (Comment comment : listComment) {

            boolean isSuccess = false;  // đánh dấu đã thành công, không thêm thất bại
            // kiểm tra Assign Đã upload
            Assign assign = comment.getAssign(context);
            if (assign.getUploadStatus() == UploadStatus.UPLOADED) {

                PostCommentWSResult puResult = CommentWS.post(comment, assign.getServerId());
                if (puResult.getStatus() == BaseWSResult.STATUS_SUCCESS
                        || puResult.getStatus() == BaseWSResult.STATUS_DUPLICATE) {

                    CommentData.update_UploadStatus_ServerId_byId(
                            context, comment.get_id(), UploadStatus.UPLOADED, puResult.getCommentID()
                    );

                    isSuccess = true;
                }
            }
            if (isSuccess) batchResult.addSuccess(1);
            else batchResult.addFail(1);
        }
        return batchResult;
    }
}
