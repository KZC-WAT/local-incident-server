package pl.kzcwat.localincidentserver.region;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RegionRepository extends JpaRepository<RegionEntity, Long> {

    Optional<RegionEntity> findByName(String name);

    List<RegionEntity> findAllBySuperRegion(RegionEntity superRegion);

}
