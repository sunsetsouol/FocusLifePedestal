package com.qgStudio.pedestal.entity.vo;

/**
 * @author yinjunbiao
 * @version 1.0
 * @date 2024/1/27
 */

public enum ResultStatusEnum {

    SUCCESS(200,"成功"),
    SUCCESS_SAVE(200, "成功接收消息"),
    REGISTER_SUCCESS(200, "注册成功"),
    LOGIN_SUCCESS(200, "登录成功"),
    FAILURE_SAVE(401, "转发到消息队列失败"),
    BUSY(400, "服务器繁忙"),
    NOT_AUTHORIZATION(401, "没有权限"),
    EMAIL_EXISTED(402, "邮箱已注册"),
    LOGIN_FAIL(403, "登录失败"),
    TEMPLATE_NOT_EXIST(404, "任务不存在"),
    UNKNOWN_ERROR(500, "服务器异常"),
    PARAMS_ERROR(405, "参数错误");

    private final Integer code;

    private final String message;

    ResultStatusEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public int code() {
        return code;
    }

    public String message() {
        return message;
    }
}
