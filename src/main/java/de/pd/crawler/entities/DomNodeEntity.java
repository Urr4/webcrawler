package de.pd.crawler.entities;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Node
@Data
public class DomNodeEntity {

    @Id
    @GeneratedValue
    private Long id;

    private transient DomNodeEntity parent;

    private String name;
    private String value;
    private String xPath;

    @CompositeProperty
    private Map<String, Object> attributes = new HashMap<>();

    @Relationship(type = "CHILD")
    private List<DomNodeEntity> children;
}
