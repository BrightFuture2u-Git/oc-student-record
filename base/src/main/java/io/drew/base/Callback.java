package io.drew.base;

public interface Callback<T> {

    void onSuccess(T res);

    void onFailure(Throwable throwable);

}
