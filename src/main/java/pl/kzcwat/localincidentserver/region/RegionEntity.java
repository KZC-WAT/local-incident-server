package pl.kzcwat.localincidentserver.region;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "region_entity")
public class RegionEntity implements Serializable {

    @Id
    @Column(name = "region_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "region_name", unique = true, nullable = false)
    private String name;

    @ManyToOne(optional = true)
    @JoinColumn(name = "super_region", nullable = true)
    private RegionEntity superRegion;
}
