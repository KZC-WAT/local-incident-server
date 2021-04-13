package pl.kzcwat.localincidentserver.announcementcategory.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.openapitools.jackson.nullable.JsonNullable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
public class AnnouncementCategoryModifyRequest {
    private final JsonNullable<Long> superCategoryId = JsonNullable.undefined();

    @NotBlank
    @Size(min = 3)
    private final JsonNullable<String> name = JsonNullable.undefined();
}
