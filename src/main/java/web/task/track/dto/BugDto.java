package web.task.track.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BugDto {

    @ApiModelProperty(example = "testBug")
    private String title;

    @ApiModelProperty(example = "test")
    private String description;


    @ApiModelProperty(example = "1")
    private Integer taskId;


}
