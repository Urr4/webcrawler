package de.pd.crawler.page;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PageRepository extends Neo4jRepository<PageEntity, Long> {
}
