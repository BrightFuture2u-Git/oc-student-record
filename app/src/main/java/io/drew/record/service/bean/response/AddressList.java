package io.drew.record.service.bean.response;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: KKK
 * @CreateDate: 2020/9/9 4:27 PM
 */
public class AddressList implements Serializable {


    /**
     * list : [{"createTime":"2020-09-09 18:59:52","updateTime":"2020-09-12 20:12:47","status":1,"id":11,"userId":"usr_00007","phone":"18900000005","name":"康师傅","provinceId":23,"cityId":375,"districtId":2447,"district":"四川省成都市锦江区","address":"详细地址天府软件园","isDefault":1,"provinceName":"四川省","cityName":"成都市","districtName":"锦江区"}]
     * pagination : {"total":1,"pageSize":100,"current":1,"pageCount":1}
     */

    private PaginationBean pagination;
    private List<Address> list;

    public PaginationBean getPagination() {
        return pagination;
    }

    public void setPagination(PaginationBean pagination) {
        this.pagination = pagination;
    }

    public List<Address> getList() {
        return list;
    }

    public void setList(List<Address> list) {
        this.list = list;
    }

    public static class PaginationBean {
        /**
         * total : 1
         * pageSize : 100
         * current : 1
         * pageCount : 1
         */

        private int total;
        private int pageSize;
        private int current;
        private int pageCount;

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

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
    }

    public static class Address implements Serializable{
        /**
         * createTime : 2020-09-09 18:59:52
         * updateTime : 2020-09-12 20:12:47
         * status : 1
         * id : 11
         * userId : usr_00007
         * phone : 18900000005
         * name : 康师傅
         * provinceId : 23
         * cityId : 375
         * districtId : 2447
         * district : 四川省成都市锦江区
         * address : 详细地址天府软件园
         * isDefault : 1
         * provinceName : 四川省
         * cityName : 成都市
         * districtName : 锦江区
         */

        private String createTime;
        private String updateTime;
        private int status;
        private int id;
        private String userId;
        private String phone;
        private String name;
        private int provinceId;
        private int cityId;
        private int districtId;
        private String district;
        private String address;
        private int isDefault;
        private String provinceName;
        private String cityName;
        private String districtName;

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getProvinceId() {
            return provinceId;
        }

        public void setProvinceId(int provinceId) {
            this.provinceId = provinceId;
        }

        public int getCityId() {
            return cityId;
        }

        public void setCityId(int cityId) {
            this.cityId = cityId;
        }

        public int getDistrictId() {
            return districtId;
        }

        public void setDistrictId(int districtId) {
            this.districtId = districtId;
        }

        public String getDistrict() {
            return district;
        }

        public void setDistrict(String district) {
            this.district = district;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public int getIsDefault() {
            return isDefault;
        }

        public void setIsDefault(int isDefault) {
            this.isDefault = isDefault;
        }

        public String getProvinceName() {
            return provinceName;
        }

        public void setProvinceName(String provinceName) {
            this.provinceName = provinceName;
        }

        public String getCityName() {
            return cityName;
        }

        public void setCityName(String cityName) {
            this.cityName = cityName;
        }

        public String getDistrictName() {
            return districtName;
        }

        public void setDistrictName(String districtName) {
            this.districtName = districtName;
        }
    }
}
