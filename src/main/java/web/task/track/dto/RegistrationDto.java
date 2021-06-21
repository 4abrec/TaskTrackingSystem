package web.task.track.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.Set;

@Data
public class RegistrationDto {

    @ApiModelProperty(example = "4abrec")
    private String username;

    @ApiModelProperty(example = "4abrec@gmail.com")
    private String email;

    @ApiModelProperty(example = "1234")
    private String password;

    @ApiModelProperty(example = "Artyom")
    private String firstName;

    @ApiModelProperty(example = "Cherkasov")
    private String lastName;

    @ApiModelProperty(example = "['manager']")
    private Set<String> roles;

}
