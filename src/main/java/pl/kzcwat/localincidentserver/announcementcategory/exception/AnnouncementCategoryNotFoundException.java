package pl.kzcwat.localincidentserver.announcementcategory.exception;

public class AnnouncementCategoryNotFoundException extends RuntimeException {
    public AnnouncementCategoryNotFoundException() {
        super("Announcement category with provided id was not found");
    }
}
