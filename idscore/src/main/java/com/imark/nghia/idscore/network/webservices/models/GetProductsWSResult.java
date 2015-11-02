package com.imark.nghia.idscore.network.webservices.models;

import java.util.List;

/**
 * Created by Nghia-PC on 9/4/2015.
 */
public class GetProductsWSResult extends BaseWSResult {

    private List<SimpleProduct> ListSimpleProduct;

    public List<SimpleProduct> getListSimpleProduct() {
        return ListSimpleProduct;
    }

    public void setListSimpleProduct(List<SimpleProduct> listSimpleProduct) {
        ListSimpleProduct = listSimpleProduct;
    }

    public class SimpleProduct{
        private int ID;
        private String Code;
        private String ProductName;
        private int Type;
        private String Color;
        private String Price;

        public int getID() {
            return ID;
        }

        public void setID(int ID) {
            this.ID = ID;
        }

        public String getCode() {
            return Code;
        }

        public void setCode(String code) {
            Code = code;
        }

        public String getProductName() {
            return ProductName;
        }

        public void setProductName(String productName) {
            ProductName = productName;
        }

        public int getType() {
            return Type;
        }

        public void setType(int type) {
            Type = type;
        }

        public String getColor() {
            return Color;
        }

        public void setColor(String color) {
            Color = color;
        }

        public String getPrice() {
            return Price;
        }

        public void setPrice(String price) {
            Price = price;
        }
    }
}
