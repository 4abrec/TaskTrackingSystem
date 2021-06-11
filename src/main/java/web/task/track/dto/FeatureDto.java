package web.task.track.dto;

import lombok.Data;
import java.util.Set;

@Data
public class FeatureDto {

    private String title;
    private String description;
    private Set<String> users;
}
