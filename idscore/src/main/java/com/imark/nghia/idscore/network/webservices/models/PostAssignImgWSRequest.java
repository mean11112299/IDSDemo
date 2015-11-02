package com.imark.nghia.idscore.network.webservices.models;

/**
 * Created by Nghia-PC on 9/18/2015.
 */
public class PostAssignImgWSRequest {
    private String pAppCode;
    private String pSessionCode;
    private String pAssignID;
    private String pImageID;
    private int pImageType;

    public PostAssignImgWSRequest(String pAppCode, String pSessionCode, String pAssignID, String pImageID, int pImageType) {
        this.pAppCode = pAppCode;
        this.pSessionCode = pSessionCode;
        this.pAssignID = pAssignID;
        this.pImageID = pImageID;
        this.pImageType = pImageType;
    }

    public String getpAppCode() {
        return pAppCode;
    }

    public void setpAppCode(String pAppCode) {
        this.pAppCode = pAppCode;
    }

    public String getpSessionCode() {
        return pSessionCode;
    }

    public void setpSessionCode(String pSessionCode) {
        this.pSessionCode = pSessionCode;
    }

    public String getpAssignID() {
        return pAssignID;
    }

    public void setpAssignID(String pAssignID) {
        this.pAssignID = pAssignID;
    }

    public String getpImageID() {
        return pImageID;
    }

    public void setpImageID(String pImageID) {
        this.pImageID = pImageID;
    }

    public int getpImageType() {
        return pImageType;
    }

    public void setpImageType(int pImageType) {
        this.pImageType = pImageType;
    }
}
