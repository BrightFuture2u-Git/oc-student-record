package io.drew.record.service.bean.response;

import java.util.List;

/**
 * @Author: KKK
 * @CreateDate: 2020/9/12 3:05 PM
 */
public class RecordOrders {


    /**
     * list : [{"address":"","consigneeName":"","consigneePhone":"","createTime":"","district":"","endTime":"","id":"","name":"","orderId":"","paymentPrice":"","price":0,"priceStr":"","startTime":""}]
     * pagination : {"current":0,"pageCount":0,"pageSize":0,"total":0}
     */

    private PaginationBean pagination;
    private List<RecordOrder> list;

    public PaginationBean getPagination() {
        return pagination;
    }

    public void setPagination(PaginationBean pagination) {
        this.pagination = pagination;
    }

    public List<RecordOrder> getList() {
        return list;
    }

    public void setList(List<RecordOrder> list) {
        this.list = list;
    }

    public static class PaginationBean {
        /**
         * current : 0
         * pageCount : 0
         * pageSize : 0
         * total : 0
         */

        private int current;
        private int pageCount;
        private int pageSize;
        private int total;

        public int getCurrent() {
            return current;
        }

        public void setCurrent(int current) {
            this.current = current;
        }

        public int getPageCount() {
            return pageCount;
        }

        public void setPageCount(int pageCount) {
            this.pageCount = pageCount;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }
    }

    public static class RecordOrder {
        /**
         * address :
         * consigneeName :
         * consigneePhone :
         * createTime :
         * district :
         * endTime :
         * id :
         * name :
         * orderId :
         * paymentPrice :
         * price : 0
         * priceStr :
         * startTime :
         */

        private String address;
        private String consigneeName;
        private String consigneePhone;
        private String createTime;
        private String district;
        private String endTime;
        private String id;
        private String name;
        private String orderId;
        private String paymentPrice;
        private int price;
        private int lectureNum;
        private String priceStr;
        private String startTime;

        public int getLectureNum() {
            return lectureNum;
        }

        public void setLectureNum(int lectureNum) {
            this.lectureNum = lectureNum;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getConsigneeName() {
            return consigneeName;
        }

        public void setConsigneeName(String consigneeName) {
            this.consigneeName = consigneeName;
        }

        public String getConsigneePhone() {
            return consigneePhone;
        }

        public void setConsigneePhone(String consigneePhone) {
            this.consigneePhone = consigneePhone;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getDistrict() {
            return district;
        }

        public void setDistrict(String district) {
            this.district = district;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public String getPaymentPrice() {
            return paymentPrice;
        }

        public void setPaymentPrice(String paymentPrice) {
            this.paymentPrice = paymentPrice;
        }

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public String getPriceStr() {
            return priceStr;
        }

        public void setPriceStr(String priceStr) {
            this.priceStr = priceStr;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }
    }
}
