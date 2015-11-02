package com.windyroad.nghia.common.models;

/**
 * Created by Nghia-PC on 8/22/2015.
 * Hành động thực hiện nhiều
 * Trả về Tổng số thành công, Tổng thất bại
 */
public class BatchActionResult {
    private int totalSuccess;
    private int totalFail;
    private int total;

    public BatchActionResult() {
    }

    public BatchActionResult(int totalSuccess, int totalFail) {
        this.totalSuccess = totalSuccess;
        this.totalFail = totalFail;
    }

    public BatchActionResult(int total) {
        this.total = total;
        this.totalSuccess = 0;
        this.totalFail = 0;
    }

    public int getTotalSuccess() {
        return totalSuccess;
    }

    public void setTotalSuccess(int totalSuccess) {
        this.totalSuccess = totalSuccess;
    }

    public int getTotalFail() {
        return totalFail;
    }

    public void setTotalFail(int totalFail) {
        this.totalFail = totalFail;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    //----- Method ------
    public void addSuccess(int number){
        this.totalSuccess += number;
    }
    public void addFail(int number){
        this.totalFail += number;
    }

    /** Lấy vị trí hiện tại */
    public int getCurrentIndex(){
        return totalSuccess + totalFail;
    }

    /** Lấy phần trăm đã thực hiện */
    public int getPercent(){
        int index = totalSuccess + totalFail;
        return PercentHandle.getPercent(total, 0, index);
    }
}
