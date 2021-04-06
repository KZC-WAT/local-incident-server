package pl.kzcwat.localincidentserver.region.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegionRequest {
    private String regionName;
    private String superRegionName;
}
