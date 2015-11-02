package com.imark.nghia.idscore.network.webservices.models;

import java.util.List;

/**
 * Created by Nghia-PC on 9/4/2015.
 */
public class GetOutletsWSResult extends BaseWSResult {

    public List<SimpleOutlet> ListSimpleOutlet;

    public List<SimpleOutlet> getListSimpleOutlet() {
        return ListSimpleOutlet;
    }

    public void setListSimpleOutlet(List<SimpleOutlet> listSimpleOutlet) {
        ListSimpleOutlet = listSimpleOutlet;
    }

    public class SimpleOutlet {
        private String OutletID;
        private String SessionCode;
        private String OutletCode;
        private String OutletName;
        private String OutletDistrict;
        private String OutletCity;
        private String Address;

        public String getOutletID() {
            return OutletID;
        }

        public void setOutletID(String outletID) {
            OutletID = outletID;
        }

        public String getSessionCode() {
            return SessionCode;
        }

        public void setSessionCode(String sessionCode) {
            SessionCode = sessionCode;
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

        public String getOutletDistrict() {
            return OutletDistrict;
        }

        public void setOutletDistrict(String outletDistrict) {
            OutletDistrict = outletDistrict;
        }

        public String getOutletCity() {
            return OutletCity;
        }

        public void setOutletCity(String outletCity) {
            OutletCity = outletCity;
        }

        public String getAddress() {
            return Address;
        }

        public void setAddress(String address) {
            Address = address;
        }
    }
}
