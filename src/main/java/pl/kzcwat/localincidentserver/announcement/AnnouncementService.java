package pl.kzcwat.localincidentserver.announcement;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.kzcwat.localincidentserver.announcement.request.AnnouncementReplaceRequest;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AnnouncementService {
    private final AnnouncementRepository announcementRepository;
    private final AnnouncementFactory announcementFactory;

    public Page<Announcement> getAnnouncementsPage(Pageable pageable) {
        return announcementRepository.findAll(pageable);
    }

    public Optional<Announcement> getAnnouncement(Long announcementId) {
        return announcementRepository.findById(announcementId);
    }

    public Announcement saveAnnouncement(AnnouncementReplaceRequest announcementRequest) {
        Announcement newAnnouncement = announcementFactory.mapToEntity(announcementRequest);
        return announcementRepository.save(newAnnouncement);
    }

    public void deleteAnnouncement(Long announcementId) {
        announcementRepository.deleteById(announcementId);
    }
}
