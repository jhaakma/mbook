package mbook.page;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.Singular;
/**
 * Model representing a web page, including end point and authentication information.
 * @author jh1540
 *
 */
@Getter @Setter
@Document(collection = "page")
public class Page {
    @Id
    private String id;
    private String title;
    @Indexed(unique = true)
    private String href;
    @Singular private List<String> allowedRoles;
    private Boolean showInMenu;
}