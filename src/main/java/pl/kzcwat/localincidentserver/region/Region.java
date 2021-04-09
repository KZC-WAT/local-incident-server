package pl.kzcwat.localincidentserver.region;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import pl.kzcwat.localincidentserver.announcement.Announcement;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Data
public class Region {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @JsonIgnore
    @OneToMany(mappedBy = "region")
    private List<Announcement> announcements;

    // TODO: this is a Region class stub created to link with Announcement entity class.
}
