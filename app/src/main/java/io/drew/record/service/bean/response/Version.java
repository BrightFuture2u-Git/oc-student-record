package io.drew.record.service.bean.response;

/**
 * @Author: KKK
 * @CreateDate: 2020/6/1 8:13 PM
 */
public class Version {


    /**
     * id : 4
     * version : 2.0.0
     * appType : android
     * description : 1.更牛版本
     * isForceUpdate : 1
     * url : https://bf-apk.oss-cn-hangzhou.aliyuncs.com/drew_release_v1.4_06_16_19_23.apk
     * createTime : 2020-06-17 09:44:55
     */

    private int id;
    private String version;
    private String appType;
    private String description;
    private String isForceUpdate;
    private String url;
    private String createTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getAppType() {
        return appType;
    }

    public void setAppType(String appType) {
        this.appType = appType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIsForceUpdate() {
        return isForceUpdate;
    }

    public void setIsForceUpdate(String isForceUpdate) {
        this.isForceUpdate = isForceUpdate;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
