package pl.kzcwat.localincidentserver.userprofile;

import lombok.Data;
import pl.kzcwat.localincidentserver.announcement.Announcement;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Data
public class UserProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @OneToMany(mappedBy = "author")
    private List<Announcement> announcements;

    // TODO: this is a User class stub created to link with Announcement entity class.
}
