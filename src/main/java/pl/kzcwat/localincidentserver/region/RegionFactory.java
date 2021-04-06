package pl.kzcwat.localincidentserver.region;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.kzcwat.localincidentserver.region.exception.RegionNotFoundException;
import pl.kzcwat.localincidentserver.region.request.RegionRequest;

@AllArgsConstructor
@Service
public class RegionFactory {

    private final RegionRepository regionRepository;

    public RegionRequest EntityToDTO(RegionEntity regionEntity) {
        return RegionRequest.builder()
                .regionName(regionEntity.getRegionName())
                .superRegionName(regionEntity.getSuperRegion().getRegionName())
                .build();
    }

    public RegionEntity DTOtoEntity(RegionRequest regionRequest) throws RegionNotFoundException {
        return regionRequest.getSuperRegionName() == null
                ?
                RegionEntity.builder()
                        .regionName(regionRequest.getRegionName())
                        .superRegion(null)
                        .build()
                :
                RegionEntity.builder()
                        .regionName(regionRequest.getRegionName())
                        .superRegion(regionRepository.findByRegionName(regionRequest.getSuperRegionName()).orElseThrow(RegionNotFoundException::new))
                        .build();
    }
}
