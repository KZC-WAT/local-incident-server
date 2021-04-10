package pl.kzcwat.localincidentserver.region;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RegionRepository extends JpaRepository<Region, Long> {

    List<Region> findAllBySuperRegion(Region superRegion);

    Boolean existsRegionByIdAndSuperRegionId(Long id, Long superRegionId);

}
