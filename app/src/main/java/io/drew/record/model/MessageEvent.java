package io.drew.record.model;

/**
 * @Author: KKK
 * @CreateDate: 2020/5/25 10:13 AM
 * <p>
 * eventBus消息model
 */
public class MessageEvent {
    private int code;
    private String message;
    private int intMessage;
    private Object objectMessage;

    public Object getObjectMessage() {
        return objectMessage;
    }

    public void setObjectMessage(Object objectMessage) {
        this.objectMessage = objectMessage;
    }

    public int getIntMessage() {
        return intMessage;
    }

    public void setIntMessage(int intMessage) {
        this.intMessage = intMessage;
    }

    public MessageEvent(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
