package pl.kzcwat.localincidentserver.announcementcategory;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AnnouncementCategoryRepository extends JpaRepository<AnnouncementCategory, UUID> {
}
