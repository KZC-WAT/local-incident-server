package pl.kzcwat.localincidentserver.region.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.openapitools.jackson.nullable.JsonNullable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
public class RegionModifyRequest {
    @NotNull
    @Size(min = 3)
    private JsonNullable<String> name = JsonNullable.undefined();
    private JsonNullable<Long> superRegionId = JsonNullable.undefined();
}
