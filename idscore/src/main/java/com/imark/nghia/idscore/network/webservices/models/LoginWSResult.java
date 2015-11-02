package com.imark.nghia.idscore.network.webservices.models;

/**
 * Created by Nghia-PC on 9/4/2015.
 */
public class LoginWSResult extends BaseWSResult {

    private UserInfo UserInfo;

    public LoginWSResult.UserInfo getUserInfo() {
        return UserInfo;
    }

    public void setUserInfo(LoginWSResult.UserInfo userInfo) {
        UserInfo = userInfo;
    }


    public class UserInfo {
        private String UserTeam_ID;
        private String DisplayName;

        public String getUserTeam_ID() {
            return UserTeam_ID;
        }

        public void setUserTeam_ID(String userTeam_ID) {
            UserTeam_ID = userTeam_ID;
        }

        public String getDisplayName() {
            return DisplayName;
        }

        public void setDisplayName(String displayName) {
            DisplayName = displayName;
        }
    }


}
