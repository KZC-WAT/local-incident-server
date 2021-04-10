package pl.kzcwat.localincidentserver.announcement;

import pl.kzcwat.localincidentserver.region.Region;
import pl.kzcwat.localincidentserver.userprofile.UserProfile;

import java.util.UUID;

public abstract class AnnouncementSampleDataGenerator {
    public static Announcement getSampleAnnouncement() {
        String randomName = UUID.randomUUID().toString();
        return Announcement.builder()
                .region(Region.builder().name(randomName).build())
                .author(new UserProfile())
                .title("foo")
                .content("bar")
                .build();
    }
}
