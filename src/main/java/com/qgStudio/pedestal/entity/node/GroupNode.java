package com.qgStudio.pedestal.entity.node;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.neo4j.ogm.annotation.*;

/**
 * @author yinjunbiao
 * @version 1.0
 * @date 2024/4/18
 */
@NodeEntity("GroupNode")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class GroupNode {
    @Id
    @GeneratedValue
    private Long id;

    @Property("GroupId")
    private Integer groupId;
}
