package com.qgStudio.pedestal.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * @author yinjunbiao
 * @version 1.0
 * @date 2024/4/2
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class LocalDateDTO {
    @NotNull(message = "日期不能为空")
    LocalDate date;
}
