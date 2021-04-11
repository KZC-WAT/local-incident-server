package pl.kzcwat.localincidentserver.announcement;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.kzcwat.localincidentserver.announcement.exception.AnnouncementExpirationDateFromPast;
import pl.kzcwat.localincidentserver.announcement.request.AnnouncementReplaceRequest;
import pl.kzcwat.localincidentserver.announcementcategory.AnnouncementCategory;
import pl.kzcwat.localincidentserver.announcementcategory.AnnouncementCategoryRepository;
import pl.kzcwat.localincidentserver.announcementcategory.exception.AnnouncementCategoryNotFoundException;
import pl.kzcwat.localincidentserver.region.Region;
import pl.kzcwat.localincidentserver.region.RegionRepository;
import pl.kzcwat.localincidentserver.region.exception.RegionNotFoundException;
import pl.kzcwat.localincidentserver.userprofile.UserProfile;
import pl.kzcwat.localincidentserver.userprofile.UserProfileRepository;
import pl.kzcwat.localincidentserver.userprofile.exception.UserProfileNotFoundException;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class AnnouncementFactory {
    private final AnnouncementCategoryRepository announcementCategoryRepository;
    private final RegionRepository regionRepository;
    private final UserProfileRepository userProfileRepository;

    public Announcement mapToEntity(AnnouncementReplaceRequest replaceRequest) {
        Region region = regionRepository
                .findById(replaceRequest.getRegionId())
                .orElseThrow(RegionNotFoundException::new);

        UserProfile author = userProfileRepository
                .findById(replaceRequest.getAuthorId())
                .orElseThrow(UserProfileNotFoundException::new);

        AnnouncementCategory announcementCategory = announcementCategoryRepository
                .findById(replaceRequest.getCategoryId())
                .orElseThrow(AnnouncementCategoryNotFoundException::new);

        if (replaceRequest.getExpirationDate() != null
                && replaceRequest.getExpirationDate().isBefore(LocalDateTime.now())) {
            throw new AnnouncementExpirationDateFromPast();
        }

        return Announcement.builder()
                .publicationDate(LocalDateTime.now())
                .expirationDate(replaceRequest.getExpirationDate())
                .region(region)
                .author(author)
                .announcementCategory(announcementCategory)
                .title(replaceRequest.getTitle())
                .content(replaceRequest.getContent())
                .build();
    }

    public AnnouncementReplaceRequest mapToReplaceRequest(Announcement announcement) {
        return AnnouncementReplaceRequest.builder()
                .expirationDate(announcement.getExpirationDate())
                .regionId(announcement.getRegion().getId())
                .authorId(announcement.getAuthor().getId())
                .categoryId(announcement.getAnnouncementCategory().getId())
                .title(announcement.getTitle())
                .content(announcement.getContent())
                .build();
    }
}