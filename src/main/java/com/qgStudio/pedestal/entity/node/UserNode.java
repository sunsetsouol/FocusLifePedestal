package com.qgStudio.pedestal.entity.node;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;

/**
 * @author yinjunbiao
 * @version 1.0
 * @date 2024/4/16
 */
@NodeEntity("UserNode")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserNode {
    @Id
    @GeneratedValue
    private Long id;

    @Property("userId")
    private Integer userId;

    @Property("Uid")
    private String uid;
}
