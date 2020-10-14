package io.drew.record.service.bean.response;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: KKK
 * @CreateDate: 2020/9/11 6:58 PM
 */
public class MyRecordWorks implements Serializable{


    /**
     * records : [{"id":9,"lectureName":"围巾的幻想","productImage":"https://bf-images.oss-cn-shanghai.aliyuncs.com/fourm__1600080565918_Image2020-09-14-18-49-09.jpg","productVoice":null,"productVoiceSeconds":"0","productTime":"2020-09-14","reviewVoice":null,"reviewVoiceSeconds":"0","reviewTime":null}]
     * total : 1
     * size : 10
     * current : 1
     * orders : []
     * searchCount : true
     * pages : 1
     */

    private int total;
    private int size;
    private int current;
    private boolean searchCount;
    private int pages;
    private List<RecordsBean> records;
    private List<?> orders;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public boolean isSearchCount() {
        return searchCount;
    }

    public void setSearchCount(boolean searchCount) {
        this.searchCount = searchCount;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public List<RecordsBean> getRecords() {
        return records;
    }

    public void setRecords(List<RecordsBean> records) {
        this.records = records;
    }

    public List<?> getOrders() {
        return orders;
    }

    public void setOrders(List<?> orders) {
        this.orders = orders;
    }

    public static class RecordsBean implements Serializable {
        /**
         * id : 9
         * lectureName : 围巾的幻想
         * productImage : https://bf-images.oss-cn-shanghai.aliyuncs.com/fourm__1600080565918_Image2020-09-14-18-49-09.jpg
         * productVoice : null
         * productVoiceSeconds : 0
         * productTime : 2020-09-14
         * reviewVoice : null
         * reviewVoiceSeconds : 0
         * reviewTime : null
         */

        private int id;
        private String courseId;
        private String lectureId;
        private String studentName;
        private String lectureName;
        private String productImage;
        private String productVoice;
        private String productVoiceSeconds;
        private String productTime;
        private String reviewVoice;
        private String reviewVoiceSeconds;
        private String reviewTime;

        public String getCourseId() {
            return courseId;
        }

        public void setCourseId(String courseId) {
            this.courseId = courseId;
        }

        public String getLectureId() {
            return lectureId;
        }

        public void setLectureId(String lectureId) {
            this.lectureId = lectureId;
        }

        public String getStudentName() {
            return studentName;
        }

        public void setStudentName(String studentName) {
            this.studentName = studentName;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getLectureName() {
            return lectureName;
        }

        public void setLectureName(String lectureName) {
            this.lectureName = lectureName;
        }

        public String getProductImage() {
            return productImage;
        }

        public void setProductImage(String productImage) {
            this.productImage = productImage;
        }

        public String getProductVoice() {
            return productVoice;
        }

        public void setProductVoice(String productVoice) {
            this.productVoice = productVoice;
        }

        public String getProductVoiceSeconds() {
            return productVoiceSeconds;
        }

        public void setProductVoiceSeconds(String productVoiceSeconds) {
            this.productVoiceSeconds = productVoiceSeconds;
        }

        public String getProductTime() {
            return productTime;
        }

        public void setProductTime(String productTime) {
            this.productTime = productTime;
        }

        public String getReviewVoice() {
            return reviewVoice;
        }

        public void setReviewVoice(String reviewVoice) {
            this.reviewVoice = reviewVoice;
        }

        public String getReviewVoiceSeconds() {
            return reviewVoiceSeconds;
        }

        public void setReviewVoiceSeconds(String reviewVoiceSeconds) {
            this.reviewVoiceSeconds = reviewVoiceSeconds;
        }

        public String getReviewTime() {
            return reviewTime;
        }

        public void setReviewTime(String reviewTime) {
            this.reviewTime = reviewTime;
        }
    }
}
