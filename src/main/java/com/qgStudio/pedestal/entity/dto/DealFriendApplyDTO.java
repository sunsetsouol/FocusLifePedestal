package com.qgStudio.pedestal.entity.dto;

import com.qgStudio.pedestal.entity.po.Friendship;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author yinjunbiao
 * @version 1.0
 * @date 2024/4/16
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DealFriendApplyDTO {
    @Range(min = 0,max = 1, message = "操作不合法")
    @NotNull(message = "操作不能为空")
    private Integer operatorNumber;

    @NotNull(message = "申请id不能为空")
    private Long friendId;

    @AllArgsConstructor
    @Getter
    public enum OperatorNumber {
        AGREE(0, Friendship.statusType.AGREE),
        REFUSE(1, Friendship.statusType.DISAGREE);

        private final int id;
        private final Friendship.statusType statusType;
        public static Map<Integer, Friendship.statusType> enumMap;
        public static Map<Integer, Friendship.statusType> getEnumMap() {
            if (Objects.isNull(enumMap)) {
                synchronized (Friendship.statusType.class) {
                    enumMap = Arrays.stream(OperatorNumber.values())
                            .collect(Collectors.toMap(OperatorNumber::getId, OperatorNumber::getStatusType, (v1, v2) -> v1));
                }
            }
            return enumMap;
        }

        public static Friendship.statusType parse(Integer id) {
            return getEnumMap().getOrDefault(id, Friendship.statusType.APPLICATION);
        }

    }
}
