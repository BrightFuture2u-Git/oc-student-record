package io.drew.record.service.bean.response;

import java.util.List;

/**
 * @Author: KKK
 * @CreateDate: 2020/5/18 6:12 PM
 */
public class CommentsInfo {

    /**
     * records : [{"createTime":"2020-05-07 22:11:35","status":1,"id":33,"articleId":40,"content":"厉害啊","userId":"usr_1a92ebe954e841878ba098ec3eba346e","isAnonymity":0,"likeNum":0,"isRead":1,"nickname":"茜茜家长","headImage":"https://image.brightfuture360.com/course/20200401194028O0kemsXD.png","isLiked":0,"formatTime":"05/07"}]
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

    public static class RecordsBean {
        /**
         * createTime : 2020-05-07 22:11:35
         * status : 1
         * id : 33
         * articleId : 40
         * content : 厉害啊
         * userId : usr_1a92ebe954e841878ba098ec3eba346e
         * isAnonymity : 0
         * likeNum : 0
         * isRead : 1
         * nickname : 茜茜家长
         * headImage : https://image.brightfuture360.com/course/20200401194028O0kemsXD.png
         * isLiked : 0
         * formatTime : 05/07
         */

        private String createTime;
        private int status;
        private int id;
        private int articleId;
        private String content;
        private String userId;
        private int isAnonymity;
        private int likeNum;
        private int isRead;
        private String nickname;
        private String headImage;
        private int isLiked;
        private String formatTime;

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
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

        public int getArticleId() {
            return articleId;
        }

        public void setArticleId(int articleId) {
            this.articleId = articleId;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public int getIsAnonymity() {
            return isAnonymity;
        }

        public void setIsAnonymity(int isAnonymity) {
            this.isAnonymity = isAnonymity;
        }

        public int getLikeNum() {
            return likeNum;
        }

        public void setLikeNum(int likeNum) {
            this.likeNum = likeNum;
        }

        public int getIsRead() {
            return isRead;
        }

        public void setIsRead(int isRead) {
            this.isRead = isRead;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getHeadImage() {
            return headImage;
        }

        public void setHeadImage(String headImage) {
            this.headImage = headImage;
        }

        public int getIsLiked() {
            return isLiked;
        }

        public void setIsLiked(int isLiked) {
            this.isLiked = isLiked;
        }

        public String getFormatTime() {
            return formatTime;
        }

        public void setFormatTime(String formatTime) {
            this.formatTime = formatTime;
        }
    }
}
