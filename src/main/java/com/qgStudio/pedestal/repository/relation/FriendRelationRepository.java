package com.qgStudio.pedestal.repository.relation;

import com.qgStudio.pedestal.entity.relationship.FriendRelation;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

/**
 * @author yinjunbiao
 * @version 1.0
 * @date 2024/4/16
 */
@Repository
public interface FriendRelationRepository extends Neo4jRepository<FriendRelation, Long> {
    @Query("MATCH (u:UserNode {userId: {userId}}),(f:UserNode {userId: {fromId}})\n" +
            "MERGE (f)-[r:FriendRelation]->(u)\n" +
            "ON CREATE SET r.CreateTime = timestamp() \n" +
            "MERGE (u)-[r2:FriendRelation]->(f)\n" +
            "ON CREATE SET r2.CreateTime = timestamp()\n")
    void addFriendship(Integer userId, Integer fromId);

    @Query("MATCH (u:UserNode {userId: {userId}})-[r:FriendRelation]->(f:UserNode {userId: {fromId}}) return r")
    FriendRelation findByUserIds(Integer userId, Integer fromId);
}
