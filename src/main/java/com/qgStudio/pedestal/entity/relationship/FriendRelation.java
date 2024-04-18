package com.qgStudio.pedestal.entity.relationship;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.RelationshipEntity;

import java.time.LocalDateTime;

/**
 * @author yinjunbiao
 * @version 1.0
 * @date 2024/4/16
 */
@RelationshipEntity("FriendRelation")
public class FriendRelation {
    @Id
    @GeneratedValue
    private Long id;

    @Property("CreateTime")
    private LocalDateTime createTime;
}
