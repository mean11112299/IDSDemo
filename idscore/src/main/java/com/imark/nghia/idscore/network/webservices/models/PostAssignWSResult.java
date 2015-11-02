package com.imark.nghia.idscore.network.webservices.models;

/**
 * Created by Nghia-PC on 9/17/2015.
 */
public class PostAssignWSResult extends BaseWSResult {
    private long AssignID;
    private long OutletID;

    public long getAssignID() {
        return AssignID;
    }

    public void setAssignID(long assignID) {
        AssignID = assignID;
    }

    public long getOutletID() {
        return OutletID;
    }

    public void setOutletID(long outletID) {
        OutletID = outletID;
    }
}
