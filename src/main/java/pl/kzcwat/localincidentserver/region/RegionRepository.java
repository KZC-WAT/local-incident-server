package pl.kzcwat.localincidentserver.region;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RegionRepository extends CrudRepository<RegionEntity, Long> {

    Optional<RegionEntity> findByRegionName(String name);

    List<RegionEntity> findAllBySuperRegion(RegionEntity superRegion);

}
