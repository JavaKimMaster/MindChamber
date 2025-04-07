package mind.chamber.naversearchdb.data;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class NaverBlogResponse {
    private String lastBuildDate;
    private int total;
    private int start;
    private int display;
    private List<NaverBlogItem> items;
}
