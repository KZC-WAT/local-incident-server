package pl.kzcwat.localincidentserver.region;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
    private Long regionId;

    @ManyToOne(optional = true)
    @JoinColumn(name = "super_region", nullable = true)
    private RegionEntity superRegion;

    @Column(name = "region_name", unique = true)
    private String regionName;

}
