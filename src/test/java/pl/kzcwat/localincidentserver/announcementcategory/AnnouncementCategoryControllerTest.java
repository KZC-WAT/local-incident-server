package pl.kzcwat.localincidentserver.announcementcategory;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pl.kzcwat.localincidentserver.announcementcategory.request.AnnouncementCategoryModifyRequest;
import pl.kzcwat.localincidentserver.announcementcategory.request.AnnouncementCategoryReplaceRequest;

import javax.transaction.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static pl.kzcwat.localincidentserver.announcementcategory.AnnouncementCategorySampleDataGenerator.getSampleAnnouncementCategory;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AnnouncementCategoryControllerTest {
    private static final String baseUri = "/api/v1/announcement-categories/";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AnnouncementCategoryRepository announcementCategoryRepository;

    @Autowired
    private AnnouncementCategoryFactory announcementCategoryFactory;

    @Autowired
    private ObjectMapper objectMapper;

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

    @Test
    public void saveAnnouncementCategory_emptyBody_shouldReturnHttpBadRequest() throws Exception {
        mockMvc.perform(post(baseUri).content(""))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void saveAnnouncementCategory_emptyJson_shouldReturnHttpBadRequest() throws Exception {
        mockMvc.perform(post(baseUri).content("{}").contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void saveAnnouncementCategory_validJson_shouldReturnHttpOk() throws Exception {
        AnnouncementCategoryReplaceRequest replaceRequest = AnnouncementCategoryReplaceRequest.builder()
                .name("foo")
                .build();

        String replaceRequestJson = objectMapper.writeValueAsString(replaceRequest);

        mockMvc.perform(post(baseUri).content(replaceRequestJson).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();
    }

    @Test
    public void replaceAnnouncementCategory_emptyBody_shouldReturnHttpBadRequest() throws Exception {
        mockMvc.perform(put(baseUri + "42").content(""))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void replaceAnnouncementCategory_notExistingId_shouldReturnHttpNotFound() throws Exception {
        AnnouncementCategory announcementCategory = AnnouncementCategorySampleDataGenerator.getSampleAnnouncementCategory();
        AnnouncementCategoryReplaceRequest replaceRequest = announcementCategoryFactory.mapToDto(announcementCategory);
        String replaceRequestJson = objectMapper.writeValueAsString(replaceRequest);

        mockMvc.perform(put(baseUri + "42").content(replaceRequestJson).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    public void replaceAnnouncementCategory_existingId_shouldReturnHttpOk() throws Exception {
        AnnouncementCategory announcementCategory = AnnouncementCategorySampleDataGenerator.getSampleAnnouncementCategory();
        AnnouncementCategoryReplaceRequest replaceRequest = announcementCategoryFactory.mapToDto(announcementCategory);
        String replaceRequestJson = objectMapper.writeValueAsString(replaceRequest);

        AnnouncementCategory savedAnnouncementCategory = announcementCategoryRepository.save(announcementCategory);

        mockMvc.perform(
                put(baseUri + savedAnnouncementCategory.getId())
                        .content(replaceRequestJson)
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void modifyAnnouncementCategory_emptyBody_shouldReturnHttpBadRequest() throws Exception {
        mockMvc.perform(patch(baseUri + "42"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void modifyAnnouncementCategory_emptyJsonNotExistingId_shouldReturnHttpNotFound() throws Exception {
        mockMvc.perform(patch(baseUri + "42").content("{}").contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    public void modifyAnnouncementCategory_emptyJsonExistingId_shouldReturnHttpOk() throws Exception {
        AnnouncementCategory announcementCategory = AnnouncementCategorySampleDataGenerator.getSampleAnnouncementCategory();
        AnnouncementCategory savedAnnouncementCategory = announcementCategoryRepository.save(announcementCategory);

        mockMvc.perform(
                patch(baseUri + savedAnnouncementCategory.getId())
                        .content("{}")
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void modifyAnnouncementCategory_validJsonExistingId_shouldReturnHttpOk() throws Exception {
        AnnouncementCategory announcementCategory = AnnouncementCategorySampleDataGenerator.getSampleAnnouncementCategory();
        AnnouncementCategory savedAnnouncementCategory = announcementCategoryRepository.save(announcementCategory);

        AnnouncementCategoryModifyRequest modifyRequest = new AnnouncementCategoryModifyRequest();
        modifyRequest.setName(JsonNullable.of("foo"));

        String modifyRequestJson = objectMapper.writeValueAsString(modifyRequest);

        mockMvc.perform(
                patch(baseUri + savedAnnouncementCategory.getId())
                        .content(modifyRequestJson)
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
    }

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