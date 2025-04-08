package mind.chamber.naversearchdb.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "trend_data_embedded")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NaverTrendEmbeddedEntity {
    @EmbeddedId
    private NaverTrendEmbeddedId id; // 복합키

    private double ratio; // data 배열의 ratio
}
