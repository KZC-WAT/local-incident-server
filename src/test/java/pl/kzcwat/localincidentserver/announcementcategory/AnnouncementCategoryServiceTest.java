package pl.kzcwat.localincidentserver.announcementcategory;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import pl.kzcwat.localincidentserver.announcement.Announcement;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
class AnnouncementCategoryServiceTest {
    @Autowired
    private AnnouncementCategoryService announcementCategoryService;

    @Autowired
    private AnnouncementCategoryRepository announcementCategoryRepository;

    @Test
    public void getAnnouncementCategoriesPage_emptyDb_shouldReturnEmptyList() {
        Pageable pageRequest = PageRequest.of(0, 5);
        Page<AnnouncementCategory> resultPage = announcementCategoryService.getAnnouncementCategoriesPage(pageRequest);
        List<AnnouncementCategory> resultList = resultPage.getContent();

        assertEquals(0, resultList.size());
    }

    @Test
    public void getAnnouncementCategoriesPage_populatedDb_shouldReturnValidListSize() {
        int listSize = 42;

        List<AnnouncementCategory> announcementCategories =
                Stream.generate(AnnouncementCategorySampleDataGenerator::getSampleAnnouncementCategory)
                        .limit(listSize)
                        .collect(Collectors.toList());

        announcementCategoryRepository.saveAll(announcementCategories);

        Pageable pageRequest = PageRequest.of(0, listSize + 1);
        Page<AnnouncementCategory> resultPage = announcementCategoryService.getAnnouncementCategoriesPage(pageRequest);
        List<AnnouncementCategory> resultList = resultPage.getContent();

        assertEquals(listSize, resultList.size());
    }

    @Test
    public void getAnnouncementCategoryById_existingId_shouldReturnSameEntity() {
        AnnouncementCategory newAnnouncement = announcementCategoryRepository.save(
                AnnouncementCategorySampleDataGenerator.getSampleAnnouncementCategory());
        Long newAnnouncementUuid = newAnnouncement.getId();

        Optional<AnnouncementCategory> announcementCategoryOptional
                = announcementCategoryService.getAnnouncementCategory(newAnnouncementUuid);

        if (announcementCategoryOptional.isPresent()) {
            AnnouncementCategory getByIdAnnouncementCategory = announcementCategoryOptional.get();
            assertEquals(newAnnouncement, getByIdAnnouncementCategory);
        } else {
            Assertions.fail();
        }
    }
}