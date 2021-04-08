package pl.kzcwat.localincidentserver.announcement;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootTest
@Transactional
class AnnouncementServiceTest {
    @Autowired
    private AnnouncementService announcementService;

    @Autowired
    private AnnouncementRepository announcementRepository;

    @Test
    public void getAnnouncementsPage_emptyDb_shouldReturnEmptyList() {
        Pageable pageRequest = PageRequest.of(0, 5);
        Page<Announcement> resultPage = announcementService.getAnnouncementsPage(pageRequest);
        List<Announcement> resultList = resultPage.getContent();

        Assertions.assertEquals(0, resultList.size());
    }

    @Test
    public void getAnnouncementsPage_populatedDb_shouldReturnValidListSize() {
        int listSize = 42;

        List<Announcement> announcements = Stream.generate(Announcement::new)
                .limit(listSize)
                .collect(Collectors.toList());

        announcementRepository.saveAll(announcements);

        Pageable pageRequest = PageRequest.of(0, listSize + 1);
        Page<Announcement> resultPage = announcementService.getAnnouncementsPage(pageRequest);
        List<Announcement> resultList = resultPage.getContent();

        Assertions.assertEquals(listSize, resultList.size());
    }

    @Test
    public void getAnnouncementById_existingId_shouldReturnSameEntity() {
        Announcement newAnnouncement = announcementRepository.save(new Announcement());
        UUID newAnnouncementUuid = newAnnouncement.getId();

        Optional<Announcement> announcementOptional = announcementService.getAnnouncement(newAnnouncementUuid);

        if (announcementOptional.isPresent()) {
            Announcement getByIdAnnouncement = announcementOptional.get();
            Assertions.assertEquals(newAnnouncement, getByIdAnnouncement);
        } else {
            Assertions.fail();
        }
    }
}