package pl.kzcwat.localincidentserver.announcementcategory.request;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Builder
@Getter
public class AnnouncementCategoryReplaceRequest {
    private final Long superCategoryId;

    @NotBlank
    @Size(min = 3)
    private final String name;
}
