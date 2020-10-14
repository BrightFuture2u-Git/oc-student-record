package io.drew.record.service.bean.response;

import android.text.TextUtils;

/**
 * @Author: KKK
 * @CreateDate: 2020/5/15 7:02 PM
 */
public class LocalImg {
    public String localPath;
    public String compressedPath;
    public String ossUrl;

    public boolean hasUpload() {
        return !TextUtils.isEmpty(ossUrl);
    }
}
