package pl.kzcwat.localincidentserver.announcement;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.kzcwat.localincidentserver.announcement.exception.AnnouncementExpirationDateFromPast;
import pl.kzcwat.localincidentserver.announcement.exception.AnnouncementNotFoundException;
import pl.kzcwat.localincidentserver.announcement.request.AnnouncementModifyRequest;
import pl.kzcwat.localincidentserver.announcement.request.AnnouncementReplaceRequest;
import pl.kzcwat.localincidentserver.announcementcategory.AnnouncementCategory;
import pl.kzcwat.localincidentserver.announcementcategory.AnnouncementCategoryRepository;
import pl.kzcwat.localincidentserver.announcementcategory.exception.AnnouncementCategoryNotFoundException;
import pl.kzcwat.localincidentserver.region.Region;
import pl.kzcwat.localincidentserver.region.RegionRepository;
import pl.kzcwat.localincidentserver.region.exception.RegionNotFoundException;
import pl.kzcwat.localincidentserver.userprofile.UserProfile;
import pl.kzcwat.localincidentserver.userprofile.UserProfileRepository;
import pl.kzcwat.localincidentserver.userprofile.exception.UserProfileNotFoundException;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AnnouncementService {
    private final AnnouncementRepository announcementRepository;
    private final AnnouncementFactory announcementFactory;

    private final RegionRepository regionRepository;
    private final UserProfileRepository userProfileRepository;
    private final AnnouncementCategoryRepository announcementCategoryRepository;

    @Transactional
    public Page<Announcement> getAnnouncementsPage(Pageable pageable) {
        return announcementRepository.findAll(pageable);
    }

    @Transactional
    public Optional<Announcement> getAnnouncement(Long announcementId) {
        return announcementRepository.findById(announcementId);
    }

    @Transactional
    public Announcement saveAnnouncement(AnnouncementReplaceRequest announcementRequest) {
        Announcement newAnnouncement = announcementFactory.mapToEntity(announcementRequest);
        return announcementRepository.save(newAnnouncement);
    }

    @Transactional
    public void deleteAnnouncement(Long announcementId) {
        announcementRepository.deleteById(announcementId);
    }

    @Transactional
    public Announcement replaceAnnouncement(Long announcementId, AnnouncementReplaceRequest replaceRequest) {
        announcementRepository.findById(announcementId).orElseThrow(AnnouncementNotFoundException::new);

        Announcement newAnnouncementVersion = announcementFactory.mapToEntity(replaceRequest);
        newAnnouncementVersion.setId(announcementId);

        return announcementRepository.save(newAnnouncementVersion);
    }

    public Announcement modifyAnnouncement(Long announcementId, AnnouncementModifyRequest modifyRequest) {
        Announcement modifiedAnnouncement = announcementRepository.findById(announcementId)
                .orElseThrow(AnnouncementNotFoundException::new);

        if (modifyRequest.getExpirationDate().isPresent()) {
            if (modifyRequest.getExpirationDate().get().isBefore(LocalDateTime.now())) {
                throw new AnnouncementExpirationDateFromPast();
            } else {
                modifiedAnnouncement.setExpirationDate(modifyRequest.getExpirationDate().get());
            }
        }

        if (modifyRequest.getRegionId().isPresent()) {
            Region region = regionRepository.findById(modifyRequest.getRegionId().get())
                    .orElseThrow(RegionNotFoundException::new);
            modifiedAnnouncement.setRegion(region);
        }
        if (modifyRequest.getAuthorId().isPresent()) {
            UserProfile author = userProfileRepository.findById(modifyRequest.getAuthorId().get())
                    .orElseThrow(UserProfileNotFoundException::new);
            modifiedAnnouncement.setAuthor(author);
        }
        if (modifyRequest.getCategoryId().isPresent()) {
            AnnouncementCategory category = announcementCategoryRepository.findById(modifyRequest.getCategoryId().get())
                    .orElseThrow(AnnouncementCategoryNotFoundException::new);
            modifiedAnnouncement.setCategory(category);
        }

        if (modifyRequest.getTitle().isPresent()) {
            modifiedAnnouncement.setTitle(modifyRequest.getTitle().get());
        }
        if (modifyRequest.getContent().isPresent()) {
            modifiedAnnouncement.setContent(modifyRequest.getContent().get());
        }

        return announcementRepository.save(modifiedAnnouncement);
    }
}
