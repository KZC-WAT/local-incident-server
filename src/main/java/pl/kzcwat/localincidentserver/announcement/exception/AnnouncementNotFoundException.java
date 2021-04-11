package pl.kzcwat.localincidentserver.announcement.exception;

public class AnnouncementNotFoundException extends RuntimeException {
    public AnnouncementNotFoundException() {
        super("Announcement with provided id was not found");
    }
}
