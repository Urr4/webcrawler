package de.pd.crawler.entities;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DomNodeRepository extends Neo4jRepository<DomNodeEntity, Long> {
}
