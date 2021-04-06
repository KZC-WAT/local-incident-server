package pl.kzcwat.localincidentserver.region;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.kzcwat.localincidentserver.region.exception.RegionNotFoundException;
import pl.kzcwat.localincidentserver.region.request.RegionRequest;

import javax.transaction.Transactional;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class RegionService {

    private final RegionRepository regionRepository;
    private final RegionFactory regionFactory;

    public Iterable<RegionEntity> getAllRegions() {
        return regionRepository.findAll();
    }

    public Optional<RegionEntity> getRegion(Long id) throws RegionNotFoundException, IllegalArgumentException {
        return regionRepository.findById(id);
    }

    @Transactional
    public RegionEntity saveRegion(RegionRequest dto) throws RegionNotFoundException, NullPointerException {
        return regionRepository.save(regionFactory.DTOtoEntity(dto));
    }

    @Transactional
    public RegionEntity replaceRegion(Long regionId, RegionRequest dto) throws RegionNotFoundException, IllegalArgumentException {
        RegionEntity regionEntity = getRegion(regionId).orElseThrow(RegionNotFoundException::new);

        RegionEntity newRegionEntity = regionFactory.DTOtoEntity(dto);
        newRegionEntity.setRegionId(regionEntity.getRegionId());

        return regionRepository.save(newRegionEntity);
    }

    @Transactional
    public RegionEntity modifyRegion(Long regionId, RegionRequest dto) throws RegionNotFoundException, IllegalArgumentException {
        RegionEntity regionEntity = regionRepository.findById(regionId).orElseThrow(RegionNotFoundException::new);

        if (dto.getRegionName() != null)
            regionEntity.setRegionName(dto.getRegionName());

        if (dto.getSuperRegionName() != null)
            regionEntity.setSuperRegion(regionRepository.findByRegionName(dto.getSuperRegionName()).orElse(null));

        return regionRepository.save(regionEntity);
    }

    @Transactional
    public void deleteRegion(Long id) throws RegionNotFoundException, IllegalArgumentException {
        RegionEntity superRegion = regionRepository.findById(id).orElseThrow(RegionNotFoundException::new);
        regionRepository.findAllBySuperRegion(superRegion)
                .forEach(x -> {
                    x.setSuperRegion(null);
                    regionRepository.save(x);
                });

        regionRepository.deleteById(id);
    }
}
