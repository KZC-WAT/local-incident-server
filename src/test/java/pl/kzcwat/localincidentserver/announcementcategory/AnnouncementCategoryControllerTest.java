package pl.kzcwat.localincidentserver.announcementcategory;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static pl.kzcwat.localincidentserver.announcementcategory.AnnouncementCategorySampleDataGenerator.getSampleAnnouncementCategory;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AnnouncementCategoryControllerTest {
    private static final String baseUri = "/api/v1/announcementCategories/";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AnnouncementCategoryRepository announcementCategoryRepository;

    @Test
    public void getAnnouncementCategoriesPage_shouldReturnHttpOk() throws Exception {
        mockMvc.perform(get(baseUri))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
    }

    @Test
    public void getAnnouncementCategoryById_existingId_shouldReturnHttpCreated() throws Exception {
        AnnouncementCategory newAnnouncementCategory = announcementCategoryRepository.save(getSampleAnnouncementCategory());
        Long newAnnouncementCategoryId = newAnnouncementCategory.getId();

        mockMvc.perform(get(baseUri + newAnnouncementCategoryId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
    }

    @Test
    public void getAnnouncementCategoryById_notExistingId_shouldReturnHttpNotFound() throws Exception {
        mockMvc.perform(get(baseUri + "42"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();
    }

    // TODO: save, replace, modify methods tests

    @Test
    public void deleteAnnouncementCategory_notExistingId_shouldReturnHttpNotFound() throws Exception {
        mockMvc.perform(delete(baseUri + "42"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    public void deleteAnnouncementCategory_existingId_shouldReturnHttpOk() throws Exception {
        AnnouncementCategory newAnnouncementCategory = announcementCategoryRepository.save(getSampleAnnouncementCategory());
        Long newAnnouncementCategoryId = newAnnouncementCategory.getId();

        mockMvc.perform(delete(baseUri + newAnnouncementCategoryId))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
    }
}