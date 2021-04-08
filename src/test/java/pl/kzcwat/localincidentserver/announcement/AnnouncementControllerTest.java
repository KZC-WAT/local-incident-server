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
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static pl.kzcwat.localincidentserver.announcement.AnnouncementSampleDataGenerator.getSampleAnnouncement;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AnnouncementControllerTest {
    private static final String baseUri = "/api/v1/announcement/";
    private static final String illformedUuid = "foo-foo-foo-foo";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AnnouncementRepository announcementRepository;

    @Autowired
    private AnnouncementMapper announcementMapper;

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
        UUID newAnnouncementUuid = newAnnouncement.getId();

        mockMvc.perform(get(baseUri + newAnnouncementUuid))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
    }

    @Test
    public void getAnnouncementById_notExistingId_shouldReturnHttpNotFound() throws Exception {
        mockMvc.perform(get(baseUri + UUID.randomUUID()))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    public void getAnnouncementById_illformedId_shouldReturnHttpBadRequest() throws Exception {
        mockMvc.perform(get(baseUri + illformedUuid))
                .andDo(print())
                .andExpect(status().isBadRequest())
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
        AnnouncementReplaceRequest announcementReplaceRequest = announcementMapper.mapToReplaceRequest(announcement);
        String announcementReplaceRequestJson = objectMapper.writeValueAsString(announcementReplaceRequest);

        mockMvc.perform(post(baseUri).content(announcementReplaceRequestJson).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andReturn();
    }
}