package io.drew.record.interfaces;

/**
 * @Author: KKK
 * @CreateDate: 2020/5/15 11:02 AM
 */
public interface OnOssFileUploadListener {
    void onUploadSuccess(String url);
    void onUploadFail();
}
