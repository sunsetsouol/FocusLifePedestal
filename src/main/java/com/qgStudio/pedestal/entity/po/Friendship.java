package com.qgStudio.pedestal.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author ${author}
 * @since 2024-04-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class Friendship implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 发送者id
     */
    private Integer fromId;

    /**
     * 接收者id
     */
    private Integer toId;

    /**
     * 状态（application，agree，disagree，overdue）
     */
    private String status;

    @AllArgsConstructor
    @Getter
    public enum statusType{
        APPLICATION("application"),
        AGREE("agree"),
        DISAGREE("disagree"),
        OVERDUE("overdue"),;
        private final String status;
    }
    /**
     * 发送时间
     */
    private LocalDateTime createTime;

    /**
     * 过期时间
     */
    private LocalDateTime expirationTime;

    /**
     * 逻辑删除
     */
    @TableLogic(delval = "NULL")
    private Integer deleted;


}
