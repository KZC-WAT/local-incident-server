package pl.kzcwat.localincidentserver.region;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.kzcwat.localincidentserver.region.exception.RegionNotFoundException;
import pl.kzcwat.localincidentserver.region.request.RegionRequest;

@RequiredArgsConstructor
@Service
public class RegionFactory {

    private final RegionRepository regionRepository;

    public RegionRequest entityToDTO(Region region) {
        return RegionRequest.builder()
                .name(region.getName())
                .superRegionId(region.getSuperRegion().getId())
                .build();
    }

    public Region dtoToEntity(RegionRequest regionRequest) throws RegionNotFoundException {
        return regionRequest.getSuperRegionId() == null
                ?
                Region.builder()
                        .name(regionRequest.getName())
                        .superRegion(null)
                        .build()
                :
                Region.builder()
                        .name(regionRequest.getName())
                        .superRegion(regionRepository.findById(regionRequest.getSuperRegionId()).orElseThrow(RegionNotFoundException::new))
                        .build();
    }
}
