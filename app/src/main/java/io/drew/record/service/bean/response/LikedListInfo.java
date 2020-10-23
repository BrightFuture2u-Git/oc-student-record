package io.drew.record.service.bean.response;

import java.util.List;

/**
 * @Author: KKK
 * @CreateDate: 2020/5/21 10:34 AM
 */
public class LikedListInfo {

    /**
     * records : [{"id":89,"itemId":57,"type":2,"itemUserId":"usr_00007","userId":"usr_00005","createTime":"2020-05-21 10:30:26","isDeleted":0,"isRead":0,"nickname":"Sha Wujing","headImage":"https://bf-images.oss-cn-shanghai.aliyuncs.com/fourm/3d4c2f018c9d7f5f9301138f45b4b429_158925209131684_1.png","articleContent":"兔兔","commentContent":"挺好的","imageList":["https://bf-images.oss-cn-shanghai.aliyuncs.com/fourm_18900000005_1589965647885_IMG_20200515_113924.jpg","https://bf-images.oss-cn-shanghai.aliyuncs.com/fourm_18900000005_1589965648305_IMG_20200515_113929.jpg","https://bf-images.oss-cn-shanghai.aliyuncs.com/fourm_18900000005_1589965648600_IMG_20200515_113931.jpg"],"formatTime":"1分钟前","articleId":83,"articleUserName":"家长"},{"id":88,"itemId":75,"type":1,"itemUserId":"usr_00007","userId":"usr_00005","createTime":"2020-05-21 10:30:18","isDeleted":0,"isRead":0,"nickname":"Sha Wujing","headImage":"https://bf-images.oss-cn-shanghai.aliyuncs.com/fourm/3d4c2f018c9d7f5f9301138f45b4b429_158925209131684_1.png","articleContent":"真的假的","commentContent":null,"imageList":["https://bf-images.oss-cn-shanghai.aliyuncs.com/fourm_18900000005_1589963255604_IMG_20200515_113904.jpg"],"formatTime":"1分钟前","articleId":75,"articleUserName":null},{"id":87,"itemId":76,"type":1,"itemUserId":"usr_00007","userId":"usr_00005","createTime":"2020-05-21 10:30:16","isDeleted":0,"isRead":0,"nickname":"Sha Wujing","headImage":"https://bf-images.oss-cn-shanghai.aliyuncs.com/fourm/3d4c2f018c9d7f5f9301138f45b4b429_158925209131684_1.png","articleContent":"1455666","commentContent":null,"imageList":["https://bf-images.oss-cn-shanghai.aliyuncs.com/fourm_18900000005_1589963655030_IMG_20200515_113911.jpg"],"formatTime":"1分钟前","articleId":76,"articleUserName":null},{"id":86,"itemId":77,"type":1,"itemUserId":"usr_00007","userId":"usr_00005","createTime":"2020-05-21 10:30:14","isDeleted":0,"isRead":0,"nickname":"Sha Wujing","headImage":"https://bf-images.oss-cn-shanghai.aliyuncs.com/fourm/3d4c2f018c9d7f5f9301138f45b4b429_158925209131684_1.png","articleContent":"125566669985555441588555666666","commentContent":null,"imageList":["https://bf-images.oss-cn-shanghai.aliyuncs.com/fourm_18900000005_1589964180485_IMG_20200515_113923.jpg"],"formatTime":"1分钟前","articleId":77,"articleUserName":null},{"id":85,"itemId":78,"type":1,"itemUserId":"usr_00007","userId":"usr_00005","createTime":"2020-05-21 10:30:12","isDeleted":0,"isRead":0,"nickname":"Sha Wujing","headImage":"https://bf-images.oss-cn-shanghai.aliyuncs.com/fourm/3d4c2f018c9d7f5f9301138f45b4b429_158925209131684_1.png","articleContent":"摸摸www","commentContent":null,"imageList":["https://bf-images.oss-cn-shanghai.aliyuncs.com/fourm_18900000005_1589964661829_IMG_20200515_113917.jpg"],"formatTime":"1分钟前","articleId":78,"articleUserName":null},{"id":84,"itemId":79,"type":1,"itemUserId":"usr_00007","userId":"usr_00005","createTime":"2020-05-21 10:30:11","isDeleted":0,"isRead":0,"nickname":"Sha Wujing","headImage":"https://bf-images.oss-cn-shanghai.aliyuncs.com/fourm/3d4c2f018c9d7f5f9301138f45b4b429_158925209131684_1.png","articleContent":"2","commentContent":null,"imageList":["https://bf-images.oss-cn-shanghai.aliyuncs.com/fourm_18900000005_1589964688176_IMG_20200515_113929.jpg","https://bf-images.oss-cn-shanghai.aliyuncs.com/fourm_18900000005_1589964688438_IMG_20200515_113931.jpg"],"formatTime":"1分钟前","articleId":79,"articleUserName":null},{"id":83,"itemId":80,"type":1,"itemUserId":"usr_00007","userId":"usr_00005","createTime":"2020-05-21 10:30:09","isDeleted":0,"isRead":0,"nickname":"Sha Wujing","headImage":"https://bf-images.oss-cn-shanghai.aliyuncs.com/fourm/3d4c2f018c9d7f5f9301138f45b4b429_158925209131684_1.png","articleContent":"3","commentContent":null,"imageList":["https://bf-images.oss-cn-shanghai.aliyuncs.com/fourm_18900000005_1589964703776_IMG_20200515_113928.jpg","https://bf-images.oss-cn-shanghai.aliyuncs.com/fourm_18900000005_1589964704007_IMG_20200515_113929.jpg","https://bf-images.oss -cn-shanghai.aliyuncs.com/fourm_18900000005_1589964704184_IMG_20200515_113931.jpg"],"formatTime":"1分钟前","articleId":80,"articleUserName":null},{"id":82,"itemId":81,"type":1,"itemUserId":"usr_00007","userId":"usr_00005","createTime":"2020-05-21 10:30:07","isDeleted":0,"isRead":0,"nickname":"Sha Wujing","headImage":"https://bf-images.oss-cn-shanghai.aliyuncs.com/fourm/3d4c2f018c9d7f5f9301138f45b4b429_158925209131684_1.png","articleContent":"4","commentContent":null,"imageList":["https://bf-images.oss-cn-shanghai.aliyuncs.com/fourm_18900000005_1589964727616_IMG_20200515_113927.jpg","https://bf-images.oss-cn-shanghai.aliyuncs.com/fourm_18900000005_1589964727814_IMG_20200515_113928.jpg","https://bf-images.oss-cn-shanghai.aliyuncs.com/fourm_18900000005_1589964728018_IMG_20200515_113929.jpg","https://bf-images.oss-cn-shanghai.aliyuncs.com/fourm_18900000005_1589964728176_IMG_20200515_113931.jpg"],"formatTime":"1分钟前","articleId":81,"articleUserName":null},{"id":81,"itemId":82,"type":1,"itemUserId":"usr_00007","userId":"usr_00005","createTime":"2020-05-21 10:30:05","isDeleted":0,"isRead":0,"nickname":"Sha Wujing","headImage":"https://bf-images.oss-cn-shanghai.aliyuncs.com/fourm/3d4c2f018c9d7f5f9301138f45b4b429_158925209131684_1.png","articleContent":"5","commentContent":null,"imageList":["https://bf-images.oss-cn-shanghai.aliyuncs.com/fourm_18900000005_1589964751879_IMG_20200515_113926.jpg","https://bf-images.oss-cn-shanghai.aliyuncs.com/fourm_18900000005_1589964752295_IMG_20200515_113927.jpg","https://bf-images.oss-cn-shanghai.aliyuncs.com/fourm_18900000005_1589964752522_IMG_20200515_113928.jpg","https://bf-images.oss-cn-shanghai.aliyuncs.com/fourm_18900000005_1589964752753_IMG_20200515_113929.jpg","https://bf-images.oss-cn-shanghai.aliyuncs.com/fourm_18900000005_1589964752976_IMG_20200515_113931.jpg"],"formatTime":"1分钟前","articleId":82,"articleUserName":null},{"id":80,"itemId":83,"type":1,"itemUserId":"usr_00007","userId":"usr_00005","createTime":"2020-05-21 10:30:03","isDeleted":0,"isRead":0,"nickname":"Sha Wujing","headImage":"https://bf-images.oss-cn-shanghai.aliyuncs.com/fourm/3d4c2f018c9d7f5f9301138f45b4b429_158925209131684_1.png","articleContent":"兔兔","commentContent":null,"imageList":["https://bf-images.oss-cn-shanghai.aliyuncs.com/fourm_18900000005_1589965647885_IMG_20200515_113924.jpg","https://bf-images.oss-cn-shanghai.aliyuncs.com/fourm_18900000005_1589965648305_IMG_20200515_113929.jpg","https://bf-images.oss-cn-shanghai.aliyuncs.com/fourm_18900000005_1589965648600_IMG_20200515_113931.jpg"],"formatTime":"1分钟前","articleId":83,"articleUserName":null}]
     * total : 10
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
         * id : 89
         * itemId : 57
         * type : 2
         * itemUserId : usr_00007
         * userId : usr_00005
         * createTime : 2020-05-21 10:30:26
         * isDeleted : 0
         * isRead : 0
         * nickname : Sha Wujing
         * headImage : https://bf-images.oss-cn-shanghai.aliyuncs.com/fourm/3d4c2f018c9d7f5f9301138f45b4b429_158925209131684_1.png
         * articleContent : 兔兔
         * commentContent : 挺好的
         * imageList : ["https://bf-images.oss-cn-shanghai.aliyuncs.com/fourm_18900000005_1589965647885_IMG_20200515_113924.jpg","https://bf-images.oss-cn-shanghai.aliyuncs.com/fourm_18900000005_1589965648305_IMG_20200515_113929.jpg","https://bf-images.oss-cn-shanghai.aliyuncs.com/fourm_18900000005_1589965648600_IMG_20200515_113931.jpg"]
         * formatTime : 1分钟前
         * articleId : 83
         * articleUserName : 家长
         */

        private int id;
        private int itemId;
        private int type;
        private String itemUserId;
        private String userId;
        private String createTime;
        private int isDeleted;
        private int isRead;
        private String nickname;
        private String headImage;
        private String articleContent;
        private String commentContent;
        private String formatTime;
        private int articleId;
        private String articleUserName;
        private List<String> imageList;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getItemId() {
            return itemId;
        }

        public void setItemId(int itemId) {
            this.itemId = itemId;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getItemUserId() {
            return itemUserId;
        }

        public void setItemUserId(String itemUserId) {
            this.itemUserId = itemUserId;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public int getIsDeleted() {
            return isDeleted;
        }

        public void setIsDeleted(int isDeleted) {
            this.isDeleted = isDeleted;
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

        public String getCommentContent() {
            return commentContent;
        }

        public void setCommentContent(String commentContent) {
            this.commentContent = commentContent;
        }

        public String getFormatTime() {
            return formatTime;
        }

        public void setFormatTime(String formatTime) {
            this.formatTime = formatTime;
        }

        public int getArticleId() {
            return articleId;
        }

        public void setArticleId(int articleId) {
            this.articleId = articleId;
        }

        public String getArticleUserName() {
            return articleUserName;
        }

        public void setArticleUserName(String articleUserName) {
            this.articleUserName = articleUserName;
        }

        public List<String> getImageList() {
            return imageList;
        }

        public void setImageList(List<String> imageList) {
            this.imageList = imageList;
        }
    }
}
