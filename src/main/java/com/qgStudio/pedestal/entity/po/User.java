package com.qgStudio.pedestal.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.*;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author ${author}
 * @since 2024-04-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 昵称
     */
    private String name;

    /**
     * 头像
     */
    private String headImage;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 密码
     */
    private String password;

    /**
     * 默认喝水量
     */
    private Integer defaultWaterIntake;

    /**
     * 提醒间隔
     */
    private Integer defaultReminderInterval;

    /**
     * 总专注时长
     */
    private Integer totalCompletionTime;

    /**
     * 总喝水量
     */
    private Integer totalWaterIntake;

    private String uid;


}
