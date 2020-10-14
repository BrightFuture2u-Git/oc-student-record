package io.drew.record.service.bean.response;

/**
 * @Author: KKK
 * @CreateDate: 2020/9/14 11:39 AM
 */
public class UnUploadRecordLecture {

    /**
     * courseId :
     * id :
     * lectureId :
     * lectureName :
     * openTime :
     */

    private String courseId;
    private String id;
    private String lectureId;
    private String lectureName;
    private String openTime;

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLectureId() {
        return lectureId;
    }

    public void setLectureId(String lectureId) {
        this.lectureId = lectureId;
    }

    public String getLectureName() {
        return lectureName;
    }

    public void setLectureName(String lectureName) {
        this.lectureName = lectureName;
    }

    public String getOpenTime() {
        return openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }
}
