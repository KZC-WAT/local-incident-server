package pl.kzcwat.localincidentserver.announcement;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.kzcwat.localincidentserver.announcement.exception.AnnouncementNotFoundException;
import pl.kzcwat.localincidentserver.announcement.request.AnnouncementReplaceRequest;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/announcement")
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
    public ResponseEntity<?> getAnnouncement(@PathVariable UUID announcementId) {
        try {
            Optional<Announcement> announcementOptional = announcementService.getAnnouncement(announcementId);
            return announcementOptional
                    .map(announcement -> new ResponseEntity<>(announcement, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (AnnouncementNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<?> saveAnnouncement(@Valid @RequestBody AnnouncementReplaceRequest announcementRequest,
                                              @ApiIgnore Errors errors) {
        if (errors.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            Announcement newAnnouncement = announcementService.saveAnnouncement(announcementRequest);
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(newAnnouncement.getId())
                    .toUri();
            return ResponseEntity.created(location).body(newAnnouncement);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
