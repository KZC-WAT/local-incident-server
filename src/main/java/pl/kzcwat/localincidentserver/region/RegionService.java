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
        return regionRepository.save(regionFactory.dtoToEntity(dto));
    }

    @Transactional
    public RegionEntity replaceRegion(Long regionId, RegionRequest dto) throws RegionNotFoundException, IllegalArgumentException {
        RegionEntity regionEntity = getRegion(regionId).orElseThrow(RegionNotFoundException::new);

        if (dto.getSuperRegionId() == null)
            regionEntity.setSuperRegion(null);
        else if (dto.getSuperRegionId().equals(regionId))
            throw new IllegalArgumentException();
        else if (regionRepository.existsRegionByIdAndSuperRegionId(dto.getSuperRegionId(), regionId))
            throw new IllegalArgumentException();

        RegionEntity updatedRegionEntity = regionFactory.dtoToEntity(dto);
        updatedRegionEntity.setId(regionEntity.getId());

        return regionRepository.save(updatedRegionEntity);
    }

    @Transactional
    public RegionEntity modifyRegion(Long regionId, RegionModifyRequest dto) throws RegionNotFoundException, IllegalArgumentException, JsonMappingException {
        RegionEntity regionEntity = regionRepository.findById(regionId).orElseThrow(RegionNotFoundException::new);

        if (dto.getName().isPresent())
            if (dto.getName().get() != null)
                regionEntity.setName(dto.getName().get());

        if (dto.getSuperRegionId().isPresent())
            if (dto.getSuperRegionId().get() == null)
                regionEntity.setSuperRegion(null);
            else if (dto.getSuperRegionId().get().equals(regionId))
                throw new IllegalArgumentException();
            else if (regionRepository.existsRegionByIdAndSuperRegionId(dto.getSuperRegionId().get(), regionId))
                throw new IllegalArgumentException();
            else
                regionEntity.setSuperRegion(
                        regionRepository
                                .findById(dto.getSuperRegionId().get())
                                .orElseThrow(RegionNotFoundException::new)
                );

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
