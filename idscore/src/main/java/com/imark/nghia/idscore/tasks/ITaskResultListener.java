package com.imark.nghia.idscore.tasks;

import com.windyroad.nghia.common.models.ActionResult;

/**
 * Created by Nghia-PC on 8/26/2015.
 * Interface trung gian, Nhận kết quả từ các Task trả về
 */
public interface ITaskResultListener {
    /**
     * Nhận kết quả từ Task trả về
     * @param result
     */
    public void onPostExecuteResult(ActionResult result);
}
