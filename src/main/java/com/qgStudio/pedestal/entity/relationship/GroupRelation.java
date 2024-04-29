package com.qgStudio.pedestal.entity.relationship;

import com.qgStudio.pedestal.entity.node.GroupNode;
import com.qgStudio.pedestal.entity.node.UserNode;
import org.neo4j.ogm.annotation.*;

import java.time.LocalDateTime;

/**
 * @author yinjunbiao
 * @version 1.0
 * @date 2024/4/18
 */
@RelationshipEntity("GroupRelationship")
public class GroupRelation {
    @Id
    @GeneratedValue
    private Long id;

    @Property("CreateTime")
    private Long joinTime;

    @StartNode
    private UserNode userNode;

    @EndNode
    private GroupNode groupNode;
}
