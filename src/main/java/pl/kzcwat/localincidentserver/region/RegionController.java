package pl.kzcwat.localincidentserver.region;

import com.fasterxml.jackson.databind.JsonMappingException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.kzcwat.localincidentserver.region.exception.RegionNotFoundException;
import pl.kzcwat.localincidentserver.region.request.RegionModifyRequest;
import pl.kzcwat.localincidentserver.region.request.RegionRequest;

import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/regions")
public class RegionController {

    private final RegionService regionService;

    @GetMapping
    public Page<RegionEntity> getRegionsPage(
            @PageableDefault(size = 5)
            @SortDefault.SortDefaults({
                    @SortDefault(sort = "id", direction = Sort.Direction.ASC)
            })
                    Pageable pageable) {
        return regionService.getRegionsPage(pageable);
    }

    @GetMapping("{id}")
    public ResponseEntity<RegionEntity> getRegion(@PathVariable Long id) {
        try {
            Optional<RegionEntity> regionEntity = regionService.getRegion(id);
            return regionEntity.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (RegionNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<RegionEntity> saveRegion(@Valid @RequestBody RegionRequest regionRequest) {
        try {
            RegionEntity region = regionService.saveRegion(regionRequest);
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(region.getId())
                    .toUri();
            return ResponseEntity.created(location).body(region);
        } catch (RegionNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (NullPointerException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<RegionEntity> replaceRegion(@PathVariable Long id, @Valid @RequestBody RegionRequest regionRequest) {
        try {
            return new ResponseEntity<>(regionService.replaceRegion(id, regionRequest), HttpStatus.OK);
        } catch (RegionNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("{id}")
    public ResponseEntity<RegionEntity> modifyRegion(@PathVariable Long id, @Valid @RequestBody RegionModifyRequest regionModifyRequest) {
        try {
            return new ResponseEntity<>(regionService.modifyRegion(id, regionModifyRequest), HttpStatus.OK);
        } catch (RegionNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (JsonMappingException e) {
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteRegion(@PathVariable Long id) {
        try {
            regionService.deleteRegion(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (RegionNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
