package pl.kzcwat.localincidentserver.announcementcategory;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.kzcwat.localincidentserver.announcementcategory.exception.AnnouncementCategoryNotFoundException;
import pl.kzcwat.localincidentserver.announcementcategory.request.AnnouncementCategoryModifyRequest;
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

    public AnnouncementCategory updateAnnouncementCategory(AnnouncementCategory updatedAnnouncementCategory,
                                                           AnnouncementCategoryModifyRequest modifyRequest) {
        AnnouncementCategory superCategory;

        if (modifyRequest.getSuperCategoryId().isPresent()) {
            if (modifyRequest.getSuperCategoryId() != null) {
                superCategory = announcementCategoryRepository
                        .findById(modifyRequest.getSuperCategoryId().get())
                        .orElseThrow(AnnouncementCategoryNotFoundException::new);
            } else {
                superCategory = null;
            }

            updatedAnnouncementCategory.setSuperCategory(superCategory);
        }

        if (modifyRequest.getName().isPresent()) {
            if (modifyRequest.getName() != null) {
                updatedAnnouncementCategory.setName(modifyRequest.getName().get());
            }
        }

        return updatedAnnouncementCategory;
    }
}
