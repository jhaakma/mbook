package mbook.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.Singular;

@Getter @Setter @Builder
@Document(collection = "page")
public class Page {
    @Id
    private String id;
    private String title;
    private String href;
    @Singular private List<String> requiredRoles;
    @Builder.Default
    private Boolean showInMenu = true;
}