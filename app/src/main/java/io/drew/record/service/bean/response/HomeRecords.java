package io.drew.record.service.bean.response;


import java.util.List;

/**
 * @Author: KKK
 * @CreateDate: 2020/9/8 9:59 AM
 */
public class HomeRecords {

    private List<RecordCourseBean> experienceList;
    private List<RecordCourseBean> hotList;

    public List<RecordCourseBean> getExperienceList() {
        return experienceList;
    }

    public void setExperienceList(List<RecordCourseBean> experienceList) {
        this.experienceList = experienceList;
    }

    public List<RecordCourseBean> getHotList() {
        return hotList;
    }

    public void setHotList(List<RecordCourseBean> hotList) {
        this.hotList = hotList;
    }

    public static class RecordCourseBean {
        /**
         * id : cos_04984b961bdd46878addf6c0c79de0c0
         * type : experience
         * name : 最长的围巾
         * originalPrice : 1200
         * price : 998
         * introduction : 赠送绘画大礼包！
         * buyNum : 1000
         * startTime : 2020-09-09
         * endTime : 2020-11-01
         * originalPriceStr : 12
         * priceStr : 9.98
         */

        private String id;
        private String type;
        private String name;
        private int originalPrice;
        private int price;
        private String tags;
        private int buyNum;
        private String startTime;
        private String endTime;
        private String originalPriceStr;
        private String priceStr;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getOriginalPrice() {
            return originalPrice;
        }

        public void setOriginalPrice(int originalPrice) {
            this.originalPrice = originalPrice;
        }

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public String getTags() {
            return tags;
        }

        public void setTags(String tags) {
            this.tags = tags;
        }

        public int getBuyNum() {
            return buyNum;
        }

        public void setBuyNum(int buyNum) {
            this.buyNum = buyNum;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getOriginalPriceStr() {
            return originalPriceStr;
        }

        public void setOriginalPriceStr(String originalPriceStr) {
            this.originalPriceStr = originalPriceStr;
        }

        public String getPriceStr() {
            return priceStr;
        }

        public void setPriceStr(String priceStr) {
            this.priceStr = priceStr;
        }
    }

}
