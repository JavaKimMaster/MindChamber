package mind.chamber.naversearchdb.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "trend_data")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NaverTrendEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String groupTitle;
    private String keyword;
    private String period;
    private double ratio;
}
