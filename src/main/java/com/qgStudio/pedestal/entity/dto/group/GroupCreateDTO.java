package com.qgStudio.pedestal.entity.dto.group;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author yinjunbiao
 * @version 1.0
 * @date 2024/4/18
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class GroupCreateDTO {

        private String name;

        private String description;

        private List<String> userUIds;
}
