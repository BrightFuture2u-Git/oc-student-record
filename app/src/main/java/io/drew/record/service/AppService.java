package io.drew.record.service;

import java.util.List;

import io.drew.record.service.bean.ResponseBody;
import io.drew.record.service.bean.response.AddressList;
import io.drew.record.service.bean.response.AliPayOrder;
import io.drew.record.service.bean.response.Articles;
import io.drew.record.service.bean.response.AuthInfo;
import io.drew.record.service.bean.response.CommentedListInfo;
import io.drew.record.service.bean.response.CommentsInfo;
import io.drew.record.service.bean.response.HomeRecords;
import io.drew.record.service.bean.response.LikedListInfo;
import io.drew.record.service.bean.response.MessageCount;
import io.drew.record.service.bean.response.MyRecordLecture;
import io.drew.record.service.bean.response.MyRecordWorks;
import io.drew.record.service.bean.response.OssRecord;
import io.drew.record.service.bean.response.PayStatus;
import io.drew.record.service.bean.response.RecordCourseInfo;
import io.drew.record.service.bean.response.RecordCourseLecture;
import io.drew.record.service.bean.response.RecordOrders;
import io.drew.record.service.bean.response.StsInfo;
import io.drew.record.service.bean.response.UnUploadRecordLecture;
import io.drew.record.service.bean.response.Version;
import io.drew.record.service.bean.response.WxPayOrder;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface AppService {

    @POST("/bf/feedback/create")
    Call<ResponseBody<String>> feedback(@Body RequestBody requestBody);//意见反馈

    @GET("/account/user/info")
    Call<ResponseBody<AuthInfo.UserBean>> getUserInfo();//用户详情

    @GET("/bf/course/ai/my")
    Call<ResponseBody<MyRecordLecture>> getRecordLectures(
            @Query("current") int current,
            @Query("pageSize") int pageSize
    );//我的录播课课程列表

    @GET("/bf/course/ai/my/orders")
    Call<ResponseBody<RecordOrders>> getMyOrders(
            @Query("current") int current,
            @Query("pageSize") int pageSize
    );//我的录播课订单列表

    @GET("/bf/course/ai/course/lectures/{id}")
    Call<ResponseBody<List<RecordCourseLecture>>> getRecordCourseLectures(@Path("id") String id);//录播课程课节详情

    @POST("/account/sms/send/verification/code")
    Call<ResponseBody<Boolean>> getVerificationCode(@Body RequestBody requestBody);//获取验证码

    @POST("/account/auth/phone")
    Call<ResponseBody<AuthInfo>> loginByPhone(@Body RequestBody requestBody);

    @POST("/account/auth/v1/parent")
    Call<ResponseBody<AuthInfo>> loginV1(@Body RequestBody requestBody);

    @POST("/bf/collect/create")
    Call<ResponseBody<String>> collect(@Body RequestBody requestBody);//收藏课程或者老师

    @GET("/admin/upload/getSts")
    Call<ResponseBody<StsInfo>> getSts();//获取Sts

    @GET("/account/address/list")
    Call<ResponseBody<AddressList>> getAddressList(@Query("current") int current,
                                                   @Query("pageSize") int pageSize);//用户收件地址列表

    @GET("/account/address/remove")
    Call<ResponseBody<String>> removeAddress(@Query("id") int id);//删除收件地址

    @POST("/account/order/pay/course")
    Call<ResponseBody<AliPayOrder>> createCourseOrder(@Body RequestBody requestBody);//创建支付宝录播课订单

    @POST("/account/order/pay/course/wxapp")
    Call<ResponseBody<WxPayOrder>> createCourseOrderWxApp(@Body RequestBody requestBody);//创建微信录播课订单

    @POST("/bf/course/ai/view/lecture")
    Call<ResponseBody<Boolean>> addWatchRecord(@Body RequestBody requestBody);//设置观看记录

    @POST("/bf/record/add")
    Call<ResponseBody<Boolean>> submitEvent(@Body RequestBody requestBody);//添加操作日志

    @GET("/account/order/paymentReview/{id}")
    Call<ResponseBody<PayStatus>> paymentReview(@Path("id") String id, @Query("token") String token);//查询支付状态

    @POST("/account/address/submit")
    Call<ResponseBody<String>> submitAddress(@Body RequestBody requestBody);//添加收件地址

    @GET("/bf/course/ai/detail/{id}")
    Call<ResponseBody<RecordCourseInfo>> getRecordCourseInfo(@Path("id") String id);//获取录播课详情

    @GET("/bf/course/ai/no/product/lectures")
    Call<ResponseBody<List<UnUploadRecordLecture>>> getUnUploadRecordLectures();//获取未上传作品课节列表接口

    @GET("/bf/course/ai/my/products")
    Call<ResponseBody<MyRecordWorks>> getMyRecordWorks(@Query("current") int current,
                                                       @Query("pageSize") int pageSize);//我的录播课作品列表

    @GET("/bf/course/ai/no/product/num")
    Call<ResponseBody<Integer>> getUnUploadRecordLectureNum();//未上传作品录播课列表

    @GET("/forum/my/new/message/count")
    Call<ResponseBody<MessageCount>> getNewMessageCount();//我的新消息总数

    @GET("/forum/article/detail/{id}")
    Call<ResponseBody<Articles.RecordsBean>> getArticleInfo(@Path("id") int id);//获取帖子详情

    @POST("/forum/article/comment")
    Call<ResponseBody<String>> comment(@Body RequestBody requestBody);//评论帖子

    @POST("/forum/article/comment/like/{id}")
    Call<ResponseBody<String>> likeComment(@Path("id") int id);//点赞（取消）评论

    @GET("/forum/article/comment/list/{id}")
    Call<ResponseBody<CommentsInfo>> getCommentList(@Path("id") int id,
                                                    @Query("current") int current,
                                                    @Query("pageSize") int pageSize);//获取评论列表

    @POST("/forum/article/like/{id}")
    Call<ResponseBody<String>> like(@Path("id") int id);//点赞（取消）帖子

    @POST("/forum/article/collect/{id}")
    Call<ResponseBody<String>> collect(@Path("id") int id);//帖子收藏

    @POST("/forum/my/articles/del/{id}")
    Call<ResponseBody<String>> delect(@Path("id") int id);//删除我的帖子

    @GET("/forum/article/list")
    Call<ResponseBody<Articles>> getArticles(
            @Query("current") int current,
            @Query("pageSize") int pageSize,
            @Query("status") int status
    );//画廊列表

    @GET("/forum/my/articles")
    Call<ResponseBody<Articles>> getMyAllArticles(
            @Query("current") int current,
            @Query("pageSize") int pageSize
    );//我的动态

    @GET("/forum/my/be/comment/messages")
    Call<ResponseBody<CommentedListInfo>> getCommentedList(
            @Query("current") int current,
            @Query("pageSize") int pageSize
    );//我的帖子被评论消息列表

    @GET("/forum/my/be/like/messages")
    Call<ResponseBody<LikedListInfo>> getlikedList(
            @Query("current") int current,
            @Query("pageSize") int pageSize
    );//我的帖子或评论被点赞消息列表

    @GET("/forum/my/collects")
    Call<ResponseBody<Articles>> getCollection(
            @Query("current") int current,
            @Query("pageSize") int pageSize
    );//我的收藏

    @POST("/forum/article/add")
    Call<ResponseBody<String>> addArticle(@Body RequestBody requestBody);//添加帖子

    @POST("/account/user/update")
    Call<ResponseBody<String>> userUpdate(@Body RequestBody requestBody);//更新用户信息

    @POST("/bf/course/ai/product")
    Call<ResponseBody<MyRecordWorks.RecordsBean>> addRecordProduct(@Body RequestBody requestBody);//添加录播作品

    @GET("/bf/version/newest")
    Call<ResponseBody<Version>> getNewVersion(@Query("appType") String appType,
                                              @Query("version") String version);//获取最新版本

    @GET("bf/live/record")
    Call<ResponseBody<OssRecord>> record(@Query("courseId") int courseId,
                                         @Query("studentId") String studentId);//获取课程回放授权接口

    @GET("/bf/course/ai/view/auth")
    Call<ResponseBody<OssRecord>> recordLectureAuth(@Query("courseLectureId") int courseLectureId);//获取录播课程授权接口

    @GET("bf/course/ai/index")
    Call<ResponseBody<HomeRecords>> getHomeRecord();//首页录播课
}
