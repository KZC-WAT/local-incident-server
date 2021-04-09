package pl.kzcwat.localincidentserver.region;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RegionRepository extends JpaRepository<RegionEntity, Long> {

    List<RegionEntity> findAllBySuperRegion(RegionEntity superRegion);

    Boolean existsRegionByIdAndSuperRegionId(Long id, Long superRegionId);

}
