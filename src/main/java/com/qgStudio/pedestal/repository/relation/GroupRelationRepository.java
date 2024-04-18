package com.qgStudio.pedestal.repository.relation;

import com.qgStudio.pedestal.entity.relationship.GroupRelation;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

/**
 * @author yinjunbiao
 * @version 1.0
 * @date 2024/4/18
 */
@Repository
public interface GroupRelationRepository extends Neo4jRepository<GroupRelation, Long> {
    @Query("MATCH (u:UserNode{userId:{userId}}),(g:GroupNode{GroupId:{groupId}}) MERGE (u)-[r:GroupRelationship]->(g) " +
            "on create set r.CreateTime = timestamp()")
    void join(Integer userId, Integer groupId);

    @Query("MATCH (u:UserNode{userId:{userId}})-[r:GroupRelationship]->(g:GroupNode{GroupId:{groupId}}) delete r return count(r)")
    int deleteRelation(Integer userId, Integer groupId);
}
