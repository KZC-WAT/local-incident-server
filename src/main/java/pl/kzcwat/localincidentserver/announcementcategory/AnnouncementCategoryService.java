package pl.kzcwat.localincidentserver.announcementcategory;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
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
    public void deleteAnnouncementCategory(Long announcementCategoryId) {
        announcementCategoryRepository.deleteById(announcementCategoryId);
    }
}
