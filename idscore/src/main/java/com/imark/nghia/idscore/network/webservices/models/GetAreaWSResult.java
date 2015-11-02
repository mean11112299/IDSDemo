package com.imark.nghia.idscore.network.webservices.models;

/**
 * Created by Nghia-PC on 9/5/2015.
 */
public class GetAreaWSResult {
    private int ID;
    private int ParentID;
    private String Code;
    private String Name;
    private int AreaLevel;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getParentID() {
        return ParentID;
    }

    public void setParentID(int parentID) {
        ParentID = parentID;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getAreaLevel() {
        return AreaLevel;
    }

    public void setAreaLevel(int areaLevel) {
        AreaLevel = areaLevel;
    }
}
