package pl.kzcwat.localincidentserver.announcementcategory;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.kzcwat.localincidentserver.announcementcategory.exception.AnnouncementCategoryNotFoundException;
import pl.kzcwat.localincidentserver.announcementcategory.request.AnnouncementCategoryModifyRequest;
import pl.kzcwat.localincidentserver.announcementcategory.request.AnnouncementCategoryReplaceRequest;

import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/announcementCategories")
@RequiredArgsConstructor
public class AnnouncementCategoryController {
    private final AnnouncementCategoryService announcementCategoryService;

    @GetMapping
    public Page<AnnouncementCategory> getAnnouncementsPage(
            @PageableDefault(size = 5)
            @SortDefault.SortDefaults({@SortDefault(sort = "id", direction = Sort.Direction.ASC)})
                    Pageable pageable) {
        return announcementCategoryService.getAnnouncementCategoriesPage(pageable);
    }

    @GetMapping("{announcementCategoryId}")
    public ResponseEntity<?> getAnnouncementCategory(@PathVariable Long announcementCategoryId) {
        try {
            Optional<AnnouncementCategory> announcementCategoryOptional =
                    announcementCategoryService.getAnnouncementCategory(announcementCategoryId);
            return announcementCategoryOptional
                    .map(announcementCategory -> new ResponseEntity<>(announcementCategory, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (AnnouncementCategoryNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<?> saveAnnouncementCategory(@RequestBody @Valid AnnouncementCategoryReplaceRequest replaceRequest) {
        try {
            AnnouncementCategory announcementCategory = announcementCategoryService.saveAnnouncementCategory(replaceRequest);
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(announcementCategory.getId())
                    .toUri();
            return ResponseEntity.created(location).body(announcementCategory);
        } catch (AnnouncementCategoryNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("{announcementCategoryId}")
    public ResponseEntity<?> replaceAnnouncementCategory(@PathVariable Long announcementCategoryId,
                                                         @RequestBody @Valid AnnouncementCategoryReplaceRequest replaceRequest) {
        try {
            return new ResponseEntity<>(
                    announcementCategoryService.replaceAnnouncementCategory(announcementCategoryId, replaceRequest), HttpStatus.OK);
        } catch (AnnouncementCategoryNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("{announcementCategoryId}")
    public ResponseEntity<?> modifyAnnouncementCategory(@PathVariable Long announcementCategoryId,
                                                        @RequestBody @Valid AnnouncementCategoryModifyRequest modifyRequest) {
        try {
            return new ResponseEntity<>(announcementCategoryService.modifyAnnouncementCategory(announcementCategoryId, modifyRequest), HttpStatus.OK);
        } catch (AnnouncementCategoryNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("{announcementCategoryId}")
    public ResponseEntity<?> deleteAnnouncementCategory(@PathVariable Long announcementCategoryId) {
        try {
            announcementCategoryService.deleteAnnouncementCategory(announcementCategoryId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (EmptyResultDataAccessException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
