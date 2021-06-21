package web.task.track.dto;

import com.sun.istack.NotNull;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import web.task.track.domain.EStatus;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Data
public class FindTaskDto {

    @NotNull
    @ApiModelProperty(example = "testTask")
    private String title;

    @NotNull
    @ApiModelProperty(example = "6abrec")
    private String username;

    @NotNull
    @Enumerated(EnumType.STRING)
    @ApiModelProperty(example = "IN_PROGRESS")
    private EStatus status;
}
