package pl.kzcwat.localincidentserver.region;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface RegionRepository extends JpaRepository<RegionEntity, Long> {

    Optional<RegionEntity> findByRegionName(String name);

    List<RegionEntity> findAllBySuperRegion(RegionEntity superRegion);

    Page<RegionEntity> findAll(Pageable pageable);

}
