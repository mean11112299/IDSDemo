package com.imark.nghia.idscore.models;

/**
 * Created by Nghia-PC on 8/24/2015.
 * Thông tin Cá nhân
 */
public class UserInfo {
    private long userID;
    private String userCode;
    private String userName;
    private String displayName;
    private String iconUrl;

    public UserInfo() {
    }

    public UserInfo(long userID, String userCode, String userName, String displayName, String iconUrl) {
        this.userID = userID;
        this.userCode = userCode;
        this.userName = userName;
        this.displayName = displayName;
        this.iconUrl = iconUrl;
    }

    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }
}
