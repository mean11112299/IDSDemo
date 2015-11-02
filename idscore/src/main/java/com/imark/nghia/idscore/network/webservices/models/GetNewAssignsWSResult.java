package com.imark.nghia.idscore.network.webservices.models;

import java.util.List;

/**
 * Created by Nghia-PC on 9/15/2015.
 */
public class GetNewAssignsWSResult extends BaseWSResult {

    private List<SimpleOutlet> ListSimpleOutlet;

    public List<SimpleOutlet> getListSimpleOutlet() {
        return ListSimpleOutlet;
    }

    public void setListSimpleOutlet(List<SimpleOutlet> listSimpleOutlet) {
        ListSimpleOutlet = listSimpleOutlet;
    }

    public class SimpleOutlet {
        private long AssignID;
        private String DateAssign;
        private long OutletID;
        private String OutletCode;
        private String OutletName;
        private String Address;
        private String City;
        private String District;
        private int AreaID;

        public long getAssignID() {
            return AssignID;
        }

        public void setAssignID(long assignID) {
            AssignID = assignID;
        }

        public String getDateAssign() {
            return DateAssign;
        }

        public void setDateAssign(String dateAssign) {
            DateAssign = dateAssign;
        }

        public long getOutletID() {
            return OutletID;
        }

        public void setOutletID(long outletID) {
            OutletID = outletID;
        }

        public String getOutletCode() {
            return OutletCode;
        }

        public void setOutletCode(String outletCode) {
            OutletCode = outletCode;
        }

        public String getOutletName() {
            return OutletName;
        }

        public void setOutletName(String outletName) {
            OutletName = outletName;
        }

        public String getAddress() {
            return Address;
        }

        public void setAddress(String address) {
            Address = address;
        }

        public String getCity() {
            return City;
        }

        public void setCity(String city) {
            City = city;
        }

        public String getDistrict() {
            return District;
        }

        public void setDistrict(String district) {
            District = district;
        }

        public int getAreaID() {
            return AreaID;
        }

        public void setAreaID(int areaID) {
            AreaID = areaID;
        }
    }
}
