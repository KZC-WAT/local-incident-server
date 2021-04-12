package pl.kzcwat.localincidentserver.announcementcategory;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AnnouncementCategoryService {
    private final AnnouncementCategoryRepository announcementCategoryRepository;

    public Page<AnnouncementCategory> getAnnouncementCategoriesPage(Pageable pageable) {
        return announcementCategoryRepository.findAll(pageable);
    }

    public Optional<AnnouncementCategory> getAnnouncementCategory(Long announcementCategoryId) {
        return announcementCategoryRepository.findById(announcementCategoryId);
    }

    public void deleteAnnouncementCategory(Long announcementCategoryId) {
        announcementCategoryRepository.deleteById(announcementCategoryId);
    }
}