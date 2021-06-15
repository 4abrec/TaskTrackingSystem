package web.task.track.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class LoginDto {

    @ApiModelProperty(example = "4abrec")
    private String username;
    @ApiModelProperty(example = "1234")
    private String password;
}
