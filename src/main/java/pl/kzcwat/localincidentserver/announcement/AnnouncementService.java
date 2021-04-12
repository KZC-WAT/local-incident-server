package pl.kzcwat.localincidentserver.announcement;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.kzcwat.localincidentserver.announcement.exception.AnnouncementNotFoundException;
import pl.kzcwat.localincidentserver.announcement.request.AnnouncementModifyRequest;
import pl.kzcwat.localincidentserver.announcement.request.AnnouncementReplaceRequest;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AnnouncementService {
    private final AnnouncementRepository announcementRepository;
    private final AnnouncementFactory announcementFactory;

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
        Announcement announcementBeingModified = announcementRepository.findById(announcementId)
                .orElseThrow(AnnouncementNotFoundException::new);

        Announcement updatedAnnouncement = announcementFactory.updateAnnouncement(announcementBeingModified, modifyRequest);
        return announcementRepository.save(updatedAnnouncement);
    }
}
