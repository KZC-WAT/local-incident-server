package pl.kzcwat.localincidentserver.announcement;

import pl.kzcwat.localincidentserver.announcementcategory.AnnouncementCategory;
import pl.kzcwat.localincidentserver.region.Region;
import pl.kzcwat.localincidentserver.userprofile.UserProfile;

import java.util.UUID;

public abstract class AnnouncementSampleDataGenerator {
    public static Announcement getSampleAnnouncement() {
        return Announcement.builder()
                .region(Region.builder().name(UUID.randomUUID().toString()).build())
                .author(new UserProfile())
                .category(AnnouncementCategory.builder().name(UUID.randomUUID().toString()).build())
                .title(UUID.randomUUID().toString())
                .content(UUID.randomUUID().toString())
                .build();
    }
}
