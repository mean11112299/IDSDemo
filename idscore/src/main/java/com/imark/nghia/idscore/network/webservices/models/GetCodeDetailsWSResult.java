package com.imark.nghia.idscore.network.webservices.models;

import java.util.List;

/**
 * Created by Nghia-PC on 9/4/2015.
 */
public class GetCodeDetailsWSResult extends BaseWSResult {

    private List<SimpleCodeDetail> ListSimpleCodeDetail;

    public List<SimpleCodeDetail> getListSimpleCodeDetail() {
        return ListSimpleCodeDetail;
    }

    public void setListSimpleCodeDetail(List<SimpleCodeDetail> listSimpleCodeDetail) {
        ListSimpleCodeDetail = listSimpleCodeDetail;
    }


    public class SimpleCodeDetail {
        public String GroupCode;
        public String Name;
        public String Value;
        public int Ordinal;

        public String getGroupCode() {
            return GroupCode;
        }

        public void setGroupCode(String groupCode) {
            GroupCode = groupCode;
        }

        public String getName() {
            return Name;
        }

        public void setName(String name) {
            Name = name;
        }

        public String getValue() {
            return Value;
        }

        public void setValue(String value) {
            Value = value;
        }

        public int getOrdinal() {
            return Ordinal;
        }

        public void setOrdinal(int ordinal) {
            Ordinal = ordinal;
        }
    }
}
