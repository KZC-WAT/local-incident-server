package pl.kzcwat.localincidentserver.announcement;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pl.kzcwat.localincidentserver.announcement.request.AnnouncementReplaceRequest;

import javax.transaction.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static pl.kzcwat.localincidentserver.announcement.AnnouncementSampleDataGenerator.getSampleAnnouncement;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AnnouncementControllerTest {
    private static final String baseUri = "/api/v1/announcements/";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AnnouncementRepository announcementRepository;

    @Autowired
    private AnnouncementFactory announcementFactory;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void getAnnouncementsPage_shouldReturnHttpOk() throws Exception {
        mockMvc.perform(get(baseUri))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
    }

    @Test
    public void getAnnouncementById_existingId_shouldReturnHttpCreated() throws Exception {
        Announcement newAnnouncement = announcementRepository.save(getSampleAnnouncement());
        Long newAnnouncementId = newAnnouncement.getId();

        mockMvc.perform(get(baseUri + newAnnouncementId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
    }

    @Test
    public void getAnnouncementById_notExistingId_shouldReturnHttpNotFound() throws Exception {
        mockMvc.perform(get(baseUri + "42"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    public void saveAnnouncement_emptyBody_shouldReturnHttpBadRequest() throws Exception {
        mockMvc.perform(post(baseUri).content(""))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void saveAnnouncement_emptyJson_shouldReturnHttpBadRequest() throws Exception {
        mockMvc.perform(post(baseUri).content("{}").contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void saveAnnouncement_validJson_shouldReturnHttpOk() throws Exception {
        Announcement announcement = announcementRepository.save(AnnouncementSampleDataGenerator.getSampleAnnouncement());
        AnnouncementReplaceRequest announcementReplaceRequest = announcementFactory.mapToReplaceRequest(announcement);
        String announcementReplaceRequestJson = objectMapper.writeValueAsString(announcementReplaceRequest);

        mockMvc.perform(post(baseUri).content(announcementReplaceRequestJson).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andReturn();
    }

    @Test
    public void replaceAnnouncement_emptyBody_shouldReturnHttpBadRequest() throws Exception {
        mockMvc.perform(put(baseUri + "42").content(""))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void replaceAnnouncement_notExistingId_shouldReturnHttpNotFound() throws Exception {
        Announcement announcement = announcementRepository.save(AnnouncementSampleDataGenerator.getSampleAnnouncement());
        AnnouncementReplaceRequest announcementReplaceRequest = announcementFactory.mapToReplaceRequest(announcement);
        String announcementReplaceRequestJson = objectMapper.writeValueAsString(announcementReplaceRequest);

        mockMvc.perform(
                put(baseUri + "42")
                        .content(announcementReplaceRequestJson)
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    public void replaceAnnouncement_existingId_shouldReturnHttpOk() throws Exception {
        Announcement announcement = announcementRepository.save(AnnouncementSampleDataGenerator.getSampleAnnouncement());
        AnnouncementReplaceRequest announcementReplaceRequest = announcementFactory.mapToReplaceRequest(announcement);
        String announcementReplaceRequestJson = objectMapper.writeValueAsString(announcementReplaceRequest);

        mockMvc.perform(
                put(baseUri + announcement.getId())
                        .content(announcementReplaceRequestJson)
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void deleteAnnouncement_notExistingId_shouldReturnHttpNotFound() throws Exception {
        mockMvc.perform(delete(baseUri + "42"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();
    }
}