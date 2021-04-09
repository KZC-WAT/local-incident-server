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

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
class AnnouncementServiceTest {
    @Autowired
    private AnnouncementService announcementService;

    @Autowired
    private AnnouncementRepository announcementRepository;

    @Autowired
    private AnnouncementMapper announcementMapper;

    @Test
    public void getAnnouncementsPage_emptyDb_shouldReturnEmptyList() {
        Pageable pageRequest = PageRequest.of(0, 5);
        Page<Announcement> resultPage = announcementService.getAnnouncementsPage(pageRequest);
        List<Announcement> resultList = resultPage.getContent();

        assertEquals(0, resultList.size());
    }

    @Test
    public void getAnnouncementsPage_populatedDb_shouldReturnValidListSize() {
        int listSize = 42;

        List<Announcement> announcements = Stream.generate(AnnouncementSampleDataGenerator::getSampleAnnouncement)
                .limit(listSize)
                .collect(Collectors.toList());

        announcementRepository.saveAll(announcements);

        Pageable pageRequest = PageRequest.of(0, listSize + 1);
        Page<Announcement> resultPage = announcementService.getAnnouncementsPage(pageRequest);
        List<Announcement> resultList = resultPage.getContent();

        assertEquals(listSize, resultList.size());
    }

    @Test
    public void getAnnouncementById_existingId_shouldReturnSameEntity() {
        Announcement newAnnouncement = announcementRepository.save(AnnouncementSampleDataGenerator.getSampleAnnouncement());
        UUID newAnnouncementUuid = newAnnouncement.getId();

        Optional<Announcement> announcementOptional = announcementService.getAnnouncement(newAnnouncementUuid);

        if (announcementOptional.isPresent()) {
            Announcement getByIdAnnouncement = announcementOptional.get();
            assertEquals(newAnnouncement, getByIdAnnouncement);
        } else {
            Assertions.fail();
        }
    }
}