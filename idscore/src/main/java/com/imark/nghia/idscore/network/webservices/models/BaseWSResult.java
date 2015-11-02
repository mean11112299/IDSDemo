package com.imark.nghia.idscore.network.webservices.models;

/**
 * Created by Nghia-PC on 9/4/2015.
 */
public class BaseWSResult {
    public static final int STATUS_SUCCESS = 1;
    public static final int STATUS_DUPLICATE = 2;  // đã cập nhật
    public static final int STATUS_UNKNOWN = -1;

    private int Status;
    private String Description;

    public BaseWSResult() {
    }

    public BaseWSResult(int status, String description) {
        Status = status;
        Description = description;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }
}
