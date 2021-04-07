package pl.kzcwat.localincidentserver.region.request;

import com.sun.istack.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.openapitools.jackson.nullable.JsonNullable;

@Data
@NoArgsConstructor
public class RegionModifyRequest {
    @NotNull
    private JsonNullable<String> regionName = JsonNullable.undefined();

    private JsonNullable<String> superRegionName = JsonNullable.undefined();
}
