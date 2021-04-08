package pl.kzcwat.localincidentserver.announcement;

import pl.kzcwat.localincidentserver.region.Region;
import pl.kzcwat.localincidentserver.userprofile.UserProfile;

public abstract class AnnouncementSampleDataGenerator {
    public static Announcement getSampleAnnouncement() {
        return Announcement.builder()
                .region(new Region())
                .author(new UserProfile())
                .title("foo")
                .content("bar")
                .build();
    }
}
