package pl.kzcwat.localincidentserver.announcement.request;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Getter
public class AnnouncementReplaceRequest {
    private final LocalDateTime expitationDate;

    @NotNull
    private final UUID regionId;

    @NotNull
    private final UUID authorId;

    @NotBlank
    private final String title;

    @NotBlank
    private final String content;
}
