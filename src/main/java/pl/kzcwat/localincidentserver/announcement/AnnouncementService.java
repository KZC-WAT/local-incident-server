package pl.kzcwat.localincidentserver.announcement;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.kzcwat.localincidentserver.announcement.exception.AnnouncementNotFoundException;
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
    public void replaceAnnouncement(Long announcementId, AnnouncementReplaceRequest replaceRequest) {
        announcementRepository.findById(announcementId).orElseThrow(AnnouncementNotFoundException::new);

        Announcement newAnnouncementVersion = announcementFactory.mapToEntity(replaceRequest);
        newAnnouncementVersion.setId(announcementId);

        announcementRepository.save(newAnnouncementVersion);
    }
}
