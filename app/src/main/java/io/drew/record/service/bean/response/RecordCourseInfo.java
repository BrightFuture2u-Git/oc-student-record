package io.drew.record.service.bean.response;

import java.io.Serializable;

/**
 * @Author: KKK
 * @CreateDate: 2020/9/8 3:01 PM
 */
public class RecordCourseInfo implements Serializable {

    /**
     * createTime : 2020-09-04 14:51:58
     * updateTime : 2020-09-04 15:18:33
     * status : 1
     * id : cos_04984b961bdd46878addf6c0c79de0c0
     * type : experience
     * name : 最长的围巾
     * icon :
     * coverImage : https://bf-images.oss-cn-shanghai.aliyuncs.com/202005082040546_1.png
     * minAge : 3
     * maxAge : 10
     * originalPrice : 1200
     * price : 998
     * tags : 赠送绘画大礼包！
     * introduction : 赠送绘画大礼包！
     * hadCourseWare : 1
     * buyNum : 1000
     * lectureNum : 5
     * body : 好绚丽的图文介绍
     * catalog : 这是课程列表
     * evaluation : 这是评价
     * seeForever : 0
     * seeEndTime : null
     * startTime : 2020-09-09 09:00:00
     * endTime : 2020-11-01 22:00:00
     * isBuy : 0
     * isCollect : 0
     * originalPriceStr : 12
     * priceStr : 9.98
     */

    private String createTime;
    private String updateTime;
    private int status;
    private String id;
    private String type;
    private String name;
    private String icon;
    private String coverImage;
    private int minAge;
    private int maxAge;
    private int originalPrice;
    private int price;
    private String tags;
    private String introduction;
    private int hadCourseWare;
    private int buyNum;
    private int lectureNum;
    private String body;
    private String catalog;
    private String evaluation;
    private int seeForever;
    private Object seeEndTime;
    private String startTime;
    private String endTime;
    private int isBuy;
    private int isCollect;
    private String originalPriceStr;
    private String priceStr;

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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public int getMinAge() {
        return minAge;
    }

    public void setMinAge(int minAge) {
        this.minAge = minAge;
    }

    public int getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(int maxAge) {
        this.maxAge = maxAge;
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

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public int getHadCourseWare() {
        return hadCourseWare;
    }

    public void setHadCourseWare(int hadCourseWare) {
        this.hadCourseWare = hadCourseWare;
    }

    public int getBuyNum() {
        return buyNum;
    }

    public void setBuyNum(int buyNum) {
        this.buyNum = buyNum;
    }

    public int getLectureNum() {
        return lectureNum;
    }

    public void setLectureNum(int lectureNum) {
        this.lectureNum = lectureNum;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getCatalog() {
        return catalog;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    public String getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(String evaluation) {
        this.evaluation = evaluation;
    }

    public int getSeeForever() {
        return seeForever;
    }

    public void setSeeForever(int seeForever) {
        this.seeForever = seeForever;
    }

    public Object getSeeEndTime() {
        return seeEndTime;
    }

    public void setSeeEndTime(Object seeEndTime) {
        this.seeEndTime = seeEndTime;
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

    public int getIsBuy() {
        return isBuy;
    }

    public void setIsBuy(int isBuy) {
        this.isBuy = isBuy;
    }

    public int getIsCollect() {
        return isCollect;
    }

    public void setIsCollect(int isCollect) {
        this.isCollect = isCollect;
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
