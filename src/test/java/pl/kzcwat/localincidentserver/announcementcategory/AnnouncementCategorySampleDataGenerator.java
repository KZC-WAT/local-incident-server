package pl.kzcwat.localincidentserver.announcementcategory;

import java.util.UUID;

public abstract class AnnouncementCategorySampleDataGenerator {
    public static AnnouncementCategory getSampleAnnouncementCategory() {
        return AnnouncementCategory.builder()
                .name(UUID.randomUUID().toString())
                .build();
    }
}
