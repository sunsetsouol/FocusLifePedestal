package com.qgStudio.pedestal.repository.node;

import com.qgStudio.pedestal.entity.node.UserNode;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author yinjunbiao
 * @version 1.0
 * @date 2024/4/16
 */
@Repository
public interface UserNodeRepository extends Neo4jRepository<UserNode, Long> {
    @Query("MATCH (u:UserNode {userId: {userId}})-[r:FriendRelation]->(f:UserNode) return f")
    List<UserNode> selectById(Integer userId);
}
