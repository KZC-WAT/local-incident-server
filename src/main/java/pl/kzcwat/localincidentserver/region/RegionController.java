package pl.kzcwat.localincidentserver.region;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.kzcwat.localincidentserver.region.exception.RegionNotFoundException;
import pl.kzcwat.localincidentserver.region.request.RegionRequest;

import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/region")
public class RegionController {

    private final RegionService regionService;

    @GetMapping()
    public Iterable<RegionEntity> getAllRegions() {
        return this.regionService.getAllRegions();
    }

    @GetMapping("/{regionId}")
    public ResponseEntity<RegionEntity> getRegion(@PathVariable Long regionId) {
        try {
            Optional<RegionEntity> regionEntity = regionService.getRegion(regionId);
            return regionEntity.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (RegionNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/addRegion")
    public ResponseEntity<RegionEntity> saveRegion(@RequestBody RegionRequest regionRequest) {
        try {
            return new ResponseEntity<>(regionService.saveRegion(regionRequest), HttpStatus.CREATED);
        } catch (RegionNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (NullPointerException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{regionId}")
    public ResponseEntity<RegionEntity> replaceEmployee(@PathVariable Long regionId, @RequestBody RegionRequest regionRequest) {
        try {
            return new ResponseEntity<>(regionService.replaceRegion(regionId, regionRequest), HttpStatus.OK);
        } catch (RegionNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("/{regionId}")
    public ResponseEntity<RegionEntity> modifyEmployee(@PathVariable Long regionId, @RequestBody RegionRequest regionRequest) {
        try {
            return new ResponseEntity<>(regionService.modifyRegion(regionId, regionRequest), HttpStatus.OK);
        } catch (RegionNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{regionId}")
    public ResponseEntity<?> deleteRegion(@PathVariable Long regionId) {
        try {
            regionService.deleteRegion(regionId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (RegionNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
