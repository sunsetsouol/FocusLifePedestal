package com.qgStudio.pedestal.repository.node;

import com.qgStudio.pedestal.entity.node.GroupNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

/**
 * @author yinjunbiao
 * @version 1.0
 * @date 2024/4/18
 */
@Repository
public interface GroupNodeRepository extends Neo4jRepository<GroupNode, Long> {
}
