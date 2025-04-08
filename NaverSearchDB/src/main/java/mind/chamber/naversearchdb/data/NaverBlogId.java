package mind.chamber.naversearchdb.data;

import java.io.Serializable;
import java.util.Objects;

public class NaverBlogId implements Serializable {

    private String id;
    private String idseq;
    public NaverBlogId() {}
    public NaverBlogId(String id, String idseq) {
        this.id = id;
        this.idseq = idseq;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NaverBlogId)) return false;
        NaverBlogId that = (NaverBlogId) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(idseq, that.idseq);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, idseq);
    }

}