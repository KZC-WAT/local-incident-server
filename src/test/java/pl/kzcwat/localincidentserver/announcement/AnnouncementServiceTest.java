package pl.kzcwat.localincidentserver.announcement;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import pl.kzcwat.localincidentserver.announcement.exception.AnnouncementExpirationDateFromPast;
import pl.kzcwat.localincidentserver.announcement.exception.AnnouncementNotFoundException;
import pl.kzcwat.localincidentserver.announcement.request.AnnouncementReplaceRequest;
import pl.kzcwat.localincidentserver.announcementcategory.AnnouncementCategory;
import pl.kzcwat.localincidentserver.announcementcategory.AnnouncementCategoryRepository;
import pl.kzcwat.localincidentserver.region.Region;
import pl.kzcwat.localincidentserver.region.RegionRepository;
import pl.kzcwat.localincidentserver.userprofile.UserProfile;
import pl.kzcwat.localincidentserver.userprofile.UserProfileRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

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
    private AnnouncementCategoryRepository announcementCategoryRepository;

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
        Long newAnnouncementUuid = newAnnouncement.getId();

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
        Region newRegion = regionRepository.save(Region.builder().name("foo").build());
        UserProfile newUserProfile = userProfileRepository.save(new UserProfile());
        AnnouncementCategory newAnnouncementCategory
                = announcementCategoryRepository.save(AnnouncementCategory.builder().name("cat").build());

        AnnouncementReplaceRequest insertedAnnouncement = AnnouncementReplaceRequest.builder()
                .regionId(newRegion.getId())
                .authorId(newUserProfile.getId())
                .categoryId(newAnnouncementCategory.getId())
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
    public void replaceAnnouncement_shouldReplaceSpecificFields() {
        Announcement insertedAnnouncement = announcementRepository.save(AnnouncementSampleDataGenerator.getSampleAnnouncement());
        Long replacedAnnouncementId = insertedAnnouncement.getId();

        Region newRegion = regionRepository.save(Region.builder().name("foo").build());
        UserProfile newUserProfile = userProfileRepository.save(new UserProfile());
        AnnouncementCategory newAnnouncementCategory
                = announcementCategoryRepository.save(AnnouncementCategory.builder().name("cat").build());

        AnnouncementReplaceRequest replaceRequest = AnnouncementReplaceRequest.builder()
                .regionId(newRegion.getId())
                .authorId(newUserProfile.getId())
                .categoryId(newAnnouncementCategory.getId())
                .title("foo")
                .content("bar")
                .build();

        announcementService.replaceAnnouncement(replacedAnnouncementId, replaceRequest);

        try {
            Announcement mappedReplaceRequest = announcementFactory.mapToEntity(replaceRequest);
            Announcement announcementAfterReplace = announcementRepository.findById(replacedAnnouncementId)
                    .orElseThrow(AnnouncementNotFoundException::new);

            assertEquals(mappedReplaceRequest.getExpirationDate(), announcementAfterReplace.getExpirationDate());
            assertEquals(mappedReplaceRequest.getTitle(), announcementAfterReplace.getTitle());
            assertEquals(mappedReplaceRequest.getContent(), announcementAfterReplace.getContent());
            assertEquals(mappedReplaceRequest.getAuthor(), announcementAfterReplace.getAuthor());
            assertEquals(mappedReplaceRequest.getRegion(), announcementAfterReplace.getRegion());
        } catch (AnnouncementNotFoundException e) {
            fail();
        }
    }

    @Test
    public void replaceAnnouncement_shouldNotInsertNewRecords() {
        Announcement insertedAnnouncement
                = announcementRepository.save(AnnouncementSampleDataGenerator.getSampleAnnouncement());

        Long replacedAnnouncementId = insertedAnnouncement.getId();
        long announcementsCountBeforeReplace = announcementRepository.count();

        Region newRegion = regionRepository.save(Region.builder().name("foo").build());
        UserProfile newUserProfile = userProfileRepository.save(new UserProfile());
        AnnouncementCategory newAnnouncementCategory
                = announcementCategoryRepository.save(AnnouncementCategory.builder().name("cat").build());

        AnnouncementReplaceRequest replaceRequest = AnnouncementReplaceRequest.builder()
                .regionId(newRegion.getId())
                .authorId(newUserProfile.getId())
                .categoryId(newAnnouncementCategory.getId())
                .title("foo")
                .content("bar")
                .build();

        announcementService.replaceAnnouncement(replacedAnnouncementId, replaceRequest);

        long announcementsCountAfterReplace = announcementRepository.count();
        assertEquals(announcementsCountBeforeReplace, announcementsCountAfterReplace);
    }

    @Test
    public void replaceAnnouncement_expirationDateFromPast_shouldThrow() {
        Announcement insertedAnnouncement
                = announcementRepository.save(AnnouncementSampleDataGenerator.getSampleAnnouncement());
        Long replacedAnnouncementId = insertedAnnouncement.getId();

        Region newRegion = regionRepository.save(Region.builder().name("foo").build());
        UserProfile newUserProfile = userProfileRepository.save(new UserProfile());
        AnnouncementCategory newAnnouncementCategory
                = announcementCategoryRepository.save(AnnouncementCategory.builder().name("cat").build());

        AnnouncementReplaceRequest replaceRequest = AnnouncementReplaceRequest.builder()
                .regionId(newRegion.getId())
                .authorId(newUserProfile.getId())
                .categoryId(newAnnouncementCategory.getId())
                .expirationDate(LocalDateTime.now().minusDays(42))
                .title("foo")
                .content("bar")
                .build();

        assertThrows(AnnouncementExpirationDateFromPast.class,
                () -> announcementService.replaceAnnouncement(replacedAnnouncementId, replaceRequest));
    }

    @Test
    public void deleteAnnouncement_shouldDelete() {
        Region newRegion = regionRepository.save(Region.builder().name("foo").build());
        UserProfile newUserProfile = userProfileRepository.save(new UserProfile());
        AnnouncementCategory newAnnouncementCategory
                = announcementCategoryRepository.save(AnnouncementCategory.builder().name("cat").build());

        AnnouncementReplaceRequest insertedAnnouncement = AnnouncementReplaceRequest.builder()
                .regionId(newRegion.getId())
                .authorId(newUserProfile.getId())
                .categoryId(newAnnouncementCategory.getId())
                .title("foo")
                .content("bar")
                .build();

        Long newAnnouncementId = announcementService.saveAnnouncement(insertedAnnouncement).getId();

        announcementService.deleteAnnouncement(newAnnouncementId);

        List<Announcement> announcements = announcementService
                .getAnnouncementsPage(PageRequest.of(0, 5))
                .toList();

        assertThat(announcements, Matchers.hasSize(0));
    }
}