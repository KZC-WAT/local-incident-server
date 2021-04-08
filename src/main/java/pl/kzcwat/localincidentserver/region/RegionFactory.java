package pl.kzcwat.localincidentserver.region;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.kzcwat.localincidentserver.region.exception.RegionNotFoundException;
import pl.kzcwat.localincidentserver.region.request.RegionRequest;

@RequiredArgsConstructor
@Service
public class RegionFactory {

    private final RegionRepository regionRepository;

    public RegionRequest EntityToDTO(RegionEntity regionEntity) {
        return RegionRequest.builder()
                .regionName(regionEntity.getName())
                .superRegionId(regionEntity.getSuperRegion().getId())
                .build();
    }

    public RegionEntity DTOtoEntity(RegionRequest regionRequest) throws RegionNotFoundException {
        return regionRequest.getSuperRegionId() == null
                ?
                RegionEntity.builder()
                        .name(regionRequest.getRegionName())
                        .superRegion(null)
                        .build()
                :
                RegionEntity.builder()
                        .name(regionRequest.getRegionName())
                        .superRegion(regionRepository.findById(regionRequest.getSuperRegionId()).orElseThrow(RegionNotFoundException::new))
                        .build();
    }
}
