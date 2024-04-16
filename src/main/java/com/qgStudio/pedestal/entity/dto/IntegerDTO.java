package com.qgStudio.pedestal.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author yinjunbiao
 * @version 1.0
 * @date 2024/3/17
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IntegerDTO {
    @NotNull(message = "参数不能为空")
    private Integer number;
}
