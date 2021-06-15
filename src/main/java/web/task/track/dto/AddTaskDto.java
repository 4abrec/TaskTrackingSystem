package web.task.track.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AddTaskDto {

    @ApiModelProperty(example = "testTask")
    private String title;

    @ApiModelProperty(example = "test")
    private String description;

    @ApiModelProperty(example = "5abrec")
    private String username;

    @ApiModelProperty(example = "1")
    private Integer featureId;
}
