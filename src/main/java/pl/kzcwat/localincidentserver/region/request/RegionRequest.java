package pl.kzcwat.localincidentserver.region.request;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
public class RegionRequest {
    @NotNull
    @Size(min = 3)
    private String regionName;
    private Long superRegionId;
}
