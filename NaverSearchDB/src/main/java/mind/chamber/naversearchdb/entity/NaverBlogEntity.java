package mind.chamber.naversearchdb.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.*;
import mind.chamber.naversearchdb.data.NaverBlogId;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(NaverBlogId.class)
@Table(name = "searchblogitem")
public class NaverBlogEntity {

    // yyyyMMddHHmmss for CurrentTime
    @Id
    private String id;

    // yyyyMMddHHmmss for CurrentTime to sequence
    @Id
    private String idseq;

    private String title;
    private String link;
    private String description;
    private String bloggername;
    private String bloggerlink;
    private String postdate;
}
