package de.pd.crawler.page;

import de.pd.crawler.entities.DomNodeEntity;
import lombok.Data;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

@Data
@Node
public class PageEntity {

    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private String url;

    @Relationship(type = "SCRAPED")
    private DomNodeEntity root;
}
