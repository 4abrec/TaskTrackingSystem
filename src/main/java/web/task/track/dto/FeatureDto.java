package web.task.track.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.Set;

@Data
@AllArgsConstructor
public class FeatureDto {

    @ApiModelProperty(example = "testFeature")
    private String title;

    @ApiModelProperty(example = "test")
    private String description;

    @ApiModelProperty(example = "['4abrec', '5abrec', '6abrec']")
    private Set<String> users;
}
