package pl.kzcwat.localincidentserver.announcementcategory;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.kzcwat.localincidentserver.announcementcategory.exception.AnnouncementCategoryNotFoundException;
import pl.kzcwat.localincidentserver.announcementcategory.request.AnnouncementCategoryReplaceRequest;

@Component
@RequiredArgsConstructor
public class AnnouncementCategoryFactory {
    private final AnnouncementCategoryRepository announcementCategoryRepository;

    public AnnouncementCategory mapToEntity(AnnouncementCategoryReplaceRequest replaceRequest) {
        AnnouncementCategory superCategory;

        if (replaceRequest.getSuperCategoryId() != null) {
            superCategory = announcementCategoryRepository
                    .findById(replaceRequest.getSuperCategoryId())
                    .orElseThrow(AnnouncementCategoryNotFoundException::new);
        } else {
            superCategory = null;
        }

        return AnnouncementCategory.builder()
                .superCategory(superCategory)
                .name(replaceRequest.getName())
                .build();
    }

    public AnnouncementCategoryReplaceRequest mapToDto(AnnouncementCategory announcementCategory) {
        Long superCategoryId = announcementCategory.getSuperCategory() == null
                ? null
                : announcementCategory.getSuperCategory().getId();

        return AnnouncementCategoryReplaceRequest.builder()
                .superCategoryId(superCategoryId)
                .name(announcementCategory.getName())
                .build();
    }
}
