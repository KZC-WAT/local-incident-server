package pl.kzcwat.localincidentserver.announcement;

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
import pl.kzcwat.localincidentserver.announcement.exception.AnnouncementExpirationDateFromPast;
import pl.kzcwat.localincidentserver.announcement.exception.AnnouncementNotFoundException;
import pl.kzcwat.localincidentserver.announcement.request.AnnouncementModifyRequest;
import pl.kzcwat.localincidentserver.announcement.request.AnnouncementReplaceRequest;
import pl.kzcwat.localincidentserver.announcementcategory.exception.AnnouncementCategoryNotFoundException;
import pl.kzcwat.localincidentserver.region.exception.RegionNotFoundException;
import pl.kzcwat.localincidentserver.userprofile.exception.UserProfileNotFoundException;

import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/announcements")
@RequiredArgsConstructor
public class AnnouncementController {
    private final AnnouncementService announcementService;

    @GetMapping
    public Page<Announcement> getAnnouncementsPage(
            @PageableDefault(size = 5)
            @SortDefault.SortDefaults({@SortDefault(sort = "id", direction = Sort.Direction.ASC)})
                    Pageable pageable) {
        return announcementService.getAnnouncementsPage(pageable);
    }

    @GetMapping("{announcementId}")
    public ResponseEntity<?> getAnnouncement(@PathVariable Long announcementId) {
        try {
            Optional<Announcement> announcementOptional = announcementService.getAnnouncement(announcementId);
            return announcementOptional
                    .map(announcement -> new ResponseEntity<>(announcement, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (AnnouncementNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<?> saveAnnouncement(@Valid @RequestBody AnnouncementReplaceRequest announcementRequest) {
        try {
            Announcement newAnnouncement = announcementService.saveAnnouncement(announcementRequest);
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(newAnnouncement.getId())
                    .toUri();
            return ResponseEntity.created(location).body(newAnnouncement);
        } catch (AnnouncementCategoryNotFoundException | RegionNotFoundException | UserProfileNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (AnnouncementExpirationDateFromPast e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("{announcementId}")
    public ResponseEntity<?> replaceAnnouncement(@PathVariable Long announcementId,
                                                 @Valid @RequestBody AnnouncementReplaceRequest replaceRequest) {
        try {
            return new ResponseEntity<>(announcementService.replaceAnnouncement(announcementId, replaceRequest), HttpStatus.OK);
        } catch (AnnouncementNotFoundException | AnnouncementCategoryNotFoundException
                | RegionNotFoundException | UserProfileNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (AnnouncementExpirationDateFromPast e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("{announcementId}")
    public ResponseEntity<?> modifyAnnouncement(@PathVariable Long announcementId,
                                                @Valid @RequestBody AnnouncementModifyRequest modifyRequest) {
        try {
            return new ResponseEntity<>(announcementService.modifyAnnouncement(announcementId, modifyRequest), HttpStatus.OK);
        } catch (AnnouncementNotFoundException | AnnouncementCategoryNotFoundException
                | RegionNotFoundException | UserProfileNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (AnnouncementExpirationDateFromPast e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("{announcementId}")
    public ResponseEntity<?> deleteAnnouncement(@PathVariable Long announcementId) {
        try {
            announcementService.deleteAnnouncement(announcementId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (EmptyResultDataAccessException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
