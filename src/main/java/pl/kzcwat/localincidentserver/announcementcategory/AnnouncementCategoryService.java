package pl.kzcwat.localincidentserver.announcementcategory;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.kzcwat.localincidentserver.announcementcategory.exception.AnnouncementCategoryNotFoundException;
import pl.kzcwat.localincidentserver.announcementcategory.request.AnnouncementCategoryModifyRequest;
import pl.kzcwat.localincidentserver.announcementcategory.request.AnnouncementCategoryReplaceRequest;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AnnouncementCategoryService {
    private final AnnouncementCategoryRepository announcementCategoryRepository;
    private final AnnouncementCategoryFactory announcementCategoryFactory;

    @Transactional
    public Page<AnnouncementCategory> getAnnouncementCategoriesPage(Pageable pageable) {
        return announcementCategoryRepository.findAll(pageable);
    }

    @Transactional
    public Optional<AnnouncementCategory> getAnnouncementCategory(Long announcementCategoryId) {
        return announcementCategoryRepository.findById(announcementCategoryId);
    }

    @Transactional
    public AnnouncementCategory saveAnnouncementCategory(AnnouncementCategoryReplaceRequest replaceRequest) {
        AnnouncementCategory newAnnouncementCategory = announcementCategoryFactory.mapToEntity(replaceRequest);
        return announcementCategoryRepository.save(newAnnouncementCategory);
    }

    @Transactional
    public AnnouncementCategory replaceAnnouncementCategory(Long announcementCategoryId,
                                                            AnnouncementCategoryReplaceRequest replaceRequest) {
        announcementCategoryRepository
                .findById(announcementCategoryId)
                .orElseThrow(AnnouncementCategoryNotFoundException::new);

        AnnouncementCategory updatedAnnouncementCategory = announcementCategoryFactory.mapToEntity(replaceRequest);
        updatedAnnouncementCategory.setId(announcementCategoryId);

        return announcementCategoryRepository.save(updatedAnnouncementCategory);
    }

    @Transactional
    public AnnouncementCategory modifyAnnouncementCategory(Long announcementCategoryId,
                                                           AnnouncementCategoryModifyRequest modifyRequest) {
        AnnouncementCategory announcementCategoryBeingModified =
                announcementCategoryRepository.findById(announcementCategoryId)
                        .orElseThrow(AnnouncementCategoryNotFoundException::new);

        AnnouncementCategory updatedAnnouncementCategory =
                announcementCategoryFactory.updateAnnouncementCategory(announcementCategoryBeingModified, modifyRequest);
        return announcementCategoryRepository.save(updatedAnnouncementCategory);
    }

    @Transactional
    public void deleteAnnouncementCategory(Long announcementCategoryId) {
        announcementCategoryRepository.deleteById(announcementCategoryId);
    }
}
