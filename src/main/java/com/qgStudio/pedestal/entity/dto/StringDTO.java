package com.qgStudio.pedestal.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @author yinjunbiao
 * @version 1.0
 * @date 2024/4/16
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class StringDTO {

    @NotBlank(message = "参数不能为空")
    private String param;
}
