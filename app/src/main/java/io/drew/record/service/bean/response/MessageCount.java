package io.drew.record.service.bean.response;

import java.io.Serializable;

/**
 * @Author: KKK
 * @CreateDate: 2020/5/22 2:01 PM
 */
public class MessageCount implements Serializable {

    /**
     * commentNum : 0
     * likeNum : 0
     */

    private int commentNum;
    private int likeNum;

    public int getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(int commentNum) {
        this.commentNum = commentNum;
    }

    public int getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(int likeNum) {
        this.likeNum = likeNum;
    }
}
