package com.qgStudio.pedestal.constant;

/**
 * @author yinjunbiao
 * @version 1.0
 * @date 2024/3/17
 */
public interface RedisConstants {
    String USER_LOGIN = "pedestal:user:login:";
//    public static final String WATER_INTAKE = "pedestal:water_intake:";
    String WATER_INTAKE_LOCK = "pedestal:water_intake_lock:";
    String USER_FOCUS_TEMPLATE = "pedestal:user_focus_template:";

    String FOCUS_PREFIX = "focus:";

    Integer FOCUS_INTERVAL = 15;
}
