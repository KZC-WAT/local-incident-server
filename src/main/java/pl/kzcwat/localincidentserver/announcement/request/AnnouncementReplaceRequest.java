package pl.kzcwat.localincidentserver.announcement.request;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Builder
@Getter
public class AnnouncementReplaceRequest {
    private final LocalDateTime expirationDate;

    @NotNull
    private final Long regionId;

    @NotNull
    private final Long authorId;

    @NotBlank
    private final String title;

    @NotBlank
    private final String content;
}
