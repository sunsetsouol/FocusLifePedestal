package com.qgStudio.pedestal.entity.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author yinjunbiao
 * @version 1.0
 * @date 2024/4/21
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class FocusEventBO {

    private Integer templateId;

    /**
     * 本次专注时长
     */
    private Integer focusTime;


    /**
     * {@link FocusEventType}
     */
    private Integer type;

//    private String isCompleted;

    @AllArgsConstructor
    @Getter
    public enum FocusEventType {
        /**
         * 开始
         */
        START(1),
        /**
         * 心跳进行
         */
        PING(2),
        /**
         * 暂停
         */
        SUSPEND(3),
        /**
         * 继续
         */
        CONTINUE(4),
        /**
         * 取消
         */
        CANCEL(5),
        /**
         * 完成
         */
        FINISH(6)
        ;
        private final Integer value;
    }
}
