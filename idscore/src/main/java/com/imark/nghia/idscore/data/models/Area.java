package com.imark.nghia.idscore.data.models;

import com.windyroad.nghia.common.network.UploadStatus;

import java.util.Calendar;

/**
 * Created by Nghia-PC on 9/5/2015.
 */
public class Area extends BaseDataObject {
    private String Name;
    private int ParentID;
    private int AreaLevel;

    public Area() {
    }

    public Area(long _id, int serverId, String sessionCode, long createBy, Calendar createAt, UploadStatus uploadStatus, String name, int parentID, int areaLevel) {
        super(_id, serverId, sessionCode, createBy, createAt, uploadStatus);
        Name = name;
        ParentID = parentID;
        AreaLevel = areaLevel;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getParentID() {
        return ParentID;
    }

    public void setParentID(int parentID) {
        ParentID = parentID;
    }

    public int getAreaLevel() {
        return AreaLevel;
    }

    public void setAreaLevel(int areaLevel) {
        AreaLevel = areaLevel;
    }

    @Override
    public String toString() {
        return Name;
    }
}
