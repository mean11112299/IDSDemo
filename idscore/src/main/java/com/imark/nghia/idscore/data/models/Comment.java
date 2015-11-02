package com.imark.nghia.idscore.data.models;

import android.content.Context;

import com.windyroad.nghia.common.network.UploadStatus;
import com.imark.nghia.idscore.data.AssignData;

import java.util.Calendar;

/**
 * Created by Nghia-PC on 8/28/2015.
 */
public class Comment extends BaseDataObject {
    private long assignId;
    private String advantage;
    private String disadvantage;
    private String suggestion;
    private String latitude;
    private String longitude;

    private Assign assign;

    public Comment() {
    }

    public Comment(long _id, int serverId, String sessionCode, long createBy, Calendar createAt, UploadStatus uploadStatus, long assignId, String advantage, String disadvantage, String suggestion, String latitude, String longitude) {
        super(_id, serverId, sessionCode, createBy, createAt, uploadStatus);
        this.assignId = assignId;
        this.advantage = advantage;
        this.disadvantage = disadvantage;
        this.suggestion = suggestion;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public long getAssignId() {
        return assignId;
    }

    public void setAssignId(long assignId) {
        this.assignId = assignId;
    }

    public String getAdvantage() {
        return advantage;
    }

    public void setAdvantage(String advantage) {
        this.advantage = advantage;
    }

    public String getDisadvantage() {
        return disadvantage;
    }

    public void setDisadvantage(String disadvantage) {
        this.disadvantage = disadvantage;
    }

    public String getSuggestion() {
        return suggestion;
    }

    public void setSuggestion(String suggestion) {
        this.suggestion = suggestion;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public Assign getAssign(Context context) {
        if (assign == null){
            assign = AssignData.getById(context, assignId);
        }
        return assign;
    }
}
