package web.task.track.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AddTaskDto {

    @ApiModelProperty(example = "testTask")
    private String title;

    @ApiModelProperty(example = "test")
    private String description;


    @ApiModelProperty(example = "1")
    private Integer featureId;

    public AddTaskDto(String title, String description, Integer featureId) {
        this.title = title;
        this.description = description;
        this.featureId = featureId;
    }
}
