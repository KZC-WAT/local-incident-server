package pl.kzcwat.localincidentserver.userprofile;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import pl.kzcwat.localincidentserver.announcement.Announcement;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class UserProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @JsonIgnore
    @OneToMany(mappedBy = "author")
    private List<Announcement> announcements;

    // TODO: this is a User class stub created to link with Announcement entity class.
}
