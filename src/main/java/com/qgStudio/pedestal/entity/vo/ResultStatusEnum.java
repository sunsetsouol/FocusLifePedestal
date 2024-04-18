package com.qgStudio.pedestal.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author yinjunbiao
 * @version 1.0
 * @date 2024/1/27
 */
@Getter
@AllArgsConstructor
public enum ResultStatusEnum {

    DEFAULT_ERROR(500, "服务器异常"),
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
    PARAMS_ERROR(405, "参数错误"),
    FRIEND_APPLY_NOT_EXIST(406, "不存在该好友申请"),
    ALREADY_DEAL(407, "该好友申请已被处理"),
    OVERDUE(408, "已过期"),
    ALREADY_FRIEND(409, "已经是好友了"),
    GROUP_NOT_EXIT(601, "群聊不存在"),
    ALREADY_IN_SPACE(602, "已经在空间"),
    RE_QUIT_SPACE(603, "重复退出空间"),
    SPACE_NOT_EXIT(604, "空间不存在"),
    INVITE_NOT_EXIT(605, "邀请不存在"),
    OWNER_CANNOT_QUIT(606, "空间所有者不能直接退出空间"),
    USER_NOT_IN_SPACE(607, "空间不存在该用户");

    private final Integer code;

    private final String message;

    private static Map<Integer, String > enumMap;

    private final static Integer DEFAULT_ERROR_CODE = 500;

    public static Map<Integer, String> getEnumMap(){
        if(Objects.isNull(enumMap)){
            synchronized (ResultStatusEnum.class){
                enumMap = Arrays.stream(ResultStatusEnum.values()).collect(
                        Collectors.toMap(ResultStatusEnum::code, ResultStatusEnum::message,(v1,v2)->v1));
            }
        }
        return enumMap;
    }
    public static String parse(Integer code){
        return enumMap.containsKey(code) ? enumMap.get(code) : enumMap.get(DEFAULT_ERROR_CODE);
    }



    public int code() {
        return code;
    }

    public String message() {
        return message;
    }
}
