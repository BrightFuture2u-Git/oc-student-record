package io.drew.record.util;

import org.json.JSONObject;

import java.util.HashMap;

import io.drew.base.MyLog;
import io.drew.base.network.RetrofitManager;
import io.drew.record.BuildConfig;
import io.drew.record.EduApplication;
import io.drew.record.base.BaseCallback;
import io.drew.record.service.AppService;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * @Author: KKK
 * @CreateDate: 2020/9/28 2:54 PM
 * <p>
 * 数据埋点工具类
 */
public class TagHelper {
    private static TagHelper tagHelper;

    public static TagHelper getInstance() {
        if (tagHelper == null) {
            tagHelper = new TagHelper();
        }
        return tagHelper;
    }

    /**
     * 上传操作日志
     *
     * @param actionEvent 事件名称
     * @param location    事件位置
     */
    public void submitEvent(String actionEvent, String location) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("actionEvent", actionEvent);
        map.put("location", location);
        map.put("source", "hualeme");
        map.put("terminal", AppUtil.isPad(EduApplication.instance) ? "Android_Pad" : "Android_Phone");
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
                new JSONObject(map).toString());
        RetrofitManager.instance().getService(BuildConfig.API_URL, AppService.class).submitEvent(body).enqueue(new BaseCallback<>(result -> {
            if (result!=null&&result) {
                MyLog.e("操作日志上传成功");
            } else {
                MyLog.e("操作日志上传失败");
            }
        }, throwable -> {
            MyLog.e("操作日志上传失败" + throwable.getMessage());
        }));
    }

}
