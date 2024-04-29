package com.qgStudio.pedestal.entity.vo.space;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author yinjunbiao
 * @version 1.0
 * @date 2024/4/17
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SpaceUserVO {

    /**
     * 用户名
     */
    private String username;

    /**
     * 头像
     */
    private String headImage;

    /**
     * 邮箱
     */
    private String email;

    /**
     * uid
     */
    private String uid;

    /**
     * 专注事件名
     */
    private String eventName;

    /**
     * 专注开始时间
     */
    private LocalDateTime focusStartTime;

    /**
     * 空间专注次数
     */
    private Integer focusTimes;

    /**
     * 总专注时长
     */
    private Integer totalFocusTime;

    /**
     * 状态（0未完成，1完成，2进行中，3：暂停）
     * {@link com.qgStudio.pedestal.entity.po.FocusOnEvent.StatusType}
     */
    private String status;
}
