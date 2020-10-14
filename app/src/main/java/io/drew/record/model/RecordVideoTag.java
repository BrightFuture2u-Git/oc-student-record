package io.drew.record.model;

/**
 * @Author: KKK
 * @CreateDate: 2020/9/28 3:04 PM
 * 录播课观看记录模型
 */
public class RecordVideoTag {
    private String courseLectureId;
    private String endTime;
    private String startTime;
    private int viewSeconds;

    public RecordVideoTag(String courseLectureId) {
        this.courseLectureId = courseLectureId;
    }

    public String getCourseLectureId() {
        return courseLectureId;
    }

    public void setCourseLectureId(String courseLectureId) {
        this.courseLectureId = courseLectureId;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public int getViewSeconds() {
        return viewSeconds;
    }

    public void setViewSeconds(int viewSeconds) {
        this.viewSeconds = viewSeconds;
    }
}
