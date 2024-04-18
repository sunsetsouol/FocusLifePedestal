package com.qgStudio.pedestal.entity.vo.friend;

import com.qgStudio.pedestal.entity.dto.DealFriendApplyDTO;
import com.qgStudio.pedestal.entity.po.Friendship;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author yinjunbiao
 * @version 1.0
 * @date 2024/4/17
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class FriendApplyVO {

    private Long id;

    /**
     * 申请人名字
     */
    private String name;

    /**
     * 申请人头像
     */
    private String headImage;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 申请时间
     */
    private String applyTime;

    /**
     * 状态
     */
    private String status;

    private String statusCN;

    public void setStatus(){
        this.statusCN = StatusType.parseStatusCN(status);
    }

    @AllArgsConstructor
    @Getter
    public enum StatusType{
        APPLICATION(Friendship.statusType.APPLICATION.getStatus(), "申请中"),
        AGREE(Friendship.statusType.AGREE.getStatus(), "已同意"),
        DISAGREE(Friendship.statusType.DISAGREE.getStatus(), "已拒绝"),
        OVERDUE(Friendship.statusType.OVERDUE.getStatus(), "已过期"),;
        private final String status;
        private final String statusCN;
        private static Map<String, String > statusMap;
        public static Map<String, String> getStatusMap() {
            if (Objects.isNull(statusMap)) {
                synchronized (StatusType.class) {
                    statusMap = Arrays.stream(StatusType.values())
                            .collect(Collectors.toMap(StatusType::getStatus, StatusType::getStatusCN, (v1, v2) -> v1));
                }
            }
            return statusMap;
        }
        public static String parseStatusCN(String status) {
            return getStatusMap().getOrDefault(status, "已过期");
        }
    }
}
