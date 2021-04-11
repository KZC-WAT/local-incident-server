package pl.kzcwat.localincidentserver.announcement.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.openapitools.jackson.nullable.JsonNullable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class AnnouncementModifyRequest {
    private JsonNullable<LocalDateTime> expirationDate = JsonNullable.undefined();

    @NotNull
    private JsonNullable<Long> regionId = JsonNullable.undefined();

    @NotNull
    private JsonNullable<Long> authorId = JsonNullable.undefined();

    @NotNull
    private JsonNullable<Long> categoryId = JsonNullable.undefined();

    @NotBlank
    private JsonNullable<String> title = JsonNullable.undefined();

    @NotBlank
    private JsonNullable<String> content = JsonNullable.undefined();
}
