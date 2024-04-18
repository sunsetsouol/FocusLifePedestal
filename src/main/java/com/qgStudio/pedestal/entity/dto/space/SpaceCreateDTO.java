package com.qgStudio.pedestal.entity.dto.space;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author yinjunbiao
 * @version 1.0
 * @date 2024/4/16
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SpaceCreateDTO {

    private String name;

    private String description;


//    private List<String> userUIds;
}
