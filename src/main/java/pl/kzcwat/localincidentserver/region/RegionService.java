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

    public Page<Region> getRegionsPage(Pageable pageable) {
        return regionRepository.findAll(pageable);
    }

    public Optional<Region> getRegion(Long id) throws RegionNotFoundException, IllegalArgumentException {
        return regionRepository.findById(id);
    }

    @Transactional
    public Region saveRegion(RegionRequest dto) throws RegionNotFoundException, NullPointerException {
        return regionRepository.save(regionFactory.dtoToEntity(dto));
    }

    @Transactional
    public Region replaceRegion(Long regionId, RegionRequest dto) throws RegionNotFoundException, IllegalArgumentException {
        Region region = getRegion(regionId).orElseThrow(RegionNotFoundException::new);

        if (dto.getSuperRegionId() == null)
            region.setSuperRegion(null);
        else if (dto.getSuperRegionId().equals(regionId))
            throw new IllegalArgumentException();
        else if (regionRepository.existsRegionByIdAndSuperRegionId(dto.getSuperRegionId(), regionId))
            throw new IllegalArgumentException();

        Region updatedRegion = regionFactory.dtoToEntity(dto);
        updatedRegion.setId(region.getId());

        return regionRepository.save(updatedRegion);
    }

    @Transactional
    public Region modifyRegion(Long regionId, RegionModifyRequest dto) throws RegionNotFoundException, IllegalArgumentException, JsonMappingException {
        Region region = regionRepository.findById(regionId).orElseThrow(RegionNotFoundException::new);

        if (dto.getName().isPresent())
            if (dto.getName().get() != null)
                region.setName(dto.getName().get());

        if (dto.getSuperRegionId().isPresent())
            if (dto.getSuperRegionId().get() == null)
                region.setSuperRegion(null);
            else if (dto.getSuperRegionId().get().equals(regionId))
                throw new IllegalArgumentException();
            else if (regionRepository.existsRegionByIdAndSuperRegionId(dto.getSuperRegionId().get(), regionId))
                throw new IllegalArgumentException();
            else
                region.setSuperRegion(
                        regionRepository
                                .findById(dto.getSuperRegionId().get())
                                .orElseThrow(RegionNotFoundException::new)
                );

        return regionRepository.save(region);
    }

    @Transactional
    public void deleteRegion(Long id) throws RegionNotFoundException, IllegalArgumentException {
        Region superRegion = regionRepository.findById(id).orElseThrow(RegionNotFoundException::new);
        regionRepository.findAllBySuperRegion(superRegion)
                .forEach(x -> {
                    x.setSuperRegion(null);
                    regionRepository.save(x);
                });

        regionRepository.deleteById(id);
    }
}
