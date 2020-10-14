package io.drew.record.service.bean.response;

import java.io.Serializable;

/**
 * @Author: KKK
 * @CreateDate: 2020/9/10 7:07 PM
 */
public class RecordCourseLecture implements Serializable {


    /**
     * icon :
     * id :
     * isOpen : 0
     * name :
     * openTime :
     * runningSeconds : 0
     * videoUuid :
     * viewTimes : 0
     */

    private String icon;
    private String id;
    private int isOpen;
    private String name;
    private String openTime;
    private int runningSeconds;
    private String videoUuid;
    private int viewTimes;

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getIsOpen() {
        return isOpen;
    }

    public void setIsOpen(int isOpen) {
        this.isOpen = isOpen;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOpenTime() {
        return openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

    public int getRunningSeconds() {
        return runningSeconds;
    }

    public void setRunningSeconds(int runningSeconds) {
        this.runningSeconds = runningSeconds;
    }

    public String getVideoUuid() {
        return videoUuid;
    }

    public void setVideoUuid(String videoUuid) {
        this.videoUuid = videoUuid;
    }

    public int getViewTimes() {
        return viewTimes;
    }

    public void setViewTimes(int viewTimes) {
        this.viewTimes = viewTimes;
    }
}
