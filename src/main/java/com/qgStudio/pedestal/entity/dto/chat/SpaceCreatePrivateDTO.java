package com.qgStudio.pedestal.entity.dto.chat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author yinjunbiao
 * @version 1.0
 * @date 2024/4/18
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SpaceCreatePrivateDTO {

    @NotNull(message = "发送对象id不能为空")
    private Integer toId;


}
