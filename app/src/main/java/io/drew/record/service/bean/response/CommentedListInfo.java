package io.drew.record.service.bean.response;

import java.util.List;

/**
 * @Author: KKK
 * @CreateDate: 2020/5/20 6:56 PM
 * 被评论列表 model
 */
public class CommentedListInfo {

    /**
     * records : [{"createTime":"2020-05-20 18:45:55","status":0,"id":64,"articleId":77,"content":"早睡早起","userId":"usr_00004","isAnonymity":0,"likeNum":0,"isRead":1,"nickname":"二师兄","headImage":"https://image.brightfuture360.com/user/head-00001.png","articleContent":"125566669985555441588555666666","imageList":["https://bf-images.oss-cn-shanghai.aliyuncs.com/fourm_18900000005_1589964180485_IMG_20200515_113923.jpg"],"formatTime":"2分钟前"},{"createTime":"2020-05-20 18:45:50","status":0,"id":63,"articleId":79,"content":"我让我朋友","userId":"usr_00004","isAnonymity":0,"likeNum":0,"isRead":1,"nickname":"二师兄","headImage":"https://image.brightfuture360.com/user/head-00001.png","articleContent":"2","imageList":["https://bf-images.oss-cn-shanghai.aliyuncs.com/fourm_18900000005_1589964688176_IMG_20200515_113929.jpg","https://bf-images.oss-cn-shanghai.aliyuncs.com/fourm_18900000005_1589964688438_IMG_20200515_113931.jpg"],"formatTime":"2分钟前"},{"createTime":"2020-05-20 18:45:47","status":0,"id":62,"articleId":79,"content":"你搜索","userId":"usr_00004","isAnonymity":0,"likeNum":0,"isRead":1,"nickname":"二师兄","headImage":"https://image.brightfuture360.com/user/head-00001.png","articleContent":"2","imageList":["https://bf-images.oss-cn-shanghai.aliyuncs.com/fourm_18900000005_1589964688176_IMG_20200515_113929.jpg","https://bf-images.oss-cn-shanghai.aliyuncs.com/fourm_18900000005_1589964688438_IMG_20200515_113931.jpg"],"formatTime":"2分钟前"},{"createTime":"2020-05-20 18:45:39","status":0,"id":61,"articleId":79,"content":"你搜索","userId":"usr_00004","isAnonymity":0,"likeNum":0,"isRead":1,"nickname":"二师兄","headImage":"https://image.brightfuture360.com/user/head-00001.png","articleContent":"2","imageList":["https://bf-images.oss-cn-shanghai.aliyuncs.com/fourm_18900000005_1589964688176_IMG_20200515_113929.jpg","https://bf-images.oss-cn-shanghai.aliyuncs.com/fourm_18900000005_1589964688438_IMG_20200515_113931.jpg"],"formatTime":"2分钟前"},{"createTime":"2020-05-20 18:45:30","status":0,"id":60,"articleId":81,"content":"太平洋寿险","userId":"usr_00004","isAnonymity":0,"likeNum":0,"isRead":1,"nickname":"二师兄","headImage":"https://image.brightfuture360.com/user/head-00001.png","articleContent":"4","imageList":["https://bf-images.oss-cn-shanghai.aliyuncs.com/fourm_18900000005_1589964727616_IMG_20200515_113927.jpg","https://bf-images.oss-cn-shanghai.aliyuncs.com/fourm_18900000005_1589964727814_IMG_20200515_113928.jpg","https://bf-images.oss-cn-shanghai.aliyuncs.com/fourm_18900000005_1589964728018_IMG_20200515_113929.jpg","https://bf-images.oss-cn-shanghai.aliyuncs.com/fourm_18900000005_1589964728176_IMG_20200515_113931.jpg"],"formatTime":"2分钟前"},{"createTime":"2020-05-20 18:45:24","status":0,"id":59,"articleId":82,"content":"几十块钱","userId":"usr_00004","isAnonymity":0,"likeNum":0,"isRead":1,"nickname":"二师兄","headImage":"https://image.brightfuture360.com/user/head-00001.png","articleContent":"5","imageList":["https://bf-images.oss-cn-shanghai.aliyuncs.com/fourm_18900000005_1589964751879_IMG_20200515_113926.jpg","https://bf-images.oss-cn-shanghai.aliyuncs.com/fourm_18900000005_1589964752295_IMG_20200515_113927.jpg","https://bf-images.oss-cn-shanghai.aliyuncs.com/fourm_18900000005_1589964752522_IMG_20200515_113928.jpg","https://bf-images.oss-cn-shanghai.aliyuncs.com/fourm_18900000005_1589964752753_IMG_20200515_113929.jpg","https://bf-images.oss-cn-shanghai.aliyuncs.com/fourm_18900000005_1589964752976_IMG_20200515_113931.jpg"],"formatTime":"2分钟前"},{"createTime":"2020-05-20 18:45:17","status":0,"id":58,"articleId":83,"content":"不错","userId":"usr_00004","isAnonymity":0,"likeNum":0,"isRead":1,"nickname":"二师兄","headImage":"https://image.brightfuture360.com/user/head-00001.png","articleContent":"兔兔","imageList":["https://bf-images.oss-cn-shanghai.aliyuncs.com/fourm_18900000005_1589965647885_IMG_20200515_113924.jpg","https://bf-images.oss-cn-shanghai.aliyuncs.com/fourm_18900000005_1589965648305_IMG_20200515_113929.jpg","https://bf-images.oss-cn-shanghai.aliyuncs.com/fourm_18900000005_1589965648600_IMG_20200515_113931.jpg"],"formatTime":"2分钟前"}]
     * total : 7
     * size : 10
     * current : 1
     * orders : [{"column":"id","asc":false}]
     * searchCount : true
     * pages : 1
     */

    private int total;
    private int size;
    private int current;
    private boolean searchCount;
    private int pages;
    private List<RecordsBean> records;
    private List<OrdersBean> orders;

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

    public List<OrdersBean> getOrders() {
        return orders;
    }

    public void setOrders(List<OrdersBean> orders) {
        this.orders = orders;
    }

    public static class RecordsBean {
        /**
         * createTime : 2020-05-20 18:45:55
         * status : 0
         * id : 64
         * articleId : 77
         * content : 早睡早起
         * userId : usr_00004
         * isAnonymity : 0
         * likeNum : 0
         * isRead : 1
         * nickname : 二师兄
         * headImage : https://image.brightfuture360.com/user/head-00001.png
         * articleContent : 125566669985555441588555666666
         * imageList : ["https://bf-images.oss-cn-shanghai.aliyuncs.com/fourm_18900000005_1589964180485_IMG_20200515_113923.jpg"]
         * formatTime : 2分钟前
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
        private String articleContent;
        private String formatTime;
        private List<String> imageList;

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

        public String getArticleContent() {
            return articleContent;
        }

        public void setArticleContent(String articleContent) {
            this.articleContent = articleContent;
        }

        public String getFormatTime() {
            return formatTime;
        }

        public void setFormatTime(String formatTime) {
            this.formatTime = formatTime;
        }

        public List<String> getImageList() {
            return imageList;
        }

        public void setImageList(List<String> imageList) {
            this.imageList = imageList;
        }
    }

    public static class OrdersBean {
        /**
         * column : id
         * asc : false
         */

        private String column;
        private boolean asc;

        public String getColumn() {
            return column;
        }

        public void setColumn(String column) {
            this.column = column;
        }

        public boolean isAsc() {
            return asc;
        }

        public void setAsc(boolean asc) {
            this.asc = asc;
        }
    }
}
