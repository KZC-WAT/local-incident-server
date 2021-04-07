package pl.kzcwat.localincidentserver.region;

import com.fasterxml.jackson.databind.JsonMappingException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.kzcwat.localincidentserver.region.exception.RegionNotFoundException;
import pl.kzcwat.localincidentserver.region.request.RegionModifyRequest;
import pl.kzcwat.localincidentserver.region.request.RegionRequest;

import javax.transaction.Transactional;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class RegionService {

    private final RegionRepository regionRepository;
    private final RegionFactory regionFactory;

    public Page<RegionEntity> getRegionsPage(Pageable pageable) {
        return regionRepository.findAll(pageable);
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

        if (dto.getSuperRegionName() != null && dto.getSuperRegionName().equals(regionEntity.getRegionName()))
            dto.setSuperRegionName(null);

        RegionEntity updatedRegionEntity = regionFactory.DTOtoEntity(dto);
        updatedRegionEntity.setRegionId(regionEntity.getRegionId());

        return regionRepository.save(updatedRegionEntity);
    }

    @Transactional
    public RegionEntity modifyRegion(Long regionId, RegionModifyRequest dto) throws RegionNotFoundException, IllegalArgumentException, JsonMappingException {
        RegionEntity regionEntity = regionRepository.findById(regionId).orElseThrow(RegionNotFoundException::new);

        if (dto.getRegionName().isPresent())
            if (dto.getRegionName().get() != null)
                regionEntity.setRegionName(dto.getRegionName().get());

        if (dto.getSuperRegionName().isPresent())
            if (dto.getSuperRegionName().get() != null && !dto.getSuperRegionName().get().equals(regionEntity.getRegionName()))
                regionEntity.setSuperRegion(
                        regionRepository
                                .findByRegionName(dto.getSuperRegionName().get())
                                .orElseThrow(RegionNotFoundException::new)
                );
            else
                regionEntity.setSuperRegion(null);

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
