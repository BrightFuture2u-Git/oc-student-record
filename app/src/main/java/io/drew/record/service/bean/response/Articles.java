package io.drew.record.service.bean.response;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: KKK
 * @CreateDate: 2020/5/13 5:17 PM
 */
public class Articles {

    /**
     * records : [{"createTime":"2020-05-07 17:14:59","status":1,"id":40,"type":"other","content":"五一期间宝贝画的爸爸妈妈在家里的样子～～","userId":"usr_eaaa5e8b30d04919ad54f88155aa8013","isAnonymity":0,"likeNum":2,"commentNum":1,"imageList":["https://image.brightfuture360.com/forum/20200507171423cKqLs1if.jpg"],"nickname":"朱伟铭","headImage":"https://image.brightfuture360.com/user/head-00001.png","isCollected":0,"isLiked":0,"userHeadList":null,"formatTime":"6天前"},{"createTime":"2020-04-22 12:02:22","status":1,"id":30,"type":"other","content":"我家闺女疫情期间画得我们一家人👨\u200d👩\u200d👧\u200d👧 哈哈哈 我的头发是不是太少了一些。。","userId":"usr_1a92ebe954e841878ba098ec3eba346e","isAnonymity":0,"likeNum":7,"commentNum":2,"imageList":["https://bf-images.oss-cn-shanghai.aliyuncs.com/forum/20200401114353TCw2A7GF.jpg"],"nickname":"茜茜家长","headImage":"https://image.brightfuture360.com/course/20200401194028O0kemsXD.png","isCollected":0,"isLiked":0,"userHeadList":null,"formatTime":"04/22"},{"createTime":"2020-04-21 21:30:04","status":1,"id":33,"type":"other","content":"加油祖国！宝宝疫情期间的【雷神山医院】画作！加油！💪 ","userId":"usr_00001","isAnonymity":0,"likeNum":5,"commentNum":0,"imageList":["https://bf-images.oss-cn-shanghai.aliyuncs.com/forum/20200401114856UeMw1AaO.jpg"],"nickname":"阳妹子家长","headImage":"https://image.brightfuture360.com/course/20200401193921GJEKariR.png","isCollected":0,"isLiked":0,"userHeadList":null,"formatTime":"04/21"},{"createTime":"2020-04-21 20:12:57","status":1,"id":32,"type":"other","content":"宝宝刚学不久的作品，欢迎点赞鼓励😊😊😊","userId":"usr_71b736221fd34a47ac752208e0e1ac91","isAnonymity":0,"likeNum":6,"commentNum":1,"imageList":["https://bf-images.oss-cn-shanghai.aliyuncs.com/forum/20200401114855KtJD2Dmx.jpg"],"nickname":"Whitney","headImage":"https://bf-images.oss-cn-shanghai.aliyuncs.com/course/202004011154189I1KxEdK.png","isCollected":0,"isLiked":0,"userHeadList":null,"formatTime":"04/21"},{"createTime":"2020-04-21 11:34:14","status":1,"id":31,"type":"other","content":"分享sissi的最新创作《幼儿园班级大合影》～\n努力的四月开始啦❤️","userId":"usr_002dcaa194f04dc5b7939fdfab55bbe0","isAnonymity":0,"likeNum":3,"commentNum":0,"imageList":["https://bf-images.oss-cn-shanghai.aliyuncs.com/forum/20200401114427lJHOtPVx.jpg"],"nickname":"sissi家长","headImage":"https://bf-images.oss-cn-shanghai.aliyuncs.com/course/20200401114407gE8KOWTq.png","isCollected":0,"isLiked":0,"userHeadList":null,"formatTime":"04/21"},{"createTime":"2020-04-20 20:28:18","status":1,"id":34,"type":"other","content":"我家宝宝画的蛋糕🍰 好大好高 感觉一家人都吃不完呀","userId":"usr_1a92ebe954e841878ba098ec3eba346e","isAnonymity":0,"likeNum":2,"commentNum":0,"imageList":["https://bf-images.oss-cn-shanghai.aliyuncs.com/forum/202004011154318QkqEMD9.jpg"],"nickname":"茜茜家长","headImage":"https://image.brightfuture360.com/course/20200401194028O0kemsXD.png","isCollected":0,"isLiked":0,"userHeadList":null,"formatTime":"04/20"}]
     * total : 6
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
         * createTime : 2020-05-07 17:14:59
         * status : 1
         * id : 40
         * type : other
         * content : 五一期间宝贝画的爸爸妈妈在家里的样子～～
         * userId : usr_eaaa5e8b30d04919ad54f88155aa8013
         * isAnonymity : 0
         * likeNum : 2
         * commentNum : 1
         * imageList : ["https://image.brightfuture360.com/forum/20200507171423cKqLs1if.jpg"]
         * nickname : 朱伟铭
         * headImage : https://image.brightfuture360.com/user/head-00001.png
         * isCollected : 0
         * isLiked : 0
         * userHeadList : null
         * formatTime : 6天前
         */

        private String createTime;
        private int status;
        private int id;
        private String type;
        private String content;
        private String userId;
        private int isAnonymity;
        private int likeNum;
        private int commentNum;
        private String nickname;
        private String headImage;
        private int isCollected;
        private int isLiked;
        private List<String> userHeadList;
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

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
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

        public int getCommentNum() {
            return commentNum;
        }

        public void setCommentNum(int commentNum) {
            this.commentNum = commentNum;
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

        public int getIsCollected() {
            return isCollected;
        }

        public void setIsCollected(int isCollected) {
            this.isCollected = isCollected;
        }

        public int getIsLiked() {
            return isLiked;
        }

        public void setIsLiked(int isLiked) {
            this.isLiked = isLiked;
        }

        public List<String> getUserHeadList() {
            return userHeadList;
        }

        public void setUserHeadList(List<String> userHeadList) {
            this.userHeadList = userHeadList;
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
}
