package com.imark.nghia.idscore.data.models;

/**
 * Created by Nghia-PC on 8/21/2015.
 * Truy vấn dữ liệu bảng CodeDetail
 * CodeDetail: Bảng con phụ nhỏ, để lấy thông tin, trạng thái
 */
public class CodeDetail {
    private String groupCode;
    private String name;
    private String value;
    private int ordinal;
    private String remark;

    public CodeDetail() {
    }

    public CodeDetail(String groupCode, String name, String value, int ordinal, String remark) {
        this.groupCode = groupCode;
        this.name = name;
        this.value = value;
        this.ordinal = ordinal;
        this.remark = remark;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getOrdinal() {
        return ordinal;
    }

    public void setOrdinal(int ordinal) {
        this.ordinal = ordinal;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
