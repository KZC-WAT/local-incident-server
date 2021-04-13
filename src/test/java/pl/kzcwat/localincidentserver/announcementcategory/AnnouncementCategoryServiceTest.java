package pl.kzcwat.localincidentserver.announcementcategory;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import pl.kzcwat.localincidentserver.announcementcategory.exception.AnnouncementCategoryNotFoundException;
import pl.kzcwat.localincidentserver.announcementcategory.request.AnnouncementCategoryReplaceRequest;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

    @Test
    public void saveAnnouncementCategory_shouldSave() {
        AnnouncementCategoryReplaceRequest replaceRequest = AnnouncementCategoryReplaceRequest.builder()
                .name("foo")
                .build();

        AnnouncementCategory newAnnouncementCategory = announcementCategoryService.saveAnnouncementCategory(replaceRequest);

        List<AnnouncementCategory> announcementCategories = announcementCategoryService
                .getAnnouncementCategoriesPage(PageRequest.of(0, 5))
                .toList();

        assertThat(announcementCategories, Matchers.hasSize(1));
        assertEquals(newAnnouncementCategory, announcementCategories.get(0));
    }

    @Test
    public void saveAnnouncementCategory_notExistingSuperCategoryId_shouldThrow() {
        AnnouncementCategoryReplaceRequest replaceRequest = AnnouncementCategoryReplaceRequest.builder()
                .name("foo")
                .superCategoryId(42L)
                .build();

        assertThrows(AnnouncementCategoryNotFoundException.class,
                () -> announcementCategoryService.saveAnnouncementCategory(replaceRequest));
    }
}