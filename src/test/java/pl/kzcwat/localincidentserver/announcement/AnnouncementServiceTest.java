package pl.kzcwat.localincidentserver.announcement;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import pl.kzcwat.localincidentserver.announcement.request.AnnouncementReplaceRequest;
import pl.kzcwat.localincidentserver.region.Region;
import pl.kzcwat.localincidentserver.region.RegionRepository;
import pl.kzcwat.localincidentserver.userprofile.UserProfile;
import pl.kzcwat.localincidentserver.userprofile.UserProfileRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
class AnnouncementServiceTest {
    @Autowired
    private AnnouncementService announcementService;

    @Autowired
    private AnnouncementRepository announcementRepository;

    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private AnnouncementFactory announcementFactory;

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

    @Test
    public void saveAnnouncement_shouldSave() {
        Region newRegion = regionRepository.save(new Region());
        UserProfile newUserProfile = userProfileRepository.save(new UserProfile());

        AnnouncementReplaceRequest insertedAnnouncement = AnnouncementReplaceRequest.builder()
                .regionId(newRegion.getId())
                .authorId(newUserProfile.getId())
                .title("foo")
                .content("bar")
                .build();

        Announcement newAnnouncement = announcementService.saveAnnouncement(insertedAnnouncement);

        List<Announcement> announcements = announcementService
                .getAnnouncementsPage(PageRequest.of(0, 5))
                .toList();

        assertThat(announcements, Matchers.hasSize(1));
        assertEquals(newAnnouncement, announcements.get(0));
    }

    @Test
    public void deleteAnnouncement_shouldDelete() {
        Region newRegion = regionRepository.save(new Region());
        UserProfile newUserProfile = userProfileRepository.save(new UserProfile());

        AnnouncementReplaceRequest insertedAnnouncement = AnnouncementReplaceRequest.builder()
                .regionId(newRegion.getId())
                .authorId(newUserProfile.getId())
                .title("foo")
                .content("bar")
                .build();

        UUID newAnnouncementUuid = announcementService.saveAnnouncement(insertedAnnouncement).getId();

        announcementService.deleteAnnouncement(newAnnouncementUuid);

        List<Announcement> announcements = announcementService
                .getAnnouncementsPage(PageRequest.of(0, 5))
                .toList();

        assertThat(announcements, Matchers.hasSize(0));
    }
}