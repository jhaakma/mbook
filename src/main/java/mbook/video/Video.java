package mbook.video;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@Document(collection = "video")
public class Video {
    @Id
    private String id;
    @Indexed(unique = true, direction = IndexDirection.DESCENDING )
    private String index;
    private String name;
    private String url;
    private String description;
}
